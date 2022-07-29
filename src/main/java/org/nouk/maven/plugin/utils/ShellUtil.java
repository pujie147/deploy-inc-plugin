package org.nouk.maven.plugin.utils;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;

public class ShellUtil {

    public static int execToString(String cmd) throws MojoExecutionException{
        int exitCode = 1;
        try {
            CommandLine commandline = CommandLine.parse(cmd);
            DefaultExecutor exec = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(System.out);
            exec.setStreamHandler(streamHandler);
            exitCode = exec.execute(commandline);
        }catch (IOException e) {
            e.printStackTrace();
            throw new MojoExecutionException("");
        }
        if (exitCode != 0) {
            throw new MojoExecutionException("");
        }
        return exitCode;
    }

}
