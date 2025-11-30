package com.meditrack.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.meditrack.util.DBConnection;

/**
 * すべてのServiceクラスの基底クラス
 * トランザクション管理を提供
 */
public abstract class BaseService {
    
    protected Connection connection;
    
    /**
     * コンストラクタ：データベース接続を取得
     */
    public BaseService() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("データベース接続エラー");
            e.printStackTrace();
        }
    }
    
    /**
     * トランザクション開始
     * @throws SQLException
     */
    protected void beginTransaction() throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false);
        }
    }
    
    /**
     * コミット
     * @throws SQLException
     */
    protected void commit() throws SQLException {
        if (connection != null) {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }
    
    /**
     * ロールバック
     */
    protected void rollback() {
        try {
            if (connection != null) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("ロールバック失敗");
            e.printStackTrace();
        }
    }
    
    /**
     * 接続をクローズ
     */
    public void closeConnection() {
        DBConnection.closeConnection(connection);
    }
}