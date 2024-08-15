package com.study.week2.vo;
import lombok.*;

@Builder
@Value
//객체 외부 생성 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class CategoryVo {

    private int categoryId;

    private String categoryName;
}
