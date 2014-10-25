/**
 * 用户表格列表
 */
Ext.define('Sys.role.RoleList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.rolelist',
    //title : '用户列表',
    viewConfig : {
        loadingText : '正在加载用户列表'
    },
    pageSize : 20,
    enabledSearch:true,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'角色名',
            dataIndex:'roleName',
            sortable:true
        },{
            header:'状态',
            dataIndex:'enabled',
            sortable:true,
            width:40,
            renderer:ipe.fuc.enabledDt
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
            handler:this.addRole
        },{
            text:'修改',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editRole
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delRole
        },'-',{
            text:'配置权限',
            iconCls:ipe.sty.set,
            scope:this,
            handler:this.setPermission
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'role/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : [
                { name: 'id', type: 'string' },
                { name: 'roleName', type: 'string' },
                { name: 'createdDate', type: 'string' },
                { name: 'enabled', type: 'string' },
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
    },delRole:function(){
        var me=this;
        var parent=this.up();
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
                            url: 'role/del',
                            params: {
                                id:record[0].data.id
                            },
                            success: function(response){
                                var resp =Ext.decode(response.responseText) ;
                                if(resp.success){
                                    parent.roleList.getStore().load();
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
    },addRole:function(){
        var parent=this.up();
        //parent.roleForm.collapse();
        parent.roleForm.expand(true);
        parent.roleForm.show();
        parent.roleForm.getForm().reset();
        parent.roleForm.setTitle("新增角色");
        parent.roleForm.oper="add";
    },editRole:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var parent=this.up();
            parent.roleForm.expand(true);
            parent.roleForm.show();
            parent.roleForm.setTitle("编辑角色");
            parent.roleForm.oper="edit";
            parent.roleForm.setData(record[0]);
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },setPermission:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var win=Ext.create('Sys.role.RoleAuthoritySetWin',{parent:this,title:'权限配置',record:record[0]});
            win.show();
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    }
});


/**
 * 编辑角色
 */
Ext.define('Sys.role.RoleForm',{
    extend:'Ext.form.Panel',
    url:'role/add',
    waitTitle:'请稍候....',
    defaults:{
        anchor:'98%'
    },
    defaultType: 'textfield',
    frame:true,
    bodyPadding: 5,
    initComponent:function(){
        this.items=[{
            fieldLabel:'角色名称',name:'roleName',allowBlank:false
        },{
            fieldLabel:'状态',xtype:'combo',name:'enabled',store:ipe.store.enabledStore,value:'1',
            displayField:'value',valueField:'key',hiddenName:'enabled',triggerAction:'all',editable:false,mode:'local'
        },{
            fieldLabel:'备注', name:'remark',xtype:'textarea'
        },{
            name:'id', xtype:'hidden'
        }];
        this.buttons=[{
            text:'保存',
            iconCls:ipe.sty.save,
            scope:this,
            handler:this.saveData
        },{
            text:'取消',
            iconCls:ipe.sty.cancel,
            scope:this,
            handler:function(){
                this.getForm().reset();
                this.hide();
            }
        }];
        this.callParent();
    },setData:function(record){
        this.loadRecord(record);
    },saveData:function(){
        var me=this;
        var parent=this.up();
        if(this.oper=='add'){
            if(me.getForm().isValid()){
                //me.getForm().findField('id').setValue(null);//清除主键
                me.getForm().submit({
                    success: function(form, action) {
                        me.getForm().reset();
                        Ext.Msg.alert('提示', '保存成功');
                        parent.roleList.getStore().reload();
                        parent.roleList.getSelectionModel().clearSelections(false);
                        me.hide();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '保存失败');
                    }});
            }else{
                Ext.Msg.alert('提示','必填项未填或输入值不能通过校验!');
            }
        }else if(this.oper=='edit'){
            if(me.getForm().isValid()){
                me.getForm().submit({
                    url:'role/edit',
                    success: function(form, action) {
                        Ext.Msg.alert('提示', '修改成功');
                        parent.roleList.getStore().load();
                        parent.roleList.getSelectionModel().deselectAll();
                        me.hide();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '修改失败');
                    }});
            }else{
                Ext.Msg.alert('提示','必填项未填或输入值不能通过校验!');
            }
        }
    }
});

/**
 * 组合-用户管理首页
 */
Ext.define('Sys.role.RoleMainPanel',{
    extend:'Ext.Panel',
    //bodyPadding: 5,
    layout:'border',
    initComponent:function(){
        this.roleList=Ext.create('Sys.role.RoleList',{region:'center'});
        this.roleForm=Ext.create('Sys.role.RoleForm',{region:'east',width:300,minWidth:300,hidden:true,split:true})
        this.items=[this.roleList,this.roleForm];
        this.callParent();
    }
});