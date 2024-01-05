package io.github.xenfork.acl;

import org.gradle.api.Plugin;

import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;

public class MainSettings implements Plugin<Settings> {
    @Override
    public void apply(@NotNull Settings target) {
        target.include("common", "fabric", "forge");
    }
}
