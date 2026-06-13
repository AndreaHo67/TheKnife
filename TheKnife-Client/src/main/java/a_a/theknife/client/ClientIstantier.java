package a_a.theknife.client;

import java.io.*;
import java.net.*;

public class ClientIstantier{
    public static void main(String[] args){
        for(int i = 0 ; i < 10; i++){
            TheKnifeClient theKnifeClient = new TheKnifeClient();
            theKnifeClient.start();
            try{
                theKnifeClient.join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("Terminato");
    }
}