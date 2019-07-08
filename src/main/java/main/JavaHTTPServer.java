package main;

import com.sun.net.httpserver.HttpServer;
import handler.Handler;
import handler.MP3Handler;
import handler.SearchHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class JavaHTTPServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/download/", new Handler());
        server.createContext("/download/mp3/", new MP3Handler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Server Started!");
        System.out.println("Listening to: " + server.getAddress().getHostName() + ":" + server.getAddress().getPort());
    }
}
