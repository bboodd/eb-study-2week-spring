package com.study.week2.dto;

import com.study.week2.vo.PostVo;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder
public class PostUpdateResponseDto {

    int postId;

    String createDate;

    String updateDate;

    int viewCount;

    String name;

    String title;

    String content;

//    public static PostUpdateResponseDto toDto(PostVo postVo) {
//
//    }
}
