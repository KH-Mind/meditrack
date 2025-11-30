<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.meditrack.model.Patient" %>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>患者情報編集 - <%= DBConnection.getProperty("app.display.name") %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .form-container {
            max-width: 800px;
            margin: 0 auto;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #34495e;
            font-weight: 600;
        }
        
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        
        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #3498db;
        }
        
        .form-row {
            display: flex;
            gap: 20px;
        }
        
        .form-row .form-group {
            flex: 1;
        }
        
        .required::after {
            content: " *";
            color: #e74c3c;
        }
        
        .error-list {
            background-color: #fee;
            color: #c33;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #c33;
        }
        
        .error-list ul {
            margin: 10px 0 0 20px;
        }
        
        .error-list li {
            margin: 5px 0;
        }
        
        .button-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }
        
        .btn-cancel {
            background-color: #95a5a6;
            color: white !important;
        }
        
        .btn-cancel:hover {
            background-color: #7f8c8d;
        }
        
        .patient-info {
            background-color: #ecf0f1;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        
        .patient-info strong {
            color: #2c3e50;
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
            
            <h2 class="page-title">患者情報編集</h2>
            
            <!-- 患者情報表示 -->
            <div class="patient-info">
                <strong>患者ID:</strong> <%= patient.getPatientId() %> 
                | <strong>編集対象:</strong> <%= patient.getName() %>
            </div>
            
            <!-- エラーメッセージ -->
            <%
                @SuppressWarnings("unchecked")
                List<String> errors = (List<String>) request.getAttribute("errors");
                if (errors != null && !errors.isEmpty()) {
            %>
                <div class="error-list">
                    <strong>⚠ 入力エラーがあります</strong>
                    <ul>
                        <% for (String error : errors) { %>
                            <li><%= error %></li>
                        <% } %>
                    </ul>
                </div>
            <% } %>
            
            <!-- 編集フォーム -->
            <form method="post" action="patientEdit" class="form-container">
                <!-- 患者IDを隠しフィールドで送信 -->
                <input type="hidden" name="patientId" value="<%= patient.getPatientId() %>">
                
                <div class="form-group">
                    <label for="name" class="required">患者名</label>
                    <input type="text" 
                           id="name" 
                           name="name" 
                           value="<%= patient.getName() %>"
                           placeholder="例: 山田太郎"
                           required>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="birthDate" class="required">生年月日</label>
                        <input type="date" 
                               id="birthDate" 
                               name="birthDate" 
                               value="<%= patient.getBirthDate() %>"
                               required>
                    </div>
                    
                    <div class="form-group">
                        <label for="gender" class="required">性別</label>
                        <select id="gender" name="gender" required>
                            <option value="">選択してください</option>
                            <option value="男性" <%= "男性".equals(patient.getGender()) ? "selected" : "" %>>男性</option>
                            <option value="女性" <%= "女性".equals(patient.getGender()) ? "selected" : "" %>>女性</option>
                            <option value="その他" <%= "その他".equals(patient.getGender()) ? "selected" : "" %>>その他</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="roomNumber">病室番号</label>
                        <input type="text" 
                               id="roomNumber" 
                               name="roomNumber" 
                               value="<%= patient.getRoomNumber() != null ? patient.getRoomNumber() : "" %>"
                               placeholder="例: 301"
                               maxlength="3">
                        <small style="color: #7f8c8d;">3桁の数字で入力してください</small>
                    </div>
                    
                    <div class="form-group">
                        <label for="admissionDate" class="required">入院日</label>
                        <input type="date" 
                               id="admissionDate" 
                               name="admissionDate" 
                               value="<%= patient.getAdmissionDate() %>"
                               required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="doctorName">主治医</label>
                    <input type="text" 
                           id="doctorName" 
                           name="doctorName" 
                           value="<%= patient.getDoctorName() != null ? patient.getDoctorName() : "" %>"
                           placeholder="例: 鈴木医師">
                </div>
                
                <div class="button-group">
                    <button type="submit" class="btn btn-primary">更新</button>
                    <a href="patientDetail?id=<%= patient.getPatientId() %>" class="btn btn-cancel">キャンセル</a>
                </div>
            </form>
            
            <% } else { %>
                <p>患者情報が見つかりませんでした。</p>
                <a href="patientList" class="btn btn-primary">患者一覧に戻る</a>
            <% } %>
        </div>
    </div>
</body>
</html>