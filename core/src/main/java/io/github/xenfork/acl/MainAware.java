package io.github.xenfork.acl;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginAware;
import org.jetbrains.annotations.NotNull;

public class MainAware implements Plugin<PluginAware> {
    @Override
    public void apply(@NotNull PluginAware target) {
       if (target instanceof Settings settings) {
           new MainSettings().apply(settings);
       } else if (target instanceof Project) {
           target.getPlugins().apply("java");
           target.getPlugins().apply("maven-publish");
       }
    }
}
