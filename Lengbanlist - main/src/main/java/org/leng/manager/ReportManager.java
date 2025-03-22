package org.leng.manager;

import org.leng.Lengbanlist;
import org.leng.object.ReportEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.File;
import java.io.IOException;

public class ReportManager {
    private final Lengbanlist plugin;
    private final Map<String, ReportEntry> reports = new HashMap<>();

    public ReportManager(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    public void addReport(ReportEntry report) {
        reports.put(report.getId(), report);
        saveReports();
    }

    public void removeReport(String id) {
        reports.remove(id);
        saveReports();
    }

    public int getReportCount(String target) {
        return (int) reports.values().stream().filter(report -> report.getTarget().equals(target)).count();
    }

    public ReportEntry getReport(String id) {
        return reports.get(id);
    }

    public void saveReports() {
        // 保存举报记录到文件
        plugin.getReportFC().set("reports", reports.values());
        try {
            plugin.getReportFC().save(new File(plugin.getDataFolder(), "reports.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadReports() {
        // 从文件加载举报记录
        if (plugin.getReportFC().contains("reports")) {
            for (String key : plugin.getReportFC().getConfigurationSection("reports").getKeys(false)) {
                ReportEntry report = plugin.getReportFC().getSerializable(key, ReportEntry.class);
                if (report != null) {
                    reports.put(report.getId(), report);
                }
            }
        }
    }
}