package io.github.xenfork.acl;

import io.github.xenfork.acl.tasks.FabricTask;
import io.github.xenfork.acl.tasks.ForgeTask;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.initialization.IncludedBuild;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Main implements Plugin<Project> {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Override
    public void apply(@NotNull Project target) {
        PropertiesSet set = target.getExtensions().create("acl", PropertiesSet.class);
        SourceSetContainer sourceSets = target.getExtensions().getByType(SourceSetContainer.class);
        target.afterEvaluate(project -> {
            init(set, project);// init acl extensions
            File commonTagFile = FileUtils.getFile(project.getBuildFile(), "generated", "sources", "commonTags");
            File fabricTagFile = FileUtils.getFile(project.getBuildFile(), "generated", "sources", "fabricTags");
            File forgeTagFile = FileUtils.getFile(project.getBuildFile(), "generated", "sources", "forgeTags");
            TaskProvider<TagTask> genTags = project.getTasks().register("commonTags", TagTask.class, task -> {
                task.getOutputDir().set(commonTagFile);
            });
            TaskProvider<FabricTask> fabricTags = project.getTasks().register("fabricTags", FabricTask.class, task -> {
                task.getOutputDir().set(fabricTagFile);
            });
            TaskProvider<ForgeTask> forgeTags = project.getTasks().register("forgeTags", ForgeTask.class, task -> {
                task.getOutputDir().set(forgeTagFile);
            });

            SourceSet tagsCommon = sourceSets.create("commonTags", sourceSet -> {
                sourceSet.getJava().setSrcDirs(project.files(commonTagFile).builtBy(genTags));
            });
            SourceSet tagsFabric = sourceSets.create("fabricTags", sourceSet -> {
                sourceSet.getJava().setSrcDirs(project.files(fabricTagFile).builtBy(fabricTags));
            });

            SourceSet tagsForge = sourceSets.create("forgeTags", sourceSet -> {
                sourceSet.getJava().setSrcDirs(project.files(fabricTagFile).builtBy(forgeTags));
            });
            project.getTasks().named(tagsCommon.getCompileJavaTaskName()).configure(task -> task.dependsOn(genTags));
            project.getTasks().named(tagsFabric.getCompileJavaTaskName()).configure(task -> task.dependsOn(fabricTags));
            project.getTasks().named(tagsForge.getCompileJavaTaskName()).configure(task -> task.dependsOn(forgeTags));

        });
    }

    private static void init(PropertiesSet set, Project project) {
        if (set.getMc_version() == null) {
            throw new RuntimeException("don't set minecraft version");
        }
        if (set.getProject_name() == null) {
            set.setProject_name(project.getName());
        }
        if (set.getGroup() == null) {
            set.setGroup("io.github.xenfork");
        }
    }
}