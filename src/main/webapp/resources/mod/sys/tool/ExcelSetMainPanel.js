/**
 * Created by tangdu on 14-2-6.
 */

/**
 * Excel导入模板
 */
Ext.define('Sys.tool.ExlImptplList',{
    extend:'Ext.grid.Panel',
    alias : 'widget.exlImptpllist',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载模型数据列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    border:false,
    pageSize : 20,
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},
            {
                header:'模板名称',
                dataIndex:'exlName',
                width:200,
                sortable:true
            },{
                header:'模板编号',
                dataIndex:'exlCode',
                sortable:true
            },{
                header:'模板类型',
                dataIndex:'exlType',
                sortable:true
            },{
                header:'映射表名',
                width:200,
                dataIndex:'mappingTable',
                sortable:true,
                renderer:function(val,cttr,record){
                    cttr.style="color:green;";
                    return val+"(<span style='color:red;font-weight:bold;'>"+record.data.tableCot+"</span>)";
                }
            },{
            	header:'表所属用户',
            	width:80,
            	dataInde:'tableBelongUser',
            	sortable:true
            },{
                header:'Excel开始行',
                width:80,
                dataIndex:'startrowIndex',
                sortable:true
            },{
                header:'Excel开始列',
                width:80,
                dataIndex:'startcolIndex',
                sortable:true
            },{
                header:'Sheet索引',
                width:70,
                dataIndex:'sheetIndex',
                sortable:true
            },{
                header:'状态',
                dataIndex:'enabled',
                sortable:true,
                width:40,
                renderer:ipe.fuc.enabledDt
            },{
                header:'Excel文件',
                width:200,
                dataIndex:'exlFile'
            },{
                header:'创建日期',
                width:150,
                dataIndex:'createdDate',
                sortable:true
            },{
                header:'备注',
                dataIndex:'remark',
                sortable:true
            }];

        this.tbar=[{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addExlImptpl
        },{
            text:'新增',
            iconCls:ipe.sty.add,
            scope:this,
            handler:this.addExlImptpl
        },{
            text:'编辑',
            iconCls:ipe.sty.edit,
            scope:this,
            handler:this.editExlImptpl
        },{
            text:'删除',
            iconCls:ipe.sty.del,
            scope:this,
            handler:this.delExlImptpl
        },'-',{text:'导入',scope:this,handler:this.impExldata,iconCls:ipe.sty["import"]}];

        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'exlImptpl/list',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            autoLoad:true,
            fields : ['exlName','exlCode','exlType','tableBelongUser','mappingTable','startrowIndex','startcolIndex','sheetIndex','enabled','createdDate','remark','exlFile','tableCot']
        });

        this.bbar=Ext.create('Ipe.PagingToolbar',{
            store:this.store,pageSize:this.pageSize
        });

        this.callParent();
    },addExlImptpl:function(){
        var win=Ext.create('Sys.tool.ExcelImpConfigWin',{parent:this,oper:'add'});
        win.show();
    },editExlImptpl:function(){
        var record=this.getSelectionModel().getSelection();
        if(record.length>0){
            var win=Ext.create('Sys.tool.ExcelImpConfigWin',{record:record[0],oper:'edit',parent:this});
            win.show();
        }else{
            Ext.Msg.alert('提示','请选择要编辑的记录!');
        }
    },delExlImptpl:function(){
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
                            url: 'exlImptpl/del',
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
    },impExldata:function(){
        var record=this.getSelectionModel().getSelection();
        if(record.length>0){
            var win=Ext.create('Sys.tool.ExcelImpWin',{parent:this});
            win.show();
            win.setData(record[0]);
        }else{
            Ext.Msg.alert('提示','请选择要执行导入的记录!');
        }
    }
});
/**
 * 录入表单
 */
