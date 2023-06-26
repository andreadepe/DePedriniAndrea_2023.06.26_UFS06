package org.example;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerHTTP
{
    public static void main( String[] args ) {
        HttpServer server = null;
        Gson gson = new Gson();
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ClassLoader classLoader = ServerHTTP.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("wines.json");
            String data = readFromInputStream(inputStream);
            System.out.println(data);
            Wine[] listWine = new Wine[0];
            listWine = gson.fromJson(data,listWine.getClass());
            System.out.println(listWine[1]);
            Winery.getInstance().add(listWine);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Can't read file: using default values");
            Winery.buildList();
        }
        //Oracle code was: server.createContext("/applications/myapp", new MyHandler());
        server.createContext("/", new MyHttpHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException,NullPointerException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
