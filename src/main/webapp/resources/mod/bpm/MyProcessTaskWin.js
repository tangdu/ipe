Ext.define("Bpm.MyProcessTaskWin",{
	title:'我的任务',
	extend:'Ext.Window',
	autoScroll :true,
	closeAction:'close',
    border:false,
    modal:true,
    layout:'fit',
	width:700,
	height:500,
	initComponent:function(){
		if(this.width){
			this.width=700;
		}
		if(this.height){
			this.height=500;
		}
		
		//TODO 
		this.buttons=[
            {text:'取消',scope:this,handler:this.close,iconCls:ipe.sty.cancel}];
		this.callParent();
	}
});