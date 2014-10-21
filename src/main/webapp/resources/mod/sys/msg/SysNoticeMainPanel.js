/**
 * Created by tangdu on 14-2-15.
 */
Ext.define('Sys.msg.NoticeList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.noticelist',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载公告列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'标题',
            dataIndex:'title',
            sortable:true
        },{
            header:'内容',
            width:400,
            dataIndex:'content',
            sortable:false
        },{
            header:'附件',
            width:250,
            dataIndex:'appendixName',
            renderer:function(val,cellmeta, record){
               return "<a href='#' onclick=ipe.fuc.downFile('"+record.data.appendixPath+"')>"+val+"</a>";
            },
            sortable:false
        },{
            header:'创建日期',
            width:150,
            dataIndex:'createdDate',
            sortable:true
        }];

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addNotice
        },{
            text:'编辑',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editNotice
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delNotice
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'notice/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : ['id','title','content','appendixPath','userId','createdDate','appendixName']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize
        });

        this.callParent();
    },addNotice:function(){
        var parent=this.parent;
        parent.noticeForm.down("textfield[name=appendixName]").hide();
        parent.noticeForm.expand(true);
        parent.noticeForm.show();
        parent.noticeForm.getForm().reset();
        parent.noticeForm.setTitle("新增提醒");
        parent.noticeForm.oper="add";
        parent.doLayout();
    },editNotice:function(){
        var parent=this.parent;
        var record=this.getSelectionModel().getSelection();
        if(record && record.length>0){
            parent.noticeForm.down("textfield[name=appendixName]").show();
            parent.noticeForm.expand(true);
            parent.noticeForm.show();
            parent.noticeForm.setTitle("编辑提醒");
            parent.noticeForm.oper="edit";
            parent.noticeForm.setData(record[0]);
            parent.doLayout();
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },delNotice:function(){
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
                            url: 'notice/del',
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

Ext.define('Sys.msg.NoticeEditForm',{
    extend:'Ext.FormPanel',
    url:'notice/add',
    waitTitle:'请稍候....',
    defaults:{
        anchor:'98%'
    },
    isFileUpload:true,
    frame:true,
    plain:true,
    bodyPadding: 5,
    border:false,
    defaultType:'textfield',
    initComponent:function(){
        this.items=[{
            fieldLabel:'标题',
            name:'title'
        },{
            fieldLabel:'公告内容',
            xtype:'htmleditor',
            height:400,
            name:'content'
        },{
            layout:'column',
            xtype: 'container',
            anchor:'100%',
            defaults:{
                border:false,
                frame:true
            },
            items:[
                {columnWidth:.9,fieldLabel:'附件', xtype:'filefield',buttonText:"浏览",name:'file'},
                {columnWidth:.1,xtype:'button',text:'删除',scope:this,handler:function(){this.down("filefield").reset();}}
            ]
        },{
            fieldLabel:'原附件',
            readOnly:true,
            //xtype:'label',//TODO　不知道为何不显示ｓｈｏｗ（）
            hidden:true,
            layout:"form",
            name:'appendixName'
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
        var content=this.getForm().findField("content").getValue();
        if(content==""){
            Ext.Msg.alert("提示",'公告内容不能为空');
            return;
        }else{
        	content=content.replace(/<\!--\[if\s*gte\s*mso\s*[\s\S]*?<\!\[endif\]-->/gi, "");
            if(content.length>2000){
                Ext.Msg.alert("提示",'公告内容长度不能超过2000字');
                return;
            }
        }
        if(this.getForm().isValid()){
            if(this.oper=="add"){
                this.getForm().submit({
                    success:function(re){
                        Ext.Msg.alert('提示', '保存成功');
                        me.parent.noticeList.getStore().load();
                        me.hide();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '保存失败');
                    }
                });
            }else if(this.oper=="edit"){
                this.getForm().submit({
                    url:'notice/edit',
                    success:function(re){
                        Ext.Msg.alert('提示', '修改成功');
                        me.parent.noticeList.getStore().load();
                        me.parent.noticeList.getSelectionModel().deselectAll();
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

Ext.define('Sys.msg.SysNoticeMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.noticeList=Ext.create('Sys.msg.NoticeList',{parent:this,region:'center'});
        this.noticeForm=Ext.create('Sys.msg.NoticeEditForm',{parent:this,region:'east',width:600,hidden:true,split:true});
        this.items=[this.noticeList,this.noticeForm];
        this.callParent();
    }
});