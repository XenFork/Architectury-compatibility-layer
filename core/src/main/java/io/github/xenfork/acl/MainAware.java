package io.github.xenfork.acl;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginAware;
import org.gradle.api.plugins.PluginContainer;
import org.jetbrains.annotations.NotNull;

public class MainAware implements Plugin<PluginAware> {
    @Override
    public void apply(@NotNull PluginAware target) {
       if (target instanceof Settings settings) {
           apply(settings.getPlugins(), MainSettings.class);
           new MainSettings().apply(settings);
       } else if (target instanceof Project project) {
           apply(
                   project.getPlugins(),
                   "java",
                   "maven-publish"
           );
           apply(project.getPlugins(), Main.class);
       }
    }

    @SafeVarargs
    public static void apply(PluginContainer plugins, Class<? extends Plugin<?>>... p) {
        for (Class<? extends Plugin<?>> aClass : p) {
            plugins.apply(aClass);
        }
    }

    public static void apply(PluginContainer plugins, String... strings) {
        for (String string : strings) {
            plugins.apply(string);
        }
    }
}
