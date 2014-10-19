
Ext.define('Sys.tool.UrlMappingList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.urlmappinglist',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载映射列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'类名',
            width:200,
            dataIndex:'className',
            sortable:true
        },{
            header:'需求',
            width:150,
            dataIndex:'consumes',
            sortable:true
        },{
            header:'自定义',
            dataIndex:'custom'
        },{
            header:'消息头',
            dataIndex:'headers'
        },{
            header:'方法',
            dataIndex:'methods'
        },{
            header:'处理',
            dataIndex:'produces'
        },{
            header:'URL',
            dataIndex:'url'
        },{
            header:'方法名称',
            dataIndex:'methodName'
        },{
            header:'返回值',
            dataIndex:'returnType'
        },{
            header:'参数',
            dataIndex:'parameters'
        },{
            header:'注解名称',
            dataIndex:'annotationName'
        }];

        var grouping=Ext.create('Ext.grid.feature.Grouping',{
            groupHeaderTpl:'{name}({rows.length})',
            hideGroupedHeader:true,
            groupByText:'对该列进行分组',
            showGroupsText:'是否分组'
        });
        this.features=[grouping];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'tool/spring_urlmapping',
                reader: {
                    root: 'rows'
                }
            },
            groupField:'className',
            remoteSort : true,
            autoLoad:true,
            fields : ['params','className','consumes','custom','headers','methods',
                'produces','url','methodName','returnType','parameters','annotationName']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize,
            items:pageCom([['dictName','名称']],this)
        });
        this.callParent();
    }
});

Ext.define('Sys.tool.SysUrlMappingMainPanel',{
    extend:'Ext.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.urlMappingList=Ext.create('Sys.tool.UrlMappingList',{parent:this,region:'center'});

        this.items=[this.urlMappingList];
        this.callParent();
    }
});
