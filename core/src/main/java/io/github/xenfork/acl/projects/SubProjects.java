package io.github.xenfork.acl.projects;

import io.github.xenfork.acl.mappings.Mojang;
import net.fabricmc.loom.LoomGradleExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.api.mappings.layered.MappingContext;
import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap;
import net.fabricmc.loom.configuration.DependencyInfo;
import net.fabricmc.loom.configuration.LoomDependencyManager;
import net.fabricmc.loom.configuration.providers.mappings.GradleMappingContext;
import net.fabricmc.loom.configuration.providers.mappings.LayeredMappingsDependency;
import net.fabricmc.loom.configuration.providers.minecraft.MinecraftSourceSets;
import net.fabricmc.loom.util.Constants;
import net.fabricmc.loom.util.service.SharedServiceManager;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.GradleDependencies;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SubProjects implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project target) {
        target.subprojects(action -> {
            action.getPlugins().apply(LoomGradlePluginBootstrap.class);
            String name = action.getName();
            String[] split = name.split("-", 2);
            action.setBuildDir(action.getRootProject().getBuildDir().toPath().resolve(split[0]).resolve(split[1]).toFile());
            ExtraPropertiesExtension extraProperties = action.getExtensions().getExtraProperties();
            String name1 = split[0] + "_version";
            if (extraProperties.has(name1))
                action.setVersion(Objects.requireNonNull(extraProperties.get(name1)));
            else
                action.setVersion("1.0.0.0");

            action.afterEvaluate(project -> {
//                LoomGradleExtensionAPI loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);
                LoomGradleExtension loom = LoomGradleExtension.get(project);
                loom.silentMojangMappingsLicense();
                DependencyHandler dependencies = project.getDependencies();
                dependencies.add(Constants.Configurations.MINECRAFT,  "com.mojang:minecraft:" + Main.acl.getMcversion());
                if (Main.acl.getMappings() instanceof Mojang) {

                    if (AclExtensions.srg_out.isEmpty()) {
                        dependencies.add(Constants.Configurations.MAPPINGS, loom.officialMojangMappings());
                    } {
                        dependencies.add(Constants.Configurations.MAPPINGS, loom.layered(layered -> {
                            layered.officialMojangMappings();
                            layered.parchment(AclExtensions.srg_out);
                        }));
                    }
                }
            });
        });
    }
}
