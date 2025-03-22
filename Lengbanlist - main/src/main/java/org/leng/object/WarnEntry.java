package org.leng.object;

public class WarnEntry {
    private String player; // 被警告的玩家
    private String staff;  // 执行警告的管理员
    private long time;     // 警告时间
    private String reason; // 警告原因

    public WarnEntry(String player, String staff, long time, String reason) {
        this.player = player;
        this.staff = staff;
        this.time = time;
        this.reason = reason;
    }

    public String getPlayer() {
        return player;
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

    public void setReason(String reason) {
        this.reason = reason;
    }
}