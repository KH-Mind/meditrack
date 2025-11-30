<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meditrack.model.Patient" %>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>患者削除確認 - <%= DBConnection.getProperty("app.display.name") %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .confirm-box {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff3cd;
            border: 2px solid #ffc107;
            border-radius: 8px;
            padding: 30px;
        }
        
        .confirm-icon {
            font-size: 48px;
            text-align: center;
            margin-bottom: 20px;
        }
        
        .confirm-message {
            text-align: center;
            font-size: 18px;
            color: #856404;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .patient-info-box {
            background-color: white;
            padding: 20px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        
        .info-row {
            display: flex;
            padding: 10px 0;
            border-bottom: 1px solid #ecf0f1;
        }
        
        .info-row:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            color: #2c3e50;
            width: 120px;
        }
        
        .info-value {
            color: #555;
        }
        
        .warning-box {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 4px;
            border-left: 4px solid #f5c6cb;
            margin-bottom: 20px;
        }
        
        .warning-box strong {
            display: block;
            margin-bottom: 5px;
        }
        
        .button-group {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 20px;
            flex-wrap: nowrap;
        }
        
        .button-group .btn {
            flex: 0 0 auto;
            min-width: 120px;
            padding: 12px 24px;
        }
        
        .btn-danger {
            background-color: #e74c3c;
            color: white !important;
        }
        
        .btn-danger:hover {
            background-color: #c0392b;
        }
        
        .btn-cancel {
            background-color: #95a5a6;
            color: white !important;
            text-decoration: none;
        }
        
        .btn-cancel:hover {
            background-color: #7f8c8d;
        }
    </style>
</head>
<body>
    <!-- ヘッダー -->
    <div class="header">
        <div class="header-content">
            <h1>🏥 <%= DBConnection.getProperty("app.display.name") %></h1>
            <div class="header-nav">
                <span class="user-info">
                    <%= session.getAttribute("userName") %> さん
                </span>
                <a href="patientList">患者一覧</a>
                <a href="logout">ログアウト</a>
            </div>
        </div>
    </div>

    <!-- メインコンテンツ -->
    <div class="container">
        <div class="content-box">
            <%
                Patient patient = (Patient) request.getAttribute("patient");
                if (patient != null) {
            %>
            
            <h2 class="page-title">患者削除確認</h2>
            
            <div class="confirm-box">
                <div class="confirm-icon">⚠️</div>
                
                <div class="confirm-message">
                    以下の患者を削除してもよろしいですか？<br>
                    <strong>この操作は取り消せません。</strong>
                </div>
                
                <!-- 患者情報表示 -->
                <div class="patient-info-box">
                    <div class="info-row">
                        <div class="info-label">患者ID:</div>
                        <div class="info-value"><%= patient.getPatientId() %></div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">患者名:</div>
                        <div class="info-value"><%= patient.getName() %></div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">生年月日:</div>
                        <div class="info-value"><%= patient.getBirthDate() %> (<%= patient.getAge() %> 歳)</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">性別:</div>
                        <div class="info-value"><%= patient.getGender() %></div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">病室:</div>
                        <div class="info-value"><%= patient.getRoomNumber() != null ? patient.getRoomNumber() : "未設定" %></div>
                    </div>
                </div>
                
                <!-- 警告メッセージ -->
                <div class="warning-box">
                    <strong>⚠ 注意事項</strong>
                    患者を削除すると、以下の関連データも同時に削除されます：
                    <ul style="margin: 10px 0 0 20px;">
                        <li>バイタルサイン記録</li>
                        <li>投薬記録</li>
                        <li>看護メモ</li>
                    </ul>
                </div>
                
                <!-- 削除フォーム -->
                <form method="post" action="patientDelete">
                    <input type="hidden" name="patientId" value="<%= patient.getPatientId() %>">
                    
                    <div class="button-group">
                    <button type="submit" class="btn btn-danger">
                        削除する
                    </button>
                    <a href="patientDetail?id=<%= patient.getPatientId() %>" class="btn btn-cancel">
                        キャンセル
                    </a>
                </div>
                </form>
            </div>
            
            <% } else { %>
                <p>患者情報が見つかりませんでした。</p>
                <a href="patientList" class="btn btn-primary">患者一覧に戻る</a>
            <% } %>
        </div>
    </div>
</body>
</html>