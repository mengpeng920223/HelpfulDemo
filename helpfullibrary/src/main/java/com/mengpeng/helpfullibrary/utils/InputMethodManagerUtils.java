package com.mengpeng.helpfullibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 创建:  MengPeng
 * 日期:  2017/8/1 , 上午11:50.
 * 作用:
 */
public class InputMethodManagerUtils {

//
//    二、关闭出入法窗口
//    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//inputMethodManager.hideSoftInputFromWindow(OpeListActivity.this.getCurrentFocus().getWindowToken()
//,InputMethodManager.HIDE_NOT_ALWAYS);
//
////接受软键盘输入的编辑文本或其它视图
//inputMethodManager.showSoftInput(submitBt,InputMethodManager.SHOW_FORCED);
//
//    三、如果输入法打开则关闭，如果没打开则打开
//    InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//    四、获取输入法打开的状态
//    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//    boolean isOpen=imm.isActive();
//    isOpen若返回true，则表示输入法打开

    /**
     * 打开输入法窗口
     *
     * @param context
     * @param view
     */
    public static void openInputMethod(Context context, View view) {

        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 关闭输入法窗口
     *
     * @param activity
     */
    public static void hideInputMethod(Activity activity) {
        if (null != activity) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            View view = activity.getCurrentFocus();

            if (null != view) {
                IBinder windowToken = view.getWindowToken();
                if (windowToken != null) {
                    // 接受软键盘输入的编辑文本或其它视图
                    inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 关闭输入法窗口
     *
     * @param view
     */
    public static void hideInputMethod(View view, Activity activity) {
        if (null != activity) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (null != view) {
                IBinder windowToken = view.getWindowToken();
                if (windowToken != null) {
                    // 接受软键盘输入的编辑文本或其它视图
                    inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 关闭输入法窗口
     *
     * @param view
     */
    public static void openInputMethod(View view, Activity activity) {
        if (null != activity) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != view) {
                // 接受软键盘输入的编辑文本或其它视图
                inputMethodManager.showSoftInput(view, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 关闭输入法
     *
     * @param view
     * @param context
     */
    public static void closeInputMethod(View view, Context context) {
        if (null != context && null != view) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void toggleSoftInputMethod(Context context) {
        InputMethodManager m = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
