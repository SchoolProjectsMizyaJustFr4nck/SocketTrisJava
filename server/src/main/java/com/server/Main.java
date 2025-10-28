package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
//Server
public class Main {
    public static void main(String[] args) throws IOException {


        System.out.println("Server socket in ascolto sulla porta 3000");
        ServerSocket ss = new ServerSocket(3000);   //apro server socket sulla porta 3000
        Socket myS = ss.accept();                        //crea un socket in ascolta da server, accept blocca, finche qualcuno non si collega alla porta 3000
        System.out.println("Si è collegato un utente con ip: " + myS.getInetAddress());

        BufferedReader in = new BufferedReader(new InputStreamReader(myS.getInputStream()));
        PrintWriter out = new PrintWriter(myS.getOutputStream(), true);
        
        out.println("Welcome to server v2.1");
        int i = 0;

        int row = -1;
        int col = -1;
        String move = "";

        do{

            while(true){

                do{

                    row = Integer.parseInt(in.readLine());
                    if(checkPos(row)){

                        out.println("ERROR: WRITE A EXISTED LINE");
                    }
                    
                }while(checkPos(row));

                do{

                    col = Integer.parseInt(in.readLine());
                    if(checkPos(col)){

                        out.println("ERROR: WRITE A EXISTED COLUMN");
                    }
                    
                }while(checkPos(col));

                

                i++;
                break;
            }
            
            



            
        }while(i < 9);
        ss.close();  //chiudo la porta
        
    }



    public static Boolean checkPos(int p){

        if(p > 3 || p < 1){

            return false;
        }
        return true;
    }
}

//10.22.9.6