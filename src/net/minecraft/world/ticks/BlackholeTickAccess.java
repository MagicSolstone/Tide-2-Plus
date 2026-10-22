package net.minecraft.world.ticks;

public class BlackholeTickAccess {
   private static final TickContainerAccess<Object> CONTAINER_BLACKHOLE = new BlackholeTickAccess$1();
   private static final LevelTickAccess<Object> LEVEL_BLACKHOLE = new BlackholeTickAccess$2();

   public static <T> TickContainerAccess<T> emptyContainer() {
      return (TickContainerAccess<T>)CONTAINER_BLACKHOLE;
   }

   public static <T> LevelTickAccess<T> emptyLevelList() {
      return (LevelTickAccess<T>)LEVEL_BLACKHOLE;
   }
}
