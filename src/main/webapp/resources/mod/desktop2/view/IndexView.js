Ext.define('Desktop.view.IndexView', {
    extend : 'Ext.panel.Panel',
    border:false,
    bodyCls:'index_img',
    autoScroll : true,
    initComponent : function(){
    	this.html="正在加载.....";
		ipe.fuc.refreshIndexView(this);    	
        this.callParent();
    }
});