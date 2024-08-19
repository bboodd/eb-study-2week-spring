package com.study.week2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.week2.dto.SearchDto;
import com.study.week2.dto.request.PostRequestDto;
import com.study.week2.service.FileService;
import com.study.week2.vo.PostVo;
import com.study.week2.vo.SearchVo;
import com.study.week2.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.then;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @InjectMocks
    BoardController boardController;

    @Mock
    PostService postService;

    @Mock
    FileService fileService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    @DisplayName("게시글 리스트 조회 테스트")
    void get_posts() throws Exception{
        //given
        SearchDto searchDto = SearchDto.builder()
                .keyword("")
                .endDate("")
                .startDate("")
                .categoryId(0).build();

        //when
        mockMvc.perform(
                get("/"))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        then(postService).should().findAllPostBySearch(searchDto);
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void get_post() throws Exception {
        //given
        int id = 1;

        //when
        mockMvc.perform(get("/post/{id}", id))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        then(postService).should().findPostById(1);
    }

    //TODO:mockMvc 에서 post로 값을 넣어주면 controller에서 null 반환함 해결필요
    @Test
    @DisplayName("게시글 작성 테스트")
    void post_save() throws Exception {
        //given
        PostVo postVo = PostVo.builder()
                .categoryId(1).writer("이름").password("qwer1234!")
                .title("제목").content("내용").build();
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .categoryId(1).writer("이름").password("qwer1234!")
                .title("제목").content("내용").checkPassword("qwer1234!").build();

        //when
        mockMvc.perform(
                post("/write/post")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("postRequestDto", postRequestDto))
//                        .content(objectMapper.writeValueAsString(postRequestDto)))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        //then
        then(postService).should().savePost(postVo);
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void post_save_comment() throws Exception {
    }
}