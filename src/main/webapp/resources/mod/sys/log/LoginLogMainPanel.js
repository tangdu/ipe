Ext.define('Sys.config.LogList',{
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
            width:150,
            dataIndex:'accessPerson',
            sortable:true
        },{
            header:'日志类型',
            dataIndex:'logType',
            width:70,
            renderer:ipe.fuc.logTypeDt
        },{
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
            text:'浏览',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.viewData
        },{
            text:'下载',
            iconCls:ipe.sty.down,
            tooltip:'日志文件打包下载',
            scope:this,
            handler:this.downLogs
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

    },downLogs:function(){
        window.location.href="log/downlogs";
    }
});

Ext.define('Sys.log.LoginLogMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.logList=Ext.create('Sys.config.LogList',{parent:this,region:'center'});
        this.items=[this.logList];
        this.callParent();
    }
});
