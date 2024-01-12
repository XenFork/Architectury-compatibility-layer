package io.github.xenfork.acl.projects.sub;

import io.github.xenfork.acl.settings.MainSettings;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import static io.github.xenfork.acl.settings.MainSettings.acl;

public class Fabric extends Basic {
    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        if (!acl.getFapi().isEmpty()) {
            dependencies.add("modApi", "net.fabricmc.fabric-api:fabric-api:" + acl.getFapi());
        }
    }
}
