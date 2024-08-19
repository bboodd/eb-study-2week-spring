package com.study.week2.service;

import com.study.week2.dto.FileDto;
import com.study.week2.mapper.PostMapper;
import com.study.week2.vo.FileVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @InjectMocks
    FileService fileService;

    @Mock
    PostMapper postMapper;

    @Test
    @DisplayName("파일 다건 조회 서비스 테스트")
    void get_files() {
        //given
        List<FileVo> fileList = new ArrayList<>();
        FileVo fileVo1 = FileVo.builder().postId(1).fileName("uuid")
                .fileOriginalName("파일이름").filePath("파일경로").fileSize(1024).build();
        FileVo fileVo2 = FileVo.builder().postId(1).fileName("uuid2")
                .fileOriginalName("파일이름2").filePath("파일경로").fileSize(1024).build();
        fileList.add(fileVo1);
        fileList.add(fileVo2);

        given(postMapper.findAllFileByPostId(anyInt())).willReturn(fileList);

        //when
        List<FileDto> result = fileService.findAllFileByPostId(1);

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("파일 조회 서비스 테스트")
    void get_file() {
        //given
        FileVo fileVo1 = FileVo.builder().fileId(1).postId(1).fileName("uuid")
                .fileOriginalName("파일이름").filePath("파일경로").fileSize(1024).build();
        given(postMapper.findFileById(anyInt())).willReturn(fileVo1);

        //when
        FileDto result = fileService.findFileById(1);

        //then
        assertThat(result.getFileOriginalName()).isEqualTo(fileVo1.getFileOriginalName());
    }
}