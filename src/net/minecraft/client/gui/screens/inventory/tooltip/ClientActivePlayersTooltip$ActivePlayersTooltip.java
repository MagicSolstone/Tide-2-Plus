package net.minecraft.client.gui.screens.inventory.tooltip;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.PlayerSkinRenderCache$RenderInfo;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

@Environment(EnvType.CLIENT)
public record ClientActivePlayersTooltip$ActivePlayersTooltip(List<PlayerSkinRenderCache$RenderInfo> profiles) implements TooltipComponent {
}
