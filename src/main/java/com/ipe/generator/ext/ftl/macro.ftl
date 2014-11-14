<#--单选框-->
<#macro radio type name><#if type='radiogroup'>,items:[{boxLabel:'是',inputValue:'1',name:'${name}',checked:true},{boxLabel:'否',inputValue:'0',name:'${name}'}]</#if></#macro>
<#--日期类型 空格为了保持格式-->
<#macro datefield val><#if val='datefield' >
            format:'Y-m-d',
</#if></#macro>

<#--字段长度限制-->
<#macro maxLength val><#if val!='0' >
            maxLength:${val},
</#if></#macro>