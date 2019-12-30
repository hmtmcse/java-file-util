package com.hmtmcse.fileutil.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by touhid on 11/04/2016.
 */
public class TextFileData {

    public Integer totalLine;
    public String text;
    public List<String> lines = new ArrayList<>();

    public Integer getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(Integer totalLine) {
        this.totalLine = totalLine;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void addLine(String line){
        this.lines.add(line);
    }

    public Integer getLineSize(){
        return lines.size();
    }

    public String getLine(Integer lineNumber){
        if (this.lines.size() > 0){
            return this.lines.get(lineNumber);
        }else{
            return null;
        }
    }

}
