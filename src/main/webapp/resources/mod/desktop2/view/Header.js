/**
 *标头
 */
Ext.define('Desktop.view.Header', {
    extend : 'Ext.panel.Panel',
    //height : 100,
    region : 'north',
    layout:{type:'hbox',align:'stretch'},
    border:false,
    initComponent : function(){
        ipe.config.userMenu=[
            {width:20,xtype:'tbspacer'},
            {text:'IPE脚手架1.0',xtype:'label',style:{fontSize:16,color:'red'}}
        ].concat(ipe.config.userMenu);
        this.eachMenu(ipe.config.userMenu);

        //任务
        ipe.config.userMenu=ipe.config.userMenu.concat(['->',{
            text:'修改密码',
            handler:this.upPwd
        },{
            text:'退出系统',
            handler:this.logout
        }]);
        ipe.config.userMenu.push({width:50,xtype:'tbspacer'});

        this.bbar = Ext.create('Ext.toolbar.Toolbar',{
            parent:this,
            enableOverflow:true,
            items:ipe.config.userMenu
        });
        this.callParent();
    },
    showTask:function(){

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
            var panel=Ext.create('Ext.Panel',{
                layout:{type:'fit',align:'stretch'},
                iconCls:ipe.sty.app,
                closable:true,
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