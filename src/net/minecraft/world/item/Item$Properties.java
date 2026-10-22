package net.minecraft.world.item;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentInitializers$Initializer;
import net.minecraft.core.component.DataComponentInitializers$SingleComponentInitializer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.VillagerFood;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.item.component.BrewingFuel;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.KineticWeapon$Condition;
import net.minecraft.world.item.component.MobVisibility;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import org.jspecify.annotations.Nullable;

public class Item$Properties {
   private static final DependantName<Item, String> BLOCK_DESCRIPTION_ID = id -> Util.makeDescriptionId("block", id.identifier());
   private static final DependantName<Item, String> ITEM_DESCRIPTION_ID = id -> Util.makeDescriptionId("item", id.identifier());
   private DataComponentInitializers$Initializer<Item> componentInitializer = (builder, var1, var2) -> builder.addAll(DataComponents.COMMON_ITEM_COMPONENTS);
   @Nullable
   private ItemStackTemplate craftingRemainingItem;
   private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
   @Nullable
   private ResourceKey<Item> id;
   private DependantName<Item, String> descriptionId = ITEM_DESCRIPTION_ID;
   private final DependantName<Item, Identifier> model = ResourceKey::identifier;

   public Item$Properties food(final FoodProperties foodProperties) {
      return this.food(foodProperties, Consumables.DEFAULT_FOOD);
   }

   public Item$Properties food(final FoodProperties foodProperties, final Consumable consumable) {
      return this.component(DataComponents.FOOD, foodProperties).component(DataComponents.CONSUMABLE, consumable);
   }

   public Item$Properties villagerFood(final int nutrition) {
      return this.component(DataComponents.VILLAGER_FOOD, new VillagerFood(nutrition));
   }

   public Item$Properties usingConvertsTo(final Item item) {
      return this.component(DataComponents.USE_REMAINDER, new UseRemainder(new ItemStackTemplate(item)));
   }

   public Item$Properties useCooldown(final float seconds) {
      return this.component(DataComponents.USE_COOLDOWN, new UseCooldown(seconds));
   }

   public Item$Properties stacksTo(final int max) {
      return this.component(DataComponents.MAX_STACK_SIZE, max);
   }

   public Item$Properties durability(final int maxDamage) {
      this.component(DataComponents.MAX_DAMAGE, maxDamage);
      this.component(DataComponents.MAX_STACK_SIZE, 1);
      this.component(DataComponents.DAMAGE, 0);
      return this;
   }

   public Item$Properties craftRemainder(final Item craftingRemainingItem) {
      return this.craftRemainder(new ItemStackTemplate(craftingRemainingItem));
   }

   public Item$Properties craftRemainder(final ItemStackTemplate craftingRemainingItem) {
      this.craftingRemainingItem = craftingRemainingItem;
      return this;
   }

   public Item$Properties rarity(final Rarity rarity) {
      return this.component(DataComponents.RARITY, rarity);
   }

   public Item$Properties fireResistant() {
      return this.delayedComponent(DataComponents.DAMAGE_RESISTANT, context -> new DamageResistant(context.getOrThrow(DamageTypeTags.IS_FIRE)));
   }

   public Item$Properties jukeboxPlayable(final ResourceKey<JukeboxSong> song) {
      return this.delayedComponent(DataComponents.JUKEBOX_PLAYABLE, context -> new JukeboxPlayable(context.getOrThrow(song)));
   }

   public Item$Properties enchantable(final int value) {
      return this.component(DataComponents.ENCHANTABLE, new Enchantable(value));
   }

   public Item$Properties potPattern(final ResourceKey<DecoratedPotPattern> pattern) {
      return this.delayedComponent(DataComponents.PROVIDES_POTTERY_PATTERN, context -> context.getOrThrow(pattern));
   }

