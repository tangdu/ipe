/**
 * Created by tangdu on 14-2-16.
 */

Ext.define('Sys.user.UserChooseList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.userChooselist',
    autoScroll : false,//TODO
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载用户列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.selModel= new Ext.selection.CheckboxModel({mode :this.usingle ? 'MULTI':'SINGLE'});

        this.columns=[{xtype: 'rownumberer'},{
            header:'用户名',
            dataIndex:'userName',
            sortable:true
        },{
            header:'帐号',
            dataIndex:'userAccount',
            sortable:true
        },{
            header:'状态',
            dataIndex:'enabled',
            sortable:true,
            renderer:ipe.fuc.enabledDt
        },{
            header:'是否超级管理员',
            dataIndex:'admin',
            sortable:true,
            renderer:ipe.fuc.flagDt
        },{
            header:'修改时间',
            dataIndex:'updatedDate',
            width:150
        },{
            header:'备注',
            dataIndex:'remark',
            width:300
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'user/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : [
                { name: 'id', type: 'string' },
                { name: 'userName', type: 'string' },
                { name: 'userAccount', type: 'string' },
                { name: 'enabled', type: 'string' },
                { name: 'admin', type: 'string' },
                { name: 'remark', type: 'string' },
                { name: 'updatedDate', type: 'string' }
            ]
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,
            pageSize:this.pageSize
        });
        this.callParent();
    }
});

Ext.define('Sys.user.UserChooseWin',{
    width:700,
    height:500,
    extend:'Ext.Window',
    closeAction:'hide',
    alias:'widget.userChooseWin',
    modal:true,
    border:false,
    title:'用户选择',
    layout:{type:'border'},
    initComponent:function(){
        this.buttons=[{text:'选择',scope:this,handler:this.getChoose,iconCls:ipe.sty.submit},{text:'取消',handler:this.hide,scope:this,iconCls:ipe.sty.cancel}];

        this.userList=Ext.create('Sys.user.UserChooseList',{parent:this,region:'center',usingle:this.usingle});
        this.orgPanel=Ext.create('Ext.Panel',{parent:this,region:'west',width:200,split:true});

        this.items=[this.userList,this.orgPanel];
        this.callParent();
    },getChoose:function(){
        var record=this.userList.getSelectionModel().getSelection();
        if(record && record.length>0){
           if(this.userId!=null && this.userName!=null){
               this.userName.setValue(record[0].data.userName);
               this.userId.setValue(record[0].data.id);
               this.hide();
           }
        }else{
            Ext.Msg.alert('提示','请选择记录!');
        }
    }
});