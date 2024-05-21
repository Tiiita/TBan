package de.tiiita.report;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created on Januar 29, 2023 | 14:26:09
 * (●'◡'●)
 */
public class ReportListener implements Listener {

    private final ReportService reportService;

    public ReportListener(ReportService reportService) {
        this.reportService = reportService;
    }

    @EventHandler
    public void onReport(ReportEvent event) {

    }
}
