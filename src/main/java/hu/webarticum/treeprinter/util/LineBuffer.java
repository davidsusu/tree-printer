package hu.webarticum.treeprinter.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineBuffer {
    
    private final Appendable out;
    
    private int flushedRowCount = 0;
    
    private List<String> lines = new ArrayList<String>();
    
    public LineBuffer(Appendable out) {
        this.out = out;
    }
    
    public void write(int row, int col, String text) {
        String[] textLines = text.split("\n");
        int lineCount = textLines.length;
        for (int i = 0; i < lineCount; i++) {
            writeLine(row + i, col, textLines[i]);
        }
    }

    public void flush() {
        flush(flushedRowCount + lines.size());
    }
    
    public void flush(int rows) {
        try {
            flushThrows(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void flushThrows(int rows) throws IOException {
        if (rows <= flushedRowCount) {
            return;
        }
        
        int currentLineCount = lines.size();
        int deleteLineCount = rows - flushedRowCount;
        if (currentLineCount <= deleteLineCount) {
            for (String line: lines) {
                out.append(line + "\n");
            }
            lines.clear();
        } else {
            for (int i = 0; i < deleteLineCount; i++) {
                String line = lines.get(i);
                out.append(line + "\n");
            }
            lines = new ArrayList<String>(lines.subList(deleteLineCount, currentLineCount));
        }
        
        flushedRowCount = rows;
    }
    
    private void writeLine(int row, int col, String textLine) {
        if (row < flushedRowCount) {
            return;
        }
        int currentLineCount = lines.size();
        int lineIndex = row - flushedRowCount;
        String originalLine;
        if (lineIndex < currentLineCount) {
            originalLine = lines.get(lineIndex);
        } else {
            for (int i = currentLineCount; i <= lineIndex; i++) {
                lines.add("");
            }
            originalLine = "";
        }
        String newLine = writeIntoLine(originalLine, col, textLine);
        lines.set(lineIndex, newLine);
    }
    
    private String writeIntoLine(String contextLine, int pos, String textLine) {
        String beforeContent;
        String beforePad;

        int contextLineLength = contextLine.length();
        
        if (contextLineLength <= pos) {
            beforeContent = contextLine;
            beforePad = Util.repeat(' ', pos - contextLineLength);
        } else {
            beforeContent = contextLine.substring(0, pos);
            beforePad = "";
        }

        int textLineLength = textLine.length();
        
        String afterContent;
        
        if (pos + textLineLength < contextLineLength) {
            afterContent = contextLine.substring(pos + textLineLength);
        } else {
            afterContent = "";
        }
        
        return beforeContent + beforePad + textLine + afterContent;
    }
    
}
