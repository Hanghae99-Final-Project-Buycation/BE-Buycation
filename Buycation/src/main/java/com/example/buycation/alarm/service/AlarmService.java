package com.example.buycation.alarm.service;

import com.example.buycation.alarm.dto.AlarmResponseDto;
import com.example.buycation.alarm.entity.Alarm;
import com.example.buycation.alarm.entity.AlarmType;
import com.example.buycation.alarm.mapper.AlarmMapper;
import com.example.buycation.alarm.repository.AlarmRepository;
import com.example.buycation.alarm.repository.EmitterRepository;
import com.example.buycation.common.PageConfig.PageRequest;
import com.example.buycation.common.PageConfig.PageResponse;
import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    public SseEmitter subscribe(UserDetailsImpl userDetails, String lastEventId) throws IOException{
        Member member = userDetails.getMember();
        Long memberId = member.getId();
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        try {
            emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
            emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

            Long count = alarmRepository.countByIsReadFalseAndMember(member);
            sendAlarm(emitter, memberId + "_" + System.currentTimeMillis(), emitterId, count);

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
                            sendAlarm(emitter, lastEventId, entry.getKey(), entry.getValue());
                        }
                );
    }

    public void sendAlarm(SseEmitter sseEmitter,  String eventId, String emitterId, Object data){
        try {
            sseEmitter.send(SseEmitter.event().id(eventId).data(data));
        }catch(IOException | IllegalStateException exception){
            emitterRepository.deleteById(emitterId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createAlarm(Member member, AlarmType alarmType, Long postingId, String title){

        Alarm alarm = new Alarm(postingId, title, alarmType.getMessage(), false, alarmType, member);
        alarmRepository.save(alarm);

        String id = String.valueOf(member.getId());
        String eventId = id + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, alarm);
                    sendAlarm(emitter, eventId, key, alarmMapper.toRealtimeAlarmDto(alarm));
                }
        );
    }

    @Transactional
    public List<AlarmResponseDto> getAlarms(UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        List<Alarm> alarms = alarmRepository.findAllByMemberOrderByMemberDesc(member);
        return alarms.stream().map(alarm -> alarmMapper.toAlarmResponseDto(alarm)).toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<AlarmResponseDto> getAlarmsPaging(UserDetailsImpl userDetails, PageRequest pageRequest) {

        List<Alarm> alarms;
        Long nextKey;

        Member member = userDetails.getMember();

        if(pageRequest.hasKey(pageRequest.getKey())){
            alarms= alarmRepository.findTop15ByIdLessThanAndMemberOrderByIdDesc(pageRequest.getKey(), member);
        }else{
            alarms= alarmRepository.findTop15ByMemberOrderByIdDesc(member);
        }
        nextKey = alarms.stream().mapToLong(Alarm::getId).min().orElse(PageRequest.NONE_KEY);
        return new PageResponse<>(pageRequest.next(nextKey)
                                , alarms.stream().map(alarm -> alarmMapper.toAlarmResponseDto(alarm)).toList());
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

    @Transactional
    public Long getAlarmCount(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Long count = alarmRepository.countByIsReadFalseAndMember(member);
        return count;
    }


}
