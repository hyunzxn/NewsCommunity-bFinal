package com.teamharmony.newscommunity.supports.controller;

import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import com.teamharmony.newscommunity.supports.dto.SupportResponseDto;
import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.supports.repository.SupportRepository;
import com.teamharmony.newscommunity.supports.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/")
public class SupportController {
    private final SupportService supportService;
    private final SupportRepository supportRepository;

    //생성
    @PostMapping("/supports")
    @ResponseBody
    public Support createSupport(@RequestBody SupportRequestDto requestedDto, @AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return supportService.generateSupport(requestedDto, username);
    }


    //조회
    @GetMapping("/supports")
    @ResponseBody
    public List<SupportResponseDto> readSupport() {
        return supportService.getSupportsList();
    }

    //내가 작성한 글만 조회하기
    @GetMapping("/supports/me")
    @ResponseBody
    public List<SupportResponseDto> getMySupports(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return supportService.getSupportsListWrittenByMe(username);
    }
    //수정
    @PutMapping("/supports/{content_id}")
    @ResponseBody
    public Long modifySupportContent(@PathVariable Long content_id, @RequestBody SupportRequestUpdateDto requestedDto) {
        supportService.update(content_id, requestedDto);
        return content_id;
    }

    //삭제
    @DeleteMapping("/supports/{content_id}")
    @ResponseBody
    public long deleteSupport(@PathVariable Long content_id) {
        supportService.removeContent(content_id);
        return content_id;
    }
}