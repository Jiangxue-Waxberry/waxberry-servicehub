package com.jiangxue.waxberry.manager.comments.service.impl;


import com.jiangxue.framework.common.security.SecurityUtils;
import com.jiangxue.framework.common.util.ObjectConvertUtil;
import com.jiangxue.waxberry.manager.comments.dto.CommentsDTO;
import com.jiangxue.waxberry.manager.comments.entity.CommentsComment;
import com.jiangxue.waxberry.manager.comments.entity.CommentsLike;
import com.jiangxue.waxberry.manager.comments.entity.CommentsSubject;
import com.jiangxue.waxberry.manager.comments.enums.CommentsEnum;
import com.jiangxue.waxberry.manager.comments.enums.SortEnum;
import com.jiangxue.waxberry.manager.comments.repository.CommentsCommentRepository;
import com.jiangxue.waxberry.manager.comments.repository.CommentsLikeRepository;
import com.jiangxue.waxberry.manager.comments.repository.CommentsSubjectRepository;
import com.jiangxue.waxberry.manager.comments.service.CommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsSubjectRepository commentsSubjectRepository;

    @Autowired
    private CommentsCommentRepository commentsCommentRepository;

    @Autowired
    private CommentsLikeRepository commentsLikeRepository;

    @Override
    @Transactional
    public void commentCommit(CommentsDTO commentDTO) {
        String userId = SecurityUtils.requireCurrentUserId();
        commentDTO.setUserId(userId);
        commentDTO.setUserName(SecurityUtils.getCurrentUserLoginName().orElse(null));
        commentDTO.setCreator(userId);
        commentDTO.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        commentDTO.setModifier(userId);
        commentDTO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        if (CommentsEnum.CHILD.getValue().equalsIgnoreCase(commentDTO.getType())) {
            Optional<CommentsSubject> subjectOptional = commentsSubjectRepository.findById(commentDTO.getSubjectId());
            CommentsSubject commentsSubject = subjectOptional.get();
            commentsSubject.setCommentCount(commentsSubject.getCommentCount() + 1);
            commentsSubjectRepository.save(commentsSubject);
            CommentsComment commentsComment = ObjectConvertUtil.convertTo(commentDTO, CommentsComment.class);
            commentsCommentRepository.save(commentsComment);
        } else {
            CommentsSubject commentsSubject = ObjectConvertUtil.convertTo(commentDTO, CommentsSubject.class);
            commentsSubjectRepository.save(commentsSubject);
        }
    }

    @Override
    @Transactional
    public List<CommentsDTO> getSubject(CommentsDTO  commentDTO, int pageNo, int pageSize) {
        List<CommentsDTO>  commentsDTOList = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<CommentsSubject> commentsSubjects = null;
        if (SortEnum.DEFAULT.getValue().equals(commentDTO.getSortFlag())) {
            commentsSubjects = commentsSubjectRepository.findAllByAgentIdDefault(commentDTO.getAgentId(), pageable);
        } else {
            commentsSubjects = commentsSubjectRepository.findAllByAgentId(commentDTO.getAgentId(), pageable);
        }
        if (!ObjectUtils.isEmpty(commentsSubjects)) {
            List<CommentsSubject> commentsSubjectList = commentsSubjects.getContent();
            commentsDTOList = ObjectConvertUtil.convertToList(commentsSubjectList, CommentsDTO.class);
            this.setLikeFlag(commentsDTOList,CommentsEnum.PARENT.getValue());
        }
        return commentsDTOList;
    }

    @Override
    @Transactional
    public List<CommentsDTO> getComment(CommentsDTO  commentDTO, int pageNo, int pageSize) {
        List<CommentsDTO> commentsDTOList = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<CommentsComment> commentsComments = commentsCommentRepository.findAllBySubjectId(commentDTO.getSubjectId(), pageable);
        if (!ObjectUtils.isEmpty(commentsComments)) {
            List<CommentsComment> commentsCommentList = commentsComments.getContent();
            commentsDTOList = ObjectConvertUtil.convertToList(commentsCommentList, CommentsDTO.class);
            this.setLikeFlag(commentsDTOList,CommentsEnum.CHILD.getValue());
        }
        return commentsDTOList;
    }

    public void setLikeFlag(List<CommentsDTO> commentsDTOList,String type) {
        Map<String,String> idMap = new HashMap<>();
        String userId = SecurityUtils.requireCurrentUserId();
        List<String> ids = commentsDTOList.stream().map(CommentsDTO::getId).collect(Collectors.toList());
        if (CommentsEnum.PARENT.getValue().equalsIgnoreCase(type)) {
            List<CommentsLike> LikeListBySubjectId = commentsLikeRepository.findAllByUseridAndSubjectId(userId, ids);
            if (!ObjectUtils.isEmpty(LikeListBySubjectId)) {
                idMap = LikeListBySubjectId.stream().collect(Collectors.toMap(CommentsLike::getSubjectId, CommentsLike::getLikeFlag));
            }
        } else {
            List<CommentsLike> LikeListBySubjectId = commentsLikeRepository.findAllByUseridAndChildId(userId, ids);
            if (!ObjectUtils.isEmpty(LikeListBySubjectId)) {
                idMap = LikeListBySubjectId.stream().collect(Collectors.toMap(CommentsLike::getChildId, CommentsLike::getLikeFlag));
            }
        }
        if (!ObjectUtils.isEmpty(idMap)) {
            for (CommentsDTO commentsDTO : commentsDTOList) {
                if (idMap.containsKey(commentsDTO.getId())) {
                    commentsDTO.setLikeFlag(CommentsEnum.CONFIRM.getValue());
                } else {
                    commentsDTO.setLikeFlag(CommentsEnum.CANCEL.getValue());
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteComment(CommentsDTO commentDTO) {
        if (CommentsEnum.CHILD.getValue().equalsIgnoreCase(commentDTO.getType())) {
            Integer likeCount = commentsLikeRepository.getCountByChildId(commentDTO.getId());
            if (likeCount > 0) {
                commentsLikeRepository.deleteByChildId(commentDTO.getId());
            }
            commentsCommentRepository.deleteById(commentDTO.getId());
        } else {
            Integer commentCount = commentsCommentRepository.getCountBySubjectId(commentDTO.getId());
            if (commentCount > 0) {
                commentsCommentRepository.deleteBySubjectId(commentDTO.getId());
            }
            Integer likeCount = commentsLikeRepository.getCountBySubjectId(commentDTO.getId());
            if (likeCount > 0) {
                commentsLikeRepository.deleteBySubjectId(commentDTO.getId());
            }
            commentsSubjectRepository.deleteById(commentDTO.getId());
        }
    }

    @Override
    @Transactional
    public int likeComments(CommentsDTO likeDto) {
        String userId = SecurityUtils.requireCurrentUserId();
        likeDto.setUserId(userId);
        likeDto.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        int likeCount = 0;
        if (!ObjectUtils.isEmpty(likeDto.getChildId())) {
            Optional<CommentsComment> commentOptional = commentsCommentRepository.findById(likeDto.getId());
            CommentsComment commentsComment = commentOptional.get();
            if (CommentsEnum.CONFIRM.getValue().equals(likeDto.getLikeFlag())) {
                likeCount = commentsComment.getLikeCount() + 1;
                commentsComment.setLikeCount(likeCount);
                CommentsLike commentsLike = ObjectConvertUtil.convertTo(likeDto, CommentsLike.class);
                commentsLikeRepository.save(commentsLike);
            } else {
                likeCount = commentsComment.getLikeCount() - 1;
                commentsComment.setLikeCount(likeCount);
                commentsLikeRepository.deleteByChildId(likeDto.getChildId());
            }
            commentsCommentRepository.save(commentsComment);
        } else {
            Optional<CommentsSubject> subjectOptional = commentsSubjectRepository.findById(likeDto.getId());
            CommentsSubject commentsSubject = subjectOptional.get();
            if (CommentsEnum.CONFIRM.getValue().equals(likeDto.getLikeFlag())) {
                likeCount = commentsSubject.getLikeCount() + 1;
                commentsSubject.setLikeCount(likeCount);
                CommentsLike commentsLike = ObjectConvertUtil.convertTo(likeDto, CommentsLike.class);
                commentsLikeRepository.save(commentsLike);
            } else {
                likeCount = commentsSubject.getLikeCount() - 1;
                commentsSubject.setLikeCount(likeCount);
                commentsLikeRepository.deleteBySubjectId(likeDto.getSubjectId());
            }
            commentsSubjectRepository.save(commentsSubject);
        }
        return likeCount;
    }
}
