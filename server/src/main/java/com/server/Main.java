package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
//Server
public class Main {

    static int [] gameBoard = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    static int pos = 0;
    static int p1 = 1;
    static int p2 = 2;

    static int[][] winCombinations = {
            {0, 1, 2}, // prima riga
            {3, 4, 5}, // seconda riga
            {6, 7, 8}, // terza riga
            {0, 3, 6}, // prima colonna
            {1, 4, 7}, // seconda colonna
            {2, 5, 8}, // terza colonna
            {0, 4, 8}, // diagonale principale
            {2, 4, 6}  // diagonale secondaria
        };
    
        public static void main(String[] args) throws IOException {
            
            System.out.println("Server socket in ascolto sulla porta 3000");
            ServerSocket ss = new ServerSocket(3000);   //apro server socket sulla porta 3000
            
            //UTENTE 1
            Socket myS1 = ss.accept();                        //crea un socket in ascolta da server, accept blocca, finche qualcuno non si collega alla porta 3000
            System.out.println("Si è collegato il primo utente con ip: " + myS1.getInetAddress());
            BufferedReader in1 = new BufferedReader(new InputStreamReader(myS1.getInputStream()));
            PrintWriter out1 = new PrintWriter(myS1.getOutputStream(), true);
            out1.println("WAIT");
    
            //UTENTE 2
            Socket myS2 = ss.accept();
            System.out.println("Si è collegato il primo utente con ip: " + myS2.getInetAddress());
            BufferedReader in2 = new BufferedReader(new InputStreamReader(myS2.getInputStream()));
            PrintWriter out2 = new PrintWriter(myS2.getOutputStream(), true);
            out1.println("READY");
            out2.println("READY");

            while(true){

                do{
                    pos = Integer.parseInt(in1.readLine()) - 1;
                    if(!testInputPos(pos) || checkIfExist(pos)){
                        out1.println("KO");
                    } else if(winCondition(gameBoard, p1)){
                        out1.println("W");
                        out2.println(printArr(gameBoard) + ", L");
                        ss.close(); 
                        return;
                    }
                    else if(checkTie(gameBoard)){
                        out1.println("P");
                        out2.println("P");
                        ss.close();
                        return;
                    }
                    else{
                        out1.println("OK");
                        out2.println(printArr(gameBoard));
                    }
                }while(testInputPos(pos));
    
                do{
                    pos = Integer.parseInt(in2.readLine()) - 1;
                    if(!testInputPos(pos) || checkIfExist(pos)){
                        out2.println("KO");
                    } else if(winCondition(gameBoard, p2)){
                        out2.println("W");
                        out1.println(printArr(gameBoard) + ", L");
                        ss.close(); 
                        return;
                    }
                    else if(checkTie(gameBoard)){
                        out1.println("P");
                        out2.println("P");
                        ss.close();
                        return;
                    }
                    else{
                        out2.println("OK");
                        out1.println(printArr(gameBoard));
                    }
                }while(testInputPos(pos));
            }
            
             //chiudo la porta
            
        }
    
    
        static public Boolean testInputPos(int pos){
            
            if(pos < 0 || pos >= 9){
    
                return false;
            }
            return true;
        }
    
        static public Boolean checkIfExist(int pos){
    
            if(gameBoard[pos] != 0){
    
                return false;
            }
          
            return true;
        }
    
        static public String printArr(int gameBoard[]){
            StringBuilder s = new StringBuilder();
    
            for (int i = 0; i < gameBoard.length; i++) {
                s.append(gameBoard[i]);  
                if (i < gameBoard.length - 1) {
                    s.append(", "); 
                }
            }
    
            return s.toString();
        }
    
        static public Boolean winCondition(int gameBoard [], int p){
            for (int[] combination : winCombinations) {
                if (gameBoard[combination[0]] == p &&
                gameBoard[combination[1]] == p &&
                gameBoard[combination[2]] == p) {
                return true; 
                }
            }
        return false;
        }

        static public Boolean checkTie(int gameBoard []){
            
            for(int i = 0; i < gameBoard.length; i++){

                if(gameBoard[i] == 0){

                    return false;
                }
            }
            return true;
        }
        
    }

//10.22.9.6

