package io.github.xenfork.acl.projects.sub;

import io.github.xenfork.acl.projects.Main;
import io.github.xenfork.acl.settings.MainSettings;
import net.fabricmc.loom.LoomGradleExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.SourceSetContainer;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static io.github.xenfork.acl.settings.MainSettings.acl;

public class Common extends Basic {
    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        DependencyHandler dependencies = target.getDependencies();

        if (!acl.getArchitectury$version().isEmpty()) {

        }
    }
}
