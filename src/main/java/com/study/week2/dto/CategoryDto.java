package com.study.week2.dto;

import com.study.week2.vo.CategoryVo;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class CategoryDto {

    private int categoryId;

    private String categoryName;

    /**
     * vo to dto
     * 카테고리 내용을 화면에 뿌려주기위해
     * @param categoryVo
     * @return categoryDto
     */

    public static CategoryDto toDto(CategoryVo categoryVo){
        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(categoryVo.getCategoryId())
                .categoryName(categoryVo.getCategoryName())
                .build();
        return categoryDto;
    }
}
