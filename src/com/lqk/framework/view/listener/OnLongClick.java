/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-2 下午1:57:44
 */
package com.lqk.framework.view.listener;

import android.view.View;
import android.view.View.OnLongClickListener;

import com.lqk.framework.core.kernel.KernelDyna;

/**
 * @author absir
 * 
 */
public class OnLongClick extends OnListener implements OnLongClickListener {

	@Override
	public boolean onLongClick(View v) {
		return KernelDyna.to(invoke(v), Boolean.class);
	}

	@Override
	protected void listener(View view) {
		view.setOnLongClickListener(this);
	}
}
