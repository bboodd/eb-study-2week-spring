package com.study.week2.dto;

import com.study.week2.vo.CommentVo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class CommentDto {

    private int commentId;

    private int postId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    private LocalDateTime createDate;

    /**
     * vo를 dto로
     * db에서 받은 객체를 화면에 뿌려주기 위함
     * @param commentVo
     * @return commentDto
     */

    public static CommentDto toDto(CommentVo commentVo){
        CommentDto commentDto = CommentDto.builder()
                .content(commentVo.getContent())
                .createDate(commentVo.getCreateDate())
                .build();
        return commentDto;
    }
}
