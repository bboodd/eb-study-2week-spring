package com.study.week2.service;

import com.study.week2.mapper.PostMapper;
import com.study.week2.dto.CategoryDto;
import com.study.week2.dto.CommentDto;
import com.study.week2.dto.FileDto;
import com.study.week2.dto.PostDto;
import com.study.week2.vo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @InjectMocks
    BoardService boardService;

    @Mock
    PostMapper postMapper;

    @Test
    @DisplayName("게시글 작성 서비스 테스트")
    void save_post() {
        //given
        PostVo postVo = PostVo.builder()
                .categoryId(1).name("test").password("qwer1234!")
                .title("test").content("test").build();
        //postMapper의 insertPost는 위의 postVo가 들어오면 1을 리턴 한다
        given(postMapper.insertPost(postVo)).willReturn(1);

        //when
        boardService.savePost(postVo);

        //then
        //postMapper의 insertPost가 호출되는지 검증
        then(postMapper).should().insertPost(any());
    }

    @Test
    @DisplayName("파일 다건 추가 서비스 테스트")
    void save_files(){
        //given
        List<FileVo> fileList = new ArrayList<>();
        FileVo fileVo1 = FileVo.builder().postId(1).fileName("uuid")
                .fileOriginalName("파일이름").filePath("파일경로").fileSize(1024).build();
        FileVo fileVo2 = FileVo.builder().postId(1).fileName("uuid2")
                .fileOriginalName("파일이름2").filePath("파일경로").fileSize(1024).build();
        fileList.add(fileVo1);
        fileList.add(fileVo2);

        given(postMapper.insertFile(any(FileVo.class))).willReturn(1);

        //when
        boardService.saveFiles(fileList);

        //then
        //2번 호출 했는지 확인
        then(postMapper).should(times(2)).insertFile(any());

    }

    @Test
    @DisplayName("댓글 작성 서비스 테스트")
    void save_comment() {
        //given
        CommentVo commentVo = CommentVo.builder().postId(1).content("댓글 작성").build();
        given(postMapper.insertComment(commentVo)).willReturn(1);

        //when
        boardService.saveComment(commentVo);

        //then
        then(postMapper).should().insertComment(any());
    }

    @Test
    @DisplayName("카테고리 조회 서비스 테스트")
    void get_categories() {
        //given
        List<CategoryVo> categoryList = new ArrayList<>();
        CategoryVo categoryVo = CategoryVo.builder().categoryId(1).categoryName("JAVA").build();
        CategoryVo categoryVo2 = CategoryVo.builder().categoryId(2).categoryName("JavaScript").build();
        categoryList.add(categoryVo);
        categoryList.add(categoryVo2);

        given(postMapper.findAllCategory()).willReturn(categoryList);

        //when
        List<CategoryDto> result = boardService.getCategories();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 다건 조회 서비스 테스트")
    void get_posts() {
        //given
        SearchVo searchVo = SearchVo.builder().categoryId(0).startDate("").endDate("").keyword("").build();
        List<PostVo> postList = new ArrayList<>();
        PostVo postVo1 = PostVo.builder().categoryId(1).name("이름").password("qwer1234!")
                .title("제목").content("내용").createDate("만든날").updateDate("만든날").build();
        PostVo postVo2 = PostVo.builder().categoryId(1).name("이름").password("qwer1234!")
                .title("제목").content("내용").createDate("만든날").updateDate("만든날").build();
        postList.add(postVo1);
        postList.add(postVo2);

        given(postMapper.findAllPostBySearch(searchVo)).willReturn(postList);

        //when
        List<PostDto> result = boardService.getPosts(searchVo);

        //then
        assertThat(result.size()).isEqualTo(2);
    }

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
        List<FileDto> result = boardService.getFiles(1);

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("댓글 다건 조회 서비스 테스트")
    void get_comments() {
        //given
        List<CommentVo> commentList = new ArrayList<>();
        CommentVo commentVo1 = CommentVo.builder().postId(1).createDate("날짜").content("내용").build();
        CommentVo commentVo2 = CommentVo.builder().postId(1).createDate("날짜").content("내용").build();
        commentList.add(commentVo1);
        commentList.add(commentVo2);

        given(postMapper.findAllCommentByPostId(anyInt())).willReturn(commentList);

        //when
        List<CommentDto> result = boardService.getComments(1);

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 조회 서비스 테스트")
    void get_post() {
        //given
        PostVo postVo1 = PostVo.builder().categoryId(1).name("이름").password("qwer1234!")
                .title("제목").content("내용").createDate("만든날").updateDate("만든날").build();
        given(postMapper.findPostById(anyInt())).willReturn(postVo1);

        //when
        PostDto result = boardService.getPost(1);

        //then
        assertThat(result.getName()).isEqualTo(postVo1.getName());
    }

    @Test
    @DisplayName("파일 조회 서비스 테스트")
    void get_file() {
        //given
        FileVo fileVo1 = FileVo.builder().fileId(1).postId(1).fileName("uuid")
                .fileOriginalName("파일이름").filePath("파일경로").fileSize(1024).build();
        given(postMapper.findFileByFileId(anyInt())).willReturn(fileVo1);

        //when
        FileDto result = boardService.getFile(1);

        //then
        assertThat(result.getFileOriginalName()).isEqualTo(fileVo1.getFileOriginalName());
    }

    @Test
    @DisplayName("조회수 증가 서비스 테스트")
    void up_viewCount() {
        //given

        //when
        boardService.upViewCount(1);

        //then
        then(postMapper).should().increaseViewCountById(anyInt());
    }

    @Test
    @DisplayName("게시글 제거 테스트")
    void delete_post() {
        //given

        //when
        boardService.deletePost(1);

        //then
        then(postMapper).should().deletePostById(anyInt());
    }
}