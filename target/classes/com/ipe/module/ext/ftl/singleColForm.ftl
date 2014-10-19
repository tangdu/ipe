/**
 * Created by tangdu on 14-2-20.
 */
<#include "macro.ftl"/>
Ext.define('${mpackage}.${entityName}EditForm',{
    extend:'Ext.FormPanel',
    url:'${entityName?uncap_first}/add',
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
        this.items=[
           <#list formItem as item>{<#if item.xtype!='undefined'>
            fieldLabel:'${item.fieldLabel}',
            <@maxLength val='${item.maxLength}'/>
            xtype:'${item.xtype}',
            <@datefield val='${item.xtype}'/>
            allowBlank:${item.allowBlank?string("true","false")},
            name:'${item.name}'
            <#else>

            layout:'column',
            xtype: 'container',
            anchor:'100%',
            defaults:{
                border:false,
                frame:true
            },
            items:[
                {columnWidth:.9,fieldLabel:'${item.fieldLabel}',xtype:'textfield',name:'${item.name}'},{xtype:'hidden',name:'${item.name}Id'},
                {width:40,xtype:'button',text:'选择',scope:this,handler:this.showChoose${item.name?cap_first}}
            ]
            </#if>
         }<#if (item_index+1 < formItem?size)>,</#if></#list>,{xtype:'hidden',name:'id'}];

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
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '保存失败');
                    }
                });
            }else if(this.oper=="edit"){
                this.getForm().submit({
                    url:'taskProxy/edit',
                    success:function(re){
                        Ext.Msg.alert('提示', '修改成功');
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '修改失败');
                    }
                });
            }
        }else{
            Ext.Msg.alert('提示','必填项为空或是输入值受限！');
        }
    }
})