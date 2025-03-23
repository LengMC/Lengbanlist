package org.leng.object;

public class BanEntry {
    private String target;
    private String staff;
    private long time;
    private String reason;

    public BanEntry(String target, String staff, long time, String reason) {
        this.target = target;
        this.staff = staff;
        this.time = time;
        this.reason = reason;
    }

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

    @Override
    public String toString() {
        return target + ":" + staff + ":" + time + ":" + reason;
    }
}