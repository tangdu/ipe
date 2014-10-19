Ext.define('Bpm.TaskProxyList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.taskProxylist',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载代理数据列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'代理人',
            width:150,
            dataIndex:'userForm',
            renderer:function(val){
               return val.userName;
            },
            sortable:true
        },{
            header:'委托人',
            width:150,
            dataIndex:'userTo',
            renderer:function(val){
                return val.userName;
            },
            sortable:true
        },{
            header:'代理原因',
            width:200,
            dataIndex:'reason'
        },{
            header:'开始时间',
            width:150,
            dataIndex:'startDate'
        },{
            header:'结束时间',
            width:150,
            dataIndex:'endDate'
        },{
            header:'状态',
            width:80,
            dataIndex:'status',
            renderer:function(val){
                if("1"==val){
                    return "启用";
                }else{
                    return "禁用";
                }
            }
        },{
            header:'创建日期',
            width:150,
            dataIndex:'createdDate'
        }];

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addTaskProxy
        },{
            text:'编辑',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editTaskProxy
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delTaskProxy
        },{
            text:'启用/禁用',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.udpateStatus
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'taskProxy/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : ['id','userForm','userTo','status','createdDate','reason','startDate','endDate']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize
        });

        this.callParent();
    },addTaskProxy:function(){
        var parent=this.parent;
        parent.taskProxyForm.expand(true);
        parent.taskProxyForm.show();
        parent.taskProxyForm.getForm().reset();
        parent.taskProxyForm.setTitle("新增代理");
        parent.taskProxyForm.oper="add";
        parent.taskProxyForm.getForm().findField("userFormName").setValue(user.userName);
        parent.doLayout();
    },editTaskProxy:function(){
        var parent=this.parent;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            parent.taskProxyForm.expand(true);
            parent.taskProxyForm.show();
            parent.taskProxyForm.setTitle("编辑代理");
            parent.taskProxyForm.oper="edit";
            parent.taskProxyForm.setData(record[0]);
            parent.doLayout();
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },delTaskProxy:function(){
        var me=this;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            Ext.Msg.show({
                title:'提示',
                msg: '你确认删除此记录?',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope:this,
                fn:function(bt){
                    if(bt=='yes'){
                        Ext.Ajax.request({
                            url: 'taskProxy/del',
                            params: {
                                ids:record[0].data.id
                            },
                            success: function(response){
                                var resp =Ext.decode(response.responseText) ;
                                if(resp.success){
                                    me.getStore().load();
                                }else{
                                    Ext.Msg.alert('提示',resp.rows);
                                }
                            }
                        });
                    }
                }
            });
        }else{
            Ext.Msg.alert('提示','请选择要删除的记录!');
        }
    },udpateStatus:function(){
        var me=this;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            Ext.Msg.show({
                title:'提示',
                msg: '你确认启用/禁用此记录?',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope:this,
                fn:function(bt){
                    if(bt=='yes'){
                        Ext.Ajax.request({
                            url: 'taskProxy/udpateStatus',
                            params: {
                                ids:record[0].data.id
                            },
                            success: function(response){
                                var resp =Ext.decode(response.responseText) ;
                                if(resp.success){
                                    me.getStore().load();
                                }else{
                                    Ext.Msg.alert('提示',resp.rows);
                                }
                            }
                        });
                    }
                }
            });
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    }
});

Ext.define('Bpm.TaskProxyEditForm',{
    extend:'Ext.FormPanel',
    url:'taskProxy/add',
    waitTitle:'请稍候....',
    defaults:{
        anchor:'98%'
    },
    frame:true,
    plain:true,
    bodyPadding: 5,
    border:false,
    defaultType:'textfield',
    initComponent:function(){
        this.items=[{
            xtype:'textfield',
            fieldLabel:'代理人',
            readOnly:true,
            name:'userFormName',
            cls:'textReadOnly'
        },{
            layout:'column',
            xtype: 'container',
            anchor:'100%',
            defaults:{
                border:false,
                frame:true
            },
            items:[
               {fieldLabel:'委托人',xtype:'textfield',name:'userToName'},{xtype:'hidden',name:'userToId'},
               {xtype:'button',text:'选择',scope:this,handler:this.showUserChooseWin}
            ]
        },{
            fieldLabel:'代理原因',
            maxLength:20,
            xtype:'textarea',
            allowBlank:false,
            name:'reason'
        },{
            fieldLabel:'开始日期',
            xtype:'datefield',
            allowBlank:false,
            format:'Y-m-d',
            name:'startDate'
        },{
            fieldLabel:'结束日期',
            xtype:'datefield',
            allowBlank:false,
            format:'Y-m-d',
            name:'endDate'
        },{xtype:'hidden',name:'id'}];

        this.buttons=[{
            text:'保存',
            iconCls:ipe.sty.save,
            scope:this,
            handler:this.saveData
        },{
            text:'取消',
            iconCls:ipe.sty.cancel,
            scope:this,
            handler:this.hide
        }];

        this.callParent();
    },showUserChooseWin:function(){
        var win=Ext.create('Sys.user.UserChooseWin',{userId:this.getForm().findField("userToId"),userName:this.getForm().findField("userToName")});
        win.show();
    },setData:function(record){
        this.getForm().findField("userToId").setValue(record.data.userTo.id);
        this.getForm().findField("userToName").setValue(record.data.userTo.userName);
        this.getForm().findField("userFormName").setValue(record.data.userForm.userName);
        this.loadRecord(record);
    },saveData:function(){
        var me=this;
        if(this.getForm().isValid()){
            if(this.oper=="add"){
                this.getForm().submit({
                    success:function(re){
                        Ext.Msg.alert('提示', '保存成功');
                        me.parent.taskProxyList.getStore().load();
                        me.hide();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '保存失败');
                    }
                });
            }else if(this.oper=="edit"){
                this.getForm().submit({
                    url:'taskProxy/edit',
                    success:function(re){
                        Ext.Msg.alert('提示', '修改成功');
                        me.parent.taskProxyList.getStore().load();
                        me.parent.taskProxyList.getSelectionModel().deselectAll();
                        me.hide();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '修改失败');
                    }
                });
            }
        }else{
            Ext.Msg.alert('提示','必填项为空或是输入值受限！');
        }
    }
});

Ext.define('Bpm.TaskProxyMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.taskProxyList=Ext.create('Bpm.TaskProxyList',{parent:this,region:'center'});
        this.taskProxyForm=Ext.create('Bpm.TaskProxyEditForm',{parent:this,region:'east',width:400,hidden:true,split:true});
        this.items=[this.taskProxyList,this.taskProxyForm];
        this.callParent();
    }
});