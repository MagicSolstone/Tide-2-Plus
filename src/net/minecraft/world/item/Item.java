package net.minecraft.world.item;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponentInitializers$Initializer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext$Block;
import net.minecraft.world.level.ClipContext$Fluid;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class Item implements ItemLike, FeatureElement {
   public static final Codec<Holder<Item>> CODEC = BuiltInRegistries.ITEM
      .holderByNameCodec()
      .validate(item -> item.is(Items.AIR.builtInRegistryHolder()) ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(item));
   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ITEM);
   public static final Codec<Holder<Item>> CODEC_WITH_BOUND_COMPONENTS = CODEC.validate(
      item -> !item.areComponentsBound()
         ? DataResult.error(() -> "Item " + item.getRegisteredName() + " does not have components yet")
         : DataResult.success(item)
   );
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final Map<Block, Item> BY_BLOCK = Maps.newHashMap();
   public static final Identifier BASE_ATTACK_DAMAGE_ID = Identifier.withDefaultNamespace("base_attack_damage");
   public static final Identifier BASE_ATTACK_SPEED_ID = Identifier.withDefaultNamespace("base_attack_speed");
   public static final int DEFAULT_MAX_STACK_SIZE = 64;
   public static final int ABSOLUTE_MAX_STACK_SIZE = 99;
   public static final int MAX_BAR_WIDTH = 13;
   protected static final int APPROXIMATELY_INFINITE_USE_DURATION = 72000;
   private final Holder$Reference<Item> builtInRegistryHolder = BuiltInRegistries.ITEM.createIntrusiveHolder(this);
   @Nullable
   private final ItemStackTemplate craftingRemainingItem;
   protected final String descriptionId;
   private final FeatureFlagSet requiredFeatures;

   public static int getId(final Item item) {
      return item == null ? 0 : BuiltInRegistries.ITEM.getId(item);
   }

   public static Item byId(final int id) {
      return BuiltInRegistries.ITEM.byId(id);
   }

   @Deprecated
   public static Item byBlock(final Block block) {
      return BY_BLOCK.getOrDefault(block, Items.AIR);
   }

   public Item(final Item$Properties properties) {
      this.descriptionId = properties.effectiveDescriptionId();
      DataComponentInitializers$Initializer<Item> componentInitializer = properties.finalizeInitializer(
         Component.translatable(this.descriptionId), properties.effectiveModel()
      );
      BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.add(properties.itemIdOrThrow(), componentInitializer);
      this.craftingRemainingItem = properties.craftingRemainingItem;
      this.requiredFeatures = properties.requiredFeatures;
      if (SharedConstants.IS_RUNNING_IN_IDE) {
         String className = this.getClass().getSimpleName();
         if (!className.endsWith("Item")) {
            LOGGER.error("Item classes should end with Item and {} doesn't.", className);
         }
      }
   }

   @Deprecated
   public Holder$Reference<Item> builtInRegistryHolder() {
      return this.builtInRegistryHolder;
   }

   public DataComponentMap components() {
      return this.builtInRegistryHolder.components();
   }

   public int getDefaultMaxStackSize() {
      return this.components().getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
   }

   public void onUseTick(final Level level, final LivingEntity livingEntity, final ItemStack itemStack, final int ticksRemaining) {
   }

   public void onDestroyed(final ItemEntity itemEntity) {
   }

   public boolean canDestroyBlock(final ItemStack itemStack, final BlockState state, final Level level, final BlockPos pos, final LivingEntity user) {
      Tool tool = itemStack.get(DataComponents.TOOL);
      return tool != null && !tool.canDestroyBlocksInCreative() ? !(user instanceof Player player && player.getAbilities().instabuild) : true;
   }

   @Override
   public Item asItem() {
      return this;
   }

   public InteractionResult useOn(final UseOnContext context) {
      ItemStack stack = context.getItemInHand();
      Holder<BlockTransformer> blockTransformer = stack.get(DataComponents.BLOCK_TRANSFORMER);
      return (InteractionResult)(blockTransformer != null ? blockTransformer.value().transformBlock(context) : InteractionResult.PASS);
   }

   public float getDestroySpeed(final ItemStack itemStack, final BlockState state) {
      Tool tool = itemStack.get(DataComponents.TOOL);
      return tool != null ? tool.getMiningSpeed(state) : 1.0F;
   }

   public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      Consumable consumable = stack.get(DataComponents.CONSUMABLE);
      if (consumable != null) {
         return consumable.startConsuming(player, stack, hand);
      } else {
         Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
         if (equippable != null && equippable.swappable()) {
            return equippable.swapWithEquipmentSlot(stack, player);
         } else if (stack.has(DataComponents.BLOCKS_ATTACKS)) {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
         } else {
            KineticWeapon kineticWeapon = stack.get(DataComponents.KINETIC_WEAPON);
            if (kineticWeapon != null) {
               player.startUsingItem(hand);
               kineticWeapon.makeSound(player);
               return InteractionResult.CONSUME;
            } else {
               return InteractionResult.PASS;
            }
         }
      }
   }

   public ItemStack finishUsingItem(final ItemStack itemStack, final Level level, final LivingEntity entity) {
      Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
      return consumable != null ? consumable.onConsume(level, entity, itemStack) : itemStack;
   }

   public boolean isBarVisible(final ItemStack stack) {
      return stack.isDamaged();
   }

   public int getBarWidth(final ItemStack stack) {
      return Mth.clamp(Math.round(13.0F - stack.getDamageValue() * 13.0F / stack.getMaxDamage()), 0, 13);
   }

   public int getBarColor(final ItemStack stack) {
      int maxDamage = stack.getMaxDamage();
      float healthPercentage = Math.max(0.0F, ((float)maxDamage - stack.getDamageValue()) / maxDamage);
      return Mth.hsvToRgb(healthPercentage / 3.0F, 1.0F, 1.0F);
   }

   public boolean overrideStackedOnOther(final ItemStack self, final Slot slot, final ClickAction clickAction, final Player player) {
      return false;
   }

   public boolean overrideOtherStackedOnMe(
      final ItemStack self, final ItemStack other, final Slot slot, final ClickAction clickAction, final Player player, final SlotAccess carriedItem
   ) {
      return false;
   }

   public float getAttackDamageBonus(final Entity victim, final float damage, final DamageSource damageSource) {
      return 0.0F;
   }

   @Deprecated
   @Nullable
   public DamageSource getItemDamageSource(final LivingEntity attacker) {
      return null;
   }

   public void hurtEnemy(final ItemStack itemStack, final LivingEntity mob, final LivingEntity attacker) {
   }

   public void postHurtEnemy(final ItemStack itemStack, final LivingEntity mob, final LivingEntity attacker) {
   }

   public boolean mineBlock(final ItemStack itemStack, final Level level, final BlockState state, final BlockPos pos, final LivingEntity owner) {
      Tool tool = itemStack.get(DataComponents.TOOL);
      if (tool == null) {
         return false;
      } else {
         if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F && tool.damagePerBlock() > 0) {
            itemStack.hurtAndBreak(tool.damagePerBlock(), owner, EquipmentSlot.MAINHAND);
         }

         return true;
      }
   }

   public boolean isCorrectToolForDrops(final ItemStack itemStack, final BlockState state) {
      Tool tool = itemStack.get(DataComponents.TOOL);
      return tool != null && tool.isCorrectForDrops(state);
   }

   public InteractionResult interactLivingEntity(final ItemStack itemStack, final Player player, final LivingEntity target, final InteractionHand type) {
      return InteractionResult.PASS;
   }

   @Override
   public String toString() {
      return BuiltInRegistries.ITEM.wrapAsHolder(this).getRegisteredName();
   }

   @Nullable
   public final ItemStackTemplate getCraftingRemainder() {
      return this.craftingRemainingItem;
   }

   public void inventoryTick(final ItemStack itemStack, final ServerLevel level, final Entity owner, @Nullable final EquipmentSlot slot) {
   }

   public void onCraftedBy(final ItemStack itemStack, final Player player) {
      this.onCraftedPostProcess(itemStack, player.level());
   }

   public void onCraftedPostProcess(final ItemStack itemStack, final Level level) {
   }

   public ItemUseAnimation getUseAnimation(final ItemStack itemStack) {
      Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
      if (consumable != null) {
         return consumable.animation();
      } else if (itemStack.has(DataComponents.BLOCKS_ATTACKS)) {
         return ItemUseAnimation.BLOCK;
      } else {
         return itemStack.has(DataComponents.KINETIC_WEAPON) ? ItemUseAnimation.SPEAR : ItemUseAnimation.NONE;
      }
   }

   public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
      Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
      if (consumable != null) {
         return consumable.consumeTicks();
      } else {
         return !itemStack.has(DataComponents.BLOCKS_ATTACKS) && !itemStack.has(DataComponents.KINETIC_WEAPON) ? 0 : 72000;
      }
   }

   public boolean releaseUsing(final ItemStack itemStack, final Level level, final LivingEntity entity, final int remainingTime) {
      return false;
   }

   @Deprecated
   public void appendHoverText(
      final ItemStack itemStack,
      final Item$TooltipContext context,
      final TooltipDisplay display,
      final Consumer<Component> builder,
      final TooltipFlag tooltipFlag
   ) {
   }

   public Optional<TooltipComponent> getTooltipImage(final ItemStack itemStack) {
      return Optional.empty();
   }

   @VisibleForTesting
   public final String getDescriptionId() {
      return this.descriptionId;
   }

   public Component getName(final ItemStack itemStack) {
      return itemStack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
   }

   public boolean isFoil(final ItemStack itemStack) {
      return itemStack.isEnchanted();
   }

   protected static BlockHitResult getPlayerPOVHitResult(final Level level, final Player player, final ClipContext$Fluid fluid) {
      Vec3 from = player.getEyePosition();
      Vec3 to = from.add(Player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.blockInteractionRange()));
      return level.clip(new ClipContext(from, to, ClipContext$Block.OUTLINE, fluid, player));
   }

   public boolean useOnRelease(final ItemStack itemStack) {
      return false;
   }

   public ItemStack getDefaultInstance() {
      return new ItemStack(this);
   }

   public boolean canFitInsideContainerItems() {
      return true;
   }

   @Override
   public final FeatureFlagSet requiredFeatures() {
      return this.requiredFeatures;
   }

   public boolean shouldPrintOpWarning(final ItemStack stack, @Nullable final Player player) {
      return false;
   }
}
