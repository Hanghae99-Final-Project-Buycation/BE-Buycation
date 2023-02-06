package com.example.buycation.scheduler;

import com.example.buycation.alarm.dto.RealtimeAlarmDto;
import com.example.buycation.alarm.entity.AlarmType;
import com.example.buycation.alarm.repository.AlarmRepository;
import com.example.buycation.alarm.service.AlarmService;
import com.example.buycation.comment.entity.Comment;
import com.example.buycation.comment.repository.CommentRepository;
import com.example.buycation.common.exception.CustomException;
import com.example.buycation.common.exception.ErrorCode;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.participant.entity.Participant;
import com.example.buycation.participant.repository.ApplicationRepository;
import com.example.buycation.participant.repository.ParticipantRepository;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.repository.PostingRepository;
import com.example.buycation.talk.dto.TalkRedisDto;
import com.example.buycation.talk.dto.TalkResponseDto;
import com.example.buycation.talk.entity.ChatRoom;
import com.example.buycation.talk.entity.Talk;
import com.example.buycation.talk.repository.ChatRoomRepository;
import com.example.buycation.talk.repository.TalkJdbcRepository;
import com.example.buycation.talk.repository.TalkRedisRepository;
import com.example.buycation.talk.repository.TalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.buycation.common.exception.ErrorCode.TALKROOM_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;
    private final ApplicationRepository applicationRepository;
    private final ParticipantRepository participantRepository;
    private final AlarmRepository alarmRepository;

    private final ChatRoomRepository chatRoomRepository;
    private final TalkRepository talkRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final TalkRedisRepository talkRedisRepository;

    private final TalkJdbcRepository talkJdbcRepository;

    // 초, 분, 시, 일, 월, 주 업데이트 순서
    @Scheduled(cron = "0 0/10 * * * *")
    @Transactional
    public void updatePostings() {
        System.out.println("게시글 업데이트 시작");

        //현재 날짜, 시간 값 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now();
        //현재 시간과 같거나 그 이하 값 전부조회
        List<Posting> postingList = postingRepository.findUpdateData(currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), false);
        List<ChatRoom> chatRoomDeleteList = new ArrayList<>();
        List<Posting> postingDeleteList = new ArrayList<>();

        for (Posting p : postingList) {
            //멤버가 다모인 상태일 경우 완료
            if (p.getTotalMembers() == p.getCurrentMembers()) {
                p.finish(true);

                //알림보내기
                p.getParticipantList().stream().forEach(participant -> {
                    try {
                        applicationEventPublisher.publishEvent(RealtimeAlarmDto.builder()
                                .postingId(p.getId())
                                .alarmType(AlarmType.DONE)
                                .member(participant.getMember())
                                .message(AlarmType.DONE.getMessage())
                                .title(p.getTitle()).build());

                    } catch(Exception e){
                        System.out.println(ErrorCode.ALARM_NOT_FOUND);
                    }

                });

            //멤버가 안모였으면 삭제
            } else {
                //알림보내기
                p.getParticipantList().stream().forEach(participant -> {
                    try {
                        applicationEventPublisher.publishEvent(RealtimeAlarmDto.builder()
                                .postingId(p.getId())
                                .alarmType(AlarmType.FAIL)
                                .member(participant.getMember())
                                .message(AlarmType.FAIL.getMessage())
                                .title(p.getTitle()).build());

                    } catch(Exception e){
                        System.out.println(ErrorCode.ALARM_NOT_FOUND);
                    }
                });

                //미리 연관 데이터 삭제
                List<Comment> comments = commentRepository.findAllByPosting(p);
                if (!comments.isEmpty()) commentRepository.deleteAllByInQuery(comments);
                List<Application> applications = applicationRepository.findAllByPosting(p);
                if (!applications.isEmpty()) applicationRepository.deleteAllByInQuery(applications);
                List<Participant> participants = participantRepository.findAllByPosting(p);
                if (!participants.isEmpty()) participantRepository.deleteAllByInQuery(participants);

                ChatRoom chatRoom = chatRoomRepository.findByPosting(p).orElseThrow(() -> new CustomException(TALKROOM_NOT_FOUND));

                List<Talk> talks = talkRepository.findAllByChatRoom(chatRoom);
                if (!talks.isEmpty()) talkRepository.deleteAllByInQuery(talks);

                //삭제목록 저장(쿼리 한번으로 삭제 시키기위함)
                chatRoomDeleteList.add(chatRoom);
                postingDeleteList.add(p);
            }
        }
        //한번에 삭제
        if (!chatRoomDeleteList.isEmpty()) chatRoomRepository.deleteAllByIdInQuery(chatRoomDeleteList);
        if (!postingDeleteList.isEmpty()) postingRepository.deleteAllByIdInQuery(postingDeleteList);

        System.out.println("게시글 업데이트 종료");
    }

    @Scheduled(cron = "0 0 5 * * *")
    @Transactional
    public void dueDatePostingChatRoomDelete() {
        System.out.println("완료된 게시글 채팅룸 삭제 시작");

        //현재 날짜, 시간 값 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now();
        //현재 시간과 같거나 그 이하 값 전부조회
        List<Posting> postingList = postingRepository.findUpdateData(currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), true);
        List<ChatRoom> chatRoomDeleteList = new ArrayList<>();

        for (Posting p : postingList){
            if (chatRoomRepository.findByPosting(p).isPresent()) {
                ChatRoom chatRoom = chatRoomRepository.findByPosting(p).orElseThrow(() -> new CustomException(TALKROOM_NOT_FOUND));

                List<Talk> talks = talkRepository.findAllByChatRoom(chatRoom);
                if (!talks.isEmpty()) talkRepository.deleteAllByInQuery(talks);
                //삭제목록 저장(쿼리 한번으로 삭제 시키기위함)
                chatRoomDeleteList.add(chatRoom);
            }
        }
        //한번에 삭제
        if (!chatRoomDeleteList.isEmpty()) chatRoomRepository.deleteAllByIdInQuery(chatRoomDeleteList);

        System.out.println("완료된 게시글 채팅룸 삭제 종료");
    }

    @Scheduled(cron = "0 0/5 * * * *")
    @Transactional
    public void alarm60minutesBefore() {
        System.out.println("마감 60분 전 게시글 알림 시작");

        List<Posting> postingList = postingRepository.findUpdateData(LocalDateTime.now().plusMinutes(60).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                                                                    , false);
        for (Posting posting : postingList) {
            posting.getParticipantList().stream().forEach(participant -> {
                try {
                    applicationEventPublisher.publishEvent(RealtimeAlarmDto.builder()
                            .postingId(posting.getId())
                            .alarmType(AlarmType.REMIND)
                            .member(participant.getMember())
                            .message(AlarmType.REMIND.getMessage())
                            .title(posting.getTitle()).build());

                } catch(Exception e){
                    System.out.println(ErrorCode.ALARM_NOT_FOUND);
                }
            });
        }

        System.out.println("마감 60분 전 게시글 알림 종료");
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void deleteOldAlarmAfterAMonth() {
        System.out.println("30일 지난 알림 데이타 삭제 시작");
        alarmRepository.deleteAlarmByDueDateBeforeAMonth(LocalDateTime.now().minusMonths(1));
        System.out.println("30일 지난 알림 데이타 삭제 종료");
    }



    // 1시간 마다 Chatting Data Redis -> MySQL 에 저장
    @Scheduled(cron = "0 0/10 * * * *")
    @Transactional
    public void writeBack() {
        System.out.println("채팅 redis내용 MySQL저장 시작");
        List<TalkRedisDto> talkRedisDtos = talkRedisRepository.findAllMsgs();
        System.out.println("schedule controller talkRedisDto suze " + talkRedisDtos.size());
        talkJdbcRepository.batchInsert(talkRedisDtos);
        talkRedisRepository.deleteMessageInRedis();
        System.out.println("채팅 redis내용 MySQL저장 종료");
    }

}
