package org.leng.object;

public class BanIpEntry {
    private String ip;
    private String staff;
    private long time;
    private String reason;

    public BanIpEntry(String ip, String staff, long time, String reason) {
        this.ip = ip;
        this.staff = staff;
        this.time = time;
        this.reason = reason;
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

    @Override
    public String toString() {
        return ip + ":" + staff + ":" + time + ":" + reason;
    }
}