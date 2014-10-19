/**
 *流程管理页面
 **/

/**
 * 我发起的流程
 */
Ext.define('Bpm.UserStartInstanceList',{
    extend:'Ext.grid.Panel',
    title : '我发起流程',
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
            text:'浏览',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.deploy
        }];
        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'bpm/my_start_process_ins_list',
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

/**
 * 我参与的流程
 */
Ext.define('Bpm.UserPartakeInstanceList',{
    extend:'Ext.grid.Panel',
    title : '我参与的流程',
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
            text:'浏览',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.deploy
        }];
        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'bpm/my_partake_process_ins_list',
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

Ext.define('Bpm.UserProcessInsManagerMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.userStartInstanceList=Ext.create('Bpm.UserStartInstanceList',{parent:this,region:'center'});
        this.userPartakeInstanceList=Ext.create('Bpm.UserPartakeInstanceList',{parent:this,region:'south',height:300,split:true});
        this.items=[this.userStartInstanceList,this.userPartakeInstanceList];
        this.callParent();
    }
});