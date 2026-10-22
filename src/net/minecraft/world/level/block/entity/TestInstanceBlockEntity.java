package net.minecraft.world.level.block.entity;

import com.mojang.logging.LogUtils;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.Vec3i;
import net.minecraft.gametest.framework.FailedTestTracker;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.GameTestRunner;
import net.minecraft.gametest.framework.GameTestRunner$Builder;
import net.minecraft.gametest.framework.GameTestTicker;
import net.minecraft.gametest.framework.RetryOptions;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestCommand;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplatePathFactory;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

public class TestInstanceBlockEntity extends BlockEntity implements BoundingBoxRenderable, BeaconBeamOwner {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Component INVALID_TEST_NAME = Component.translatable("test_instance_block.invalid_test");
   private static final List<BeaconBeamOwner$Section> BEAM_CLEARED = List.of();
   private static final List<BeaconBeamOwner$Section> BEAM_RUNNING = List.of(new BeaconBeamOwner$Section(ARGB.color(128, 128, 128)));
   private static final List<BeaconBeamOwner$Section> BEAM_SUCCESS = List.of(new BeaconBeamOwner$Section(ARGB.color(0, 255, 0)));
   private static final List<BeaconBeamOwner$Section> BEAM_REQUIRED_FAILED = List.of(new BeaconBeamOwner$Section(ARGB.color(255, 0, 0)));
   private static final List<BeaconBeamOwner$Section> BEAM_OPTIONAL_FAILED = List.of(new BeaconBeamOwner$Section(ARGB.color(255, 128, 0)));
   private static final Vec3i STRUCTURE_OFFSET = new Vec3i(0, 1, 1);
   private TestInstanceBlockEntity$Data data;
   private final List<TestInstanceBlockEntity$ErrorMarker> errorMarkers = new ArrayList<>();

