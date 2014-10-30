/**
 * 首页JS
 */
Ext.define('Desktop.view.Container', {
    alias: 'widget.ipeContainer',
    extend : 'Ext.tab.Panel',
    requires : ['Ext.ux.TabReorderer','Ext.ux.TabCloseMenu'],
    activeTab : 0,
    enableTabScroll : true,
    border:false,
    defaults: {
        bodyPadding: 5
    },
    region : 'center',
    //split : true,
    initComponent:function(){
        this.portal=Ext.create('Ext.Panel');
        this.plugins=[Ext.create('Ext.ux.TabReorderer'),
            Ext.create('Ext.ux.TabCloseMenu',{
            parent:this,
            scope:this,
            closeTabText: '关闭面板',
            closeOthersTabsText: '关闭其他',
            closeAllTabsText: '关闭所有'
        })];
        this.callParent();
    }
});