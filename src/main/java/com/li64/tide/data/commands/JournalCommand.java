package com.li64.tide.data.commands;

import com.li64.tide.Tide;
import com.li64.tide.data.TideData;
import com.li64.tide.data.player.TidePlayerData;
import com.li64.tide.util.TideUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class JournalCommand {
    public JournalCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(Commands.literal("journal").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("reset").executes(ctx -> reset(ctx.getSource())))
                .then(Commands.literal("unlock")
                        .then(Commands.literal("all")
                                .executes(ctx -> unlockAll(ctx.getSource())))
                        .then(Commands.argument("fish", ItemArgument.item(buildContext))
                                .executes(ctx -> unlock(ctx.getSource(), ItemArgument.getItem(ctx, "fish").item().value())))));
    }

    private int unlock(CommandSourceStack source, Item fishItem) {
        ItemStack fish = fishItem.getDefaultInstance();
        if (source.getPlayer() == null) return 0;
        if (!TideUtils.isJournalFish(fish)) {
            source.sendFailure(Component.translatable("commands.journal.unlock.fail", fish.getHoverName().getString()));
            return 0;
        }
        TidePlayerData data = TidePlayerData.getOrCreate(source.getPlayer());
        boolean success = data.unlockFish(BuiltInRegistries.ITEM.wrapAsHolder(fishItem), source.getPlayer());
        if (success) {
            TideUtils.showFishToast(fish, source.getPlayer());
            data.syncTo(source.getPlayer());
            source.sendSuccess(() -> Component.translatable("commands.journal.unlock",
                    fish.getHoverName().getString(), source.getPlayer().getDisplayName()), true);
            return 1;
        }
        source.sendFailure(Component.translatable("commands.journal.unlock.already_unlocked", fish.getDisplayName().getString()));
        return 0;
    }

    private int unlockAll(CommandSourceStack source) {
        if (source.getPlayer() == null) return 0;
        TidePlayerData data = TidePlayerData.getOrCreate(source.getPlayer());
        TideData.FISH.get().values().forEach(fishData ->
                data.unlockFish(fishData.fish(), source.getPlayer()));
        data.syncTo(source.getPlayer());
        source.sendSuccess(() -> Component.translatable("commands.journal.unlock_all",
                source.getPlayer().getDisplayName()), true);
        return 1;
    }

    private int reset(CommandSourceStack source) {
        if (source.getPlayer() == null) return 0;
        try {
            TidePlayerData data = new TidePlayerData();
            data.syncTo(source.getPlayer());
            source.sendSuccess(() -> Component.translatable("commands.journal.lock",
                    source.getPlayer().getDisplayName()), true);
        } catch (Exception e) {
            Tide.LOG.error("Could not reset player data: ", e);
            return 0;
        }

        return 1;
    }
}