/**
 * 首页面板
 */
Ext.define('Desktop.view.Viewport', {
    extend : 'Ext.container.Viewport',
    requires : ['Desktop.view.Navigation'],
    layout : 'border',
    id:ipe.viewID,
    initComponent : function() {
        this.ipeHd=Ext.create('Desktop.view.Header',{parent:this});
        this.ipeCon=Ext.create('Desktop.view.Container',{parent:this});
        this.ipeNav=Ext.create('Desktop.view.Navigation',{parent:this});
        this.footer=Ext.create('Desktop.view.Footer',{parent:this});
        this.items=[this.ipeHd,this.ipeCon,this.ipeNav,this.footer];
        
        this.on('afterrender',this.initSysOper,this);
        this.callParent();
    },
    initSysOper:function(){//初始化系统操作
    	Ext.get('loading').update('');
    	ipe.fuc.openTab('Desktop.view.IndexView','1','首页','btn_home');
    }
});

//Header
Ext.define('Desktop.view.Header', {
    extend : 'Ext.Container',
    height : 50,
    region : 'north',
    border:false,
    cls:'sys_header',
    layout:{type:'hbox',align:'stretch'},
    initComponent : function(){
    	
    	//F:用ExtJS构建的Header不好设置全局的背景图
        /*this.title=Ext.create('Ext.Container',{flex:8,html:'IPE系统平台1.0'});
       
        this.btn1=Ext.create('Ext.Toolbar',{flex:1,border:false,cls:'toolbar',items:[
			'->',{
				xtype:'label',html:"欢迎您，"+user.userName,padding:'0 5 0 5'
			},'-',{
				xtype:'label',text:"我的消息",padding:'0 5 0 5',
				listeners:{'click':{
					element: 'el',
					fn:this.showMsg
				}},cls:'toolbar_btn'
			},'-',{
				xtype:'label',text:"修改密码",padding:'0 5 0 5',
				listeners:{'click':{
					element: 'el',
					fn:this.upPwd
				}},cls:'toolbar_btn'
			},'-',{
				xtype:'label',text:"退出系统",padding:'0 5 0 5',
				listeners:{'click':{
					element: 'el',
					fn:this.logout
				}},cls:'toolbar_btn'
			},{width:20,xtype:'tbspacer'}
        ]});
        
        this.btn2=Ext.create('Ext.Toolbar',{flex:1,border:false,cls:'toolbar'});
        this.opera=Ext.create('Ext.Container',{
            flex:3,
            layout:{type:'vbox',align:'stretch'},
            items:[this.btn1,this.btn2]
        });*/
        
    	//F:用HTML构建Header,修改样式:sys_header
    	this.title=Ext.create('Ext.Container',{flex:1,html:
    		'<span class="sys_title bold">IPE系统平台1.0</span>' +
    		'<span class="block-right list-groups">' +
    			/*'<span class="list-item">欢迎您，'+user.userName+'</span>|' +*/
    			'<span class="list-item bold"><a href="javascript:ipe.fuc.showMsg()">我的消息</a></span>|' +
    			'<span class="list-item bold"><a href="javascript:ipe.fuc.upPwd()">修改密码</a></span>|' +
    			'<span class="list-item bold"><a href="javascript:ipe.fuc.logout()">退出系统</a></span>' +
    			/*'<div class="m_time">'+new Date().toLocaleString()+'</div>'+*/
    		'</span>'
    	});
        this.items=[this.title];
        this.callParent();
    }
});

//容器
Ext.define('Desktop.view.Container', {
    alias: 'widget.ipeContainer',
    extend : 'Ext.tab.Panel',
    requires : ['Ext.ux.TabReorderer','Ext.ux.TabCloseMenu'],
    activeTab : 0,
    enableTabScroll : true,
    //animScroll : true,
    //border : false,
    //autoScroll : true,
	border:false,
    region : 'center',
    defaults: {
        //bodyPadding: 1
    },
    plugins : [
        Ext.create('Ext.ux.TabReorderer'),
        Ext.create('Ext.ux.TabCloseMenu',{
            closeTabText: '关闭面板',
            closeOthersTabsText: '关闭其他',
            closeAllTabsText: '关闭所有'
        })
    ],
    initComponent:function(){
        /*this.portal=Ext.create('Desktop.view.IndexView',{border:false});
        this.items = [{title:'首页',iconCls:'btn_home',layout:'fit',items:this.portal}];*/
        this.callParent();
    }
});

//脚注
Ext.define('Desktop.view.Footer', {
    extend : 'Ext.panel.Panel',
    height : 20,
    region : 'south',
    border:false,
    initComponent : function(){
        this.html="<div style='text-align:center;margin:0 auto;'>© 2014 tdu</div>";
        this.callParent();
    }
});
