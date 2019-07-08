package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.FileWriter;
import youtube.YoutubeDownloader;

import java.io.*;

public class Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Trying to handle request...");

        String toSearch = exchange.getRequestURI().toString()
                .replace("/api/download/", "")
                .replaceAll("_", " ");

        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/octec-stream");
        headers.add("Content-Disposition","attachment;filename=" + toSearch + ".webm");
        exchange.sendResponseHeaders(200, 0);

        File downloaded = new YoutubeDownloader().downloadWEBMFile(toSearch);
        if(downloaded == null){
            System.out.println("The file was null!");
            return;
        }

        new FileWriter().writeFIle(exchange, downloaded);
    }
}