   public TestInstanceBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
      super(BlockEntityTypes.TEST_INSTANCE_BLOCK, worldPosition, blockState);
      this.data = new TestInstanceBlockEntity$Data(Optional.empty(), Vec3i.ZERO, Rotation.NONE, false, TestInstanceBlockEntity$Status.CLEARED, Optional.empty());
   }

   public void set(final TestInstanceBlockEntity$Data data) {
      this.data = data;
      this.setChanged();
   }

   public static Optional<Vec3i> getStructureSize(final ServerLevel level, final ResourceKey<GameTestInstance> testKey) {
      return getStructureTemplate(level, testKey).map(StructureTemplate::getSize);
   }

   public BoundingBox getStructureBoundingBox() {
      BlockPos corner1 = this.getStructurePos();
      BlockPos corner2 = corner1.offset(this.getTransformedSize()).offset(-1, -1, -1);
      return BoundingBox.fromCorners(corner1, corner2);
   }

   public BoundingBox getTestBoundingBox() {
      return this.getStructureBoundingBox().inflatedBy(this.getPadding());
   }

   public AABB getStructureBounds() {
      return AABB.of(this.getStructureBoundingBox());
   }

   public AABB getTestBounds() {
      return this.getStructureBounds().inflate(this.getPadding());
   }

   private static Optional<StructureTemplate> getStructureTemplate(final ServerLevel level, final ResourceKey<GameTestInstance> testKey) {
      return level.registryAccess().get(testKey).map(test -> test.value().structure()).flatMap(template -> level.getStructureTemplateManager().get(template));
   }

   public Optional<ResourceKey<GameTestInstance>> test() {
      return this.data.test();
   }

   public Component getTestName() {
      return this.test().map(key -> Component.literal(key.identifier().toString())).orElse(INVALID_TEST_NAME);
   }

   private Optional<Holder$Reference<GameTestInstance>> getTestHolder() {
      return this.test().flatMap(this.level.registryAccess()::get);
   }

   public boolean ignoreEntities() {
      return this.data.ignoreEntities();
   }

   public Vec3i getSize() {
      return this.data.size();
   }

   public Rotation getRotation() {
      return this.getTestHolder().map(Holder::value).map(GameTestInstance::rotation).orElse(Rotation.NONE).getRotated(this.data.rotation());
   }

   public Optional<Component> errorMessage() {
      return this.data.errorMessage();
   }

   public void setErrorMessage(final Component errorMessage) {
      this.set(this.data.withError(errorMessage));
   }

   public void setSuccess() {
      this.set(this.data.withStatus(TestInstanceBlockEntity$Status.FINISHED));
   }

   public void setRunning() {
      this.set(this.data.withStatus(TestInstanceBlockEntity$Status.RUNNING));
   }

   @Override
   public void setChanged() {
      super.setChanged();
      if (this.level instanceof ServerLevel) {
         this.level.sendBlockUpdated(this.getBlockPos(), Blocks.AIR.defaultBlockState(), this.getBlockState(), 3);
      }
   }

   public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public CompoundTag getUpdateTag(final HolderLookup$Provider registries) {
      return this.saveCustomOnly(registries);
   }

   @Override
   protected void loadAdditional(final ValueInput input) {
      input.<TestInstanceBlockEntity$Data>read("data", TestInstanceBlockEntity$Data.CODEC).ifPresent(this::set);
      this.errorMarkers.clear();
      this.errorMarkers
         .addAll(input.<List<? extends TestInstanceBlockEntity$ErrorMarker>>read("errors", TestInstanceBlockEntity$ErrorMarker.LIST_CODEC).orElse(List.of()));
   }

   @Override
   protected void saveAdditional(final ValueOutput output) {
      output.store("data", TestInstanceBlockEntity$Data.CODEC, this.data);
      if (!this.errorMarkers.isEmpty()) {
         output.store("errors", TestInstanceBlockEntity$ErrorMarker.LIST_CODEC, this.errorMarkers);
      }
   }

   @Override
   public BoundingBoxRenderable$Mode renderMode() {
      return BoundingBoxRenderable$Mode.BOX;
   }

   public BlockPos getStructurePos() {
      int padding = this.getPadding();
      return getStructurePos(this.getBlockPos().offset(padding, padding, padding));
   }

   public static BlockPos getStructurePos(final BlockPos blockPos) {
      return blockPos.offset(STRUCTURE_OFFSET);
   }

   @Override
   public BoundingBoxRenderable$RenderableBox getRenderableBox() {
      int padding = this.getPadding();
      BlockPos offsetPos = new BlockPos(STRUCTURE_OFFSET.getX() + padding, STRUCTURE_OFFSET.getY() + padding, STRUCTURE_OFFSET.getZ() + padding);
      return new BoundingBoxRenderable$RenderableBox(offsetPos, this.getTransformedSize());
   }

   @Override
   public List<BeaconBeamOwner$Section> getBeamSections() {
      return switch (this.data.status()) {
         case CLEARED -> BEAM_CLEARED;
         case RUNNING -> BEAM_RUNNING;
         case FINISHED -> this.errorMessage().isEmpty()
            ? BEAM_SUCCESS
            : (this.getTestHolder().map(Holder::value).map(GameTestInstance::required).orElse(true) ? BEAM_REQUIRED_FAILED : BEAM_OPTIONAL_FAILED);
      };
   }

   private Vec3i getTransformedSize() {
      Vec3i size = this.getSize();
      Rotation rotation = this.getRotation();
      boolean axesSwitched = rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90;
      int xSize = axesSwitched ? size.getZ() : size.getX();
      int zSize = axesSwitched ? size.getX() : size.getZ();
      return new Vec3i(xSize, size.getY(), zSize);
   }

   public void resetTest(final Consumer<Component> feedbackOutput) {
      this.removeBarriers();
      this.clearErrorMarkers();
      boolean placed = this.placeStructure();
      if (placed) {
         feedbackOutput.accept(Component.translatable("test_instance_block.reset_success", this.getTestName()).withStyle(ChatFormatting.GREEN));
      }

      this.set(this.data.withStatus(TestInstanceBlockEntity$Status.CLEARED));
   }

   public Optional<Identifier> saveTest(final Consumer<Component> feedbackOutput) {
      Optional<Holder$Reference<GameTestInstance>> test = this.getTestHolder();
      Optional<Identifier> identifier;
      if (test.isPresent()) {
         identifier = Optional.of(test.get().value().structure());
      } else {
         identifier = this.test().map(ResourceKey::identifier);
      }

      if (identifier.isEmpty()) {
         BlockPos pos = this.getBlockPos();
         feedbackOutput.accept(
            Component.translatable("test_instance_block.error.unable_to_save", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.RED)
         );
         return identifier;
      } else {
         if (this.level instanceof ServerLevel serverLevel) {
            StructureBlockEntity.saveStructure(
               serverLevel, identifier.get(), this.getStructurePos(), this.getSize(), this.ignoreEntities(), "", true, List.of(Blocks.AIR)
            );
         }

         return identifier;
      }
   }

   public boolean exportTest(final Consumer<Component> feedbackOutput) {
      Optional<Identifier> saved = this.saveTest(feedbackOutput);
      return !saved.isEmpty() && this.level instanceof ServerLevel serverLevel ? export(serverLevel, saved.get(), feedbackOutput) : false;
   }

   public static boolean export(final ServerLevel level, final Identifier structureId, final Consumer<Component> feedbackOutput) {
      StructureTemplateManager structureManager = level.getStructureTemplateManager();
      TemplatePathFactory testTemplatePathFactory = structureManager.testTemplates();
      if (testTemplatePathFactory == null) {
         feedbackOutput.accept(Component.literal("Test structure exporting is disabled").withStyle(ChatFormatting.RED));
         return true;
      } else {
         Optional<StructureTemplate> structureTemplate = structureManager.get(structureId);
         if (structureTemplate.isEmpty()) {
            feedbackOutput.accept(Component.literal("Could not find structure " + structureId).withStyle(ChatFormatting.RED));
            return true;
         } else {
            Path outputFile = testTemplatePathFactory.createAndValidatePathToStructure(structureId, StructureTemplateManager.RESOURCE_TEXT_STRUCTURE_LISTER);

            try {
               StructureTemplateManager.save(outputFile, structureTemplate.get(), true);
            } catch (Exception var8) {
               LOGGER.error("Failed to save structure file {} to {}", new Object[]{structureId, outputFile, var8});
               feedbackOutput.accept(Component.literal("Failed to save structure file " + structureId + " to " + outputFile).withStyle(ChatFormatting.RED));
               return true;
            }

            feedbackOutput.accept(Component.literal("Exported " + structureId + " to " + outputFile.toAbsolutePath()));
            return false;
         }
      }
   }

   public void runTest(final Consumer<Component> feedbackOutput) {
      if (this.level instanceof ServerLevel serverLevel) {
         Optional var7 = this.getTestHolder();
         BlockPos pos = this.getBlockPos();
         if (var7.isEmpty()) {
            feedbackOutput.accept(Component.translatable("test_instance_block.error.no_test", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.RED));
         } else if (!this.placeStructure()) {
            feedbackOutput.accept(
               Component.translatable("test_instance_block.error.no_test_structure", pos.getX(), pos.getY(), pos.getZ()).withStyle(ChatFormatting.RED)
            );
         } else {
            this.clearErrorMarkers();
            GameTestTicker.SINGLETON.clear();
            FailedTestTracker.forgetFailedTests();
            feedbackOutput.accept(Component.translatable("test_instance_block.starting", ((Holder$Reference)var7.get()).getRegisteredName()));
            GameTestInfo gameTestInfo = new GameTestInfo(
               (Holder$Reference<GameTestInstance>)var7.get(), this.data.rotation(), serverLevel, RetryOptions.noRetries()
            );
            gameTestInfo.setTestBlockPos(pos);
            GameTestRunner runner = GameTestRunner$Builder.fromInfo(List.of(gameTestInfo), serverLevel.getServer()).build();
            TestCommand.trackAndStartRunner(serverLevel.getServer().createCommandSourceStack(), runner);
         }
      }
   }

   public boolean placeStructure() {
      if (this.level instanceof ServerLevel serverLevel) {
         Optional<StructureTemplate> template = this.data.test().flatMap(test -> getStructureTemplate(serverLevel, (ResourceKey<GameTestInstance>)test));
         if (template.isPresent()) {
            this.placeStructure(serverLevel, template.get());
            return true;
         }
      }

      return false;
   }

   private void placeStructure(final ServerLevel level, final StructureTemplate template) {
      StructurePlaceSettings placeSettings = new StructurePlaceSettings()
         .setRotation(this.getRotation())
         .setIgnoreEntities(this.data.ignoreEntities())
         .setKnownShape(true);
      BlockPos pos = this.getStartCorner();
      this.forceLoadChunks();
      int padding = this.getPadding();
      StructureUtils.clearSpaceForStructure(this.getTestBoundingBox(), level);
      this.removeEntities();
      template.placeInWorld(level, pos, pos, placeSettings, level.getRandom(), 818);
   }

   private int getPadding() {
      return this.getTestHolder().map(r -> r.value().padding()).orElse(0);
   }

   private void removeEntities() {
      this.level.getEntities(null, this.getTestBounds()).stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::discard);
   }

   private void forceLoadChunks() {
      if (this.level instanceof ServerLevel serverLevel) {
         this.getStructureBoundingBox().intersectingChunks().forEach(pos -> serverLevel.setChunkForced(pos.x(), pos.z(), true));
      }
   }

   public BlockPos getStartCorner() {
      Vec3i structureSize = this.getSize();
      Rotation rotation = this.getRotation();
      BlockPos northWestCorner = this.getStructurePos();

      return switch (rotation) {
         case NONE -> northWestCorner;
         case CLOCKWISE_90 -> northWestCorner.offset(structureSize.getZ() - 1, 0, 0);
         case CLOCKWISE_180 -> northWestCorner.offset(structureSize.getX() - 1, 0, structureSize.getZ() - 1);
         case COUNTERCLOCKWISE_90 -> northWestCorner.offset(0, 0, structureSize.getX() - 1);
      };
   }

   public void encaseStructure() {
      this.processStructureBoundary(blockPos -> {
         if (!this.level.getBlockState(blockPos).is(Blocks.TEST_INSTANCE_BLOCK)) {
            this.level.setBlockAndUpdate(blockPos, Blocks.BARRIER.defaultBlockState());
         }
      });
   }

   public void removeBarriers() {
      this.processStructureBoundary(blockPos -> {
         if (this.level.getBlockState(blockPos).is(Blocks.BARRIER)) {
            this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
         }
      });
   }

   public void processStructureBoundary(final Consumer<BlockPos> action) {
      AABB bounds = this.getStructureBounds();
      boolean hasCeiling = !this.getTestHolder().map(h -> h.value().skyAccess()).orElse(false);
      BlockPos low = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ).offset(-1, -1, -1);
      BlockPos high = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
      BlockPos.betweenClosedStream(low, high)
         .forEach(
            blockPos -> {
               boolean isNonCeilingEdge = blockPos.getX() == low.getX()
                  || blockPos.getX() == high.getX()
                  || blockPos.getZ() == low.getZ()
                  || blockPos.getZ() == high.getZ()
                  || blockPos.getY() == low.getY();
               boolean isCeiling = blockPos.getY() == high.getY();
               if (isNonCeilingEdge || isCeiling && hasCeiling) {
                  action.accept(blockPos);
               }
            }
         );
   }

   public void markError(final BlockPos pos, final Component text) {
      this.errorMarkers.add(new TestInstanceBlockEntity$ErrorMarker(pos, text));
      this.setChanged();
   }

   public void clearErrorMarkers() {
      if (!this.errorMarkers.isEmpty()) {
         this.errorMarkers.clear();
         this.setChanged();
      }
   }

   public List<TestInstanceBlockEntity$ErrorMarker> getErrorMarkers() {
      return this.errorMarkers;
   }
}
