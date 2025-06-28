package com.jiangxue.waxberry.manager.comments.controller;


import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.manager.comments.dto.CommentsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jiangxue.waxberry.manager.comments.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@Tag(name = "评论管理", description = "评论相关接口")
@RestController
@RequestMapping("/mgr/comments/")
public class CommentsController {

	@Autowired
	private CommentsService commentsService;

	/**
	 * 描述 : 提交评论
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "提交评论", description = "提交新的评论")
	@PostMapping("commentCommit")
	public ResponseEntity<ApiResult> commentCommit(
			@Parameter(description = "评论信息") @RequestBody CommentsDTO commentDTO) {
		commentsService.commentCommit(commentDTO);
		return ResponseEntity.ok(ApiResult.success());
	}

	/**
	 * 描述 : 获取智能体评论
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "获取智能体评论", description = "获取智能体相关的评论列表")
	@PostMapping("getSubject")
	public ResponseEntity<ApiResult<List<CommentsDTO>>> getSubject(
			@Parameter(description = "评论查询条件") @RequestBody CommentsDTO commentDTO,
			@Parameter(description = "页码，从0开始") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
			@Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize) {
		return ResponseEntity.ok(ApiResult.success(commentsService.getSubject(commentDTO,pageNo,pageSize)));
	}

	/**
	 * 描述 : 获取主体下评论
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "获取主体下评论", description = "获取指定主体下的评论列表")
	@PostMapping("getComment")
	public ResponseEntity<ApiResult<List<CommentsDTO>>> getComment(
			@Parameter(description = "评论查询条件") @RequestBody CommentsDTO commentDTO,
			@Parameter(description = "页码，从0开始") @RequestParam(name="pageNo",required = false,defaultValue = "0") int pageNo,
			@Parameter(description = "每页大小") @RequestParam(name="pageSize",required = false,defaultValue = "5") int pageSize) {
		return ResponseEntity.ok(ApiResult.success(commentsService.getComment(commentDTO,pageNo,pageSize)));
	}

	/**
	 * 描述 : 删除回复评论
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "删除回复评论", description = "删除指定的回复评论")
	@PostMapping("deleteComment")
	public ResponseEntity<ApiResult> deleteComment(
			@Parameter(description = "评论信息") @RequestBody CommentsDTO commentDTO) {
		commentsService.deleteComment(commentDTO);
		return ResponseEntity.ok(ApiResult.success());
	}

	/**
	 * 描述 : 点赞评论
	 * @param
	 * @return {@link ResponseEntity}
	 * @throws
	 */
	@Operation(summary = "点赞评论", description = "对评论进行点赞操作")
	@PostMapping("likeComments")
	public ResponseEntity<ApiResult<Integer>> likeComments(
			@Parameter(description = "点赞信息") @RequestBody CommentsDTO likeDto) {
		return ResponseEntity.ok(ApiResult.success(commentsService.likeComments(likeDto)));
	}

}
