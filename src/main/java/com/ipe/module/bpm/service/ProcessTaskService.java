package com.ipe.module.bpm.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.service.BaseService;
import com.ipe.module.bpm.controller.pojo.ActTask;
import com.ipe.module.core.dao.RoleDao;
import com.ipe.module.core.dao.UserDao;
import com.ipe.module.core.entity.User;
import com.ipe.module.core.web.security.SystemRealm;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-18
 * Time: 下午1:10
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = false)
public class ProcessTaskService extends BaseService<IDEntity, String> {
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;

    @Override
    public BaseDao<IDEntity, String> getBaseDao() {
        return null;
    }

    /**
     * 个人任务
     *
     * @param params
     * @param rest
     * @return
     */
    public BodyWrapper userTaskList(String params, RestRequest rest) {
        BodyWrapper bodyWrapp = new BodyWrapper();
        SystemRealm.UserInfo userInfo = (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
        List<Task> tasklist = taskService.createTaskQuery().taskAssignee(userInfo.getUserId()).active()
                .orderByTaskCreateTime().desc().listPage(rest.getStart(), rest.getLimit() + rest.getStart());
        Long count = taskService.createTaskQuery().taskAssignee(userInfo.getUserId()).active().count();
        bodyWrapp.setRows(setValue(tasklist));
        bodyWrapp.setTotal(count);
        return bodyWrapp;
    }

    public List<ActTask> setValue(List<Task> tasklist) {
        List<ActTask> list = new ArrayList<ActTask>();
        for (Task task : tasklist) {
            ActTask actTask = new ActTask();
            actTask.setDescription(task.getDescription());
            if (StringUtils.isNotBlank(task.getAssignee())) {
                User user = userDao.get(task.getAssignee());
                actTask.setAssignee(user.getUserName() + "(" + user.getUserAccount() + ")");
            }
            String applyUserId =(String) runtimeService.getVariable(task.getExecutionId(), "applyUserId");
            if(applyUserId!=null && !StringUtils.isBlank(applyUserId)){
            	try {
            		User user = userDao.get(applyUserId);
            		actTask.setAssignee(user.getUserName() + "(" + user.getUserAccount() + ")");
				} catch (Exception e) {
					e.printStackTrace();
				}
            }else{
            	actTask.setUser("系统发送");
            }
            actTask.setTaskId(task.getId());
            actTask.setCreateTime(task.getCreateTime());
            actTask.setDueDate(task.getDueDate());
            actTask.setExecutionId(task.getExecutionId());
            actTask.setName(task.getName());
            actTask.setOwner(task.getOwner());
            actTask.setParentTaskId(task.getParentTaskId());
            actTask.setPriority(task.getPriority());
            actTask.setProcessDefinitionId(task.getProcessDefinitionId());
            actTask.setProcessInstanceId(task.getProcessInstanceId());
            list.add(actTask);
        }
        return list;
    }

    /**
     * 待领取任务
     *
     * @param params
     * @param rest
     * @return
     */
    public BodyWrapper getTaskList(String params, RestRequest rest) {
        BodyWrapper bodyWrapp = new BodyWrapper();
        SystemRealm.UserInfo userInfo = (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
        //得到用户角色
        List<String> roleIdss = userDao.getUserRoleIds(userInfo.getUserId());
        //得到用户组任务
        List<Task> tasklist = taskService.createTaskQuery().taskCandidateGroupIn(roleIdss).active()
                .orderByTaskCreateTime().desc().listPage(rest.getStart(), rest.getLimit() + rest.getStart());
        Long count = taskService.createTaskQuery().taskCandidateGroupIn(roleIdss).active().count();
        bodyWrapp.setRows(setValue(tasklist));
        bodyWrapp.setTotal(count);
        return bodyWrapp;
    }

    /**
     * 删除任务
     *
     * @param taskId
     */
    public void delTask(String taskId) {
        SystemRealm.UserInfo userInfo = (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String info = "流程取消【" + userInfo.getUserName() + "(" + userInfo.getUserAccount() + ")】";
        taskService.addComment(taskId, task.getProcessInstanceId(), info);
        runtimeService.deleteProcessInstance(task.getProcessInstanceId(), info);
    }

    /**
     * 领取任务
     *
     * @param taskId
     */
    public void clainTask(String taskId) {
        SystemRealm.UserInfo userInfo = (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
        taskService.claim(taskId, userInfo.getUserId());
    }

    /**
     * 释放任务
     *
     * @param taskId
     */
    public void releaseTask(String taskId) {
        taskService.claim(taskId, null);
    }

    /**
     * 任务代理
     *
     * @param taskId
     * @param userId
     */
    public void taskProxy(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
        taskService.claim(taskId, userId);
    }

    /**
     * 任务代办
     *
     * @param taskId
     * @param userId
     */
    public void taskDelegate(String taskId, String userId) {
        taskService.delegateTask(taskId, userId);
    }
}
