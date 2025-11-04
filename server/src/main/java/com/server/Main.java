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


            int currentPlayer = p1;  
            BufferedReader currentInput = in1;  
            PrintWriter currentOutput = out1;  
            BufferedReader otherInput = in2;  
            PrintWriter otherOutput = out2;    

            while(true){

                do{
                    pos = Integer.parseInt(currentInput.readLine());
                    if(!testInputPos(pos) || !checkIfExist(pos)){
                        currentOutput.println("KO");
                    } 
                    else if(winCondition(gameBoard, currentPlayer)){
                        addVal(gameBoard, pos, currentPlayer);
                        currentOutput.println("W");
                        otherOutput.println(printArr(gameBoard) + ", L");
                        ss.close(); 
                        return;
                    }
                    else if(checkTie(gameBoard)){
                        addVal(gameBoard, pos, currentPlayer);
                        currentOutput.println("P");
                        otherOutput.println("P");
                        ss.close();
                        return;
                    }
                    else{
                        addVal(gameBoard, pos, currentPlayer);
                        currentOutput.println("OK");
                        otherOutput.println(printArr(gameBoard));
                    }

                    if (currentPlayer == p1) {
                        currentPlayer = p2;
                        currentInput = in2;
                        currentOutput = out2;
                        otherInput = in1;
                        otherOutput = out1;
                    } else {
                        currentPlayer = p1;
                        currentInput = in1;
                        currentOutput = out1;
                        otherInput = in2;
                        otherOutput = out2;
                    }
                }while(!testInputPos(pos) || !checkIfExist(pos));
            }
            
            
            
        }
    
    
        static public Boolean testInputPos(int pos){
            return pos >= 0 && pos < 8;
        }
    
        static public Boolean checkIfExist(int pos){
    
            if(gameBoard[pos] != 0){
    
                return false;
            }
          
            return true;
        }

        static public void addVal(int gameBoard[], int pos, int p){

            gameBoard[pos] = p;
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

