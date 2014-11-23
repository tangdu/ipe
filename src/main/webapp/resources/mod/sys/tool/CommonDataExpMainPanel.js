
Ext.define('ExtColumn', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'name',  type: 'string'},
        {name: 'field',  type: 'string'},
        {name: 'sno',  type: 'int'}
    ]
});

Ext.define('Sys.tool.CommonDataExpList',{
    extend:'Ext.grid.Panel',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},
           {
                header:'编号',
                width:100,
                dataIndex:'code',
                sortable:true
            },{
                header:'名称',
                width:200,
                dataIndex:'name',
                sortable:true
            },{
                header:'状态',
                width:200,
                dataIndex:'name',
                sortable:true
            },{
                header:'创建时间',
                width:150,
                dataIndex:'createdDate',
                sortable:true
            },{
            	header:'更新时间',
            	width:150,
            	dataIndex:'updatedDate'
            },{
            	header:'备注',
            	dataIndex:'remark',
            	sortable:true
            }];

        this.store=Ext.create('Ext.data.JsonStore', {
        	proxy: {
                type: 'ajax',
                url: 'exlExptpl/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : ['id','code','remark','name','createdDate','sql','titles','updatedDate','enabled']
        });

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addExlExptpl
        },{
            text:'编辑',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editExlExptpl
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delExlExptpl
        },{text:'导出',scope:this,handler:this.expExldata,iconCls:ipe.sty["export"]},
        '->',{xtype:'label',text:'查询:'},{
        	xtype:'searchfield',
            emptyText:'输入编号/名称查询',
            scope:this,
            name:'code',
            store:this.store,
            width:150,
            handler:this.searchExpTpl
        },{width:ipe.config.paddingWidth,xtype:'tbspacer'}];

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize
        });
        
        this.callParent();
    },addExlExptpl:function(){
    	var win=Ext.create('Sys.tool.CommonDataExpWin',{parent:this,oper:'add'});
    	win.show();
    },editExlExptpl:function(){
    	var record=this.getSelectionModel().getSelection();
        if(record.length>0){
        	var win=Ext.create('Sys.tool.CommonDataExpWin',{parent:this,oper:'edit'});
    		win.show();
    		win.setInfo(record[0].data);
        }else{
        	Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },delExlExptpl:function(){
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
                            url: 'exlExptpl/del',
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
    },expExldata:function(){
    	var record=this.getSelectionModel().getSelection();
        if(record.length>0){
        	window.location.href='exlExptpl/expexcelData?code='+record[0].data.code;
        }else{
        	Ext.Msg.alert('提示','请选择要导出的记录!');
        }
    },searchExpTpl:function(){
    	
    }
});


Ext.define("Sys.tool.CommonDataExpMainPanel",{
    extend:'Ipe.Panel',
    layout:{type:'border',align:'stretch'},
    initComponent:function(){
        this.commonDataExpList=Ext.create('Sys.tool.CommonDataExpList',{region:'center',parent:this});
        this.items=[this.form,this.commonDataExpList];
        this.callParent();
    }
});


