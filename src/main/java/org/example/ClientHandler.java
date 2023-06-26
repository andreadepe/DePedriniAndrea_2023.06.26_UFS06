package org.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        InetAddress address = clientSocket.getInetAddress();
        int port = clientSocket.getPort();
        System.out.println("Connected: " + address + ":" + port);
    }

    void handle(){
        PrintWriter out = null; // allocate to write answer to client.
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            readLoop(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Boolean readLoop(BufferedReader in,  PrintWriter out ) {
        // waits for data and reads it in until connection dies
        // readLine() blocks until the server receives a new line from client
        String s = "";
        ClientManager cm = ClientManager.getInstance();
        try {
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                String response = null;
                try {
                    response = processCMD(s);
                } catch (CommandException e) {
                    response = "Command not recognized";
                }
                out.println(response);
                out.flush();
            }
            InetAddress address = clientSocket.getInetAddress();
            int port = clientSocket.getPort();
            System.out.println("done on: " + address + ":" + port);
            cm.remove(this);
            System.out.println("Number clients: " + cm.nClients());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String processCMD(String cmd) throws CommandException {
        String resp = null;
        Gson gson = new Gson();
        List<Wine> wineList;
        String lowerCmd = cmd.toLowerCase();
        switch(lowerCmd){
            case "red":
            case "white":
                wineList = Winery.getWineOfType(lowerCmd);
                resp = gson.toJson(wineList);
                break;
            case "sorted_by_name":
                wineList = Winery.getSortedByName();
                resp = gson.toJson(wineList);
                break;
            case "sorted_by_price":
                wineList = Winery.getSortedByPrice();
                resp = gson.toJson(wineList);
                break;
            default:
                throw new CommandException();
        }
        return resp;
    }

    @Override
    public void run() {
        handle();
    }
}