Ext.define('Sys.tool.ExlImpEditForm',{
    extend:'Ext.FormPanel',
    url:'exlImptpl/impexcel',
    waitTitle:'请稍候....',
    isFileUpload:true,
    frame:true,
    border:false,
    layout:{type:'vbox',align:'stretch'},
    initComponent:function(){
        this.containerView=Ext.widget('container',{parent:this});
        this.defImpType=new Ext.form.ComboBox({
        	fieldLabel:'默认类型',
            store: new Ext.data.SimpleStore({
                fields:['value','text'],
                data: ipe.conifg.excelSupType
            }),
            emptyText: '请选择',
            value:ipe.conifg.defImpType,//默认类型
            hideField:'colType',
            mode: 'local',
            triggerAction: 'all',
            valueField: 'value',
            displayField: 'text',
            editable: true
        });
        this.enasetCol=Ext.create('Ext.form.Checkbox',{fieldLabel:'表头作表字段'});
        this.setCol=new Ext.form.NumberField({
           fieldLabel:'对应标题行',
           hidden:true,
           value:1
        });
        this.items=[
            {xtype:'fieldset',title:'Excel上传',layout:'column',flex:2,items:[
                {columnWidth:.25,fieldLabel:'Sheet索引',allowBlank:false,xtype:'numberfield',name:'sheetIndex',value:1},
                {columnWidth:.25,layout:'form',xtype: 'container',items:[this.defImpType]},
                {columnWidth:.25,layout:'form',xtype: 'container',items:[this.enasetCol]},
            	{columnWidth:.25,layout:'form',xtype: 'container',items:[this.setCol]},
                {columnWidth:.9,fieldLabel:'文件',allowBlank:false,clearOnSubmit:false,xtype:'filefield',buttonText:"浏览",name:'file',anchor:'95%'},
                {columnWidth:.1,xtype:'button',text:'上传',iconCls:ipe.sty.up,handler:this.uploadExcel,scope:this}]},
            {xtype:'fieldset',title:'预览',flex:9,items:this.containerView,autoScroll:true,layout:'fit'}];
        this.callParent();
        this.enasetCol.on('change',this.showSeetCol,this);
    },showSeetCol:function(th,checked){
    	if(checked){
    		this.setCol.show();
    	}else{
    		this.setCol.hide();
    	}
    },uploadExcel:function(){
        var me=this;
        var file=this.getForm().findField("file").getValue();
        var reg_=/\.xlsx$/gi;
        if(!reg_.test(file)){
            Ext.Msg.alert('提示','上传Excel只能为07及上版本!');
            return;
        }
        var _ensetCol=this.enasetCol.getValue();
        var _tindex= this.setCol.getValue();
        var _columns=null;
        this.getForm().submit({
        	 waitMsg:'正在提交....',
            //clientValidation: false,
            success:function(a,re){
                me.containerView.getEl().update("");
                var result=re.result;
                var tables="<table style='border-collapse:collapse;width:97%;'>";
                var data=result.rows.data;
                var rwsize=data.length;
                var clsize=result.rows.collength;
                me.collength=clsize;//判断是否可以下一步
                for(i=0;i<rwsize;i++){
                    tables+="<tr>";
                    if(_tindex==(i+1)){
                    	_columns=data[i];
                    }
                    for(j=0;j<clsize;j++){
                    	var _at=data[i][j];
                    	if(typeof _at=='undefined'){
                    		_at="";
                    	}
                        tables+="<td style='border:1px #000000 solid;'>"+_at+"</td>";
                    }
                    tables+="</tr>";
                }
                tables+="</table>";
                var header="<div>系统统计有效行数为：<span style='color:red'>"+data.length
                    +"</span> | 有效列数为：<span style='color:red'>"+clsize+"</span>"
                    +"</span> | 显示前：<span style='color:red'>500</span>条</div><br>";
                me.containerView.getEl().update(header+tables);
                if(_ensetCol){
                	me.enableSetCol=true;
                }
                me.setColumns=_columns;
                //清空原先列表
                me.parent.card2.list.getStore().removeAll();
            },
            failure: function(form, action) {
                Ext.Msg.alert('提示', '导入失败');
            }
        });
    }
});

