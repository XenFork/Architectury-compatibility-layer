package io.github.xenfork.acl.projects;

import io.github.xenfork.acl.mappings.Mojang;
import io.github.xenfork.acl.settings.MainSettings;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap;
import net.fabricmc.loom.util.Constants;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SubProjects implements Plugin<Project> {

    public static LoomGradleExtensionAPI loom;
    public static ExtraPropertiesExtension extraProperties;

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    @Override
    public void apply(@NotNull Project target) {
        target.subprojects(action -> {
            action.getPlugins().apply(LoomGradlePluginBootstrap.class);
            String name = action.getName();
            String[] split = name.split("-", 2);
            action.setBuildDir(action.getRootProject().getBuildDir().toPath().resolve(split[0]).resolve(split[1]).toFile());
            extraProperties = action.getExtensions().getExtraProperties();
            String name1 = split[0] + "_version";
            if (extraProperties.has(name1))
                action.setVersion(Objects.requireNonNull(extraProperties.get(name1)));
            else
                action.setVersion("1.0.0.0");

            loom = (LoomGradleExtensionAPI) action.getExtensions().findByName("loom");
            if (loom != null) {
                loom.silentMojangMappingsLicense();
                DependencyHandler dependencies = action.getDependencies();
                dependencies.add(Constants.Configurations.MINECRAFT, "com.mojang:minecraft:" + MainSettings.acl.getMcversion());
                if (MainSettings.acl.getMappings() instanceof Mojang) {

                    if (MainSettings.acl.srg_out.isEmpty()) {
                        dependencies.add(Constants.Configurations.MAPPINGS, loom.officialMojangMappings());
                    }
                    {
                        dependencies.add(Constants.Configurations.MAPPINGS, loom.layered(layered -> {
                            layered.officialMojangMappings();
                            layered.parchment(MainSettings.acl.srg_out);
                        }));
                    }
                }
            }

        });
    }
}
