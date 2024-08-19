package com.study.week2.mapper;

import com.study.week2.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    int savePost(PostVo postVo);

    List<CategoryVo> findAllCategory();

    List<PostVo> findAllPostBySearch(SearchVo searchVo);

    int count(SearchVo searchVo);

    PostVo findPostById(int postId);

    int saveComment(CommentVo commentVo);

    CommentVo findCommentById(int commentId);

    int updateComment(CommentVo commentVo);

    int deleteCommentById(int commentId);

    List<CommentVo> findAllCommentByPostId(int postId);

    int deletePostById(int postId);

    int saveFile(FileVo fileVo);

    List<FileVo> findAllFileByPostId(int postId);

    FileVo findFileById(int fileId);

    int increaseViewCountById(int postId);

    int updatePost(PostVo postVo);

    int deleteFileById(int fileId);

    String findPostPasswordById(int postId);
}
