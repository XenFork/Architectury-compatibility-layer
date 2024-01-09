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
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class Main implements Plugin<Project> {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
    public static AclExtensions acl;

    @Override
    public void apply(@NotNull Project target) {
        acl = target.getExtensions().create("acl", AclExtensions.class);
        target.getPlugins().apply(ArchitecturyPlugin.class);
        System.out.println(MainSettings.acl.getSrg());
        System.out.println(MainSettings.acl.getMcversion());
        init(MainSettings.acl, target);
        ArchitectPluginExtension architectury = target.getExtensions().getByType(ArchitectPluginExtension.class);
        architectury.setMinecraft(MainSettings.acl.getMcversion());
        target.getPlugins().apply(AllProjects.class);
        target.getPlugins().apply(SubProjects.class);

        findProject(target);

//        SourceSetContainer sourceSets = target.getExtensions().getByType(SourceSetContainer.class);
//        Project common = target.getRootProject().getSubprojects().stream().filter(p -> p.getName().equals("common")).toList().get(0);
//        Project fabric = target.getRootProject().getSubprojects().stream().filter(p -> p.getName().equals("fabric")).toList().get(0);
//        Project forge = target.getRootProject().getSubprojects().stream().filter(p -> p.getName().equals("forge")).toList().get(0);
//        SourceSetContainer commonSourceSet = common.getExtensions().getByType(SourceSetContainer.class);
//        SourceSetContainer fabricSourceSet = fabric.getExtensions().getByType(SourceSetContainer.class);
//        SourceSetContainer forgeSourceSet = forge.getExtensions().getByType(SourceSetContainer.class);
//        target.afterEvaluate(project -> {
//            init(set, project);// init acl extensions
//            File commonTagFile = FileUtils.getFile(project.getBuildFile(), "generated", "sources", "commonTags");
//            File fabricTagFile = FileUtils.getFile(project.getBuildFile(), "generated", "sources", "fabricTags");
//            File forgeTagFile = FileUtils.getFile(project.getBuildFile(), "generated", "sources", "forgeTags");
//            TaskProvider<TagTask> genTags = project.getTasks().register("commonTags", TagTask.class, task -> {
//                task.getOutputDir().set(commonTagFile);
//            });
//            TaskProvider<FabricTask> fabricTags = project.getTasks().register("fabricTags", FabricTask.class, task -> {
//                task.getOutputDir().set(fabricTagFile);
//            });
//            TaskProvider<ForgeTask> forgeTags = project.getTasks().register("forgeTags", ForgeTask.class, task -> {
//                task.getOutputDir().set(forgeTagFile);
//            });
//
//            SourceSet tagsCommon = commonSourceSet.create("commonTags", sourceSet -> {
//                sourceSet.getJava().setSrcDirs(project.files(commonTagFile).builtBy(genTags));
//            });
//            SourceSet tagsFabric = fabricSourceSet.create("fabricTags", sourceSet -> {
//                sourceSet.getJava().setSrcDirs(project.files(fabricTagFile).builtBy(fabricTags));
//            });
//
//            SourceSet tagsForge = forgeSourceSet.create("forgeTags", sourceSet -> {
//                sourceSet.getJava().setSrcDirs(project.files(fabricTagFile).builtBy(forgeTags));
//            });
//            common.getTasks().named(tagsCommon.getCompileJavaTaskName()).configure(task -> task.dependsOn(genTags));
//            fabric.getTasks().named(tagsFabric.getCompileJavaTaskName()).configure(task -> task.dependsOn(fabricTags));
//            forge.getTasks().named(tagsForge.getCompileJavaTaskName()).configure(task -> task.dependsOn(forgeTags));
//
//
//        });
    }

    public static void findProject(Project target) {
        String projects = (String) target.getProperties().get("sts.projects");
        if (!projects.isEmpty()) {
            for (String name : projects.split(",")) {
                Project common = target.findProject(":" + name + "-common");
                if (common != null)
                    new Common().apply(common);
                Project fabric = target.findProject(":" + name + "-fabric");
                if (fabric != null)
                    new Fabric().apply(fabric);
                Project forge = target.findProject(":" + name + "-forge");
                if (forge != null) {
                    new Forge().apply(forge);
                }
                Project quilt = target.findProject(":" + name + "-quilt");
                if (quilt != null) {
                    new Quilt().apply(quilt);
                }
                Project neoforge = target.findProject(":" + name + "-neoforge");
                if (neoforge != null) {
                    new NeoForge().apply(neoforge);
                }
            }
        }
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