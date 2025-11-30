<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>エラー - <%= DBConnection.getProperty("app.display.name") %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🏥 <%= DBConnection.getProperty("app.display.name") %></h1>
            <% if (session != null && session.getAttribute("user") != null) { %>
                <div class="user-info">
                    <span>ログイン中: <%= session.getAttribute("username") %></span>
                    <a href="logout" class="btn btn-secondary btn-sm">ログアウト</a>
                </div>
            <% } %>
        </div>

        <div class="content">
            <div class="error-page">
                <div class="error-icon">⚠️</div>
                <h2>エラーが発生しました</h2>
                
                <%
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    if (errorMessage != null) {
                %>
                    <div class="error-message">
                        <%= errorMessage %>
                    </div>
                <% } else { %>
                    <p>予期しないエラーが発生しました。</p>
                <% } %>
                
                <div class="button-group" style="margin-top: 30px;">
                    <% if (session != null && session.getAttribute("user") != null) { %>
                        <a href="patientList" class="btn btn-primary">患者一覧に戻る</a>
                    <% } else { %>
                        <a href="login" class="btn btn-primary">ログイン画面に戻る</a>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
    
    <style>
        .error-page {
            text-align: center;
            padding: 60px 20px;
        }
        
        .error-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }
        
        .error-page h2 {
            color: #e74c3c;
            margin-bottom: 20px;
        }
        
        .error-message {
            background-color: #fee;
            color: #c33;
            padding: 15px;
            border-radius: 4px;
            margin: 20px auto;
            max-width: 600px;
            border-left: 4px solid #c33;
        }
    </style>
</body>
</html>