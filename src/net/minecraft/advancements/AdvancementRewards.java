package net.minecraft.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.commands.CacheableFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Prediction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams$Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record AdvancementRewards(int experience, HolderSet<LootTable> loot, List<ResourceKey<Recipe<?>>> recipes, Optional<CacheableFunction> function) {
   public static final Codec<AdvancementRewards> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.INT.optionalFieldOf("experience", 0).forGetter(AdvancementRewards::experience),
            LootTable.LIST_CODEC.optionalFieldOf("loot", HolderSet.empty()).forGetter(AdvancementRewards::loot),
            Recipe.KEY_CODEC.listOf().optionalFieldOf("recipes", List.of()).forGetter(AdvancementRewards::recipes),
            CacheableFunction.CODEC.optionalFieldOf("function").forGetter(AdvancementRewards::function)
         )
         .apply(i, AdvancementRewards::new)
   );
   public static final AdvancementRewards EMPTY = new AdvancementRewards(0, HolderSet.empty(), List.of(), Optional.empty());

   public void grant(final ServerPlayer player) {
      player.giveExperiencePoints(this.experience);
      ServerLevel level = player.level();
      MinecraftServer server = level.getServer();
      LootParams params = new LootParams$Builder(level)
         .withParameter(LootContextParams.THIS_ENTITY, player)
         .withParameter(LootContextParams.ORIGIN, player.position())
         .create(LootContextParamSets.ADVANCEMENT_REWARD);
      boolean changes = false;

      for (Holder<LootTable> lootTable : this.loot) {
         ObjectListIterator var8 = lootTable.value().getRandomItems(params).iterator();

         while (var8.hasNext()) {
            ItemStack itemStack = (ItemStack)var8.next();
            if (player.addItem(itemStack)) {
               level.playSound(
                  null,
                  player.getX(),
                  player.getY(),
                  player.getZ(),
                  SoundEvents.ITEM_PICKUP,
                  SoundSource.PLAYERS,
                  0.2F,
                  ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
               );
               changes = true;
            } else {
               ItemEntity drop = player.drop(itemStack, false, Prediction.SERVER_ONLY);
               if (drop != null) {
                  drop.setNoPickUpDelay();
                  drop.setTarget(player.getUUID());
               }
            }
         }
      }

      if (changes) {
         player.containerMenu.broadcastChanges();
      }

      if (!this.recipes.isEmpty()) {
         player.awardRecipesByKey(this.recipes);
      }

      this.function
         .flatMap(function -> function.get(server.getFunctions()))
         .ifPresent(
            function -> server.getFunctions()
               .execute(
                  (CommandFunction<CommandSourceStack>)function,
                  player.createCommandSourceStack().withSuppressedOutput().withPermission(LevelBasedPermissionSet.GAMEMASTER)
               )
         );
   }
}
