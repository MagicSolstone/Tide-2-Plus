package net.minecraft.server.jsonrpc.methods;

import java.util.Optional;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.NameAndId;

record OperatorService$Op(NameAndId user, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) {
}
