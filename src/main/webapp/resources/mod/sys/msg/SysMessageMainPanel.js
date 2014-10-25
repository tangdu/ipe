/**
 * Created by tangdu on 14-2-15.
 */
Ext.define('Sys.msg.MessageList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.Messagelist',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载消息列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'内容',
            width:'60%',
            dataIndex:'msgContent',
            sortable:true
        },{
            header:'消息类型',
            width:80,
            dataIndex:'msgType'
        },{
            header:'标记',
            width:40,
            dataIndex:'read',
            renderer:ipe.fuc.msgDt
        },{
            header:'创建日期',
            width:150,
            dataIndex:'createdDate'
        }];

        this.tbar=[{text:'浏览',iconCls:ipe.sty.view,handler:this.readMsg,scope:this}];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'message/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : ['id','userId','msgContent','msgType','createdDate','read']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize
        });

        this.callParent();
    },readMsg:function(){
    	var parent=this.parent;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
        	var win=Ext.create('Sys.msg.MessageViewWin',{parent:this,data:record[0].data});
    		win.show();
        }else{
        	 Ext.Msg.alert('提示','请选择要查看的记录!');
        }
    }
});

Ext.define('Sys.msg.SysMessageMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'fit',align:'stretch'},
    initComponent:function(){
        this.messageList=Ext.create('Sys.msg.MessageList',{parent:this,region:'center'});
        this.items=[this.messageList];
        this.callParent();
    }
});