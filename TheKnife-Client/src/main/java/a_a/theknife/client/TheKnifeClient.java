package a_a.theknife.client;

import java.io.*;
import java.net.*;

public class TheKnifeClient extends Thread{
    public void run(){
        try(Socket socket = new Socket(InetAddress.getLocalHost(), 10002);
            BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketPrintWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)
            ){
            socketPrintWriter.println("utenti");
            System.out.println(socketBufferedReader.readLine());
            
            socketPrintWriter.println("END");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
