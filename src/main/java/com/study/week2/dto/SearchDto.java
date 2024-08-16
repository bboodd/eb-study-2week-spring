package com.study.week2.dto;

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

}
