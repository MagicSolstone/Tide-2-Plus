package net.minecraft.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent$Action;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.TooltipProvider$Getter;
import org.jspecify.annotations.Nullable;

public class SignText {
   public static final int LINES = 4;
   public static final Codec<SignText> CODEC = RecordCodecBuilder.create(
         i -> i.group(
               ComponentSerialization.CODEC.listOf(4, 4).fieldOf("messages").forGetter(o -> o.messages),
               ComponentSerialization.CODEC.listOf(4, 4).lenientOptionalFieldOf("filtered_messages").forGetter(SignText::filteredMessagesForSerialization),
               ExtraCodecs.optionalAlwaysPresentFieldOf(DyeColor.CODEC, "color", DyeColor.BLACK).forGetter(o -> o.color),
               ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.BOOL, "has_glowing_text", false).forGetter(o -> o.hasGlowingText)
            )
            .apply(i, SignText::load)
      )
      .validate(SignText::validateLineCounts);
   public static final StreamCodec<RegistryFriendlyByteBuf, SignText> STREAM_CODEC = StreamCodec.composite(
      ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.fixedSizeList(4)),
      o -> o.messages,
      ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.fixedSizeList(4)).apply(ByteBufCodecs::optional),
      SignText::filteredMessagesForSerialization,
      DyeColor.STREAM_CODEC,
      o -> o.color,
      ByteBufCodecs.BOOL,
      o -> o.hasGlowingText,
      SignText::load
   );
   public static final TooltipProvider$Getter<SignText> FRONT_TEXT = createTooltip(Component.translatable("sign.front_text").withStyle(ChatFormatting.GRAY));
   public static final TooltipProvider$Getter<SignText> BACK_TEXT = createTooltip(Component.translatable("sign.back_text").withStyle(ChatFormatting.GRAY));
   private static final List<Component> EMPTY_MESSAGES = Collections.nCopies(4, CommonComponents.EMPTY);
   public static final SignText EMPTY = new SignText(EMPTY_MESSAGES, EMPTY_MESSAGES, DyeColor.BLACK, false);
   private final List<Component> messages;
   private final List<Component> filteredMessages;
   private final DyeColor color;
   private final boolean hasGlowingText;
   @Nullable
   private FormattedCharSequence[] renderMessages;
   private boolean renderMessagedFiltered;

   public SignText(final List<Component> messages, final List<Component> filteredMessages, final DyeColor color, final boolean hasGlowingText) {
      this.messages = messages;
      this.filteredMessages = filteredMessages;
      this.color = color;
      this.hasGlowingText = hasGlowingText;
   }

   private static SignText load(
      final List<Component> messages, final Optional<List<Component>> filteredMessages, final DyeColor color, final boolean hasGlowingText
   ) {
      return new SignText(messages, filteredMessages.orElse(messages), color, hasGlowingText);
   }

   private static DataResult<SignText> validateLineCounts(final SignText signText) {
      return signText.filteredMessages.size() != signText.messages.size()
         ? DataResult.error(() -> "Filtered and raw line counts are not equal")
         : DataResult.success(signText);
   }

   public boolean hasGlowingText() {
      return this.hasGlowingText;
   }

   public SignText withGlowingText(final boolean hasGlowingText) {
      return hasGlowingText == this.hasGlowingText ? this : new SignText(this.messages, this.filteredMessages, this.color, hasGlowingText);
   }

   public DyeColor getColor() {
      return this.color;
   }

   public SignText withColor(final DyeColor color) {
      return color == this.getColor() ? this : new SignText(this.messages, this.filteredMessages, color, this.hasGlowingText);
   }

   public boolean hasMessage(final boolean shouldFilter) {
      return this.getMessages(shouldFilter).stream().anyMatch(component -> !component.getString().isEmpty());
   }

   public List<Component> getMessages(final boolean shouldFilter) {
      return shouldFilter ? this.filteredMessages : this.messages;
   }

   public FormattedCharSequence[] getRenderMessages(final boolean shouldFilter, final Function<Component, FormattedCharSequence> prepare) {
      if (this.renderMessages == null || this.renderMessagedFiltered != shouldFilter) {
         List<Component> messages = this.getMessages(shouldFilter);
         this.renderMessagedFiltered = shouldFilter;
         this.renderMessages = new FormattedCharSequence[messages.size()];

         for (int i = 0; i < messages.size(); i++) {
            this.renderMessages[i] = prepare.apply(messages.get(i));
         }
      }

      return this.renderMessages;
   }

   private Optional<List<Component>> filteredMessagesForSerialization() {
      return this.filteredMessages.equals(this.messages) ? Optional.empty() : Optional.of(this.filteredMessages);
   }

   public boolean hasAnyClickCommands(final boolean shouldFilter) {
      for (Component message : this.getMessages(shouldFilter)) {
         Style style = message.getStyle();
         ClickEvent event = style.getClickEvent();
         if (event != null && event.action() == ClickEvent$Action.RUN_COMMAND) {
            return true;
         }
      }

      return false;
   }

   public boolean hasEditableText(final boolean shouldFilter) {
      return this.getMessages(shouldFilter)
         .stream()
         .allMatch(message -> message.equals(CommonComponents.EMPTY) || message.getContents() instanceof PlainTextContents);
   }

   @Override
   public boolean equals(final Object object) {
      return !(object instanceof SignText signText)
         ? false
         : this.hasGlowingText == signText.hasGlowingText
            && this.messages.equals(signText.messages)
            && this.filteredMessages.equals(signText.filteredMessages)
            && this.color == signText.color;
   }

   @Override
   public int hashCode() {
      int result = 1;
      result = 31 * result + this.messages.hashCode();
      result = 31 * result + this.filteredMessages.hashCode();
      result = 31 * result + this.color.hashCode();
      return 31 * result + Boolean.hashCode(this.hasGlowingText);
   }

   public SignText$Mutable asMutable() {
      return new SignText$Mutable(this);
   }

   public static TooltipProvider$Getter<SignText> createTooltip(final Component title) {
      return component -> component.equals(EMPTY) ? (var0x, var1x, var2, var3) -> {} : (var2, consumer, var4, var5) -> {
         consumer.accept(title);

         for (Component message : component.messages) {
            consumer.accept(Component.literal("  ").append(message));
         }
      };
   }
}
