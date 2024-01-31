package io.github.xenfork.acl.mappings;
/**
 * @author baka4n
 */
public interface Type {
    /**
     * @since mojang
     */
    Mojang mojang = new Mojang();
    /**
     * @since mcp
     */
    Mcp mcp = new Mcp();
    /**
     * @since yarn
     */
    Yarn yarn = new Yarn();

}
