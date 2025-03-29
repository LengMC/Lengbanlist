package org.leng.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.leng.Lengbanlist;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class GitHubUpdateChecker {
    // 默认的 GitHub API 地址
    private static final String GITHUB_API_URL = "https://api.github.com/repos/LengMC/Lengbanlist/releases/latest";

    // 静态的备用 API 地址列表（选择响应时间较短的节点）
    private static final List<String> STATIC_API_URLS = Arrays.asList(
            "https://ghproxy.cxkpro.top/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://ghproxy.monkeyray.net/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://ghfile.geekertao.top/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://gh.zwy.me/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://ghp.keleyaa.com/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://gitproxy.click/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://gh.monlor.com/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://ghproxy.cc/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://gh-proxy.ygxz.in/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://github.ednovas.xyz/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest"
    );

    // 超时时间（毫秒）
    private static final int TIMEOUT = 10000; // 10 秒超时

    /**
     * 获取最新版本号
     *
     * @return 最新版本号
     * @throws Exception 如果所有 API 都请求失败
     */
    public static String getLatestReleaseVersion() throws Exception {
        // 遍历所有 API 地址，尝试获取版本号
        for (String apiUrl : STATIC_API_URLS) {
            try {
                return fetchVersionFromUrl(apiUrl);
            } catch (Exception e) {
                Lengbanlist.getInstance().getLogger().warning("哇呜，当前 API 请求失败: " + apiUrl + "，喵喵正在尝试下一个备用 API...");
            }
        }
        throw new Exception("喵喵：所有 API 请求均失败，无法获取最新版本号");
    }

    /**
     * 从指定 URL 获取版本号
     *
     * @param url API 地址
     * @return 版本号
     * @throws Exception 如果请求失败
     */
    private static String fetchVersionFromUrl(String url) throws Exception {
        int retryCount = 3; // 重试次数
        Exception lastException = null;

        for (int i = 0; i < retryCount; i++) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                connection.setConnectTimeout(TIMEOUT);
                connection.setReadTimeout(TIMEOUT);

                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    StringBuilder response = new StringBuilder();
                    int data = reader.read();
                    while (data != -1) {
                        response.append((char) data);
                        data = reader.read();
                    }
                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    return jsonResponse.get("tag_name").getAsString();
                }
            } catch (Exception e) {
                lastException = e;
                Lengbanlist.getInstance().getLogger().warning("喵总请求失败，重试中... (" + (i + 1) + "/" + retryCount + ")");
                Thread.sleep(2000); // 等待 2 秒后重试
            }
        }

        throw lastException; // 如果所有重试都失败，抛出最后一次异常
    }

    /**
     * 比较两个版本号的大小
     *
     * @param version1 版本号1
     * @param version2 版本号2
     * @return 如果 version1 小于 version2，返回 -1；如果 version1 等于 version2，返回 0；如果 version1 大于 version2，返回 1
     */
    private static int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int maxLength = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLength; i++) {
            int v1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int v2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;

            if (v1 < v2) {
                return -1;
            } else if (v1 > v2) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 检查是否有更新
     *
     * @param localVersion 当前插件版本
     * @return 是否有更新
     * @throws Exception 如果请求失败
     */
    public static boolean isUpdateAvailable(String localVersion) throws Exception {
        String latestVersion = getLatestReleaseVersion();
        return compareVersions(localVersion, latestVersion) < 0; // 当前版本小于最新版本时才返回 true
    }

    /**
     * 检查更新并输出日志
     */
    public static void checkUpdate() {
        try {
            String localVersion = Lengbanlist.getInstance().getDescription().getVersion();
            if (isUpdateAvailable(localVersion)) {
                // 创建主消息组件
                TextComponent mainMessage = new TextComponent("§a喵喵发现有新版本可用，当前版本：§e" + localVersion + "§a，最新版本：§e" + getLatestReleaseVersion() + "§a 请前往: §bhttps://github.com/LengMC/Lengbanlist/releases");

                // 创建点击组件
                TextComponent clickableComponent = new TextComponent("§f【§b点击前往喵~§f】");
                clickableComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/LengMC/Lengbanlist/releases"));
                clickableComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a点击打开更新页面喵~").create()));

                // 将 TextComponent 转换为字符串并输出日志
                String logMessage = mainMessage.toLegacyText() + " " + clickableComponent.toLegacyText();
                Lengbanlist.getInstance().getLogger().info(logMessage);
            } else {
                Lengbanlist.getInstance().getLogger().info("哇塞，喵呜现在是最新版本！QwQ");
            }
        } catch (Exception e) {
            Lengbanlist.getInstance().getLogger().warning("检测更新时出错: " + e.getMessage());
            e.printStackTrace(); // 打印完整的异常堆栈
        }
    }
}