package com.hmtmcse.fileutil.data;

import java.nio.file.CopyOption;

public enum JDCopyOption implements CopyOption {
    REPLACE_EXISTING,
    COPY_ATTRIBUTES,
    ATOMIC_MOVE,
    NOFOLLOW_LINKS
}
