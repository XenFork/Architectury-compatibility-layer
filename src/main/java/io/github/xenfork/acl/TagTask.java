package io.github.xenfork.acl;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.TaskAction;

public abstract class TagTask extends DefaultTask {
    public abstract DirectoryProperty getOutputDir();

    @TaskAction
    public void commonTags() {

    }
}
