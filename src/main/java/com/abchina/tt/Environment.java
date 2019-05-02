package com.abchina.tt;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.sql.DataSource;

public enum Environment {
    instance;

    private final HttpClient httpClient;
    private final String key;
    private final String host;
    private static DataSource dataSource;

    Environment() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(50);
        connectionManager.setMaxTotal(50);
        RequestConfig requestConfig = RequestConfig.custom()
//                .setSocketTimeout(2000)
                .setConnectionRequestTimeout(600000)
//                .setConnectTimeout(30000)
                .build();
        httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .build();
        new Thread(new IdleConnectionMonitor(connectionManager)).start();
        key = "d7b239c76d85dd015a2b37e92d09c62f";
        host = "restapi.amap.com";
    }

    public static Environment getInstance() {
        return instance;
    }

    public DataSource getDataSource(){
        if (null == dataSource) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/house?serverTimezone=UTC");
            config.setUsername("sylll");
            config.setPassword("pswd");
            config.setMaximumPoolSize(10);
            config.setAutoCommit(true);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public String getKey() {
        return key;
    }

    public String getHost() {
        return host;
    }
}
