package com.li64.tide.data.commands;

import com.li64.tide.Tide;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.fishing.selector.FishingEntry;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FishingCommand {
    public FishingCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(Commands.literal("fishing")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("test")
                        .then(Commands.literal("loot").executes(ctx -> test(ctx.getSource(), TestType.LOOT)))
                        .then(Commands.literal("crate").executes(ctx -> test(ctx.getSource(), TestType.CRATES)))
                        .then(Commands.literal("fish").executes(ctx -> test(ctx.getSource(), TestType.FISH))))
                .then(Commands.literal("describe")
                        .then(Commands.argument("item", net.minecraft.commands.arguments.item.ItemArgument.item(buildContext))
                                .executes(ctx -> describe(ctx.getSource(),
                                        net.minecraft.commands.arguments.item.ItemArgument.getItem(ctx, "item").createItemStack(1))))));
    }

    /**
     * An item's tooltip (everything under its name), for Solstone's item picker. One message so RCON keeps the lines
     * together: "tooltip <id>", then a line each as "RRGGBB<tab>text" in the line's main colour.
     */
    private int describe(CommandSourceStack source, net.minecraft.world.item.ItemStack stack) {
        String out = tooltip(stack, source.getServer().registryAccess());
        source.sendSuccess(() -> Component.literal(out), false);
        return (int) out.chars().filter(ch -> ch == '\n').count();
    }

    public static String tooltip(net.minecraft.world.item.ItemStack stack, net.minecraft.core.HolderLookup.Provider registries) {
        java.util.List<Component> lines = new java.util.ArrayList<>(stack.getTooltipLines(
                net.minecraft.world.item.Item.TooltipContext.of(registries), null,
                net.minecraft.world.item.TooltipFlag.NORMAL));
        if (!lines.isEmpty()) lines.removeFirst();                      // the name
        com.li64.tide.data.item.TideTooltips.add(stack, lines, false);
        StringBuilder out = new StringBuilder("tooltip " + net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()));
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (Component c : lines) {
            String text = c.getString();
            if (text.isBlank() || !seen.add(text)) continue;             // singleplayer also runs the client's tooltip hook
            int[] colour = {0xAAAAAA};
            c.visit((style, part) -> {
                if (part.isBlank()) return java.util.Optional.empty();
                if (style.getColor() != null) colour[0] = style.getColor().getValue();
                return java.util.Optional.of(true);
            }, net.minecraft.network.chat.Style.EMPTY);
            out.append('\n').append(String.format("%06X", colour[0] & 0xFFFFFF)).append('\t').append(text.replace('\n', ' '));
        }
        return out.toString();
    }

    private int test(CommandSourceStack source, TestType type) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        TideFishingHook hook = HookAccessor.getHook(player);
        if (hook == null) {
            source.sendFailure(Component.translatable("commands.fishing.no_hook"));
            return 0;
        }

        Map<FishingEntry, Double> results = Tide.FISHING_MANAGER.test(hook.getContext(), type);
        if (results == null || results.isEmpty()) {
            source.sendFailure(Component.translatable("commands.fishing.nothing_found"));
            return 0;
        }

        double total = results.values().stream().mapToDouble(d -> d).sum();
        List<Map.Entry<FishingEntry, Double>> sortedResults = results.entrySet().stream()
                .sorted(Comparator.comparing(entry -> -entry.getValue())).toList();

        MutableComponent formattedResults = Component.translatable("commands.fishing.results")
                .withStyle(ChatFormatting.GOLD);
        sortedResults.forEach(entry -> {
            formattedResults.append(Component.literal("\n"));
            formattedResults.append(entry.getKey().getTestKey().withStyle(ChatFormatting.WHITE));
            formattedResults.append(Component.literal(" - ").withStyle(ChatFormatting.WHITE));
            String percent = String.format("%.1f", (entry.getValue() / total) * 100.0) + "%";
            formattedResults.append(Component.literal(percent).withStyle(ChatFormatting.AQUA));
        });

        source.sendSuccess(() -> formattedResults, true);
        return 1;
    }
}