Ext.define('Sys.tool.ExlImpEditList',{
    extend:'Ext.grid.Panel',
    enableColumnMove : false,
    columnLines : true,
    viewConfig : {
        loadingText : '正在加载列表',
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    pageSize : 20,
    plugins:[
        Ext.create('Ext.grid.plugin.CellEditing',{
            clicksToEdit:1
        })
    ],
    initComponent:function(){
        this.columns=[{xtype: 'rownumberer'},{
            header:'Excel索引',
            width:200,
            dataIndex:'exlCol',
            editor:{xtype:'numberfield',maxLength:50}
        },{
            header:'表字段',
            width:250,
            dataIndex:'tableCol',
            editor:{xtype:'textfield',maxLength:30}
        },{
            header:'字段类型',
            width:150,
            dataIndex:'colType',
            editor:new Ext.form.ComboBox({
                store: new Ext.data.SimpleStore({
                    fields:['value','text'],
                    data:ipe.conifg.excelSupType
                }),
                emptyText: '请选择',
                hideField:'colType',
                mode: 'local',
                triggerAction: 'all',
                valueField: 'value',
                displayField: 'text',
                editable: true
            })
        },{
            header:'默认值',
            width:150,
            dataIndex:'defValue',
            editor:{}
        },{dataIndex:'id',hidden:true},{dataIndex:'exlType',hidden:true,value:'01'}];

        Ext.define('ExlImpDetails',{
            extend: 'Ext.data.Model',
            fields : ['id','exlCol','defValue','colType','tableCol']
        });
        this.store=Ext.create('Ext.data.JsonStore', {
            proxy: {
                type: 'ajax',
                url: 'exlImptplDetailes/getByTplId',
                reader: {
                    root: 'rows'
                }
            },
            remoteSort : true,
            model:'ExlImpDetails'
        });
        this.tbar=[
            {text:'添加',iconCls:ipe.sty.add,scope:this,handler:this.addRow},
            {text:'删除',iconCls:ipe.sty.del,scope:this,handler:this.delRow},'->',
            {text:'(注：日期格式：yyyy-MM-dd HH:mm:ss | Double格式请将excel内容格式设置数值，以免导入时损失精度)',xtype:'label',style:{color:'red'}}
        ];
        this.callParent();
    },addRow:function(title,defType){
        var cot=this.getStore().getCount()+1;
        var _tableCol='po'+cot;
        if(title!=null){
        	_tableCol=title;
        }
        if(defType==null || defType==""){
        	defType=ipe.conifg.defImpType;
        }
        var record=Ext.create('ExlImpDetails',{
            id:'',exlCol:cot,tableCol:_tableCol,colType:defType,defValue:''
        });
        this.getStore().add(record);
    },delRow:function(){
        Ext.Msg.confirm('提示','确定要删除？',function(btn){
            if(btn=='yes'){
                var sm = this.getSelectionModel().getSelection();
                if(sm.length>0){
                    this.getStore().remove(sm[0]);
                }
            }
        },this);
    }
});

Ext.define('Sys.tool.ExlImptplEditForm',{
    extend:'Ext.FormPanel',
    url:'exlImptpl/add',
    waitTitle:'请稍候....',
    defaults:{
        anchor:'98%'
    },
    isFileUpload:true,
    frame:true,
    border:false,
    oper:'add',
    layout:{type:'vbox',align:'stretch'},
    initComponent:function(){
        this.list=Ext.create('Sys.tool.ExlImpEditList',{flex:.7});
        this.items=[
            {xtype:'fieldset',title:'详细信息',layout:'column',flex:.3,items:[
                {columnWidth:.33,xtype:'container',frame:true,border:false,items:[
                    {
                        fieldLabel:'模板名称',
                        xtype:'textfield',
                        allowBlank:false,
                        originalValue:'2',
                        name:'exlName'
                    },{
                        fieldLabel:'模板编号',
                        xtype:'textfield',
                        allowBlank:false,
                        name:'exlCode'
                    }
                ]},{columnWidth:.33,xtype:'container',frame:true,border:false,items:[
                    {
                        fieldLabel:'状态',
                        xtype:'combo',
                        name:'enabled',
                        store:ipe.store.enabledStore,
                        value:'1',
                        displayField:'value',
                        valueField:'key',
                        hiddenName:'enabled',
                        triggerAction:'all',
                        editable:false,
                        queryMode:'local'
                    },{
                        fieldLabel:'映射表名',
                        xtype:'textfield',
                        allowBlank:false,
                        name:'mappingTable'
                    }
                ]},{columnWidth:.33,xtype:'container',frame:true,border:false,items:[
                    {
                        fieldLabel:'Excel开始行',
                        xtype:'numberfield',
                        value:2,
                        allowBlank:false,
                        name:'startrowIndex'
                    },{
                        fieldLabel:'Excel开始列',
                        xtype:'numberfield',
                        value:1,
                        allowBlank:false,
                        name:'startcolIndex'
                    },{
                        fieldLabel:'Sheet索引',
                        xtype:'hidden',
                        allowBlank:false,
                        name:'sheetIndex'
                    },{
                        fieldLabel:'上传Excel文件名',
                        xtype:'hidden',
                        allowBlank:false,
                        name:'exlFile'
                    }
                ]},{
                    columnWidth:1,
                    fieldLabel:'备注',
                    xtype:'textarea',
                    allowBlank:true,
                    name:'remark'
                },{xtype:'hidden',name:'id'}]},this.list];
        this.callParent();
    },setData:function(record){
        this.loadRecord(record);
    }
});

Ext.define('Sys.tool.ExcelImpConfigWin',{
    extend:'Ext.Window',
    title:'模板设置',
    layout:'card',
    modal:true,
    width:900,
    height:600,
    border:false,
    initComponent:function(){
        this.card1=Ext.create('Sys.tool.ExlImpEditForm',{parent:this});
        this.card2=Ext.create('Sys.tool.ExlImptplEditForm',{parent:this});
        this.items=[this.card1,this.card2];

        if(this.oper=="edit"){
            this.buttons=['->',
                {text:'保存',iconCls:ipe.sty.save,id:'move-save',disabled:true,handler:this.saveData,scope:this},
                {text:'取消',iconCls:ipe.sty.cancel,handler:this.close,scope:this}
            ];
        }else{
            this.buttons=['->',
                {text:'保存',iconCls:ipe.sty.save,id:'move-save',disabled:true,handler:this.saveData,scope:this},'-',
                {text:'上一步',handler:this.prevStep,scope:this,id:'move-prev',disabled:true,iconCls:ipe.sty.prev},'-',
                {text:'下一步',handler:this.nextStep,scope:this,id:'move-next',iconCls:ipe.sty.next}];
        }
        this.on('show',this.editShow,this);
        this.callParent();
    },editShow:function(){ //编辑状态
        if(this.oper=="edit"){
            Ext.getCmp('move-save').setDisabled(false);
            this.getLayout()['next']();
            this.card2.setData(this.record);
            this.card2.list.getStore().load({params:{tplId:this.record.data.id}});
        }
    },prevStep:function(){
        this.getLayout()['prev']();
        Ext.getCmp('move-save').setDisabled(true);
        Ext.getCmp('move-prev').setDisabled(true);
        Ext.getCmp('move-next').setDisabled(false);
        //this.card1.collength=null;//清空标记
        //this.card1.setColumns=null;
    },nextStep:function(){
        if(this.card1.collength && this.card1.getForm().isValid()){
            this.getLayout()['next']();
            Ext.getCmp('move-save').setDisabled(false);
            Ext.getCmp('move-prev').setDisabled(false);
            Ext.getCmp('move-next').setDisabled(true);
            this.card2.getForm().findField("sheetIndex").setValue(this.card1.getForm().findField("sheetIndex").getValue());
            var file=this.card1.getForm().findField("file").getValue();
            this.card2.getForm().findField("exlFile").setValue(file.substring(file.lastIndexOf("\\")+1));
            var defType=this.card1.defImpType.getValue();
            /////////////
            if(this.card2.list.getStore().getCount()==0){
                for(i=0;i<this.card1.collength;i++){
                	if(this.card1.enableSetCol && this.card1.setColumns){
                		this.card2.list.addRow(this.card1.setColumns[i],defType);
                	}else{
                		this.card2.list.addRow(null,defType);
                	}
                }
            }
        }else{
        	Ext.Msg.alert('提示','请点击上传文件');
        }
        //////////////
        this.card1.collength=null;//清空标记
        this.card1.setColumns=null;
    },saveData:function(){
        var me=this;
        if(this.card2.list.getStore().getCount()==0){
            return;
        }
       
        if(this.card2.getForm().isValid()){
            var details=[];
            var valid=false;
            this.card2.list.getStore().each(function(record){
                if(record.data.exlCol==""||record.data.tableCol==""){
                    valid=true;
                    Ext.Msg.alert('提示', 'Excel列或是表字段不能为空!');
                    return false;
                }
                details.push(record.data);
            },this);

            if(valid){
                return;
            }
            if(this.oper=="add"){
                this.card2.getForm().submit({
                	waitMsg:'正在提交....',
                    params:{
                        details:Ext.encode(details)
                    },
                    success:function(re){
                        Ext.Msg.alert('提示', '保存成功');
                        me.close();
                        me.parent.getStore().load();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '保存失败');
                    }
                });
            }else if(this.oper=="edit"){
                this.card2.getForm().submit({
                	waitMsg:'正在提交....',
                    url:'exlImptpl/edit',
                    params:{
                        details:Ext.encode(details)
                    },
                    success:function(re){
                        Ext.Msg.alert('提示', '修改成功');
                        me.close();
                        me.parent.getStore().load();
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('提示', '修改失败');
                    }
                });
            }
        }else{
            ipe.FormMsg();
        }
    }
});

