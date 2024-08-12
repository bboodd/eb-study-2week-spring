package com.study.week2.mapper;

import com.study.week2.model.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    int insertPost(PostVo postVo);

    List<CategoryVo> findAllCategory();

    List<PostVo> findAllPostBySearch(SearchVo searchVo);

    PostVo findPostById(int postId);

    int increaseViewCountById(int postId);

    int insertComment(CommentVo commentVo);

    List<CommentVo> findAllCommentByPostId(int postId);

    int deletePostById(int postId);

    int insertFile(FileVo fileVo);

    List<FileVo> findAllFileByPostId(int postId);

    FileVo findFileByFileId(int fileId);
}
