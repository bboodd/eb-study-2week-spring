package com.study.week2.service;

import com.study.week2.mapper.PostMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @InjectMocks
    BoardService boardService;

    @Mock
    PostMapper postMapper;

    @Test
    void savePost() {
    }

    @Test
    void saveFiles() {
    }

    @Test
    void saveComment() {
    }

    @Test
    void getCategories() {
    }

    @Test
    void getPosts() {
    }

    @Test
    void getFiles() {
    }

    @Test
    void getComments() {
    }

    @Test
    void getPost() {
    }

    @Test
    void getFile() {
    }

    @Test
    void upViewCount() {
    }

    @Test
    void deletePost() {
    }
}