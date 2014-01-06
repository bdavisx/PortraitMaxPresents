package org.loosefx.domain.commands;

import org.loosefx.eventsourcing.DomainEvent;

/** Marker interface for commands that are to be handled by a ApplicationCommandHandler. */
public interface ApplicationCommand extends Command {
}
