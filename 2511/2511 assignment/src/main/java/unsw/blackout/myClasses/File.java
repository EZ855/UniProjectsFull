package unsw.blackout.myClasses;

public class File {

    private String fileName;
    private String content;
    private int filesize;
    private boolean hasTransferCompleted = true;
    
    public File(String name, String content) {
        setFileName(name);
        setContent(content);
        setFilesize(content.length());
    }
    

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public boolean isHasTransferCompleted() {
        return hasTransferCompleted;
    }
    public void setHasTransferCompleted(boolean hasTransferCompleted) {
        this.hasTransferCompleted = hasTransferCompleted;
    }
    public int getFilesize() {
        return filesize;
    }
    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }
    
}
