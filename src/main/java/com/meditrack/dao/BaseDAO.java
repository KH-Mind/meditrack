package com.meditrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.meditrack.util.DBConnection;

/**
 * すべてのDAOクラスの基底クラス
 * 共通メソッドを定義
 * @param <T> エンティティの型
 */
public abstract class BaseDAO<T> {
    
    protected Connection connection;
    
    /**
     * コンストラクタ：データベース接続を取得
     */
    public BaseDAO() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("データベース接続エラー");
            e.printStackTrace();
        }
    }
    
    /**
     * 全件取得（各DAOで実装）
     * @return エンティティのリスト
     * @throws SQLException
     */
    public abstract List<T> findAll() throws SQLException;
    
    /**
     * IDで検索（各DAOで実装）
     * @param id 検索するID
     * @return エンティティ
     * @throws SQLException
     */
    public abstract T findById(int id) throws SQLException;
    
    /**
     * 新規登録（各DAOで実装）
     * @param entity 登録するエンティティ
     * @return 成功したらtrue
     * @throws SQLException
     */
    public abstract boolean insert(T entity) throws SQLException;
    
    /**
     * 更新（各DAOで実装）
     * @param entity 更新するエンティティ
     * @return 成功したらtrue
     * @throws SQLException
     */
    public abstract boolean update(T entity) throws SQLException;
    
    /**
     * 削除（各DAOで実装）
     * @param id 削除するID
     * @return 成功したらtrue
     * @throws SQLException
     */
    public abstract boolean delete(int id) throws SQLException;
    
    /**
     * リソースをクローズ
     * @param ps PreparedStatement
     * @param rs ResultSet
     */
    protected void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
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