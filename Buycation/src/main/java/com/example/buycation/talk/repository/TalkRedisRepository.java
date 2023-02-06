package com.example.buycation.talk.repository;


import com.example.buycation.talk.dto.TalkRedisDto;
import com.example.buycation.talk.dto.TalkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class TalkRedisRepository {

    private static final String TALK_MESSAGES = "TALK_MESSAGES";

    private final RedisTemplate<String, Object> redisTemplate;

    //레디스 해쉬에 저장할떄 활용 : 채팅 메세지
    private HashOperations<String, String, List<TalkRedisDto>> opsHashChatMsg;


    @PostConstruct
    private void init() {

        opsHashChatMsg = redisTemplate.opsForHash();
    }

    public List<TalkRedisDto> findMsgByRoomId(Long roomId) {

        List<TalkRedisDto> talkRedisDtos;

        talkRedisDtos = opsHashChatMsg.get(TALK_MESSAGES, "TALK" + roomId.toString());

        if (talkRedisDtos == null) talkRedisDtos = new ArrayList<>();

        return talkRedisDtos;
    }


    @Transactional
    public List<TalkRedisDto> findAllMsgs() {
        Set<String> keys = opsHashChatMsg.keys(TALK_MESSAGES);
        List<TalkRedisDto> talkRedisDtos = new ArrayList<>();
        keys.stream().forEach(key -> {
            talkRedisDtos.addAll(opsHashChatMsg.get(TALK_MESSAGES, key));
        });
        return talkRedisDtos;
    }

    // 채팅방 메세지 생성 : 채팅방마다의 메세지를 redis hash에 저장한다.
    public void addMessageInRedis(Long roomId, TalkRedisDto talkRedisDto) {
        //채팅 메세지를 빌더 패턴으로 만들고,
        TalkResponseDto talkResponseDto = TalkResponseDto.builder()
                .talkRoomId(roomId)
                .memberId(talkRedisDto.getId())
                .sender(talkRedisDto.getSender())
                .message(talkRedisDto.getMessage())
                .sendDate(talkRedisDto.getSendDate().format(DateTimeFormatter.ofPattern("MM월 dd일 HH:mm")))
                .build();

        List<TalkRedisDto> chatMessages = opsHashChatMsg.get(TALK_MESSAGES, "TALK" + roomId.toString());

        if(chatMessages == null){
            chatMessages = new ArrayList<>();
            opsHashChatMsg.put(TALK_MESSAGES, "TALK" + roomId.toString(), chatMessages);
        }

        chatMessages.add(talkRedisDto);
        opsHashChatMsg.put(TALK_MESSAGES, "TALK" + roomId.toString(), chatMessages);

    }

    public void deleteMessageInRedis(){
        Set<String> keys = opsHashChatMsg.keys(TALK_MESSAGES);
        keys.stream().forEach(key -> {
            opsHashChatMsg.delete(TALK_MESSAGES, key);
        });
    }
}
