package com.rqmana.scrambling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

class Utils {

    public final static String TAG = "MyTAG";


    static void initPassword(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(LoginActivity.PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        String savedPassword = sharedPref.getString(LoginActivity.PASSWORD,"");
        if (savedPassword != null && savedPassword.isEmpty()) {
            sharedPrefEditor.putString(LoginActivity.PASSWORD, LoginActivity.DEFAULT_PASSWORD);
        }
        sharedPrefEditor.apply();
    }

    static void savePassword(Context context, String password) {
        SharedPreferences sharedPref = context.getSharedPreferences(LoginActivity.PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(LoginActivity.PASSWORD, password);
        sharedPrefEditor.apply();
    }

    static String getPassword(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(LoginActivity.PASSWORD, Context.MODE_PRIVATE);
        return sharedPref.getString(LoginActivity.PASSWORD,"");
    }

    static Bitmap scramble(Bitmap bitmap) {

        int z = bitmap.getWidth() * bitmap.getHeight();
        int[] bitmapArray = new int[z];
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap.getPixels(bitmapArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int w = bitmap.getHeight() / 100, w2 = bitmap.getWidth() / 100;
        if (w < 7 || w2 <10 ) {
            w = 7;
            w2 = 7;
        }
        int hold;
        boolean s = true;
        int h1 = bitmapArray.length / 2;
        boolean b = true;
        for (int r = 0; r < bitmap.getWidth() / 2; r++) {
            if (r % w == 0)
                b = !b;

            if (b)
                for (int i = r; i < bitmapArray.length; i += bitmap.getWidth()) {
                    hold = bitmapArray[i];
                    bitmapArray[i] = bitmapArray[i + (bitmap.getWidth() / 2)];
                    bitmapArray[i + (bitmap.getWidth() / 2)] = hold;
                }
        }
        int q = 0;
        for (int i = 0; i < bitmapArray.length / 2; i += bitmap.getWidth()) {
            if (q % w2 == 0)
                s = !s;

            if (s)
                for (int j = i; j < i + bitmap.getWidth(); j++) {
                    hold = bitmapArray[j];
                    bitmapArray[j] = bitmapArray[h1 + j];
                    bitmapArray[h1 + j] = hold;
                }
            q++;
        }
        bitmap = Bitmap.createBitmap(bitmapArray, 0, bitmap.getWidth(), bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        return bitmap;
    }

    static void saveImage(Context context, Bitmap bitmap) {
        String fileName = "Image_" + (System.currentTimeMillis() / 1000) + ".png";
        checkPermission((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1234);
        String scrambled_image = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, fileName, "scrambled image");
        Log.d(TAG, "saveImage: " + scrambled_image);
    }

    static void deleteImage(Context context, String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            boolean is = file.delete();
            if (is)
                callBroadcast(context);
        }
    }

    private static void callBroadcast(Context context) {
        MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) { }
        });
    }

    /**
     * Getting All Images Path.
     *
     * @param context
     *            the context
     * @return ArrayList with images Path
     */
    static ArrayList<String> getAllShownImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                listOfAllImages.add(absolutePathOfImage);
            }
            cursor.close();
        }
        return listOfAllImages;
    }


    static boolean checkPermission(Activity activity, String permission, int requestCode) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
