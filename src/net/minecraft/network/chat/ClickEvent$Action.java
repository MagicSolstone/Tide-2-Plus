package net.minecraft.network.chat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.StringRepresentable;

public enum ClickEvent$Action implements StringRepresentable {
   OPEN_URL("open_url", true, ClickEvent$OpenUrl.CODEC),
   OPEN_FILE("open_file", false, ClickEvent$OpenFile.CODEC),
   RUN_COMMAND("run_command", true, ClickEvent$RunCommand.CODEC),
   SUGGEST_COMMAND("suggest_command", true, ClickEvent$SuggestCommand.CODEC),
   SHOW_DIALOG("show_dialog", true, ClickEvent$ShowDialog.CODEC),
   CHANGE_PAGE("change_page", true, ClickEvent$ChangePage.CODEC),
   COPY_TO_CLIPBOARD("copy_to_clipboard", true, ClickEvent$CopyToClipboard.CODEC),
   CUSTOM("custom", true, ClickEvent$Custom.CODEC);

   public static final Codec<ClickEvent$Action> UNSAFE_CODEC = StringRepresentable.fromEnum(ClickEvent$Action::values);
   public static final Codec<ClickEvent$Action> CODEC = UNSAFE_CODEC.validate(ClickEvent$Action::filterForSerialization);
   private final boolean allowFromServer;
   private final String name;
   private final MapCodec<? extends ClickEvent> codec;

   private ClickEvent$Action(final String name, final boolean allowFromServer, final MapCodec<? extends ClickEvent> codec) {
      this.name = name;
      this.allowFromServer = allowFromServer;
      this.codec = codec;
   }

   public boolean isAllowedFromServer() {
      return this.allowFromServer;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public MapCodec<? extends ClickEvent> valueCodec() {
      return this.codec;
   }

   public static DataResult<ClickEvent$Action> filterForSerialization(final ClickEvent$Action action) {
      return !action.isAllowedFromServer() ? DataResult.error(() -> "Click event type not allowed: " + action) : DataResult.success(action, Lifecycle.stable());
   }
}
