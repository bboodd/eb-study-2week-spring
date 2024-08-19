package com.study.week2.dto;

import com.study.week2.vo.Pagination;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class SearchDto {

    private String startDate = "";
    private String endDate = "";
    private int categoryId = 0;
    private String keyword = "";

    private int page = 1;           //현재 페이지 번호
    private int recordSize = 10;     //페이지당 출력할 데이터 개수
    private int pageSize = 10;       //화면 하단 출력할 페이지 사이즈

    Pagination pagination;
}
