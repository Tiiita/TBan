package de.tiiita.report;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

/**
 * Created on Januar 29, 2023 | 14:19:46
 * (●'◡'●)
 */
public class ReportEvent extends Event implements Cancellable {
    private final Report report;
    private boolean cancelled;

    public ReportEvent(Report report) {
        this.report = report;
    }

    public Report getReport() {
        return report;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
