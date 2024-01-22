package io.github.xenfork.acl.projects;

import cn.hutool.core.io.FileUtil;
import com.google.common.io.Resources;
import dev.architectury.plugin.ArchitectPluginExtension;
import dev.architectury.plugin.ArchitecturyPlugin;
import dev.architectury.plugin.ArchitecturyPluginExtensionKt;
import io.github.xenfork.acl.mappings.Mojang;
import io.github.xenfork.acl.mappings.Type;
import io.github.xenfork.acl.mappings.Yarn;
import io.github.xenfork.acl.projects.sub.*;
import io.github.xenfork.acl.settings.MainSettings;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main implements Plugin<Project> {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
    public static AclExtensions acl;
    public static ArchitectPluginExtension architectury;

    @Override
    public void apply(@NotNull Project target) {
        target.getRootProject().setBuildDir(target.getRootProject().file(".gradle/build"));
        acl = target.getExtensions().create("acl", AclExtensions.class);
        target.getPlugins().apply(ArchitecturyPlugin.class);
        init(MainSettings.acl, target);
        architectury = target.getExtensions().getByType(ArchitectPluginExtension.class);
        architectury.setMinecraft(MainSettings.acl.getMcversion());
        target.getPlugins().apply(AllProjects.class);
        target.getPlugins().apply(SubProjects.class);
        findProject(target);
        target.afterEvaluate(project -> {
            for (String p : MainSettings.sts.getProjects().split(",")) {
                Task task = project.task(p + "_publish");
                String platform = MainSettings.sts.getPlatform();
                task.dependsOn(project.project(":" + p + "-common").getTasks().getByName("publish"));
                if (platform != null) {
                    for (String plat : platform.split(",")) {
                        task.dependsOn(project.project(":%s-%s".formatted(p, plat)).getTasks().getByName("publish"));
                    }
                } else {
                    task.dependsOn(project.project(":" + p + "-fabric").getTasks().getByName("publish"));
                    task.dependsOn(project.project(":" + p + "-forge").getTasks().getByName("publish"));
                }
                task.setGroup("acl");
            }
        });
    }
    public static void findProject(@NotNull Project target) {
        String projects = (String) target.getProperties().get("sts.projects");
        if (!projects.isEmpty()) {
            for (String name : projects.split(",")) {
                initSubProjectSettings(name, target);
            }
        }
    }

    private static void initSubProjectSettings(String name, @NotNull Project target) {
        new Common().init(target.findProject(":" + name + "-common"));
        new Fabric().init(target.findProject(":" + name + "-fabric"));
        new Forge().init(target.findProject(":" + name + "-forge"));
        new Quilt().init(target.findProject(":" + name + "-quilt"));
        new NeoForge().init(target.findProject(":" + name + "-neoforge"));
    }

    private static void init(AclExtensions acl, Project project) {
        if (MainSettings.acl.getMcversion() == null) {
            throw new RuntimeException("don't set minecraft version, can use acl.mcversion=\"1.20.1\" or other version");
        }
        if (MainSettings.acl.getFlv() == null) {
            throw new RuntimeException("don't set fabric loader version, can use acl.flv=\"flv_version\"");
        }
        if (MainSettings.acl.getProject$name() == null) {
            MainSettings.acl.setProject$name(project.getName());
        }
        if (MainSettings.acl.getGroup() == null) {
            MainSettings.acl.setGroup("io.github.xenfork");
        }
        Type mappings = MainSettings.acl.getMappings();
        String srg = MainSettings.acl.getSrg();
        if (mappings instanceof Mojang) {
            if (!srg.isEmpty()) {
                if (srg.contains(":")) {
                    MainSettings.acl.srg_out = "org.parchmentmc.data:parchment-%s@zip".formatted(srg);
                } else {
                    MainSettings.acl.srg_out = "org.parchmentmc.data:parchment-" + MainSettings.acl.getMcversion() + ":" + srg + "@zip";
                }
            }
        }
        else if (mappings instanceof Yarn) {
            if (srg.isEmpty()) {
                throw new RuntimeException("this yarn mapping is null");
            }
            if (srg.contains(":")) {
                String[] split = srg.split(":", 2);
                MainSettings.acl.srg_out = "net.fabricmc:yarn:%s+build.%s".formatted(split[0], split[1]);
            } else {
                MainSettings.acl.srg_out = "net.fabricmc:yarn:%s+build.%s".formatted(acl.getMcversion(), srg);
            }
        }
    }
}