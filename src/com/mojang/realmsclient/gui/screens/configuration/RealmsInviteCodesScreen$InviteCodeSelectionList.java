package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.InviteCode;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RealmsInviteCodesScreen$InviteCodeSelectionList extends ObjectSelectionList<RealmsInviteCodesScreen$InviteCodeSelectionList$Entry> {
   public RealmsInviteCodesScreen$InviteCodeSelectionList(final List<InviteCode> this$0, final List inviteCodes) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(
         Minecraft.getInstance(),
         this$0x.width,
         RealmsInviteCodesScreen.access$000(this$0x).getContentHeight(),
         RealmsInviteCodesScreen.access$100(this$0x).getHeaderHeight(),
         28
      );
      inviteCodes.forEach(inviteCode -> this.addEntry(new RealmsInviteCodesScreen$InviteCodeSelectionList$Entry(this, inviteCode)));
   }

   @Nullable
   public InviteCode getSelectedCode() {
      RealmsInviteCodesScreen$InviteCodeSelectionList$Entry selected = this.getSelected();
      return selected == null ? null : selected.inviteCode;
   }

   public void setSelected(final RealmsInviteCodesScreen$InviteCodeSelectionList$Entry entry) {
      super.setSelected(entry);
      this.this$0.updateButtonStates();
   }

   @Override
   public int getRowWidth() {
      return 308;
   }
}
