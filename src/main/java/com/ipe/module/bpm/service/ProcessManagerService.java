package com.ipe.module.bpm.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.service.BaseService;
import com.ipe.module.bpm.controller.pojo.ActHistoricProcessInstance;
import com.ipe.module.bpm.controller.pojo.ActProcessDef;
import com.ipe.module.core.dao.RoleDao;
import com.ipe.module.core.dao.UserDao;
import com.ipe.module.core.entity.User;
import com.ipe.module.core.web.security.SystemRealm;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-18
 * Time: 下午1:10
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = false)
public class ProcessManagerService extends BaseService<IDEntity, String> {
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    IdentityService identityService;

    @Override
    public BaseDao<IDEntity, String> getBaseDao() {
        return null;
    }

    /**
     * 流程部署
     *
     * @param processName
     * @throws IOException
     */
    public void deploy(String processName) throws IOException {
        File file = new File(processName);
        InputStream inputStream = FileUtils.openInputStream(file);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        processEngine.getRepositoryService()
                .createDeployment().name(file.getName()).addZipInputStream(zipInputStream).deploy();
    }

    public void testStartProcess(String key) {
        Map<String, Object> variables = new HashMap<String, Object>();
        startProcess("test" + new Date(), key, variables);
    }

    public void startProcess(String businessKey, String key, Map<String, Object> variables) {
        SystemRealm.UserInfo userInfo = (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
        if (!variables.containsKey("user")) {//发送用户(默认)
            variables.put("user", userInfo.getUserId());
        }
        identityService.setAuthenticatedUserId(userInfo.getUserId());
        runtimeService.startProcessInstanceByKey(key, businessKey, variables);
    }

    /**
     * 查看流程图
     *
     * @param key
     * @param version
     * @return
     */
    public InputStream viewDiagram(String key, String version) {
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .processDefinitionVersion(Integer.valueOf(version))
                .singleResult();
        String diagramResourceName = processDefinition.getDiagramResourceName();
        return repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(), diagramResourceName);
    }

    /**
     * 查看运行中的流程实例
     *
     * @param executionId
     * @return
     */
    public InputStream viewDnyDiagram(String executionId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
        Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
        return ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
    }

    /**
     * 得到各节点坐标信息
     *
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> viewProcessInfo(String processInstanceId) throws Exception {
        Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
        Object property = PropertyUtils.getProperty(execution, "activityId");
        String activityId = "";
        if (property != null) {
            activityId = property.toString();
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
        for (ActivityImpl activity : activitiList) {
            boolean currentActiviti = false;
            String id = activity.getId();
            // 当前节点
            if (id.equals(activityId)) {
                currentActiviti = true;
            }
            Map<String, Object> activityImageInfo = packageActivitiInfo(activity, processInstance, currentActiviti);
            activityInfos.add(activityImageInfo);
        }
        return activityInfos;
    }

    private Map<String, Object> packageActivitiInfo(ActivityImpl activity, ProcessInstance processInstance,
                                                    boolean currentActiviti) throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> activityInfo = new HashMap<String, Object>();
        //坐标信息
        activityInfo.put("currentActiviti", currentActiviti);
        activityInfo.put("width", activity.getWidth());
        activityInfo.put("height", activity.getHeight());
        activityInfo.put("x", activity.getX());
        activityInfo.put("y", activity.getY());

        Map<String, Object> properties = activity.getProperties();
        //vars.put("任务类型", WorkflowUtils.parseToZhType(properties.get("type").toString()));
        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        if (activityBehavior instanceof UserTaskActivityBehavior) {
            Task currentTask = null;
            if (currentActiviti) {
                currentTask = getCurrentTaskInfo(processInstance);
            }
            UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
            if (!candidateGroupIdExpressions.isEmpty()) {
                // 任务的处理角色
                setTaskGroup(vars, candidateGroupIdExpressions);
                // 当前处理人
                if (currentTask != null) {
                    setCurrentTaskAssignee(vars, currentTask);
                }
            }
        }
        vars.put("节点说明", properties.get("documentation"));
        String description = activity.getProcessDefinition().getDescription();
        vars.put("描述", description);
        activityInfo.put("vars", vars);
        return activityInfo;
    }

    private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
        String assignee = currentTask.getAssignee();
        if (assignee != null) {
            User assigneeUser = userDao.get(assignee);
            String userInfo = assigneeUser.getUserName() + " (" + assigneeUser.getUserAccount() + ")";
            vars.put("当前处理人", userInfo);
        }
    }

    private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
        String roles = "";
        for (Expression expression : candidateGroupIdExpressions) {
            String expressionText = expression.getExpressionText();
            String roleName = roleDao.get(expressionText).getRoleName();
            roles += roleName;
        }
        vars.put("任务所属角色", roles);
    }

    private Task getCurrentTaskInfo(ProcessInstance processInstance) {
        Task currentTask = null;
        try {
            String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
            currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId)
                    .singleResult();
        } catch (Exception e) {
        }
        return currentTask;
    }

    /**
     * 删除流程定义
     *
     * @param deployId
     */
    public void delDefAll(String deployId) {
        repositoryService.deleteDeployment(deployId, true);
    }

    /**
     * 流程定义列表
     *
     * @param params
     * @param restRequest
     * @return
     */
    public BodyWrapper proDefList(String params, RestRequest restRequest) {
        BodyWrapper bodyWrapp = new BodyWrapper();
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion().listPage(restRequest.getStart(), restRequest.getLimit() + restRequest.getStart());
        Long count = repositoryService.createProcessDefinitionQuery().latestVersion().count();
        List<ProcessDefinition> list = new ArrayList<ProcessDefinition>();
        for (ProcessDefinition p : definitions) {
            list.add(setDefValue(p));
        }
        bodyWrapp.setRows(list);
        bodyWrapp.setTotal(count);
        return bodyWrapp;
    }

