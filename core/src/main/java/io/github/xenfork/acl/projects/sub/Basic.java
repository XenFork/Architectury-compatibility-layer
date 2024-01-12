package io.github.xenfork.acl.projects.sub;

import cn.hutool.core.io.FileUtil;
import io.github.xenfork.acl.mappings.Mojang;
import io.github.xenfork.acl.projects.AclExtensions;
import io.github.xenfork.acl.settings.MainSettings;
import net.fabricmc.loom.util.Constants;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static io.github.xenfork.acl.projects.SubProjects.loom;
import static io.github.xenfork.acl.settings.MainSettings.acl;

public class Basic implements Plugin<Project> {
    public String archivesBaseName, loader;
    public DependencyHandler dependencies;
    @Override
    public void apply(@NotNull Project target) {
        archivesBaseName = target.getName().split("-")[0];
        loader = target.getName().split("-")[1];
//        target.setBuildDir(target.getRootProject().file("build/" + archivesBaseName + "/" + loader));
        File file = target.getRootProject().file(archivesBaseName + "/common/" + loader + ".gradle");
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists())
                parentFile.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        target.apply(act -> {
            act.from(file.getAbsolutePath());
        });
        dependencies = target.getDependencies();
        flvDepends();
        architecturyDepends();
    }

    private void architecturyDepends() {
        if (loader.equals("common")) {
            dependencies.add("modApi", "dev.architectury:architectury:" + acl.getArchitectury$version());
        } else {
            dependencies.add("modApi", "dev.architectury:architectury-" + loader + ":" + acl.getArchitectury$version());
        }
    }

    private void flvDepends() {
        if (loader.equals("common") || loader.equals("fabric")) {
            if (!acl.getFlv().isEmpty()) {
                dependencies.add("modImplementation", "net.fabricmc:fabric-loader:" + acl.getFlv());
            }
        }

    }
}
