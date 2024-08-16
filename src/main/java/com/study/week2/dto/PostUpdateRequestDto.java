package com.study.week2.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class PostUpdateRequestDto {

    String postId;

    String categoryId;

    @NotBlank(message = "이름을 입력해주세요.")
    String name;

    String inputPassword;

    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    @NotBlank(message = "내용을 입력해주세요.")
    String content;

    int[] removeFileList;

}
