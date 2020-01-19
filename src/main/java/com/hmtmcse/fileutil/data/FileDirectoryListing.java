package com.hmtmcse.fileutil.data;

import java.util.ArrayList;
import java.util.List;

public class FileDirectoryListing {
    public FDInfo fileDirectoryInfo;
    public List<FileDirectoryListing> subDirectories = new ArrayList<>();

    public FileDirectoryListing setFileDirectoryInfo(FDInfo fileDirectoryInfo) {
        this.fileDirectoryInfo = fileDirectoryInfo;
        return this;
    }

    public FileDirectoryListing setSubDirectories(List<FileDirectoryListing> subDirectories) {
        this.subDirectories = subDirectories;
        return this;
    }

    public static FileDirectoryListing addInfo(FDInfo fileDirectoryInfo){
        return new FileDirectoryListing().setFileDirectoryInfo(fileDirectoryInfo);
    }

    public static FileDirectoryListing addDirectories(List<FileDirectoryListing> subDirectories){
        return new FileDirectoryListing().setSubDirectories(subDirectories);
    }
}
