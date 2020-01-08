package com.hmtmcse.fileutil.fd;

import com.hmtmcse.fileutil.common.FileUtilException;
import com.hmtmcse.fileutil.data.FDInfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.Comparator;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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


    public Boolean isExist(Path path) {
        return Files.exists(path);
    }


    public Boolean isExist(String path) {
        Path sourceFile = Paths.get(path);
        return this.isExist(sourceFile);
    }


    public Boolean copyAll(Path source, Path target, CopyOption... options) throws FileUtilException {
        try {
            Files.walkFileTree(source, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    Path newdir = target.resolve(source.relativize(dir));
                    try {
                        Files.copy(dir, newdir, COPY_ATTRIBUTES);
                    } catch (FileAlreadyExistsException x) {
                        // ignore
                    } catch (IOException x) {
                        System.err.format("Unable to create: %s: %s%n", newdir, x);
                        return SKIP_SUBTREE;
                    }
                    return CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        Path newdir = target.resolve(source.relativize(file));
                        Files.copy(file, newdir, REPLACE_EXISTING);
                    } catch (IOException x) {
                        System.err.format("Unable to copy: %s: %s%n", source, x);
                    }
                    return CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    if (exc instanceof FileSystemLoopException) {
                        System.err.println("cycle detected: " + file);
                    } else {
                        System.err.format("Unable to copy: %s: %s%n", file, exc);
                    }
                    return CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        Path newdir = target.resolve(source.relativize(dir));
                        try {
                            FileTime time = Files.getLastModifiedTime(dir);
                            Files.setLastModifiedTime(newdir, time);
                        } catch (IOException x) {
                            System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
                        }
                    }
                    return CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new FileUtilException(e.getMessage());
        }
        return true;
    }


    public Boolean copyAll(String source, String destination) throws FileUtilException {
        return copyAll(Paths.get(source), Paths.get(destination));
    }


    private void copyData(Path source, Path destination, CopyOption... options) throws FileUtilException {
        try {
            Files.copy(source, destination, options);
        } catch (Exception e) {
            throw new FileUtilException(e.getMessage());
        }
    }

    public Boolean copy(String source, String destination) throws FileUtilException {
        Path sourceFile = Paths.get(source);
        Path targetFile = Paths.get(destination);
        copyData(sourceFile, targetFile);
        return true;
    }

    public Boolean move(String source, String destination) throws FileUtilException {
        Path sourceFile = Paths.get(source);
        Path targetFile = Paths.get(destination);
        try {
            Files.move(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException ex) {
            throw new FileUtilException(ex.getMessage());
        }
    }

    public Boolean remove(String path) throws FileUtilException {
        Path sourceFile = Paths.get(path);
        try {
            if (isExist(path)){
                Files.delete(sourceFile);
            }
            return true;
        } catch (IOException ex) {
            throw new FileUtilException(ex.getMessage());
        }
    }

    public Boolean removeAll(String path) throws FileUtilException {
        Path sourceFile = Paths.get(path);
        try {
            if (isExist(path)) {
                Files.walkFileTree(sourceFile, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            return true;
        } catch (IOException ex) {
            throw new FileUtilException(ex.getMessage());
        }
    }

    public Boolean createDirectory(String path) throws FileUtilException {
        Path sourceFile = Paths.get(path);
        try {
            Files.createDirectory(sourceFile);
            return true;
        } catch (IOException ex) {
            throw new FileUtilException(ex.getMessage());
        }
    }


    public Boolean createDirectories(String path) throws FileUtilException {
        Path sourceFile = Paths.get(path);
        try {
            Files.createDirectories(sourceFile);
            return true;
        } catch (IOException ex) {
            throw new FileUtilException(ex.getMessage());
        }
    }

    public Boolean createSymbolicLink(String source, String destination) throws FileUtilException {
        Path sourceFile = Paths.get(source);
        Path targetFile = Paths.get(destination);
        try {
            Files.createSymbolicLink(sourceFile, targetFile);
            return true;
        } catch (IOException ex) {
            throw new FileUtilException(ex.getMessage());
        }
    }


    public Boolean isSymbolicLink (String path){
        Path sourceFile = Paths.get(path);
        return Files.isSymbolicLink(sourceFile);
    }

    private void throwIfNull(String object, String message) throws FileUtilException{
        if (object == null){
            throwException(message);
        }
    }

    private void throwException(String message) throws FileUtilException {
        throw new FileUtilException(message);
    }

    public File getFile(String location) throws FileUtilException {
        throwIfNull(location, "Path Should not be null");
        return new File(location);
    }

}
