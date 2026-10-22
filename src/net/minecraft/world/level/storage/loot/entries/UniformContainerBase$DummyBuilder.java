package net.minecraft.world.level.storage.loot.entries;

class UniformContainerBase$DummyBuilder extends UniformContainerBase$Builder<UniformContainerBase$DummyBuilder> {
   private final UniformContainerBase$EntryConstructor constructor;

   public UniformContainerBase$DummyBuilder(final UniformContainerBase$EntryConstructor constructor) {
      this.constructor = constructor;
   }

   protected UniformContainerBase$DummyBuilder getThis() {
      return this;
   }

   @Override
   public LootPoolEntryContainer build() {
      return this.constructor.build(this.weight, this.quality, this.getCondition(), this.getModifier());
   }
}
