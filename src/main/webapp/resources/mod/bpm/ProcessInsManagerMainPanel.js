/**
 *流程管理页面
 **/

Ext.define('Bpm.ProcessInstanceList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.processMlist',
    //title : '角色列表',
    autoScroll : false,
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载流程列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'流程实例ID',
            dataIndex:'id'
        },{
            header:'流程定义名称',
            dataIndex:'processName',
            width:150,
            sortable:true
        },{
            header:'业务关键字',
            dataIndex:'businessKey',
            width:150
        },{
            header:'流程定义ID',
            width:150,
            dataIndex:'processDefinitionId'
        },{
            header:'启动者',
            dataIndex:'startUserId'
        },{
            header:'当前状态',
            dataIndex:'status'
        },{
            header:'启动时间',
            dataIndex:'startTime',
            width:200
        },{
            header:'启动节点ID',
            dataIndex:'startActivityId'
        },{
            header:'变量',
            dataIndex:'processVariables',
            width:300
        }];

        this.tbar=[{
            text:'重置',
            iconCls:ipe.sty.reset,
            scope:this,
            handler:this.deploy
        }];
        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'bpm/process_ins_list',
                reader: {
                    root: 'rows'
                }
            },
            autoLoad:true,
            remoteSort : true,
            fields : ['id','name','businessKey','processName','status','processDefinitionId',
                'startTime','endTime','durationInMillis','startUserId','startActivityId','deleteReason','superProcessInstanceId','processVariables']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{store:this.store,pageSize:this.pageSize});
        this.callParent();
    }
});


Ext.define('Bpm.ProcessInsManagerMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'hbox',align:'stretch'},
    initComponent:function(){
        this.processInsList=Ext.create('Bpm.ProcessInstanceList',{parent:this,flex:7});
        this.items=[this.processInsList];
        this.callParent();
    }
});