package net.minecraft.client.gui.spectator.categories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.TeamColor;

@Environment(EnvType.CLIENT)
class TeleportToTeamMenuCategory$TeamSelectionItem implements SpectatorMenuItem {
   private final PlayerTeam team;
   private final Supplier<PlayerSkin> iconSkin;
   private final List<PlayerInfo> players;

   private TeleportToTeamMenuCategory$TeamSelectionItem(final PlayerTeam team, final List<PlayerInfo> players, final Supplier<PlayerSkin> iconSkin) {
      this.team = team;
      this.players = players;
      this.iconSkin = iconSkin;
   }

   public static Optional<SpectatorMenuItem> create(final Minecraft minecraft, final PlayerTeam team) {
      List<PlayerInfo> players = new ArrayList<>();

      for (String name : team.getPlayers()) {
         PlayerInfo info = minecraft.getConnection().getPlayerInfo(name);
         if (info != null && info.getGameMode() != GameType.SPECTATOR) {
            players.add(info);
         }
      }

      if (players.isEmpty()) {
         return Optional.empty();
      } else {
         PlayerInfo playerInfo = players.get(RandomSource.createThreadLocalInstance().nextInt(players.size()));
         return Optional.of(new TeleportToTeamMenuCategory$TeamSelectionItem(team, players, playerInfo::getSkin));
      }
   }

   @Override
   public void selectItem(final SpectatorMenu menu) {
      menu.selectCategory(new TeleportToPlayerMenuCategory(this.players));
   }

   @Override
   public Component getName() {
      return this.team.getDisplayName();
   }

   @Override
   public void extractIcon(final GuiGraphicsExtractor graphics, final float brightness, final float alpha) {
      Optional<TeamColor> teamColor = this.team.getColor();
      if (teamColor.isPresent()) {
         graphics.fill(1, 1, 15, 15, ARGB.scaleRGB(teamColor.get().rgb(), brightness));
      }

      PlayerFaceExtractor.extractRenderState(graphics, this.iconSkin.get(), 2, 2, 12, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
