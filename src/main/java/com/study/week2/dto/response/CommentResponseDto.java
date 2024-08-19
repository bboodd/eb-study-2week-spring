package com.study.week2.dto.response;

import com.study.week2.vo.CommentVo;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private int commentId;
    private int postId;
    private String content;
    private LocalDateTime createDate;

    public static CommentResponseDto toDto(CommentVo commentVo){
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .commentId(commentVo.getCommentId())
                .postId(commentVo.getPostId())
                .content(commentVo.getContent())
                .createDate(commentVo.getCreateDate())
                .build();
        return commentResponseDto;
    }
}
