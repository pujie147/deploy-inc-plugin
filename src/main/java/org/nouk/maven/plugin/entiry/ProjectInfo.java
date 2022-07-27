package org.nouk.maven.plugin.entiry;


import com.google.gson.annotations.Expose;
import org.apache.commons.io.FileUtils;
import org.apache.maven.project.MavenProject;

import java.io.File;

public class ProjectInfo extends ArtifactInfo {
    @Expose
    private Long size;
    @Expose
    private String pathSrc;
    @Expose
    private String pathPom;

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPathSrc() {
        return pathSrc;
    }

    public void setPathSrc(String pathSrc) {
        this.pathSrc = pathSrc;
    }

    public String getPathPom() {
        return pathPom;
    }

    public void setPathPom(String pathPom) {
        this.pathPom = pathPom;
    }

    public ProjectInfo(MavenProject mavenProject) {
        setGroupId(mavenProject.getGroupId());
        setArtifactId(mavenProject.getArtifactId());
        setName(mavenProject.getArtifact());
        setVersion(mavenProject.getVersion());
        setPathSrc(mavenProject.getFile().getParent() + File.separator+"src");
        setPathPom(mavenProject.getFile().getPath());
        final File file = new File(getPathSrc());
        if (file.exists()) {
            setSize(FileUtils.sizeOf(file));
        }
    }

    public ProjectInfo() {
    }
}
