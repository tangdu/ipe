Ext.define('Sys.config.SysConfigEditForm',{
	extend:'Ext.FormPanel',
    url:'sysConfig/edit',
    frame:true,
    defaults:{
        anchor:'28%'
    },
    bodyPadding: 5,
    buttonAlign:'center',
    layout:{type:'vbox',align:'stretch'},
	initComponent:function(){
		this.items=[
            {xtype:'fieldset',title:'作者信息',layout:'column',flex:2,items:[
            	{layout:'form',columnWidth:.3,defaultType: 'textfield',frame:true,border:false,xtype:'container',items:[
	            		{fieldLabel:'作者',name:'author'},
	            		{fieldLabel:'QQ',name:'qq'}
            		]}
            ]},
            {xtype:'fieldset',title:'系统信息',layout:'column',flex:2,items:[
            	{layout:'form',columnWidth:.3,defaultType: 'textfield',frame:true,border:false,xtype:'container',items:[
	            		{fieldLabel:'描述',name:'description',xtype:'textarea'},
            			{fieldLabel:'版本',name:'version'}
            		]}
            ]},
            {xtype:'fieldset',title:'开发者选项',layout:'column',flex:2,items:[
            	{layout:'form',columnWidth:.3,defaultType: 'combo',frame:true,border:false,xtype:'container',items:[
	            	{
			        	fieldLabel:'启用多Tab',
			            store: ipe.store.flagStore,
			            allowBlank:false,
			            emptyText: '请选择',
			            hideField:'ena_mtab',
			            name:'ena_mtab',
			            mode: 'local',
			            triggerAction: 'all',
			            valueField: 'key',
			            displayField: 'value',
			            editable: true
			        },{
			        	fieldLabel:'启用开发模式',
			            store: ipe.store.flagStore,
			            emptyText: '请选择',
			            hideField:'ena_dev',
			            name:'ena_dev',
			            allowBlank:false,
			            mode: 'local',
			            triggerAction: 'all',
			            valueField: 'key',
			            displayField: 'value',
			            editable: true
			        },{
			        	fieldLabel:'桌面风格',
			            store: ipe.store.sysStyleStore,
			            emptyText: '请选择',
			            hideField:'sys_style',
			            name:'sys_style',
			            mode: 'local',
			            triggerAction: 'all',
			            allowBlank:false,
			            valueField: 'key',
			            displayField: 'value',
			            editable: true
			        }
		        ]},{layout:'form',columnWidth:.3,defaultType: 'displayfield',frame:true,border:false,xtype:'container',items:[
		        	{value:'(选择‘是’多个Tab页，‘否’只显示一个Tab页)'},
		        	{value:'(开发模式没有权限控制)'},
		        	{value:'(选择系统风格)'}
		        ]}
            ]},{
            	xtype:'label',
            	text:'*修改后请重新登入系统',
            	style:'color:red'
            }
		]
		
		this.buttons=[{text:'保存',tooltip:'生效请重新刷新页面',handler:this.saveData,scope:this,iconCls:ipe.sty.save}];
		this.callParent();
		this.on('render',this.loadData,this);
	},saveData:function(){
		var me=this;
		if(this.getForm().isValid()){
			this.getForm().submit({
				params:{params:Ext.encode(this.getForm().getValues())},
                success:function(re){
                    Ext.Msg.alert('提示','保存成功');
					me.loadData();
                },
                failure: function(form, action) {
                    Ext.Msg.alert('提示','保存失败');
                }
            });
		}
	},loadData:function(){
		var me=this;
		Ext.Ajax.request({
			url:'sysConfig/list',
			success:function(resp){
				var result=Ext.decode(resp.responseText);
				if(result.success){
					me.getForm().setValues(result.rows);
				}
			}
		});
	}
});

/**
 * 组合-配置管理首页
 */
Ext.define('Sys.config.SysConfigMainPanel',{
    extend:'Ipe.Panel',
    //bodyPadding: 5,
    layout:'fit',
    initComponent:function(){
        this.sysConfigEditForm=Ext.create('Sys.config.SysConfigEditForm',{parent:this});
        this.items=[this.sysConfigEditForm];
        this.callParent();
    }
});