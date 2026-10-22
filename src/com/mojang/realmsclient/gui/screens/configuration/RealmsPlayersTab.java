package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.dto.RealmsServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout$RowHelper;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsPlayersTab extends GridLayoutTab implements RealmsConfigurationTab {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final Component TITLE = Component.translatable("mco.configure.world.players.title");
   private static final Component QUESTION_TITLE = Component.translatable("mco.question");
   private static final int PADDING = 8;
   private final RealmsConfigureWorldScreen configurationScreen;
   private final Minecraft minecraft;
   private final Font font;
   private RealmsServer serverData;
   private final RealmsPlayersTab$InvitedObjectSelectionList invitedList;

   public RealmsPlayersTab(final RealmsConfigureWorldScreen configurationScreen, final Minecraft minecraft, final RealmsServer serverData) {
      super(TITLE);
      this.configurationScreen = configurationScreen;
      this.minecraft = minecraft;
      this.font = configurationScreen.getFont();
      this.serverData = serverData;
      GridLayout$RowHelper helper = this.layout.spacing(8).createRowHelper(1);
      this.invitedList = helper.addChild(
         new RealmsPlayersTab$InvitedObjectSelectionList(this, configurationScreen.width, this.calculateListHeight()),
         LayoutSettings.defaults().alignVerticallyTop().alignHorizontallyCenter()
      );
      LinearLayout actions = helper.addChild(LinearLayout.horizontal().spacing(8), LayoutSettings.defaults().alignVerticallyBottom().alignHorizontallyCenter());
      actions.addChild(
         Button.builder(
               Component.translatable("mco.configure.world.buttons.invite"),
               var3 -> minecraft.gui.setScreen(new RealmsInviteScreen(configurationScreen, serverData))
            )
            .build()
      );
      actions.addChild(
         Button.builder(RealmsInviteCodesScreen.TITLE, var3 -> minecraft.gui.setScreen(new RealmsInviteCodesScreen(configurationScreen, serverData))).build()
      );
      this.updateData(serverData);
   }

   public int calculateListHeight() {
      return this.configurationScreen.getContentHeight() - 20 - 16;
   }

   @Override
   public void doLayout(final ScreenRectangle screenRectangle) {
      this.invitedList.updateSizeAndPosition(this.configurationScreen.width, this.calculateListHeight(), this.invitedList.getX(), this.invitedList.getY());
      super.doLayout(screenRectangle);
   }

   @Override
   public void updateData(final RealmsServer serverData) {
      this.serverData = serverData;
      this.invitedList.updateList(serverData);
   }
}
