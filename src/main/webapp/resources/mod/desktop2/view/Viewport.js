/**
 * 首页面板
 */
Ext.define('Desktop.view.Viewport', {
    extend : 'Ext.container.Viewport',
    requires : ['Desktop.view.Container', 'Desktop.view.Header'],
    layout : 'border',
    initComponent : function() {

        this.ipeHd=Ext.create('Desktop.view.Header',{parent:this});
        this.ipeCon=Ext.create('Desktop.view.Container',{parent:this});
        this.footer=Ext.create('Desktop.view.Footer');

        this.items=[this.ipeHd,this.ipeCon,this.footer];
        this.callParent();
    }
});