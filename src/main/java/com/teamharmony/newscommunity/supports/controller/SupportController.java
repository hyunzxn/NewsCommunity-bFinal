package com.teamharmony.newscommunity.supports.controller;

import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import com.teamharmony.newscommunity.supports.dto.SupportResponseDto;
import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.supports.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class SupportController {
    private final SupportService supportService;

    //bean이면 서비스에서 주입이 가능한데, bean이 아니면 불가해서 controllerd에서 서비스로 넘겨줘야 한다.
    //생성
    @PostMapping("/user/supports")
    public Support createSupport(@RequestBody SupportRequestDto requestDto, @AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return supportService.generateSupport(requestDto, username);
    }

    //조회
    @GetMapping("/supports")
    public List<SupportResponseDto> readSupport() {
        return supportService.getSupportsList();
    }

    //내가 작성한 글만 조회하기
    @GetMapping("/user/supports/mine")
    public List<SupportResponseDto> getMySupportList(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return supportService.getMySupportList(username);
    }
    //수정
    @PutMapping("/user/supports/{content_id}")
    public String modifySupportContent(@PathVariable Long content_id, @RequestBody SupportRequestUpdateDto requestedDto, @AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return supportService.update(content_id, requestedDto, username);
    }

    //삭제
    @DeleteMapping("/user/supports/{content_id}")
    public String deleteSupport(@PathVariable Long content_id, @AuthenticationPrincipal UserDetails user) {
        return supportService.removeContent(content_id, user);
    }
}