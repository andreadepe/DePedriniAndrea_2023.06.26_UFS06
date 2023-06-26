package org.example;

import java.util.ArrayList;
import java.util.List;

public final class ClientManager {

    private static ClientManager INSTANCE;

    List<ClientHandler> clientList = new ArrayList<>();

    private ClientManager() {
    }

    public static ClientManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ClientManager();
        }

        return INSTANCE;
    }

    // getters and setters
    void add(ClientHandler clientHandler){
        clientList.add(clientHandler);
    }

    void remove(ClientHandler clientHandler){
        clientList.remove(clientHandler);
    }

    int nClients(){
        return clientList.size();
    }
}