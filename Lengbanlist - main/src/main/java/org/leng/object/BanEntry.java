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

    public void setTarget(String target) {
        this.target = target;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public long getTime() {
        return time;
    }

    public void setEndTime(long time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    @Override
    public String toString() {
        return target + ":" + staff + ":" + time + ":" + reason + ":" + isAuto;
    }
}