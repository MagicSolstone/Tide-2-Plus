package net.minecraft.server;

import net.minecraft.commands.Commands$CommandSelection;
import net.minecraft.server.permissions.PermissionSet;

public record WorldLoader$InitConfig(WorldLoader$PackConfig packConfig, Commands$CommandSelection commandSelection, PermissionSet functionCompilationPermissions) {
}
