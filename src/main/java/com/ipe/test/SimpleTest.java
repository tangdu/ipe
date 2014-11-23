package com.ipe.test;

public class SimpleTest {
	public static void main(String[] args) {
		Integer aa=11;
		System.out.println(aa.getClass().isLocalClass());
		System.out.println(aa.getClass().isMemberClass());
		System.out.println(aa.getClass().isPrimitive());
		System.out.println(aa.getClass().isSynthetic());
		System.out.println(aa.getClass().getDeclaredFields().length);
		System.out.println(aa.getClass().getFields().length);
		System.out.println(aa.getClass().getName());
	}
	
}
