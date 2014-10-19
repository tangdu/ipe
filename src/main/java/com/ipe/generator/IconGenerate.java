package com.ipe.generator;


import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-11-10
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 *
 .textReadOnly input{
 background-color: #EFEFEF !important;
 cursor: default;
 background-image: none !important;
 border-color:#AFBFF8 !important;
 }
 .toolbar{
 background-color:#dfe8f6;
 background-image:none;
 }
 */
public class IconGenerate {

    public static void main(String [] args){
        File dir=new File("F:\\workspace\\jaeespace\\smh2\\src\\main\\webapp\\resources\\extjs4\\icons");
        File css=new File("F:\\workspace\\jaeespace\\smh2\\src\\main\\webapp\\resources\\extjs4\\icons\\style.css");
        StringBuffer stringBuffer = new StringBuffer();

        for(File file :dir.listFiles()){
            String fname=file.getName();
            String cssf=".btn_"+fname.substring(0,fname.lastIndexOf("."))+"\n" +
                    "{\n" +
                    "    background-image:url("+fname+")!important;\n" +
                    "}\n";
            stringBuffer.append(fname.substring(0,fname.lastIndexOf("."))+":'"+"btn_"+fname.substring(0,fname.lastIndexOf("."))+"',\n");
            try {
                FileUtils.write(css,cssf,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(stringBuffer.toString());
    }
}
