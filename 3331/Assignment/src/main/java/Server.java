/*
 * Base structure taken from https://webcms3.cse.unsw.edu.au/COMP3331/22T2/resources/75940
 * Java multi-threading server with TCP
 * There are two points of this example code:
 * - socket programming with TCP e.g., how to define a server socket, how to exchange data between client and server
 * - multi-threading
 *
 * Author: Wei Song
 * Date: 2021-09-28
 * */

import java.net.*;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class TCPServer {

    // Server information
    private static ServerSocket serverSocket;
    private static Integer serverPort;
    private static int messageCount = 1;
    private static int userCount = 1;
    private static int roomCount = 1;

    // define ClientThread for handling multi-threading issue
    // ClientThread needs to extend Thread and override run() method
    private static class ClientThread extends Thread {
        private final Socket clientSocket;
        private boolean clientAlive = false;
        private int failAttempts;
        private boolean logged = false;
        private String userName;
        private String buffer = "";

        ClientThread(Socket clientSocket, int failAttempts) {
            this.clientSocket = clientSocket;
            this.failAttempts = failAttempts;
        }
        // Returns the number of active users and increments it by increment
        synchronized int incrementUserCount(int increment) {
            userCount = userCount + increment;
            return userCount - increment;
        }
        // Returns the number of messages and increments it by increment
        synchronized int incrementMessageCount(int increment) {
            messageCount = messageCount + increment;
            return messageCount - increment;
        }
        // Returns the number of rooms and increments it by increment
        synchronized int incrementRoomCount(int increment) {
            roomCount = roomCount + increment;
            return roomCount - increment;
        }
        // Writes to given file
        synchronized void writeToFile(String writeMessage, String file, boolean append) throws IOException {
            FileWriter fw = new FileWriter(file, append);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(writeMessage);
            bw.newLine();
            bw.close();
        }
        // Gets date section for timestamp
        public String getDate() {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");  
            Date date = new Date();
            return formatter.format(date);
        }
        // Converts string in format "dd MMM yyyy HH:mm:ss" to date
        public Date stringToDate(String date) throws ParseException{
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            return formatter.parse(date);
        } 
        // Checks whether the current user is a member of a room
        public boolean isMember(File seperateRoom) {
            Scanner scanner;
            try {
                scanner = new Scanner(seperateRoom);
            } catch (FileNotFoundException e) {
                return false;
            }
            
            String[] roomUsers = scanner.nextLine().split(" ");
            scanner.close();
            for (int i = 0; i < roomUsers.length; i++) {
                if (roomUsers[i].equals(userName)) {
                    return true;
                }
            }
            return false;
        }
        // Checks whether given user is active
        public boolean isActive(String user) {
            
            try {
                File userlog = new File("userlog.txt");
                Scanner scanner = new Scanner(userlog);
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split("; ");
                    if (line[2].equals(user)) {
                        scanner.close();
                        return true;
                    }
                }
                scanner.close();
                return false;
            } catch (FileNotFoundException e) {
                return false;
            }
            
        }

        // Finds an active user's IP address and UDP port number
        public String findAddressPort(String user) {
            try {
                File userlog = new File("userlog.txt");
                File userport = new File(user + "port.txt");
                Scanner scanner = new Scanner(userlog);
                Scanner scanner2 = new Scanner(userport);
                while (scanner.hasNextLine()) {
                    String[] activeUser = scanner.nextLine().split("; ");
                    if (activeUser[2].equals(user)) {
                        scanner.close();
                        return activeUser[3] + " " + scanner2.nextLine();
                    }
                }
                scanner.close();
                return "error";
            } catch (FileNotFoundException e) {
                return "error";
            }
            
        }

        // Login function, given input and output streams. Changes the logged variable.
        private void login(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
            boolean match = false;
            // Attempts to login failAttempts amount of times before sleeping for 10s.
            for (int i = 1; i <= failAttempts; i++) {
                // requests username
                String responseMessage = "Please input username:";
                System.out.println("[sent]\n" + responseMessage + "\n[sent]");
                dataOutputStream.writeUTF(responseMessage);
                dataOutputStream.flush();
                
                // gets username
                assert dataInputStream != null;
                assert dataOutputStream != null;
                String username = (String) dataInputStream.readUTF();
                System.out.println("[recv] " + username);

                // requests password
                responseMessage = "Please input password:";
                System.out.println("[sent]\n" + responseMessage + "\n[sent]");
                dataOutputStream.writeUTF(responseMessage);
                dataOutputStream.flush();

                // gets password
                assert dataInputStream != null;
                assert dataOutputStream != null;
                String password = (String) dataInputStream.readUTF();
                System.out.println("[recv] " + password);

                // checks if username and password pair exists
                Scanner scanner = new Scanner(new File("credentials.txt"));
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] lineSplit = line.split(" ");
                    if (lineSplit[0].equals(username) && lineSplit[1].equals(password)) {
                        match = true;
                        break;
                    }
                }

                // if username and password pair exists
                if (match) {
                    // sends welcome message
                    buffer = "===== Successfully logged in! Welcome " + username + "! =====\n";
                    
                    // records timestamp into userlog.txt
                    
                    int num = incrementUserCount(1);
                    String sDate = getDate();
                    String clientAddress = clientSocket.getInetAddress().getHostAddress();
                    int clientPort = clientSocket.getPort();
                    String writeMessage =  num + "; " + sDate + "; " + username + "; " + clientAddress + "; " + clientPort + ";";
                    if (num == 1) {
                        writeToFile(writeMessage, "userlog.txt", false);
                    }
                    else {
                        writeToFile(writeMessage, "userlog.txt", true);
                    }
                    logged = true;
                    userName = username;
                }
                /*
                else {
                    buffer = "Wrong username/password. " + (failAttempts - i) + " attempts left";
                    // Sends different message if this attempt locks user out
                    if (i == failAttempts) {
                        buffer = "Too many failed attempts. Locked out for 10s.";
                    }
                }*/
            }
            // Locks user out for 10s if fail attempts reached
            if (!match) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void doCommand(String command, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
            String [] strings = command.split(" ");
            ArrayList<String> splitCommand = new ArrayList<String>(Arrays.asList(strings));
            switch(splitCommand.get(0)) {
                case "BCM":
                    doBCM(splitCommand, dataInputStream, dataOutputStream);
                    break;
                case "ATU":
                    doATU(splitCommand, dataInputStream, dataOutputStream);
                    break;
                case "SRB":
                    doSRB(splitCommand, dataInputStream, dataOutputStream);
                    break;
                case "SRM":
                    doSRM(splitCommand, dataInputStream, dataOutputStream);
                    break;
                case "RDM":
                    doRDM(splitCommand, dataInputStream, dataOutputStream);
                    break;
                case "OUT":
                    doOUT(splitCommand, dataInputStream, dataOutputStream);
                    break;
                case "UPD":
                    doUPD(splitCommand, dataInputStream, dataOutputStream);
                    break;
                default:
                    break;
            }
        }

        private void doBCM(ArrayList<String> splitCommand, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
            if (splitCommand.size() < 2) {
                // Error for no message after BCM command
                buffer = "===== Error BCM: no message found =====\n";
            }
            else {
                int messageNumber = incrementMessageCount(1);
                String timeStamp = getDate();
                String username = userName;

                // piecing together message
                Iterator<String> it = splitCommand.iterator();
                String message = "";
                it.next();
                while (it.hasNext()) {
                    message += it.next() + " ";
                }
                message = message.substring(0, message.length() - 1);
                
                String finalMessage = messageNumber + "; " + timeStamp + "; " + username + "; " + message;
                if (messageNumber == 1) {
                    writeToFile(finalMessage, "messagelog.txt", false);
                }
                else {
                    writeToFile(finalMessage, "messagelog.txt", true);
                }

                // Sends user confirmation message
                buffer = "===== Successfully sent message: Type: BCM, Message number: " + messageNumber + ", sent at " + timeStamp + " ===== \n";
            }
        }

        private void doATU(ArrayList<String> splitCommand, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
            File userlogFile = new File ("userlog.txt");
            Scanner scanner = new Scanner(userlogFile);
            String responseMessage = "";
            while (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split("; ");
                if (!split[2].equals(userName)) {
                    responseMessage += split[2] + "; " + split[1] + "; " + split[3] + "; " + split[4] + "\n";
                }
            }
            scanner.close();
            if (responseMessage.equals("")) {
                buffer = "===== no other active user =====\n";
            }
            else {
                buffer = "===== " + responseMessage + " =====\n";
            }
        }

        private void doSRB(ArrayList<String>  splitCommand, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws FileNotFoundException, IOException{
            if (splitCommand.size() < 2) {
                // Error for no users after SRB command
                buffer = "===== Error SRB: Please enter users to be added after SRB =====\n";
            }
            else if (splitCommand.size() == 2) {
                // Error for only one user in room
                buffer = "===== Error SRB: Please enter at least two users to be added =====\n";
            }
            else {
                // Gets online/existing users from userlog.txt
                File userlogFile = new File ("userlog.txt");
                Scanner scanner = new Scanner(userlogFile);
                ArrayList<String> users = new ArrayList<String>();
                while (scanner.hasNextLine()) {
                    String[] split = scanner.nextLine().split("; ");
                    users.add(split[2]);
                }
                scanner.close();
                // Checks if any given users do not exist/are online
                String userString = "";
                for (int i = 1; i < splitCommand.size(); i++) {
                    if (!users.contains(splitCommand.get(i))) {
                        buffer = "===== Error SRB: User '" + splitCommand.get(i) + "' does not exist/is not online =====\n";
                        return;
                    }
                    else {
                        userString += splitCommand.get(i) + " ";
                    }
                }
                // Removes extra space at the end
                userString = userString.substring(0, userString.length() - 1);

                // Checks if any existing rooms contains exactly the users given
                for (int i = 1; i < roomCount; i++) {
                    File room = new File ("SR_" + i + "_messagelog.txt");
                    Scanner scanner2 = new Scanner(room);
                    String[] roomUsers = scanner2.nextLine().split(" ");
                    scanner2.close();
                    int numMatchingUsers = 0;
                    for (int j = 0; j < roomUsers.length; j++) {
                        if (users.contains(roomUsers[j])) {
                            numMatchingUsers++;
                        }
                        else {
                            numMatchingUsers = 0;
                        }
                        if (numMatchingUsers == users.size()) {
                            buffer = "===== Error SRB: a separate room (ID: " + i + ") already created for these users??????.=====\n";
                            return;
                        }
                    }

                }
                // Creates room if none of the above are triggered
                int num = incrementRoomCount(1);
                writeToFile(userString, "SR_" + num + "_messagelog.txt", false);
                buffer = "===== Separate chat room has been created, room ID: " + num + ", users in this room: " + userString + ". =====\n";
            }
        }

        private void doSRM(ArrayList<String>  splitCommand, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws FileNotFoundException, IOException{
            if (splitCommand.size() < 3) {
                // Error for not enough arguments
                buffer = "===== Error SRM: Usage SRM roomID message =====\n";
            }
            else {
                // Gets roomID, checking whether it's an int and above 0
                int roomId;
                try {
                    roomId = Integer.parseInt(splitCommand.get(1));
                }
                catch (NumberFormatException e) {
                    roomId = 0;
                }
                if (roomId < 1) {
                    buffer = "===== Error SRM: roomID must be an integer greater than 0 =====\n";
                    return;
                }

                // Checks if seperate room exists
                File seperateRoom = new File("SR_" + roomId + "_messagelog.txt");
                if (!seperateRoom.exists()) {
                    buffer = "===== Error SRM: The separate room does not exist =====\n";
                    return;
                }
                
                // Checks if user is a member of the separate room
                boolean member = false;
                Scanner scanner = new Scanner(seperateRoom);
                String[] roomUsers = scanner.nextLine().split(" ");
                for (int i = 0; i < roomUsers.length; i++) {
                    if (roomUsers[i].equals(userName)) {
                        member = true;
                        break;
                    }
                }
                if (!member) {
                    buffer = "===== Error SRM: You are not in this separate room chat =====\n";
                    scanner.close();
                    return;
                }

                // Gets all required components of message to be sent
                int messageNumber = 1;
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split("; ");
                    messageNumber = Integer.parseInt(line[0]) + 1;
                }
                scanner.close();
                String timestamp = getDate();
                String message = "";
                for (int i = 2; i < splitCommand.size(); i++) {
                    message += splitCommand.get(i) + " ";
                }
                // Getting rid of extra space
                message = message.substring(0, message.length() - 1);
                String write = messageNumber + "; " + timestamp + "; " + userName + "; " + message;
                // Sends message
                writeToFile(write, "SR_" + roomId + "_messagelog.txt", true);

                // Confirmation message
                buffer = "Successfully sent message: SRM, " + messageNumber + ", " + timestamp + ", " + message + "\n";
            }
        }
        
        private void doRDM(ArrayList<String>  splitCommand, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
            if (splitCommand.size() < 6) {
                // Error for not enough arguments
                buffer = "===== Error RDM: Usage RDM messageType dd MMM yyyy HH:mm:ss =====\n"; 
                return;
            }
            // Gets type of message to return
            String type = splitCommand.get(1);
            // Gets timestamp
            String sDate = splitCommand.get(2) + " " + splitCommand.get(3) + " " + splitCommand.get(4) + " " + splitCommand.get(5);
            Date date;
            try {
                date = stringToDate(sDate);
            }
            catch (ParseException e) {
                //date = new Date("01 Jan 3000 01:01:01");
                buffer = "===== Error RDM: invalid date format. Must be dd MMM yyyy HH:mm:ss\n";
                return;
            }
            // If broadcast type
            if (type.equals("b")) {
                // Checks if messagelog exists
                File messageLog = new File("messagelog.txt");
                if (!messageLog.exists()) {
                    buffer = "===== no new message =====\n";
                    return;
                }
                // Finds lines after given timestamp and prepares to send them
                boolean newMessages = false;
                Scanner scanner;
                try {
                    scanner = new Scanner(messageLog);
                }
                catch (FileNotFoundException e) {
                    return;
                }
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Date lineDate;
                    try {
                        lineDate = stringToDate(line.split("; ")[1]);
                    }
                    catch (ParseException e) {
                        return;
                    }
                    if (lineDate.after(date)) {
                        newMessages = true;
                        buffer += line + "\n";
                    }
                }
                scanner.close();
                if (!newMessages) {
                    buffer = "===== no new message =====\n";
                }

            }
            // If seperate room type
            else if (type.equals("s")) {
                boolean newMessages = false;
                for (int i = 1; i < roomCount; i++) {
                    File sRM = new File("SR_" + i + "_messagelog.txt");
                    if (isMember(sRM)) {
                        // Finds lines after given timestamp and prepares to send them
                        Scanner scanner;
                        try {
                            scanner = new Scanner(sRM);
                        }
                        catch (FileNotFoundException e) {
                            return;
                        }
                        scanner.nextLine();
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            Date lineDate;
                            try {
                                lineDate = stringToDate(line.split("; ")[1]);
                            }
                            catch (ParseException e) {
                                scanner.close();
                                return;
                            }
                            if (lineDate.after(date)) {
                                newMessages = true;
                                buffer += line + "\n";
                            }
                        }
                        scanner.close();
                    }
                }
                if (!newMessages) {
                    buffer = "===== no new message =====\n";
                }
            }
            else {
                // Error for unknown type
                buffer = "===== Error RDM: messageType must be 's' or 'b' =====\n";
            }
        }

        private void doOUT(ArrayList<String>  splitCommand, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
            List<String> lines = new ArrayList<String>();
            File userlog = new File("userlog.txt");
            try {
                FileReader fr = new FileReader(userlog);
                BufferedReader br = new BufferedReader(fr);
                boolean change = false;
                String line = "";
                while ((line = br.readLine()) != null) {
                    if (!line.contains("; " + userName + "; ")){
                        if (change) {
                            int num = Character.getNumericValue(line.charAt(0)) - 1;
                            lines.add(num + line.substring(1));
                        }
                        else {
                            lines.add(line);
                        }
                    }
                    else {
                        change = true;
                    }
                }
                fr.close();
                br.close();

                // Wiping current userlog.txt
                new PrintWriter("userlog.txt").close();
                // Writing edited lines back to userlog.txt
                for (String l : lines) {
                    writeToFile(l, "userlog.txt", true);
                }
            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                return;
            }
            userCount -= 1;

        }

        // Sends IP and port of given user if active. Sends "inactive" if inactive.
        private void doUPD(ArrayList<String>  splitCommand, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
            if (splitCommand.size() < 3) {
                // Error for wrong usage type
                buffer +="===== Error UPD: Usage UPD username filename =====\n";
                return;
            }
            if (isActive(splitCommand.get(1))) {
                try {
                    dataOutputStream.writeUTF(findAddressPort(splitCommand.get(1)));
                    dataOutputStream.flush();
                } catch (IOException e) {
                    return;
                }
                
            }
            else {
                String s = "inactive";
                try {
                    dataOutputStream.writeUTF(s);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    return;
                }
            }
        }


        @Override
        public void run() {
            super.run();
            // get client Internet Address and port number
            String clientAddress = clientSocket.getInetAddress().getHostAddress();
            int clientPort = clientSocket.getPort();
            String clientID = "("+ clientAddress + ", " + clientPort + ")";

            System.out.println("===== New connection created for user - " + clientID);
            clientAlive = true;

            // define the dataInputStream to get message (input) from client
            // DataInputStream - used to acquire input from client
            // DataOutputStream - used to send data to client
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;
            try {
                dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
                dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String udpPort;
            try {
                assert dataInputStream != null;
                assert dataOutputStream != null;
                udpPort = (String) dataInputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while (clientAlive) {
                try {
                    // get input from client
                    assert dataInputStream != null;
                    assert dataOutputStream != null;

                    if (!logged) {
                        login(dataInputStream, dataOutputStream);
                        writeToFile(udpPort, userName + "port.txt", false);
                    }
                    // once user is logged in
                    else {
                        // requests user choose one of seven available commands
                        buffer += "\nPlease input one of the following style commands:\nBCM message\nATU\nSRB username1 username2 ...\nSRM roomID message\nRDM messageType dd MMM yyyy HH:mm:ss\nOUT\nUPD username filename";
                        System.out.println("[sent]\n" + buffer + "\n[sent]");
                        dataOutputStream.writeUTF(buffer);
                        dataOutputStream.flush();
                        buffer = "";

                        // gets command and executes relevant function
                        assert dataInputStream != null;
                        assert dataOutputStream != null;
                        String command = (String) dataInputStream.readUTF();
                        System.out.println("[recv] " + command);
                        doCommand(command, dataInputStream, dataOutputStream);
                    }
                } catch (EOFException e) {
                    System.out.println("===== the user disconnected, user - " + clientID);
                    clientAlive = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("===== Error usage: java TCPServer SERVER_PORT NUMBER_OF_CONSECUTIVE_FAILED_ATTEMPTS =====");
            return;
        }

        // acquire port number from command line parameter
        serverPort = Integer.parseInt(args[0]);

        // acquire number of consecutive fail attempts from command line parameter
        int failAttempts;
        // Accounts for floating point numbers
        try {
            failAttempts = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            failAttempts = 0;
        }
        if (failAttempts < 1 || failAttempts > 5) {
            System.out.println("===== Error: NUMBER_OF_CONSECUTIVE_FAILED_ATTEMPTS must be an integer between 1 and 5 =====");
            return;
        }

        // define server socket with the input port number, by default the host would be localhost i.e., 127.0.0.1
        serverSocket = new ServerSocket(serverPort);
        // make serverSocket listen connection request from clients
        System.out.println("===== Server is running =====");
        System.out.println("===== Waiting for connection request from clients...=====");

        while (true) {
            // when new connection request reaches the server, then server socket establishes connection
            Socket clientSocket = serverSocket.accept();
            // for each user there would be one thread, all the request/response for that user would be processed in that thread
            // different users will be working in different thread which is multi-threading (i.e., concurrent)
            ClientThread clientThread = new ClientThread(clientSocket, failAttempts);
            clientThread.start();
        }
    }
}
