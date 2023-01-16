package com.example.buycation.scheduler;

import com.example.buycation.comment.entity.Comment;
import com.example.buycation.comment.repository.CommentRepository;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.participant.entity.Participant;
import com.example.buycation.participant.repository.ApplicationRepository;
import com.example.buycation.participant.repository.ParticipantRepository;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;
    private final ApplicationRepository applicationRepository;
    private final ParticipantRepository participantRepository;

    // 초, 분, 시, 일, 월, 주 업데이트 순서
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updatePrice() throws InterruptedException {
        System.out.println("게시글 업데이트 시작");

        //현재 날짜, 시간 값 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now();
        //현재 시간과 같거나 그 이하 값 전부조회
        List<Posting> postingList = postingRepository.findUpdateData(currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        List<Long> postingDeleteList = new ArrayList<>();

        for (Posting p : postingList) {
            //멤버가 다모인 상태일 경우 완료
            if (p.getTotalMembers() == p.getCurrentMembers()) {
                p.finish(true);
            //멤버가 안모였으면 삭제
            } else {
                //게시글 삭제목록 저장(쿼리 한번으로 삭제 시키기위함)
                postingDeleteList.add(p.getId());

                //미리 연관 데이터 삭제
                List<Comment> comments = commentRepository.findAllByPosting(p);
                if (!comments.isEmpty()) commentRepository.deleteAllByInQuery(comments);
                List<Application> applications = applicationRepository.findAllByPosting(p);
                if (!applications.isEmpty()) applicationRepository.deleteAllByInQuery(applications);
                List<Participant> participants = participantRepository.findAllByPosting(p);
                if (!participants.isEmpty()) participantRepository.deleteAllByInQuery(participants);
            }
        }
        //한번에 삭제
        if (!postingDeleteList.isEmpty()) postingRepository.deleteAllByIdInQuery(postingDeleteList);

        System.out.println("게시글 업데이트 종료");
    }
}
