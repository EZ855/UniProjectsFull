package unsw.blackout;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.blackout.myClasses.DeviceI;
import unsw.blackout.myClasses.File;
import unsw.blackout.myClasses.SatelliteI;
import unsw.blackout.myClasses.Devices.DesktopDevice;
import unsw.blackout.myClasses.Devices.HandheldDevice;
import unsw.blackout.myClasses.Devices.LaptopDevice;
import unsw.blackout.myClasses.Satellites.RelaySatellite;
import unsw.blackout.myClasses.Satellites.StandardSatellite;
import unsw.blackout.myClasses.Satellites.TeleportingSatellite;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class BlackoutController {

    
    private List<DeviceI> devices = new ArrayList<>();
    private List<SatelliteI> satellites = new ArrayList<>();
    
    public List<DeviceI> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceI> devices) {
        this.devices = devices;
    }

    public List<SatelliteI> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<SatelliteI> satellites) {
        this.satellites = satellites;
    }


    public void createDevice(String deviceId, String type, Angle position) {

        List<DeviceI> myDevs = getDevices();
       
        if (type == "DesktopDevice") {
            DesktopDevice myDev = new DesktopDevice(deviceId, position);
            myDevs.add(myDev);
        }
        else if (type == "LaptopDevice") {
            LaptopDevice myDev = new LaptopDevice(deviceId, position);
            myDevs.add(myDev);
        }
        else if (type == "HandheldDevice") {
            HandheldDevice myDev = new HandheldDevice(deviceId, position);
            myDevs.add(myDev);
        }
        
        setDevices(myDevs);
    }

    public void removeDevice(String deviceId) {

        List<DeviceI> myDevs = getDevices();

        for(DeviceI myDev : myDevs) {
            if (myDev.getiD() == deviceId) {
                myDevs.remove(myDev);
                break;
            }
        }
        setDevices(myDevs);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {

        List<SatelliteI> mySats = getSatellites();
       
        if (type == "StandardSatellite") {
            StandardSatellite mySat = new StandardSatellite(satelliteId, height, position);
            mySats.add(mySat);
        }
        else if (type == "TeleportingSatellite") {
            TeleportingSatellite mySat = new TeleportingSatellite(satelliteId, height, position);
            mySats.add(mySat);
        }
        else if (type == "RelaySatellite") {
            RelaySatellite mySat = new RelaySatellite(satelliteId, height, position);
            mySats.add(mySat);
        }
        
        setSatellites(mySats);
    }

    public void removeSatellite(String satelliteId) {
        List<SatelliteI> mySats = getSatellites();

        for(SatelliteI mySat : mySats) {
            if(mySat.getiD() == satelliteId) {
                mySats.remove(mySat);
                break;
            }
        }
        setSatellites(mySats);
    }

    public List<String> listDeviceIds() {

        List<String> myIdList = new ArrayList<>();
        List<DeviceI> myDevs = getDevices();

        for(DeviceI myDev : myDevs) {
            myIdList.add(myDev.getiD());
        }

        return myIdList;
    }

    public List<String> listSatelliteIds() {
        List<String> myIdList = new ArrayList<>();
        List<SatelliteI> mySats = getSatellites();

        for(SatelliteI mySat : mySats) {
            myIdList.add(mySat.getiD());
        }

        return myIdList;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {

        List<DeviceI> myDevs = getDevices();
        File myFile = new File(filename, content);

        for(DeviceI myDev : myDevs) {
            if (myDev.getiD() == deviceId) {
                myDev.addFile(myFile);
                break;
            }
        }
    }

    public EntityInfoResponse getInfo(String id) {
        
        List<DeviceI> myDevs = getDevices();

        for(DeviceI myDev : myDevs) {
            if (myDev.getiD() == id) {
                Map<String, FileInfoResponse> myHashMap = new HashMap<>();
                for(File myFile : myDev.getFileList()) {
                    myHashMap.put(myFile.getFileName(), new FileInfoResponse(myFile.getFileName(), myFile.getContent(), myFile.getFilesize(), myFile.isHasTransferCompleted()));
                }
                
                return new EntityInfoResponse(id, myDev.getPosition(), myDev.getHeight(), myDev.getClass().getSimpleName(), myHashMap);
            }
        }

        List<SatelliteI> mySats = getSatellites();

        for(SatelliteI mySat : mySats) {
            if(mySat.getiD() == id) {
                Map<String, FileInfoResponse> myHashMap = new HashMap<>();
                for(File myFile : mySat.getFileList()) {
                    myHashMap.put(myFile.getFileName(), new FileInfoResponse(myFile.getFileName(), myFile.getContent(), myFile.getFilesize(), myFile.isHasTransferCompleted()));
                }

                return new EntityInfoResponse(id, mySat.getPosition(), mySat.getHeight(), mySat.getClass().getSimpleName(), myHashMap);
            }
        }

        return null;
    }

    public void simulate() {
        // TODO: Task 2a)
        /* 
        List<DeviceI> myDevs = getDevices();

        for(DeviceI myDev : myDevs) {
            
        }
        */
        List<SatelliteI> mySats = getSatellites();

        for(SatelliteI mySat : mySats) {
            mySat.moveSat();
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }
    /**
     * Helper function to find device, returns null if none found
     */
    public DeviceI findDeviceI(String id) {

        List<DeviceI> myDevs = getDevices();

        for(DeviceI myDev : myDevs) {
            if (myDev.getiD() == id) {
                return myDev;
            }
        }
        return null;
    }

    /**
     * Helper function to find device, returns null if none found
     */
    public SatelliteI findSatelliteI(String id) {

        List<SatelliteI> mySats = getSatellites();

        for(SatelliteI mySat : mySats) {
            if(mySat.getiD() == id) {
                return mySat;
            }
        }
        return null;
    }

    public List<String> communicableEntitiesInRange(String id) {

        List<String> myIdList = new ArrayList<>();
        DeviceI myDev = findDeviceI(id);
        SatelliteI mySat = findSatelliteI(id);
        
        // If it is a device
        if (mySat == null) {
            List<SatelliteI> mySats = getSatellites();
            for (SatelliteI aSat : mySats) {
                // if visible
                if (MathsHelper.isVisible(aSat.getHeight(), aSat.getPosition(), myDev.getPosition())) {
                    // if in range
                    if (myDev.getRange() >= MathsHelper.getDistance(aSat.getHeight(), aSat.getPosition(), myDev.getPosition())) {
                        // if not desktopdevice and standardsatellite
                        if (!(myDev.getClass().getSimpleName() == "DesktopDevice" && aSat.getClass().getSimpleName() == "StandardSatellite")) {
                            myIdList.add(aSat.getiD());
                        }
                    }
                }
            }
        }
        // If it is a satellite
        else {
            List<SatelliteI> mySats = getSatellites();
            for (SatelliteI aSat : mySats) {
                // if visible
                if (MathsHelper.isVisible(aSat.getHeight(), aSat.getPosition(), mySat.getHeight(), mySat.getPosition())) {
                    // if in range
                    if (mySat.getRange() >= MathsHelper.getDistance(aSat.getHeight(), aSat.getPosition(), mySat.getHeight(), mySat.getPosition())) {
                        myIdList.add(aSat.getiD());
                    }
                }
            }

            List<DeviceI> myDevs = getDevices();
            for(DeviceI aDev : myDevs) {
                // if visible
                if (MathsHelper.isVisible(mySat.getHeight(), mySat.getPosition(), aDev.getPosition())) {
                    // if in range
                    if (mySat.getRange() >= MathsHelper.getDistance(mySat.getHeight(), mySat.getPosition(), aDev.getPosition())) {
                        // if not desktopdevice and standardsatellite
                        if (!(aDev.getClass().getSimpleName() == "DesktopDevice" && mySat.getClass().getSimpleName() == "StandardSatellite")) {
                            myIdList.add(aDev.getiD());
                        }
                    }
                }
            }
        }

        return myIdList;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO: Task 2 c)

        DeviceI fromDev = findDeviceI(fromId);
        SatelliteI fromSat = findSatelliteI(fromId);
        DeviceI toDev = findDeviceI(toId);
        SatelliteI toSat = findSatelliteI(toId);
        File myFile;

        // Checks if file exists on source
        if (fromSat == null) {
            myFile = fromDev.findFile(fileName);
            if (myFile == null) {
                throw new unsw.blackout.FileTransferException.VirtualFileNotFoundException(fileName);
            }
            if (!myFile.isHasTransferCompleted()) {
                throw new unsw.blackout.FileTransferException.VirtualFileNotFoundException(fileName);
            }
        }
        else {
            myFile = fromSat.findFile(fileName);
            if (myFile == null) {
                throw new unsw.blackout.FileTransferException.VirtualFileNotFoundException(fileName);
            }
            if (!myFile.isHasTransferCompleted()) {
                throw new unsw.blackout.FileTransferException.VirtualFileNotFoundException(fileName);
            }
        }

        // Checks if file already exists on destination
        if (toSat == null) {
            File aFile = toDev.findFile(fileName);
            // If file exists, file is currently downloading as well, so takes care of both cases for this exception.
            if (aFile != null) {
                throw new unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException(fileName);
            }
        }
        else {
            File aFile = toSat.findFile(fileName);
            // If file exists, file is currently downloading as well, so takes care of both cases for this exception.
            if (aFile != null) {
                throw new unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException(fileName);
            }
        }
        // Checks if there is available bandwidth on source if satellite
        if (fromDev == null) {
            if (fromSat.isSendBandwidthFull()) {
                throw new unsw.blackout.FileTransferException.VirtualFileNoBandwidthException(fromSat.getiD());

            }
        }
        // Checks if available bandwidth on destination if satellite
        if (toDev == null) {
            if (toSat.isReceiveBandwidthFull()) {
                throw new unsw.blackout.FileTransferException.VirtualFileNoBandwidthException(toSat.getiD());
            }
        }
        // Checks if available storage on destination if satellite
        if (toDev == null) {
            if (toSat.isFull()) {
                throw new unsw.blackout.FileTransferException.VirtualFileNoBandwidthException(toSat.isFullMessage());
            }
        }
        // Adds empty file to destination (not sure what to do if it is a relay satellite), lots unfinished here :\
        if (toDev == null) {
            File newFile = new File(fileName, "");
            newFile.setHasTransferCompleted(false);
            newFile.setFilesize(myFile.getFilesize());
            toSat.addFile(newFile);
        }
        else {
            File newFile = new File(fileName, "");
            newFile.setHasTransferCompleted(false);
            newFile.setFilesize(myFile.getFilesize());
            toDev.addFile(newFile);
        }
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

}
