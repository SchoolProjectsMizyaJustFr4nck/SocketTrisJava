package com.client;

import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader userInput;
    private ArrayList<Integer> board;
    private boolean isMyTurn = false;
    private boolean isPlayerOne = false;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== CLIENT TIC TAC TOE ===");
            System.out.print("Inserisci l'indirizzo IP del server: ");
            String ip = scanner.nextLine().trim();
            System.out.print("Inserisci la porta del server: ");
            int port = Integer.parseInt(scanner.nextLine().trim());

            new TicTacToeClient(ip, port);
        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }

    public TicTacToeClient(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));
            board = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));

            System.out.println("Connesso al server Tic Tac Toe su " + serverAddress + ":" + port);
            start();
        } catch (IOException e) {
            System.err.println("Errore di connessione: " + e.getMessage());
        }
    }

    private void start() {
        try {
            while (true) {
                String msg = in.readLine();
                if (msg == null) {
                    System.out.println("Connessione chiusa dal server.");
                    break;
                }

                switch (msg) {
                    case "WAIT" -> {
                        isPlayerOne = true;
                        System.out.println("ei il Giocatore 1 (X). In attesa di un altro giocatore...");
                    }

                    case "READY" -> {
                        System.out.println("Partita pronta!");
                        if (isPlayerOne) {
                            System.out.println("Tocca a te! Inserisci la prima mossa (0-8):");
                            isMyTurn = true;
                            sendMove();
                            isMyTurn = false;
                        } else {
                            System.out.println("Sei il Giocatore 2 (O). Attendi la mossa dell’avversario...");
                        }
                    }

                    case "OK" -> {
                        System.out.println("Mossa valida, ora tocca all’avversario...");
                        isMyTurn = false;
                    }

                    case "KO" -> {
                        System.out.println("Mossa non valida, riprova:");
                        sendMove();
                    }

                    case "W" -> {
                        System.out.println("Hai VINTO!");
                        return;
                    }

                    case "P" -> {
                        System.out.println("Partita terminata in pareggio.");
                        return;
                    }

                    case "DISCONNECTED" -> {
                        System.out.println("L’altro giocatore si è disconnesso. Partita terminata.");
                        return;
                    }

                    default -> {
                        if (msg.contains(",")) { // aggiornamento griglia
                            updateBoard(msg);
                            printBoard();

                            String[] parts = msg.split(",");
                            String esito = "";
                            if (parts.length > 9) {
                                esito = parts[9];
                            }

                            if (esito.equals("L")) {
                                System.out.println("Hai perso!");
                                return;
                            } else if (esito.equals("P")) {
                                System.out.println(" Pareggio!");
                                return;
                            } else {
                                isMyTurn = true;
                                System.out.println(" È il tuo turno. Inserisci la tua mossa (0-8):");
                                sendMove();
                                isMyTurn = false;
                            }
                        } else {
                            System.out.println("Messaggio sconosciuto dal server: " + msg);
                        }
                    }

                }
            }
        } catch (IOException e) {
            System.err.println("Errore di comunicazione: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void sendMove() throws IOException {
        String move;
        while (true) {
            System.out.print("> ");
            move = userInput.readLine();
            try {
                int pos = Integer.parseInt(move);
                if (pos >= 0 && pos <= 8) {
                    out.println(move);
                    break;
                } else {
                    System.out.println("Inserisci un numero compreso tra 0 e 8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Inserisci un numero valido.");
            }
        }
    }

    private void updateBoard(String msg) {
        String[] parts = msg.split(",");
        board.clear();
        for (int i = 0; i < 9; i++) {
            board.add(Integer.parseInt(parts[i]));
        }
    }

    private void printBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.size(); i++) {
            sb.append(board.get(i));
            if (i < board.size() - 1)
                sb.append(",");
        }
        System.out.println("Stato attuale della griglia:");
        System.out.println(sb.toString());
    }

    private void close() {
        try {
            if (socket != null)
                socket.close();
            System.out.println("Connessione chiusa.");
        } catch (IOException e) {
            System.err.println("Errore durante la chiusura della connessione.");
        }
    }

}

// 192.168.1.136