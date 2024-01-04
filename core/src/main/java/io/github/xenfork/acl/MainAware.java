package io.github.xenfork.acl;

import org.gradle.api.Plugin;
import org.gradle.api.plugins.PluginAware;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.plugins.PluginManager;
import org.jetbrains.annotations.NotNull;

public class MainAware implements Plugin<PluginAware> {
    @Override
    public void apply(@NotNull PluginAware target) {
        PluginManager pluginManager = target.getPluginManager();
        pluginManager.apply(MainSettings.class);
        pluginManager.apply(Main.class);
    }
}
