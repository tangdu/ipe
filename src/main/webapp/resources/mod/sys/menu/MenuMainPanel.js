Ext.define('Sys.menu.MenuTreePanel',{
    //title:'菜单列表',
    region:'center',
    extend:'Ext.tree.Panel',
    //animate: true,
    rootVisible: false,
    multiSelect: false,
    //ingleExpand: true,
    reserveScrollbar: true,
    useArrows: true,
    viewConfig: {
        plugins: {
            ptype: 'treeviewdragdrop',
            containerScroll: true
        },
        listeners: {
            drop: function(node, data, dropRec, dropPosition) {
                //通过data得到parent，然后把childer全部全新
                var ids=[];
                var pid=data.records[0].parentNode.data.id;
                Ext.each(data.records[0].parentNode.childNodes,function(r){
                        ids.push(r.data.id);
                });
                if(pid=="root"){
                    Ext.Msg.alert('提示','禁止操作');
                    return;
                }
                Ext.Ajax.request({
                    url: 'menu/update',
                    params: {
                        ids:ids,
                        pid:pid
                    }
                });
            }
        }
    },
    initComponent:function(){
        this.store= Ext.create('Ext.data.TreeStore', {
            //nodeParam:'pid',
            proxy: {
                type: 'ajax',
                url: 'menu/getMenus',
                reader: {
                    root: 'rows'
                }
            },
            folderSort: true,
            root: {
                text: '根节点',
                id:'root',
                expanded: true
            },/*
            listeners:{
                'beforeload':function(store,oper){
                    var record=oper.node.data;
                    if(record.id!='root'){
                        oper.params.pid=record.id;
                    }
                }
            },*/
            fields:['menuName','menuType','id','parent','enabled','remark','sno','menuStyle','resource','menuUrl','resourceId']/*,
            listeners:{
                'beforeload':function(store,oper){
                    var record=oper.node.data;
                    if(record.id!='root'){
                        oper.params.pid=record.id;
                    }
                }
            }*/
        });

        this.columns=[{xtype: 'rownumberer'},{
            text:'菜单名称',dataIndex:'menuName',xtype:'treecolumn',width:300
        },{
            text:'菜单类型',dataIndex:'menuType',renderer:ipe.fuc.menuDt
        },{
            text:'菜单路径',dataIndex:'menuUrl',width:300
        },{
            text:'状态',dataIndex:'enabled',renderer:ipe.fuc.enabledDt
        },{
            text:'关联资源',dataIndex:'resource',renderer:function(val){
            	if(val){
            		return val.resourceName;
            	}
            }
        }];

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addMenu
        },{
            text:'修改',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editMenu
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delMenu
        }];

        this.callParent();
    },addMenu:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var parent=this.up();
            parent.menuForm.expand(true);
            parent.menuForm.show();
            parent.menuForm.getForm().reset();
            parent.menuForm.setTitle("新增菜单");
            parent.menuForm.oper="add";
            parent.menuForm.setInfo(record[0]);
        }else{
            Ext.Msg.alert('提示','请选择父菜单记录!');
        }
    },editMenu:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var parent=this.up();
            parent.menuForm.expand(true);
            parent.menuForm.show();
            parent.menuForm.setTitle("编辑菜单");
            parent.menuForm.oper="edit";
            parent.menuForm.setData(record[0]);
            parent.menuForm.setInfo(record[0]);
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },delMenu:function(){
        var me=this;
        var parent=this.up();
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            if(record[0].data.parentId=="root"){
                Ext.Msg.alert('提示','此节点不能删除');
                return;
            }

            Ext.Msg.show({
                title:'提示',
                msg: '你确认删除此记录?该操作不能回退！',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope:this,
                fn:function(bt){
                    if(bt=='yes'){
                        Ext.Ajax.request({
                            url: 'menu/del',
                            params: {
                                ids:record[0].data.id
                            },
                            success: function(response){
                                var resp =Ext.decode(response.responseText) ;
                                if(resp.success){
                                    parent.menuTree.getStore().load();
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
    },dropNode:function(){
        alert(0);
    }
});


Ext.define('Sys.menu.MenuForm',{
    extend:'Ext.form.Panel',
    url:'menu/add',
    defaults:{
        anchor:'98%',
        border:'5px'
    },
    frame:true,
    initComponent:function(){
        this.items=[{
            fieldLabel:'父节点',name:'parentMenuName',xtype:'textfield',readOnly:true
        },{
            fieldLabel:'菜单名称',name:'menuName',xtype:'textfield',allowBlank:false
        },{
            fieldLabel:'菜单类型',name:'menuType',allowBlank:false,xtype:'combo',store:ipe.store.menuStore,value:'1',
            displayField:'value',valueField:'key',hiddenName:'menuType',triggerAction:'all',editable:false,mode:'local'
        },{
         fieldLabel:'菜单路径', name:'menuUrl',xtype:'textarea'
         },{
            fieldLabel:'菜单样式', name:'menuStyle',xtype:'textfield'
        },{
            fieldLabel:'状态',xtype:'combo',name:'enabled',store:ipe.store.enabledStore,value:'1',
            displayField:'value',valueField:'key',hiddenName:'enabled',triggerAction:'all',editable:false,mode:'local'
        },{
            layout:'column',border:false,frame:true,xtype: 'container',items:[
                {fieldLabel:'关联资源',xtype:'textfield',name:'resourceName'},
                {xtype:'hidden',name:'resourceId'},
                {text:'选择',xtype:'button',scope:this,handler:this.chooseResource}]
        },{
            fieldLabel:'备注', name:'remark',xtype:'textarea'
        },{
            name:'id', xtype:'hidden'
        },{
            name:'parent.id', xtype:'hidden'
        },{
            name:'sno', xtype:'hidden'
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
    },setInfo:function(data){
    	if(this.oper=='add'){
    		this.getForm().setValues({'parent.id':data.data.id});
    		this.getForm().setValues({'parentMenuName':data.data.menuName});
    	}else{
    		if(data.parentNode.data.id!="root"){
    			this.getForm().setValues({'parent.id':data.parentNode.data.id});
    			this.getForm().setValues({'parentMenuName':data.parentNode.data.menuName});
    		}
    	}
    },setData:function(record){
        this.loadRecord(record);
        if(record.data.resource){
            this.getForm().findField('resourceName').setValue(record.data.resource.resourceName);
            this.getForm().findField('resourceId').setValue(record.data.resource.id);
        }
    },chooseResource:function(){
        var resourceName=this.getForm().findField('resourceName');
        var resourceId=this.getForm().findField('resourceId');
        var win=Ext.create('Sys.resource.ResourceChooseWin',{parent:this,nameField:resourceName,valueField:resourceId});
        win.show();
    },saveData:function(){
        var me=this;
        var parent=this.up();
        if(this.oper=='add'){
            if(me.getForm().isValid()){
                me.getForm().submit({
                    success: function(form, action) {
                        Ext.Msg.alert('提示', '保存成功');
                        parent.menuTree.getStore().load();
                        me.hide();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', action.result.rows);
                    }});
            }else{
                Ext.Msg.alert('提示','必填项未填或输入值不能通过校验!');
            }
        }else if(this.oper=='edit'){
            if(me.getForm().isValid()){
                me.getForm().submit({
                    url:'menu/edit',
                    success: function(form, action) {
                        Ext.Msg.alert('提示', '修改成功');
                        parent.menuTree.getStore().load();
                        parent.menuTree.getSelectionModel().deselectAll();
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


Ext.define('Sys.menu.MenuMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.menuTree=Ext.create('Sys.menu.MenuTreePanel',{parent:this,region:'center'});
        this.menuForm=Ext.create('Sys.menu.MenuForm',{parent:this,width:400,region:'east',hidden:true,split:true});
        this.items=[this.menuTree,this.menuForm];
        this.callParent();
    }
});