package com.study.week2.dto;

import com.study.week2.vo.PostVo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class PostDto {

    int postId;

    int categoryId;

    String categoryName;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 10, message = "이름은 10글자이하여야합니다.")
    String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{4,16}$", message = "비밀번호는 4글자 이상, 16글자 미만인 영문/숫자/특수문자를 포함한 문자여야 합니다.")
    String password;

    String checkPassword;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100글자 미만이여야합니다.")
    String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 2000, message = "내용은 2000글자 미만이여야합니다.")
    String content;

    int viewCount;

    LocalDateTime createDate;

    LocalDateTime updateDate;

    int status;

    int fileCount;

    int[] removeFileList;

    /**
     * vo객체를 dto객체로
     * db에서 받아서 화면으로 넘길때
     * @param postVo
     * @return postDto
     */

    public static PostDto toDto(PostVo postVo){
        PostDto postDto = PostDto.builder()
                .postId(postVo.getPostId())
                .categoryId(postVo.getCategoryId())
                .categoryName(postVo.getCategoryName())
                .name(postVo.getWriter())
                .password(postVo.getPassword())
                .title(postVo.getTitle())
                .content(postVo.getContent())
                .viewCount(postVo.getViewCount())
                .createDate(postVo.getCreateDate())
                .updateDate(postVo.getUpdateDate())
                .fileCount(postVo.getFileCount())
                .status(postVo.getStatus())
                .build();
        return postDto;
    }
}