   public Item$Properties repairable(final Item repairItem) {
      return this.component(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(repairItem.builtInRegistryHolder())));
   }

   public Item$Properties repairable(final TagKey<Item> repairItems) {
      HolderGetter<Item> registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
      return this.component(DataComponents.REPAIRABLE, new Repairable(registrationLookup.getOrThrow(repairItems)));
   }

   public Item$Properties equippable(final EquipmentSlot slot) {
      return this.component(DataComponents.EQUIPPABLE, Equippable.builder(slot).build());
   }

   public Item$Properties equippableUnswappable(final EquipmentSlot slot) {
      return this.component(DataComponents.EQUIPPABLE, Equippable.builder(slot).setSwappable(false).build());
   }

   public Item$Properties tool(
      final ToolMaterial material,
      final TagKey<Block> minesEfficiently,
      final float attackDamageBaseline,
      final float attackSpeedBaseline,
      final float disableBlockingSeconds
   ) {
      return material.applyToolProperties(this, minesEfficiently, attackDamageBaseline, attackSpeedBaseline, disableBlockingSeconds);
   }

   public Item$Properties pickaxe(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
      return this.tool(material, BlockTags.MINEABLE_WITH_PICKAXE, attackDamageBaseline, attackSpeedBaseline, 0.0F);
   }

   public Item$Properties axe(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
      return this.tool(material, BlockTags.MINEABLE_WITH_AXE, attackDamageBaseline, attackSpeedBaseline, 5.0F)
         .delayedComponent(DataComponents.BLOCK_TRANSFORMER, context -> context.getOrThrow(BlockTransformers.AXE));
   }

   public Item$Properties hoe(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
      return this.tool(material, BlockTags.MINEABLE_WITH_HOE, attackDamageBaseline, attackSpeedBaseline, 0.0F)
         .delayedComponent(DataComponents.BLOCK_TRANSFORMER, context -> context.getOrThrow(BlockTransformers.HOE));
   }

   public Item$Properties shovel(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
      return this.tool(material, BlockTags.MINEABLE_WITH_SHOVEL, attackDamageBaseline, attackSpeedBaseline, 0.0F)
         .delayedComponent(DataComponents.BLOCK_TRANSFORMER, context -> context.getOrThrow(BlockTransformers.SHOVEL));
   }

   public Item$Properties sword(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
      return material.applySwordProperties(this, attackDamageBaseline, attackSpeedBaseline);
   }

   public Item$Properties spear(
      final ToolMaterial material,
      final float attackDuration,
      final float damageMultiplier,
      final float delay,
      final float dismountTime,
      final float dismountThreshold,
      final float knockbackTime,
      final float knockbackThreshold,
      final float damageTime,
      final float damageThreshold
   ) {
      return this.durability(material.durability())
         .repairable(material.repairItems())
         .enchantable(material.enchantmentValue())
         .delayedHolderComponent(DataComponents.DAMAGE_TYPE, DamageTypes.SPEAR)
         .component(
            DataComponents.KINETIC_WEAPON,
            new KineticWeapon(
               10,
               (int)(delay * 20.0F),
               KineticWeapon$Condition.ofAttackerSpeed((int)(dismountTime * 20.0F), dismountThreshold),
               KineticWeapon$Condition.ofAttackerSpeed((int)(knockbackTime * 20.0F), knockbackThreshold),
               KineticWeapon$Condition.ofRelativeSpeed((int)(damageTime * 20.0F), damageThreshold),
               0.38F,
               damageMultiplier,
               Optional.of(material == ToolMaterial.WOOD ? SoundEvents.SPEAR_WOOD_USE : SoundEvents.SPEAR_USE),
               Optional.of(material == ToolMaterial.WOOD ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)
            )
         )
         .component(
            DataComponents.PIERCING_WEAPON,
            new PiercingWeapon(
               true,
               false,
               Optional.of(material == ToolMaterial.WOOD ? SoundEvents.SPEAR_WOOD_ATTACK : SoundEvents.SPEAR_ATTACK),
               Optional.of(material == ToolMaterial.WOOD ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)
            )
         )
         .component(DataComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
         .component(DataComponents.MINIMUM_ATTACK_CHARGE, 1.0F)
         .component(DataComponents.ATTACK_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(attackDuration * 20.0F)))
         .attributes(
            ItemAttributeModifiers.builder()
               .add(
                  Attributes.ATTACK_DAMAGE,
                  new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 0.0F + material.attackDamageBonus(), AttributeModifier$Operation.ADD_VALUE),
                  EquipmentSlotGroup.MAINHAND
               )
               .add(
                  Attributes.ATTACK_SPEED,
                  new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 1.0F / attackDuration - 4.0, AttributeModifier$Operation.ADD_VALUE),
                  EquipmentSlotGroup.MAINHAND
               )
               .build()
         )
         .component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
         .component(DataComponents.WEAPON, new Weapon(1));
   }

   public Item$Properties spawnEgg(final EntityType<?> type) {
      return this.component(DataComponents.ENTITY_DATA, TypedEntityData.of(type, new CompoundTag())).requiredFeatures(type.requiredFeatures());
   }

   public Item$Properties humanoidArmor(final ArmorMaterial material, final ArmorType type) {
      return this.durability(type.getDurability(material.durability()))
         .attributes(material.createAttributes(type))
         .enchantable(material.enchantmentValue())
         .component(DataComponents.EQUIPPABLE, Equippable.builder(type.getSlot()).setEquipSound(material.equipSound()).setAsset(material.assetId()).build())
         .repairable(material.repairIngredient());
   }

   public Item$Properties wolfArmor(final ArmorMaterial material) {
      return this.durability(ArmorType.BODY.getDurability(material.durability()))
         .attributes(material.createAttributes(ArmorType.BODY))
         .repairable(material.repairIngredient())
         .component(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.BODY)
               .setEquipSound(material.equipSound())
               .setAsset(material.assetId())
               .setAllowedEntities(HolderSet.direct(EntityTypes.WOLF.builtInRegistryHolder()))
               .setCanBeSheared(true)
               .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_UNEQUIP_WOLF))
               .build()
         )
         .component(DataComponents.BREAK_SOUND, SoundEvents.WOLF_ARMOR_BREAK)
         .stacksTo(1);
   }

   public Item$Properties horseArmor(final ArmorMaterial material) {
      HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
      return this.attributes(material.createAttributes(ArmorType.BODY))
         .component(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.BODY)
               .setEquipSound(SoundEvents.HORSE_ARMOR)
               .setAsset(material.assetId())
               .setAllowedEntities(entityGetter.getOrThrow(EntityTypeTags.CAN_WEAR_HORSE_ARMOR))
               .setDamageOnHurt(false)
               .setCanBeSheared(true)
               .setShearingSound(SoundEvents.HORSE_ARMOR_UNEQUIP)
               .build()
         )
         .stacksTo(1);
   }

   public Item$Properties nautilusArmor(final ArmorMaterial material) {
      HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
      return this.attributes(material.createAttributes(ArmorType.BODY))
         .component(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.BODY)
               .setEquipSound(SoundEvents.ARMOR_EQUIP_NAUTILUS)
               .setAsset(material.assetId())
               .setAllowedEntities(entityGetter.getOrThrow(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR))
               .setDamageOnHurt(false)
               .setEquipOnInteract(true)
               .setCanBeSheared(true)
               .setShearingSound(SoundEvents.ARMOR_UNEQUIP_NAUTILUS)
               .build()
         )
         .stacksTo(1);
   }

   public Item$Properties trimMaterial(final ResourceKey<TrimMaterial> material) {
      return this.delayedHolderComponent(DataComponents.PROVIDES_TRIM_MATERIAL, material);
   }

   public Item$Properties signText() {
      return this.component(DataComponents.SIGN_TEXT_BACK, SignText.EMPTY).component(DataComponents.SIGN_TEXT_FRONT, SignText.EMPTY);
   }

   public Item$Properties cookingFuel(final ResourceKey<ContextIntProvider> burnTime) {
      return this.component(DataComponents.COOKING_FUEL, new CookingFuel(burnTime, ContextFloatProviders.COOKING_DEFAULT_SPEED_MULTIPLIER));
   }

   public Item$Properties brewingFuel(final ResourceKey<ContextIntProvider> uses) {
      return this.component(DataComponents.BREWING_FUEL, new BrewingFuel(uses, ContextFloatProviders.BREWING_DEFAULT_SPEED_MULTIPLIER));
   }

   public Item$Properties requiredFeatures(final FeatureFlag... flags) {
      this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
      return this;
   }

   public Item$Properties requiredFeatures(final FeatureFlagSet flags) {
      if (!FeatureFlags.REGISTRY.isSubset(flags)) {
         throw new IllegalArgumentException("Mismatched flag sets");
      } else {
         this.requiredFeatures = flags;
         return this;
      }
   }

   public Item$Properties setId(final ResourceKey<Item> id) {
      this.id = id;
      return this;
   }

   public Item$Properties overrideDescription(final String descriptionId) {
      this.descriptionId = DependantName.fixed(descriptionId);
      return this;
   }

   public Item$Properties useBlockDescriptionPrefix() {
      this.descriptionId = BLOCK_DESCRIPTION_ID;
      return this;
   }

   public Item$Properties useItemDescriptionPrefix() {
      this.descriptionId = ITEM_DESCRIPTION_ID;
      return this;
   }

   private ResourceKey<Item> itemIdOrThrow() {
      return Objects.requireNonNull(this.id, "Item id not set");
   }

   protected String effectiveDescriptionId() {
      return this.descriptionId.get(this.itemIdOrThrow());
   }

   public Identifier effectiveModel() {
      return this.model.get(this.itemIdOrThrow());
   }

   public <T> Item$Properties component(final DataComponentType<T> type, final T value) {
      this.componentInitializer = this.componentInitializer.add(type, value);
      return this;
   }

   public <T> Item$Properties delayedComponent(final DataComponentType<T> type, final DataComponentInitializers$SingleComponentInitializer<T> initializer) {
      this.componentInitializer = this.componentInitializer.andThen(initializer.asInitializer(type));
      return this;
   }

   public <T> Item$Properties delayedHolderComponent(final DataComponentType<Holder<T>> type, final ResourceKey<T> valueKey) {
      this.componentInitializer = this.componentInitializer.andThen((components, context, var4) -> components.set(type, context.getOrThrow(valueKey)));
      return this;
   }

   public Item$Properties attributes(final ItemAttributeModifiers attributes) {
      return this.component(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
   }

   public Item$Properties compostable(final ResourceKey<ContextIntProvider> layers) {
      return this.component(DataComponents.COMPOSTABLE, new Compostable(layers));
   }

   @SafeVarargs
   public final Item$Properties loweredMobVisibility(final ResourceKey<EntityType<?>>... entityTypeIds) {
      return this.delayedComponent(DataComponents.MOB_VISIBILITY, context -> {
         HolderGetter<EntityType<?>> registry = context.lookupOrThrow(Registries.ENTITY_TYPE);
         List<Holder$Reference<EntityType<?>>> entityTypes = Arrays.stream(entityTypeIds).map(registry::getOrThrow).toList();
         return new MobVisibility(HolderSet.direct(entityTypes), 0.5F);
      });
   }

   private DataComponentInitializers$Initializer<Item> finalizeInitializer(final Component name, final Identifier model) {
      return this.componentInitializer
         .andThen((components, var3, var4) -> components.set(DataComponents.ITEM_NAME, name).set(DataComponents.ITEM_MODEL, model).addValidator(c -> {
            if (c.has(DataComponents.DAMAGE) && c.getOrDefault(DataComponents.MAX_STACK_SIZE, 1) > 1) {
               throw new IllegalStateException("Item cannot have both durability and be stackable");
            }
         }));
   }
}
