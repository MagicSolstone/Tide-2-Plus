package com.li64.tide.data.commands;

import com.li64.tide.data.angler.Angler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

/**
 * /fishinglevel                          your level, title and stats
 * /fishinglevel get <players>            (op) theirs
 * /fishinglevel set <players> <level>    (op)
 * /fishinglevel addxp <players> <xp>     (op)
 * /fishinglevel recalc <players>        (op) recount XP from the journal
 * /fishinglevel fair <players> <on>      (op) fish at the baseline level regardless (contests)
 */
public class FishingLevelCommand {
    public FishingLevelCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fishinglevel")
                .executes(ctx -> {
                    ServerPlayer p = ctx.getSource().getPlayerOrException();
                    ctx.getSource().sendSuccess(() -> describe(p), false);
                    return Angler.effectiveLevel(p);
                })
                .then(Commands.literal("get").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument("players", EntityArgument.players()).executes(ctx -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
                            for (ServerPlayer p : players) ctx.getSource().sendSuccess(() -> describe(p), false);
                            return players.size();
                        })))
                .then(Commands.literal("set").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, Angler.MAX_LEVEL)).executes(ctx -> {
                                    int level = IntegerArgumentType.getInteger(ctx, "level");
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
                                    for (ServerPlayer p : players) Angler.setLevel(p, level);
                                    ctx.getSource().sendSuccess(() -> Component.literal("Set fishing level " + level
                                            + " for " + players.size() + " player(s)"), true);
                                    return players.size();
                                }))))
                .then(Commands.literal("reset").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument("players", EntityArgument.players()).executes(ctx -> {
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
                                    for (ServerPlayer p : players) Angler.reset(p, false);
                                    ctx.getSource().sendSuccess(() -> Component.literal("Reset the fishing level of " + players.size()
                                            + " player(s) to 1 (journal kept)"), true);
                                    return players.size();
                                })
                                .then(Commands.literal("everything").executes(ctx -> {
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
                                    for (ServerPlayer p : players) Angler.reset(p, true);
                                    ctx.getSource().sendSuccess(() -> Component.literal("Reset " + players.size()
                                            + " player(s) to a fresh start: level 1, empty fish journal and tackle log"), true);
                                    return players.size();
                                }))))
                .then(Commands.literal("addxp").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("xp", LongArgumentType.longArg(-100_000_000L, 100_000_000L)).executes(ctx -> {
                                    long xp = LongArgumentType.getLong(ctx, "xp");
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
                                    for (ServerPlayer p : players) Angler.addXp(p, xp, "");
                                    ctx.getSource().sendSuccess(() -> Component.literal("Gave " + xp + " fishing XP to "
                                            + players.size() + " player(s)"), true);
                                    return players.size();
                                }))))
                .then(Commands.literal("recalc").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument("players", EntityArgument.players()).executes(ctx -> {
                            Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
                            for (ServerPlayer p : players) {
                                long xp = Angler.recalculate(p);
                                ctx.getSource().sendSuccess(() -> Component.literal(p.getScoreboardName() + ": recalculated from the journal, "
                                        + xp + " XP (level " + Angler.levelForXp(xp) + ")"), true);
                            }
                            return players.size();
                        })))
                .then(Commands.literal("fair").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("on", BoolArgumentType.bool()).executes(ctx -> {
                                    boolean on = BoolArgumentType.getBool(ctx, "on");
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
                                    for (ServerPlayer p : players) Angler.setFair(p, on);
                                    ctx.getSource().sendSuccess(() -> Component.literal((on ? "Fair play on" : "Fair play off")
                                            + " for " + players.size() + " player(s)"), true);
                                    return players.size();
                                })))));
    }

    private static Component describe(ServerPlayer p) {
        Angler.Data d = Angler.Data.of(p);
        int level = Angler.levelForXp(d.xp);
        Angler.Stats s = Angler.stats(p);
        String next = level >= Angler.MAX_LEVEL ? "max level" : String.format("%,d XP to level %d", Angler.xpForLevel(level + 1) - d.xp, level + 1);
        return Component.literal("🎣 ").withStyle(ChatFormatting.AQUA)
                .append(p.getDisplayName().copy().withStyle(ChatFormatting.WHITE))
                .append(Component.literal(": fishing level " + level + " — " + Angler.title(level)).withStyle(ChatFormatting.AQUA))
                .append(Component.literal("\n  " + d.xp + " XP, " + next
                        + (d.fair ? "\n  Fair play: fishing at level " + Angler.baseline() : "")
                        + (Angler.enabled() ? "" : "\n  (levels are switched off on this server)")
                        + String.format("\n  vs a normal rod: casting %+d%% · bite speed %+d%% · hook window %+d%% · rare finds %+d%%",
                        Math.round((s.castDistance() - 1) * 100), Math.round((1 / s.biteWait() - 1) * 100),
                        Math.round((s.hookZones() - 1) * 100), Math.round(com.li64.tide.data.rods.RodEffects.rareFinds(s.luck()) * 100))).withStyle(ChatFormatting.GRAY));
    }
}
