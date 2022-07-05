package com.teamharmony.newscommunity.supports.service;

import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import com.teamharmony.newscommunity.supports.dto.SupportResponseDto;


import java.util.List;

public interface SupportServiceImpl {
    List<SupportResponseDto> getSupportList();
    long saveSupport(SupportRequestDto supportRequestDto);
    Long update(Long id, SupportRequestUpdateDto requestUpdateDto);
    Long removeContent(Long id);
}