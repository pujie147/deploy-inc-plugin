package org.nouk.maven.plugin.service.impl;

import com.google.common.collect.Maps;
import com.google.inject.internal.util.Sets;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.*;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.nouk.maven.plugin.entiry.JarInfo;
import org.nouk.maven.plugin.entiry.ProjectInfo;
import org.nouk.maven.plugin.service.MavenProjectService;
import org.nouk.maven.plugin.utils.MavenUtil;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.*;

@Named
@Singleton
public class MavenProjectServiceImpl implements MavenProjectService {
    @Inject
    protected org.apache.maven.project.ProjectBuilder projectBuilder;

    private Map<String,MavenProject> allProjects = Maps.newLinkedHashMap();
    private Map<String,MavenProject> allProjectsIgnorePom = Maps.newLinkedHashMap();

    @Parameter
    private List<String> ignoredPackagings = Arrays.asList( "pom", "ear" );

    @Override
    public Map<String,MavenProject> getAllProjects(MavenProject project, MavenSession session) throws ComponentLookupException, ProjectBuildingException {
        if (allProjects.size()==0) {
            getProject(MavenUtil.getGroupIdRootProject(project),session);
        }
        return allProjects;
    }

    @Override
    public Map<String, Artifact> getAllJars(MavenProject project, MavenSession session){
        Set<String> allModelName = getAllProjectsSet(project, session);
        Map<String,Artifact> jars = Maps.newHashMap();
        for (Artifact artifact : project.getArtifacts()) {
            final JarInfo jarInfo = new JarInfo(artifact);
            if (!allModelName.contains(jarInfo.getName())) {
                jars.put(jarInfo.getName(),artifact);
            }
        }
        return jars;
    }

    @Override
    public Set<String> getAllProjectsSet(MavenProject project, MavenSession session) {
        final HashSet<String> objects = Sets.newHashSet();
        try {
            final Map<String, MavenProject> allProjects = getAllProjects(project, session);
            for (MavenProject value : allProjects.values()) {
                final ProjectInfo projectInfo = new ProjectInfo(value);
                objects.add(projectInfo.getName());
            }
        } catch (ComponentLookupException e) {
            e.printStackTrace();
        } catch (ProjectBuildingException e) {
            e.printStackTrace();
        }
        return objects;
    }

    @Override
    public Set<String> getAllProjectsIgnorePomSet(MavenProject project, MavenSession session) {
        final HashSet<String> objects = Sets.newHashSet();
        try {
            final Map<String, MavenProject> allProjects = getAllProjectsIgnorePom(project, session);
            for (MavenProject value : allProjects.values()) {
                objects.add(value.getGroupId()+":"+value.getArtifactId());
            }
        } catch (ComponentLookupException e) {
            e.printStackTrace();
        } catch (ProjectBuildingException e) {
            e.printStackTrace();
        }
        return objects;
    }

    @Override
    public Map<String,MavenProject> getAllProjectsIgnorePom(MavenProject project, MavenSession session) throws ComponentLookupException, ProjectBuildingException {
        if (allProjectsIgnorePom.size()==0) {
            getProject(MavenUtil.getGroupIdRootProject(project),session);
        }
        return allProjectsIgnorePom;
    }

    private void getProject(MavenProject project, MavenSession session) throws ProjectBuildingException {
        ProjectBuildingRequest configuration = new DefaultProjectBuildingRequest();
        configuration.setRepositorySession(session.getRepositorySession());
        for(String module : project.getModules()){
            File modulePom = new File(project.getBasedir(), module);
            if(modulePom.isDirectory()) {
                modulePom = new File(modulePom, "pom.xml");
            }
            final MavenProject mavenProject = projectBuilder.build(modulePom, configuration).getProject();
            final ProjectInfo projectInfo = new ProjectInfo(mavenProject);
            if (!ignoredPackagings.contains(mavenProject.getPackaging())) {
                allProjectsIgnorePom.put(projectInfo.getName(),mavenProject);
            }
            allProjects.put(projectInfo.getName(),mavenProject);
            if (mavenProject.getModules()!=null && mavenProject.getModules().size()>0) {
                this.getProject(mavenProject,session);
            }
        }
    }

}
