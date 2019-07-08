package youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.apache.commons.lang3.ObjectUtils;
import utils.ProcessCustomBuilder;
import utils.ProcessRedirect;
import utils.ProcessType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class YoutubeDownloader {
    private static final String APPLICATION_NAME = "Youtube Client";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private YouTube getService() {
        NetHttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return new YouTube.Builder(httpTransport, JSON_FACTORY,  getHttpRequestInitializer())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private HttpRequestInitializer getHttpRequestInitializer() {
        return httpRequest -> {
        };
    }

    public List<SearchResult> getResults(String toSearch) {
        try {
            System.out.println("Starting to fetch results on youtube for " + toSearch + "\n");
            YouTube youtubeService = getService();
            if(youtubeService == null){
                System.out.println("Youtube service == null");
                return null;
            }

            YouTube.Search.List search = youtubeService.search().list("snippet");
            search.setKey("AIzaSyD0DE9PZHpthQR-A4QxPMYdEUj97lL2wjA");
            search.setQ(toSearch);
            search.setType("video");
            search.setMaxResults(25L);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            System.out.println("These videos were fetched: " + searchResultList.toString() + "\n");
            return searchResponse.getItems();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }


    public File downloadWEBMFile(String toSearch) throws IOException{
        List<SearchResult> searchResultList = getResults(toSearch);
        System.out.println("These videos were fetched: " + searchResultList.toString() + "\n");

        SearchResult accurateResult = searchResultList.get(0);
        System.out.println("Selected as an accurate result " + accurateResult.getSnippet().getTitle() + " by " + accurateResult.getSnippet().getChannelTitle() + "\n");
        String url = "https://www.youtube.com/watch?v=" + accurateResult.getId().getVideoId();

        System.out.println("Selected as an accurate result " + url + " named " + accurateResult.getSnippet().getTitle() + " by " + accurateResult.getSnippet().getChannelTitle() + "\n");

        String command = "youtube-dl -f 251 " + url;
        Process process = new ProcessCustomBuilder().buildProcess(command.split(" "));
        while (process.isAlive()){
            //WAIT
        }

        File processOutput = new File("/root/" + accurateResult.getSnippet().getTitle() + "-" + accurateResult.getId().getVideoId() + ".webm");
        if(!processOutput.exists()){
            throw new NullPointerException("File does not exist!");
        }

        File finalFIle = new File("/root/" + accurateResult.getId().getVideoId() + ".webm");
        processOutput.renameTo(finalFIle);

        return finalFIle;
    }

    public File downloadMP3File(String toSearch) throws IOException{
        List<SearchResult> searchResultList = getResults(toSearch);

        SearchResult accurateResult = searchResultList.get(0);
        String url = "https://www.youtube.com/watch?v=" + accurateResult.getId().getVideoId();

        String command = "youtube-dl -i --extract-audio --audio-format mp3 --audio-quality 0 " +  url;
        Process process = new ProcessCustomBuilder().buildProcess(command.split(" "));
        while (process.isAlive()){
            //WAIT
        }

        File processOutput = new File("/root/" + accurateResult.getSnippet().getTitle() + "-" + accurateResult.getId().getVideoId() + ".mp3");
        if(!processOutput.exists()){
            throw new NullPointerException("File does not exist!");
        }

        File finalFIle = new File("/root/" + accurateResult.getId().getVideoId() + ".mp3");
        processOutput.renameTo(finalFIle);

        return finalFIle;
    }
}
