package org.leng.object;

public class BanIpEntry {
    private String ip;
    private String staff;
    private long time;
    private String reason;
    private boolean isAuto;

    public BanIpEntry(String ip, String staff, long time, String reason, boolean isAuto) {
        this.ip = ip;
        this.staff = staff;
        this.time = time;
        this.reason = reason;
        this.isAuto = isAuto;
    }

    // Getters and setters
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        return ip + ":" + staff + ":" + time + ":" + reason + ":" + isAuto;
    }
}