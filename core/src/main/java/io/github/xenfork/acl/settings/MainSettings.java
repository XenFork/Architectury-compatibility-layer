package io.github.xenfork.acl.settings;

import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainSettings implements Plugin<Settings> {

    @Override
    public void apply(@NotNull Settings target) {

        AclExtensions sts = target.getExtensions().create("sts", AclExtensions.class);
        Properties properties = new Properties();
        try {
            properties.load(new BufferedReader(new FileReader(new File(target.getRootProject().getProjectDir(), "gradle.properties"))));
            sts.projects = properties.getProperty("sts.projects");
            sts.platform = properties.getProperty("sts.platform");
        } catch (IOException ignored) {}

        if (sts.projects != null && !sts.projects.isEmpty()) {
            System.out.println("test");
            for (String project : sts.getProjects().split(",")) {
                String common = project + "-common";
                target.project(":" + common).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/common").toFile());
                if (sts.platform == null) {
                    String fabric = project + "-fabric";
                    String forge = project + "-forge";
                    target.include(common, fabric, forge);
                    target.project(":" + fabric).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/fabric").toFile());
                    target.project(":" + forge).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/forge").toFile());
                } else {
                    for (String platform : sts.platform.split(",")) {
                        target.project(":" + project + "-" + platform).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/" + platform).toFile());
                    }
                }

//                target.include(project + "/common", project + "/fabric", project + "/forge");
            }
        }

    }
}
