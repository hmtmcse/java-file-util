package com.hmtmcse.fileutil.common;

/**
 * Created by Touhid Mia on 11/09/2014.
 */
public class FileUtilException extends Exception {

    public FileUtilException(){
        super("File Util Exception");
    }

    public FileUtilException(String message){
        super(message);
    }
}
