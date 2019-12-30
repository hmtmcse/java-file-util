package com.hmtmcse.fileutil.data;

public enum OpenOption implements java.nio.file.OpenOption {
    WRITE, // Opens the file for write access.
    APPEND, // Appends the new data to the end of the file. This option is used with the WRITE or CREATE options.
    CREATE_NEW,   // Creates a new file and throws an exception if the file already exists.
    CREATE, // Opens the file if it exists or creates a new file if it does not.
    TRUNCATE_EXISTING ,
}
