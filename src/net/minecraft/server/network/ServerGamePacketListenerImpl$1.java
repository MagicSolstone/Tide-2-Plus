package net.minecraft.server.network;

import net.minecraft.network.protocol.game.ServerboundClientCommandPacket$Action;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket$Action;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket$Action;
import net.minecraft.world.level.block.entity.CommandBlockEntity$Mode;

// $VF: synthetic class
class ServerGamePacketListenerImpl$1 {
   static {
      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundClientCommandPacket$Action[ServerboundClientCommandPacket$Action.PERFORM_RESPAWN.ordinal()] = 1;
      } catch (NoSuchFieldError var21) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundClientCommandPacket$Action[ServerboundClientCommandPacket$Action.REQUEST_STATS.ordinal()] = 2;
      } catch (NoSuchFieldError var20) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundClientCommandPacket$Action[ServerboundClientCommandPacket$Action.REQUEST_GAMERULE_VALUES
            .ordinal()] = 3;
      } catch (NoSuchFieldError var19) {
      }

      $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerCommandPacket$Action = new int[ServerboundPlayerCommandPacket$Action.values().length];

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerCommandPacket$Action[ServerboundPlayerCommandPacket$Action.START_SPRINTING.ordinal()] = 1;
      } catch (NoSuchFieldError var18) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerCommandPacket$Action[ServerboundPlayerCommandPacket$Action.STOP_SPRINTING.ordinal()] = 2;
      } catch (NoSuchFieldError var17) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerCommandPacket$Action[ServerboundPlayerCommandPacket$Action.STOP_SLEEPING.ordinal()] = 3;
      } catch (NoSuchFieldError var16) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerCommandPacket$Action[ServerboundPlayerCommandPacket$Action.START_RIDING_JUMP.ordinal()] = 4;
      } catch (NoSuchFieldError var15) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerCommandPacket$Action[ServerboundPlayerCommandPacket$Action.STOP_RIDING_JUMP.ordinal()] = 5;
      } catch (NoSuchFieldError var14) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerCommandPacket$Action[ServerboundPlayerCommandPacket$Action.OPEN_INVENTORY.ordinal()] = 6;
      } catch (NoSuchFieldError var13) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerCommandPacket$Action[ServerboundPlayerCommandPacket$Action.START_FALL_FLYING.ordinal()] = 7;
      } catch (NoSuchFieldError var12) {
      }

      $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action = new int[ServerboundPlayerActionPacket$Action.values().length];

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.STAB.ordinal()] = 1;
      } catch (NoSuchFieldError var11) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.SWAP_ITEM_WITH_OFFHAND
            .ordinal()] = 2;
      } catch (NoSuchFieldError var10) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.DROP_ITEM.ordinal()] = 3;
      } catch (NoSuchFieldError var9) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.DROP_ALL_ITEMS.ordinal()] = 4;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.RELEASE_USE_ITEM.ordinal()] = 5;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.START_DESTROY_BLOCK.ordinal()] = 6;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.CHANGE_DESTROY_DIRECTION
            .ordinal()] = 7;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.ABORT_DESTROY_BLOCK.ordinal()] = 8;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$network$protocol$game$ServerboundPlayerActionPacket$Action[ServerboundPlayerActionPacket$Action.STOP_DESTROY_BLOCK.ordinal()] = 9;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$net$minecraft$world$level$block$entity$CommandBlockEntity$Mode = new int[CommandBlockEntity$Mode.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$CommandBlockEntity$Mode[CommandBlockEntity$Mode.SEQUENCE.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$CommandBlockEntity$Mode[CommandBlockEntity$Mode.AUTO.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
