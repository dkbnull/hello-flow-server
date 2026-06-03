/*
 * Copyright (c) 2017-2026 null. All rights reserved.
 */

package cn.wbnull.helloflow.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务响应码枚举
 *
 * @author null
 * @date 2026-05-26
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(0, "操作成功"),

    // 参数校验 1xxxx
    PARAM_VALIDATION_FAILED(10001, "参数校验失败"),

    // 认证 2xxxx
    UNAUTHORIZED(20001, "未认证"),
    TOKEN_EXPIRED(20002, "Token已过期"),
    TOKEN_INVALID(20003, "Token无效"),
    LOGIN_FAILED(20004, "用户名或密码错误"),
    ACCOUNT_DISABLED(20005, "账号已被禁用"),

    // 权限 3xxxx
    FORBIDDEN(30001, "权限不足"),

    // 资源 4xxxx
    NOT_FOUND(40001, "资源不存在"),
    USER_NOT_FOUND(40002, "用户不存在"),
    PROJECT_NOT_FOUND(40003, "项目不存在"),
    TASK_NOT_FOUND(40004, "任务不存在"),
    SPRINT_NOT_FOUND(40005, "迭代不存在"),
    ROLE_NOT_FOUND(40006, "角色不存在"),

    // 冲突/业务 5xxxx
    USERNAME_EXISTS(50001, "用户名已存在"),
    PROJECT_MEMBER_EXISTS(50002, "项目成员已存在"),
    TASK_RELATION_EXISTS(50003, "任务关联已存在"),
    BUSINESS_ERROR(50004, "业务处理失败"),
    TASK_STATUS_TRANSITION_INVALID(50005, "任务状态流转不合法"),
    CANNOT_REVIEW_OWN_TASK(50006, "不能审查自己的任务"),
    PM_CANNOT_REVIEW(50013, "项目经理不能审查任务"),
    TASK_ALREADY_CANCELLED(50007, "任务已取消"),
    PROJECT_ARCHIVED(50008, "项目已归档"),
    POSITION_MISMATCH(50009, "用户职位与项目角色不匹配"),
    NOT_PROJECT_MEMBER(50010, "用户不是项目成员"),
    PROJECT_CODE_EXISTS(50011, "项目编码已存在"),
    PROJECT_NAME_EXISTS(50012, "项目名称已存在"),

    // 系统 9xxxx
    INTERNAL_ERROR(90001, "服务内部错误"),
    DATABASE_ERROR(90003, "数据库操作失败");

    private final int code;
    private final String message;
}
