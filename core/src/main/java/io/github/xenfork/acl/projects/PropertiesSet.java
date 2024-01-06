package io.github.xenfork.acl.projects;

import io.github.xenfork.acl.mappings.Type;

public class PropertiesSet {
    private String mcversion, group, project$name;
    public Type mappings;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMcversion() {
        return mcversion;
    }

    public void setMcversion(String mcversion) {
        this.mcversion = mcversion;
    }

    public String getProject$name() {
        return project$name;
    }

    public void setProject$name(String project$name) {
        this.project$name = project$name;
    }

    public void setMappings(Type mappings) {
        this.mappings = mappings;
    }

    public Type getMappings() {
        if (mappings == null)
            mappings = Type.mojang;
        return mappings;
    }
}
