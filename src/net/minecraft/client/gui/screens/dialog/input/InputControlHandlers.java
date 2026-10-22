package net.minecraft.client.gui.screens.dialog.input;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.input.BooleanInput;
import net.minecraft.server.dialog.input.InputControl;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.minecraft.server.dialog.input.SingleOptionInput;
import net.minecraft.server.dialog.input.TextInput;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class InputControlHandlers {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Map<MapCodec<? extends InputControl>, InputControlHandler<?>> HANDLERS = new HashMap<>();

   private static <T extends InputControl> void register(final MapCodec<T> type, final InputControlHandler<? super T> handler) {
      HANDLERS.put(type, handler);
   }

   @Nullable
   private static <T extends InputControl> InputControlHandler<T> get(final T inputControl) {
      return (InputControlHandler<T>)HANDLERS.get(inputControl.mapCodec());
   }

   public static <T extends InputControl> void createHandler(final T inputControl, final Screen screen, final InputControlHandler$Output outputConsumer) {
      InputControlHandler<T> handler = get(inputControl);
      if (handler == null) {
         LOGGER.warn("Unrecognized input control {}", inputControl);
      } else {
         handler.addControl(inputControl, screen, outputConsumer);
      }
   }

   public static void bootstrap() {
      register(TextInput.MAP_CODEC, new InputControlHandlers$TextInputHandler());
      register(SingleOptionInput.MAP_CODEC, new InputControlHandlers$SingleOptionHandler());
      register(BooleanInput.MAP_CODEC, new InputControlHandlers$BooleanHandler());
      register(NumberRangeInput.MAP_CODEC, new InputControlHandlers$NumberRangeHandler());
   }
}
