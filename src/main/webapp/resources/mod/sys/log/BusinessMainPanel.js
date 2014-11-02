/**
 * Created by tangdu on 14-2-16.
 */
Ext.define('Sys.log.BusLogList',{
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
        this.columns=[{xtype: 'rownumberer'},{
            header:'访问方法',
            width:200,
            dataIndex:'accessMethod',
            sortable:true
        },{
            header:'访问用户',
            width:100,
            dataIndex:'accessPerson',
            sortable:true
        },{
            header:'访问IP',
            dataIndex:'accessIp'
        },{
            header:'访问时间',
            width:150,
            dataIndex:'accessTime'
        },{
            header:'访问路径',
            width:300,
            dataIndex:'operate'
        },{
            header:'备注',
            width:300,
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
        },{
            text:'下载',
            iconCls:ipe.sty.down,
            tooltip:'Log4j日志文件打包下载',
            scope:this,
            handler:this.downLogs
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'log/buslist',
                extraParams:{'a':'L2'},
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

    },downLogs:function(){
        window.location.href="log/downlogs";
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
            	{fieldLabel:'访问方法',xtype:'textfield',name:'accessMethod'},
            	{layout: {type: 'table',columns:3},xtype:'container',frame:true,items:[
            		{fieldLabel:'访问时间',name:'accessTimeStart',xtype:'datefield',format:'Y-m-d',width:200},
            		{text:'至',xtype:'label'},
            		{xtype:'datefield',name:'accessTimeEnd',format:'Y-m-d',width:100}
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

Ext.define('Sys.log.BusinessMainPanel',{
    extend:'Ipe.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
    	this.queryForm=Ext.create('Sys.log.QueryForm',{parent:this,region:'north',height:70,hidden:true});
        this.busLogList=Ext.create('Sys.log.BusLogList',{parent:this,region:'center'});
        this.items=[this.queryForm,this.busLogList];
        this.callParent();
    }
});
