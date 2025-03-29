package org.leng.object;

public class BanEntry {
    private String target;
    private String staff;
    private long time;
    private String reason;
    private boolean isAuto;

    public BanEntry(String target, String staff, long time, String reason, boolean isAuto) {
        this.target = target;
        this.staff = staff;
        this.time = time;
        this.reason = reason;
        this.isAuto = isAuto;
    }

    // Getters and setters
    public String getTarget() {
        return target;
    }

    public String getStaff() {
        return staff;
    }

    public long getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }

    public boolean isAuto() {
        return isAuto;
    }

    @Override
    public String toString() {
        return target + ":" + staff + ":" + time + ":" + reason;
    }
}