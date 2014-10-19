/**
 * 公共组件.
 */
Ext.define('Sys.com.OrgModel',{
    extend: 'Ext.data.Model',
    fields: [
        {name : 'id',type:'string'},
        {name : 'orgName',type:'string'},
        {name : 'orgCode',type:'string'},
        {name : 'remark',type:'string'},
        {name : 'sno',type:'int'}
    ],
    autoLoad:true,
    proxy : {
        type: 'ajax',
        url: 'org/getTree',
        reader: {
            root: 'rows'
        }
    }
});

Ext.define('Sys.com.OrgTreePanel',{
    title:'机构',
    width:200,
    minWidth:200,
    region:'west',
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
            autoLoad:true,
            model:'Sys.com.OrgModel',
            folderSort: true,
            root: {
                text: '根节点',
                id:'root',
                expanded: true
            }
        });

        this.columns=[{xtype: 'rownumberer'}/*,{
            text:'机构编号',dataIndex:'orgCode',xtype:'treecolumn',width:300
        }*/,{
            text:'机构名称',dataIndex:'orgName'
        }];

        this.callParent();
    }
});

function viewDnyDiagram(record){
    var infos=null;//取得各节点信息
    Ext.Ajax.request({
        async : false,
        url: 'bpm/viewProcessInfo',
        params: {
            processInstanceId:record.data.processInstanceId
        },
        success: function(response){
            var resp =Ext.decode(response.responseText) ;
            if(resp.success){
                infos=resp.rows;
            }
        }
    });

    var tabls="";
    if(infos!=null){
        tabls+="<table>";
        Ext.each(infos,function(r,i){
            if(r.currentActiviti){//显示正在执行任务的节点信息
                for(p in r.vars){
                    tabls += "<tr><td class='label'>" + p+ ": </td><td>" + r.vars[p] + "<td/></tr>";
                }
            }
        });
        tabls+="</table>";
    }

    var win=Ext.create('Ext.Window',{
        title:'执行中流程图',
        resizable : true,
        draggable : true,
        maximizable:true,
        layout : 'fit',
        width : 730,
        height : 400,
        items : [{xtype:'panel',autoScroll:true,items:{
            xtype : 'box',
            autoEl : {
                tag : 'img',
                'data-qtip':tabls,
                'data-qtitle':"执行中.",
                src : 'bpm/viewDnyDiagram?date='+new Date()+'&executionId='+record.data.executionId
            }
        }}],buttons:[{text:'关闭',handler:function(){
            this.up('window').close();
        }}]
    });
    win.show();
}