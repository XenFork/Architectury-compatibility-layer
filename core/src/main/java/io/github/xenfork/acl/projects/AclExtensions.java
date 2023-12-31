package io.github.xenfork.acl.projects;

import io.github.xenfork.acl.mappings.Type;

public class AclExtensions {
    private String mcversion, group, project$name, srg;
    public static String srg_out = "";
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

    public void setSrg(String srg) {
        this.srg = srg;
    }

    public String getSrg() {
        //mojang的时候可空 空为原生，非空为parchment mapoing
        //parchment 如果想跨版本点映射获取请书写 version:parchment version
        //否则只需要书写当前的parchment即可
        //yarn模式下它不可为空，跨版本和上同理，不需要写入build.
        //mcp模式暂时缺省。
        if (srg == null)
            srg = "";
        return srg;
    }
}
