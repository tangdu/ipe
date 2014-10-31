Ext.define('Sys.model.OrgModel',{
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id',type:'string'},
        {name : 'orgName',type:'string'},
        {name : 'orgCode',type:'string'},
        {name : 'orgLevel',type:'int'},
        {name : 'parent'},
        {name : 'remark',type:'string'},
        {name : 'sno',type:'int'}
    ],
    proxy : {
        type: 'ajax',
        url: 'org/getTree',
        reader: {
            root: 'rows'
        }
    }
});

/**
 * 部门或机构查询树
 */
Ext.define('Sys.org.OrgTreePanel',{
    //title:'机构',
    //collapsible : true,
    layoutConfig : {
        animate : true
    },
    rootVisible:false,
    split : true,
    extend:'Ext.tree.Panel',
    initComponent:function(){
        this.store= Ext.create('Ext.data.TreeStore', {
            //nodeParam:'pid',
            model:'Sys.model.OrgModel',
            folderSort: true,
            root: {
                text: '根节点',
                id:'root',
                expanded: true
            },
            listeners:{
                'beforeload':function(store,oper){
                    var record=oper.node.data;
                    if(record.id!='root'){
                        oper.params.pid=record.id;
                    }
                }
            }
        });

        this.columns=[{xtype: 'rownumberer'},{
            text:'机构编号',dataIndex:'orgCode',xtype:'treecolumn',width:300
        },{
            text:'机构名称',dataIndex:'orgName'
        },{
            text:'机构层级',dataIndex:'orgLevel'
        },{
            text:'父节点',dataIndex:'parent',renderer:function(obj){
            	if(obj){
            		return obj.orgName;
            	}
            }
        }];
        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addOrg
        },{
            text:'修改',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editOrg
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delOrg
        }];
        this.callParent();
    },
    addOrg:function(){
 		var me=this;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var parent=this.up();
            parent.orgForm.expand(true);
            parent.orgForm.show();
            parent.orgForm.getForm().reset();
            parent.orgForm.setTitle("新增机构");
            parent.orgForm.oper="add";
            parent.orgForm.setData(record[0]);
            parent.orgForm.setInfo(record[0]);
            parent.doLayout();
        }else{
            Ext.Msg.alert('提示','请选择父资源记录!');
        }
    },
    editOrg:function(){
		var me=this;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var parent=this.up();
            parent.orgForm.expand(true);
            parent.orgForm.show();
            parent.orgForm.getForm().reset();
            parent.orgForm.setTitle("修改机构");
            parent.orgForm.oper="edit";
            parent.orgForm.setData(record[0]);
            parent.orgForm.setInfo(record[0]);
            parent.doLayout();
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },
    delOrg:function(){
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
                            url: 'org/del',
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
    }
});

Ext.define('Sys.org.OrgEditForm',{
    extend:'Ext.form.Panel',
    url:'org/add',
    waitTitle:'请稍候....',
    defaults:{
        anchor:'90%',
        border:'5px'
    },
    frame:true,
    initComponent:function(){
        this.items=[{
            fieldLabel:'机构编码',name:'orgCode',xtype:'textfield',allowBlank:false
        },{
            fieldLabel:'机构名称', name:'orgName',xtype:'textfield',allowBlank:false
        },{
            fieldLabel:'机构层级', name:'orgLevel',xtype:'textfield',allowBlank:false,readOnly:true
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
    },setInfo:function(re){
    	if(this.oper=="add"){
    		this.getForm().setValues({'parent.id':re.data.id});
    		this.getForm().setValues({'orgLevel':re.data.orgLevel+1});
    	}else if(this.oper=="edit"){
    		this.getForm().setValues({'parent.id':re.data.parent.id});
    		this.getForm().setValues({'orgLevel':re.data.orgLevel});
    	}
    },setData:function(record){
        this.loadRecord(record);
    },saveData:function(){
        var me=this;
        var parent=this.up();
        if(this.oper=='add'){
            if(me.getForm().isValid()){
                me.getForm().submit({
                    success: function(form, action) {
                        Ext.Msg.alert('提示', '保存成功');
                        parent.orgTree.getStore().load();
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
                    url:'org/edit',
                    success: function(form, action) {
                        Ext.Msg.alert('提示', '修改成功');
                        parent.orgTree.getStore().load();
                        parent.orgTree.getSelectionModel().deselectAll();
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


Ext.define('Sys.org.OrgMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.orgTree=Ext.create('Sys.org.OrgTreePanel',{parent:this,region:'center'});
        this.orgForm=Ext.create('Sys.org.OrgEditForm',{parent:this,hidden:true,split:true,region:'east',width:300});
        this.items=[this.orgTree,this.orgForm];
        this.callParent();
    }
});
