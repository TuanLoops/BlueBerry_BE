package com.blueberry.controller;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.ChatRoom;
import com.blueberry.model.app.Message;
import com.blueberry.model.dto.ChatRoomDTO;
import com.blueberry.model.dto.MessageDTO;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.service.AppUserService;
import com.blueberry.service.ChatService;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/api/chat")
@CrossOrigin("*")
@AllArgsConstructor
public class ChatController {
    private ChatService chatService;
    private AppUserService appUserService;
    private ModelMapperUtil modelMapperUtil;

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRoomByUserId() {
        List<ChatRoom> chatRooms = chatService.getAllChatRoomByUserId(appUserService.getCurrentAppUser().getId());
        return new ResponseEntity<>(modelMapperUtil.mapList(chatRooms, ChatRoomDTO.class),
                HttpStatus.OK);
    }

    @PostMapping("/sendMessage/{chatRoomId}")
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable String chatRoomId, @RequestBody MessageResponse response) {
        return new ResponseEntity<>(modelMapperUtil.map(chatService.sendMessage(chatRoomId, response.getMessage()),
                MessageDTO.class), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> createNewEmptyChatRoomWithUser(@PathVariable Long userId) {
        AppUser appUser = appUserService.findById(userId).orElse(null);
        if (appUser == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(chatService.createNewEmptyChatRoomWithUser(appUser), HttpStatus.OK);
    }
}
