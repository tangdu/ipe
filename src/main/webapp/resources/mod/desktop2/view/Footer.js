/**
 *脚注
 */
Ext.define('Desktop.view.Footer', {
    extend : 'Ext.panel.Panel',
    height : 20,
    region : 'south',
    border:false,
    initComponent : function(){
        this.html="<div style='text-align:center;margin:0 auto;'>© 2013 tdu</div>";
        this.callParent();
    }
});

