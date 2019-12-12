package com.hmtmcse.fileutil.fd;

import com.hmtmcse.fileutil.common.FileUtilException;
import com.hmtmcse.fileutil.data.FDInfo;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;

public class FileDirectory {

    public Boolean setOwner(Path path, String ownerName) throws FileUtilException {
        try {
            UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
            UserPrincipal owner = lookupService.lookupPrincipalByName(ownerName);
            if (owner != null) {
                Files.setOwner(path, owner);
                return true;
            } else {
                throw new FileUtilException("Invalid User.");
            }
        } catch (IOException e) {
            throw new FileUtilException(e.getMessage());
        }
    }


    public Boolean setGroup(Path path, String groupName) throws FileUtilException {
        try {
            GroupPrincipal group = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByGroupName(groupName);
            Files.getFileAttributeView(path, PosixFileAttributeView.class).setGroup(group);
            return true;
        } catch (IOException e) {
            throw new FileUtilException(e.getMessage());
        }
    }



    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }


    public FDInfo getDetailsInfo(String location, Boolean isDetails) throws FileUtilException {
        Path path = Paths.get(location);
        FDInfo fdInfo = new FDInfo();
        try {
            BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
            fdInfo.createdAt = basicFileAttributes.creationTime();
            fdInfo.updatedAt = basicFileAttributes.lastModifiedTime();
            fdInfo.lastAccessAt = basicFileAttributes.lastAccessTime();
            fdInfo.size = basicFileAttributes.size();
            fdInfo.isDirectory = basicFileAttributes.isDirectory();
            fdInfo.isRegularFile = basicFileAttributes.isRegularFile();
            fdInfo.isSymbolicLink = basicFileAttributes.isSymbolicLink();
            fdInfo.name = path.getFileName().toString();
            fdInfo.absolutePath = path.toAbsolutePath().toString();

            if (isDetails) {
                fdInfo.fileExtension = getFileExtension(fdInfo.name);
                fdInfo.isWritable = Files.isWritable(path);
                fdInfo.mimeType = Files.probeContentType(path);
                UserPrincipal userPrincipal = Files.getOwner(path);
                if (userPrincipal != null) {
                    fdInfo.owner = userPrincipal.getName();
                }
                PosixFileAttributeView posixFileAttributeView = Files.getFileAttributeView(path, PosixFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
                if (posixFileAttributeView != null) {
                    PosixFileAttributes posixFileAttributes = posixFileAttributeView.readAttributes();
                    GroupPrincipal groupPrincipal = posixFileAttributes.group();
                    posixFileAttributes.permissions();
                }
            }
        } catch (IOException e) {
            throw new FileUtilException(e.getMessage());
        }
        return fdInfo;
    }


}
