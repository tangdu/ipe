
function getMoreTask(t){
	ipe.fuc.openTab('Bpm.ProcessTaskManagerMainPanel','1','我的任务','',t);
}

function getMoreMsg(t){
	ipe.fuc.openTab('Sys.msg.SysRemindMainPanel','1','我的消息','',t);
}

function getUserManager(t){
	ipe.fuc.openTab('Sys.user.UserMainPanel','1','用户管理','',t);
}

function openSysModule(clz,text,t){
	ipe.fuc.openTab(clz,'1',text,'',t);
}
function openUrlLink(url,text,t){
	ipe.fuc.openTab(url,'0',text,'',t);
}