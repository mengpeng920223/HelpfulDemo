package com.mengpeng.helpfullibrary.utils;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孟鹏
 * on 2017/2/17.
 * 用途：用于存放和销毁activity的工具类
 */

public class ActivityCollector {

    //存放activity的list
    public static List<Activity> activities = new ArrayList<>();

    //把activity添加到list中
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    //把activity从list中移除
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static Activity getActivity() {
        int size = activities.size();
        if (size > 0) {
            Activity activity = activities.get(size - 1);
            return activity;
        } else {
            return null;
        }
    }

    //关闭所有activity
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    //关闭所有activity
    public static void finishOtherActivity(Activity otherActivity) {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                if (null != otherActivity && activity == otherActivity) {

                } else {
                    activity.finish();
                }
            }
        }
    }
}
