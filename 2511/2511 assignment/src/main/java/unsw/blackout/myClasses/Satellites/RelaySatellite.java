package unsw.blackout.myClasses.Satellites;

import java.util.ArrayList;
import java.util.List;

import unsw.blackout.myClasses.File;
import unsw.blackout.myClasses.SatelliteI;
import unsw.utils.Angle;

public class RelaySatellite implements SatelliteI{

    private static final int range = 300000;
    private static final int linearSpeed = 1500;
    private Angle angularVelocity;
    private int direction = 1;
    private int start = 1;
    private static final int maxFileCapacity = 0;
    private static final int maxFileSizeCapacity = 0;
    private static final int fileReceiveRate = -1;
    private static final int fileSendRate = -1;
    private int filesCurrentlySending = 0;

    public RelaySatellite(String iD, double height, Angle position) {
        setiD(iD);
        setHeight(height);
        setPosition(position);
        setAngularVelocity(Angle.fromRadians(RelaySatellite.linearSpeed/height));
    }

    public void moveSat() {
        Angle myPos = getPosition();
        double myPosDoub = myPos.toDegrees();

        if (this.start == 1) {
            if (myPosDoub < 345) {
                setPosition(Angle.fromDegrees(myPos.subtract(getAngularVelocity()).toDegrees()));
                this.direction = -1;
                if (myPosDoub <= 190) {
                    this.start = 0;
                }
            }
            else {
                setPosition(Angle.fromDegrees(myPos.add(getAngularVelocity()).toDegrees() % 360));
                if (myPosDoub >= 140) {
                    this.start = 0;
                }
            }

        }
        else if (myPos.toDegrees() >= 190) {
            setPosition(Angle.fromDegrees(myPos.subtract(getAngularVelocity()).toDegrees() % 360));
            this.direction = -1;
        }
        else if (myPos.toDegrees() <= 140) {
            setPosition(Angle.fromDegrees(myPos.add(getAngularVelocity()).toDegrees() % 360));
            this.direction = 1;
        }
        else {
            if (this.direction == 1) {
                setPosition(Angle.fromDegrees(myPos.add(getAngularVelocity()).toDegrees() % 360));
            }
            else {
                setPosition(Angle.fromDegrees(myPos.subtract(getAngularVelocity()).toDegrees() % 360));
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
        return false;
    }

    public boolean isReceiveBandwidthFull() {
        return false;
    }
    public boolean isFull() {
        return false;
    }
    public String isFullMessage() {
        return "stub";
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
