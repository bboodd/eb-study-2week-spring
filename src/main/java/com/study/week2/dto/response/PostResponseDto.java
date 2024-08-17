package com.study.week2.dto.response;

import com.study.week2.vo.PostVo;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class PostResponseDto {

    private int postId;                     //pk
    private int categoryId;                 //카테고리
    private String writer;                    //이름
    private String title;                   //제목
    private String content;                 //내용
    private LocalDateTime createDate;       //생성일시
    private LocalDateTime updateDate;       //최종수정일시
    private int status;                     //삭제 0 미삭제 1
    private int viewCount;                  //조회수

    private int fileCount;                  //서브쿼리 - 파일개수

    private String categoryName;            //조인 - 카테고리이름


    public static PostResponseDto toDto(PostVo postVo){
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .postId(postVo.getPostId())
                .categoryId(postVo.getCategoryId())
                .writer(postVo.getWriter())
                .title(postVo.getTitle())
                .content(postVo.getContent())
                .createDate(postVo.getCreateDate())
                .updateDate(postVo.getUpdateDate())
                .status(postVo.getStatus())
                .viewCount(postVo.getViewCount())
                .fileCount(postVo.getFileCount())
                .categoryName(postVo.getCategoryName())
                .build();
        return postResponseDto;
    }
}
