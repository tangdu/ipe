package com.ipe.common.util;

import java.lang.reflect.Method;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 14-1-17
 * Time: 下午12:56
 * To change this template use File | Settings | File Templates.
 */
public class CollectionSort {
    private Comparator<Object> comparator= Collator.getInstance(Locale.CHINA);

    private String [] sortBy;

    public CollectionSort(String [] sortBy,int order){
        this.sortBy=sortBy;
    }

    public int compare(Object o1,Object o2){
        for(int i=0;i<sortBy.length;i++){
            Object value1=getFieldValue(sortBy[i],o1);
            Object value2=getFieldValue(sortBy[i],o2);
            return comparator.compare(value1,value2);
        }
        return -1;
    }

    private Object getFieldValue(String filedName,Object o){
        try {
            String getter="get"+filedName.substring(0,1).toUpperCase()+filedName.substring(1);
            Method method=o.getClass().getMethod(getter,new Class[]{});
            return method.invoke(o,new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static void sortList(List list,String prop,boolean isAsc){
        Comparator beanProp=new BeanComparator(prop);
        Comparator comparator1=new ComparatorChain(beanProp,!isAsc);
        Collections.sort(list,comparator1);
    }
}
