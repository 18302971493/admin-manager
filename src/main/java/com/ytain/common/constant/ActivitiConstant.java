package com.ytain.common.constant;

/**
 * @author hugd
 */
public interface ActivitiConstant {

    /**
     * 流程状态 已激活
     */
    Integer PROCESS_STATUS_ACTIVE = 1;

    /**
     * 流程状态 暂停挂起
     */
    Integer PROCESS_STATUS_SUSPEND = 0;

    /**
     * 资源类型 XML
     */
    Integer RESOURCE_TYPE_XML = 0;

    /**
     * 资源类型 图片
     */
    Integer RESOURCE_TYPE_IMAGE = 1;

    /**
     * 状态 待提交申请
     */
    Integer STATUS_TO_APPLY = 0;

    /**
     * 状态 处理中
     */
    Integer STATUS_DEALING = 1;

    /**
     * 状态 处理结束
     */
    Integer STATUS_FINISH = 2;

    /**
     * 结果 待提交
     */
    Integer RESULT_TO_SUBMIT = 0;

    /**
     * 结果 处理中
     */
    Integer RESULT_DEALING = 1;

    /**
     * 结果 通过
     */
    Integer RESULT_PASS = 2;

    /**
     * 结果 驳回
     */
    Integer RESULT_FAIL = 3;

    /**
     * 节点类型 开始节点
     */
    Integer NODE_TYPE_START = 0;

    /**
     * 节点类型 用户任务
     */
    Integer NODE_TYPE_TASK = 1;

    /**
     * 节点类型 结束
     */
    Integer NODE_TYPE_END = 2;

    /**
     * 节点关联类型 角色
     */
    Integer NODE_ROLE = 0;

    /**
     * 节点关联类型 用户
     */
    Integer NODE_USER = 1;

    /**
     * 节点关联类型 部门
     */
    Integer NODE_DEPARTMENT = 2;
}
