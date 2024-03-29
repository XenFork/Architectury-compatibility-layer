package io.github.xenfork.acl.projects.sub;

import cn.hutool.core.io.FileUtil;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.util.gradle.SourceSetHelper;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.tasks.SourceSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import static io.github.xenfork.acl.settings.MainSettings.acl;

/**
 * @author baka4n
 */
public class Basic implements Plugin<Project> {

    /**
     * @since  archivesBaseName
     */
    public String archivesBaseName;
    /**
     * loader
     */
    public String loader;
    /**
     * @since dependencies
     */
    public DependencyHandler dependencies;
    /**
     * @since loom extension
     */
    public static LoomGradleExtensionAPI loom;
    /**
     * @since  source main
     */
    public static SourceSet mainSourceSet;

    /**
     * @since  all script
     */
    public File allScript;

    /**
     * @param target project
     */
    public void init(@Nullable Project target) {
        if (target != null) {
            apply(target);
        }
    }

    @Override
    public void apply(@NotNull Project target) {
        allScript = new File(target.getBuildDir(), "allscript.gradle");
        archivesBaseName = target.getName().split("-")[0];
        loader = target.getName().split("-")[1];
        dependencies = target.getDependencies();
        loom = (LoomGradleExtensionAPI) target.getExtensions().getByName("loom");
        mainSourceSet = SourceSetHelper.getMainSourceSet(target);
        target.apply(act -> {
            act.from(FileUtil.touch(target.getRootProject().file(archivesBaseName + "/common/" + loader + ".gradle")).getAbsolutePath());
        });

    }

    final void architecturyCommonDepends() {
        dependencies.add("modApi", "dev.architectury:architectury:" + acl.getArchitectury$version());
    }

    final void architecturyDepends() {
        if (!loader.equals("common")) {
            dependencies.add("modApi", "dev.architectury:architectury-" + loader + ":" + acl.getArchitectury$version());
        }
    }

    final void flvDepends() {
        if (!acl.getFlv().isEmpty()) {
            dependencies.add("modImplementation", "net.fabricmc:fabric-loader:" + acl.getFlv());
        }
    }
}
