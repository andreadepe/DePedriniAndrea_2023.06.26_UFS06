package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.List;

public class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        InputStream is = exchange.getRequestBody();

        URI uri = exchange.getRequestURI();
        System.out.println(uri);

        String method = exchange.getRequestMethod();
        System.out.println(method);

        String s = read(is); // .. read the request body
        System.out.println(s);

        String response = null;
        try {
            response = process(uri.toString());
            exchange.sendResponseHeaders(200, response.length());
        } catch (CommandException e) {
            response = "Error 400: Bad request";
            exchange.sendResponseHeaders(400, response.length());
        }


        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }



    private String process( String body) throws CommandException {
        String[] splitted = body.split("\\?|/");
        System.out.println(splitted.length + "   " + splitted[2]);
        if (splitted.length == 0)
            throw new CommandException();

        String cmd = splitted[2].split("=")[0];

        System.out.println(cmd);

        switch(cmd){
            case "cmd":
                return processCMD(splitted[2].split("=")[1]);
            default:
                throw new CommandException();
        }
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
                resp = generateHTML(wineList);
                break;
            case "sorted_by_name":
                wineList = Winery.getSortedByName();
                resp = generateHTML(wineList);
                break;
            case "sorted_by_price":
                wineList = Winery.getSortedByPrice();
                resp = generateHTML(wineList);
                break;
            default:
                throw new CommandException();
        }
        return resp;
    }

    public String generateHTML(Wine wine){
        String resp = "<!doctype html>\n" +
                "<html lang=en>\n" +
                "<head>\n" +
                "<meta charset=utf-8>\n" +
                "<title> " + "Wine" + "</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table>\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th>" + "ID" + "</th>\n" +
                "<th>" + "Name" + "</th>\n" +
                "<th>" + "Price" + "</th>\n" +
                "<th>" + "Type" + "</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                HTMLWine(wine) +
                "</tbody\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n";
        return resp;
    }

    public String generateHTML(List<Wine> wines){
        String s_wine = "";
        for(Wine wine : wines){
            s_wine += HTMLWine(wine);
        }
        String resp = "<!doctype html>\n" +
                "<html lang=en>\n" +
                "<head>\n" +
                "<meta charset=utf-8>\n" +
                "<title> " + "Wines" + "</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table>\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th>" + "ID" + "</th>\n" +
                "<th>" + "Name" + "</th>\n" +
                "<th>" + "Price" + "</th>\n" +
                "<th>" + "Type" + "</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                s_wine +
                "</tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n";
        return resp;
    }

    public String HTMLWine(Wine wine){
        return "<tr>\n" +
                "<td>" + wine.getId() + "</td>\n" +
                "<td>" + wine.getName() + "</td>\n" +
                "<td>" + wine.getPrice() + "</td>\n" +
                "<td>" + wine.getType() + "</td>\n" +
                "</tr>\n";
    }

    private String read(InputStream is) {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is));
        String received = "";
        while (true) {
            String s = "";
            try {
                if ((s = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            received += s;

        }
        return received;
    }

}