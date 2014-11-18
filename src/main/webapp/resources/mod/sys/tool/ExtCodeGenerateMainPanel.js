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

Ext.define('Sys.tool.ExtCodeGenerateList',{
    extend:'Ext.grid.Panel',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    plugins:[
        Ext.create('Ext.grid.plugin.CellEditing',{
            clicksToEdit:1
        })
    ],
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},
            {
                header:'fieldLabel',
                dataIndex:'fieldLabel',
                width:100,
                sortable:true,
                editor:{xtype:'textfield',maxLength:50}
            },{
                header:'name',
                width:100,
                dataIndex:'name',
                sortable:true
            },{
                header:'javaType(属性)',
                width:200,
                dataIndex:'javaType',
                sortable:true
            },{
                header:'maxLength',
                width:80,
                dataIndex:'maxLength',
                sortable:true,
                editor:{xtype:'numberfield',max:500,min:1}
            },{
            	header:'allowBlank',
            	width:80,
            	dataIndex:'allowBlank',
            	sortable:true
            },{
                header:'ExtJs(xtype)',
                width:150,
                dataIndex:'xtype',
                sortable:true,
                editor:{
                	xtype:'combo',
                	/*store:Ext.create('Ext.data.Store',{
			            fields:['key','value'],
			            data:[{key:'textfield',value:'表单文本框'},{key:'textarea',value:'多行文本框'},
			            	{key:'htmleditor',value:'HTML编辑器'},{key:'hidden',value:'表单隐藏域'},{key:'combo',value:'下拉框'}
			            	,{key:'label',value:'标签'},{key:'numberfield',value:'数字编辑器'},{key:'datefield',value:'日期选择项'}
			            	,{key:'fieldset',value:'表单字段组'},{key:'radiogroup',value:'编组单选框'}
			            	,{key:'checkboxgroup',value:'编组多选框'},{key:'object',value:'对象类型'}]
			        }),*/
                	store:ipe.store.xtypeStore,
                    displayField:'value',
                    valueField:'key',
                    hiddenName:'type',
                    triggerAction:'all',
                    editable:false,
                    queryMode:'local'
                }
            }];

        this.store=Ext.create('Ext.data.JsonStore', {
            fields : ['xtype','allowBlank','maxLength','javaType','name','fieldLabel']
        });

        this.tbar=[];

        this.callParent();
    }
});

Ext.define("KellGridCombox",{
	extend:'Ext.form.field.Picker',
	alias: 'widget.keelgridcombo',  
	editable : true,  
    allowBlank:false, 
    initComponent : function(){  
        this.grid=Ext.create('Ext.grid.Panel', {
        	height : 250,
        	floating : true,width : 350,
        	//tbar:['输入名称检索：',{xtype:'textfield',width:200}],
        	columns:[{header:'包结构',dataIndex:'packageName',width:'40%'},{header:'实体名',dataIndex:'entityName',width:'20%'}],
        	store:Ext.create('Ext.data.JsonStore',{
	            fields:['name','entityName','columns','packageName'],
	            autoLoad:true,
	            proxy: {
	                type: 'ajax',
	                url: 'ext/getEntittys',
	                reader: {
	                    root: 'rows'
	                }
	            }
	        })
        });
        this.callParent();
        this.grid.on('itemclick',this.selectRecord,this);
    },createPicker:function(){
	    return  this.grid;
    },selectRecord:function(th,record){
    	this.setValue(record.data.entityName);
    	this.createStuct(record.data.entityName,record);
    	this.collapse();
    },createStuct:function(entityName,record){
    	var me=this;
		if(entityName && record){
			me.parent.extCodeGenerateList.getStore().removeAll();
			Ext.each(record.data.columns,function(r,i){
				var _record=Ext.create('ExtColumn',r);
				me.parent.extCodeGenerateList.getStore().add(_record);
			});
		}
    }
});

Ext.define("Sys.tool.ExtCodeGenerateMainPanel",{
    extend:'Ipe.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
    	this.form=Ext.create('Ext.FormPanel',{
            region:'north',
            defaults:{
                anchor:'98%'
            },
            frame:true,
            border:false,
            bodyPadding:5,
            buttonAlign:'center',
            items:[
                {fieldLabel:'实体对象',xtype:'keelgridcombo',parent:this,name:'entityName'},
                {	fieldLabel:'生成类型',
                    xtype:'combo',
                    name:'type',
                    allowBlank:false,
                    /*store:Ext.create('Ext.data.Store',{
			            fields:['key','value'],
			            data:[{key:'singleColForm',value:'单列表单'},{key:'multColForm',value:'多列表单'},
			            	{key:'gridList',value:'表格'},{key:'querygridList',value:'查询表格'}]
			        }),*/
                    store:ipe.store.extTplCodeStore,
                    value:'gridList',
                    displayField:'value',
                    valueField:'key',
                    hiddenName:'type',
                    triggerAction:'all',
                    editable:false,
                    queryMode:'local'
	             },
                {fieldLabel:'文件包名',xtype:'textfield',name:'mpackage',allowBlank:false},
                {fieldLabel:'是否生成文件',name:'createFile',xtype:'checkbox'},
                {fieldLabel:'文件路径',xtype:'textfield',name:'path'}
            ],
            buttons:[{text:'生成',width:100,handler:this.generaCode,scope:this,iconCls:ipe.sty.save}]
        });
        
        this.extCodeGenerateList=Ext.create('Sys.tool.ExtCodeGenerateList',{region:'center'});

        this.items=[this.form,this.extCodeGenerateList];
        this.callParent();
    },generaCode:function(){
    	var me=this;
    	var params=this.getParams();
    	if(params!=null){
    		Ext.Ajax.request({
	    		params:params,
	    		method:'post',
	    		url:'ext/generate_execute',
	    		success:function(re){
	    			var result=Ext.decode(re.responseText);
	    			var win=new Sys.tool.ExtPrevieCodewWin({
	    				title:'代码预览 '+result.className,parent:me
	    			});
	    			win.show();
	    			win.codePanel.setValue(result.context);
	    		}
	    	});
    	}
    },getParams:function(){
    	var params=this.form.getValues();
    	var records=[];
    	var _r=null;
    	this.extCodeGenerateList.getStore().each(function(r,i){
    		_r=r.data;
    		_r["sno"]=i;
    		records.push(_r);
    	});
    	if(records.length<=0){
    		Ext.Msg.alert('明细表格为空，不能生成！');
    		return null;
    	}
    	params.dt=Ext.encode(records);
    	return params;
    }
});

Ext.define('Sys.tool.ExtPrevieCodewWin',{
    extend:'Ext.Window',
    title:'代码预览',
    layout:'card',
    action:'close',
    modal:true,
    width:850,
    height:600,
    border:false,
    layout:'fit',
    initComponent:function(){
    	this.codePanel=Ext.create('Ext.form.TextArea',{readOnly:true,style:'font-size:15px!important;'});
    	this.buttons=['->',
    		{text:'复制',iconCls:ipe.sty.save,handler:this.copyCode,scope:this},
            {text:'预览',iconCls:ipe.sty.save,handler:this.preCode,scope:this},
            {text:'取消',iconCls:ipe.sty.cancel,handler:this.close,scope:this}
        ];
        this.items=[this.codePanel];
        this.callParent();
    },copyCode:function(){
    	window.clipboardData.setData('text', this.codePanel.getValue());
    },preCode:function(){
    	var params=this.parent.getParams();
    	var className=params.mpackage+"."+params.entityName;
    	window.open('ext/code_view?htmlId='+this.codePanel.inputId+"&className="+className);
    }
});