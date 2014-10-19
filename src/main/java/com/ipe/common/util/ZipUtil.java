package com.ipe.common.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by tangdu on 14-1-26.
 */
public class ZipUtil {

    /**
     *
     * @param zipfile
     * @throws Exception
     */
    public static void zipFiles(String zipfile,OutputStream out)
            throws Exception {
        File ff = new File(zipfile);
        ZipOutputStream zipOut = new ZipOutputStream(out);

        if(ff.isDirectory()){
            for(File file:ff.listFiles()){
                try {
                    FileInputStream in = new FileInputStream(file);
                    ZipEntry entry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(entry);
                    int nNumber = 0;
                    byte[] buffer = new byte[512];
                    while ((nNumber = in.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, nNumber);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        zipOut.close();
        out.close();
    }
}
