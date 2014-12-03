/**
 * 导航
 */
Ext.define('Desktop.view.Navigation', {
    alias: 'widget.ipeNavigation',
    extend : 'Ext.panel.Panel',
    region : 'west',
    title : '系统菜单',
    iconCls:ipe.sty.monitor,
    width : 220,
    maxWidth:250,
    minWidth:220,
    autoScroll : false,
    layout : 'accordion',
    showSwitch:true,
    collapsible : true,
    layoutConfig : {
        animate : true
    },
    split : true,
    initComponent : function(){
        this.items=[];
        var menus=Ext.decode(ipe.config.userMenu);
        this.makeTreeMenu(menus);
        this.makeTree(menus);
        this.callParent();
    },makeTreeMenu:function(menu){
        Ext.each(menu,function(r,i){
            if(r.menu && r.menu.length>0){
                r.children= Ext.clone(r.menu);
                // r.menu=null;
                this.makeTreeMenu(r.children);
            }else{
                Ext.apply(r,{scope:this,handler:this.menuClick});
            }
        },this);
    },makeTree:function(menu){
        //绑定方法
        Ext.each(menu,function(r,i){
            /*if(r.menu==null|| r.menu.length<1){
            }else{
                var store = Ext.create('Ext.data.TreeStore', {
                    root: {
                        expanded: true,
                        children: r.children
                    },
                    fields:['text','leaf','jsClass',"menuUrl","menuType"]
                });
                var treePanel=Ext.create('Ext.TreePanel',{store:store,rootVisible: false,title:r.text});
                treePanel.on('itemdblclick',this.treeClick,this);
                this.items.push(treePanel);
            }*/
        	var store = Ext.create('Ext.data.TreeStore', {
                root: {
                    expanded: true,
                    children: r.children
                },
                fields:['text','leaf','jsClass',"menuUrl","menuType"]
            });
            var treePanel=Ext.create('Ext.TreePanel',{store:store,rootVisible: false,title:r.text});
            treePanel.on('itemclick',this.treeClick,this);
            this.items.push(treePanel);
        },this);
    },treeClick:function(view,record){
        if(record.childNodes.length==0 && this.showSwitch){
            var ts=record.data;
            if(ts.menuUrl){
            	this.showSwitch=false;
                var ipeCont=(this.parent.ipeCon);
                var tname_=ts.menuUrl.replace(/\.|\-|_|\//gi,'');
                var sheetId="Tab_Panel_"+tname_;
                if(ipe.config.sysConfig.ena_mtab=="1"){
        			this.mutiTabView(ipeCont,ts,sheetId);
        		}else{
        			this.singTabView(ipeCont,ts,sheetId);
        		}
            }
        }
    },getTabView:function(ts){
        var myMask = new Ext.LoadMask(this, {msg:"..."});
        var me=this;
        myMask.show();
        var pcontainer=null;
        try{
            if(ts.menuType=='1'){//类实例
                pcontainer=Ext.create(
                	ts.menuUrl,{
	                	bodyCls:'defaultColor',
	                    border:false,
	                    parent:this
                });
            }else if(ts.menuType=='0'){//URL路径
                pcontainer=Ext.create(
                'Ext.panel.Panel',{
                    border:true,
                    parent:this,
                    html:"<iframe width='100%' height='100%' frameborder='0' src='"+ts.menuUrl+"'></iframe>"
                });
            }
        }catch(e){
            Ext.Msg.alert('提示',"页面不存在或是未完成初始化["+e+"]");
        }finally{
            setTimeout(function(){
                myMask.hide();
                me.nav.showSwitch=true;
            },1000);
        }
        return pcontainer;
    },singTabView:function(ipeCont,ts,sheetId){
    	var sheet=ipeCont.getComponent(1);
    	if(sheet){
        	Ext.applyIf(ipeCont,{getTabView:this.getTabView,nav:this});
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
    },mutiTabView:function(ipeCont,ts,sheetId){
    	var sheet=Ext.getCmp(sheetId);
    	if(sheet){
            ipeCont.setActiveTab(sheet);
        }else{
            Ext.applyIf(ipeCont,{getTabView:this.getTabView,nav:this});
            var pcontainer=ipeCont.getTabView(ts);
            if(pcontainer==null){
                return;
            }
            
            var panel=Ext.create('Ext.Panel',{
                layout:{type:'fit',align:'stretch'},
                iconCls:(ts.cls==null ? ipe.sty.app:(ts.cls=='' ? ipe.sty.app :ts.cls)),
                closable:(ts.text=="首页" ? false:true),
                refreable:true,
                border:(ts.text=="首页" ? true:false),//双重边框问题fix
                //frame:true,
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
    }
});