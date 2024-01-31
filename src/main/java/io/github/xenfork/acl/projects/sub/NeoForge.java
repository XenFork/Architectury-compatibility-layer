package io.github.xenfork.acl.projects.sub;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
/**
 * @author baka4n
 */
public class NeoForge extends Basic {

    @Override
    public void apply(@NotNull Project target) {
        super.apply(target);
        architecturyDepends();
    }
}
