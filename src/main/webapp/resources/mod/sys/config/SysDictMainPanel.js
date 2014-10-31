
Ext.define('Sys.config.DictList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.dictlist',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载字典列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    enabledSearch:true,
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'名称',
            width:200,
            dataIndex:'dictName',
            sortable:true
        },{
            header:'代码',
            width:150,
            dataIndex:'dictCode',
            sortable:true
        },{
            header:'备注',
            width:300,
            dataIndex:'remark'
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'dict/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : ['id','dictName','dictCode','remark','status']
        });

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addDict
        },{
            text:'编辑',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editDict
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delDict
        },'->',{
        	xtype:'searchfield',
            emptyText:'名称/编号查询',
            fieldLabel:'查询:',
            labelWidth:30,
            scope:this,
            name:'query',
            store:this.store,
            width:150,
            handler:this.searchUser
        },{width:ipe.config.paddingWidth,xtype:'tbspacer'}];

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize,parent:this
        });

        this.on('select',this.loadDictVal,this);
        this.store.on('beforeload',this.load,this);
        this.callParent();
    },load:function(s){
        if(this.searchInput && this.searchField
            && this.searchInput.getValue()!=""
            && this.searchField.getValue()!=""){
            var params={};
            params[this.searchField.getValue()]=this.searchInput.getValue();
            Ext.apply(s.proxy.extraParams,params);
        }
    },addDict:function(){
        var win=Ext.create('Sys.config.DictWindow',{parent:this,title:'新增键',oper:'add'});
        win.show();
    },editDict:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var win=Ext.create('Sys.config.DictWindow',{parent:this,title:'修改键',oper:'edit'});
            win.show();
            win.setData(record[0]);
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },delDict:function(){
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
                            url: 'dict/del',
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
    },loadDictVal:function(grid,record){
        var store=this.parent.dictValList.getStore();
        Ext.apply(store.proxy.extraParams,{dictId:record.data.id});
        store.load();
    }
});

Ext.define('Sys.config.DictValList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.dictvallist',
    viewConfig : {
        loadingText : '正在加载字典值列表'
    },
    sorters: [{
        property: 'dictValCode',
        direction: 'ASC'
    }],
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'名称',
            dataIndex:'dictValName',
            width:200,
            sortable:true
        },{
            header:'代码',
            width:150,
            dataIndex:'dictValCode',
            sortable:true
        },{
            header:'备注',
            dataIndex:'remark'
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'dictVal/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            fields : ['id','dictValName','dictValCode','remark','dictId']
        });
        
        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addDitVal
        },{
            text:'编辑',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editDitVal
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delDitVal
        },'->',{
        	xtype:'searchfield',
            emptyText:'名称/编号查询',
            fieldLabel:'查询:',
            labelWidth:30,
            scope:this,
            name:'query',
            store:this.store,
            width:150,
            handler:this.searchUser
        },{width:ipe.config.paddingWidth,xtype:'tbspacer'}];

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize
        });

        this.callParent();
    },addDitVal:function(){
        var record=this.parent.dictList.getSelectionModel().getSelection();
        if(record && record.length>0){
            var win=Ext.create('Sys.config.DictValWindow',{parent:this,title:'新增值',oper:'add'});
            win.show();
            win.setDictInfo(record[0].data);
        }else{
            Ext.Msg.alert('提示','请选择左侧字典类型后添加!');
        }
    },editDitVal:function(){
    	var precord=this.parent.dictList.getSelectionModel().getSelection();
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            var win=Ext.create('Sys.config.DictValWindow',{parent:this,title:'修改值',oper:'edit'});
            win.show();
            win.setDictInfo(precord[0].data);
            win.setData(record[0]);
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },delDitVal:function(){
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
                            url: 'dictVal/del',
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


Ext.define('Sys.config.DictWindow',{
    extend:'Ext.Window',
    layout:'fit',
    width:380,
    height:230,
    closeAction:'close',
    modal:true,
    border:false,
    initComponent:function(){
        this.form=Ext.create('Ext.FormPanel',{
           url:'dict/add',
           frame:true,
           defaults:{
            	anchor:'98%'
            },
           defaultType:'textfield',
           items:[{
               fieldLabel:'名称',
               maxLength:50,
               anchor:'98%',
               allowBlank:false,
               name:'dictName'
           },{
               fieldLabel:'代码',
               maxLength:20,
               anchor:'98%',
               allowBlank:false,
               name:'dictCode'
           },{
               fieldLabel:'备注',
               maxLength:200,
               anchor:'98%',
               xtype:'textarea',
               name:'remark'
           },{xtype:'hidden',name:'id'}]
        });
        this.items=[this.form];
        this.buttons=[
            {text:'保存',scope:this,handler:this.saveData,iconCls:ipe.sty.save},
            {text:'取消',scope:this,handler:this.close,iconCls:ipe.sty.cancel}];
        this.callParent();
    },setData:function(record){
        this.form.loadRecord(record);
    },saveData:function(){
        var me=this;
        if(this.form.getForm().isValid()){
            if(this.oper=="add"){
                this.form.getForm().submit({
                    success:function(re){
                        Ext.Msg.alert('提示', '保存成功');
                        me.parent.getStore().load();
                        me.close();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '保存失败');
                    }
                });
            }else if(this.oper=="edit"){
                this.form.getForm().submit({
                    url:'dict/edit',
                    success:function(re){
                        Ext.Msg.alert('提示', '修改成功');
                        me.parent.getStore().load();
                        me.parent.getSelectionModel().deselectAll();
                        me.close();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '修改失败');
                    }
                });
            }
        }
    }
});

