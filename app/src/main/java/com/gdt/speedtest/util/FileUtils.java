package com.gdt.speedtest.util;

import android.graphics.Bitmap;
import android.os.Environment;

import com.gdt.speedtest.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileUtils {

    public static String saveBitmap(Bitmap bitmap){
        String dir= Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_PATH;
        File file=new File(dir);
        if (!file.exists()){
            file.mkdir();
        }
        String path=dir+Constants.DEFAULT_IMAGE_NAME;
        try {
            FileOutputStream outputStream=new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return path;
    }
}
