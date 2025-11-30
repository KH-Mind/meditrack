<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= DBConnection.getProperty("app.display.name") %></title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .welcome-container {
            max-width: 600px;
            width: 100%;
            background-color: white;
            padding: 60px 40px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            text-align: center;
        }
        
        .logo {
            font-size: 64px;
            margin-bottom: 20px;
            animation: pulse 2s ease-in-out infinite;
        }
        
        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }
        
        h1 {
            color: #2c3e50;
            font-size: 36px;
            margin-bottom: 10px;
        }
        
        .subtitle {
            color: #7f8c8d;
            font-size: 18px;
            margin-bottom: 40px;
        }
        
        .btn-login {
            display: inline-block;
            padding: 15px 40px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 18px;
            font-weight: 600;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-login:active {
            transform: translateY(0);
        }
        
        .features {
            margin-top: 40px;
            text-align: left;
        }
        
        .features h3 {
            color: #2c3e50;
            font-size: 18px;
            margin-bottom: 20px;
            text-align: center;
        }
        
        .feature-item {
            margin: 15px 0;
            padding-left: 35px;
            position: relative;
            color: #555;
            line-height: 1.6;
        }
        
        .feature-item::before {
            content: "✓";
            position: absolute;
            left: 0;
            color: #27ae60;
            font-weight: bold;
            font-size: 20px;
        }
        
        .version {
            margin-top: 40px;
            padding-top: 20px;
            border-top: 1px solid #ecf0f1;
            color: #95a5a6;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <div class="logo">🏥</div>
        <h1><%= DBConnection.getProperty("app.display.name") %></h1>
        <p class="subtitle">患者管理システム</p>
        
        <a href="login" class="btn-login">ログインして始める →</a>
        
        <div class="features">
            <h3>主な機能</h3>
            <div class="feature-item">患者情報の一元管理</div>
            <div class="feature-item">バイタルサインの記録・追跡</div>
            <div class="feature-item">投薬管理とチェック機能</div>
            <div class="feature-item">看護記録の共有</div>
        </div>
        
        <div class="version">
            Version <%= DBConnection.getProperty("app.version") %>
        </div>
    </div>
</body>
</html>