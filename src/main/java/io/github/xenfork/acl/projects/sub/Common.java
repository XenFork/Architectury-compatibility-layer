package io.github.xenfork.acl.projects.sub;

import dev.architectury.plugin.ArchitectPluginExtension;
import dev.architectury.plugin.ModLoader;
import io.github.xenfork.acl.projects.Main;
import io.github.xenfork.acl.settings.MainSettings;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.reflect.jvm.internal.impl.storage.MemoizedFunctionToNotNull;
import net.fabricmc.loom.LoomGradleExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.util.ModPlatform;
import net.fabricmc.loom.util.gradle.SourceSetHelper;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.tasks.DefaultSourceSetContainer;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.xenfork.acl.settings.MainSettings.acl;
import static io.github.xenfork.acl.settings.MainSettings.sts;

public class Common extends Basic {


    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        architecturyCommonDepends();
        flvDepends();
        ArchitectPluginExtension architectury = (ArchitectPluginExtension) target.getExtensions().getByName("architectury");
        File accesswidener = target.file("src/main/resources/" + archivesBaseName + ".accesswidener");
        if (accesswidener.exists()) {
            loom.getAccessWidenerPath().set(accesswidener);
        }

        mainSourceSet.resources(action -> {
            Set<File> srcDirs = action.getSrcDirs();
            srcDirs.add(target.file("src/main/generated/resources"));
            action.setSrcDirs(srcDirs);
            action.exclude(".cache");
        });
        PublishingExtension publishing = target.getExtensions().getByType(PublishingExtension.class);
        RepositoryHandler repositories = publishing.getRepositories();
        repositories.maven(mvn -> {
            mvn.setUrl(target.getRootProject().file("rootmaven"));
        });
        PublicationContainer publications = publishing.getPublications();
        MavenPublication mavenCommon = publications.create("mavenCommon", MavenPublication.class);
        mavenCommon.setArtifactId(target.getName() + "-" + acl.getMcversion());
        mavenCommon.from(target.getComponents().getByName("java"));

        String[] split = sts.getPlatform().split(",", 4);
        List<String> platform = new ArrayList<>();
        AtomicBoolean hasNeo = new AtomicBoolean();
        for (String s : split) {
            switch (s) {
                case "forge", "fabric" -> {
                    platform.add(s);
                }
                case "neoforge" -> {
                    hasNeo.set(true);
                }
            }

        }
        architectury.common(platform, commonSettings -> {
            if (hasNeo.get()) {
                commonSettings.platformPackage(ModLoader.Companion.getNEOFORGE(), ModLoader.Companion.getFORGE().getId());
            }
            return Unit.INSTANCE;
        });
    }
}
