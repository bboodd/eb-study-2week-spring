package com.study.week2.service;

import com.study.week2.mapper.PostMapper;
import com.study.week2.model.dto.*;
import com.study.week2.model.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.study.week2.model.dto.CategoryDto.toDto;
import static com.study.week2.model.dto.PostDto.toDto;
import static com.study.week2.model.dto.FileDto.toDto;
import static com.study.week2.model.dto.CommentDto.toDto;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final PostMapper postMapper;

    /**
     * 게시글 등록 후 등록된 id 반환
     * @param postVo
     * @return
     */

    public int savePost(PostVo postVo){
        postMapper.insertPost(postVo);
        int postId = postVo.getPostId();
        return postId;
    }

    /**
     * 파일 다건 등록 후 등록된 개수 반환
     * @param fileVoList
     * @return
     */

    public int saveFiles(List<FileVo> fileVoList){
        final int[] result = {0}; //lambda 접근
        fileVoList.stream().forEach(file -> {
            result[0] += postMapper.insertFile(file);
        });
        return result[0];
    }

    /**
     * 댓글 등록 후 0혹은 1 반환
     * @param commentVo
     * @return
     */

    public int saveComment(CommentVo commentVo){
        int result = postMapper.insertComment(commentVo);
        return result;
    }

    /**
     * 카테고리 목록 조회
     * @return
     */

    public List<CategoryDto> getCategories(){
        List<CategoryVo> categoryList = postMapper.findAllCategory();

        List<CategoryDto> result = categoryList.stream()
                .map(c -> toDto(c)).collect(toList());
        return result;
    }

    /**
     * 검색 게시글 목록 조회
     * @param searchVo
     * @return
     */

    public List<PostDto> getPosts(SearchVo searchVo){
        List<PostVo> postList = postMapper.findAllPostBySearch(searchVo);

        List<PostDto> result = postList.stream()
                .map(p -> toDto(p)).collect(toList());
        return result;
    }

    /**
     * 파일 목록 조회
     * @param postId
     * @return
     */

    public List<FileDto> getFiles(int postId){
        List<FileVo> fileList = postMapper.findAllFileByPostId(postId);

        List<FileDto> result = fileList.stream()
                .map(f -> toDto(f)).collect(toList());
        return result;
    }

    /**
     * 댓글 목록 조회
     * @param postId
     * @return
     */

    public List<CommentDto> getComments(int postId){
        List<CommentVo> commentList = postMapper.findAllCommentByPostId(postId);

        List<CommentDto> result = commentList.stream()
                .map(c -> toDto(c)).collect(toList());
        return result;
    }

    /**
     * 게시글 단건 조회
     * @param postId
     * @return
     */

    public PostDto getPost(int postId){
        PostDto result = toDto(postMapper.findPostById(postId));
        return result;
    }

    public FileDto getFile(int fileId){
        FileDto result = toDto(postMapper.findFileByFileId(fileId));
        return result;
    }

    /**
     * 조회수 1 증가
     * @param postId
     * @return
     */

    public int upViewCount(int postId){
        int result = postMapper.increaseViewCountById(postId);
        return result;
    }

    /**
     * 게시글 삭제
     * @param postId
     * @return
     */

    public int deletePost(int postId){
        int result = postMapper.deletePostById(postId);
        return result;
    }

}
