package com.example.buycation.talk.controller;


import com.example.buycation.common.PageConfig.PageRequest;
import com.example.buycation.common.ResponseMessage;
import com.example.buycation.security.UserDetailsImpl;
import com.example.buycation.talk.dto.TalkEntryResponseDto;
import com.example.buycation.talk.dto.TalkResponseDto;
import com.example.buycation.talk.dto.ChatRoomResponseDto;
import com.example.buycation.talk.service.TalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.buycation.common.MessageCode.CHATROOM_SEARCH_SUCCESS;
import static com.example.buycation.common.MessageCode.TALK_SEARCH_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talk")
public class ChatRoomController {
    private final TalkService talkService;

    @GetMapping("/room")
    public ResponseMessage<List<ChatRoomResponseDto>> roomList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<ChatRoomResponseDto> chatRoomList = talkService.findAllRoom(userDetails);
        return new ResponseMessage<List<ChatRoomResponseDto>>(CHATROOM_SEARCH_SUCCESS, chatRoomList);
    }

    @GetMapping("/room/{roomId}")
    public ResponseMessage<?> joinChatRoom(@PathVariable Long roomId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestParam(value = "key" , required = false) Long key){
        //TalkEntryResponseDto talkList = talkService.findAllMessageByTalkRoomId(roomId, userDetails, new PageRequest(key));
        TalkEntryResponseDto talkList = talkService.findAllMessageFromRedis(roomId, userDetails, new PageRequest(key));
        System.out.println("talkList size " + talkList.getTalks().size());
        return new ResponseMessage<TalkEntryResponseDto>(TALK_SEARCH_SUCCESS, talkList);
    }

}
