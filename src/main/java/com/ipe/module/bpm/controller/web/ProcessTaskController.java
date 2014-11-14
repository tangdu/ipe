package com.ipe.module.bpm.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.module.bpm.service.ProcessTaskService;
import com.ipe.module.core.web.controller.AbstractController;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-18
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("bpm/task")
public class ProcessTaskController extends AbstractController {

    @Autowired
    private ProcessTaskService taskService;

    /**
     * 我的任务
     * @param restRequest
     * @param params
     * @return
     */
    @RequestMapping("/my_task_list")
    public  @ResponseBody
    BodyWrapper myTaskList(RestRequest restRequest,String params) {
        try {
            return taskService.userTaskList(params,restRequest);
        } catch (Exception e) {
            LOGGER.error("Exception {}",e);
            return  failure();
        }
    }

    /**
     * 待领取任务
     * @param restRequest
     * @param params
     * @return
     */
    @RequestMapping("/get_task_list")
    public  @ResponseBody BodyWrapper getTaskList(RestRequest restRequest,String params) {
        try {
            return taskService.getTaskList(params, restRequest);
        } catch (Exception e) {
            LOGGER.error("Exception {}",e);
            return  failure();
        }
    }

    /**
     * 释放任务
     * @param taskId
     * @return
     */
    @RequestMapping("/del_task")
    public  @ResponseBody BodyWrapper delList(@RequestParam String taskId) {
        try {
            taskService.delTask(taskId);
            return success();
        } catch (Exception e) {
            LOGGER.error("Exception {}",e);
            return  failure();
        }
    }

    /**
     * 领取任务
     * @param taskId
     * @return
     */
    @RequestMapping("/clain_task")
    public  @ResponseBody BodyWrapper clainTask(@RequestParam String taskId) {
        try {
            taskService.clainTask(taskId);
            return success();
        } catch (Exception e) {
            LOGGER.error("Exception {}",e);
            return  failure();
        }
    }

    /**
     * 释放任务
     * @param taskId
     * @return
     */
    @RequestMapping("/release_task")
    public  @ResponseBody BodyWrapper releaseTask(@RequestParam String taskId) {
        try {
            taskService.releaseTask(taskId);
            return success();
        } catch (Exception e) {
            LOGGER.error("Exception {}",e);
            return  failure();
        }
    }

    /**
     * 任务代理
     * @param taskId
     * @param userId
     * @return
     */
    @RequestMapping("/task_proxy")
    public  @ResponseBody BodyWrapper taskProxy(@RequestParam String taskId,@RequestParam String userId) {
        try {
            taskService.taskProxy(taskId,userId);
            return success();
        } catch (Exception e) {
            LOGGER.error("Exception {}",e);
            return  failure();
        }
    }

    /**
     * 任务指派、代办
     * @param taskId
     * @param userId
     * @return
     */
    @RequestMapping("/task_delegate")
    public  @ResponseBody BodyWrapper taskDelegate(@RequestParam String taskId,@RequestParam String userId) {
        try {
            taskService.taskDelegate(taskId,userId);
            return success();
        } catch (Exception e) {
            LOGGER.error("Exception {}",e);
            return  failure();
        }
    }
}
