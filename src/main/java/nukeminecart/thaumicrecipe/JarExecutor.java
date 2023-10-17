package nukeminecart.thaumicrecipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JarExecutor {

    private BufferedReader error;
    private BufferedReader op;
    private int exitVal;

    public void executeJar(String jarFilePath,String... args) throws NullPointerException {
        // Create run arguments for the

        String javaloc = System.getProperty("java.home");
        String separator = System.getProperty("file.separator");
        javaloc = javaloc + separator + "bin" + separator;
        final List<String> actualArgs = new ArrayList<>();
        actualArgs.add(0, javaloc + "java.exe");
        actualArgs.add(1, "-jar");
        actualArgs.add(2, jarFilePath);
        actualArgs.addAll(Arrays.asList(args));
        try {
            final Runtime re = Runtime.getRuntime();
            //final Process command = re.exec(cmdString, args.toArray(new String[0]));
            final Process command = re.exec(actualArgs.toArray(new String[0]));
            this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
            this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));
            // Wait for the application to Finish
            command.waitFor();
            this.exitVal = command.exitValue();
            if (this.exitVal != 0) {
                throw new IOException("Failed to execure jar, " + this.getExecutionLog()+"->"+jarFilePath);
            }

        } catch (final IOException | InterruptedException e) {
            throw new NullPointerException(e.getMessage());
        }
    }


    public String getExecutionLog() {
        StringBuilder error = new StringBuilder();
        String line;
        try {
            while((line = this.error.readLine()) != null) {
                error.append("\n").append(line);
            }
        } catch (final IOException ignored) {
        }
        StringBuilder output = new StringBuilder();
        try {
            while((line = this.op.readLine()) != null) {
                output.append("\n").append(line);
            }
        } catch (final IOException ignored) {
        }
        try {
            this.error.close();
            this.op.close();
        } catch (final IOException ignored) {
        }
        return "exitVal: " + this.exitVal + ", error: " + error + ", output: " + output;
    }
}
