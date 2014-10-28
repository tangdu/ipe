package com.ipe.common.util;

/**
 * 标识不需要被权限的方法
 * @author tangdu
 *
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Anonymous {

}
