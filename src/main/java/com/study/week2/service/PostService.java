package com.study.week2.service;

import com.study.week2.dto.CategoryDto;
import com.study.week2.dto.SearchDto;
import com.study.week2.dto.response.CommentResponseDto;
import com.study.week2.dto.response.PagingResponse;
import com.study.week2.dto.response.PostResponseDto;
import com.study.week2.mapper.PostMapper;
import com.study.week2.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.study.week2.vo.SearchVo.toVo;
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
        postMapper.saveComment(commentVo);
        int resultId = commentVo.getCommentId();
        return resultId;
    }

    public CommentResponseDto findCommentById(int commentId){
        CommentVo commentVo = postMapper.findCommentById(commentId);
        return CommentResponseDto.toDto(commentVo);
    }

    public int updateComment(CommentVo commentVo) {
        postMapper.updateComment(commentVo);
        return commentVo.getCommentId();
    }

    @Transactional
    public int deleteComment(int commentId){
        postMapper.deleteCommentById(commentId);
        return commentId;
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
     * @param searchDto
     * @return
     */

    public PagingResponse<PostResponseDto> findAllPostBySearch(SearchDto searchDto){

        // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null을 담아 반환
        int count = postMapper.count(toVo(searchDto));
        if(count < 1){
            return new PagingResponse<>(Collections.emptyList(), null);
        }

        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchVo 타입 객체에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, searchDto);
        searchDto.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<PostVo> postList = postMapper.findAllPostBySearch(toVo(searchDto));

        List<PostResponseDto> result = postList.stream()
                .map(PostResponseDto::toDto).collect(toList());
        return new PagingResponse<>(result, pagination);
    }

    /**
     * 댓글 목록 조회
     * @param postId
     * @return
     */

    public List<CommentResponseDto> findAllCommentByPostId(int postId){
        List<CommentVo> commentList = postMapper.findAllCommentByPostId(postId);

        List<CommentResponseDto> result = commentList.stream()
                .map(CommentResponseDto::toDto).collect(toList());
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
