package hueic.kenhoang.diaryapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kenhoang on 3/29/17.
 */

public class Util {
    //PUBLIC METHODS
    //Save image to internal memory
    public static boolean saveImageToSDCard(Bitmap image, String folder, String name) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/";
        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            OutputStream os = null;
            File file = new File(fullPath, name);
            if (!file.exists()) {
                file.createNewFile();
            }
            os = new FileOutputStream(file);

            // 100 means no compression, the lover you go
            image.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();


            return true;
        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }

    public static String getCurrentDateTime() {
        //get datetime
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(new Date());
        return dateTime;
    }

    private static boolean isSDReadable() {
        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read add write the media
            mExternalStorageAvailable = true;
            Log.i("isSdRReadable", "External storage card is readable.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can read add write the media
            Log.i("isSdRReadable", "External storage card is readable.");
            mExternalStorageAvailable = true;
        } else {
            //Something else is wrong. It may be one of many other
            //states, but all we need to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }
        return mExternalStorageAvailable;
    }

    public static Date converStringToDate(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ;
        try {
            Date date = sdf.parse(dateTime);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static void setBitmapToImage(final Context context, final String folder, final String name, final ImageView imageView) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap = (Bitmap) msg.obj;
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }
        };
        //set flag
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = Util.readImage(folder, name, context);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }

            };
            thread.start();
        }
        catch (Exception ex) {

        }
    }
    public static Bitmap readImage(String folder, String filename, Context context) {
        Bitmap img = null;
        // read image from SD card
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/" + filename;
        try{
            img = BitmapFactory.decodeFile(fullPath);
        }
        catch (Exception e) {
            Log.i("DemoReadWriteImage", "Can not read image from SD card");
        }
        //read image from internal memory
        try {
            File myFile = context.getFileStreamPath(filename);
            FileInputStream fIn = new FileInputStream(myFile);
            img = BitmapFactory.decodeStream(fIn);
        }
        catch (Exception ex) {
            Log.i("DemoReadWriteImage", "Can not read image from internal memory");
        }
        return img;
    }
    public static String convertStringDatetimeToFileName(String date) {
        return date.toString().replace(":", "").replace(" ","".replace("-", ""));
    }

}



















