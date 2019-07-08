package utils;

import com.google.api.services.youtube.model.SearchResult;
import com.sun.net.httpserver.HttpExchange;
import configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class FileWriter {
    public void writeFIle(HttpExchange exchange, File file) {
        try {
            try (BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody())) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    byte [] buffer = new byte [inputStream.available()];
                    int count;
                    while ((count = inputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            file.delete();
            exchange.getResponseBody().close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public File serializeResults(List<SearchResult> searchResults) {
        try {
            File file = new File(UUID.randomUUID().toString() + ".yaml");
            file.createNewFile();
            YamlConfiguration fc = new YamlConfiguration().loadConfiguration(file);
            searchResults.forEach(result -> {
                fc.set("name", result.getSnippet().getTitle());
                fc.set("artist", result.getSnippet().getChannelTitle());
                fc.set("artUrl", result.getSnippet().getThumbnails().getStandard().getUrl());
                fc.save(file);
            });

            return file;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
