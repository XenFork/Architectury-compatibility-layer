package io.github.xenfork.acl.projects.sub;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
/**
 * @author baka4n
 */
public class Quilt extends Basic {
    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        architecturyDepends();
    }
}
