package org.nouk.maven.plugin.service;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.nouk.maven.plugin.entiry.JarInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MavenProjectService {
    Map<String,MavenProject> getAllProjects(MavenProject project, MavenSession session) throws ComponentLookupException, ProjectBuildingException;

    Map<String, Artifact> getAllJars(MavenProject project, MavenSession session);

    Set<String> getAllProjectsSet(MavenProject project, MavenSession session);

    Set<String> getAllProjectsIgnorePomSet(MavenProject project, MavenSession session);

    Map<String,MavenProject> getAllProjectsIgnorePom(MavenProject project, MavenSession session) throws ComponentLookupException, ProjectBuildingException;
}
