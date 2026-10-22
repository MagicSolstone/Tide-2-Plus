package net.minecraft.client.resources.model;

import com.mojang.logging.LogUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel$UnbakedRoot;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ClientItem$Properties;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModel$BakingContext;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.thread.ParallelMapTransform;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ModelBakery {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final SpriteId FIRE_0 = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("fire_0");
   public static final SpriteId FIRE_1 = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("fire_1");
   private static final int DESTROY_STAGE_COUNT = 10;
   private static final List<Identifier> DESTROY_STAGES = IntStream.range(0, 10)
      .mapToObj(i -> Identifier.withDefaultNamespace("block/destroy_stage_" + i))
      .collect(Collectors.toList());
   private static final List<Identifier> DESTROY_STAGES_OIT = IntStream.range(0, 10)
      .mapToObj(i -> Identifier.withDefaultNamespace("block/destroy_oit_stage_" + i))
      .collect(Collectors.toList());
   private static final List<Identifier> BREAKING_LOCATIONS = DESTROY_STAGES.stream()
      .map(location -> location.withPath(path -> "textures/" + path + ".png"))
      .collect(Collectors.toList());
   private static final List<Identifier> BREAKING_LOCATIONS_OIT = DESTROY_STAGES_OIT.stream()
      .map(location -> location.withPath(path -> "textures/" + path + ".png"))
      .collect(Collectors.toList());
   public static final List<RenderType> DESTROY_TYPES = BREAKING_LOCATIONS.stream().map(RenderTypes::crumbling).collect(Collectors.toList());
   public static final List<RenderType> DESTROY_TYPES_OIT = BREAKING_LOCATIONS_OIT.stream().map(RenderTypes::crumbling).collect(Collectors.toList());
   private static final Matrix4fc IDENTITY = new Matrix4f();
   private final EntityModelSet entityModelSet;
   private final SpriteGetter sprites;
   private final PlayerSkinRenderCache playerSkinRenderCache;
   private final Map<BlockState, BlockStateModel$UnbakedRoot> unbakedBlockStateModels;
   private final Map<Identifier, ClientItem> clientInfos;
   private final Map<Identifier, ResolvedModel> resolvedModels;
   private final ResolvedModel missingModel;

   public ModelBakery(
      final EntityModelSet entityModelSet,
      final SpriteGetter sprites,
      final PlayerSkinRenderCache playerSkinRenderCache,
      final Map<BlockState, BlockStateModel$UnbakedRoot> unbakedBlockStateModels,
      final Map<Identifier, ClientItem> clientInfos,
      final Map<Identifier, ResolvedModel> resolvedModels,
      final ResolvedModel missingModel
   ) {
      this.entityModelSet = entityModelSet;
      this.sprites = sprites;
      this.playerSkinRenderCache = playerSkinRenderCache;
      this.unbakedBlockStateModels = unbakedBlockStateModels;
      this.clientInfos = clientInfos;
      this.resolvedModels = resolvedModels;
      this.missingModel = missingModel;
   }

   public CompletableFuture<ModelBakery$BakingResult> bakeModels(final MaterialBaker materials, final Executor taskExecutor) {
      ModelBakery$InternerImpl interner = new ModelBakery$InternerImpl();
      ModelBakery$MissingModels missingModels = ModelBakery$MissingModels.bake(this.missingModel, materials, interner);
      ModelBakery$ModelBakerImpl baker = new ModelBakery$ModelBakerImpl(this, materials, interner, missingModels);
      CompletableFuture<Map<BlockState, BlockStateModel>> bakedBlockStateModelFuture = ParallelMapTransform.schedule(
         this.unbakedBlockStateModels, (blockState, model) -> {
            try {
               return model.bake(blockState, baker);
            } catch (Exception var4x) {
               LOGGER.warn("Unable to bake model: '{}'", blockState, var4x);
               return null;
            }
         }, taskExecutor
      );
      CompletableFuture<Map<Identifier, ItemModel>> bakedItemStackModelFuture = ParallelMapTransform.schedule(
         this.clientInfos,
         (location, clientInfo) -> {
            try {
               return clientInfo.model()
                  .bake(
                     new ItemModel$BakingContext(
                        baker, this.entityModelSet, this.sprites, this.playerSkinRenderCache, missingModels.item, clientInfo.registrySwapper()
                     ),
                     IDENTITY
                  );
            } catch (Exception var6x) {
               LOGGER.warn("Unable to bake item model: '{}'", location, var6x);
               return null;
            }
         },
         taskExecutor
      );
      Map<Identifier, ClientItem$Properties> itemStackModelProperties = new HashMap<>(this.clientInfos.size());
      this.clientInfos.forEach((id, clientInfo) -> {
         ClientItem$Properties properties = clientInfo.properties();
         if (!properties.equals(ClientItem$Properties.DEFAULT)) {
            itemStackModelProperties.put(id, properties);
         }
      });
      return bakedBlockStateModelFuture.thenCombine(
         bakedItemStackModelFuture,
         (bakedBlockStateModels, bakedItemStateModels) -> new ModelBakery$BakingResult(
            missingModels, (Map<BlockState, BlockStateModel>)bakedBlockStateModels, (Map<Identifier, ItemModel>)bakedItemStateModels, itemStackModelProperties
         )
      );
   }
}
