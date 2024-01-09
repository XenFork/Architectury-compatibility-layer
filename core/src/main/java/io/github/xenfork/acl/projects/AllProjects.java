package io.github.xenfork.acl.projects;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import dev.architectury.plugin.ArchitecturyPlugin;
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
    @Override
    public void apply(Project target) {

        target.allprojects(action -> {
            action.getLogger().lifecycle("load project " + action.getName());
            PluginContainer plugins = action.getPlugins();
            plugins.apply(JavaPlugin.class);
            plugins.apply(MavenPublishPlugin.class);
            plugins.apply(ArchitecturyPlugin.class);
            plugins.apply(ShadowPlugin.class);
            RepositoryHandler repositories = action.getRepositories();
            repositories.maven(mvn -> {
                mvn.setUrl("https://maven.parchmentmc.org");
                mvn.setName("ParchmentMc Mapping");
            });

            action.afterEvaluate(project -> {
                project.setGroup(acl.getGroup());
                project.getExtensions().getExtraProperties().set("archivesBaseName", project.getName().split("-")[0]);
                TaskCollection<JavaCompile> javaCompiles = project.getTasks().withType(JavaCompile.class);
                javaCompiles.configureEach(it -> {
                    CompileOptions options = it.getOptions();
                    options.setEncoding("UTF-8");
                    options.getRelease().set(17);
                });
                JavaPluginExtension javaPluginExtension = project.getExtensions().getByType(JavaPluginExtension.class);
                javaPluginExtension.withSourcesJar();
            });
        });
    }
}
