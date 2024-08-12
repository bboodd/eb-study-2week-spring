package com.study.week2.model.dto;

import com.study.week2.model.vo.SearchVo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class SearchDto {

    @NotNull(message = "날짜를 입력해주세요.")
    private String startDate;

    @NotNull(message = "날짜를 입력해주세요.")
    private String endDate;

    @NotBlank(message = "카테고리를 입력해주세요.")
    @Pattern(regexp = "(0-9)", message = "카테고리를 확인해주세요.")
    private String categoryId;

    @NotNull(message = "검색어를 입력해주세요.")
    private String keyword;

}
