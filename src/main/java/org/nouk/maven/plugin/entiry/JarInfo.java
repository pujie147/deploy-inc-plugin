package org.nouk.maven.plugin.entiry;


import com.google.gson.annotations.Expose;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;

public class JarInfo extends ArtifactInfo {


    private ArtifactHandler artifactHandler;


    public ArtifactHandler getArtifactHandler() {
        return artifactHandler;
    }

    public void setArtifactHandler(ArtifactHandler artifactHandler) {
        this.artifactHandler = artifactHandler;
    }

    public JarInfo(Artifact artifact) {
        this.setGroupId(artifact.getGroupId());
        this.setArtifactId(artifact.getArtifactId());
        this.setVersion(artifact.getVersion());
        this.setScope(artifact.getScope());
        this.setType(artifact.getType());
        this.setClassifier(artifact.getClassifier());
        this.artifactHandler = artifact.getArtifactHandler();
        this.setName(artifact);
    }

    public DefaultArtifact toDefaultArtifact(){
        return new DefaultArtifact(this.getGroupId(), this.getArtifactId(), this.getVersion(), getScope(), getType(), getClassifier(), artifactHandler);
    }

    public JarInfo(DefaultArtifact defaultArtifact) {
        this.setGroupId(defaultArtifact.getGroupId());
        this.setArtifactId(defaultArtifact.getArtifactId());
        this.setVersion(defaultArtifact.getVersion());
        this.setScope(defaultArtifact.getScope());
        this.setType(defaultArtifact.getType());
        this.setClassifier(defaultArtifact.getClassifier());
        this.artifactHandler = defaultArtifact.getArtifactHandler();
        this.setName(defaultArtifact);
    }
}
