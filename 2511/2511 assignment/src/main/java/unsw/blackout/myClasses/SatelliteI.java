package unsw.blackout.myClasses;

import java.util.List;

import unsw.utils.Angle;

public interface SatelliteI {
    
    public void moveSat();
    public void addFile(File file);
    public void removeFile(File file);
    public File findFile(String fileName);
    public int numFilesTransferring();
    public boolean isSendBandwidthFull();
    public boolean isReceiveBandwidthFull();
    public boolean isFull();
    public String isFullMessage();


    public int getRange();

    public List<File> getFileList();

    public void setFileList(List<File> fileList);

    public Angle getPosition();

    public void setPosition(Angle position);

    public double getHeight();

    public void setHeight(double height);

    public String getiD();

    public void setiD(String iD);

    public int getFilesCurrentlySending();
    
    public void setFilesCurrentlySending(int filesCurrentlySending);

    public int getLinearspeed();

    public int getMaxfilecapacity();

    public int getMaxfilesizecapacity();

    public int getFilereceiverate();

    public int getFilesendrate();

    public Angle getAngularVelocity();

    public void setAngularVelocity(Angle angularVelocity);
}
