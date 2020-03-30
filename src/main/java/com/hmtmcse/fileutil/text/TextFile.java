package com.hmtmcse.fileutil.text;

import com.hmtmcse.fileutil.common.FileUtilException;
import com.hmtmcse.fileutil.data.TextFileData;
import com.hmtmcse.fileutil.fd.FDUtil;
import com.hmtmcse.fileutil.fd.FileDirectory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class TextFile {

    private FileDirectory fileDirectory;

    public TextFile() {
        this.fileDirectory = new FileDirectory();
    }

    public TextFileData fileToString(String location) throws FileUtilException {
        try {
            Path path = Paths.get(location);
            if (!fileDirectory.isExist(path)) {
                throw new FileUtilException("File not exist.");
            }
            BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));
            TextFileData textFileData = readFromBufferedReaderToString(reader, true);
            reader.close();
            return textFileData;
        } catch (IOException e) {
            throw new FileUtilException(e.getMessage());
        }
    }


    public TextFileData readFromBufferedReaderToString(BufferedReader bufferedReader, Boolean isNewLine) throws FileUtilException {
        TextFileData textFileData = new TextFileData();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                if (isNewLine) {
                    stringBuilder.append(System.getProperty("line.separator"));
                }
                textFileData.addLine(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new FileUtilException(e.getMessage());
        }
        textFileData.setText(stringBuilder.toString());
        textFileData.setTotalLine(textFileData.getLines().size());
        return textFileData;
    }

    public Boolean stringToFile(String location, String fileName, String content) throws FileUtilException {
        if (!fileDirectory.isExist(location)) {
            fileDirectory.createDirectories(location);
        }
        return stringToFile(FDUtil.concatPath(location, fileName), content);
    }

    public Boolean stringToFile(String location, String content) throws FileUtilException {
        return stringToFile(location, content, StandardOpenOption.CREATE);
    }

    public Boolean stringToFileAppendText(String location, String content) throws FileUtilException {
        return stringToFile(location, content, StandardOpenOption.APPEND);
    }

    public Boolean stringToCreateNewFile(String location, String content) throws FileUtilException {
        return stringToFile(location, content, StandardOpenOption.CREATE_NEW);
    }

    public Boolean stringToFile(String location, String content, StandardOpenOption... options) throws FileUtilException {
        try {
            Path path = Paths.get(location);
            BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), options);
            writer.write(content);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            throw new FileUtilException(e.getMessage());
        }
    }

    public String findReplaceAndPlaceFromFile(String filePath, LinkedHashMap<String, String> findReplace) throws FileUtilException {
        String content = fileToString(filePath).getText();
        return findReplaceInText(content, findReplace);
    }

    public String findReplaceInText(String content, LinkedHashMap<String, String> findReplace) {
        if (content != null && !content.equals("")) {
            for (Map.Entry<String, String> entry : findReplace.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    content = content.replaceAll(entry.getKey(), entry.getValue());
                }
            }
        }
        return content;
    }

    public String findOnlyReplaceInText(String content, LinkedHashMap<String, String> findReplace) {
        if (content != null && !content.equals("")) {
            for (Map.Entry<String, String> entry : findReplace.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    content = content.replace(entry.getKey(), entry.getValue());
                }
            }
        }
        return content;
    }

    public Boolean findReplaceAndPlaceClean(String templatePath, String placeToPath, LinkedHashMap<String, String> findReplace) throws FileUtilException {
        String content = findReplaceAndPlaceFromFile(templatePath, findReplace);
        if (fileDirectory.removeIfExist(placeToPath)) {
            stringToFile(placeToPath, content);
        }
        return true;
    }


    public Boolean findReplaceAndPlace(String templatePath, String placeToPath, LinkedHashMap<String, String> findReplace) throws FileUtilException {
        String content = findReplaceAndPlaceFromFile(templatePath, findReplace);
        stringToFile(placeToPath, content);
        return true;
    }

}
