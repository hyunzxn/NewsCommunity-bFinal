package com.teamharmony.newscommunity.supports.controller;

import com.teamharmony.newscommunity.comments.dto.CommentResponseDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import com.teamharmony.newscommunity.supports.dto.SupportResponseDto;
import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.supports.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class SupportController {
    private final SupportService supportService;

    //bean이면 서비스에서 주입이 가능한데, bean이 아니면 불가해서 controllerd에서 서비스로 넘겨줘야 한다.
    //생성
    @PostMapping("/user/supports")
    public Support createSupport(@RequestBody @Valid SupportRequestDto requestDto, @AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return supportService.generateSupport(requestDto, username);
    }

//    //조회
//    @GetMapping("/supports")
//    public List<SupportResponseDto> readSupport() {
//        return supportService.getSupportsList();
//    }
    //조회
    @GetMapping("/supports")
    public ResponseEntity<List<SupportResponseDto>> readSupport() {
        List<SupportResponseDto> supportList = supportService.getSupportsList();
        return ResponseEntity.ok().body(supportList);
    }

    //내가 작성한 글만 조회하기
    @GetMapping("/user/supports/mine")
    public ResponseEntity<List<SupportResponseDto>> getMySupportList(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        List<SupportResponseDto> supportList = supportService.getMySupportList(username);
        return ResponseEntity.ok().body(supportList);
    }
    //수정
    @PutMapping("/user/supports/{content_id}")
    public ResponseEntity<Long> modifySupportContent(@PathVariable Long content_id, @RequestBody SupportRequestUpdateDto requestedDto, @AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        Long modifyContentId = supportService.update(content_id, requestedDto, username);
        return ResponseEntity.ok().body(modifyContentId);
    }

    //삭제
    @DeleteMapping("/user/supports/{content_id}")
    public ResponseEntity<Long> deleteSupport(@PathVariable Long content_id, @AuthenticationPrincipal UserDetails user) {
        Long deleteContentId = supportService.removeContent(content_id, user);
        return ResponseEntity.ok().body(deleteContentId);
    }
}