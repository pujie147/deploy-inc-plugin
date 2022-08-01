package org.nouk.maven.plugin.service;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


public abstract class FileService<T> {
    private File using;
    private File usingTmp;

    public FileService(File using) {
        this.using = using;
        this.usingTmp = new File(using.getPath()+".tmp");
        createFile(using);
        createFile(usingTmp);
    }

    private void createFile(File file){
        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
                Files.touch(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void wireTmp(T list) throws MojoExecutionException {
        try {
            if (usingTmp.canWrite()) {
                wire(list);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new MojoExecutionException("");
        }
    }

    private void wire(T list) throws IOException {
        if (usingTmp.exists()) {
            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            final String s = gson.toJson(list);
            try(final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(usingTmp))) {
                bufferedOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    public abstract T read(String json);

    public T read() {
        if (using.canRead()) {
            try {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(using)));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    buffer.append(line);
                }
                return read(buffer.toString());
            }catch (Exception e){
            }
        }
        return null;
    }

    public void takeEffect() throws IOException {
        if (usingTmp.canRead() && using.canWrite()) {
            Files.copy(usingTmp, using);
        }
    }

}
