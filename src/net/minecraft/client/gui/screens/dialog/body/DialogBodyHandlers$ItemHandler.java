package net.minecraft.client.gui.screens.dialog.body;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.FocusableTextWidget$BackgroundFill;
import net.minecraft.client.gui.components.ItemDisplayWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
class DialogBodyHandlers$ItemHandler implements DialogBodyHandler<ItemBody> {
   private DialogBodyHandlers$ItemHandler() {
   }

   public LayoutElement createControls(final DialogScreen<?> parent, final ItemBody item) {
      ItemStack displayStack = item.item().create();
      if (item.description().isPresent()) {
         PlainMessage description = item.description().get();
         LinearLayout layout = LinearLayout.horizontal().spacing(2);
         layout.defaultCellSetting().alignVerticallyMiddle();
         ItemDisplayWidget itemWidget = new ItemDisplayWidget(
            Minecraft.getInstance(), 0, 0, item.width(), item.height(), CommonComponents.EMPTY, displayStack, item.showDecorations(), item.showTooltip()
         );
         layout.addChild(itemWidget);
         layout.addChild(
            FocusableTextWidget.builder(description.contents(), parent.getFont())
               .maxWidth(description.width())
               .alwaysShowBorder(false)
               .backgroundFill(FocusableTextWidget$BackgroundFill.NEVER)
               .build()
               .setComponentClickHandler(style -> DialogBodyHandlers.runActionOnParent(parent, style))
         );
         return layout;
      } else {
         return new ItemDisplayWidget(
            Minecraft.getInstance(), 0, 0, item.width(), item.height(), displayStack.getHoverName(), displayStack, item.showDecorations(), item.showTooltip()
         );
      }
   }
}
