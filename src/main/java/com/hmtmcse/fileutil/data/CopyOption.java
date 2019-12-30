package com.hmtmcse.fileutil.data;

public enum CopyOption implements java.nio.file.CopyOption {
    REPLACE_EXISTING,
    COPY_ATTRIBUTES,
    ATOMIC_MOVE,
    NOFOLLOW_LINKS
}
