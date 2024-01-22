package io.github.xenfork.acl.projects;

import io.github.xenfork.acl.mappings.Type;

public class AclExtensions {

    private String mcversion, group, project$name, srg, flv, architectury$version, fapi, fgv;
    public String srg_out = "";
    public int j = 17;
    public Type mappings;

    public void copy(AclExtensions aclExtensions) {
        this.mcversion = aclExtensions.mcversion;
        this.group = aclExtensions.group;
        this.project$name = aclExtensions.project$name;
        this.srg = aclExtensions.srg;
        this.flv = aclExtensions.flv;
        this.architectury$version = aclExtensions.architectury$version;
        this.fapi = aclExtensions.fapi;
        this.fgv = aclExtensions.fgv;
        this.srg_out = aclExtensions.srg_out;
        this.j = aclExtensions.j;
        this.mappings = aclExtensions.mappings;
    }

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

    public void setFlv(String flv) {
        this.flv = flv;
    }

    public String getFlv() {
        return flv;
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

    public String getArchitectury$version() {
        if (architectury$version == null) architectury$version = "";
        return architectury$version;
    }

    public void setArchitectury$version(String architectury$version) {
        this.architectury$version = architectury$version;
    }

    public String getFapi() {
        if (fapi == null) fapi = "";
        return fapi;
    }

    public void setFapi(String fapi) {
        this.fapi = fapi;
    }

    public String getFgv() {
        return fgv;
    }

    public void setFgv(String fgv) {
        this.fgv = fgv;
    }
}
