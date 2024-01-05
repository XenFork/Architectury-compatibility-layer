package io.github.xenfork.acl.projects;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

public class Main implements Plugin<Project> {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Override
    public void apply(@NotNull Project target) {
        PropertiesSet set = target.getExtensions().create("acl", PropertiesSet.class);

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

    private static void init(PropertiesSet set, Project project) {
        if (set.getMcversion() == null) {
            throw new RuntimeException("don't set minecraft version");
        }
        if (set.getProject$name() == null) {
            set.setProject$name(project.getName());
        }
        if (set.getGroup() == null) {
            set.setGroup("io.github.xenfork");
        }
    }
}