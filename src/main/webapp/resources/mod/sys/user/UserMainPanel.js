/**
 * 用户表格列表
 */
Ext.define('Sys.user.UserList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.userlist',
    //title : '用户列表',
    viewConfig : {
        loadingText : '正在加载用户列表'
    },
    pageSize : 20,
    enabledSearch:true,
    initComponent:function(){
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
            width:40,
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

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addUser
        },{
            text:'修改',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editUser
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delUser
        },'-',{
            text:'重置密码',
            iconCls:ipe.sty.reset,
            scope:this,
            handler:this.recoverPwd
        },'-',{
            text:'角色配置',
            iconCls:ipe.sty.set,
            scope:this,
            handler:this.setRole
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
            pageSize:this.pageSize,
            parent:this
        });
        this.callParent();
    },delUser:function(){
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
                            url: 'user/del',
                            params: {
                               ids:record[0].data.id
                            },
                            success: function(response){
                                var resp =Ext.decode(response.responseText) ;
                                if(resp.success){
                                    me.getStore().load();
                                }else{
                                    ipe.Alert.show('提示',resp.rows);
                                }
                            }
                        });
                    }
                }
            });
        }else{
            ipe.Alert.show('提示','请选择要删除的记录!');
        }
    },addUser:function(){
        var parent=this.parent;
        parent.userForm.expand(true);
        parent.userForm.show();
        parent.userForm.getForm().reset();
        parent.userForm.setTitle("新增用户");
        parent.userForm.getForm().findField("userAccount").setReadOnly(false);
        parent.userForm.oper="add";
        parent.doLayout();
    },editUser:function(){
        var me=this;
        var parent=this.parent;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            parent.userForm.expand(true);
            parent.userForm.show();
            parent.userForm.setTitle("编辑用户");
            parent.userForm.oper="edit";
            parent.userForm.getForm().findField("userAccount").setReadOnly(true);
            parent.userForm.setData(record[0]);
            parent.doLayout();
        }else{
            ipe.Alert.show('提示','请选择要编辑的记录!');
        }
    },setRole:function(){
        var me=this;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var win=Ext.create('Sys.user.UserRoleSetWin',{parent:this,record:record[0]});
            win.setTitle(win.title+"->"+record[0].data.userName);
            win.show();
        }else{
            ipe.Alert.show('提示','请选择记录!');
        }
    },recoverPwd:function(){
        var me=this;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            Ext.Ajax.request({
                url: 'user/recoverPwd',
                params: {
                    userId:record[0].data.id
                },
                success: function(response){
                    var resp =Ext.decode(response.responseText) ;
                    if(resp.success){
                        Ext.Msg.alert('提示','重置成功!');
                    }else{
                        Ext.Msg.alert('提示',resp.rows);
                    }
                }
            });
        }else{
            ipe.Alert.show('提示','请选择记录!');
        }
    }
});


Ext.define('Sys.user.userEditForm',{
    extend:'Ext.form.Panel',
    url:'user/add',
    waitTitle:'请稍候',
    waitMsg:'正在提交....',
    defaults:{
        anchor:'98%'
    },
    frame:true,
    plain:true,
    bodyPadding: 5,
    border:false,
    defaultType: 'textfield',
    initComponent:function(){
        this.items=[{
            fieldLabel:'用户名',xtype:'textfield',name:'userName',allowBlank:false
        },{
            fieldLabel:'账号',xtype:'textfield',name:'userAccount',allowBlank:false
        },{
            fieldLabel:'状态',xtype:'combo',name:'enabled',store:ipe.store.enabledStore,value:'1',
            displayField:'value',valueField:'key',hiddenName:'enabled',triggerAction:'all',editable:false,queryMode:'local'
        },{
            fieldLabel:'是否超级管理员',xtype:'combo',name:'admin',store:ipe.store.flagStore,value:'0',
            displayField:'value',valueField:'key',hiddenName:'admin',triggerAction:'all',editable:false,queryMode:'local'
        },{
            fieldLabel:'备注',xtype:'textarea',name:'remark'
        },{xtype:'hidden',name:'id'}];

        this.buttons=[{
            text:'确定',
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
    },setData:function(record){
        this.loadRecord(record);
    },saveData:function(){
        var me=this;
        var parent=this.parent;
        if(this.oper=='add'){
            if(me.getForm().isValid()){
                me.getForm().submit({
                    success: function(form, action) {
                        ipe.Alert.show('提示', '保存成功');
                        parent.userList.getStore().load();
                        me.hide();
                    },
                    failure: function(form, action) {
                        ipe.Alert.show('提示', '保存失败');
                    }});
            }
        }else if(this.oper=='edit'){
            if(me.getForm().isValid()){
                me.getForm().submit({
                    url:'user/edit',
                    success: function(form, action) {
                        ipe.Alert.show('提示', '修改成功');
                        parent.userList.getStore().load();
                        parent.userList.getSelectionModel().deselectAll();
                        me.hide();
                    },
                    failure: function(form, action) {
                        ipe.Alert.show('提示', '修改失败');
                    }});
            }
        }
    }
});

/**
 * 组合-用户管理首页
 */
Ext.define('Sys.user.UserMainPanel',{
    extend:'Ext.Panel',
    //bodyPadding: 5,
    layout:'border',
    initComponent:function(){
        //this.orgTree=Ext.create('Sys.com.OrgTreePanel',{parent:this,flag:'1'});//浏览状态
        this.userForm=Ext.create('Sys.user.userEditForm',{parent:this,region:'east',width:400,hidden:true,split:true});
        this.userList=Ext.create('Sys.user.UserList',{parent:this,region:'center'});

        this.items=[this.userList,this.userForm];
        this.callParent();
    }
});