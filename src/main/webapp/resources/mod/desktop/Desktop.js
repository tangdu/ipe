
Ext.define('Ipe.desktop.Desktop', {
    extend : 'Ext.app.Application',
    appFolder : _basePath+'/resources/mod/desktop',
    name : 'Desktop',
    //controllers : ['Desktop'],
    requires:['Ext.ux.form.SearchField'],
    enableQuickTips : true,
    autoCreateViewport: true
});