package io.github.xenfork.acl.projects;

import cn.hutool.core.io.FileUtil;
import io.github.xenfork.acl.settings.MainSettings;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.compile.CompileOptions;
import org.gradle.api.tasks.compile.JavaCompile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AllProjects implements Plugin<Project> {

    public static RepositoryHandler repositories;
    public static TaskCollection<JavaCompile> javac;
    public static JavaPluginExtension java;
    @Override
    public void apply(Project target) {

        target.allprojects(action -> {
            action.getLogger().lifecycle("load project " + action.getName());
            PluginContainer plugins = action.getPlugins();
            plugins.apply("java");
            plugins.apply("maven-publish");
            plugins.apply("architectury-plugin");
            plugins.apply("com.github.johnrengelman.shadow");
            repositories = action.getRepositories();
            repositories.maven(mvn -> {
                mvn.setUrl("https://maven.parchmentmc.org");
                mvn.setName("ParchmentMc Mapping");
            });
            target.setGroup(MainSettings.acl.getGroup());
            target.getExtensions().getExtraProperties().set("archivesBaseName", target.getName().split("-")[0]);

            String script = """
                    tasks.withType(JavaCompile).configureEach {
                        options.encoding = "UTF-8"
                        options.release.set(%d)
                    }

                    java {
                        java.withSourcesJar()
                    }""".formatted(MainSettings.acl.j);
            File allScript = new File(action.getRootProject().getBuildDir(), "allscript.gradle");
            FileUtil.touch(allScript);
            BufferedWriter writer = FileUtil.getWriter(allScript, StandardCharsets.UTF_8, false);
            try {
                writer.write(script);
                writer.close();
            } catch (IOException ignored) {}
            action.apply(act -> {
                act.from(allScript);
            });

        });
    }
}
