package net.minecraft.world.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Objects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

class GrindstoneMenu$4 extends Slot {
   GrindstoneMenu$4(final GrindstoneMenu this$0, final Container container, final int slot, final int x, final int y, final ContainerLevelAccess val$access) {
      this.val$access = val$accessx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(container, slot, x, y);
   }

   @Override
   public boolean mayPlace(final ItemStack itemStack) {
      return false;
   }

   @Override
   public void onTake(final Player player, final ItemStack carried) {
      this.val$access.execute((level, pos) -> {
         if (level instanceof ServerLevel serverLevel) {
            ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), this.getExperienceAmount(level));
         }

         level.levelEvent(1042, pos, 0);
      });
      this.this$0.repairSlots.setItem(0, ItemStack.EMPTY);
      this.this$0.repairSlots.setItem(1, ItemStack.EMPTY);
   }

   private int getExperienceAmount(final Level level) {
      int amount = 0;
      amount += this.getExperienceFromItem(this.this$0.repairSlots.getItem(0));
      amount += this.getExperienceFromItem(this.this$0.repairSlots.getItem(1));
      if (amount > 0) {
         int halfAmount = (int)Math.ceil(amount / 2.0);
         return halfAmount + level.getRandom().nextInt(halfAmount);
      } else {
         return 0;
      }
   }

   private int getExperienceFromItem(final ItemStack item) {
      int amount = 0;
      ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(item);

      for (Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
         Holder<Enchantment> enchant = (Holder<Enchantment>)entry.getKey();
         int lvl = entry.getIntValue();
         if (!enchant.is(EnchantmentTags.CURSE)) {
            amount += enchant.value().getMinCost(lvl);
         }
      }

      return amount;
   }
}
