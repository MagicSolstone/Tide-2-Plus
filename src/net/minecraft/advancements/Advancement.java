package net.minecraft.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.HolderGetter$Provider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent$ShowText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.ProblemReporter$RootFieldPathElement;
import net.minecraft.world.level.storage.loot.ValidationContextSource;

public record Advancement(
   Optional<Identifier> parent,
   Optional<DisplayInfo> display,
   AdvancementRewards rewards,
   Map<String, Criterion<?>> criteria,
   AdvancementRequirements requirements,
   boolean sendsTelemetryEvent,
   Optional<Component> name
) {
   private static final Codec<Map<String, Criterion<?>>> CRITERIA_CODEC = Codec.unboundedMap(Codec.STRING, Criterion.CODEC)
      .validate(criteria -> criteria.isEmpty() ? DataResult.error(() -> "Advancement criteria cannot be empty") : DataResult.success(criteria));
   public static final Codec<Advancement> CODEC = RecordCodecBuilder.create(
         i -> i.group(
               Identifier.CODEC.optionalFieldOf("parent").forGetter(Advancement::parent),
               DisplayInfo.CODEC.optionalFieldOf("display").forGetter(Advancement::display),
               AdvancementRewards.CODEC.optionalFieldOf("rewards", AdvancementRewards.EMPTY).forGetter(Advancement::rewards),
               CRITERIA_CODEC.fieldOf("criteria").forGetter(Advancement::criteria),
               AdvancementRequirements.CODEC.optionalFieldOf("requirements").forGetter(a -> Optional.of(a.requirements())),
               Codec.BOOL.optionalFieldOf("sends_telemetry_event", false).forGetter(Advancement::sendsTelemetryEvent)
            )
            .apply(i, (parent, display, rewards, criteria, requirementsOpt, sendsTelemetryEvent) -> {
               AdvancementRequirements requirements = requirementsOpt.orElseGet(() -> AdvancementRequirements.allOf(criteria.keySet()));
               return new Advancement(parent, display, rewards, criteria, requirements, sendsTelemetryEvent);
            })
      )
      .validate(Advancement::validate);
   public static final StreamCodec<RegistryFriendlyByteBuf, Advancement> STREAM_CODEC = StreamCodec.composite(
      Identifier.STREAM_CODEC.apply(ByteBufCodecs::optional),
      Advancement::parent,
      DisplayInfo.STREAM_CODEC.apply(ByteBufCodecs::optional),
      Advancement::display,
      AdvancementRequirements.STREAM_CODEC,
      Advancement::requirements,
      ByteBufCodecs.BOOL,
      Advancement::sendsTelemetryEvent,
      (parent, display, requirements, sendsTelemetryEvent) -> new Advancement(
         parent, display, AdvancementRewards.EMPTY, Map.of(), requirements, sendsTelemetryEvent
      )
   );

   public Advancement(
      final Optional<Identifier> parent,
      final Optional<DisplayInfo> display,
      final AdvancementRewards rewards,
      final Map<String, Criterion<?>> criteria,
      final AdvancementRequirements requirements,
      final boolean sendsTelemetryEvent
   ) {
      this(parent, display, rewards, Map.copyOf(criteria), requirements, sendsTelemetryEvent, display.map(Advancement::decorateName));
   }

   private static DataResult<Advancement> validate(final Advancement advancement) {
      boolean isRoot = advancement.parent.isEmpty();
      boolean isVisible = advancement.display.isPresent();
      boolean hasBackground = isVisible && advancement.display.get().background().isPresent();
      if (isRoot && isVisible && !hasBackground) {
         return DataResult.error(() -> "Visible advancement roots must have background");
      } else if (!isRoot && hasBackground) {
         return DataResult.error(() -> "Only advancement roots can have background");
      } else {
         DataResult<AdvancementRequirements> requirementValidation = advancement.requirements().validate(advancement.criteria().keySet());
         return requirementValidation.isError() ? requirementValidation.map(var1x -> advancement) : DataResult.success(advancement);
      }
   }

   private static Component decorateName(final DisplayInfo display) {
      Component displayTitle = display.title();
      ChatFormatting color = display.type().getChatColor();
      Component tooltip = ComponentUtils.mergeStyles(displayTitle.copy(), Style.EMPTY.withColor(color)).append("\n").append(display.description());
      Component title = displayTitle.copy().withStyle(s -> s.withHoverEvent(new HoverEvent$ShowText(tooltip)));
      return ComponentUtils.wrapInSquareBrackets(title).withStyle(color);
   }

   public static Component name(final AdvancementHolder holder) {
      return holder.value().name().orElseGet(() -> Component.literal(holder.id().toString()));
   }

   public boolean isRoot() {
      return this.parent.isEmpty();
   }

   public void validate(final ProblemReporter reporter, final HolderGetter$Provider lootData) {
      this.criteria.forEach((name, criterion) -> {
         ValidationContextSource validator = new ValidationContextSource(reporter.forChild(new ProblemReporter$RootFieldPathElement(name)), lootData);
         criterion.triggerInstance().validate(validator);
      });
   }
}
