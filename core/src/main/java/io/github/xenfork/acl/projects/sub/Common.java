package io.github.xenfork.acl.projects.sub;

import io.github.xenfork.acl.projects.Main;
import net.fabricmc.loom.LoomGradleExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.SourceSetContainer;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Common extends Basic {
    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
//        target.afterEvaluate(project -> {
//            SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);
//            SourceDirectorySet main = sourceSets.getByName("main").getResources();
//            main.getSrcDirs().add(project.file("src/main/generated/resources"));
//            main.exclude(".cache");
//            ExtensionContainer extensions = project.getExtensions();
//            LoomGradleExtensionAPI loom = extensions.getByType(LoomGradleExtensionAPI.class);
//            File file = project.file("src/main/resources/" + extensions.getExtraProperties().get("archivesBaseName") + ".accesswidener");
//            if (file.exists()) {
//                loom.getAccessWidenerPath().set(file);
//            }
//            DependencyHandler dependencies = project.getDependencies();
//            dependencies.add("modImplementation", "net.fabricmc:fabric-loader:" + Main.acl.getFlv());
//
//        });
    }
}
