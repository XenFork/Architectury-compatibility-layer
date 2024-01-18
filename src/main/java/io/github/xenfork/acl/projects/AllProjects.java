package io.github.xenfork.acl.projects;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import dev.architectury.plugin.ArchitecturyPlugin;
import io.github.xenfork.acl.settings.MainSettings;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.plugins.internal.DefaultJavaPluginExtension;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.compile.CompileOptions;
import org.gradle.api.tasks.compile.JavaCompile;

import static io.github.xenfork.acl.projects.Main.acl;

public class AllProjects implements Plugin<Project> {

    public static RepositoryHandler repositories;
    public static TaskCollection<JavaCompile> javac;
    public static JavaPluginExtension java;
    @Override
    public void apply(Project target) {

        target.allprojects(action -> {
            action.getLogger().lifecycle("load project " + action.getName());
            PluginContainer plugins = action.getPlugins();
            plugins.apply(JavaPlugin.class);
            plugins.apply(MavenPublishPlugin.class);
            plugins.apply(ArchitecturyPlugin.class);
            plugins.apply(ShadowPlugin.class);
            repositories = action.getRepositories();
            repositories.maven(mvn -> {
                mvn.setUrl("https://maven.parchmentmc.org");
                mvn.setName("ParchmentMc Mapping");
            });
            target.setGroup(MainSettings.acl.getGroup());
            target.getExtensions().getExtraProperties().set("archivesBaseName", target.getName().split("-")[0]);
            javac = target.getTasks().withType(JavaCompile.class);
            javac.configureEach(it -> {
                CompileOptions options = it.getOptions();
                options.setEncoding("UTF-8");
                options.getRelease().set(17);
            });
            java = target.getExtensions().getByType(JavaPluginExtension.class);
            java.withSourcesJar();
        });
    }
}
