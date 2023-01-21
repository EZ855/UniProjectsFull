package unsw.blackout.myClasses;

import java.util.List;

import unsw.utils.Angle;

public interface DeviceI {

    public void addFile(File file);
    public void removeFile(File file);
    public File findFile(String fileName);


    public int getRange();

    public List<File> getFileList();

    public void setFileList(List<File> fileList);

    public Angle getPosition();

    public void setPosition(Angle position);

    public double getHeight();

    public void setHeight(double height);

    public String getiD();

    public void setiD(String iD);

}
