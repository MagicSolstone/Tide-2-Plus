package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.PendingInvite;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button$CreateNarration;
import net.minecraft.client.gui.components.ContainerObjectSelectionList$Entry;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
class RealmsPendingInvitesScreen$Entry extends ContainerObjectSelectionList$Entry<RealmsPendingInvitesScreen$Entry> {
   private static final Component ACCEPT_INVITE = Component.translatable("mco.invites.button.accept");
   private static final Component REJECT_INVITE = Component.translatable("mco.invites.button.reject");
   private static final WidgetSprites ACCEPT_SPRITE = new WidgetSprites(
      Identifier.withDefaultNamespace("pending_invite/accept"), Identifier.withDefaultNamespace("pending_invite/accept_highlighted")
   );
   private static final WidgetSprites REJECT_SPRITE = new WidgetSprites(
      Identifier.withDefaultNamespace("pending_invite/reject"), Identifier.withDefaultNamespace("pending_invite/reject_highlighted")
   );
   private static final int SPRITE_TEXTURE_SIZE = 18;
   private static final int SPRITE_SIZE = 21;
   private static final int TEXT_LEFT = 38;
   private final PendingInvite pendingInvite;
   private final List<AbstractWidget> children;
   private final SpriteIconButton acceptButton;
   private final SpriteIconButton rejectButton;
   private final StringWidget realmName;
   private final StringWidget realmOwnerName;
   private final StringWidget inviteDate;

   public RealmsPendingInvitesScreen$Entry(final RealmsPendingInvitesScreen this$0, final PendingInvite pendingInvite) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.children = new ArrayList<>();
      this.pendingInvite = pendingInvite;
      int maxTextWidth = this$0x.pendingInvitationSelectionList.getRowWidth() - 32 - 32 - 42;
      this.realmName = new StringWidget(Component.literal(pendingInvite.realmName()), RealmsPendingInvitesScreen.access$000(this$0x)).setMaxWidth(maxTextWidth);
      this.realmOwnerName = new StringWidget(
            Component.literal(pendingInvite.realmOwnerName()).withColor(-6250336), RealmsPendingInvitesScreen.access$100(this$0x)
         )
         .setMaxWidth(maxTextWidth);
      this.inviteDate = new StringWidget(
            ComponentUtils.mergeStyles(RealmsUtil.convertToAgePresentationFromInstant(pendingInvite.date()), Style.EMPTY.withColor(-6250336)),
            RealmsPendingInvitesScreen.access$200(this$0x)
         )
         .setMaxWidth(maxTextWidth);
      Button$CreateNarration narration = this.getCreateNarration(pendingInvite);
      this.acceptButton = SpriteIconButton.builder(ACCEPT_INVITE, button -> this.handleInvitation(true), false)
         .sprite(ACCEPT_SPRITE, 18, 18)
         .size(21, 21)
         .narration(narration)
         .withTootip()
         .build();
      this.rejectButton = SpriteIconButton.builder(REJECT_INVITE, button -> this.handleInvitation(false), false)
         .sprite(REJECT_SPRITE, 18, 18)
         .size(21, 21)
         .narration(narration)
         .withTootip()
         .build();
      this.children.addAll(List.of(this.acceptButton, this.rejectButton));
   }

   private Button$CreateNarration getCreateNarration(final PendingInvite pendingInvite) {
      return defaultNarrationSupplier -> {
         MutableComponent narration = CommonComponents.joinForNarration(
            defaultNarrationSupplier.get(),
            Component.literal(pendingInvite.realmName()),
            Component.literal(pendingInvite.realmOwnerName()),
            RealmsUtil.convertToAgePresentationFromInstant(pendingInvite.date())
         );
         return Component.translatable("narrator.select", narration);
      };
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return this.children;
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return this.children;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int x = this.getContentX();
      int y = this.getContentY();
      int textX = x + 38;
      RealmsUtil.extractPlayerFace(graphics, x, y, 32, this.pendingInvite.realmOwnerUuid());
      this.realmName.setPosition(textX, y + 1);
      this.realmName.extractWidgetRenderState(graphics, mouseX, mouseY, x);
      this.realmOwnerName.setPosition(textX, y + 12);
      this.realmOwnerName.extractWidgetRenderState(graphics, mouseX, mouseY, x);
      this.inviteDate.setPosition(textX, y + 24);
      this.inviteDate.extractWidgetRenderState(graphics, mouseX, mouseY, x);
      int buttonY = y + this.getContentHeight() / 2 - 10;
      this.acceptButton.setPosition(x + this.getContentWidth() - 16 - 42, buttonY);
      this.acceptButton.extractRenderState(graphics, mouseX, mouseY, a);
      this.rejectButton.setPosition(x + this.getContentWidth() - 8 - 21, buttonY);
      this.rejectButton.extractRenderState(graphics, mouseX, mouseY, a);
   }

   private void handleInvitation(final boolean accept) {
      String invitationId = this.pendingInvite.invitationId();
      CompletableFuture.<Boolean>supplyAsync(() -> {
         try {
            RealmsClient client = RealmsClient.getOrCreate();
            if (accept) {
               client.acceptInvitation(invitationId);
            } else {
               client.rejectInvitation(invitationId);
            }

            return true;
         } catch (RealmsServiceException var3) {
            RealmsPendingInvitesScreen.LOGGER.error("Couldn't handle invite", var3);
            return false;
         }
      }, Util.ioPool()).thenAcceptAsync(result -> {
         if (result) {
            this.this$0.pendingInvitationSelectionList.removeInvitation(this);
            RealmsDataFetcher dataFetcher = RealmsPendingInvitesScreen.access$300(this.this$0).realmsDataFetcher();
            if (accept) {
               dataFetcher.serverListUpdateTask.reset();
            }

            dataFetcher.pendingInvitesTask.reset();
         }
      }, RealmsPendingInvitesScreen.access$400(this.this$0));
   }
}