Ext.define('Sys.config.DictValWindow',{
    extend:'Ext.Window',
    layout:'fit',
    width:380,
    height:250,
    closeAction:'close',
    border:false,
    modal:true,
    initComponent:function(){
        this.form=Ext.create('Ext.FormPanel',{
            url:'dictVal/add',
            frame:true,
            defaults:{
            	anchor:'98%'
            },
            defaultType:'textfield',
            items:[{
                fieldLabel:'父节点',
                readOnly:true,
                name:'dictName'
            },{
                fieldLabel:'名称',
                maxLength:50,
                allowBlank:false,
                name:'dictValName'
            },{
                fieldLabel:'代码',
                maxLength:20,
                allowBlank:false,
                name:'dictValCode'
            },{
                fieldLabel:'备注',
                maxLength:200,
                xtype:'textarea',
                name:'remark'
            },{xtype:'hidden',name:'id'},{xtype:'hidden',name:'dictId'}]
        });
        this.items=[this.form];
        this.buttons=[
            {text:'保存',scope:this,handler:this.saveData,iconCls:ipe.sty.save},
            {text:'取消',scope:this,handler:this.close,iconCls:ipe.sty.cancel}];
        this.callParent();
    },setDictInfo:function(data){
        this.form.getForm().findField('dictId').setValue(data.id);
        this.form.getForm().findField('dictName').setValue(data.dictName);
    },setData:function(record){
        this.form.loadRecord(record);
    },saveData:function(){
        var me=this;
        if(this.form.getForm().isValid()){
            if(this.oper=="add"){
                this.form.getForm().submit({
                    success:function(re){
                        Ext.Msg.alert('提示', '保存成功');
                        me.parent.getStore().load();
                        me.close();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '保存失败');
                    }
                });
            }else if(this.oper=="edit"){
                this.form.getForm().submit({
                    url:'dictVal/edit',
                    success:function(re){
                        Ext.Msg.alert('提示', '修改成功');
                        me.parent.getStore().load();
                        me.parent.getSelectionModel().deselectAll();
                        me.close();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '修改失败');
                    }
                });
            }
        }
    }
});


Ext.define('Sys.config.SysDictMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.dictList=Ext.create('Sys.config.DictList',{parent:this,region:'center'});
        this.dictValList=Ext.create('Sys.config.DictValList',{parent:this,region:'east',width:500,split:true});
        this.items=[this.dictList,this.dictValList];
        this.callParent();
    }
});
