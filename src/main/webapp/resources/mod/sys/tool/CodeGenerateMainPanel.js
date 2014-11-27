Ext.define('ExtColumn', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'fieldLabel',  type: 'string'},
         {name: 'name',  type: 'string'},
          {name: 'javaType',  type: 'string'},
           {name: 'maxLength',  type: 'string'},
            {name: 'allowBlank',  type: 'string'},
             {name: 'xtype',  type: 'string'}
    ]
});

Ext.define("Sys.tool.CodeGenerateMainPanel",{
    extend:'Ipe.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
    	this.cgroup=Ext.create('Ext.form.CheckboxGroup',{
	    	fieldLabel:'生成结构层次',
	    	columns: 1,items:[
	        	{boxLabel: 'Entity', name: 'cEntity', checked: true},     
	            {boxLabel: 'Dao', name: 'cDao', checked: true},     
	            {boxLabel: 'Service', name: 'cService', checked: true},    
	            {boxLabel: 'Controller', name: 'cController', checked: true}
	        ]
    	}); 
    	this.form=Ext.create('Ext.FormPanel',{
            region:'north',
            defaults:{
                anchor:'98%'
            },
            url:'code/create',
            frame:true,
            border:false,
            bodyPadding:5,
            buttonAlign:'center',
            items:[
                {fieldLabel:'所属用户',xtype:'textfield',parent:this,name:'schema',allowBlank:false},
                {xtype:'label',margin:'0 0 0 120',text:'例：ipe_db'},
                {fieldLabel:'表名',xtype:'textfield',name:'tableName',allowBlank:false},
                {xtype:'label',margin:'0 0 0 120',text:'例：t_cor_theme'},
                {fieldLabel:'实体名字',name:'entityName',xtype:'textfield'},
                {xtype:'label',margin:'0 0 0 120',text:'例：Theme'},
                {fieldLabel:'文件路径',xtype:'textfield',name:'path',allowBlank:false},
                {xtype:'label',margin:'0 0 0 120',text:'例：E:/studyspace/ipe/src/main/java'},
                {fieldLabel:'包名',xtype:'textfield',name:'packageName',allowBlank:false},
                {xtype:'label',margin:'0 0 0 120',text:'例：com.ipe.module.core'},
                this.cgroup
            ],
            buttons:[{text:'生成',width:100,handler:this.generaCode,scope:this,iconCls:ipe.sty.save}]
        });
        
        this.items=[this.form];
        this.callParent();
    },generaCode:function(){
    	var me=this;
    	if(this.form.getForm().isValid()){
    		var p={};
    		var citems=this.cgroup.getChecked();
    		Ext.each(citems,function(r,i){
    			 p[r.name]=true;   
    		});
    		
    		Ext.Msg.show({
                title:'提示',
                msg: '注意：勾选的层次生成的代码将覆盖，确认操作吗?',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope:this,
                fn:function(bt){
                    if(bt=='yes'){
                    	me.form.getForm().submit({
			    			params:p,
			    			success: function(form, action) {
			                    Ext.Msg.alert('提示', '保存成功');
			                },
			                failure: function(form, action) {
			                    Ext.Msg.alert('提示', '保存失败');
			            }});
                    }
                }
    		});
    	}
    }
});

