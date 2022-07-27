package org.nouk.maven.plugin.utils;

import com.google.inject.internal.util.Sets;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.project.*;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class MavenUtil {

    /**
     * 获取root project
     * @param project
     * @return
     */
    public static MavenProject getRootProject(MavenProject project){
        MavenProject curProject = project;
        while(curProject.getParent()!=null){
            curProject = curProject.getParent();
        }
        return curProject;
    }

    /**
     * 获取相同groupId的root project
     * @param project
     * @return
     */
    public static MavenProject getGroupIdRootProject(MavenProject project){
        MavenProject curProject = project;
        String groupId = project.getGroupId();
        while(curProject.getParent()!=null&&groupId.equals(project.getGroupId())){
            curProject = curProject.getParent();
        }
        return curProject;
    }

    public static Set<String> getAllModelName(MavenProject project){
        final String groupId = project.getGroupId();
        final List<String> modules = MavenUtil.getGroupIdRootProject(project).getModules();
        final HashSet<String> objects = Sets.newHashSet();
        for (String module : modules) {
            objects.add(groupId+":"+module);
        }
        return objects;
    }
//    public ProjectBuildingResult createProject(File pomFile) throws ComponentLookupException, ProjectBuildingException {
//        ProjectBuildingRequest configuration = new DefaultProjectBuildingRequest();
//        configuration.setRepositorySession( session.getRepositorySession() );
//        ModelSource modelSource = new FileModelSource( pomFile );
//        ProjectBuildingResult result = container.lookup( org.apache.maven.project.ProjectBuilder.class ).build( modelSource, configuration );
//        return result;
//    }

}
