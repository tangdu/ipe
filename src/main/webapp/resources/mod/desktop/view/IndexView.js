Ext.define('Desktop.view.IndexView', {
    extend : 'Ext.panel.Panel',
    border:false,
    bodyCls:'index_img',
    autoScroll : true,
    initComponent : function(){
    	this.html="正在加载.....";
    	var me=this;
    	Ext.Ajax.request({
    		url:'getIndexView',
    		method:'POST',
    		success:function(resp){
    			var result=Ext.decode(resp.responseText);
    			/*var script=/<script>([\s\S]*?)<\/script>/gi;
    			var match=script.exec(result.rows);
    			while(match!=null){
    				eval('('+match[1]+')');
    				match=script.exec(result.rows);
    			}*/
    			me.body.update(result.rows);
    			me.doRender();
    		}
    	});   
        this.callParent();
    },doRender:function(){
    	Ext.Loader.loadScript('resources/mod/portal.js');
    	//Ext.get("i-more-msg").on('click',this.getMoreMsg,this);
    	//Ext.get("i-more-task").on('click',this.getMoreTask,this);
    }
});