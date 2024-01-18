package io.github.xenfork.acl.projects.sub;

import io.github.xenfork.acl.settings.MainSettings;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import static io.github.xenfork.acl.settings.MainSettings.acl;

public class Forge extends Basic {

    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        architecturyDepends();
        if (acl.getFgv() == null) {
            throw new RuntimeException("this forge version is null");
        }
        dependencies.add("forge", "net.minecraftforge:forge:" + acl.getFgv());
    }
}
