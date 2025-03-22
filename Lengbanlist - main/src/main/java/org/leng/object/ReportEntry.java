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

    public ReportEntry() {}

    public ReportEntry(String target, String reporter, String reason, String id) {
        this.target = target;
        this.reporter = reporter;
        this.reason = reason;
        this.id = id;
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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("target", target);
        map.put("reporter", reporter);
        map.put("reason", reason);
        map.put("id", id);
        return map;
    }

    public static ReportEntry deserialize(Map<String, Object> map) {
        return new ReportEntry(
                (String) map.get("target"),
                (String) map.get("reporter"),
                (String) map.get("reason"),
                (String) map.get("id")
        );
    }
}