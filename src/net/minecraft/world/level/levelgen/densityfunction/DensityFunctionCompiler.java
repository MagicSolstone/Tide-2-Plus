package net.minecraft.world.level.levelgen.densityfunction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import net.minecraft.world.level.levelgen.densityfunction.op.CacheFunction;

public class DensityFunctionCompiler {
   private final DensityFunction$CompileContext context;
   private final Map<DensityFunction, DensitySampler> samplers = new ConcurrentHashMap<>();
   private final Function<DensityFunction, DensitySampler> optimizeAndCompile = this::optimizeAndCompile;
   private final DfRewriteRule optimizerRule = DfRewriteRule.sequence(new DensityFunctionCompiler$1(this), DfRewriteRule.SLICE_UNIFORM_AXES);
   private final ReentrantLock compileLock = new ReentrantLock();
   private final Map<DensityFunction, DensityFunctionCompiler$PreparedCache> preparedCaches = new HashMap<>();
   private int nextCacheId;

   public DensityFunctionCompiler(final DensityFunction$CompileContext context) {
      this.context = context;
   }

   public DensitySampler getSampler(final DensityFunction function) {
      return this.samplers.computeIfAbsent(function, this.optimizeAndCompile);
   }

   private DensitySampler optimizeAndCompile(final DensityFunction function) {
      this.compileLock.lock();

      DensitySampler var3;
      try {
         DensityFunction optimizedFunction = this.optimizerRule.rewrite(function);
         var3 = optimizedFunction.compileSampler(this.context);
      } finally {
         this.compileLock.unlock();
      }

      return var3;
   }

   private DensityFunction reuseOrPrepareCache(final CacheFunction cache) {
      DensityFunctionCompiler$PreparedCache prepared = this.preparedCaches.get(cache.input());
      if (prepared == null) {
         prepared = this.prepareCache(cache);
         this.preparedCaches.put(cache.input(), prepared);
      }

      return prepared;
   }

   private DensityFunctionCompiler$PreparedCache prepareCache(final CacheFunction cache) {
      int id = this.nextCacheId++;
      DensityFunction input = this.optimizerRule.rewrite(cache.input());
      CachingDensitySampler cachingSampler = new CachingDensitySampler(id, input.compileSampler(this.context));
      return new DensityFunctionCompiler$PreparedCache(id, input.range(), input.domainAxes(), cachingSampler);
   }
}
