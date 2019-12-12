package com.hmtmcse.fileutil.data;

import java.nio.file.attribute.FileTime;

public class FDInfo extends FDAttribute {

    public Boolean isDirectory;
    public Boolean isRegularFile;
    public Boolean isSymbolicLink;
    public Boolean isWritable;
    public String name;
    public String mimeType;
    public String fileExtension;
    public String absolutePath;
    public FileTime createdAt;
    public FileTime updatedAt;
    public FileTime lastAccessAt;
    public Long size;

}
