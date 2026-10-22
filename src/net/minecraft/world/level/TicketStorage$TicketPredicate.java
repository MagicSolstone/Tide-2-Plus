package net.minecraft.world.level;

import net.minecraft.server.level.Ticket;

public interface TicketStorage$TicketPredicate {
   boolean test(Ticket ticket, long chunkPos);
}
