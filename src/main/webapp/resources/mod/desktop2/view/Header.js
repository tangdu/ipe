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
        ipe.conifg.userMenu=[
            {width:20,xtype:'tbspacer'},
            {text:'IPE脚手架1.0',xtype:'label',style:{fontSize:16,color:'red'}}
        ].concat(ipe.conifg.userMenu);
        this.eachMenu(ipe.conifg.userMenu);

        //任务
        ipe.conifg.userMenu=ipe.conifg.userMenu.concat(['->',{
            text:'待办项',
            iconCls:ipe.sty.comment,
            handler:this.showTask
        }]);
        //日历
        /*ipe.conifg.userMenu=ipe.conifg.userMenu.concat(['-',{
            text:'办公日历',
            iconCls:ipe.sty.calendar,
            handler:this.workCanlendar
        }]);*/

        //用户
        ipe.conifg.userMenu=ipe.conifg.userMenu.concat(['-',{
            text:user.userName,
            iconCls:ipe.sty.user,
            menu:[
                {text:'修改密码',scope:this,handler:this.upPwd},
                {iconCls : 'icon-power-off',text:'退出',handler:this.logout}
            ]
        }]);
        ipe.conifg.userMenu.push({width:50,xtype:'tbspacer'});

        this.bbar = Ext.create('Ext.toolbar.Toolbar',{
            parent:this,
            enableOverflow:true,
            items:ipe.conifg.userMenu
        });
        this.callParent();
    },
    workCanlendar:function(){
        var panel=Ext.create('Sys.tool.CalendarPanel');
        var win=Ext.getCmp("c_CalendarPanel");
        if(!win){
            win=Ext.create('Ext.Window',{id:'c_CalendarPanel',title:'办公日历',closeAction:'hide',items:[panel],maximizable:true,height:600,width:600,layout:'fit',buttons:[{text:'取消',iconCls:ipe.sty.cancel,handler:function(){win.hide();}}]});
        }
        win.show();
    },
    showTask:function(){

    },
    logout:function(){
        window.location.href="logout";
    },
    upPwd:function(){
        var win=Ext.create('Desktop.view.UpPwdWin',{parent:this});
        win.show();
        win.form.getForm().findField('userName').setValue(user.userName);
    },
    menuClick:function(ts){
        if(ts.menuUrl){
            var ipeCont=(this.parent.ipeCon);
            var tname_=ts.menuUrl.replace(/\.|\-|_|\//gi,'');
            var sheetId="Tab_Panel_"+tname_;
            var sheet=Ext.getCmp(sheetId);
            //////////////////
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
        }
    },eachMenu:function(menu){
        //绑定方法
        Ext.each(menu,function(r,i){
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
            Ext.Msg.alert('提示',"页面不存在或是未完成初始化["+e+"]");
        }finally{
            setTimeout(function(){
                myMask.hide();
            },1000);
        }
        return pcontainer;
    }
});