/**
 * 系统常量
 * @type {{conifg: {}, fuc: {flagDt: Function}, store: {flagStore: *}}}
 */
var ipe={
	viewID:'IPE_Viewport_ID',//系统TAB全局ID-不要重名(唯一ID命名)
    sty:{
        add:'btn_add',
        app:'btn_app',
        branch:'btn_branch',
        bug:'btn_bug',
        calendar:'btn_calendar',
        cancel:'btn_cancel',
        chart:'btn_chart',
        check:'btn_check',
        clear:'btn_clear',
        clock:'btn_clock',
        clock:'btn_clock',
        comment:'btn_comment',
        del:'btn_del',
        down:'btn_down',
        edit:'btn_edit',
        fav:'btn_fav',
        gmail:'btn_gmail',
        help:'btn_help',
        home:'btn_home',
        'import':'btn_import',
        'bell':'btn_bell',
        'export':'btn_export',
        info:'btn_info',
        'interface':'btn_interface',
        lock:'btn_lock',
        mail:'btn_mail',
        mailadd:'btn_mailadd',
        mailbox:'btn_mailbox',
        maildel:'btn_maildel',
        mailinfo:'btn_mailinfo',
        mailold:'btn_mailold',
        mailompose:'btn_mailompose',
        mailopen:'btn_mailopen',
        mailre:'btn_mailre',
        mails:'btn_mails',
        mailve:'btn_mailve',
        mialwarn:'btn_mialwarn',
        monitor:'btn_monitor',
        mouse:'btn_mouse',
        news:'btn_news',
        next:'btn_next',
        prev:'btn_prev',
        printer:'btn_printer',
        process:'btn_process',
        redo:'btn_redo',
        refresh:'btn_refresh',
        reset:'btn_reset',
        save:'btn_save',
        scriptcode:'btn_scriptcode',
        'set':'btn_set',
        start:'btn_start',
        style:'btn_style',
        submit:'btn_submit',
        target:'btn_target',
        Trash:'btn_Trash',
        turn:'btn_turn',
        undo:'btn_undo',
        up:'btn_up',
        user:'btn_user',
        valid:'btn_valid',
        view:'btn_view',
        warn:'btn_warn',
        zoomin:'btn_zoomin',
        zoomout:'btn_zoomout',
        query:'btn_query'
    },
    config:{
        userMenu:[],
        sysConfig:{},
        dictData:{},
        user:{},
        basePath:'',
        paddingWidth:20
    },
    exl:{//excel配置-MySQL
    	defImpType:'varchar(100)',
        excelSupType:[
            ['varchar(100)','varchar(100)'],
            ['timestamp','timestamp'],
            ['integer','integer'],
            ['double(24,8)','number(24,8)']
        ]
    },
    fuc:{
    	openWidow:function(url,title){
    		return window.open(url, title, "height=700, width=815,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
    	},
        downFile:function(filePath){
            if(filePath!=null && filePath!=""){
                filePath=encodeURI(encodeURI(filePath));
                window.location.href=_basePath+"/downFile?filePath="+filePath;
            }
        },
        /*enabledDt:function(val){
            if(val=='0'){
                return '<span style="color:red;font-weight:bold;">禁用</span>'
            }else{
                return '<span style="color:green;font-weight:bold;">启用</span>'
            }
        },
        flagDt:function(val){
            if(val=='0'){
                return '<span style="color:red;font-weight:bold;">否</span>'
            }else{
                return '<span style="color:green;font-weight:bold;">是</span>'
            }
        },*/
        menuDt:function(val){
            if(val=='0'){
                return 'URL'
            }else if(val=='1'){
                return 'Component'
            }
        },
        resourceDt:function(val){
            if(val=='0'){
                return 'METHOD'
            }else if(val=='1'){
                return 'URL'
            }
        },
        openTab:function(murl,mtype,mtext,cls,record){
        	mtext=mtext||'';
        	mtype=mtype||'1';
        	cls=cls||'';
        	if(murl){
        		Ext.getCmp(ipe.viewID).ipeNav
					.treeClick(null,{childNodes:[],data:{
						menuUrl:murl,
						menuType:mtype,
						cls:cls,
						record:record,
						text:mtext}
				});
        	}
        },
        openMenu2:function(r){
        	if(r){
        		Ext.getCmp(ipe.viewID).ipeHd.menuClick(r);
        	}
        },
        logout:function(){
	        window.location.href="logout";
	    },
	    upPwd:function(){
	        var win=Ext.create('Ipe.view.UpPwdWin');
	        win.show();
	        win.form.getForm().findField('userName').setValue(user.userName);
	    },
	    changeRole:function(){
	    	var win=Ext.create('Ipe.view.ChangeRoleWin');
	        win.show();
	    },
	    refreshIndexView:function(panel){
	    	panel.body.update("正在加载.......");
	    	Ext.Ajax.request({
	    		url:'getIndexView',
	    		method:'POST',
	    		success:function(resp){
	    			Ext.Loader.loadScript('resources/mod/portal.js');
	    			panel.body.update(resp.responseText);
	    		}
	    	});   
	    }
    },
    store:{
        /*enabledStore:Ext.create('Ext.data.Store',{
            fields:['key','value'],
            data:[{key:'1',value:'启用'},{key:'0',value:'禁用'}]
        }),
        flagStore:Ext.create('Ext.data.Store',{
            fields:['key','value'],
            data:[{key:'1',value:'是'},{key:'0',value:'否'}]
        }),*/
        menuStore:Ext.create('Ext.data.Store',{
            fields:['key','value'],
            data:[{key:'1',value:'Component'},{key:'0',value:'URL'}]
        }),
        resourceStore:Ext.create('Ext.data.Store',{
            fields:['key','value'],
            data:[{key:'1',value:'URL'},{key:'0',value:'METHOD'}]
        })
    }
}
////初始化-字典数据///
function init(){
	Ext.each(ipe.config.dictData,function(r,i){
		//step1:初始化STORE
		var arr=[];
        if(r.dictVals!=null){
        	Ext.each(r.dictVals,function(r2,i2){
        		arr.push({key:r2.dictValCode,value:r2.dictValName});
        	});
        }
        ipe.store[r.dictCode+"Store"]=Ext.create('Ext.data.Store',{
            fields:['key','value'],
            data:arr
        })
        //step2:初始化FUC
        ipe.fuc[r.dictCode+"Dt"]=function(v){
        	return (function(){
        		this.data=arr;
	        	for(var i=0;i<arr.length;i++){
	        		if(arr[i].key==v){
	        			return arr[i].value;
	        		}
	        	}
	        	return "";
        	})();
        }
	});
	//console.log(ipe.store);
	//console.log(ipe.fuc);
	//console.log(ipe.fuc.enabledDt('01'));
}

Ext.Loader.setConfig({
    enabled : true,
    scriptCharset:'UTF-8',
    disableCaching:true,
    paths:{
        'Ext.ux' : _basePath + '/resources/extjs4/ux',
        'Ipe' : _basePath+'/resources/mod',
        'Sys':_basePath+'/resources/mod/sys',
        'Bpm':_basePath+'/resources/mod/bpm'
    }
});

/**
 * 分页条
 */
Ext.define('Ipe.PagingToolbar',{
    extend:'Ext.PagingToolbar',
    alias : 'widget.ipetoolbar',
    pageSize:20,
    store:this.store,
    displayInfo:true,
    beforePageText:'第',
    afterPageText:'页- 共{0}页',
    displayMsg:'本页显示{0}-{1}条信息,总共{2}条',
    emptyMsg:'暂无数据',
    scope:this,
    prependButtons:true,
    initComponent:function(){
        /*var grid=this.parent;
        if(grid && grid.enabledSearch){
            if(grid.columns!=null){
                grid.searchField=Ext.create('Ext.form.Hidden');
                var fields=[];
                Ext.each(grid.columns,function(r,i){
                    if(r.header!=null && r.header!=""){
                        fields.push({
                            text:r.header,
                            field: r.dataIndex,
                            checked: false,
                            group: 'header',
                            listeners:{
                                scope:this,
                                click:function(me){
                                    grid.searchField.setValue(me.field);
                                }
                            }
                        });
                    }
                });
                grid.searchInput=Ext.create('Ext.form.TriggerField',{
                    selectOnFocus: true,
                    width:120,
                    trigger1Cls: 'x-form-clear-trigger',
                    trigger2Cls: 'x-form-search-trigger',
                    emptyText:'检索条件',
                    hideTrigger:true,
                    enableKeyEvents:true,
                    listeners:{
                        scope:this,
                        keyup:function(f,e){
                            grid.searchInput.setHideTrigger(false);
                            if (Ext.EventObject.ENTER == e.getKey()) {
                                grid.getStore().load();
                            }
                        }
                    },
                    onTrigger1Click:function(){
                        grid.searchInput.reset();
                        grid.searchInput.setHideTrigger(true);
                    },onTrigger2Click:function(){
                        grid.getStore().load();
                    }
                });

                this.items=['',{menu:fields,text:'查询',iconCls:ipe.sty.view},grid.searchInput,grid.searchField,'','-'];
            }
        }*/
        this.callParent();
    }
});

/**
 *  必填项标识
 */
Ext.override(Ext.form.field.Text,{
    initComponent:function(){
        if (this.allowBlank === false && this.fieldLabel) {
            this.afterLabelTextTpl = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
        }
        this.callParent();
    }
});
Ext.override(Ext.form.ComboBox,{
    emptyText:'请选择'
});
//submitEmptyText 不将空值的元素提交-emptyText
Ext.override(Ext.form.action.Submit,{
    submitEmptyText:false
});
Ext.override(Ext.grid.Panel,{
    viewConfig: {
        enableTextSelection: true,
        scrollOffset:0,
        autoFit: true,
        forceFit:true,
        enableRowBody:true
    },
    autoScroll:false,
    enableColumnMove:false,
    columnLines:true,
    pageSize : 20,
    enabledSearch:false
});
Ext.define('Ipe.Panel',{
	extend:'Ext.Panel'
});
Ext.override(Ipe.Panel,{
	beforeRender:function(){
		this.callParent();
		if(ipe.config.user.admin==="1"){
			//管理员不做过滤
		}else{
			var tools=Ext.ComponentQuery.query("panel>toolbar");
			Ext.each(tools,function(r,i){
				//console.log(r);
				if(r.xtype==='toolbar'){//TODO 排除其它组件干扰
					if(r.items!=null && r.items.items!=null){
						//1 先将按钮隐藏
						Ext.each(r.items.items,function(r2,i2){
							if(r2.itemId){
								r2.hide();
								//delete r2;
							}
						});
						//2 再显示按钮
						Ext.each(ipe.config.authorits,function(r2,i2){
							if(r.getComponent(r2)){
								r.getComponent(r2).show();
							}
						});
					}
				}
			});
		}
	}
});
Ext.override(Ext.Window,{
    constrain:true,	        // 整个窗体都不能移出浏览器
    constrainHeader:true	// 窗体标题栏不能移出浏览器
});
Ext.override(Ext.data.proxy.Ajax,{
    actionMethods:{create: "POST", read: "POST", update: "POST", destroy: "POST"}
});
Ext.override(Ext.grid.column.RowNumberer,{
    width:30
});
Ext.override(Ext.form.action.Action,{
    waitTitle:'请稍候.....',
    waitMsg:'正在提交数据...'
});
Ext.override(Ext.form.field.Base,{
    readOnlyCls:'m_textReadOnly'
});
Ext.override(Ext.form.field.HtmlEditor,{
    fontFamilies: [
        'Arial',
        'Courier New',
        'Tahoma',
        'Times New Roman',
        'Verdana',
        '宋体'
    ],
    defaultValue:'Arial'
});
Ext.override(Ext.button.Button,{
	fireHandler:function(e){
		var me = this;
		this.callParent(arguments);
        if(this.xtype!='tab' && this.xtype!='filebutton'){//对tab-上传按钮有干扰
			me.setDisabled(true);
			var task=new Ext.util.DelayedTask(function(){
				me.setDisabled(false);
			});
			task.delay(1000);
        }
	}
});
/*Ext.Ajax.on('requestexception', function(conn, response, options) {
    var msg = '访问系统资源时发生异常<br/>' + '异常状态:' + response.status + '('
        + response.statusText + ')<br/>' + '异常信息:'
        + response.responseText;
    console.log(msg);
    Ext.Msg.show({
        title : '系统异常',
        msg : msg,
        width : 400,
        icon : Ext.MessageBox.ERROR,
        buttonText : {
            ok : '&nbsp;&nbsp;提交错误报告&nbsp;&nbsp;'
        },
        buttons : Ext.MessageBox.OKCANCEL
    });
});

Ext.Ajax.on('requestcomplete',function(conn,response,options){
    if(typeof(response.getResponseHeader) != "undefined"){
        var sessionStatus = response.getResponseHeader("sessionStatus");

        if(sessionStatus){
            Ext.Msg.show({
                title : '系统异常',
                msg : '会话超时，请重新登录!',
                width : 400,
                icon : Ext.MessageBox.ERROR,
                buttonText : {
                    ok : '确定'
                },
                buttons : Ext.MessageBox.OK
            });
        }
    }
});*/

/**
 * 修改密码
 */
Ext.define('Ipe.view.UpPwdWin', {
    title : '修改密码',
    plain:true,
    extend:'Ext.Window',
    width : 320,
    height : 200,
    modal:true,
    layout:'fit',
    closeAction:'hide',
    buttonAlign:'center',
    resizable:false,
    border:false,
    initComponent : function() {
        this.form=Ext.create('Ext.form.Panel',{
            labelWidth : 60,
            border:false,
            frame:true,
            url:'user/uppwd',
            bodyStyle:'padding:10px;',
            defaultType: 'textfield',
            defaults:{
		        anchor:'98%'
		    },
            items:[
                {fieldLabel:'用户名',value:ipe.config.user.userName,name:'userName',readOnly:true},
                {fieldLabel:'原密码',allowBlank:false,inputType:'password',name:'ouserPwd'},
                {fieldLabel:'新密码',allowBlank:false,inputType:'password',name:'userPwd'},
                {fieldLabel:'重复密码',allowBlank:false,inputType:'password',name:'userPwd2'}
            ]
        })

        this.items=[this.form];
        this.buttons = [{
            text : "确定",
            iconCls:ipe.sty.save,
            handler:this.submitFrom,
            scope : this
        }, {
            text : "关闭",
            scope : this,
            iconCls:ipe.sty.cancel,
            handler:this.close
        }];

        this.callParent();
    },
    submitFrom:function(){
        var me=this;
        var uform=this.form.getForm();
        if(uform.isValid()){
            uform.submit({
                success: function(form, action) {
                    var result=action.result;
                    if(result.success){
                        Ext.Msg.alert('提示', '修改成功');
                    }else{
                        Ext.Msg.alert('提示', result.rows);
                    }
                    me.close();
                },
                failure: function(form, action) {
                    Ext.Msg.alert('提示', '修改失败');
                }
            });

        }
    }
});

/**
 * 切换角色
 */
Ext.define('Ipe.view.ChangeRoleWin', {
    title : '切换角色',
    plain:true,
    extend:'Ext.Window',
    width : 300,
    height : 200,
    modal:true,
    layout:'fit',
    closeAction:'hide',
    buttonAlign:'center',
    resizable:false,
    border:false,
    initComponent : function() {
    	
    	this.buttons = [{
            text : "确定",
            iconCls:ipe.sty.save,
            handler:this.submitFrom,
            scope : this
        }, {
            text : "关闭",
            scope : this,
            iconCls:ipe.sty.cancel,
            handler:this.close
        }];
    	this.callParent();
    },submitFrom:function(){
    	
    }
});

//TODO 自定义消息弹出框
ipe.Msg=function(){
    this.show=function(info){
        
    }
    this.hide=function(msgId){
       
    }
    return this;
}();
ipe.Alert=function(){
    this.show=function(title,content){
       
    }
    return this;
}();
ipe.FormMsg=function(){
    ipe.Alert.show('提示','必填项为空或是输入值受限！');
}

//扩展ExtJS提醒方法
Ext.override(Ext.Msg,{
	error:function(msg){
		Ext.Msg.show({
            title : '提示',
            msg : msg||'',
            buttons : Ext.Msg.OK,
            icon : Ext.Msg.ERROR
        });
	},
	success:function(msg){
		Ext.Msg.show({
            title : '提示',
            msg : msg||'',
            buttons : Ext.Msg.OK,
            icon : Ext.Msg.INFO
        });
	},
	warnin:function(msg){
		Ext.Msg.show({
            title : '提示',
            msg : msg||'',
            buttons : Ext.Msg.OK,
            icon : Ext.Msg.WARNING
        });
	}
});