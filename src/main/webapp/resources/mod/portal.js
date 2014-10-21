
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

/*显示我的任务*/
function showMyTaskWin(obj){
	//TODO 支持自定义参数传递..width,height等，具体可在:description携带参数
	var config={
		taskId:'',
		title:obj.name+" "+(typeof obj.description=='undefined' ? '':obj.description),
		data:obj
	};
	var win=Ext.create('Bpm.MyProcessTaskWin',config);
	win.show();
}