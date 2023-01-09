package com.example.buycation.alarm.service;

import com.example.buycation.alarm.dto.AlarmResponseDto;
import com.example.buycation.alarm.entity.Alarm;
import com.example.buycation.alarm.entity.AlarmType;
import com.example.buycation.alarm.mapper.AlarmMapper;
import com.example.buycation.alarm.repository.AlarmRepository;
import com.example.buycation.alarm.repository.EmitterRepository;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private static final Long DEFAULT_TIMEOUT =60 * 60 * 1000L;
    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmMapper alarmMapper;
    public SseEmitter subscribe(Long memberId) throws IOException{
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        sendToClient(emitter, emitterId, "test data ==> " + emitterId);
        return emitter;
    }

    public void sendToClient(SseEmitter sseEmitter, String emitterId, Object data){
        try {
            sseEmitter.send(SseEmitter.event().id(emitterId).data(data));
        }catch(IOException | IllegalStateException exception){
            emitterRepository.deleteById(emitterId);
        }
    }

    public void send(Member member, AlarmType alarmType, String content){
        System.out.println("알림 content => " + content);
        Alarm alarm = new Alarm(content, false, alarmType, member);
        alarmRepository.save(alarm);

        String id = String.valueOf(member.getId());
        //해당 유저id로 시작하는 SseEmitter 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, alarm);//캐쉬에 이벤트 저장
                    //클라이언트에 알람 전송
                    sendToClient(emitter, key, alarmMapper.toAlarmResponseDto(alarm));
                }
        );
    }
}
