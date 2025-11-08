package com.server;

import java.io.*;
import java.net.*;

public class Main {

    static int[] gameBoard = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    static final int P1 = 1;
    static final int P2 = 2;

    static final int[][] winCombinations = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server in ascolto sulla porta 3000...");

            // Connessione primo giocatore
            Socket s1 = serverSocket.accept();
            System.out.println("Giocatore 1 connesso da " + s1.getInetAddress());
            BufferedReader in1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
            PrintWriter out1 = new PrintWriter(s1.getOutputStream(), true);
            out1.println("WAIT");

            // Connessione secondo giocatore
            Socket s2 = serverSocket.accept();
            System.out.println("Giocatore 2 connesso da " + s2.getInetAddress());
            BufferedReader in2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
            PrintWriter out2 = new PrintWriter(s2.getOutputStream(), true);

            out1.println("READY");
            out2.println("READY");

            int currentPlayer = P1;
            BufferedReader currentIn = in1;
            PrintWriter currentOut = out1;
            PrintWriter opponentOut = out2;

            while (true) {
                try {
                    String line = currentIn.readLine();
                    if (line == null) {
                        opponentOut.println("DISCONNECTED");
                        break;
                    }

                    int pos;
                    try {
                        pos = Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        currentOut.println("KO");
                        continue;
                    }

                    if (!isValidPos(pos) || !isEmpty(pos)) {
                        currentOut.println("KO");
                        continue;
                    }

                    gameBoard[pos] = currentPlayer;

                    if (checkWin(currentPlayer)) {
                        currentOut.println("W");
                        opponentOut.println(buildBoardString() + "L");
                        break;
                    }

                    if (checkTie()) {
                        currentOut.println("P");
                        opponentOut.println(buildBoardString() + "P");
                        break;
                    }

                    currentOut.println("OK");
                    opponentOut.println(buildBoardString());

                    if (currentPlayer == P1) {
                        currentPlayer = P2;
                        currentIn = in2;
                        currentOut = out2;
                        opponentOut = out1;
                    } else {
                        currentPlayer = P1;
                        currentIn = in1;
                        currentOut = out1;
                        opponentOut = out2;
                    }

                } catch (IOException e) {
                    System.out.println("Errore di comunicazione: " + e.getMessage());
                    break;
                }
            }

            s1.close();
            s2.close();
            System.out.println("Partita terminata, connessioni chiuse.");

        } catch (IOException e) {
            System.err.println("Errore server: " + e.getMessage());
        }
    }

    // === Metodi di supporto ===

    static boolean isValidPos(int pos) {
        return pos >= 0 && pos <= 8;
    }

    static boolean isEmpty(int pos) {
        return gameBoard[pos] == 0;
    }

    static boolean checkWin(int player) {
        for (int[] combo : winCombinations) {
            if (gameBoard[combo[0]] == player &&
                gameBoard[combo[1]] == player &&
                gameBoard[combo[2]] == player) {
                return true;
            }
        }
        return false;
    }

    static boolean checkTie() {
        for (int i : gameBoard)
            if (i == 0)
                return false;
        return true;
    }

    static String buildBoardString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gameBoard.length; i++) {
            sb.append(gameBoard[i]);
            sb.append(",");
        }
        sb.append(""); 
        return sb.toString();
    }
}
