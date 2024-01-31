package io.github.xenfork.acl.projects.sub;

import cn.hutool.core.io.FileUtil;
import dev.architectury.plugin.ArchitectPluginExtension;
import dev.architectury.plugin.ModLoader;
import kotlin.Unit;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.xenfork.acl.settings.MainSettings.acl;
import static io.github.xenfork.acl.settings.MainSettings.sts;

/**
 * @author baka4n
 */
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

        String script = """
                sourceSets {
                    main {
                        resources {
                            srcDirs += file("src/main/generated/resources").absolutePath
                            exclude ".cache"
                        }
                    }
                }
                """;
        FileUtil.touch(allScript);
        BufferedWriter writer = FileUtil.getWriter(allScript, StandardCharsets.UTF_8, false);
        try {
            writer.write(script);
            writer.close();
        } catch (IOException ignored) {}
        target.apply(action -> {
            action.from(allScript);
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
