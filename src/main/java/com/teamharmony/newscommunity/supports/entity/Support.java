package com.teamharmony.newscommunity.supports.entity;

import com.teamharmony.newscommunity.supports.dto.SupportRequestDto;
import com.teamharmony.newscommunity.supports.dto.SupportRequestUpdateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Support extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String post_title;

    @Column(nullable = false)
    private String post_content;


    public Support(String username, String post_title, String post_content) {
        this.username = username;
        this.post_title = post_title;
        this.post_content = post_content;
    }

    public Support(SupportRequestDto requestedDto) {
        this.username = requestedDto.getUsername();
        this.post_title = requestedDto.getPost_title();
        this.post_content = requestedDto.getPost_content();
    }

    public void update(SupportRequestUpdateDto requestDto){
        this.post_content = requestDto.getPost_content();
    }
}