Ext.define('Sys.tool.CommonDataExpWin',{
    extend:'Ext.Window',
    title:'新增导出模板',
    action:'close',
    modal:true,
    width:850,
    height:600,
    border:false,
    layout:'fit',
    initComponent:function(){
    	if(this.oper=="add"){
    	}else if(this.oper=="edit"){
    		this.title="编辑导出模板";
    	}
    	
    	this.form=Ext.create('Ext.form.FormPanel',{
    		flex:7,
    		frame:true,
    		layout:'column',
    		items:[{
    			name:'id',
    			xtype:'hidden'
    		},{
    			columnWidth:.33,
    			xtype:'container',
    			layout:'form',
    			items:[{fieldLabel:'编号',xtype:'textfield',name:'code',labelWidth:40,allowBlank:false}]
    		},{
    			columnWidth:.33,
    			xtype:'container',
    			layout:'form',
    			items:[{fieldLabel:'名称',xtype:'textfield',name:'name',labelWidth:40,allowBlank:false}]
    		},{
    			columnWidth:.33,
    			xtype:'container',
    			layout:'form',
    			items:[{fieldLabel:'备注',xtype:'textfield',name:'remark',labelWidth:40}]
    		},{
    			columnWidth:1,
    			xtype:'container',
    			layout:'form',
    			items:[{xtype:'label',labelWidth:1,text:'查询SQL:'}]
    		},{
    			columnWidth:1,
    			xtype:'container',
    			layout:'form',
    			items:[{
    				xtype:'textareafield',
    				labelWidth:1,
    				emptyText:'freemarker语法',
    				height:450,
    				name:'sql',
    				cls:'freemarker-editor',
    				allowBlank:false,
    				maxLength:2000
    			}]
    		}]
    	});
    	
    	this.grid=Ext.create('Ext.grid.Panel',{
    		title:'Excel导出标题(按查询字段排序)',
    		flex:3,
    		plugins:[
		        Ext.create('Ext.grid.plugin.CellEditing',{
		            clicksToEdit:1
		        })
		    ],
    		columns:[{header:'字段',dataIndex:'field'},{header:'名称',dataIndex:'name',editor:{xtype:'textfield',maxLength:50}}],
    		store:Ext.create('Ext.data.JsonStore', {
	            fields : ['name','field']
	        })
    	});
    	
    	this.vbox=Ext.create('Ext.Panel',{
    		border:false,
    		layout:{type:'hbox',align:'stretch'},
    		items:[this.form,this.grid]
    	})
    	this.buttons=['->',
    		{text:'执行',iconCls:ipe.sty.start,handler:this.execute,scope:this},
    		{text:'保存',iconCls:ipe.sty.save,handler:this.save,scope:this},
            {text:'取消',iconCls:ipe.sty.cancel,handler:this.close,scope:this}
        ];
        this.items=[this.vbox];
        this.callParent();
    },save:function(){
    	var me=this;
    	if(this.form.getForm().isValid()){
    		var _titles=[];
    		this.grid.getStore().each(function(r,i){
    			_titles.push({name:r.data.name,field:r.data.field});
    		});
    		
    		if(_titles.length<=0){
    			Ext.Msg.alert('提示','导出标题不能为空!');
    			return;
    		}
    		var params=this.form.getForm().getValues();
    		params.titles=Ext.encode(_titles);
    		
    		var url="exlExptpl/add";
    		if(this.oper=="edit"){
    			url="exlExptpl/edit";
    		}
    		Ext.Ajax.request({
	            url: url,
	            params: params,
	            success: function(response){
	                var resp =Ext.decode(response.responseText) ;
	                if(resp.success){
	                   	me.parent.getStore().load();
	                    me.close();
	                }else{
	                    Ext.Msg.alert('提示',resp.rows);
	                }
	            },
	            scope:this
        	});
    	}else{
    		Ext.Msg.alert('提示','表单参数验证失败');
    	}
    },execute:function(){
    	var me=this;
    	var sqlTpl=this.form.getForm().findField("sql").getValue();
    	if(sqlTpl==""){
    		Ext.Msg.alert('提示','模板SQL不能为空');
    		return;
    	}
    	
    	Ext.Ajax.request({
            url: 'exlExptpl/loadColumns',
            params: {
                sqlTpl:sqlTpl
            },
            success: function(response){
                var resp =Ext.decode(response.responseText) ;
                if(resp.success){
                   me.addRecordToGrid(resp.rows.titles);
                }else{
                    Ext.Msg.alert('提示',resp.rows);
                }
            },
            scope:this
        });
    },addRecordToGrid:function(titles){
    	if(titles!=null){
    		this.grid.getStore().removeAll();
	    	Ext.each(titles,function(r,i){
	    		var record=Ext.create('ExtColumn',{field:r,name:'',sno:i});
	    		this.grid.getStore().add(record);
	    	},this);
    	}
    },setInfo:function(data){
    	this.form.getForm().setValues(data);
    	if(data.titles){
    		var rows=Ext.decode(data.titles);
    		Ext.each(rows,function(r,i){
	    		var record=Ext.create('ExtColumn',r);
		    	this.grid.getStore().add(record);
	    	},this);
    	}
    }
});
