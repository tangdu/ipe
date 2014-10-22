
Ext.define('Sys.msg.MessageForm',{
    extend:'Ext.FormPanel',
    defaults:{
        anchor:'98%'
    },
    frame:true,
    bodyPadding: 5,
    border:false,
    defaultType:'textfield',
    initComponent:function(){
        this.items=[{
            fieldLabel:'消息类型',readOnly:true,
            xtype:'combo',name:'msgType',store:ipe.store.flagStore,value:'0',
            displayField:'value',valueField:'key',hiddenName:'msgType',triggerAction:'all',editable:false,queryMode:'local'
        },{
            fieldLabel:'消息内容',
            xtype:'textarea',
            height:160,
            name:'msgContent'
        },{
            fieldLabel:'创建时间',
            name:'createdDate',
            readOnly:true
        }];
        
        this.callParent();
        this.setDataValue();
    },setData:function(record){
        this.loadRecord(record);
    },setDataValue:function(){
        this.getForm().setValues(this.data);
    }
});


Ext.define('Sys.msg.MessageViewWin',{
    title:'我的消息',
    extend:'Ext.Window',
    modal:true,
    layout:'fit',
    width:500,
    height:300,
    border:false,
    initComponent:function(){
        this.messageForm=Ext.create('Sys.msg.MessageForm',{parent:this,data:this.data});
        this.items=[this.messageForm];

        this.buttons=[{
            text:'关闭',
            iconCls:ipe.sty.cancel,
            scope:this,
            handler:this.close
        }];
        this.callParent();
    }
});