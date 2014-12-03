
Ext.define('Desktop.view.Header', {
    extend : 'Ext.Container',
    region : 'north',
    layout:{type:'vbox',align:'stretch'},
    border:false,
    initComponent : function(){
    	this.title=Ext.create('Ext.Container',{
    	   border:false,
    	   cls:'sys_header',
    	   height:50,
    	   html:'<span class="sys_title bold">'+ipe.config.sysConfig.sysname+'</span>' +
    		'<span class="to-right list-groups">' +
    			/*'<span class="list-item">欢迎您，'+ipe.config.user.userName+'</span>|' +*/
    			'<span class="list-item bold"><a href="javascript:ipe.fuc.changeRole()">切换角色</a></span>|' +
    			'<span class="list-item bold"><a href="javascript:ipe.fuc.upPwd()">修改密码</a></span>|' +
    			'<span class="list-item bold"><a href="javascript:ipe.fuc.logout()">退出系统</a></span>' +
    		'</span>'
    	});
    	this.menu=Ext.create('Ext.Container',{
    		border:false,
    		height:29,
    		html:ipe.config.userMenu
    	});
    	
        this.items=[this.title,this.menu];
        this.on('afterrender',this.showMenubar,this);
        this.callParent();
	},
	showMenubar:function(){
		ddlevelsmenu.setup("ddtopmenubar", "topbar");	
	},
    menuClick:function(ts){
        if(ts.menuUrl){
            var ipeCont=(this.parent.ipeCon);
            var tname_=ts.menuUrl.replace(/\.|\-|_|\//gi,'');
            var sheetId="Tab_Panel_"+tname_;
            if(ipe.config.sysConfig.ena_mtab=="1"){
    			this.mutiTabView(ipeCont,ts,sheetId);
    		}else{
    			this.singTabView(ipeCont,ts,sheetId);
    		}
        }
    },
    singTabView:function(ipeCont,ts,sheetId){
    	var sheet=ipeCont.getComponent(1);
    	if(sheet){
        	Ext.applyIf(ipeCont,{getTabView:this.getTabView});
            var pcontainer=ipeCont.getTabView(ts);
            if(pcontainer==null){
                return;
            }
            sheet.removeAll();
            sheet.setTitle(ts.text);
            sheet.add(pcontainer);
            sheet.mattr=ts;
            ipeCont.setActiveTab(sheet);
    	}else{
    		this.mutiTabView(ipeCont,ts);
    	}
    },
    mutiTabView:function(ipeCont,ts,sheetId){
    	var sheet=Ext.getCmp(sheetId);
        if(sheet){
            ipeCont.setActiveTab(sheetId);
        }else{
            //Ext.get('loading').update('加载系统组件...');
            Ext.applyIf(ipeCont,{getTabView:this.getTabView});
            var pcontainer=ipeCont.getTabView(ts);
            if(pcontainer==null){
                return;
            }
            /////////////
            var ct=true;
            if(ts.closable!=null){
            	ct=ts.closable;
            }
            var panel=Ext.create('Ext.Panel',{
                layout:{type:'fit',align:'stretch'},
                iconCls:ipe.sty.app,
                closable:ct ,
                refreable:true,
                border:false,
                frame:true,
                title:ts.text,
                menuUrl:ts.menuUrl,
                menuType:ts.menuType,
                id:sheetId,
                items:pcontainer,
                mattr:ts
            });
            ipeCont.add(panel);
            ipeCont.setActiveTab(panel);
        }
    },eachMenu:function(menu){
        Ext.each(menu,function(r,i){//绑定方法
            if(r.menu==null|| r.menu.length<1){
                Ext.apply(r,{scope:this,handler:this.menuClick,scale: 'large',rowspan: 3,height:30,shadow:true});
            }else{
                this.eachMenu(r.menu);
            }
        },this);
    },getTabView:function(ts){
        var myMask = new Ext.LoadMask(this, {msg:"正在加载..."});
        myMask.show();
        var pcontainer=null;
        try{
            if(ts.menuType=='1'){//类实例
                pcontainer=Ext.create(
                    ts.menuUrl,{
                        border:false,
                        parent:this
                    });
            }else if(ts.menuType=='0'){//URL路径
                pcontainer=Ext.create(
                    'Ext.panel.Panel',{
                        border:false,
                        parent:this,
                        html:"<iframe width='100%' height='100%' frameborder='0' src='"+ts.menuUrl+"'></iframe>"
                    });
            }
        }catch(e){
        	console.log(e);
            Ext.Msg.alert('提示',"页面不存在或是未完成初始化["+e+"]");
        }finally{
            setTimeout(function(){
                myMask.hide();
            },1000);
        }
        return pcontainer;
    }
});