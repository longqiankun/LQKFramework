package com.lqk.framework.invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import android.app.Activity;
import android.view.View;

import com.lqk.framework.app.Ioc;
import com.lqk.framework.util.InjectExcutor;
import com.lqk.framework.view.listener.OnListener;

public class InjectObjects extends InjectInvoker {

	 Field field;
	Class[] clazz;
	InjectExcutor<?> injectExcutor;

	public InjectObjects(Field field,  Class[] clazz, InjectExcutor<?> injectExcutor) {
		this.field = field;
		this.clazz = clazz;
		this.injectExcutor = injectExcutor;
	}

	@Override
	public void invoke(Object beanObject, Object... args) {

		// TODO Auto-generated method stub
		try {
			field.setAccessible(true);
			field.set(beanObject, field.getType().newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public String toString() {
		return "InjectMethods [field=" + field + ", clazz=" + Arrays.toString(clazz) + "]";
	}
}
