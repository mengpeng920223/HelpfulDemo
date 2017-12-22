package com.mengpeng.helpfullibrary.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建:  MengPeng
 * 日期:  2017/6/27 , 下午12:30.
 * 作用:  相机管理工具类,适配Android 7.x版本
 */
public class CameraUtils {

    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2;// 拍摄视频

    /**
     * 打开相机拍照
     *
     * @param activity
     * @param cameraFile
     * @param requestCodeCamera
     */
    public static void openCamera(Activity activity, File cameraFile, int requestCodeCamera) {
        //获取系统版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            cameraFile.getParentFile().mkdirs();

            if (currentapiVersion < 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, cameraFile.getAbsolutePath());
                Uri uri = activity.getApplication().getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }

        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        activity.startActivityForResult(intent, requestCodeCamera);
    }


    /**
     * 打开相机录像
     */
    public static void startToVideo(Activity activity) {
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Uri fileUri = null;
        File file = null;
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {
            file = createMediaFile();
            if (file.exists()) {
                fileUri = Uri.fromFile(file); // create a file to save the video
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (currentapiVersion < 24) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        } else {
            //兼容android7.0
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = activity.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // start the Video Capture Intent
        activity.startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    /*
   * 判断sdcard是否被挂载
   */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建保存录制得到的视频文件
     *
     * @return
     * @throws IOException
     */
    public static File createMediaFile() throws IOException {
        if (hasSdcard()) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES), "CameraVideos");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "VID_" + timeStamp;
            String suffix = ".mp4";
            File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            return mediaFile;
        }
        return null;
    }

}
