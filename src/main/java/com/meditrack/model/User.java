package com.meditrack.model;

/**
 * ログインユーザー情報を表すモデルクラス
 */
public class User {
    
    private int userId;           // ユーザーID
    private String username;      // ユーザー名（ログインID）
    private String password;      // パスワード
    private String name;          // 表示名
    private String role;          // 役割（看護師など）
    
    /**
     * デフォルトコンストラクタ
     */
    public User() {
    }
    
    /**
     * 全項目を設定するコンストラクタ
     */
    public User(int userId, String username, String password, String name, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }
    
    // ========================================
    // ゲッター・セッター
    // ========================================
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}