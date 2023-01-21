/*
 * Base structure taken from https://webcms3.cse.unsw.edu.au/COMP3331/22T2/resources/75938
 * Java socket programming client example with TCP
 * socket programming at the client side, which provides example of how to define client socket, how to send message to
 * the server and get response from the server with DataInputStream and DataOutputStream
 *
 * Author: Wei Song
 * Date: 2021-09-28
 * */


import java.net.*;
import java.io.*;


public class TCPClient {
    // server host and port number, which would be acquired from command line parameter
    private static String serverHost;
    private static Integer serverPort;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("===== Error usage: java TCPClient SERVER_IP SERVER_PORT =====");
            return;
        }

        serverHost = args[0];
        serverPort = Integer.parseInt(args[1]);

        // define socket for client
        Socket clientSocket = new Socket(serverHost, serverPort);

        // define DataInputStream instance which would be used to receive response from the server
        // define DataOutputStream instance which would be used to send message to the server
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

        // define a BufferedReader to get input from command line i.e., standard input from keyboard
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        DatagramSocket socket = new DatagramSocket();

        // Give UDP port number
        System.out.println(Integer.toString(socket.getLocalPort()));
        dataOutputStream.writeUTF(Integer.toString(socket.getLocalPort()));
        dataOutputStream.flush();

        while (true) {
            // receive the server response from dataInputStream
            String responseMessage = (String) dataInputStream.readUTF();
            System.out.println("[recv]\n" + responseMessage + "\n[recv]");
            System.out.println("===== Please input any message you want to send to the server: ");

            // read input from command line
            String message = reader.readLine();
            String[] splitMessage = message.split(" ");

            // Case for UPD command
            if (splitMessage[0].equals("UPD")) {
                if (splitMessage.length < 3) {
                    // Error for wrong usage type
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                }
                else {
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();

                    String response = (String) dataInputStream.readUTF();
                    if (response.equals("inactive")) {
                        System.out.println("===== Error UPD: given user is inactive =====\n");
                        System.out.println("Please input one of the following style commands:\nBCM message\nATU\nSRB username1 username2 ...\nSRM roomID message\nRDM messageType dd MMM yyyy HH:mm:ss\nOUT\nUPD username filename");
                    }
                    else if (response.equals("error")) {
                        System.out.println("===== Error UPD: Error in finding IP/port =====\n");
                        System.out.println("Please input one of the following style commands:\nBCM message\nATU\nSRB username1 username2 ...\nSRM roomID message\nRDM messageType dd MMM yyyy HH:mm:ss\nOUT\nUPD username filename");
                    }
                    else {
                        // gets port and ip address with correct types
                        String ip = response.split(" ")[0];
                        InetAddress ipAddress = InetAddress.getByName(ip);
                        String port = response.split(" ")[1];
                        int intPort = Integer.parseInt(port);

                        /*
                         * Need to create multithread for receiving video files, not completed.
                         * Below is for logic for sending file.
                         
                         InputStream inputStream = new FileInputStream(splitMessage[3]);
                         ds.send(new DatagramPacket(, ipAddress, intPort));
                
                         */

                        /*
                         * multithread would consist of the following logic:
                         * while (true) {
                         *     DatagramPacket p = new DatagramPacket();
                         *     socket.receive(p);
                         * }
                         */

                        String m = "hi";
                        // Alignment of message receive to send
                        dataOutputStream.writeUTF(m);
                        dataOutputStream.flush();
                    }
                }
            }
            else {
                // write message into dataOutputStream and send/flush to the server
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
            }

            // If user inputs out
            if (message.equals("OUT")){
                System.out.println("Shutting down connection with server.");
                clientSocket.close();
                dataOutputStream.close();
                dataInputStream.close();
                break;
            }
        }
    }
}
