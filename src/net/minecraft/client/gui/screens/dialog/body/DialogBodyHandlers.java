package net.minecraft.client.gui.screens.dialog.body;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class DialogBodyHandlers {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Map<MapCodec<? extends DialogBody>, DialogBodyHandler<?>> HANDLERS = new HashMap<>();

   private static <B extends DialogBody> void register(final MapCodec<B> type, final DialogBodyHandler<? super B> handler) {
      HANDLERS.put(type, handler);
   }

   @Nullable
   private static <B extends DialogBody> DialogBodyHandler<B> getHandler(final B body) {
      return (DialogBodyHandler<B>)HANDLERS.get(body.mapCodec());
   }

   @Nullable
   public static <B extends DialogBody> LayoutElement createBodyElement(final DialogScreen<?> screen, final B body) {
      DialogBodyHandler<B> handler = getHandler(body);
      if (handler == null) {
         LOGGER.warn("Unrecognized dialog body {}", body);
         return null;
      } else {
         return handler.createControls(screen, body);
      }
   }

   public static void bootstrap() {
      register(PlainMessage.MAP_CODEC, new DialogBodyHandlers$PlainMessageHandler());
      register(ItemBody.MAP_CODEC, new DialogBodyHandlers$ItemHandler());
   }

   private static void runActionOnParent(final DialogScreen<?> parent, @Nullable final Style clickedStyle) {
      if (clickedStyle != null) {
         ClickEvent clickEvent = clickedStyle.getClickEvent();
         if (clickEvent != null) {
            parent.runAction(Optional.of(clickEvent));
         }
      }
   }
}
