package com.hmtmcse.fileutil.data;

public class FDListingFilter {

    public Boolean isRecursive = true;
    public Boolean isDirectoryOnly = false;
    public Boolean isDetails = false;

    public FDListingFilter notRecursive() {
        isRecursive = false;
        return this;
    }

    public FDListingFilter directoryOnly() {
        isDirectoryOnly = true;
        return this;
    }

    public FDListingFilter details() {
        isDetails = true;
        return this;
    }
}
