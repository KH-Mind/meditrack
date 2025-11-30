<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログイン - MediTrack</title>
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
        }
        
        .login-container {
            max-width: 400px;
            width: 90%;
            background-color: #fff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .login-header h1 {
            color: #2c3e50;
            font-size: 28px;
            margin-bottom: 10px;
        }
        
        .login-header p {
            color: #7f8c8d;
            font-size: 14px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #34495e;
            font-weight: 600;
            font-size: 14px;
        }
        
        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .btn-login {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-login:active {
            transform: translateY(0);
        }
        
        .error-message {
            background-color: #fee;
            color: #c33;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
            text-align: center;
            border-left: 4px solid #c33;
            font-size: 14px;
        }
        
        .info-box {
            margin-top: 25px;
            padding: 15px;
            background-color: #f8f9fa;
            border-left: 4px solid #667eea;
            border-radius: 6px;
            font-size: 13px;
        }
        
        .info-box h4 {
            margin: 0 0 10px 0;
            color: #2c3e50;
            font-size: 14px;
        }
        
        .info-box p {
            margin: 5px 0;
            color: #555;
        }
        
        .info-box strong {
            color: #667eea;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <h1>🏥 MediTrack</h1>
            <p>患者管理システム</p>
        </div>
        
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>
        
        <form method="post" action="login">
            <div class="form-group">
                <label for="username">ユーザー名</label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       placeholder="ユーザー名を入力"
                       value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"
                       required 
                       autofocus>
            </div>
            
            <div class="form-group">
                <label for="password">パスワード</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       placeholder="パスワードを入力"
                       required>
            </div>
            
            <button type="submit" class="btn-login">ログイン</button>
        </form>
        
        <div class="info-box">
            <h4>📝 テストアカウント</h4>
            <p><strong>ユーザー名:</strong> nurse01</p>
            <p><strong>パスワード:</strong> password123</p>
        </div>
    </div>
</body>
</html>