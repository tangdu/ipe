/**
 * 用户表格列表
 */
Ext.define('Sys.config.ConfigList',{
    extend:'Ext.grid.property.Grid',
    nameColumnWidth:300,
    sourceConfig:{
       created:{
           type:'date',
           editor: Ext.create('Ext.form.field.Date', {selectOnFocus: true,format:'Y-m-d',altFormats:'Y-m-d'})
       }
    },
    initComponent:function(){
        this.tbar=[{text:'保存',scope:this,handler:this.saveData,iconCls:ipe.sty.save}];
        this.on('render',this.afterLoad,this);
        this.callParent();
    },afterLoad:function(){
        Ext.Ajax.request({
            url:'sysConfig/list',
            async:false,
            scope:this,
            success:function(re){
                var result=Ext.decode(re.responseText);
                if(result.success){
                    this.setSource(result.rows);
                }
            }
        });
    },
    saveData:function(){
        var source=this.getSource();
        Ext.Ajax.request({
            url:'sysConfig/edit',
            async:false,
            params:{params:Ext.encode(source)},
            scope:this,
            success:function(re){
                var result=Ext.decode(re.responseText);
                if(result.success){
                    Ext.Msg.alert('提示','操作成功');
                    this.afterLoad();
                }
            }
        });
    }
});

/**
 * 组合-配置管理首页
 */
Ext.define('Sys.config.SysConfigMainPanel',{
    extend:'Ext.Panel',
    //bodyPadding: 5,
    layout:'fit',
    initComponent:function(){
        this.configList=Ext.create('Sys.config.ConfigList',{parent:this,region:'center'});
        this.items=[this.configList];
        this.callParent();
    }
});