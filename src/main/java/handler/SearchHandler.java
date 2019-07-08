package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.FileWriter;
import youtube.YoutubeDownloader;

import java.io.File;
import java.io.IOException;

public class SearchHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            System.out.println("Trying to handle request...");

            String toSearch = exchange.getRequestURI().toString()
                    .replace("/search/", "")
                    .replaceAll("_", " ");

            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "application/octec-stream");
            headers.add("Content-Disposition","attachment;filename=" + toSearch + ".yaml");
            exchange.sendResponseHeaders(200, 0);

            File file = new FileWriter().serializeResults(new YoutubeDownloader().getResults(toSearch));
            if(file == null){
                System.out.println("The search result is null!");
                return;
            }

            new FileWriter().writeFIle(exchange, file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
