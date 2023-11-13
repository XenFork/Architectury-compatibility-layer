package io.github.xenfork.acl;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

public class Main implements Plugin<Project> {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Override
    public void apply(@NotNull Project target) {
        PropertiesSet set = target.getExtensions().create("acl", PropertiesSet.class);
        target.afterEvaluate(project -> {
            init(set, project);// init acl extensions
        });
    }

    private static void init(PropertiesSet set, Project project) {
        if (set.getMc_version() == null) {
            throw new RuntimeException("don't set minecraft version");
        }
        if (set.getProject_name() == null) {
            set.setProject_name(project.getName());
        }
        if (set.getGroup() == null) {
            set.setGroup("io.github.xenfork");
        }
    }
}