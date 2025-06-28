package com.jiangxue.waxberry.manager.comments.service;


import com.jiangxue.waxberry.manager.comments.dto.CommentsDTO;

import java.util.List;


public interface CommentsService {

    void commentCommit(CommentsDTO commentDTO);

    List<CommentsDTO> getSubject(CommentsDTO  commentDTO, int pageNo, int pageSize);

    List<CommentsDTO> getComment(CommentsDTO  commentDTO, int pageNo, int pageSize);

    void deleteComment(CommentsDTO commentDTO);

    int likeComments(CommentsDTO likeDto);
}
