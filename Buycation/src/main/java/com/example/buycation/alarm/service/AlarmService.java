package com.example.buycation.alarm.service;

import com.example.buycation.alarm.dto.AlarmResponseDto;
import com.example.buycation.alarm.entity.Alarm;
import com.example.buycation.alarm.entity.AlarmType;
import com.example.buycation.alarm.mapper.AlarmMapper;
import com.example.buycation.alarm.repository.AlarmRepository;
import com.example.buycation.alarm.repository.EmitterRepository;
import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.member.repository.MemberRepository;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.buycation.common.exception.ErrorCode.ALARM_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.SUBSCRIBE_FAIL;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private static final Long DEFAULT_TIMEOUT =60 * 60 * 1000L;
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmMapper alarmMapper;

    private final MemberRepository memberRepository;

    public SseEmitter subscribe(Long memberId, String lastEventId) throws IOException{
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        try {
            emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
            emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
            sendAlarm(emitter, emitterId, "connect check : " + emitterId);

            if(!lastEventId.isEmpty()){
                sendLostAlarm(emitter, memberId, lastEventId);
            }

        }catch(Exception e){
            throw new CustomException(SUBSCRIBE_FAIL);
        }
        return emitter;
    }

    public void sendLostAlarm(SseEmitter emitter, Long memberId, String lastEventId){
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartsWithId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(
                        entry -> lastEventId.compareTo(entry.getKey()) < 0
                )
                .forEach(
                        (entry) -> {
                            sendAlarm(emitter, entry.getKey(), entry.getValue());
                            System.out.println("entry.getKey() :: " + entry.getKey());
                        }
                );
    }

    public void sendAlarm(SseEmitter sseEmitter, String emitterId, Object data){
        try {
            sseEmitter.send(SseEmitter.event().id(emitterId).data(data));
        }catch(IOException | IllegalStateException exception){
            emitterRepository.deleteById(emitterId);
        }
    }

    @Transactional
    public void createAlarm(Member member, AlarmType alarmType, Long postingId, String content){
        System.out.println("알림 content => " + content);
        Alarm alarm = new Alarm(postingId, content, false, alarmType, member);
        alarmRepository.save(alarm);

        String id = String.valueOf(member.getId());
        //해당 유저id로 시작하는 SseEmitter 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, alarm);//캐쉬에 이벤트 저장
                    System.out.println("sseEmitters key :: " + key);
                    //클라이언트에 알람 전송
                    sendAlarm(emitter, key, alarmMapper.toAlarmResponseDto(alarm));
                }
        );
    }

    @Transactional
    public List<AlarmResponseDto> getAlarms(UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        List<Alarm> alarms = alarmRepository.findAllByMemberOrderByIdDesc(member);
        return alarms.stream().map(alarm -> alarmMapper.toAlarmResponseDto(alarm)).toList();
    }

    @Transactional
    public String readAlarm(Long alarmId){
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                ()->new CustomException(ALARM_NOT_FOUND)
        );
        alarm.read();
        return "read complete " + alarmId;
    }

    @Transactional
    public void deleteAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                ()->new CustomException(ALARM_NOT_FOUND)
        );
        alarmRepository.deleteById(alarmId);
    }

    @Transactional
    public void deleteAllAlarms(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        alarmRepository.deleteAllByMember(member);
    }

    public Long getAlarmCount(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Long count = alarmRepository.countByReadFalseAndMember(member);
        return count;
    }
}
