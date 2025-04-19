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

    public String getIp() {
        return ip;
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
        return ip + ":" + staff + ":" + time + ":" + reason + ":" + isAuto;
    }
}