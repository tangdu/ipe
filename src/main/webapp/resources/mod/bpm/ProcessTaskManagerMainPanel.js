/**
 *流程任务管理页面
 **/

Ext.define('Bpm.ProcessTodoTaskList',{
    title:'待办任务',
    extend:'Ext.grid.Panel',
    alias : 'widget.todoTaskList',
    autoScroll : false,
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载任务列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'任务ID',
            dataIndex:'executionId'
        },{
            header:'任务名称',
            dataIndex:'name',
            width:150,
            sortable:true
        },{
            header:'节点描述',
            width:200,
            dataIndex:'description'
        },{
            header:'发送人',
            dataIndex:'user'
        },{
            header:'领取人',
            dataIndex:'assignee'
        },{
            header:'创建时间',
            width:200,
            dataIndex:'createTime'
        }];

        this.tbar=[{
            text:'处理',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.getTask
        },{
            text:'指派',
            iconCls:ipe.sty.branch,
            scope:this,
            handler:this.taskDelegate
        },{
            text:'释放',
            iconCls:ipe.sty.undo,
            scope:this,
            handler:this.releaseTask
        },'-',{
            text:'流程图',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.viewDnyDiagram
        }];
        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'bpm/task/my_task_list',
                reader: {
                    root: 'rows'
                }
            },
            autoLoad:true,
            remoteSort : true,
            fields : ['name','description','priority','user','processInstanceId','executionId','createTime','assignee']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{store:this.store,pageSize:this.pageSize});
        this.callParent();
    },getTask:function(){

    },taskDelegate:function(){

    },releaseTask:function(){

    },viewDnyDiagram:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            viewDnyDiagram(record[0]);
        }else{
            Ext.Msg.alert("提示", "请先选择一条数据");
        }
    }
});


Ext.define('Bpm.ProcessReceiveTaskList',{
    title:'待领取任务',
    extend:'Ext.grid.Panel',
    alias : 'widget.receiveTaskList',
    autoScroll : false,
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载任务列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'任务ID',
            dataIndex:'executionId'
        },{
            header:'任务名称',
            dataIndex:'name',
            width:150,
            sortable:true
        },{
            header:'节点描述',
            width:200,
            dataIndex:'description'
        },{
            header:'申请人',
            dataIndex:'assignee'
        },{
            header:'创建时间',
            width:200,
            dataIndex:'createTime'
        }];

        this.tbar=[{
            text:'处理',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.getTask
        },'-',{
            text:'流程图',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.viewDnyDiagram
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'bpm/task/get_task_list',
                reader: {
                    root: 'rows'
                }
            },
            autoLoad:true,
            remoteSort : true,
            fields : ['name','description','priority','processInstanceId','executionId','createTime','assignee']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{store:this.store,pageSize:this.pageSize});
        this.callParent();
    },getTask:function(){

    },viewDnyDiagram:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            viewDnyDiagram(record[0]);
        }else{
            Ext.Msg.alert("提示", "请先选择一条数据");
        }
    }
});

Ext.define('Bpm.ProcessTaskManagerMainPanel',{
    extend:'Ext.Panel',
    layout:'border',
    initComponent:function(){
        this.todoTaskList=Ext.create('Bpm.ProcessTodoTaskList',{parent:this,region:'center'});
        this.reciveTasklist=Ext.create('Bpm.ProcessReceiveTaskList',{parent:this,region:'south',height:200,split:true,collapseMode:'omitted',collapsible:true});
        this.items=[this.todoTaskList,this.reciveTasklist];
        this.callParent();
    }
});