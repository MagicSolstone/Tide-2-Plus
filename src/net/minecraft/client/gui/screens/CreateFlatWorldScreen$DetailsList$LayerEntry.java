package net.minecraft.client.gui.screens;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;

@Environment(EnvType.CLIENT)
class CreateFlatWorldScreen$DetailsList$LayerEntry extends CreateFlatWorldScreen$DetailsList$Entry {
   private final FlatLayerInfo layerInfo;
   private final int index;

   public CreateFlatWorldScreen$DetailsList$LayerEntry(final CreateFlatWorldScreen$DetailsList this$1, final FlatLayerInfo layerInfo, final int index) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.layerInfo = layerInfo;
      this.index = index;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      BlockState blockState = this.layerInfo.getBlockState();
      ItemStack itemStack = this.getDisplayItem(blockState);
      this.blitSlot(graphics, this.getContentX(), this.getContentY(), itemStack);
      int y = this.getContentYMiddle() - 9 / 2;
      graphics.text(this.this$1.this$0.font, itemStack.getHoverName(), this.getContentX() + 18 + 5, y, -1);
      Component height;
      if (this.index == 0) {
         height = Component.translatable("createWorld.customize.flat.layer.top", this.layerInfo.getHeight());
      } else if (this.index == this.this$1.this$0.generator.getLayersInfo().size() - 1) {
         height = Component.translatable("createWorld.customize.flat.layer.bottom", this.layerInfo.getHeight());
      } else {
         height = Component.translatable("createWorld.customize.flat.layer", this.layerInfo.getHeight());
      }

      graphics.text(this.this$1.this$0.font, height, this.getContentRight() - this.this$1.this$0.font.width(height), y, -1);
   }

   private ItemStack getDisplayItem(final BlockState blockState) {
      Item item = blockState.getBlock().asItem();
      if (item == Items.AIR) {
         if (blockState.is(Blocks.WATER)) {
            item = Items.WATER_BUCKET;
         } else if (blockState.is(Blocks.LAVA)) {
            item = Items.LAVA_BUCKET;
         }
      }

      return new ItemStack(item);
   }

   @Override
   public Component getNarration() {
      ItemStack itemStack = this.getDisplayItem(this.layerInfo.getBlockState());
      return (Component)(!itemStack.isEmpty()
         ? CommonComponents.joinForNarration(
            Component.translatable("narrator.select", itemStack.getHoverName()),
            CreateFlatWorldScreen$DetailsList.HEIGHT_TITLE,
            Component.literal(String.valueOf(this.layerInfo.getHeight()))
         )
         : CommonComponents.EMPTY);
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.this$1.setSelected((CreateFlatWorldScreen$DetailsList$Entry)this);
      return super.mouseClicked(event, doubleClick);
   }

   private void blitSlot(final GuiGraphicsExtractor graphics, final int x, final int y, final ItemStack itemStack) {
      this.blitSlotBg(graphics, x + 1, y + 1);
      if (!itemStack.isEmpty()) {
         graphics.fakeItem(itemStack, x + 2, y + 2);
      }
   }

   private void blitSlotBg(final GuiGraphicsExtractor graphics, final int x, final int y) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, CreateFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
   }
}
