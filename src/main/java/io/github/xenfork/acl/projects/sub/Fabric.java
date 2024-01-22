package io.github.xenfork.acl.projects.sub;

import cn.hutool.core.io.FileUtil;
import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import dev.architectury.plugin.ArchitectPluginExtension;
import io.github.xenfork.acl.projects.AllProjects;
import io.github.xenfork.acl.projects.SubProjects;
import io.github.xenfork.acl.settings.MainSettings;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.configuration.ide.RunConfigSettings;
import net.fabricmc.loom.task.RemapJarTask;
import net.fabricmc.loom.task.RemapSourcesJarTask;
import net.fabricmc.loom.util.Constants;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.component.ConfigurationVariantDetails;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Provider;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.internal.component.DefaultAdhocSoftwareComponent;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static io.github.xenfork.acl.settings.MainSettings.acl;

public class Fabric extends Basic {


    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        architecturyDepends();
        flvDepends();
        target.getPlugins().apply(ShadowPlugin.class);

        Project common_project = target.findProject(":" + archivesBaseName + "-common");
        if (common_project != null) {
            LoomGradleExtensionAPI common_loom = (LoomGradleExtensionAPI) common_project.getExtensions().getByName("loom");
            if (common_loom.getAccessWidenerPath().isPresent()) {
                loom.getAccessWidenerPath().set(common_loom.getAccessWidenerPath().get());
            }
            for (RunConfigSettings runConfig : loom.getRunConfigs()) {
                runConfig.runDir("../common/run/fabric");
            }

        }

        ArchitectPluginExtension architectury = target.getExtensions().getByType(ArchitectPluginExtension.class);
        architectury.platformSetupLoomIde();
        architectury.fabric();
        String script = """
                def tps = "%s"
                sourceSets {
                    main {
                        resources {
                            srcDirs += rootProject.file("$tps/common/src/fabric/resources")
                            srcDirs -= file("src/main/resources")
                        }
                        java {
                            srcDirs += rootProject.file("$tps/common/src/fabric/java")
                            srcDirs -= file("src/main/java")
                        }
                    }
                }
                
                configurations {
                    common
                    shadowCommon // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
                    compileClasspath.extendsFrom common
                    runtimeClasspath.extendsFrom common
                    developmentFabric.extendsFrom common
                }
                dependencies {
                    common(project(path: ":$tps-common", configuration: "namedElements")) { transitive false }
                    shadowCommon(project(path: ":$tps-common", configuration: "transformProductionFabric")) { transitive false }
                }
                
                processResources {
                    inputs.property "version", project.version
                    filesMatching("META-INF/mods.toml") {
                        expand "version": project.version
                    }
                }
                
                shadowJar {
                    configurations = [project.configurations.shadowCommon]
                    archiveClassifier.set("dev-shadow")
                }
                
                remapJar {
                    inputFile.set shadowJar.archiveFile
                    dependsOn shadowJar
                    archiveClassifier.set(null)
                }
                        
                jar {
                    archiveClassifier.set("dev")
                }
                        
                sourcesJar {
                    def commonSources = project(":$tps-common").sourcesJar
                    dependsOn commonSources
                    from commonSources.archiveFile.map { zipTree(it) }
                }
                                
                components.java {
                    withVariantsFromConfiguration(project.configurations.shadowRuntimeElements) {
                        skip()
                    }
                }
                """.formatted(archivesBaseName);
        FileUtil.touch(allScript);
        BufferedWriter writer = FileUtil.getWriter(allScript, StandardCharsets.UTF_8, false);
        try {
            writer.write(script);
            writer.close();
        } catch (IOException ignored) {}
        target.apply(action -> {
            action.from(allScript);
        });

        if (!acl.getFapi().isEmpty()) {
            dependencies.add("modApi", "net.fabricmc.fabric-api:fabric-api:" + acl.getFapi());
        }

        PublishingExtension publishing = target.getExtensions().getByType(PublishingExtension.class);
        RepositoryHandler repositories = publishing.getRepositories();
        repositories.maven(mvn -> {
            mvn.setUrl(target.getRootProject().file("rootmaven"));
        });
        PublicationContainer publications = publishing.getPublications();
        MavenPublication mavenCommon = publications.create("mavenFabric", MavenPublication.class);
        mavenCommon.setArtifactId(target.getName() + "-" + acl.getMcversion());
        mavenCommon.from(target.getComponents().getByName("java"));
    }
}
