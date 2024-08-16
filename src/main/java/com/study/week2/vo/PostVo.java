package com.study.week2.vo;

import com.study.week2.dto.PostDto;
import lombok.*;

@Builder
@Value
//객체 외부 생성 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class PostVo {

    private int postId;

    private int categoryId;

    private String categoryName;

    private String name;

    private String password;

    private String title;

    private String content;

    private int viewCount;

    private String createDate;

    private String updateDate;

    private int status;

    private int fileCount;

    /**
     * dto를 vo로 변환
     * 화면에서 받아서 db로 넣을때
     * @param postDto
     * @return postVo
     */

    public static PostVo toVo(PostDto postDto){
        PostVo postVo = PostVo.builder()
                .postId(postDto.getPostId())
                .categoryId(postDto.getCategoryId())
                .name(postDto.getName())
                .password(postDto.getPassword())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();
        return postVo;
    }


}
