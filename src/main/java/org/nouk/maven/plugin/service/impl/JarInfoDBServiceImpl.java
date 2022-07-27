package org.nouk.maven.plugin.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.nouk.maven.plugin.entiry.JarInfo;
import org.nouk.maven.plugin.service.FileService;

import java.io.File;
import java.util.Map;

public class JarInfoDBServiceImpl extends FileService<Map<String,JarInfo>> {
    public JarInfoDBServiceImpl(File using) {
        super(using);
    }

    @Override
    public Map<String, JarInfo> read(String json) {
        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(json, new TypeToken<Map<String, JarInfo>>() {
        }.getType());
    }


}
