package utils;

import java.io.IOException;

public class ProcessCustomBuilder {
    public Process buildProcess(String[] command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        processBuilder.redirectInput(java.lang.ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(java.lang.ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(java.lang.ProcessBuilder.Redirect.INHERIT);

        return processBuilder.start();
    }
}
