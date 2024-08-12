package com.study.week2.model.dto;

import com.study.week2.model.vo.PostVo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class PostDto {

    int postId;

    @NotBlank(message = "카테고리를 입력해주세요.")
    @Pattern(regexp = "(0-9)", message = "카테고리를 확인해주세요.")
    String categoryId;

    String categoryName;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 10, message = "이름은 10글자이하여야합니다.")
    String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{4,16}$", message = "비밀번호는 4글자 이상, 16글자 미만인 영문/숫자/특수문자를 포함한 문자여야 합니다.")
    String password;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100글자 미만이여야합니다.")
    String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 2000, message = "내용은 2000글자 미만이여야합니다.")
    String content;

    int viewCount;

    String createDate;

    String updateDate;

    int status;

    int fileCount;

    /**
     * vo객체를 dto객체로
     * db에서 받아서 화면으로 넘길때
     * @param postVo
     * @return postDto
     */

    public static PostDto toDto(PostVo postVo){
        PostDto postDto = PostDto.builder()
                .postId(postVo.getPostId())
                .categoryName(postVo.getCategoryName())
                .name(postVo.getName())
                .password(postVo.getPassword())
                .title(postVo.getTitle())
                .content(postVo.getContent())
                .viewCount(postVo.getViewCount())
                .createDate(postVo.getCreateDate())
                .updateDate(postVo.getUpdateDate())
                .fileCount(postVo.getFileCount())
                .build();
        return postDto;
    }
}
