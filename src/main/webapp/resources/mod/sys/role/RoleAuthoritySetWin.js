

Ext.define('Sys.role.AuthorityTreePanel',{
    //title:'权限列表',
    region:'center',
    extend:'Ext.tree.Panel',
    animate: true,
    rootVisible:false,
    lines:true,
    singleExpand: true,
    useArrows: true,
    viewConfig:{
        onCheckboxChange: function(e, t) {
            var item = e.getTarget(this.getItemSelector(), this
                .getTargetEl()), record;
            if (item) {
                record = this.getRecord(item);
                var check = !record.get('checked');
                record.set('checked', check);

                if (check) {
                    record.bubble(function(parentNode) {
                        parentNode.set('checked', true);
                    });
                    record.cascadeBy(function(node) {
                        node.set('checked', true);
                    });
                } else {
                    record.cascadeBy(function(node) {
                        node.set('checked', false);
                    });
                }
            }
        }
    },
    initComponent:function(){
        this.store= Ext.create('Ext.data.TreeStore', {
            nodeParam:'pid',
            fields: [
                {name : 'id',type:'string'},
                {name : 'resourceUrl',type:'string'},
                {name : 'resourceName',type:'string'},
                {name : 'resourceType',type:'string'}
            ],
            proxy : {
                type: 'ajax',
                url: 'role/getAuthority',
                extraParams:{roleId:this.parent.record.data.id},
                reader: {
                    type: 'json',
                    root: 'rows'
                }
            },
            //folderSort: true,
            root: {
                resourceName: '根节点',
                id:'root'/*,
                expanded: true*/
            }
        });

        this.columns=[{xtype: 'rownumberer'},{
            text:'资源名称',dataIndex:'resourceName',xtype:'treecolumn',width:300
        },{
            text:'资源类型',dataIndex:'resourceType',renderer:ipe.fuc.resourceDt
        },{
            text:'资源路径',dataIndex:'resourceUrl',width:300
        }];

        this.callParent();
    }
});

Ext.define('Sys.role.RoleAuthoritySetWin',{
    width:600,
    height:450,
    modal:true,
    extend:'Ext.Window',
    layout:'fit',
    //buttonAlign:'center',
    initComponent:function(){
        this.panel=Ext.create('Sys.role.AuthorityTreePanel',{parent:this,border:false});

        this.items=[this.panel];
        this.buttons=[{
            text:'确定',
            iconCls:ipe.sty.save,
            scope:this,
            handler:this.saveData
        },{
            text:'取消',
            iconCls:ipe.sty.cancel,
            scope:this,
            handler:this.close
        }];
        this.callParent();
    },saveData:function(){
        var me=this;
        var nodes=this.panel.getView().getChecked();
        if(nodes.length>0){
            var ps=[];
            Ext.each(nodes,function(r,i){
                if(r.data.id!='root'){
                    ps.push(r.data.id);
                }
            });

            Ext.Ajax.request({
                url: 'role/addAuthority',
                params: {
                    ids:ps,
                    roleId:me.record.data.id
                },
                success: function(response){
                    var resp =Ext.decode(response.responseText) ;
                    if(resp.success){
                        Ext.Msg.alert('提示','配置成功!');
                    }else{
                        Ext.Msg.alert('提示',resp.rows);
                    }
                    me.close();
                }
            });
        }
    }
});