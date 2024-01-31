package io.github.xenfork.acl.projects.sub;

import cn.hutool.core.io.FileUtil;
import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import dev.architectury.plugin.ArchitectPluginExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.configuration.ide.RunConfigSettings;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.github.xenfork.acl.settings.MainSettings.acl;

/**
 * @author baka4n
 */
public class Forge extends Basic {

    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        target.getPlugins().apply(ShadowPlugin.class);
        architecturyDepends();
        ArchitectPluginExtension architectury = target.getExtensions().getByType(ArchitectPluginExtension.class);
        architectury.platformSetupLoomIde();
        architectury.forge();
        Project common_project = target.findProject(":" + archivesBaseName + "-common");
        if (common_project != null) {
            LoomGradleExtensionAPI common_loom = (LoomGradleExtensionAPI) common_project.getExtensions().getByName("loom");
            if (common_loom.getAccessWidenerPath().isPresent()) {
                loom.getAccessWidenerPath().set(common_loom.getAccessWidenerPath().get());
            }
            loom.forge(action -> {
                action.mixinConfig(archivesBaseName + ".mixins.json", archivesBaseName + "-common.mixins.json");
                if (common_loom.getAccessWidenerPath().isPresent()) {
                    action.getConvertAccessWideners().set(true);
                }
                loom.runs(act -> {
                    RunConfigSettings datagen = act.create("datagen");
                    datagen.data();
                    datagen.programArgs("--all", "--mod", archivesBaseName);
                    datagen.programArgs("--output", common_project.file("src/main/generated/resources").getAbsolutePath());
                });
                for (RunConfigSettings runConfig : loom.getRunConfigs()) {
                    runConfig.runDir("../common/run/forge");
                }
            });
        }

        String script = """
                def tps = "%s"
                
                sourceSets {
                    main {
                        java {
                            srcDirs += rootProject.file("$tps/common/src/main/datagen")
                            srcDirs += rootProject.file("$tps/common/src/forge/java")
                            srcDirs -= file("src/main/java")
                        }
                        resources {
                            srcDirs += rootProject.file("$tps/common/src/main/forge")
                            srcDirs += rootProject.file("$tps/common/src/forge/resources")
                            srcDirs -= file("src/main/resources")
                        }
                    }
                }
                
                configurations {
                    common
                    shadowCommon // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
                    compileClasspath.extendsFrom common
                    runtimeClasspath.extendsFrom common
                    developmentForge.extendsFrom common
                }
                
                dependencies {
                    common(project(path: ":$tps-common", configuration: "namedElements")) { transitive false }
                    shadowCommon(project(path: ":$tps-common", configuration: "transformProductionForge")) { transitive = false }
                }
                
                processResources {
                    inputs.property "version", project.version
                    filesMatching("META-INF/mods.toml") {
                        expand "version": project.version
                    }
                }
                
                shadowJar {
                    exclude "fabric.mod.json"
                    
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

        if (acl.getFgv() == null) {
            throw new RuntimeException("this forge version is null");
        }
        dependencies.add("forge", "net.minecraftforge:forge:" + acl.getFgv());
        PublishingExtension publishing = target.getExtensions().getByType(PublishingExtension.class);
        RepositoryHandler repositories = publishing.getRepositories();
        repositories.maven(mvn -> {
            mvn.setUrl(target.getRootProject().file("rootmaven"));
        });
        PublicationContainer publications = publishing.getPublications();
        MavenPublication mavenCommon = publications.create("mavenForge", MavenPublication.class);
        mavenCommon.setArtifactId(target.getName() + "-" + acl.getMcversion());
        mavenCommon.from(target.getComponents().getByName("java"));
    }
}
