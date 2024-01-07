package io.github.xenfork.acl.projects;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import dev.architectury.plugin.ArchitecturyPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.compile.CompileOptions;
import org.gradle.api.tasks.compile.JavaCompile;

public class AllProjects implements Plugin<Project> {
    public static ArchitecturyPlugin architecturyPlugin;
    public static JavaPlugin javaPlugin;
    public static MavenPublishPlugin mavenPublishPlugin;
    public static ShadowPlugin shadowPlugin;
    @Override
    public void apply(Project target) {

        target.allprojects(action -> {
            action.getLogger().lifecycle("load project" + action.getName());
            PluginContainer plugins = action.getPlugins();
            javaPlugin = plugins.apply(JavaPlugin.class);
            mavenPublishPlugin = plugins.apply(MavenPublishPlugin.class);
            architecturyPlugin = plugins.apply(ArchitecturyPlugin.class);
            shadowPlugin = plugins.apply(ShadowPlugin.class);
            RepositoryHandler repositories = action.getRepositories();
            repositories.maven(mvn -> {
                mvn.setUrl("https://maven.parchmentmc.org");
                mvn.setName("ParchmentMc Mapping");
            });
            TaskCollection<JavaCompile> javaCompiles = action.getTasks().withType(JavaCompile.class);
            javaCompiles.configureEach(it -> {
                CompileOptions options = it.getOptions();
                options.setEncoding("UTF-8");
                options.getRelease().set(17);
            });
            JavaPluginExtension javaPluginExtension = action.getExtensions().getByType(JavaPluginExtension.class);
            javaPluginExtension.withSourcesJar();
        });
    }
}
