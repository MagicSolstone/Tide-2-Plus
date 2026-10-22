package net.minecraft.advancements;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Optional;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.ClientAsset$ResourceTexture;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

public class Advancement$Builder {
   private Optional<Identifier> parent = Optional.empty();
   private Optional<DisplayInfo> display = Optional.empty();
   private AdvancementRewards rewards = AdvancementRewards.EMPTY;
   private final Builder<String, Criterion<?>> criteria = ImmutableMap.builder();
   private Optional<AdvancementRequirements> requirements = Optional.empty();
   private AdvancementRequirements$Strategy requirementsStrategy = AdvancementRequirements$Strategy.AND;
   private boolean sendsTelemetryEvent;

   public static Advancement$Builder advancement() {
      return new Advancement$Builder().sendsTelemetryEvent();
   }

   public static Advancement$Builder recipeAdvancement() {
      return new Advancement$Builder();
   }

   public Advancement$Builder parent(final AdvancementHolder parent) {
      this.parent = Optional.of(parent.id());
      return this;
   }

   @Deprecated(
      forRemoval = true
   )
   public Advancement$Builder parent(final Identifier parent) {
      this.parent = Optional.of(parent);
      return this;
   }

   public Advancement$Builder rootDisplay(
      final ItemStackTemplate icon,
      final Component title,
      final Component description,
      final Identifier background,
      final AdvancementType frame,
      final boolean showToast,
      final boolean announceChat,
      final boolean hidden
   ) {
      return this.display(
         new DisplayInfo(icon, title, description, Optional.of(new ClientAsset$ResourceTexture(background)), frame, showToast, announceChat, hidden)
      );
   }

   public Advancement$Builder rootDisplay(
      final Item icon,
      final Component title,
      final Component description,
      final Identifier background,
      final AdvancementType frame,
      final boolean showToast,
      final boolean announceChat,
      final boolean hidden
   ) {
      return this.rootDisplay(new ItemStackTemplate(icon), title, description, background, frame, showToast, announceChat, hidden);
   }

   public Advancement$Builder display(
      final ItemStackTemplate icon,
      final Component title,
      final Component description,
      final AdvancementType frame,
      final boolean showToast,
      final boolean announceChat,
      final boolean hidden
   ) {
      return this.display(new DisplayInfo(icon, title, description, Optional.empty(), frame, showToast, announceChat, hidden));
   }

   public Advancement$Builder display(
      final Item icon,
      final Component title,
      final Component description,
      final AdvancementType frame,
      final boolean showToast,
      final boolean announceChat,
      final boolean hidden
   ) {
      return this.display(new ItemStackTemplate(icon), title, description, frame, showToast, announceChat, hidden);
   }

   public Advancement$Builder display(final DisplayInfo display) {
      this.display = Optional.of(display);
      return this;
   }

   public Advancement$Builder rewards(final AdvancementRewards$Builder rewards) {
      return this.rewards(rewards.build());
   }

   public Advancement$Builder rewards(final AdvancementRewards rewards) {
      this.rewards = rewards;
      return this;
   }

   public Advancement$Builder addCriterion(final String name, final Criterion<?> criterion) {
      this.criteria.put(name, criterion);
      return this;
   }

   public Advancement$Builder requirements(final AdvancementRequirements$Strategy strategy) {
      this.requirementsStrategy = strategy;
      return this;
   }

   public Advancement$Builder requirements(final AdvancementRequirements requirements) {
      this.requirements = Optional.of(requirements);
      return this;
   }

   public Advancement$Builder sendsTelemetryEvent() {
      this.sendsTelemetryEvent = true;
      return this;
   }

   public AdvancementHolder build(final Identifier id) {
      Map<String, Criterion<?>> criteria = this.criteria.buildOrThrow();
      AdvancementRequirements requirements = this.requirements.orElseGet(() -> this.requirementsStrategy.create(criteria.keySet()));
      return new AdvancementHolder(id, new Advancement(this.parent, this.display, this.rewards, criteria, requirements, this.sendsTelemetryEvent));
   }

   public AdvancementHolder save(final BootstrapContext<Advancement> output, final String name) {
      AdvancementHolder advancement = this.build(Identifier.parse(name));
      advancement.register(output);
      return advancement;
   }
}