    private ActProcessDef setDefValue(ProcessDefinition p) {
        ActProcessDef def = new ActProcessDef();
        def.setId(p.getId());
        def.setName(p.getName());
        def.setCategory(p.getCategory());
        def.setDeploymentId(p.getDeploymentId());
        def.setDescription(p.getDescription());
        def.setDiagramResourceName(p.getDiagramResourceName());
        def.setKey(p.getKey());
        def.setResourceName(p.getResourceName());
        def.setStartFormKey(p.hasStartFormKey());
        def.setStartFormKey(p.hasStartFormKey());
        def.setSuspended(p.isSuspended());
        def.setVersion(p.getVersion());
        return def;
    }

    private ActHistoricProcessInstance setHisValue(HistoricProcessInstance pi) {
        ActHistoricProcessInstance in = new ActHistoricProcessInstance();
        in.setId(pi.getId());
        in.setProcessDefinitionId(pi.getProcessDefinitionId());
        in.setBusinessKey(pi.getBusinessKey());
        in.setDeleteReason(pi.getDeleteReason());
        in.setDurationInMillis(pi.getDurationInMillis());
        in.setStartTime(pi.getStartTime());
        in.setEndTime(pi.getEndTime());
        in.setProcessVariables(pi.getProcessVariables());
        in.setStartActivityId(pi.getStartActivityId());
        if (StringUtils.isNotBlank(pi.getStartUserId())) {
            User user = userDao.get(pi.getStartUserId());
            in.setStartUserId(user.getUserName() + "(" + user.getUserAccount() + ")");
        }
        in.setSuperProcessInstanceId(pi.getSuperProcessInstanceId());
        in.setStatus("RUNNING");
        //流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(pi.getProcessDefinitionId())
                .singleResult();
        in.setProcessName(processDefinition.getName() + "(" + processDefinition.getVersion() + ")");
        return in;
    }

    /**
     * 流程定义历史列表
     *
     * @param params
     * @param restRequest
     * @return
     */
    public BodyWrapper proDefHisList(String params, RestRequest restRequest) {
        BodyWrapper bodyWrapp = new BodyWrapper();
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(params).listPage(restRequest.getStart(), restRequest.getLimit() + restRequest.getStart());
        Long count = repositoryService.createProcessDefinitionQuery().processDefinitionKey(params).count();
        List<ProcessDefinition> list = new ArrayList<ProcessDefinition>();
        for (ProcessDefinition p : definitions) {
            list.add(setDefValue(p));
        }
        bodyWrapp.setRows(list);
        bodyWrapp.setTotal(count);
        return bodyWrapp;
    }

    /**
     * 所有流程实例
     *
     * @param params
     * @param rest
     * @return
     */
    public BodyWrapper proInstaceList(String params, RestRequest rest) {
        BodyWrapper bodyWrapp = new BodyWrapper();
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery()
                .listPage(rest.getStart(), rest.getLimit() + rest.getStart());
        //
        List<ActHistoricProcessInstance> list = new ArrayList<ActHistoricProcessInstance>();
        for (ProcessInstance instance : instances) {
            HistoricProcessInstance pi = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                    .processInstanceId(instance.getProcessInstanceId()).singleResult();
            list.add(setHisValue(pi));
        }
        Long count = runtimeService.createProcessInstanceQuery().count();
        bodyWrapp.setRows(list);
        bodyWrapp.setTotal(count);
        return bodyWrapp;
    }

    /**
     * 用户发起的流程
     *
     * @param params
     * @param rest
     * @return
     */
    public BodyWrapper myStartTask(String params, RestRequest rest) {
        BodyWrapper bodyWrapp = new BodyWrapper();
        SystemRealm.UserInfo userInfo = (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
        List<HistoricProcessInstance> processInstances = historyService
                .createHistoricProcessInstanceQuery()
                .startedBy(userInfo.getUserId())
                .unfinished()
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage(rest.getStart(), rest.getLimit() + rest.getStart());
        Long count = historyService
                .createHistoricProcessInstanceQuery()
                .startedBy(userInfo.getUserId())
                .unfinished()
                .count();
        List<ActHistoricProcessInstance> list = new ArrayList<ActHistoricProcessInstance>();
        for (HistoricProcessInstance pi : processInstances) {
            list.add(setHisValue(pi));
        }
        bodyWrapp.setRows(list);
        bodyWrapp.setTotal(count);
        return bodyWrapp;
    }

    /**
     * 用户参与流程(INCLUDE　我发起的流程)
     *
     * @param params
     * @param rest
     * @return
     */
    public BodyWrapper myPartakeTask(String params, RestRequest rest) {
        BodyWrapper bodyWrapp = new BodyWrapper();
        SystemRealm.UserInfo userInfo = (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
        List<HistoricProcessInstance> processInstances = historyService
                .createHistoricProcessInstanceQuery()
                .involvedUser(userInfo.getUserId())
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage(rest.getStart(), rest.getLimit() + rest.getStart());
        long count = historyService
                .createHistoricProcessInstanceQuery()
                .involvedUser(userInfo.getUserId())
                .count();
        List<ActHistoricProcessInstance> list = new ArrayList<ActHistoricProcessInstance>();
        for (HistoricProcessInstance pi : processInstances) {
            list.add(setHisValue(pi));
        }
        bodyWrapp.setRows(list);
        bodyWrapp.setTotal(count);
        return bodyWrapp;
    }
}
