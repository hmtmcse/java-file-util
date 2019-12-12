package com.hmtmcse.fileutil

import com.hmtmcse.fileutil.fd.FileDirectory
import spock.lang.Specification

class FileDirectoryTest extends Specification{

    def "Check File Details Information"(){
        expect: "Will get File Information"
        FileDirectory fileDirectory = new FileDirectory()
        fileDirectory.getDetailsInfo("C:\\Users\\touhid\\Desktop\\temp\\Feedback.htm", true)
    }

}
