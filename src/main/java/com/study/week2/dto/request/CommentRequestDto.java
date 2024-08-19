package com.study.week2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class CommentRequestDto {
    private int commentId;

    private int postId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

}
