package org.nouk.maven.plugin.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.nouk.maven.plugin.entiry.ProjectInfo;
import org.nouk.maven.plugin.service.FileService;

import java.io.File;
import java.util.Map;

public class ProjectInfoDBServiceImpl extends FileService<Map<String,ProjectInfo>> {
    public ProjectInfoDBServiceImpl(File using) {
        super(using);
    }

    @Override
    public Map<String, ProjectInfo> read(String json) {
        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(json, new TypeToken<Map<String, ProjectInfo>>() {
        }.getType());
    }


}
