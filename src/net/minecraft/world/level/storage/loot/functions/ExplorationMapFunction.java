package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

public class ExplorationMapFunction extends LootItemConditionalFunction {
   public static final Holder<MapDecorationType> DEFAULT_DECORATION = MapDecorationTypes.WOODLAND_MANSION;
   public static final byte DEFAULT_ZOOM = 2;
   public static final int DEFAULT_SEARCH_RADIUS = 50;
   public static final boolean DEFAULT_SKIP_EXISTING = true;
   public static final MapCodec<ExplorationMapFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               RegistryCodecs.holderSet(Registries.STRUCTURE).fieldOf("destination").forGetter(f -> f.destination),
               MapDecorationType.CODEC.optionalFieldOf("decoration", DEFAULT_DECORATION).forGetter(f -> f.mapDecoration),
               Codec.BYTE.optionalFieldOf("zoom", (byte)2).forGetter(f -> f.zoom),
               Codec.INT.optionalFieldOf("search_radius", 50).forGetter(f -> f.searchRadius),
               Codec.BOOL.optionalFieldOf("skip_existing_chunks", true).forGetter(f -> f.skipKnownStructures)
            )
         )
         .apply(i, ExplorationMapFunction::new)
   );
   private final HolderSet<Structure> destination;
   private final Holder<MapDecorationType> mapDecoration;
   private final byte zoom;
   private final int searchRadius;
   private final boolean skipKnownStructures;

   private ExplorationMapFunction(
      final Optional<Holder<LootItemCondition>> condition,
      final HolderSet<Structure> destination,
      final Holder<MapDecorationType> mapDecoration,
      final byte zoom,
      final int searchRadius,
      final boolean skipKnownStructures
   ) {
      super(condition);
      this.destination = destination;
      this.mapDecoration = mapDecoration;
      this.zoom = zoom;
      this.searchRadius = searchRadius;
      this.skipKnownStructures = skipKnownStructures;
   }

   @Override
   public MapCodec<ExplorationMapFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public Set<ContextKey<?>> getReferencedContextParams() {
      return Set.of(LootContextParams.ORIGIN);
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      if (itemStack.isEmpty()) {
         return itemStack;
      } else {
         Vec3 lootPos = context.getOptional(LootContextParams.ORIGIN);
         if (lootPos == null) {
            return itemStack;
         } else {
            ServerLevel level = context.getLevel();
            BlockPos lootBlockPos = BlockPos.containing(lootPos);
            if (this.skipKnownStructures) {
               this.claimStructureAt(level, lootBlockPos);
            }

            BlockPos nearestMapStructure = level.findNearestMapStructure(this.destination, lootBlockPos, this.searchRadius, this.skipKnownStructures);
            if (nearestMapStructure == null) {
               return itemStack;
            } else {
               MapItem.applyNewSavedData(level, itemStack, nearestMapStructure.getX(), nearestMapStructure.getZ(), this.zoom, true, true);
               MapItem.renderBiomePreviewMap(level, itemStack);
               MapItemSavedData.addTargetDecoration(itemStack, nearestMapStructure, "+", this.mapDecoration);
               return itemStack;
            }
         }
      }
   }

   private void claimStructureAt(final ServerLevel level, final BlockPos pos) {
      StructureManager structureManager = level.structureManager();
      StructureStart start = structureManager.getStructureAt(pos, this.destination);
      if (start.isValid() && start.canBeReferenced()) {
         structureManager.addReference(start);
      }
   }

   public static ExplorationMapFunction$Builder makeExplorationMap(final HolderSet<Structure> destination) {
      return new ExplorationMapFunction$Builder(destination);
   }
}
