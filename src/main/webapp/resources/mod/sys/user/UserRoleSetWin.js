
Ext.define('Sys.user.RoleList',{
    extend:'Ext.grid.Panel',
    //title : '角色列表',
    flex:6,
    autoScroll : false,
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载角色列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true,
        plugins: {
            ptype: 'gridviewdragdrop',
            dragGroup: 'firstGridDDGroup',
            dropGroup: 'secondGridDDGroup',
            enableDrop: true
        }
    },
    pageSize : 15,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'角色',
            dataIndex:'roleName',
            width:200,
            sortable:true,
            field:{xtype:'textfield',required:true}
        },{
            header:'备注',
            dataIndex:'remark'
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                method:'POST',
                url: 'user/userNotRole',
                extraParams:{userId:this.parent.record.data.id},
                reader: {
                    root: 'rows'
                }
            },
            autoLoad:true,
            remoteSort : true,
            fields : [
                { name: 'id', type: 'string' },
                { name: 'roleName', type: 'string' },
                { name: 'enabled', type: 'string' },
                { name: 'remark', type: 'string' },
                { name: 'updatedDate', type: 'string' }
            ]
        });
        this.bbar=Ext.create('Ipe.PagingToolbar',{store:this.store,pageSize:this.pageSize});
        this.callParent();
    }
});

Ext.define('Sys.user.RoleSetList',{
    extend:'Ext.grid.Panel',
    title : '已选角色列表',
    autoScroll : false,
    flex:4,
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true,
        plugins: {
            ptype: 'gridviewdragdrop',
            dragGroup: 'secondGridDDGroup',
            dropGroup: 'firstGridDDGroup'
        }
    },
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'角色',
            dataIndex:'roleName',
            width:200,
            sortable:true,
            field:{xtype:'textfield',required:true}
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'user/userRole',
                method:'POST',
                extraParams:{userId:this.parent.record.data.id},
                reader: {
                    root: 'rows'
                }
            },
            autoLoad:true,
            remoteSort : true,
            fields : [
                { name: 'id', type: 'string' },
                { name: 'roleName', type: 'string' },
                { name: 'enabled', type: 'string' },
                { name: 'remark', type: 'string' },
                { name: 'updatedDate', type: 'string' }
            ]
        });
        this.callParent();
    }
});

Ext.define('Sys.user.UserRoleSetWin',{
    title:'角色配置',
    width:700,
    height:450,
    modal:true,
    extend:'Ext.Window',
    layout:{type:'border',align:'stretch'},
    buttonAlign:'left',
    border:false,
    initComponent:function(){
        this.roleList=Ext.create('Sys.user.RoleList',{parent:this,region:'center'});
        this.roleSetList=Ext.create('Sys.user.RoleSetList',{parent:this,region:'east',width:200,split:true});

        this.items=[this.roleList,this.roleSetList];
        this.buttons=[{
            html:'<span style="color:red">[将左侧角色托入右侧列表]</span>',
            xtype:'label'
        },'->',{
            text:'确定',
            scope:this,
            iconCls:ipe.sty.save,
            handler:this.saveData
        },{
            text:'取消',
            scope:this,
            iconCls:ipe.sty.cancel,
            handler:this.close
        }];
        this.callParent();
    },saveData:function(){
        var me=this;
        //var size_=me.roleSetList.getStore().getCount();
        var urids=[];
        me.roleSetList.getStore().each(function(rc){
            urids.push(rc.data.id);
        });

        Ext.Ajax.request({
            url:'role/addUserRole',
            method:'post',
            params:{urids:urids,userId:me.record.data.id},
            success:function(response){
                var result=Ext.decode(response.responseText);
                if(result.success){
                    Ext.Msg.alert('提示','保存成功');
                }else{
                    Ext.Msg.alert('提示',result.rows);
                }
                me.close();
            }
        });
    }
});