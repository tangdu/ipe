
Ext.define('Sys.menu.MenuChoosePanel',{
    //title:'菜单列表',
    region:'center',
    extend:'Ext.tree.Panel',
    animate: true,
    rootVisible: false,
    multiSelect: true,
    singleExpand: true,
    useArrows: true,
    initComponent:function(){
        this.store= Ext.create('Ext.data.TreeStore', {
            //nodeParam:'pid',
            autoLoad:true,
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
            },
            fields:['menuName','menuType','id','parent','enabled','remark','sno','menuStyle','resource',"menuUrl"]
        });

        this.columns=[{xtype: 'rownumberer'},{
            text:'菜单名称',dataIndex:'menuName',xtype:'treecolumn',width:300
        },{
            text:'菜单类型',dataIndex:'menuType',renderer:ipe.fuc.resourceDt
        },{
            text:'菜单路径',dataIndex:'menuUrl',width:300
        }];
        this.callParent();
    }
});


Ext.define('Sys.menu.MenuChooseWin',{
    title:'菜单选择',
    extend:'Ext.Window',
    layout:'fit',
    width:500,
    height:400,
    initComponent:function(){
        this.menuTree=Ext.create('Sys.menu.MenuChoosePanel',{parent:this});
        this.items=[this.menuTree];

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
    },
    saveData:function(){
        if(this.nameField && this.valueField){
            var records=this.menuTree.getSelectionModel().getSelection();
            if(records.length>0){
                this.nameField.setValue(records[0].data.menuUrl);
                this.valueField.setValue(records[0].data.id);
                this.close();
            }else{
                Ext.Msg.alert('提示','请选择记录!');
            }
        }
    }
});