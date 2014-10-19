/**
 * Created by tangdu on 14-2-15.
 */
Ext.define('Sys.msg.RemindList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.remindlist',
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
            header:'消息标题',
            width:200,
            dataIndex:'rtitle',
            sortable:true
        },{
            header:'内容',
            width:250,
            dataIndex:'rcontent',
            sortable:true
        },{
            header:'类型',
            width:80,
            dataIndex:'rtype'
        },{
            header:'开始时间',
            width:150,
            dataIndex:'remindStDate'
        },{
            header:'结束时间',
            width:150,
            dataIndex:'remindEnDate'
        },{
            header:'创建日期',
            width:150,
            dataIndex:'createdDate'
        }];

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addRemind
        },{
            text:'编辑',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editRemind
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delRemind
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'remind/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : ['id','rtitle','rcontent','userId','rtype','createdDate','read','remindStDate','remindEnDate']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize
        });

        this.callParent();
    },addRemind:function(){
        var parent=this.parent;
        parent.remindForm.expand(true);
        parent.remindForm.show();
        parent.remindForm.getForm().reset();
        parent.remindForm.setTitle("新增提醒");
        parent.remindForm.oper="add";
        parent.doLayout();
    },editRemind:function(){
        var parent=this.parent;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            parent.remindForm.expand(true);
            parent.remindForm.show();
            parent.remindForm.setTitle("编辑提醒");
            parent.remindForm.oper="edit";
            parent.remindForm.setData(record[0]);
            parent.doLayout();
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },delRemind:function(){
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
                            url: 'remind/del',
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

Ext.define('Sys.msg.RemindEditForm',{
    extend:'Ext.FormPanel',
    url:'remind/add',
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
            fieldLabel:'消息标题',
            maxLength:50,
            allowBlank:false,
            name:'rtitle'
        },{
            fieldLabel:'消息内容',
            maxLength:20,
            xtype:'textarea',
            allowBlank:false,
            name:'rcontent'
        },{
            fieldLabel:'类型',
            maxLength:200,
            name:'rtype'
        },{
            fieldLabel:'提醒时段',
            maxLength:200,
            xtype:'datefield',
            format:'Y-m-d',
            name:'remindStDate'
        },{
            maxLength:200,
            fieldLabel:'至',
            xtype:'datefield',
            format:'Y-m-d',
            name:'remindEnDate'
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
    },setData:function(record){
        this.loadRecord(record);
    },saveData:function(){
        var me=this;
        if(this.getForm().isValid()){
            if(this.oper=="add"){
                this.getForm().submit({
                    success:function(re){
                        Ext.Msg.alert('提示', '保存成功');
                        me.parent.remindList.getStore().load();
                        me.hide();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '保存失败');
                    }
                });
            }else if(this.oper=="edit"){
                this.getForm().submit({
                    url:'remind/edit',
                    success:function(re){
                        Ext.Msg.alert('提示', '修改成功');
                        me.parent.remindList.getStore().load();
                        me.parent.remindList.getSelectionModel().deselectAll();
                        me.hide();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '修改失败');
                    }
                });
            }
        }
    }
});

Ext.define('Sys.msg.SysRemindMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.remindList=Ext.create('Sys.msg.RemindList',{parent:this,region:'center'});
        this.remindForm=Ext.create('Sys.msg.RemindEditForm',{parent:this,region:'east',width:400,hidden:true,split:true});
        this.items=[this.remindList,this.remindForm];
        this.callParent();
    }
});