package com.teamharmony.newscommunity.supports.service;

import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import com.teamharmony.newscommunity.supports.dto.SupportResponseDto;
import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.supports.repository.SupportRepository;
import com.teamharmony.newscommunity.users.entity.User;
import com.teamharmony.newscommunity.users.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {
    private final SupportRepository supportRepository;
    private final UserRepository userRepository;
    //생성
    @Transactional
    public Support generateSupport(SupportRequestDto requestedDto, String username){
        Support support = new Support(requestedDto, username);
        User user = userRepository.findByUsername(username);
        user.addSupports(support);
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
                    .id(supportItems.getSupport_id())
                    .build();
            resultList.add(supportResponseDto);
        }
        return resultList;
    }

    // 내가 작성한 것만 찾기
    public List<SupportResponseDto> getSupportsListWrittenByMe(String username) {
        List<Support> supportList = supportRepository.findAllByUsername(username);
        List<SupportResponseDto> resultList = new LinkedList<>();
        for (Support supportItems : supportList) {
            SupportResponseDto supportResponseDto = SupportResponseDto.builder()
                    .username(supportItems.getUsername())
                    .post_title(supportItems.getPost_title())
                    .post_content(supportItems.getPost_content())
                    .created_at(supportItems.getCreatedAt())
                    .modified_at(supportItems.getModifiedAt())
                    .id(supportItems.getSupport_id())
                    .build();
            resultList.add(supportResponseDto);
        }
        return resultList;
    }

    //삭제
    public Long removeContent(Long contentId) {
        supportRepository.deleteById(contentId);
        return contentId;
    }

    //수정
    @Transactional
    public Long update(Long support_id, SupportRequestUpdateDto requestUpdateDto) {
        Support supportObject = supportRepository.findById(support_id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );
        supportObject.setPost_content(requestUpdateDto.getPost_content());
        supportObject.update(requestUpdateDto);
        return supportObject.getSupport_id();
    }
}