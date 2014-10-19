package com.ipe.common.util;

/**
 * 实现Controller日志拦截注解
 * optype 拦截类型-add|edit|delete...
 * opdesc 描述-该方法处理逻辑描述
 * Created by tangdu on 14-2-8.
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Logs {
    String opdesc() default "";
}
