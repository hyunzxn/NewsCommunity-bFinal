package com.teamharmony.newscommunity.supports.controller;

import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import com.teamharmony.newscommunity.supports.dto.SupportResponseDto;
import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.supports.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/")
public class SupportController {
    private final SupportService supportService;

    //생성
    @PostMapping("/supports")
    @ResponseBody
    public Support createSupport(@RequestBody SupportRequestDto requestedDto) {
        return supportService.generateSupport(requestedDto);
    }

    //조회 - try
    @GetMapping("/supports")
    @ResponseBody
    public List<SupportResponseDto> readSupport() {
        return supportService.getSupportsList();
    }

    //수정
    @PutMapping("/supports/{id}")
    @ResponseBody
    public Long modifySupportContent(@PathVariable Long id, @RequestBody SupportRequestUpdateDto requestedDto) {
        supportService.update(id, requestedDto);
        return id;
    }

    //삭제
    @DeleteMapping("/supports/{id}")
    @ResponseBody
    public long deleteSupport(@PathVariable Long id) {
        supportService.removeContent(id);
        return id;
    }
}