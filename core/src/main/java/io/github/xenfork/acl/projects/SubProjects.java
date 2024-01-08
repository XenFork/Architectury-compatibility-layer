package io.github.xenfork.acl.projects;

import groovy.lang.Closure;
import io.github.xenfork.acl.mappings.Mojang;
import net.fabricmc.loom.LoomGradlePlugin;
import net.fabricmc.loom.api.mappings.layered.spec.LayeredMappingSpecBuilder;
import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap;
import net.fabricmc.loom.configuration.providers.mappings.parchment.ParchmentMappingLayer;
import net.fabricmc.loom.configuration.providers.mappings.parchment.ParchmentMappingsSpecBuilderImpl;
import net.fabricmc.loom.extension.LoomGradleExtensionImpl;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.jetbrains.annotations.NotNull;

public class SubProjects implements Plugin<Project> {
    public static LoomGradlePluginBootstrap loomGradlePluginBootstrap;
    @Override
    public void apply(@NotNull Project target) {
        target.subprojects(action -> {
            action.afterEvaluate(project -> {
                loomGradlePluginBootstrap = project.getPlugins().apply(LoomGradlePluginBootstrap.class);
                LoomGradleExtensionImpl loom = project.getExtensions().getByType(LoomGradleExtensionImpl.class);

                loom.silentMojangMappingsLicense();
                DependencyHandler dependencies = project.getDependencies();
                dependencies.add("minecraft",  "com.mojang:minecraft:" + Main.acl.getMcversion());
                if (Main.acl.getMappings() instanceof Mojang) {
                    if (AclExtensions.srg_out.isEmpty()) {
                        dependencies.add("mappings", loom.layered(LayeredMappingSpecBuilder::officialMojangMappings));
                    } {
                        dependencies.add("mappings", loom.layered(layered -> layered.parchment(AclExtensions.srg_out)));
                    }
                }
            });
        });
    }
}
