package io.github.xenfork.acl.projects;

import io.github.xenfork.acl.mappings.Type;

/**
 * @author baka4n
 */
public class AclExtensions {

    private String mcversion, group, project$name, srg, flv, architectury$version, fapi, fgv;

    /**
     * @since generate mapping srg
     */
    public String srg_out = "";
    /**
     * @since gradle plugin set java version
     */
    public int java = 17;
    /**
     * @since mcp yarn mojang mapping set
     */
    public Type mappings;

    /**
     * @param aclExtensions acl extensions
     */

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
        this.java = aclExtensions.java;
        this.mappings = aclExtensions.mappings;
    }

    /**
     * @return get maven group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group set maven group
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return get minecraft version
     */
    public String getMcversion() {
        return mcversion;
    }

    /**
     * @param mcversion set minecraft version
     */
    public void setMcversion(String mcversion) {
        this.mcversion = mcversion;
    }

    /**
     * @return get project name
     */
    public String getProject$name() {
        return project$name;
    }

    /**
     * @param flv set fabric loader version
     */
    public void setFlv(String flv) {
        this.flv = flv;
    }

    /**
     * @return get fabric loader version
     */
    public String getFlv() {
        return flv;
    }

    /**
     * @param project$name this is project name
     */
    public void setProject$name(String project$name) {
        this.project$name = project$name;
    }

    /**
     * @param mappings set mapping
     */
    public void setMappings(Type mappings) {
        this.mappings = mappings;
    }

    /**
     * @return get mapping
     */
    public Type getMappings() {
        if (mappings == null)
            mappings = Type.mojang;
        return mappings;
    }

    /**
     * @param srg set mapping version
     */
    public void setSrg(String srg) {
        this.srg = srg;
    }

    /**
     * @return mapping version
     */
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

    /**
     * @return architectury version
     */
    public String getArchitectury$version() {
        if (architectury$version == null) architectury$version = "";
        return architectury$version;
    }

    /**
     * @param architectury$version set architectury version
     */
    public void setArchitectury$version(String architectury$version) {
        this.architectury$version = architectury$version;
    }

    /**
     * @return get fabric api version
     */
    public String getFapi() {
        if (fapi == null) fapi = "";
        return fapi;
    }

    /**
     * @param fapi set fabric api versions
     */
    public void setFapi(String fapi) {
        this.fapi = fapi;
    }

    /**
     * @return get forge version
     */
    public String getFgv() {
        return fgv;
    }

    /**
     * @param fgv set forge version
     */
    public void setFgv(String fgv) {
        this.fgv = fgv;
    }
}
