package hu.webarticum.treeprinter.text;

public class PlainLineMerger implements LineMerger {

    @Override
    public String merge(String existingLine, int fromPosition, String replacement) {
        String beforeContent;
        String beforePad;

        int contextLineLength = existingLine.length();
        
        if (contextLineLength <= fromPosition) {
            beforeContent = existingLine;
            beforePad = TextUtil.repeat(' ', fromPosition - contextLineLength);
        } else {
            beforeContent = existingLine.substring(0, fromPosition);
            beforePad = "";
        }

        int textLineLength = replacement.length();
        
        String afterContent;
        
        if (fromPosition + textLineLength < contextLineLength) {
            afterContent = existingLine.substring(fromPosition + textLineLength);
        } else {
            afterContent = "";
        }
        
        return beforeContent + beforePad + replacement + afterContent;
    }
    
}
