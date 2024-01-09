package io.github.xenfork.acl.projects;

import io.github.xenfork.acl.mappings.Mojang;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.jetbrains.annotations.NotNull;

public class SubProjects implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project target) {
        target.subprojects(action -> {
            action.getPlugins().apply(LoomGradlePluginBootstrap.class);
            action.afterEvaluate(project -> {
                LoomGradleExtensionAPI loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);

                loom.silentMojangMappingsLicense();
                DependencyHandler dependencies = project.getDependencies();
                dependencies.add("minecraft",  "com.mojang:minecraft:" + Main.acl.getMcversion());
                if (Main.acl.getMappings() instanceof Mojang) {
                    if (AclExtensions.srg_out.isEmpty()) {
                        dependencies.add("mappings", loom.officialMojangMappings());
                    } {
                        dependencies.add("mappings", loom.layered(layered -> {
                            layered.officialMojangMappings();
                            layered.parchment(AclExtensions.srg_out);
                        }));
                    }
                }
            });
        });
    }
}
