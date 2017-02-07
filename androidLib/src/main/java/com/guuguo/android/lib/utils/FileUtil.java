package com.guuguo.android.lib.utils;

import java.io.File;

/**
 * Created by mimi on 2017-01-19.
 */

public class FileUtil {
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            file.delete();
        }
    }
}
