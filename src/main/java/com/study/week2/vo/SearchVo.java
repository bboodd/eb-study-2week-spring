package com.study.week2.vo;
import com.study.week2.dto.SearchDto;
import lombok.*;

@Builder
@Value
//객체 외부 생성 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class SearchVo {

    private String startDate;

    private String endDate;

    private int categoryId;

    private String keyword;

    /**
     * dto를 vo로 변환
     * 화면에서 받아서 db로 넘길때
     * @param searchDto
     * @return searchVo
     */

    public static SearchVo toVo(SearchDto searchDto){
        SearchVo searchVo = SearchVo.builder()
                .startDate(searchDto.getStartDate())
                .endDate(searchDto.getEndDate())
                .categoryId(Integer.parseInt(searchDto.getCategoryId()))
                .keyword(searchDto.getKeyword())
                .build();
        return searchVo;
    }
}
