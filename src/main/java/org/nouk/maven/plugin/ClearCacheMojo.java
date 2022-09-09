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
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;


@Mojo( name = "clearCache", requiresDependencyResolution = ResolutionScope.COMPILE, defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
@Execute(phase = LifecyclePhase.INITIALIZE)
public class ClearCacheMojo extends AbstractDependencyMojo {

    @Override
    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        final File parentFile = this.incJarOutputCache.getParentFile();
        getLog().info("cache file :"+parentFile.getPath()+" exists:"+parentFile.exists());
        if (parentFile.exists()) {
            try {
                FileUtils.deleteDirectory(parentFile);
            } catch (IOException e) {
                getLog().info("fileExists:"+parentFile.getPath()+" Delete fail !");
                return;
            }
            getLog().info("fileExists:"+parentFile.getPath()+" Deleted");
        }
    }

}
