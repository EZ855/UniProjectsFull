package unsw.blackout.myClasses.Satellites;

import java.util.ArrayList;
import java.util.List;

import unsw.blackout.myClasses.File;
import unsw.blackout.myClasses.SatelliteI;
import unsw.utils.Angle;

public class TeleportingSatellite implements SatelliteI{

    private static final int range = 200000;
    private static final int linearSpeed = 60000;
    private Angle angularVelocity;
    private int direction = 1;
    private int start = 1;
    private static final int maxFileCapacity = 200; //assuming min file size = 1 byte
    private static final int maxFileSizeCapacity = 200;
    private static final int fileReceiveRate = 15;
    private static final int fileSendRate = 10;
    private int filesCurrentlySending = 0;

    public TeleportingSatellite(String iD, double height, Angle position) {
        setiD(iD);
        setHeight(height);
        setPosition(position);
        setAngularVelocity(Angle.fromRadians(TeleportingSatellite.linearSpeed/height));
    }
    
    public void moveSat() {
        Angle myPos = getPosition();
        double myPosDoub = myPos.toDegrees();
        if (this.start == 1) {
            setPosition(Angle.fromDegrees(myPos.add(getAngularVelocity()).toDegrees() % 360));
            if (getPosition().toDegrees() >= 0) {
                this.start = 0;
            }
        }
        else if (myPosDoub >= 180 && direction == 1) {
            setPosition(Angle.fromDegrees(0));
            this.direction = -1;
        }
        else if (myPosDoub <= 180 && direction == -1) {
            setPosition(Angle.fromDegrees(360));
            this.direction = 1;
        }
        else {
            if (direction == 1) {
                setPosition(Angle.fromDegrees(myPos.add(getAngularVelocity()).toDegrees()));
            }
            else {
                setPosition(Angle.fromDegrees(myPos.subtract(getAngularVelocity()).toDegrees()));
            }
            
        }
        
    }

    public int getRange() {
        return range;
    }

    public int getLinearspeed() {
        return linearSpeed;
    }

    public int getMaxfilecapacity() {
        return maxFileCapacity;
    }

    public int getMaxfilesizecapacity() {
        return maxFileSizeCapacity;
    }

    public int getFilereceiverate() {
        return fileReceiveRate;
    }

    public int getFilesendrate() {
        return fileSendRate;
    }

    public Angle getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(Angle angularVelocity) {
        this.angularVelocity = angularVelocity;
    }
    
    public int getFilesCurrentlySending() {
        return filesCurrentlySending;
    }

    public void setFilesCurrentlySending(int filesCurrentlySending) {
        this.filesCurrentlySending = filesCurrentlySending;
    }

    private String iD;
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
    public int numFilesTransferring() {
        int numFilesTransferring = 0;
        List<File> myFiles = getFileList();
            for (File aFile : myFiles) {
                if (!aFile.isHasTransferCompleted()) {
                    numFilesTransferring++;
                }
            }
        return numFilesTransferring;
    }
    public boolean isSendBandwidthFull() {
        return getFilesendrate() <= this.filesCurrentlySending;
    }
    public boolean isReceiveBandwidthFull() {
        return getFilereceiverate() <= numFilesTransferring();
    }
    public boolean isFull() {
        if (getFileList().size() >= getMaxfilecapacity()) {
            return true;
        }
        
        int totalFileSize = 0;
        List<File> myFiles = getFileList();
        for (File aFile : myFiles) {
            totalFileSize += aFile.getContent().length();
        }
        if (totalFileSize >= getMaxfilesizecapacity()) {
            return true;
        }

        return false;
    }
    public String isFullMessage() {
        if (getFileList().size() >= getMaxfilecapacity()) {
            return "Max Files Reached";
        }
        
        int totalFileSize = 0;
        List<File> myFiles = getFileList();
        for (File aFile : myFiles) {
            totalFileSize += aFile.getContent().length();
        }
        if (totalFileSize >= getMaxfilesizecapacity()) {
            return "Max Storage Reached";
        }
        return "Error in error: toSat not full";
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

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }
}
