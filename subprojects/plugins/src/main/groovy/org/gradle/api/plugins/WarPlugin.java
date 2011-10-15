/*
 * Copyright 2009 the original author or authors.
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

package org.gradle.api.plugins;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.PublishArtifact;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.tasks.bundling.War;

import java.util.concurrent.Callable;

/**
 * <p>A {@link Plugin} which extends the {@link JavaPlugin} to add tasks which assemble a web application into a WAR
 * file.</p>
 *
 * @author Hans Dockter
 */
public class WarPlugin implements Plugin<Project> {
    public static final String PROVIDED_COMPILE_CONFIGURATION_NAME = "providedCompile";
    public static final String PROVIDED_RUNTIME_CONFIGURATION_NAME = "providedRuntime";
    public static final String WAR_TASK_NAME = "war";
    public static final String WEB_APP_GROUP = "web application";

    public void apply(final Project project) {
        project.getPlugins().apply(JavaPlugin.class);
        final WarPluginConvention pluginConvention = new WarPluginConvention(project);
        project.getConvention().getPlugins().put("war", pluginConvention);

        project.getTasks().withType(War.class, new Action<War>() {
            public void execute(War task) {
                task.from(new Callable() {
                    public Object call() throws Exception {
                        return pluginConvention.getWebAppDir();
                    }
                });
                task.dependsOn(new Callable() {
                    public Object call() throws Exception {
                        return project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().getByName(
                                SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath();
                    }
                });
                task.classpath(new Object[] {new Callable() {
                    public Object call() throws Exception {
                        FileCollection runtimeClasspath = project.getConvention().getPlugin(JavaPluginConvention.class)
                                .getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath();
                        Configuration providedRuntime = project.getConfigurations().getByName(
                                PROVIDED_RUNTIME_CONFIGURATION_NAME);
                        return runtimeClasspath.minus(providedRuntime);
                    }
                }});
            }
        });
        
        War war = project.getTasks().add(WAR_TASK_NAME, War.class);
        war.setDescription("Generates a war archive with all the compiled classes, the web-app content and the libraries.");
        war.setGroup(BasePlugin.BUILD_GROUP);
        Configuration archivesConfiguration = project.getConfigurations().getByName(Dependency.ARCHIVES_CONFIGURATION);
        removeJarTaskFromArchivesConfiguration(project, archivesConfiguration);
        archivesConfiguration.getArtifacts().add(new ArchivePublishArtifact(war));
        configureConfigurations(project.getConfigurations());
    }

    private void removeJarTaskFromArchivesConfiguration(Project project, Configuration archivesConfiguration) {
        Jar jarTask = (Jar) project.getTasks().getByName(JavaPlugin.JAR_TASK_NAME);
        removeJarTaskFromArchivesConfiguration(archivesConfiguration, jarTask);
    }

    private void removeJarTaskFromArchivesConfiguration(Configuration archivesConfiguration, Jar jar) {
        // todo: There should be a richer connection between an ArchiveTask and a PublishArtifact
        for (PublishArtifact publishArtifact : archivesConfiguration.getAllArtifacts()) {
            if (publishArtifact instanceof ArchivePublishArtifact) {
                ArchivePublishArtifact archivePublishArtifact = (ArchivePublishArtifact) publishArtifact;
                if (archivePublishArtifact.getArchiveTask() == jar) {
                    archivesConfiguration.getArtifacts().remove(publishArtifact);
                }
            }
        }
    }

    public void configureConfigurations(ConfigurationContainer configurationContainer) {
        Configuration provideCompileConfiguration = configurationContainer.add(PROVIDED_COMPILE_CONFIGURATION_NAME).setVisible(false).
                setDescription("Additional compile classpath for libraries that should not be part of the WAR archive.");
        Configuration provideRuntimeConfiguration = configurationContainer.add(PROVIDED_RUNTIME_CONFIGURATION_NAME).setVisible(false).
                extendsFrom(provideCompileConfiguration).
                setDescription("Additional runtime classpath for libraries that should not be part of the WAR archive.");
        configurationContainer.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME).extendsFrom(provideCompileConfiguration);
        configurationContainer.getByName(JavaPlugin.RUNTIME_CONFIGURATION_NAME).extendsFrom(provideRuntimeConfiguration);
    }
}
