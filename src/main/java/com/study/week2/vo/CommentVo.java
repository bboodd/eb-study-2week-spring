package com.study.week2.vo;
import com.study.week2.dto.CommentDto;
import lombok.*;

@Builder
@Value
//객체 외부 생성 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class CommentVo {

    private int commentId;

    private int postId;

    private String content;

    private String createDate;

    /**
     * dto를 vo로
     * db에 넣기위함
     * @param commentDto
     * @return commentVo
     */

    public static CommentVo toVo(CommentDto commentDto){
        CommentVo commentVo = CommentVo.builder()
                .postId(commentDto.getPostId())
                .content(commentDto.getContent())
                .build();
        return commentVo;
    }
}
