package io.github.xenfork.acl.projects.sub;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import dev.architectury.plugin.ArchitectPluginExtension;
import io.github.xenfork.acl.projects.SubProjects;
import io.github.xenfork.acl.settings.MainSettings;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.configuration.ide.RunConfigSettings;
import net.fabricmc.loom.util.Constants;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;
import java.util.concurrent.Callable;

import static io.github.xenfork.acl.settings.MainSettings.acl;

public class Fabric extends Basic {


    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        architecturyDepends();
        flvDepends();
        target.getPlugins().apply(ShadowPlugin.class);

        Project project = target.findProject(":" + archivesBaseName + "-common");
        if (project != null) {
            LoomGradleExtensionAPI common_loom = (LoomGradleExtensionAPI) project.getExtensions().getByName("loom");
            if (common_loom.getAccessWidenerPath().isPresent()) {
                loom.getAccessWidenerPath().set(common_loom.getAccessWidenerPath().get());
            }
            for (RunConfigSettings runConfig : loom.getRunConfigs()) {
                runConfig.runDir("../common/run/fabric");
            }

        }

        mainSourceSet.resources(act -> {
            Set<File> srcDirs = act.getSrcDirs();
            srcDirs.add(target.getRootProject().file(archivesBaseName + "/common/src/fabric/resources"));
            srcDirs.remove(target.file("src/main/resources"));
            act.setSrcDirs(srcDirs);
        });
        mainSourceSet.java(act -> {
            Set<File> srcDirs = act.getSrcDirs();
            srcDirs.add(target.getRootProject().file(archivesBaseName + "/common/src/fabric/java"));
            srcDirs.remove(target.file("src/main.java"));
            act.setSrcDirs(srcDirs);
        });
        ArchitectPluginExtension architectury = target.getExtensions().getByType(ArchitectPluginExtension.class);
        architectury.platformSetupLoomIde();
        architectury.fabric();

//        Configuration common = target.getConfigurations().getByName("common");
//        Configuration shadowCommon = target.getConfigurations().getByName("shadowCommon");
//        target.getConfigurations().getByName("compileClasspath").extendsFrom(common);
//        target.getConfigurations().getByName("runtimeClasspath").extendsFrom(common);
//        target.getConfigurations().getByName("developmentFabric").extendsFrom(common);
//        Project namedElements = target.project(":" + archivesBaseName + "-common", act -> {
//            act.getConfigurations().getByName("namedElements");
//
//        });
//
//        dependencies.add("common", namedElements);
//        dependencies.add("shadowCommon", target.project(":" + archivesBaseName + "-common", act -> {
//            act.getConfigurations().getByName("transformProductionFabric");
//        }));

        if (!acl.getFapi().isEmpty()) {
            dependencies.add("modApi", "net.fabricmc.fabric-api:fabric-api:" + acl.getFapi());
        }
    }
}
