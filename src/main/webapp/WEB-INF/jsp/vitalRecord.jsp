<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.meditrack.model.Patient" %>
<%@ page import="com.meditrack.model.VitalSign" %>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>バイタル記録 - <%= DBConnection.getProperty("app.display.name") %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .form-container {
            max-width: 800px;
            margin: 0 auto;
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
        
        .form-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        
        .form-section h3 {
            margin: 0 0 15px 0;
            color: #2c3e50;
            font-size: 18px;
            border-bottom: 2px solid #3498db;
            padding-bottom: 10px;
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
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        
        .form-group input:focus,
        .form-group textarea:focus {
            outline: none;
            border-color: #3498db;
        }
        
        .form-group textarea {
            resize: vertical;
            min-height: 80px;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
        }
        
        .form-row-3 {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
        }
        
        .unit {
            color: #7f8c8d;
            font-size: 13px;
            margin-top: 5px;
        }
        
        .required::after {
            content: " *";
            color: #e74c3c;
        }
        
        .optional {
            color: #95a5a6;
            font-size: 12px;
            font-weight: normal;
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
        
        .info-box {
            background-color: #e8f4f8;
            padding: 15px;
            border-radius: 4px;
            border-left: 4px solid #3498db;
            margin-bottom: 20px;
            font-size: 14px;
            color: #2c3e50;
        }
        
        .latest-vital {
            background-color: #fff;
            padding: 15px;
            border-radius: 4px;
            margin-top: 10px;
        }
        
        .latest-vital-item {
            display: inline-block;
            margin-right: 20px;
            color: #7f8c8d;
        }
        
        .latest-vital-value {
            font-weight: 600;
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
                VitalSign latestVital = (VitalSign) request.getAttribute("latestVital");
                
                if (patient != null) {
            %>
            
            <h2 class="page-title">バイタルサイン記録</h2>
            
            <!-- 患者情報表示 -->
            <div class="patient-info">
                <strong>患者ID:</strong> <%= patient.getPatientId() %> 
                | <strong>患者名:</strong> <%= patient.getName() %>
                | <strong>年齢:</strong> <%= patient.getAge() %> 歳
                | <strong>病室:</strong> <%= patient.getRoomNumber() != null ? patient.getRoomNumber() : "未設定" %>
            </div>
            
            <!-- 前回の測定値参考表示 -->
            <% if (latestVital != null) { %>
                <div class="info-box">
                    <strong>📊 前回の測定値（参考）</strong>
                    <div class="latest-vital">
                        <% if (latestVital.getTemperature() != null) { %>
                            <span class="latest-vital-item">
                                体温: <span class="latest-vital-value"><%= latestVital.getTemperature() %>℃</span>
                            </span>
                        <% } %>
                        <% if (latestVital.getBloodPressureHigh() != null && latestVital.getBloodPressureLow() != null) { %>
                            <span class="latest-vital-item">
                                血圧: <span class="latest-vital-value"><%= latestVital.getFormattedBloodPressure() %> mmHg</span>
                            </span>
                        <% } %>
                        <% if (latestVital.getPulse() != null) { %>
                            <span class="latest-vital-item">
                                脈拍: <span class="latest-vital-value"><%= latestVital.getPulse() %> bpm</span>
                            </span>
                        <% } %>
                        <% if (latestVital.getSpo2() != null) { %>
                            <span class="latest-vital-item">
                                SpO2: <span class="latest-vital-value"><%= latestVital.getSpo2() %>%</span>
                            </span>
                        <% } %>
                        <span class="latest-vital-item" style="color: #95a5a6;">
                            測定: <%= latestVital.getFormattedMeasuredAt() %>
                        </span>
                    </div>
                </div>
            <% } %>
            
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
            
            <!-- 記録フォーム -->
            <form method="post" action="vitalRecord" class="form-container">
                <!-- 患者IDを隠しフィールドで送信 -->
                <input type="hidden" name="patientId" value="<%= patient.getPatientId() %>">
                
                <!-- 測定日時 -->
                <div class="form-section">
                    <h3>測定日時</h3>
                    <div class="form-group">
                        <label for="measuredAt" class="required">測定日時</label>
                        <input type="datetime-local" 
                               id="measuredAt" 
                               name="measuredAt" 
                               value="<%= request.getAttribute("measuredAt") != null ? request.getAttribute("measuredAt") : request.getAttribute("defaultDateTime") %>"
                               required>
                    </div>
                </div>
                
                <!-- バイタル測定値 -->
                <div class="form-section">
                    <h3>測定値 <span class="optional">（少なくとも1つ入力してください）</span></h3>
                    
                    <div class="form-row">
                        <!-- 体温 -->
                        <div class="form-group">
                            <label for="temperature">体温 <span class="optional">（任意）</span></label>
                            <input type="number" 
                                   id="temperature" 
                                   name="temperature" 
                                   value="<%= request.getAttribute("temperature") != null ? request.getAttribute("temperature") : "" %>"
                                   placeholder="36.5"
                                   step="0.1"
                                   min="34.0"
                                   max="42.0">
                            <div class="unit">単位: ℃ （34.0〜42.0）</div>
                        </div>
                        
                        <!-- 脈拍 -->
                        <div class="form-group">
                            <label for="pulse">脈拍 <span class="optional">（任意）</span></label>
                            <input type="number" 
                                   id="pulse" 
                                   name="pulse" 
                                   value="<%= request.getAttribute("pulse") != null ? request.getAttribute("pulse") : "" %>"
                                   placeholder="72"
                                   min="30"
                                   max="200">
                            <div class="unit">単位: 回/分 （30〜200）</div>
                        </div>
                    </div>
                    
                    <div class="form-row-3">
                        <!-- 収縮期血圧 -->
                        <div class="form-group">
                            <label for="bloodPressureHigh">収縮期血圧 <span class="optional">（任意）</span></label>
                            <input type="number" 
                                   id="bloodPressureHigh" 
                                   name="bloodPressureHigh" 
                                   value="<%= request.getAttribute("bloodPressureHigh") != null ? request.getAttribute("bloodPressureHigh") : "" %>"
                                   placeholder="120"
                                   min="40"
                                   max="250">
                            <div class="unit">最高血圧 mmHg</div>
                        </div>
                        
                        <!-- 拡張期血圧 -->
                        <div class="form-group">
                            <label for="bloodPressureLow">拡張期血圧 <span class="optional">（任意）</span></label>
                            <input type="number" 
                                   id="bloodPressureLow" 
                                   name="bloodPressureLow" 
                                   value="<%= request.getAttribute("bloodPressureLow") != null ? request.getAttribute("bloodPressureLow") : "" %>"
                                   placeholder="80"
                                   min="40"
                                   max="250">
                            <div class="unit">最低血圧 mmHg</div>
                        </div>
                        
                        <!-- SpO2 -->
                        <div class="form-group">
                            <label for="spo2">SpO2 <span class="optional">（任意）</span></label>
                            <input type="number" 
                                   id="spo2" 
                                   name="spo2" 
                                   value="<%= request.getAttribute("spo2") != null ? request.getAttribute("spo2") : "" %>"
                                   placeholder="98"
                                   min="70"
                                   max="100">
                            <div class="unit">酸素飽和度 %</div>
                        </div>
                    </div>
                </div>
                
                <!-- 備考 -->
                <div class="form-section">
                    <h3>備考</h3>
                    <div class="form-group">
                        <label for="memo">メモ <span class="optional">（任意）</span></label>
                        <textarea id="memo" 
                                  name="memo" 
                                  placeholder="特記事項があれば入力してください"><%= request.getAttribute("memo") != null ? request.getAttribute("memo") : "" %></textarea>
                    </div>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="btn btn-primary">記録する</button>
                    <a href="patientDetail?id=<%= patient.getPatientId() %>&tab=vital" class="btn btn-cancel">キャンセル</a>
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