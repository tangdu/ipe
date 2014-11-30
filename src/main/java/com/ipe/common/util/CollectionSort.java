package com.ipe.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void sortList(List list,String prop,boolean isAsc){
        Comparator beanProp=new BeanComparator(prop);
        Comparator comparator1=new ComparatorChain(beanProp,!isAsc);
        Collections.sort(list,comparator1);
    }
}
