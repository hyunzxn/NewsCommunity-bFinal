package com.teamharmony.newscommunity.supports.service;

import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import com.teamharmony.newscommunity.supports.dto.SupportResponseDto;
import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.supports.repository.SupportRepository;
import com.teamharmony.newscommunity.users.entity.User;
import com.teamharmony.newscommunity.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
    public Support generateSupport(SupportRequestDto requestDto, String username){
        Support support = new Support(requestDto, username);
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
    public List<SupportResponseDto> getMySupportList(String username) {
        List<Support> supportList = supportRepository.findAllByUsernameOrderByCreatedAtDesc(username);
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
    public String removeContent(Long contentId, UserDetails user) {
        Support supportObject = supportRepository.findById(contentId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        Long supportsUserID = supportObject.getUser().getId(); //현재 게시글에서 userID정보획득
        String username = user.getUsername(); //현재 로그인한 사람 이름가져오기
        User currentUser = userRepository.findByUsername(username); // 현재 로그인한 사람 이름(unigue)으로 user정보 획득
        Long loginUserId = currentUser.getId();// 로그인한 사용자ID 정보(Long) 획득

        if (supportsUserID == loginUserId){ //글 쓴 사람의 Id번호와 지금 로그인한 사람의 ID 번호 동일
            supportRepository.deleteById(contentId);
        }
        String result = "User: "+username+", ContentNumber: "+contentId;
        return result;
    }

    //수정
    @Transactional
    public String update(Long support_id, SupportRequestUpdateDto requestUpdateDto, String username) {
        Support supportObject = supportRepository.findById(support_id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Long supportsUserID = supportObject.getUser().getId(); //현재 게시글에서 userID정보획득
        User currentUser = userRepository.findByUsername(username); // 현재 로그인한 사람 이름(unigue)으로 user정보 획득
        Long loginUserId = currentUser.getId();// 로그인한 사용자ID 정보(Long) 획득

        if (supportsUserID == loginUserId){ //글 쓴 사람의 Id번호와 지금 로그인한 사람의 ID 번호 동일
            supportObject.setPost_content(requestUpdateDto.getPost_content());
            supportObject.update(requestUpdateDto);
        }
        String result = "supportsUserID "+supportsUserID+", loginUserId "+loginUserId;
        return result;
    }
}