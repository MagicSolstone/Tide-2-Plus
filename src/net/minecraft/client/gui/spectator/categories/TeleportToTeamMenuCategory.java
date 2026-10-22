package net.minecraft.client.gui.spectator.categories;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.SpectatorMenuCategory;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.scores.Scoreboard;

@Environment(EnvType.CLIENT)
public class TeleportToTeamMenuCategory implements SpectatorMenuCategory, SpectatorMenuItem {
   private static final Identifier TELEPORT_TO_TEAM_SPRITE = Identifier.withDefaultNamespace("spectator/teleport_to_team");
   private static final Component TELEPORT_TEXT = Component.translatable("spectatorMenu.team_teleport");
   private static final Component TELEPORT_PROMPT = Component.translatable("spectatorMenu.team_teleport.prompt");
   private final List<SpectatorMenuItem> items;

   public TeleportToTeamMenuCategory() {
      Minecraft minecraft = Minecraft.getInstance();
      this.items = createTeamEntries(minecraft, minecraft.level.getScoreboard());
   }

   private static List<SpectatorMenuItem> createTeamEntries(final Minecraft minecraft, final Scoreboard scoreboard) {
      return scoreboard.getPlayerTeams().stream().flatMap(team -> TeleportToTeamMenuCategory$TeamSelectionItem.create(minecraft, team).stream()).toList();
   }

   @Override
   public List<SpectatorMenuItem> getItems() {
      return this.items;
   }

   @Override
   public Component getPrompt() {
      return TELEPORT_PROMPT;
   }

   @Override
   public void selectItem(final SpectatorMenu menu) {
      menu.selectCategory(this);
   }

   @Override
   public Component getName() {
      return TELEPORT_TEXT;
   }

   @Override
   public void extractIcon(final GuiGraphicsExtractor graphics, final float brightness, final float alpha) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TELEPORT_TO_TEAM_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
   }

   @Override
   public boolean isEnabled() {
      return !this.items.isEmpty();
   }
}
