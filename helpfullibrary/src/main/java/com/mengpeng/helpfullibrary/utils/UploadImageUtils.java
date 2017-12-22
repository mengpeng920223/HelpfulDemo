package com.mengpeng.helpfullibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 * 创建:  MengPeng
 * 日期:  2017/8/29 , 下午2:41.
 * 作用:  上传图片
 */
public class UploadImageUtils {

    private static String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    /**
     * 相册选择上传
     */
    public static void selectPicFromLocal(Activity activity, int code) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, code);
    }

    /**
     * 相册选择上传
     */
    public static void selectPicFromLocal(Fragment fragment, int code) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        fragment.startActivityForResult(intent, code);
    }

    /**
     * 拍照上传
     */
    public static void selectPicFromCamera(File cameraFile, int code, Activity activity) {
        if (!SDCardHelper.isSDCardMounted()) {
            Toast.makeText(activity, "SDCard不存在，无法拍照", Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM) + "/Camera/",
                System.currentTimeMillis() + ".jpg");
        CameraUtils.openCamera(activity, cameraFile, code);

    }


    /**
     * 裁剪
     *
     * @param imagePath 图片路径
     */
    public static File initCrop(String imagePath, Activity activity, int code) {
        File mCropImageFile = getmCropImageFile(activity);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(activity, new File(imagePath)), "image/*");
        intent.putExtra("crop", true);

        //避免特殊机型裁剪时为圆形
        intent.putExtra("aspectX", 9998);
        intent.putExtra("aspectY", 9999);

        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, code);
//        startActivityForResult(intent, REQUEST_CROP);

        return mCropImageFile;


    }

    /**
     * 裁剪
     *
     * @param imagePath 图片路径
     */
    public static File initCropRectangle(String imagePath, Activity activity, int code) {
        File mCropImageFile = getmCropImageFile(activity);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(activity, new File(imagePath)), "image/*");
        intent.putExtra("crop", true);

        //避免特殊机型裁剪时为圆形
        intent.putExtra("aspectX", 1920);
        intent.putExtra("aspectY", 1080);

        intent.putExtra("outputX", 1920 / 2);
        intent.putExtra("outputY", 1080 / 2);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, code);
//        startActivityForResult(intent, REQUEST_CROP);

        return mCropImageFile;


    }

    //把fileUri转换成ContentUri
    public static Uri getImageContentUri(Activity activity, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return activity.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    //获取裁剪的图片保存地址
    public static File getmCropImageFile(Activity activity) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(activity.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            return file;
        }
        return null;
    }

    public static String sendPicByUri(Activity activity, Uri selectedImage) {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast.makeText(activity, "找不到图片", Toast.LENGTH_SHORT).show();
                return "";
            }
            return picturePath;
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast.makeText(activity, "找不到图片", Toast.LENGTH_SHORT).show();
                return "";
            }
            return file.getAbsolutePath();
        }
    }

    public static Uri getFileUri(Context context, File file) {

        // 判断是否是7.0
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, "com.souget.kpl.FileProvider",
                    file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
