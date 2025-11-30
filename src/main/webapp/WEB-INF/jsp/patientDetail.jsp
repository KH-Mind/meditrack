<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meditrack.model.Patient" %>
<%@ page import="com.meditrack.model.VitalSign" %>
<%@ page import="com.meditrack.model.Medication" %>
<%@ page import="java.util.List" %>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>患者詳細 - <%= DBConnection.getProperty("app.display.name") %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .patient-header {
            background-color: #ecf0f1;
            padding: 20px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        
        .patient-header h2 {
            margin: 0 0 10px 0;
            color: #2c3e50;
        }
        
        .patient-meta {
            display: flex;
            gap: 20px;
            color: #7f8c8d;
            font-size: 14px;
        }
        
        .tabs {
            display: flex;
            gap: 10px;
            border-bottom: 2px solid #ecf0f1;
            margin-bottom: 20px;
        }
        
        .tab {
            padding: 12px 20px;
            background-color: transparent;
            border: none;
            cursor: pointer;
            color: #7f8c8d;
            font-size: 14px;
            text-decoration: none;
            border-bottom: 3px solid transparent;
            transition: all 0.3s;
        }
        
        .tab:hover {
            color: #3498db;
        }
        
        .tab.active {
            color: #3498db;
            border-bottom-color: #3498db;
            font-weight: 600;
        }
        
        .tab-content {
            display: none;
        }
        
        .tab-content.active {
            display: block;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .info-item {
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 4px;
        }
        
        .info-label {
            font-size: 12px;
            color: #7f8c8d;
            margin-bottom: 5px;
        }
        
        .info-value {
            font-size: 16px;
            color: #2c3e50;
            font-weight: 600;
        }
        
        .action-buttons {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }
        
        .btn-danger {
            background-color: #e74c3c;
            color: white;
        }
        
        .btn-danger:hover {
            background-color: #c0392b;
        }
        
        .empty-state {
            text-align: center;
            padding: 40px;
            color: #95a5a6;
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
                String activeTab = (String) request.getAttribute("activeTab");
                
                if (patient != null) {
            %>
            
            <!-- 成功メッセージ -->
           <%
                String successMessage = (String) session.getAttribute("successMessage");
             if (successMessage != null) {
                session.removeAttribute("successMessage");
            %>
            <div style="background-color: #d5f4e6; color: #27ae60; padding: 15px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #27ae60;">
             ✅ <%= successMessage %>
           </div>
           <% } %>
         
         
			<!-- 患者ヘッダー -->
            <div class="patient-header">
                <h2><%= patient.getName() %></h2>
                <div class="patient-meta">
                    <span>ID: <%= patient.getPatientId() %></span>
                    <span>年齢: <%= patient.getAge() %> 歳</span>
                    <span>性別: <%= patient.getGender() %></span>
                    <span>病室: <%= patient.getRoomNumber() != null ? patient.getRoomNumber() : "未設定" %></span>
                </div>
            </div>
            
            <!-- タブメニュー -->
            <div class="tabs">
    <a href="?id=<%= patient.getPatientId() %>&tab=basic" 
       class="tab <%= "basic".equals(activeTab) ? "active" : "" %>">基本情報</a>
    <a href="?id=<%= patient.getPatientId() %>&tab=vital" 
       class="tab <%= "vital".equals(activeTab) ? "active" : "" %>">容態確認</a>
    <a href="?id=<%= patient.getPatientId() %>&tab=medication" 
       class="tab <%= "medication".equals(activeTab) ? "active" : "" %>">投薬管理</a>
    <a href="?id=<%= patient.getPatientId() %>&tab=nursing" 
       class="tab <%= "nursing".equals(activeTab) ? "active" : "" %>">看護メモ</a>
</div>
            
            <!-- 基本情報タブ -->
            <div class="tab-content <%= "basic".equals(activeTab) ? "active" : "" %>">
                <h3 style="margin-bottom: 20px;">基本情報</h3>
                
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">患者ID</div>
                        <div class="info-value"><%= patient.getPatientId() %></div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">患者名</div>
                        <div class="info-value"><%= patient.getName() %></div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">生年月日</div>
                        <div class="info-value"><%= patient.getBirthDate() %></div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">年齢</div>
                        <div class="info-value"><%= patient.getAge() %> 歳</div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">性別</div>
                        <div class="info-value"><%= patient.getGender() %></div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">病室番号</div>
                        <div class="info-value"><%= patient.getRoomNumber() != null ? patient.getRoomNumber() : "未設定" %></div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">入院日</div>
                        <div class="info-value"><%= patient.getAdmissionDate() %></div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">入院日数</div>
                        <div class="info-value"><%= patient.getDaysInHospital() %> 日</div>
                    </div>
                    
                    <div class="info-item" style="grid-column: 1 / -1;">
                        <div class="info-label">主治医</div>
                        <div class="info-value"><%= patient.getDoctorName() != null ? patient.getDoctorName() : "未設定" %></div>
                    </div>
                </div>
                
                <div class="action-buttons">
                    <a href="patientEdit?id=<%= patient.getPatientId() %>" class="btn btn-primary">編集</a>
                    <a href="patientDelete?id=<%= patient.getPatientId() %>" class="btn btn-danger" 
                       onclick="return confirm('患者「<%= patient.getName() %>」を削除してもよろしいですか？\n関連するバイタル記録、投薬記録、看護メモも削除されます。');">
                        削除
                    </a>
                    <a href="patientList" class="btn btn-secondary">一覧に戻る</a>
                </div>
            </div>
            
            <!-- 容態確認タブ -->
<div class="tab-content <%= "vital".equals(activeTab) ? "active" : "" %>">
    <div class="flex-between mb-20">
        <h3>バイタルサイン記録</h3>
        <a href="vitalRecord?patientId=<%= patient.getPatientId() %>" class="btn btn-primary">新規記録</a>
    </div>
    
    <%
        // バイタル記録を取得（PatientDetailServletで設定される）
        @SuppressWarnings("unchecked")
        List<VitalSign> vitalSigns = (List<VitalSign>) request.getAttribute("vitalSigns");
        
        if (vitalSigns != null && !vitalSigns.isEmpty()) {
    %>
        <table class="table">
            <thead>
                <tr>
                    <th>測定日時</th>
                    <th>体温</th>
                    <th>血圧</th>
                    <th>脈拍</th>
                    <th>SpO2</th>
                    <th>備考</th>
                </tr>
            </thead>
            <tbody>
                <% for (VitalSign vital : vitalSigns) { %>
                    <tr>
                        <td><%= vital.getFormattedMeasuredAt() %></td>
                        <td>
                            <% if (vital.getTemperature() != null) { %>
                                <span style="<%= vital.isAbnormalTemperature() ? "color: #e74c3c; font-weight: 600;" : "" %>">
                                    <%= vital.getTemperature() %>℃
                                </span>
                            <% } else { %>
                                -
                            <% } %>
                        </td>
                        <td>
                            <% if (vital.getBloodPressureHigh() != null && vital.getBloodPressureLow() != null) { %>
                                <span style="<%= vital.isAbnormalBloodPressure() ? "color: #e74c3c; font-weight: 600;" : "" %>">
                                    <%= vital.getFormattedBloodPressure() %>
                                </span>
                            <% } else { %>
                                -
                            <% } %>
                        </td>
                        <td>
                            <% if (vital.getPulse() != null) { %>
                                <%= vital.getPulse() %> bpm
                            <% } else { %>
                                -
                            <% } %>
                        </td>
                        <td>
                            <% if (vital.getSpo2() != null) { %>
                                <span style="<%= vital.isAbnormalSpO2() ? "color: #e74c3c; font-weight: 600;" : "" %>">
                                    <%= vital.getSpo2() %>%
                                </span>
                            <% } else { %>
                                -
                            <% } %>
                        </td>
                        <td>
                            <%= vital.getMemo() != null ? vital.getMemo() : "" %>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else { %>
        <div class="empty-state">
            <div class="empty-state-icon">📊</div>
            <div class="empty-state-text">
                まだバイタルサインが記録されていません
                <br><br>
                <a href="vitalRecord?patientId=<%= patient.getPatientId() %>" class="btn btn-primary">最初の記録を追加</a>
            </div>
        </div>
    <% } %>
</div>
            <!-- 投薬管理タブ -->
<div class="tab-content <%= "medication".equals(activeTab) ? "active" : "" %>">
    <div class="flex-between mb-20">
        <h3>投薬記録</h3>
        <a href="medicationRecord?patientId=<%= patient.getPatientId() %>" class="btn btn-primary">新規記録</a>
    </div>

    <%
        // 投薬記録を取得（PatientDetailServletで設定される）
        @SuppressWarnings("unchecked")
        List<Medication> medications = (List<Medication>) request.getAttribute("medications");
        
        if (medications != null && !medications.isEmpty()) {
    %>
        <table class="table">
            <thead>
                <tr>
                    <th>薬剤名</th>
                    <th>用量</th>
                    <th>頻度</th>
                    <th>開始日時</th>
                    <th>終了日時</th>
                    <th>状態</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <% for (Medication med : medications) { %>
                    <tr>
                        <td><%= med.getMedicineName() %></td>
                        <td><%= med.getDosage() %></td>
                        <td><%= med.getFrequency() %></td>
                        <td><%= med.getFormattedStartDate() %></td>
                        <td><%= med.getFormattedEndDate() %></td>
                        <td>
                            <% if (med.isActive()) { %>
                                <span style="color: #27ae60; font-weight: 600;">● 有効</span>
                            <% } else { %>
                                <span style="color: #95a5a6;">○ 終了</span>
                            <% } %>
                        </td>
                        <td>
                            <a href="medicationRecord?patientId=<%= patient.getPatientId() %>&medicationId=<%= med.getMedicationId() %>" 
                               class="btn btn-sm btn-secondary">編集</a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else { %>
        <div class="empty-state">
            <div class="empty-state-icon">💊</div>
            <div class="empty-state-text">
                まだ投薬記録がありません
                <br><br>
                <a href="medicationRecord?patientId=<%= patient.getPatientId() %>" class="btn btn-primary">最初の記録を追加</a>
            </div>
        </div>
    <% } %>
</div>
            
            <!-- 看護メモタブ -->
<div class="tab-content <%= "nursing".equals(activeTab) ? "active" : "" %>">
    <div class="flex-between mb-20">
        <h3>看護メモ</h3>
        <a href="nursingNote?action=add&patientId=<%= patient.getPatientId() %>" class="btn btn-primary">新規メモ追加</a>
    </div>

    <%
        @SuppressWarnings("unchecked")
        List<com.meditrack.model.NursingNote> nursingNotes = (List<com.meditrack.model.NursingNote>) request.getAttribute("nursingNotes");
        
        if (nursingNotes != null && !nursingNotes.isEmpty()) {
            java.time.format.DateTimeFormatter noteDateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd");
            java.time.format.DateTimeFormatter noteTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
    %>
        <table class="table">
            <thead>
                <tr>
                    <th>日時</th>
                    <th>重要度</th>
                    <th>内容</th>
                    <th>記録者</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (com.meditrack.model.NursingNote note : nursingNotes) {
                        String priorityStyle = "";
                        if ("高".equals(note.getPriority())) {
                            priorityStyle = "color: #e74c3c; font-weight: bold;";
                        } else if ("中".equals(note.getPriority())) {
                            priorityStyle = "color: #f39c12; font-weight: 600;";
                        } else {
                            priorityStyle = "color: #95a5a6;";
                        }
                %>
                    <tr>
                        <td>
                            <%= note.getNoteDate().format(noteDateFormatter) %><br>
                            <%= note.getNoteTime().format(noteTimeFormatter) %>
                        </td>
                        <td style="<%= priorityStyle %>">
                            <%= note.getPriority() %>
                        </td>
                        <td style="text-align: left;">
                            <%= note.getContent() %>
                        </td>
                        <td><%= note.getNurseName() %></td>
                        <td>
                            <a href="nursingNote?action=edit&noteId=<%= note.getNoteId() %>&patientId=<%= patient.getPatientId() %>" 
                               class="btn btn-sm btn-secondary">編集</a>
                            <a href="nursingNote?action=delete&noteId=<%= note.getNoteId() %>&patientId=<%= patient.getPatientId() %>" 
                               class="btn btn-sm btn-danger"
                               onclick="return confirm('このメモを削除してもよろしいですか？');">削除</a>
                        </td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    <%
        } else {
    %>
        <div class="empty-state">
            <div class="empty-state-icon">📝</div>
            <div class="empty-state-text">
                まだ看護メモが記録されていません
                <br><br>
                <a href="nursingNote?action=add&patientId=<%= patient.getPatientId() %>" class="btn btn-primary">最初のメモを追加</a>
            </div>
        </div>
    <%
        }
    %>
</div>

            <% } else { %>
                <p>患者情報が見つかりませんでした。</p>
                <a href="patientList" class="btn btn-primary">患者一覧に戻る</a>
            <% } %>
        </div>
    </div>
</body>
</html>