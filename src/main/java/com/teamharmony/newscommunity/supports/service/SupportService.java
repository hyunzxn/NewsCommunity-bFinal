package com.teamharmony.newscommunity.supports.service;


import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import com.teamharmony.newscommunity.supports.dto.SupportResponseDto;
import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.supports.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {
    private final SupportRepository supportRepository;

    //생성
    @Transactional
    public Support generateSupport(SupportRequestDto requestedDto){
        Support support = new Support(requestedDto); // to dto -> to entity
        supportRepository.save(support);
        return support;
    }

    //조회
    public List<SupportResponseDto> getSupportsList() {
        List<Support> supportList = supportRepository.findAllByOrderByCreatedAtDesc();
        List<SupportResponseDto> resultList = new LinkedList<>();
        for (Support supportItems : supportList) {
            SupportResponseDto supportResponseDto = SupportResponseDto.builder()
                    .username(supportItems.getUsername())
                    .post_title(supportItems.getPost_title())
                    .post_content(supportItems.getPost_content())
                    .created_at(supportItems.getCreatedAt())
                    .modified_at(supportItems.getModifiedAt())
                    .id(supportItems.getId())
                    .build();
            resultList.add(supportResponseDto);
        }
        return resultList;
    }

    //삭제
    public Long removeContent(Long id) {
        supportRepository.deleteById(id);
        return id;
    }

    //수정
    @Transactional
    public Long update(Long id, SupportRequestUpdateDto requestUpdateDto) {
        Support supportObject = supportRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );
        supportObject.setPost_content(requestUpdateDto.getPost_content());
        supportObject.update(requestUpdateDto);
        return supportObject.getId();
    }
}