Ext.define('Sys.tool.ExcelImpWin',{
    extend:'Ext.Window',
    title:'导入数据',
    modal:true,
    width:400,
    height:200,
    waitTitle:'请稍候',
    waitMsg:'正在提交....',
    layout:'fit',
    border:false,
    initComponent:function(){
        this.form=Ext.create('Ext.FormPanel',{
            url:'exlImptpl/impexcelData',
            waitTitle:'请稍候....',
            defaults:{
                anchor:'98%'
            },
            isFileUpload:true,
            frame:true,
            border:false,
            bodyPadding:5,
            items:[
                {fieldLabel:'模板编号',xtype:'textfield',name:'exlCode',readOnly:true,cls:'textReadOnly'},
                {fieldLabel:'Sheet索引',xtype:'textfield',name:'sheetIndex',readOnly:true,cls:'textReadOnly'},
                {fieldLabel:'数据表',xtype:'textfield',name:'mappingTable',readOnly:true,cls:'textReadOnly'},
                {fieldLabel:'文件',xtype:'filefield',buttonText:"浏览",name:'file',clearOnSubmit:false,allowBlank:false},{name:'id',xtype:'hidden'}]
        });
        this.buttons=[{text:'确定',iconCls:ipe.sty.valid,scope:this,handler:this.impData},{text:'取消',iconCls:ipe.sty.cancel,scope:this,handler:this.close}];
        this.items=[this.form];
        this.callParent();
    },setData:function(record){
        this.form.loadRecord(record);
    },impData:function(){
        var me=this;
        if(this.form.getForm().isValid()){
            this.form.getForm().submit({
            	 waitMsg:'正在提交....',
                success:function(a,r){
                    var result= Ext.decode(r.result.rows)
                    Ext.Msg.alert('提示','导入成功<br>'+'总条数：'+result.total+'<br>成功条数:'+result.successCot+"<br>失败条数:"+result.failureCot);
                    me.close();
                    me.parent.getStore().load();
                },failure: function(form, action) {
                    Ext.Msg.alert('提示', '导入失败');
                }
            });
        }
    }
});


Ext.define("Sys.tool.ExcelSetMainPanel",{
    extend:'Ext.Panel',
    layout:{type:'fit',align:'stretch'},
    initComponent:function(){
        this.exlImptplList=Ext.create('Sys.tool.ExlImptplList');

        this.items=[this.exlImptplList];
        this.callParent();
    }
});