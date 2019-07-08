package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessRedirect {
    public void redirect(Process process, ProcessType processType) throws IOException {
        switch (processType){
            default:
                redirectInput(process);
                break;
            case INPUT:
                redirectInput(process);
                break;
            case ERROR:
                redirectOutput(process);
                break;
        }

    }

    private void redirectInput(Process process) throws IOException{
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }

        input.close();
    }

    private void redirectOutput(Process process) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }

        input.close();
    }
}
