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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitHubUpdateChecker {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/LengMC/Lengbanlist/releases/latest";
    private static final List<String> STATIC_API_URLS = Arrays.asList(
            "https://ghproxy.net/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://ghproxy.cc/https://api.github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://gitproxy.click/https://github.com/repos/LengMC/Lengbanlist/releases/latest",
            "https://github.proxy.class3.fun/https://github.com/repos/LengMC/Lengbanlist/releases/latest"
    );
    private static final String DYNAMIC_API_SOURCE = "https://github.akams.cn/";
    private static final int TIMEOUT = 10000;

    /**
     * 获取最新版本号
     */
    public static String getLatestReleaseVersion() throws Exception {
        List<String> dynamicApiUrls = fetchDynamicApiUrls();
        List<String> allApiUrls = new ArrayList<>(STATIC_API_URLS);
        allApiUrls.addAll(dynamicApiUrls);

        for (String apiUrl : allApiUrls) {
            try {
                JsonObject jsonResponse = fetchJsonFromUrl(apiUrl);
                return jsonResponse.get("tag_name").getAsString(); // 提取 tag_name 字段
            } catch (Exception e) {
                Lengbanlist.getInstance().getLogger().warning("API 请求失败: " + apiUrl + "，尝试下一个备用 API...");
            }
        }
        throw new Exception("所有 API 请求均失败，无法获取最新版本号");
    }

    /**
     * 从动态 API 源获取可用的 API 地址列表
     */
    private static List<String> fetchDynamicApiUrls() {
        List<String> dynamicApiUrls = new ArrayList<>();
        try {
            JsonObject jsonResponse = fetchJsonFromUrl(DYNAMIC_API_SOURCE);
            // 假设返回的 JSON 中包含一个数组字段 "urls"
            if (jsonResponse.has("urls")) {
                jsonResponse.getAsJsonArray("urls").forEach(url -> {
                    dynamicApiUrls.add(url.getAsString() + "/repos/LengMC/Lengbanlist/releases/latest");
                });
            }
        } catch (Exception e) {
            Lengbanlist.getInstance().getLogger().warning("无法从动态 API 源获取可用地址: " + e.getMessage());
        }
        return dynamicApiUrls;
    }

    /**
     * 从指定 URL 获取 JSON 数据
     */
    private static JsonObject fetchJsonFromUrl(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json"); // 明确要求 JSON 响应
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            JsonParser parser = new JsonParser();
            return parser.parse(reader).getAsJsonObject();
        }
    }

    /**
     * 检查是否有更新
     */
    public static boolean isUpdateAvailable(String localVersion) throws Exception {
        String latestVersion = getLatestReleaseVersion();
        return compareVersions(localVersion, latestVersion) < 0;
    }

    /**
     * 比较版本号
     */
    private static int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");
        int maxLength = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLength; i++) {
            int v1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int v2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (v1 < v2) return -1;
            if (v1 > v2) return 1;
        }
        return 0;
    }

    /**
     * 检查更新并输出日志
     */
    public static void checkUpdate() {
        try {
            String localVersion = Lengbanlist.getInstance().getDescription().getVersion();
            if (isUpdateAvailable(localVersion)) {
                JsonObject latestRelease = fetchJsonFromUrl(GITHUB_API_URL);
                String downloadUrl = latestRelease.get("html_url").getAsString(); // 提取下载链接

                TextComponent message = new TextComponent("§a发现新版本: §e" + localVersion + " → " + latestRelease.get("tag_name").getAsString());
                TextComponent link = new TextComponent("§f【§b点击下载§f】");
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl));
                link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a点击下载最新版本").create()));

                Lengbanlist.getInstance().getLogger().info(message.toLegacyText() + " " + link.toLegacyText());
            } else {
                Lengbanlist.getInstance().getLogger().info("当前已是最新版本！");
            }
        } catch (Exception e) {
            Lengbanlist.getInstance().getLogger().warning("更新检查失败: " + e.getMessage());
        }
    }
}