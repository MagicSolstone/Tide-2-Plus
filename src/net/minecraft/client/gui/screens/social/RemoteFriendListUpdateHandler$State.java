package net.minecraft.client.gui.screens.social;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum RemoteFriendListUpdateHandler$State {
   LOADING,
   UPGRADE_NEEDED,
   CONNECTION_ISSUE,
   USER_MAY_LACK_ACTIVE_PROFILE,
   UNAUTHORIZED,
   TEMPORARY_UNAVAILABLE,
   GENERIC_ERROR,
   SUCCESS;
}
