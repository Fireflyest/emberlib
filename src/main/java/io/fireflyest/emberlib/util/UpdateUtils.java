package io.fireflyest.emberlib.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.fireflyest.emberlib.data.Latest;

/**
 * 插件更新工具类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class UpdateUtils {
    
    private static final Gson gson = new GsonBuilder().create();

    private UpdateUtils() {
        // 
    }

    /**
     * 网络get请求
     * 
     * @param <T> 数据类型
     * @param uri 地址
     * @param dataClass 数据类
     * @return 请求返回数据
     */
    public static <T> T doGet(String uri, Class<T> dataClass) {
        T t = null;
        try {
            final String resultString = getRequest(uri);
            t = gson.fromJson(resultString, dataClass);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return t;
    }

    /**
     * 检查更新并返回新版本的下载地址，如果已是最新则返回null
     * 
     * @param pluginName 插件名称
     * @param version 现版本
     * @return 新版本下载地址
     */
    public static String checkUpdate(String pluginName, String version) {
        final String infoUrl = "https://api.github.com/repos/Fireflyest/" + pluginName + "/releases/latest";
        final Latest latest = doGet(infoUrl, Latest.class);
        String updateUrl = null;
        if (latest != null && compareVersion(latest.getName().substring(1), version) == 1) {
            updateUrl = latest.getAssets().get(0).getBrowserDownloadUrl();
        }
        return updateUrl;
    }

    /**
     * 网络post请求
     * 
     * @param <T> 数据类型
     * @param uri 地址
     * @param values 值
     * @param dataClass 数据类
     * @return 请求返回数据
     */
    public static <T> T doPost(String uri, Map<String, String> values, Class<T> dataClass) {
        return doPost(uri, gson.toJson(values), dataClass);
    }

    /**
     * 网络post请求
     * 
     * @param <T> 数据类型
     * @param uri 地址
     * @param values 值
     * @param dataClass 数据类
     * @return 请求返回数据
     */
    public static <T> T doPost(String uri, List<String> values, Class<T> dataClass) {
        return doPost(uri, gson.toJson(values), dataClass);
    }

    /**
     * 网络post请求
     * 
     * @param <T> 数据类型
     * @param uri 地址
     * @param data 值
     * @param dataClass 数据类
     * @return 请求返回数据
     */
    public static <T> T doPost(String uri, String data, Class<T> dataClass) {
        T t = null;
        try {
            final String resultString = postRequest(uri, data);
            t = gson.fromJson(resultString, dataClass);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return t;
    }
    
    /**
     * 网络get请求
     * 
     * @param uri 地址
     * @return 请求结果
     * @throws IOException 连接异常
     * @throws InterruptedException 线程阻塞异常
     */
    private static String getRequest(String uri) throws IOException, InterruptedException {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        final HttpResponse<String> response = 
            client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * 网络post请求
     * 
     * @param uri 地址
     * @param values 请求数据
     * @return 请求结果
     * @throws IOException 连接异常
     * @throws InterruptedException 线程阻塞异常
     */
    private static String postRequest(String uri, String values) 
            throws IOException, InterruptedException {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(values))
                .build();
        final HttpResponse<String> response = 
            client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * 版本号比较
     * 
     * @param v1 版本1
     * @param v2 版本2
     * @return 0代表相等，1代表左边大，-1代表右边大
     */
    public static int compareVersion(String v1, String v2) {
        if (v1.equals(v2)) {
            return 0;
        }
        final String[] version1Array = v1.split("[._]");
        final String[] version2Array = v2.split("[._]");
        final int minLen = Math.min(version1Array.length, version2Array.length);
        int index = 0;
        long diff = 0;
        while (index < minLen
            && (diff = Long.parseLong(version1Array[index])
            - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        }
        return diff > 0 ? 1 : -1;
    }

    
}
