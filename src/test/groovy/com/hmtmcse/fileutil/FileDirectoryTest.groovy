package com.hmtmcse.fileutil

import com.hmtmcse.fileutil.data.FDListingFilter
import com.hmtmcse.fileutil.data.FileDirectoryListing
import com.hmtmcse.fileutil.fd.FileDirectory
import spock.lang.Specification

import java.nio.file.Paths

class FileDirectoryTest extends Specification{

    def "Check File Details Information"(){
        expect: "Will get File Information"
        FileDirectory fileDirectory = new FileDirectory()
        fileDirectory.getDetailsInfo("C:\\Users\\touhid\\Desktop\\temp\\Feedback.htm", true)
    }

    def "Delete Test"(){
        expect: "Will Delete Recursively"
        FileDirectory fileDirectory = new FileDirectory()
        fileDirectory.removeAll("C:\\Users\\touhid\\Desktop\\temp\\delete-it")
    }

    def "Copy Test"(){
        expect: "Copy Directory"
        FileDirectory fileDirectory = new FileDirectory()
        fileDirectory.copyAll("C:\\Users\\touhid\\Desktop\\temp\\copy", "C:\\Users\\touhid\\Desktop\\temp\\bismillah\\copyTo")
    }

    def "Recursively List Dir Test"(){
        expect: "Recursively List Directory"
        FileDirectory fileDirectory = new FileDirectory()
        List<FileDirectoryListing>  list = fileDirectory.listDirRecursively("J:\\text-xyz\\test")
        println(list);
    }

    def "Recursively List Dir With filter Test"(){
        expect: "Directory List with filter"
        FileDirectory fileDirectory = new FileDirectory()
        FDListingFilter fdListingFilter = new FDListingFilter().directoryOnly().notRecursive()
        List<FileDirectoryListing>  list = fileDirectory.listDirRecursively("J:\\text-xyz\\test", fdListingFilter)
        println(list);
    }

}
