package org.nouk.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.collect.Sets;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.nouk.maven.plugin.entiry.JarInfo;
import org.nouk.maven.plugin.entiry.ProjectInfo;
import org.nouk.maven.plugin.service.impl.JarInfoDBServiceImpl;
import org.nouk.maven.plugin.service.impl.ProjectInfoDBServiceImpl;
import org.nouk.maven.plugin.utils.DependencyUtil;
import org.nouk.maven.plugin.utils.GZipUtil;
import org.nouk.maven.plugin.utils.MavenUtil;
import org.nouk.maven.plugin.utils.ShellUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Mojo( name = "build-inc", requiresDependencyResolution = ResolutionScope.COMPILE, defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
@Execute(phase = LifecyclePhase.INITIALIZE)
public class BuildIncMojo extends AbstractDependencyMojo {
    /**
     * Either append the artifact's baseVersion or uniqueVersion to the filename. Will only be used if
     *
     * @since 2.6
     */
    @Parameter( property = "mdep.useBaseVersion", defaultValue = "true" )
    protected boolean useBaseVersion = true;

    /**
     * Prepend the groupId during copy.
     *
     * @since 2.2
     */
    @Parameter( property = "mdep.prependGroupId", defaultValue = "false" )
    protected boolean prependGroupId = false;

    /**
     * Place each type of file in a separate subdirectory. (example /outputDirectory/runtime /outputDirectory/provided
     * etc)
     *
     * @since 2.2
     */
    @Parameter( property = "mdep.useSubDirectoryPerScope", defaultValue = "false" )
    protected boolean useSubDirectoryPerScope;

    /**
     * Place each type of file in a separate subdirectory. (example /outputDirectory/jars /outputDirectory/wars etc)
     *
     * @since 2.0-alpha-1
     */
    @Parameter( property = "mdep.useSubDirectoryPerType", defaultValue = "false" )
    protected boolean useSubDirectoryPerType;

    /**
     * Place each file in a separate subdirectory. (example <code>/outputDirectory/junit-3.8.1-jar</code>)
     *
     * @since 2.0-alpha-1
     */
    @Parameter( property = "mdep.useSubDirectoryPerArtifact", defaultValue = "false" )
    protected boolean useSubDirectoryPerArtifact;

    /**
     * Strip artifact version during copy
     */
    @Parameter( property = "mdep.stripVersion", defaultValue = "false" )
    protected boolean stripVersion = false;

    /**
     * <p>
     * Place each artifact in the same directory layout as a default repository.
     * </p>
     * <p>
     * example:
     * </p>
     *
     * <pre>
     *   /outputDirectory/junit/junit/3.8.1/junit-3.8.1.jar
     * </pre>
     *
     * @since 2.0-alpha-2
     */
    @Parameter( property = "mdep.useRepositoryLayout", defaultValue = "false" )
    protected boolean useRepositoryLayout;
    /**
     * Strip artifact classifier during copy
     */
    @Parameter( property = "mdep.stripClassifier", defaultValue = "false" )
    protected boolean stripClassifier = false;

    @Parameter( property = "incOutputDirectory", defaultValue = "${project.build.directory}/inc-dependency" )
    protected File incOutputDirectory;
    @Parameter( property = "incOutputDirectory", defaultValue = "${project.build.directory}" )
    protected File outputDirectory;

    /**
     * 必定下载jar包
     */
    @Parameter(property = "mustDownloadJars")
    protected List<String> mustDownloadJars;

    @Override
    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        ProjectInfoDBServiceImpl projectInfoDBService = new ProjectInfoDBServiceImpl(incProjectOutputCache);
        JarInfoDBServiceImpl jarInfoDBService = new JarInfoDBServiceImpl(incJarOutputCache);

        // 过滤增量 jars
        final Map<String, JarInfo> jarInfos = jarInfoDBService.read();

        final Map<String, Artifact> jars = mavenProjectService.getAllJars(getProject(), session);
        if (jars != null && jars.size()>0) {
            if(jarInfos!=null && jars.size()>0) {
                getLog().info(String.format("----------------- build inc jars difference comparison begin -----------------"));
                final HashSet<String> strings = (HashSet<String>) Sets.newHashSet(jars.keySet()).clone();
                for (String jarKey : strings) {
                    if (containMustDownloadJars(jarKey)) {
                        getLog().info(String.format("jarName:%s action:ignore version:%s",jarKey,jars.get(jarKey).getVersion()));
                        continue;
                    }
                    if (jarInfos.containsKey(jarKey)) {
                        jars.remove(jarKey);
                    }else{
                        getLog().info(String.format("jarName:%s action:update version:%s",jarKey,jars.get(jarKey).getVersion()));
                    }
                }
                getLog().info(String.format("----------------- build inc jars difference comparison end -----------------"));
            }
            final File lib = new File(incOutputDirectory, "lib");
            if (!lib.exists()) {
                lib.mkdirs();
            }
            // 增量jar复制到统一目录下
            for (Artifact value : jars.values()) {
                copyArtifact( value, stripVersion, this.prependGroupId, this.useBaseVersion,
                        this.stripClassifier );
            }
        }
        getLog().info("-----------------------------jar info update cache-----------------------------");
        for (String key : jars.keySet()) {
            jarInfos.put(key,new JarInfo(jars.get(key)));
        }
        for (JarInfo jInfo : jarInfos.values()) {
            getLog().info("name:"+jInfo.getName()+" version:"+jInfo.getVersion());
        }
        jarInfoDBService.wireTmp(jarInfos);
        // 过滤增量模块
        final Map<String, ProjectInfo> projectInfoMap = projectInfoDBService.read();
        final Map<String, MavenProject> allProjectsIgnorePom = mavenProjectService.getAllProjectsIgnorePom(getProject(), session);

        if (allProjectsIgnorePom != null && allProjectsIgnorePom.size()>0) {
            getLog().info(String.format("----------------- build inc projects difference comparison begin -----------------"));
            if(projectInfoMap!=null&& projectInfoMap.size()>0) {
                final HashSet<String> strings = (HashSet<String>) Sets.newHashSet(allProjectsIgnorePom.keySet()).clone();
                for (String projectName : strings) {
                    final ProjectInfo projectInfo = new ProjectInfo(allProjectsIgnorePom.get(projectName));
                    if (projectInfoMap.containsKey(projectName)) {
                        if (projectInfo.getSize().compareTo(projectInfoMap.get(projectName).getSize())==0) {
                            allProjectsIgnorePom.remove(projectName);
                        }else{
                            getLog().info(String.format("projectName:%s status:exist action:update oldSize:%d newSize:%d",projectName,projectInfoMap.get(projectName).getSize(),projectInfo.getSize()));
                        }
                    }else{
                        getLog().info(String.format("projectName:%s status:notExist action:new newSize:%d",projectName,projectInfo.getSize()));
                    }
                }
            }
            getLog().info(String.format("----------------- build inc projects difference comparison end -----------------"));
            final ArtifactRepository localRepository = getProject().getProjectBuildingRequest().getLocalRepository();
            final String basedir = localRepository.getBasedir();
            final StringBuilder stringBuilder = new StringBuilder();
            for (MavenProject value : allProjectsIgnorePom.values()) {
                stringBuilder.append(":"+value.getName()+",");
            }

            String substring = "";
            // 安装 模块
            if (allProjectsIgnorePom.values().size()>0) {
                substring = stringBuilder.substring(0, stringBuilder.length() - 1);
                final String cmd = "mvn install -f " + MavenUtil.getGroupIdRootProject(getProject()).getFile().getPath() + " -pl " + substring + " -Dmdep.skip=true -DdeployInc.skip=true -Dassembly.skipAssembly=true -Dmaven.test.skip=true";
                getLog().info("------------------------------------<SHELL>------------------------------------");
                getLog().info(cmd);
                getLog().info("------------------------------------<SHELL>------------------------------------");
                ShellUtil.execToString(cmd);
            }
            // 增量模块复制到统一目录下
            for (MavenProject value : allProjectsIgnorePom.values()) {
                final Artifact artifact = value.getArtifact();
                artifact.setFile(new File(basedir,localRepository.pathOf(artifact)));
                copyArtifact(artifact, stripVersion, this.prependGroupId, this.useBaseVersion,
                        this.stripClassifier);
            }
            getLog().info("-----------------------------project info update cache-----------------------------");
            for (String key : allProjectsIgnorePom.keySet()) {
                projectInfoMap.put(key,new ProjectInfo(allProjectsIgnorePom.get(key)));
            }
            for (ProjectInfo projectInfo : projectInfoMap.values()) {
                getLog().info("name:"+projectInfo.getName()+" version:"+projectInfo.getVersion()+" size:"+projectInfo.getSize());
            }
            projectInfoDBService.wireTmp(projectInfoMap);
        }
        final File resourcesFile = new File(getProject().getFile().getParentFile(), "src" + File.separator + "main" + File.separator + "resources");
        if (resourcesFile.exists()) {
            try {
                final File conf = new File(incOutputDirectory, "conf");
                if (!conf.exists()) {
                    conf.mkdirs();
                }
                FileUtils.copyDirectoryStructure(resourcesFile,conf);
            } catch (IOException e) {
                getLog().error("resources file copy fail!");
                getLog().error(e.getMessage());
            }
        }
        getLog().info("build package compression start >>>>>>");
        final String packageFileName = "february-biz-inc";
        getLog().info("build package-name:"+packageFileName+" ");
        // 压缩打包
        GZipUtil.compression(incOutputDirectory.getPath(),outputDirectory.getPath(), packageFileName);
        getLog().info("build package compression end <<<<<<<");
    }

    private boolean containMustDownloadJars(String jarKey){
        if (mustDownloadJars!=null&& mustDownloadJars.size()>0) {
            for (String s : mustDownloadJars) {
                if (jarKey.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Copies the Artifact after building the destination file name if overridden. This method also checks if the
     * classifier is set and adds it to the destination file name if needed.
     *
     * @param artifact representing the object to be copied.
     * @param removeVersion specifies if the version should be removed from the file name when copying.
     * @param prependGroupId specifies if the groupId should be prepend to the file while copying.
     * @param theUseBaseVersion specifies if the baseVersion of the artifact should be used instead of the version.
     * @param removeClassifier specifies if the classifier should be removed from the file name when copying.
     * @throws MojoExecutionException with a message if an error occurs.
     * @see #copyFile(File, File)
     * @see DependencyUtil#getFormattedOutputDirectory(boolean, boolean, boolean, boolean, boolean, File, Artifact)
     */
    protected void copyArtifact(Artifact artifact, boolean removeVersion, boolean prependGroupId,
                                 boolean theUseBaseVersion, boolean removeClassifier )
            throws MojoExecutionException
    {

        String destFileName = DependencyUtil.getFormattedFileName( artifact, removeVersion, prependGroupId,
                theUseBaseVersion, removeClassifier );

        File destDir = DependencyUtil.getFormattedOutputDirectory( useSubDirectoryPerScope, useSubDirectoryPerType,
                useSubDirectoryPerArtifact, useRepositoryLayout,
                stripVersion, new File(incOutputDirectory,"lib"), artifact );
        File destFile = new File( destDir, destFileName );

        copyFile( artifact.getFile(), destFile );
    }

//    /**
//     * Copy the pom files associated with the artifacts.
//     *
//     * @param destDir The destination directory {@link File}.
//     * @param artifacts The artifacts {@link Artifact}.
//     * @param removeVersion remove version or not.
//     * @param removeClassifier remove the classifier or not.
//     * @throws MojoExecutionException in case of errors.
//     */
//    public void copyPoms( File destDir, Set<Artifact> artifacts, boolean removeVersion, boolean removeClassifier )
//            throws MojoExecutionException
//
//    {
//        for ( Artifact artifact : artifacts )
//        {
//            Artifact pomArtifact = getResolvedPomArtifact( artifact );
//
//            // Copy the pom
//            if ( pomArtifact != null && pomArtifact.getFile() != null && pomArtifact.getFile().exists() )
//            {
//                File pomDestFile =
//                        new File( destDir, DependencyUtil.getFormattedFileName( pomArtifact, removeVersion, prependGroupId,
//                                useBaseVersion, removeClassifier ) );
//                if ( !pomDestFile.exists() )
//                {
//                    copyFile( pomArtifact.getFile(), pomDestFile );
//                }
//            }
//        }
//    }


//    /**
//     * @param artifact {@link Artifact}
//     * @return {@link Artifact}
//     */
//    protected Artifact getResolvedPomArtifact( Artifact artifact )
//    {
//        DefaultArtifactCoordinate coordinate = new DefaultArtifactCoordinate();
//        coordinate.setGroupId( artifact.getGroupId() );
//        coordinate.setArtifactId( artifact.getArtifactId() );
//        coordinate.setVersion( artifact.getVersion() );
//        coordinate.setExtension( "pom" );
//
//        Artifact pomArtifact = null;
//        // Resolve the pom artifact using repos
//        try
//        {
//            ProjectBuildingRequest buildingRequest = newResolveArtifactProjectBuildingRequest();
//
//            pomArtifact = artifactResolver.resolveArtifact( buildingRequest, coordinate ).getArtifact();
//        }
//        catch ( ArtifactResolverException e )
//        {
//            getLog().info( e.getMessage() );
//        }
//        return pomArtifact;
//    }


}
