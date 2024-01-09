package io.github.xenfork.acl.settings;

import io.github.xenfork.acl.projects.AclExtensions;
import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainSettings implements Plugin<Settings> {
    public static AclExtensions acl;
    @Override
    public void apply(@NotNull Settings target) {

        StsExtensions sts = target.getExtensions().create("sts", StsExtensions.class);
        acl = target.getExtensions().create("acl", AclExtensions.class);
        Properties properties = new Properties();
        try {
            properties.load(new BufferedReader(new FileReader(new File(target.getRootProject().getProjectDir(), "gradle.properties"))));
            sts.projects = properties.getProperty("sts.projects");
            sts.platform = properties.getProperty("sts.platform");
        } catch (IOException ignored) {}

        if (sts.projects != null && !sts.projects.isEmpty()) {
            for (String project : sts.getProjects().split(",")) {
                String common = project + "-common";
                target.include(common);
                target.project(":" + common).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/common").toFile());
                if (sts.platform == null) {
                    String fabric = project + "-fabric";
                    String forge = project + "-forge";
                    target.include(fabric, forge);
                    target.project(":" + fabric).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/fabric").toFile());
                    target.project(":" + forge).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/forge").toFile());
                } else {
                    for (String platform : sts.platform.split(",")) {
                        String path = project + "-" + platform;
                        target.include(path);
                        target.project(":" + path).setProjectDir(target.getRootProject().getProjectDir().toPath().resolve(project + "/" + platform).toFile());
                    }
                }

            }
        }

    }
}
