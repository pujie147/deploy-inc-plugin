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


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;



@Mojo(name = "persistence-inc", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class PersistenceIncInfoMojo extends AbstractDependencyMojo {
//
//    @Parameter( property = "incOutputCache", defaultValue = "${project.build.directory}/inc-cache/jar.classInfo" )
//    protected File incJarOutputCache;
//    @Parameter( property = "incOutputCache", defaultValue = "${project.build.directory}/inc-cache/project.classInfo" )
//    protected File incProjectOutputCache;
//
//    @Inject
//    private MavenProjectService mavenProjectService;
//
//    @Parameter( defaultValue = "${session}", readonly = true, required = true )
//    protected MavenSession session;


    @Override
    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        persistenceIncInfo();
    }
}
