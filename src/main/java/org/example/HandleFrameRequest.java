//HandleFrameRequest Lato Client
package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HandleFrameRequest {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private List<Wine> wineList = new ArrayList<>();
    public HandleFrameRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
        setupStreams();
    }

    public void setupStreams() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeConex() {
        sendRequest("EXIT");

    }

    public List<Wine> getResponse(String s){
        try {
            sendRequest(s);
            String response = receiveResponse();
            wineList = fromJsonToList(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return wineList;
    }

    private void sendRequest(String request) {
        out.println(request);
        out.flush();
    }

    private String receiveResponse() throws IOException {
        StringBuilder jsonData = new StringBuilder();
        String line;
        line = in.readLine();
        jsonData.append(line);
        return jsonData.toString();
    }

    private List<Wine> fromJsonToList(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Wine>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    public String transformToJson(List<Wine> carList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(carList);
    }
    public void closeConnections() {
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}