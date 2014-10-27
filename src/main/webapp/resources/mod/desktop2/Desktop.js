/**
 * 系统菜单桌面
 */
Ext.define('Ipe.desktop2.Desktop', {
    extend : 'Ext.app.Application',
    appFolder : _basePath+'/resources/mod/desktop2',
    name : 'Desktop',
    requires:['Ext.ux.form.SearchField'],
    //controllers : ['Desktop'],
    enableQuickTips : true,
    autoCreateViewport: true
});