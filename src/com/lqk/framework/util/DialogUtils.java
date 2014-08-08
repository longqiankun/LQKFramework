package com.lqk.framework.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
/**
 * 
* @ClassName: DialogUtils
* @Description: 对话框弹出工具
* @author longqiankun
* @date 2014-7-7 上午11:25:47
*
 */
public class DialogUtils {

	/**
	 * 
	* @Title: showDialog
	* @Description: 显示对话框
	* @param @param mActivity
	* @param @param title
	* @param @param okBtnTxt
	* @param @param cancleBtnTxt
	* @return void
	* @throws
	 */
	public void showDialog(Activity mActivity,String title,String okBtnTxt,String cancleBtnTxt){
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setMessage(title);
		builder.setPositiveButton(okBtnTxt,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(mDialogOkListener!=null){
							mDialogOkListener.dialogOk();
						}
					}
				});
		builder.setNegativeButton(cancleBtnTxt,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(mDialogOkListener!=null){
							mDialogOkListener.dialogCancle();
						}
					}
				});
		builder.show();
	}
	IDialogOkListener mDialogOkListener;
	public void setIDialogOkListener(IDialogOkListener mDialogOkListener){
		this.mDialogOkListener=mDialogOkListener;
	}
	public interface IDialogOkListener{
		/**
		 * 
		* @Title: dialogOk
		* @Description: 对话框的确定事件
		* @return void
		* @throws
		 */
		void dialogOk();
		/**
		 * 
		* @Title: dialogCancle 
		* @Description: 对话框的取消事件
		* @return void
		* @throws
		 */
		void dialogCancle();
	}
}
