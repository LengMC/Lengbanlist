package org.leng.manager;

import org.leng.Lengbanlist;
import org.leng.object.ReportEntry;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.File;

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
        FileConfiguration reportFC = plugin.getReportFC();
        reportFC.set("reports", null); // 清空旧数据
        for (ReportEntry report : reports.values()) {
            reportFC.set("reports." + report.getId(), report.serialize());
        }
        try {
            reportFC.save(new File(plugin.getDataFolder(), "reports.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadReports() {
        // 从文件加载举报记录
        FileConfiguration reportFC = plugin.getReportFC();
        if (reportFC.contains("reports")) {
            for (String key : reportFC.getConfigurationSection("reports").getKeys(false)) {
                ReportEntry report = ReportEntry.deserialize(reportFC.getConfigurationSection("reports").getConfigurationSection(key).getValues(false));
                if (report != null) {
                    reports.put(report.getId(), report);
                }
            }
        }
    }

    public List<ReportEntry> getPendingReports() {
        return reports.values().stream().filter(report -> report.getStatus() == null || !report.getStatus().equals("已关闭")).toList();
    }

    public int getPendingReportCount() {
        return (int) reports.values().stream().filter(report -> report.getStatus() == null || !report.getStatus().equals("已关闭")).count();
    }
}