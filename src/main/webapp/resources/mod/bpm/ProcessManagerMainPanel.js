/**
 *流程管理页面
 **/

Ext.define('Bpm.ProcessDefList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.processMlist',
    //title : '角色列表',
    autoScroll : false,
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载流程列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'部署ID',
            dataIndex:'deploymentId'
        },{
            header:'流程定义ID',
            dataIndex:'id',
            width:150,
            sortable:true
        },{
            header:'名称',
            dataIndex:'name',
            width:150
        },{
            header:'版本',
            dataIndex:'version'
        },{
            header:'Key',
            dataIndex:'key'
        },{
            header:'有无表单',
            dataIndex:'startFormKey'
        },{
            header:'目录',
            dataIndex:'category',
            width:200
        },{
            header:'资源名称',
            dataIndex:'resourceName',
            width:300
        },{
            header:'图片资源名称',
            dataIndex:'diagramResourceName',
            width:300
        },{
            header:'描述',
            dataIndex:'description',
            width:300
        }];

        this.tbar=[{
            text:'发布',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.deploy
        },{
            text:'启动',
            iconCls:ipe.sty.start,
            scope:this,
            handler:this.startProcess
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delDef
        },{
            text:'历史',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.viewHis
        },'-',{
            text:'流程图',
            iconCls:ipe.sty.view,
            scope:this,
            handler:this.viewDiagram
        }];
        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'bpm/process_def_list',
                reader: {
                    root: 'rows'
                }
            },
            autoLoad:true,
            remoteSort : true,
            fields : [
                { name: 'id', type: 'string' },
                { name: 'rev', type: 'string' },
                { name: 'category', type: 'string' },
                { name: 'name', type: 'string' },
                { name: 'key', type: 'string' },
                { name: 'version', type: 'int' },
                { name: 'resourceName', type: 'string' },
                { name: 'diagramResourceName', type: 'string' },
                { name: 'description', type: 'string' },
                { name: 'startFormKey', type: 'boolean' },
                { name: 'deploymentId', type: 'string'}
            ]
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{store:this.store,pageSize:this.pageSize});
        this.callParent();
    },deploy:function(){
        var win=Ext.create('Bpm.ProcessDeployWin',{parent:this});
        win.show();
    },startProcess:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            Ext.Ajax.request({
                url: 'bpm/teststartProcess',
                params: {
                    key:record[0].data.key
                },
                success: function(response){
                    var resp =Ext.decode(response.responseText) ;
                    if(resp.success){
                        Ext.Msg.alert('提示','启动成功');
                    }else{
                        Ext.Msg.alert('提示',resp.rows);
                    }
                }
            });
        }else{
            Ext.Msg.alert('提示','请选择一条记录!');
        }
    },delDef:function(){
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
                            url: 'bpm/del_def_all',
                            params: {
                                deployId:record[0].data.deploymentId
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
    },viewHis:function(){

    },viewDiagram:function(){
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            Ext.create('Ext.Window',{
                title:'查看流程图->'+record[0].data.name+'['+record[0].data.key+']',
                resizable : true,
                draggable : true,
                maximizable:true,
                modal:true,
                layout : 'fit',
                width : 730,
                height : 400,
                items : [{
                    xtype : 'box',
                    autoEl : {
                        tag : 'img',
                        src : 'bpm/viewDiagram?date='+new Date()+'&key='+record[0].data.key+'&version='+record[0].data.version
                    }
                }],buttons:[{text:'关闭',iconCls:ipe.sty.cancel,handler:function(){
                    this.up('window').close();
                }}]
            }).show();
        }else{
            Ext.Msg.alert("提示", "请先选择一条数据");
        }
    }
});

/**
 * 流程部署
 */
Ext.define('Bpm.ProcessDeployWin',{
    title:'流程部署',
    extend:'Ext.Window',
    closeAction:'close',
    modal:true,
    width:400,
    height:100,
    layout:'fit',
    initComponent:function(){
        this.items=Ext.create('Ext.form.Panel',{
            isUpload:true,
            url:'bpm/deploy',
            frame:true,
            defaults:{
                anchor:'93%',
                border:'5px'
            },
            items:[{fieldLabel:'流程文件',name:'file',allowBlank:false,buttonText:'选择',xtype:'filefield'}]
        });

        this.buttons=[{text:'确定',scope:this,handler:this.submit},{text:'返回',scope:this,handler:this.close}];
        this.callParent();
    },submit:function(){
        var form=(this.down('form').getForm());
        if(form.isValid()){
            var fileName =form.findField('file').getValue();
            if (!/([.zip]|[.bar])$/.test(fileName)) {
                Ext.Msg.alert("提示", '请导入有效的zip或bar文件');
                return;
            }
            form.submit({
                success:function(a,b){
                    this.parent.getStore().load();
                    this.close();
                },
                failure:function(){
                    Ext.MessageBox.show({icon:Ext.MessageBox.ERROR,buttons: Ext.MessageBox.CANCEL,msg:'操作失败',title:'提示'});
                },
                scope:this
            });
        }else{
            Ext.Msg.alert('提示','必输项未填写或值非法!');
        }
    }
});


Ext.define('Bpm.ProcessManagerMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'hbox',align:'stretch'},
    initComponent:function(){
        this.processList=Ext.create('Bpm.ProcessDefList',{parent:this,flex:7});
        this.items=[this.processList];
        this.callParent();
    }
});