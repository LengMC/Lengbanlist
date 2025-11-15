package org.leng.object;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("ReportEntry")
public class ReportEntry implements ConfigurationSerializable {
    private String target;
    private String reporter;
    private String reason;
    private String id;
    private String status;
    private long timestamp; // 添加时间戳字段

    public ReportEntry() {}

    // 保留原有的构造函数
    public ReportEntry(String target, String reporter, String reason, String id) {
        this.target = target;
        this.reporter = reporter;
        this.reason = reason;
        this.id = id;
    }

    // 新增构造函数，支持时间戳
    public ReportEntry(String target, String reporter, String reason, String id, long timestamp, String status) {
        this.target = target;
        this.reporter = reporter;
        this.reason = reason;
        this.id = id;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getTarget() {
        return target;
    }

    public String getReporter() {
        return reporter;
    }

    public String getReason() {
        return reason;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() { // 添加获取时间戳的方法
        return timestamp;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("target", target);
        map.put("reporter", reporter);
        map.put("reason", reason);
        map.put("id", id);
        map.put("status", status);
        map.put("timestamp", timestamp); // 保存时间戳
        return map;
    }

    public static ReportEntry deserialize(Map<String, Object> map) {
        // 从Map中读取时间戳和状态，如果不存在则使用默认值
        long timestamp = map.containsKey("timestamp") ? (long) map.get("timestamp") : System.currentTimeMillis();
        String status = map.containsKey("status") ? (String) map.get("status") : "未处理";

        return new ReportEntry(
                (String) map.get("target"),
                (String) map.get("reporter"),
                (String) map.get("reason"),
                (String) map.get("id"),
                timestamp,
                status
        );
    }
}