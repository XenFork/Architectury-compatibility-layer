package io.github.xenfork.acl.settings;

import org.gradle.api.Action;
import org.gradle.api.Plugin;

import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

public class MainSettings implements Plugin<Settings> {




    @Override
    public void apply(@NotNull Settings target) {
        RepositoryHandler repositories = target.getPluginManagement().getRepositories();
        repositories.maven(mvn -> {
            mvn.setUrl("https://maven.fabricmc.net/");
            mvn.setName("fabric maven");
        });
        repositories.maven(mvn -> {
            mvn.setUrl("https://maven.architectury.dev/");
            mvn.setName("architectury maven");
        });
        repositories.maven(mvn -> {
            mvn.setUrl("https://maven.minecraftforge.net/");
            mvn.setName("forge maven");
        });
        repositories.gradlePluginPortal();

        AclExtensions sts = target.getExtensions().create("sts", AclExtensions.class);
        Properties properties = new Properties();
        try {
            properties.load(new BufferedReader(new FileReader(new File(target.getRootProject().getProjectDir(), "gradle.properties"))));
            sts.projects = properties.getProperty("sts.projects");
        } catch (IOException ignored) {}

        if (sts.projects != null) {
            System.out.println("test");
            for (String project : sts.getProjects().split(",")) {
                String common = project + "-common";
                String fabric = project + "-fabric";
                String forge = project + "-forge";
                target.include(common, fabric, forge);
                target.project(":" + common).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/common").toFile());
                target.project(":" + fabric).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/fabric").toFile());
                target.project(":" + forge).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/forge").toFile());

//                target.include(project + "/common", project + "/fabric", project + "/forge");
            }
        }

    }
}