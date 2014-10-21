/**
 * Created by tangdu on 14-2-16.
 */
Ext.define('Sys.config.BusLogList',{
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
            text:'浏览',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.viewData
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

    }
});

Ext.define('Sys.log.BusinessMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.busLogList=Ext.create('Sys.config.BusLogList',{parent:this,region:'center'});
        this.items=[this.busLogList];
        this.callParent();
    }
});
