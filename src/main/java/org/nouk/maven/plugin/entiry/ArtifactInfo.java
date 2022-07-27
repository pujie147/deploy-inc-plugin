package org.nouk.maven.plugin.entiry;

import com.google.gson.annotations.Expose;
import org.apache.maven.artifact.Artifact;
import org.nouk.maven.plugin.utils.DependencyUtil;

import java.io.Serializable;

public class ArtifactInfo implements Serializable {
    @Expose
    private String name;
    @Expose
    private String groupId;
    @Expose
    private String artifactId;
    @Expose
    private String version;
    @Expose
    private String scope;
    @Expose
    private String type;
    @Expose
    private String classifier;


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }


    public String getName() {
        return name;
    }

    public void setName(Artifact name) {
        this.name = DependencyUtil.getFormattedFileName(name,false);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}
