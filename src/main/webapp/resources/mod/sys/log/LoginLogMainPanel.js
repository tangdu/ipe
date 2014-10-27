Ext.define('Sys.log.LogList',{
    extend:'Ext.grid.Panel',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载日志列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},/*{
            header:'访问方法',
            width:200,
            dataIndex:'accessMethod',
            sortable:true
        },*/{
            header:'访问用户',
            width:150,
            dataIndex:'accessPerson',
            sortable:true
        },/*{
            header:'日志类型',
            dataIndex:'logType',
            width:70,
            renderer:ipe.fuc.logTypeDt
        },*/{
            header:'访问IP',
            dataIndex:'accessIp'
        },{
            header:'访问时间',
            width:150,
            dataIndex:'accessTime'
        },{
            header:'退出时间',
            width:150,
            dataIndex:'leaveTime'
        },{
            header:'访问路径',
            width:350,
            dataIndex:'operate'
        },{
            header:'备注',
            dataIndex:'remark'
        }];

        this.tbar=[{
            text:'查询',
            iconCls:ipe.sty.query,
            scope:this,
            handler:this.queryLog
        },{
            text:'浏览',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.viewData
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                enablePaging: true,
                url: 'log/loginlist',
                extraParams:{'a':'L1'},
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : ['id','accessMethod','accessPerson','leaveTime','logType','accessIp','operate','remark','accessTime']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{store:this.store,pageSize:this.pageSize});
        this.callParent();
    },viewData:function(){

    },queryLog:function(){
    	var obj=this.parent.queryForm;
    	obj.isVisible() ? obj.hide():obj.show();
    }
});


Ext.define('Sys.log.QueryForm',{
    extend:'Ext.FormPanel',
    frame:true,
    bodyPadding: 5,
    border:false,
    layout:'column',
     /*fieldDefaults: {
            labelWidth: 125,
            msgTarget: 'side',
            autoFitErrors: false
        },
        defaults: {
            width: 300,
            inputType: 'password'
        },*/
    defaults:{
    	xtype:'container',
    	frame:true
    },
    initComponent:function(){
        this.items=[{
            columnWidth:.25,items:[
            	{layout: {type: 'table',columns:3},xtype:'container',frame:true,items:[
            		{fieldLabel:'访问时间',name:'accessTimeStart',xtype:'datefield',format:'Y-m-d',width:200},
            		{text:'至',xtype:'label'},
            		{xtype:'datefield',name:'accessTimeEnd',format:'Y-m-d',width:100}
            	]},{layout: {type: 'table',columns:3},xtype:'container',frame:true,items:[
            		{fieldLabel:'退出时间',name:'leaveTimeStart',xtype:'datefield',format:'Y-m-d',width:200},
            		{text:'至',xtype:'label'},
            		{xtype:'datefield',name:'leaveTimeEnd',format:'Y-m-d',width:100}
            	]}]
        },{
            columnWidth:.25,items:[
            	{fieldLabel:'访问用户',xtype:'textfield',name:'accessPerson'},
            	{fieldLabel:'访问路径',xtype:'textfield',name:'operate'}]
        },{
            columnWidth:.25,items:[
            	{fieldLabel:'访问IP',xtype:'textfield',name:'accessIp'}]
        },{
            columnWidth:.25,layout:'table',items:[
            	{text:'查询',xtype:'button',iconCls:ipe.sty.query,margin:'0 20 0 0'},
            	{text:'重置',xtype:'button',iconCls:ipe.sty.reset,handler:function(){this.up("form").getForm().reset()}
            }]
        }]
        this.callParent();
    }
});


Ext.define('Sys.log.LoginLogMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
    	this.queryForm=Ext.create('Sys.log.QueryForm',{parent:this,region:'north',height:70,hidden:true});
        this.logList=Ext.create('Sys.log.LogList',{parent:this,region:'center'});
        this.items=[this.queryForm,this.logList];
        this.callParent();
    }
});
