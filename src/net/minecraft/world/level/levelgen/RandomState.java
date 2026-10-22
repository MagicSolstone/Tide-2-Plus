package net.minecraft.world.level.levelgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.biome.Climate$Sampler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.densityfunction.DensityBufferPool;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctionCompiler;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DensitySamplerSet;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.material.MaterialSystem;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class RandomState {
   private static final int MAX_BUFFER_POOLS = 16;
   private static final int MAX_BUFFER_AGE_TICKS = 20;
   private final long seed;
   private final PositionalRandomFactory random;
   private final HolderGetter<NormalNoise> noises;
   private final NoiseRouter router;
   private final MaterialSystem materialSystem;
   private final Map<ResourceKey<NormalNoise>, Noise> noiseInstances;
   private final Map<Identifier, PositionalRandomFactory> positionalRandoms;
   private final DensityFunctionCompiler densityFunctionCompiler;
   private final ReentrantLock densityBufferPoolLock = new ReentrantLock();
   private final List<DensityBufferPool> densityBufferPools = new ArrayList<>(16);

   public static RandomState create(final HolderGetter<NormalNoise> noises, final long seed, final NoiseGeneratorSettings settings) {
      return create(noises, seed, settings.useLegacyRandomSource(), settings.defaultBlock(), settings.seaLevel(), settings.noiseRouter());
   }

   public static RandomState create(
      final HolderGetter<NormalNoise> noises,
      final long seed,
      final boolean useLegacyRandom,
      final BlockState defaultBlock,
      final int seaLevel,
      final NoiseRouter noiseRouter
   ) {
      return new RandomState(noises, seed, useLegacyRandom, defaultBlock, seaLevel, noiseRouter);
   }

   private RandomState(
      final HolderGetter<NormalNoise> noises,
      final long seed,
      final boolean useLegacyRandom,
      final BlockState defaultBlock,
      final int seaLevel,
      final NoiseRouter router
   ) {
      WorldgenRandom$Algorithm randomAlgorithm = useLegacyRandom ? WorldgenRandom$Algorithm.LEGACY : WorldgenRandom$Algorithm.XOROSHIRO;
      this.seed = seed;
      this.random = randomAlgorithm.newInstance(seed).forkPositional();
      this.noises = noises;
      this.router = router;
      this.noiseInstances = new ConcurrentHashMap<>();
      this.positionalRandoms = new ConcurrentHashMap<>();
      this.materialSystem = new MaterialSystem(this, defaultBlock, seaLevel, router.chunkSurfaceLevel(), this.random);
      this.densityFunctionCompiler = new DensityFunctionCompiler(new RandomState$1(this, seed, useLegacyRandom));
   }

   public DensitySamplerSet samplersWithContext(final SamplerContext context) {
      return new RandomState$2(this, context);
   }

   public Climate$Sampler createClimateSampler(final SamplerContext context) {
      return this.router.createClimateSampler(this.samplersWithContext(context));
   }

   public Noise getOrCreateNoise(final ResourceKey<NormalNoise> noise) {
      return this.noiseInstances.computeIfAbsent(noise, key -> Noises.instantiate(this.noises, this.random, noise));
   }

   public PositionalRandomFactory getOrCreateRandomFactory(final Identifier name) {
      return this.positionalRandoms.computeIfAbsent(name, key -> this.random.fromHashOf(name).forkPositional());
   }

   public MaterialSystem surfaceSystem() {
      return this.materialSystem;
   }

   public DensitySampler getSampler(final DensityFunction function) {
      return this.densityFunctionCompiler.getSampler(function);
   }

   @VisibleForDebug
   public float sampleBlockValueUncached(final DensityFunction function, final int x, final int y, final int z) {
      return this.getSampler(function).sampleValue(SamplerContext.EMPTY_UNCACHED, x, y, z);
   }

   public DensityBufferPool acquireDensityBufferPool() {
      this.densityBufferPoolLock.lock();

      DensityBufferPool var1;
      try {
         if (!this.densityBufferPools.isEmpty()) {
            return this.densityBufferPools.removeLast();
         }

         var1 = new DensityBufferPool(20);
      } finally {
         this.densityBufferPoolLock.unlock();
      }

      return var1;
   }

   public void releaseDensityBufferPool(final DensityBufferPool pool) {
      this.densityBufferPoolLock.lock();

      try {
         if (this.densityBufferPools.size() < 16) {
            this.densityBufferPools.add(pool);
         }
      } finally {
         this.densityBufferPoolLock.unlock();
      }
   }

   public void garbageCollect() {
      this.densityBufferPoolLock.lock();

      try {
         this.densityBufferPools.removeIf(pool -> {
            pool.garbageCollect();
            return pool.isEmpty();
         });
      } finally {
         this.densityBufferPoolLock.unlock();
      }
   }

   @Deprecated
   public long seed() {
      return this.seed;
   }
}
