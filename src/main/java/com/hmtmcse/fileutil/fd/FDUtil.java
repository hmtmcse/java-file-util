package com.hmtmcse.fileutil.fd;

import java.nio.file.Paths;

public class FDUtil {

    public static String concatPath(String start, String end){
        return Paths.get(start, end).toAbsolutePath().toString();
    }

    public static String concatPathToURI(String start, String end){
        return Paths.get(start, end).toAbsolutePath().toUri().toString();
    }

}
