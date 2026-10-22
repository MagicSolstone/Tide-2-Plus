package net.minecraft.client.gui.screens.friends;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.social.RemoteFriendListUpdateHandler$State;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class FriendsOverlayScreen$1 {
   static {
      try {
         $SwitchMap$net$minecraft$client$gui$screens$social$RemoteFriendListUpdateHandler$State[RemoteFriendListUpdateHandler$State.LOADING.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$screens$social$RemoteFriendListUpdateHandler$State[RemoteFriendListUpdateHandler$State.UPGRADE_NEEDED.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$screens$social$RemoteFriendListUpdateHandler$State[RemoteFriendListUpdateHandler$State.CONNECTION_ISSUE.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$screens$social$RemoteFriendListUpdateHandler$State[RemoteFriendListUpdateHandler$State.TEMPORARY_UNAVAILABLE
            .ordinal()] = 4;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$screens$social$RemoteFriendListUpdateHandler$State[RemoteFriendListUpdateHandler$State.USER_MAY_LACK_ACTIVE_PROFILE
            .ordinal()] = 5;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$screens$social$RemoteFriendListUpdateHandler$State[RemoteFriendListUpdateHandler$State.UNAUTHORIZED.ordinal()] = 6;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$screens$social$RemoteFriendListUpdateHandler$State[RemoteFriendListUpdateHandler$State.GENERIC_ERROR.ordinal()] = 7;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$gui$screens$social$RemoteFriendListUpdateHandler$State[RemoteFriendListUpdateHandler$State.SUCCESS.ordinal()] = 8;
      } catch (NoSuchFieldError var1) {
      }
   }
}
