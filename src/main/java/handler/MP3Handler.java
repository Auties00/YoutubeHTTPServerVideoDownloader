package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.FileWriter;
import youtube.YoutubeDownloader;

import java.io.*;

public class MP3Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Trying to handle request...");

        String toSearch = exchange.getRequestURI().toString()
                .replace("/download/mp3/", "")
                .replaceAll("_", " ");

        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/octec-stream");
        headers.add("Content-Disposition","attachment;filename=" + toSearch + ".mp3");
        exchange.sendResponseHeaders(200, 0);

        File mp3 = new YoutubeDownloader().downloadMP3File(toSearch);
        if(mp3 == null){
            return;
        }

        new FileWriter().writeFIle(exchange, mp3);
    }
}
