package com.study.week2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class PostRequestDto {

    private int postId;

    //TODO: db에 있는 카테고리id랑 유효성 검사?
    @Positive(message = "카테고리를 선택해주세요.")
    private int categoryId;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 10, message = "이름은 10글자이하여야합니다.")
    private String writer;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{4,16}$", message = "비밀번호는 4글자 이상, 16글자 미만인 영문/숫자/특수문자를 포함한 문자여야 합니다.")
    private String password;

    private String checkPassword;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100글자 미만이여야합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 2000, message = "내용은 2000글자 미만이여야합니다.")
    private String content;

    int[] removeFileList;


}
