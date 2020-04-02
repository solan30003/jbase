package com.solan.jbase.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import okhttp3.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * 自定义输出写入统一日志服务中心
 *
 * @author: hyl
 * @date: 2020/3/30 11:50
 */
public final class EncAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private final String DEFAULT_QUERY = "/logs/log";
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient httpClient;
    /**
     *
     */
    protected String baseUrl;
    protected String query = DEFAULT_QUERY;
    protected int maxIdleConnections = 20;
    protected long keepAliveDurationSeconds = 300L;
    protected long connectTimeoutSeconds = 120L;
    protected long readTimeout = 60L;
    protected Boolean consoleEnabled = false;

    public EncAppender() {

    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean getConsoleEnabled() {
        return consoleEnabled;
    }

    public void setConsoleEnabled(Boolean consoleEnabled) {
        this.consoleEnabled = consoleEnabled;
    }

    @Override
    public void start() {
        this.initOkHttpClient();
        super.start();
    }

    private void initOkHttpClient() {
        this.httpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDurationSeconds, TimeUnit.SECONDS))
                .retryOnConnectionFailure(true)
                .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (iLoggingEvent.getThrowableProxy() != null) {
            String str = parseThrowable(iLoggingEvent.getThrowableProxy());
        }
        String msg = String.format("LoggerName:%s, level:%s, threadName:%s, message:%s, TimeStamp:%s, throwable:%s",
                iLoggingEvent.getLoggerName(),
                iLoggingEvent.getLevel(),
                iLoggingEvent.getThreadName(),
                iLoggingEvent.getFormattedMessage(),
                iLoggingEvent.getTimeStamp(),
                parseThrowable(iLoggingEvent.getThrowableProxy())
        );
        printToConsole(msg);
        postLogData(String.format("{\"message\":\"%s\"}", msg));
    }

    private void postLogData(String jsonData) {
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonData);
        Request request = new Request.Builder()
                .url(String.format("http://%s%s", this.baseUrl, this.query))
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                printToConsole(String.format("Post log failure: %s", e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                printToConsole(String.format("Post log success: %s: %s", response.code(), response.message()));
                if (!response.isSuccessful()) {
                    saveLocalCache(jsonData);
                }
            }
        });
    }

    private void printToConsole(String msg) {
        if (consoleEnabled) {
            System.out.println(String.format("[EncAppender] %s", msg));
        }
    }

    /**
     * 放入本地存储，稍候进行重试
     *
     * @param data
     */
    private void saveLocalCache(String data) {

    }

    private String parseThrowable(IThrowableProxy tp) {
        StringBuilder buf = new StringBuilder();
        ThrowableProxyUtil.subjoinFirstLine(buf, tp);
        StringBuilder sb = new StringBuilder();
//        if (buf.length() > 0)
//            sb.append(String.format("%s%s", buf.toString(), System.getProperty("line.separator")));
        int commonFrames = tp.getCommonFrames();
        StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
        for (int i = 0; i < stepArray.length - commonFrames; i++) {
            sb.append(CoreConstants.TAB);
            ThrowableProxyUtil.subjoinSTEP(sb, stepArray[i]);
        }
        if (commonFrames > 0) {
            sb.append(CoreConstants.TAB).append("... ").append(commonFrames).append(" common frames omitted");
        }
        return sb.toString();

    }

    @Override
    public void stop() {
        super.stop();
    }
}
