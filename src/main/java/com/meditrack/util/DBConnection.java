package com.meditrack.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * データベース接続を管理するユーティリティクラス
 */
public class DBConnection {
    
    private static Properties config;
    
    // 設定ファイルを読み込む（起動時に1度だけ実行）
    static {
        config = new Properties();
        try (InputStream input = DBConnection.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                System.err.println("config.propertiesが見つかりません");
                throw new RuntimeException("設定ファイルが見つかりません");
            }
            
            config.load(input);
            
            // JDBCドライバーをロード
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("✅ データベース設定を読み込みました");
            
        } catch (IOException e) {
            System.err.println("❌ 設定ファイルの読み込みに失敗しました");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBCドライバーが見つかりません");
            e.printStackTrace();
        }
    }
    
    /**
     * データベース接続を取得
     * @return データベース接続
     * @throws SQLException データベース接続エラー
     */
    public static Connection getConnection() throws SQLException {
        String url = config.getProperty("db.url");
        String user = config.getProperty("db.user");
        String password = config.getProperty("db.password");
        
        return DriverManager.getConnection(url, user, password);
    }
    
    /**
     * データベース接続をクローズ
     * @param connection クローズする接続
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 設定値を取得
     * @param key キー
     * @return 設定値
     */
    public static String getProperty(String key) {
        return config.getProperty(key);
    }
}