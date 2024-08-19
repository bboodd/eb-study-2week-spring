package com.study.week2.service;

import com.study.week2.dto.CategoryDto;
import com.study.week2.dto.CommentDto;
import com.study.week2.dto.FileDto;
import com.study.week2.dto.PostDto;
import com.study.week2.dto.response.PostResponseDto;
import com.study.week2.mapper.PostMapper;
import com.study.week2.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.study.week2.dto.CategoryDto.toDto;
import static com.study.week2.dto.PostDto.toDto;
import static com.study.week2.dto.FileDto.toDto;
import static com.study.week2.dto.CommentDto.toDto;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    //TODO:받을때 dto안됨

    private final PostMapper postMapper;

    /**
     * 게시글 등록 후 등록된 id 반환
     * @param postVo
     * @return
     */

    @Transactional
    public int savePost(PostVo postVo){
        postMapper.savePost(postVo);
        int postId = postVo.getPostId();

        return postId;
    }

    /**
     * 댓글 등록 후 0혹은 1 반환
     * @param commentVo
     * @return
     */

    @Transactional
    public int saveComment(CommentVo commentVo){
        int result = postMapper.saveComment(commentVo);
        return result;
    }

    /**
     * 카테고리 목록 조회
     * @return
     */

    public List<CategoryDto> findAllCategory(){
        List<CategoryVo> categoryList = postMapper.findAllCategory();

        List<CategoryDto> result = categoryList.stream()
                .map(CategoryDto::toDto).collect(toList());
        return result;
    }

    /**
     * 검색 게시글 목록 조회
     * @param searchVo
     * @return
     */

    public List<PostResponseDto> findAllPostBySearch(SearchVo searchVo){
        List<PostVo> postList = postMapper.findAllPostBySearch(searchVo);

        List<PostResponseDto> result = postList.stream()
                .map(PostResponseDto::toDto).collect(toList());
        return result;
    }

    /**
     * 댓글 목록 조회
     * @param postId
     * @return
     */

    public List<CommentDto> findAllCommentByPostId(int postId){
        List<CommentVo> commentList = postMapper.findAllCommentByPostId(postId);

        List<CommentDto> result = commentList.stream()
                .map(CommentDto::toDto).collect(toList());
        return result;
    }

    /**
     * 게시글 단건 조회
     * @param postId
     * @return
     */

    public PostResponseDto findPostById(int postId){
        PostResponseDto result = PostResponseDto.toDto(postMapper.findPostById(postId));
        return result;
    }

    public String findPostPasswordById(int postId){
        String password = postMapper.findPostPasswordById(postId);
        return password;
    }

    /**
     * 조회수 1 증가
     * @param postId
     * @return
     */

    public int increaseViewCountById(int postId){
        int result = postMapper.increaseViewCountById(postId);
        return result;
    }

    /**
     * 게시글 삭제
     * @param postId
     * @return
     */

    public int deletePostById(int postId){
        int result = postMapper.deletePostById(postId);
        return result;
    }

    @Transactional
    public int updatePost(PostVo postVo) {
        int result = postMapper.updatePost(postVo);
        return result;
    }

}
