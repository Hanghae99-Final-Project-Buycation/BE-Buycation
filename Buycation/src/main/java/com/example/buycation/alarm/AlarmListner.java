package com.example.buycation.alarm;


import com.example.buycation.alarm.dto.RealtimeAlarmDto;
import com.example.buycation.alarm.entity.AlarmType;
import com.example.buycation.alarm.mapper.AlarmMapper;
import com.example.buycation.alarm.service.AlarmService;
import com.example.buycation.members.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AlarmListner {
    private final AlarmService alarmService;

    @TransactionalEventListener
    @Async
    public void handleNotification(RealtimeAlarmDto alarmDto) {
        alarmService.createAlarm(alarmDto);
    }

}
