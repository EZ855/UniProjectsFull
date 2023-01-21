package unsw.blackout.myClasses.Devices;

import java.util.ArrayList;
import java.util.List;

import unsw.blackout.myClasses.DeviceI;
import unsw.blackout.myClasses.File;
import unsw.utils.Angle;

public class HandheldDevice implements DeviceI{

    private static final int range = 50000;

    public HandheldDevice(String ID, Angle position) {
        setPosition(position);
        setiD(ID);
    }

    public int getRange() {
        return range;
    }
    private String iD;
    private String type;
    private List<File> fileList = new ArrayList<>();
    private Angle position;
    private double height = 69911;
    
    public void addFile(File file) {
        List<File> myFiles = getFileList();
        myFiles.add(file);
        setFileList(myFiles);
    }

    public void removeFile(File file) {
        List<File> myFiles = getFileList();
        myFiles.remove(file);
        setFileList(myFiles);
    }
    public File findFile(String fileName) {
        List<File> myFiles = getFileList();
        for (File aFile : myFiles) {
            if (aFile.getFileName() == fileName) {
                return aFile;
            }
        }
        return null;
    }

    public List<File> getFileList() {
        return fileList;
    }
    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public Angle getPosition() {
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }
    
}
