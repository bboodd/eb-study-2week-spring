package com.study.week2.vo;

import com.study.week2.dto.PostDto;
import com.study.week2.dto.request.PostRequestDto;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Value
//객체 외부 생성 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class PostVo {

    private int postId;                     //pk
    private int categoryId;                 //카테고리
    private String writer;                  //작성자
    private String password;                //비밀번호
    private String title;                   //제목
    private String content;                 //내용
    private LocalDateTime createDate;       //생성일시
    private LocalDateTime updateDate;       //최종수정일시
    private int status;                     //삭제 0 미삭제 1
    private int viewCount;                  //조회수

    private int fileCount;                  //서브쿼리 - 파일개수

    private String categoryName;            //조인 - 카테고리이름



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
                .writer(postDto.getName())
                .password(postDto.getPassword())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();
        return postVo;
    }

    public static PostVo toVo(PostRequestDto postRequestDto){
        PostVo postVo = PostVo.builder()
                .postId(postRequestDto.getPostId())
                .categoryId(postRequestDto.getCategoryId())
                .writer(postRequestDto.getWriter())
                .password(postRequestDto.getPassword())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .build();
        return postVo;
    }


}
