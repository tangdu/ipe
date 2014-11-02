Ext.define('Desktop.view.Footer', {
    extend : 'Ext.panel.Panel',
    height : 20,
    region : 'south',
    border:false,
    initComponent : function(){
        this.html="<div style='text-align:center;margin:0 auto;'>© 2013 tdu</div>";
        this.callParent();
    }
});

Ext.define('Desktop.view.IndexView', {
    extend : 'Ext.panel.Panel',
    border:false,
    bodyCls:'index_img',
    autoScroll : true,
    initComponent : function(){
		this.on('afterrender',this.initIndexView,this);
        this.callParent();
    },initIndexView:function(){
    	ipe.fuc.refreshIndexView(this); 
    }
});

Ext.define('Desktop.view.Container', {
    alias: 'widget.ipeContainer',
    extend : 'Ext.tab.Panel',
    requires : ['Ext.ux.TabReorderer','Ext.ux.TabCloseMenu'],
    activeTab : 0,
    enableTabScroll : true,
    border:false,
    defaults: {
        bodyPadding: 5
    },
    region : 'center',
    //split : true,
    initComponent:function(){
        this.portal=Ext.create('Ext.Panel');
        this.plugins=[Ext.create('Ext.ux.TabReorderer'),
            Ext.create('Ext.ux.TabCloseMenu',{
            parent:this,
            scope:this,
            closeTabText: '关闭面板',
            closeOthersTabsText: '关闭其他',
            closeAllTabsText: '关闭所有'
        })];
        this.callParent();
    }
});

/**
 * 首页面板
 */
Ext.define('Desktop.view.Viewport', {
    extend : 'Ext.container.Viewport',
    requires : ['Desktop.view.Container', 'Desktop.view.Header'],
    layout : 'border',
    id:ipe.viewID,
    initComponent : function() {
        this.ipeHd=Ext.create('Desktop.view.Header',{parent:this});
        this.ipeCon=Ext.create('Desktop.view.Container',{parent:this});
        this.footer=Ext.create('Desktop.view.Footer');
        this.items=[this.ipeHd,this.ipeCon,this.footer];
        
        this.on('afterrender',this.initSysOper,this);
        this.callParent();
    },
    initSysOper:function(){//初始化系统操作
    	if(Ext.get('loading')){
    		Ext.get('loading').update('');
    	}
    	ipe.fuc.openMenu('Desktop.view.IndexView','1','首页','btn_home');
    }
});

