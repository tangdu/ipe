Ext.define('${mpackage}.${entityName}',{
    extend:'Ext.grid.Panel',
    alias : 'widget.${entityName?uncap_first}list',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载代理数据列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},
            <#list formItem as grid>{
            header:'${grid.fieldLabel}',<#if (gird.width)?if_exists>width:'${(grid.width)!}',</#if>
            dataIndex:'${grid.name}',
            sortable:true
        }<#if (grid_index+1 < formItem?size)>,</#if></#list>];

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.add${entityName}
        },{
            text:'编辑',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.edit${entityName}
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.del${entityName}
        }];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: '${entityName?uncap_first}/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            //autoLoad:true,
            fields : [<#list formItem as col><#if (col_index+1 < formItem?size)>'${col.name}',<#else>'${col.name}'</#if></#list>]
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize
        });

        this.callParent();
    },add${entityName}:function(){

    },edit${entityName}:function(){
        var record=this.getSelectionModel().getSelection();
        if(record.length>0){
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },del${entityName}:function(){
        var me=this;
        var record=this.getSelectionModel().getSelection();
        if(record.length>0){
            Ext.Msg.show({
                title:'提示',
                msg: '你确认删除此记录?',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope:this,
                fn:function(bt){
                    if(bt=='yes'){
                        Ext.Ajax.request({
                            url: '${entityName?uncap_first}/del',
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