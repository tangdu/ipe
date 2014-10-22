
/*打开模块类*/
function openSysModule(clz,text,t){
	ipe.fuc.openTab(clz,'1',text,'',t);
}
/*打开模块URL*/
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
/*显示我的消息*/
function showMyMsgWin(obj){
	var win=Ext.create('Sys.msg.MessageViewWin',{data:obj});
	win.show();
}
/*打开新Tab页*/
function openNewTab(obj){
	
}