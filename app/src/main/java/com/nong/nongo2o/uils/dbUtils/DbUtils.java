package com.nong.nongo2o.uils.dbUtils;


import android.content.Context;
import android.util.Log;

import com.nong.nongo2o.R;
import com.nong.nongo2o.uils.SPUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017-9-19.
 */

public class DbUtils {

    public static void copyDBToDatabases(Context context) {
        String DB_PATH = "/data/data/com.nong.nongo2o/databases/";
        String DB_NAME = "city.db";
        try {
            String outFileName = DB_PATH + DB_NAME;
            File file = new File(DB_PATH);
            if (!file.mkdirs()) {
                file.mkdirs();
            }
            File dataFile = new File(outFileName);
            if (dataFile.exists()) {
                dataFile.delete();
            }
            InputStream myInput;
            myInput = context.getResources().openRawResource(R.raw.city);
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
            Log.d("DbUtils", "copy db");
            SPUtils.put(context, "city_database", true);
        } catch (IOException e) {
            Log.d("DbUtils", "error " + e.toString());
            e.printStackTrace();
        }
    }
}
