package io.github.xenfork.acl.projects;

import io.github.xenfork.acl.mappings.Mojang;
import io.github.xenfork.acl.mappings.Type;
import io.github.xenfork.acl.mappings.Yarn;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

public class Main implements Plugin<Project> {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Override
    public void apply(@NotNull Project target) {
        AclExtensions acl = target.getExtensions().create("acl", AclExtensions.class);
        target.afterEvaluate(project -> {
            String projects = (String) project.getProperties().get("sts.projects");
            init(acl, project);
            for (String name : projects.split(",")) {
                Project common = project.findProject(":" + name + "-common");

                Project fabric = project.findProject(":" + name + "-fabric");
                Project forge = project.findProject(":" + name + "-forge");
            }
        });
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

    private static void init(AclExtensions acl, Project project) {
        if (acl.getMcversion() == null) {
            if (project.getProperties().containsKey("acl.mcversion")) 
                acl.setMcversion(String.valueOf(project.getProperties().get("acl.mcversion")));

            else
                throw new RuntimeException("don't set minecraft version");
        }
        if (acl.getProject$name() == null) {
            acl.setProject$name(project.getName());
        }
        if (acl.getGroup() == null) {
            acl.setGroup("io.github.xenfork");
        }
        Type mappings = acl.getMappings();
        String srg = acl.getSrg();
        if (mappings instanceof Mojang) {

            if (!srg.isEmpty()) {
                if (srg.contains(":")) {
                    AclExtensions.srg_out = "org.parchmentmc.data:parchment-%s@zip".formatted(srg);
                } else {
                    AclExtensions.srg_out = "org.parchmentmc.data.parchment-%s:%s@zip".formatted(acl.getMcversion(), srg);
                }
            }
        }
        else if (mappings instanceof Yarn) {
            if (srg.isEmpty()) {
                throw new RuntimeException("this yarn mapping is null");
            }
            if (srg.contains(":")) {
                String[] split = srg.split(":", 2);
                AclExtensions.srg_out = "net.fabricmc:yarn:%s+build.%s".formatted(split[0], split[1]);
            } else {
                AclExtensions.srg_out = "net.fabricmc:yarn:%s+build.%s".formatted(acl.getMcversion(), srg);
            }
        }
    }
}