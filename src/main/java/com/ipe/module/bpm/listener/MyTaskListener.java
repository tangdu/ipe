package com.ipe.module.bpm.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-22
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
       System.out.print(delegateTask.getAssignee()+"--"+delegateTask.getExecutionId());
    }
}
