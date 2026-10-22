package net.minecraft.server.packs.linkfs;

import java.util.Map;

public record PathContents$DirectoryContents(Map<String, LinkFSPath> children) implements PathContents {
}
