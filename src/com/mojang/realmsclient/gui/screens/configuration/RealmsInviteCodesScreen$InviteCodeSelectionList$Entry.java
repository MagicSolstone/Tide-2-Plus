package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.InviteCode;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class RealmsInviteCodesScreen$InviteCodeSelectionList$Entry extends ObjectSelectionList$Entry<RealmsInviteCodesScreen$InviteCodeSelectionList$Entry> {
   private final InviteCode inviteCode;

   public RealmsInviteCodesScreen$InviteCodeSelectionList$Entry(final RealmsInviteCodesScreen$InviteCodeSelectionList this$1, final InviteCode inviteCode) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.inviteCode = inviteCode;
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.this$1.setSelected(this);
      return true;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int textY = this.getContentYMiddle() - 9 / 2;
      graphics.text(RealmsInviteCodesScreen.access$300(this.this$1.this$0), this.inviteCode.code(), this.getContentX() + 8, textY, -1);
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", this.inviteCode.code());
   }
}
