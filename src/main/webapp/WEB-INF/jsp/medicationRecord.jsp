<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meditrack.model.Patient" %>
<%@ page import="com.meditrack.model.Medication" %>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>投薬記録 - <%= DBConnection.getProperty("app.display.name") %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🏥 <%= DBConnection.getProperty("app.display.name") %></h1>
            <div class="user-info">
                <span>ログイン中: <%= session.getAttribute("username") %></span>
                <a href="logout" class="btn btn-secondary btn-sm">ログアウト</a>
            </div>
        </div>

        <div class="content">
            <%
                Patient patient = (Patient) request.getAttribute("patient");
                Medication medication = (Medication) request.getAttribute("medication");
                Boolean isEdit = (Boolean) request.getAttribute("isEdit");
                boolean editMode = isEdit != null && isEdit;
            %>
            
            <% if (patient != null) { %>
                <div class="page-header">
                    <h2><%= editMode ? "投薬記録編集" : "投薬記録登録" %></h2>
                    <div class="breadcrumb">
                        <a href="patientList">患者一覧</a> &gt;
                        <a href="patientDetail?id=<%= patient.getPatientId() %>"><%= patient.getName() %></a> &gt;
                        <%= editMode ? "投薬記録編集" : "投薬記録登録" %>
                    </div>
                </div>

                <!-- 患者情報 -->
                <div class="info-box mb-20">
                    <h3>患者情報</h3>
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="info-label">患者ID:</span>
                            <span class="info-value"><%= patient.getPatientId() %></span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">氏名:</span>
                            <span class="info-value"><%= patient.getName() %></span>
                        </div>
                    </div>
                </div>

                <!-- エラーメッセージ -->
                <%
                    String errorMessage = (String) session.getAttribute("errorMessage");
                    if (errorMessage != null) {
                        session.removeAttribute("errorMessage");
                %>
                    <div class="error-message mb-20">
                        ⚠ <%= errorMessage %>
                    </div>
                <% } %>

                <!-- 投薬記録フォーム -->
                <form method="post" action="medicationRecord" class="form-card">
                    <input type="hidden" name="patientId" value="<%= patient.getPatientId() %>">
                    <% if (editMode && medication != null) { %>
                        <input type="hidden" name="medicationId" value="<%= medication.getMedicationId() %>">
                    <% } %>

                    <div class="form-group">
                        <label for="medicineName" class="required">薬剤名</label>
                        <input type="text" id="medicineName" name="medicineName" 
                               class="form-control" required
                               value="<%= editMode && medication != null ? medication.getMedicineName() : "" %>"
                               placeholder="例：ロキソニン錠60mg">
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="dosage" class="required">用量</label>
                            <input type="text" id="dosage" name="dosage" 
                                   class="form-control" required
                                   value="<%= editMode && medication != null ? medication.getDosage() : "" %>"
                                   placeholder="例：1錠、10mg">
                        </div>

                        <div class="form-group">
                            <label for="frequency" class="required">頻度</label>
                            <input type="text" id="frequency" name="frequency" 
                                   class="form-control" required
                                   value="<%= editMode && medication != null ? medication.getFrequency() : "" %>"
                                   placeholder="例：1日3回、朝夕2回">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="startDate" class="required">開始日時</label>
                            <input type="datetime-local" id="startDate" name="startDate" 
                                   class="form-control" required
                                   value="<%= editMode && medication != null && medication.getStartDate() != null ? medication.getStartDate().toString().substring(0, 16) : "" %>">
                        </div>

                        <div class="form-group">
                            <label for="endDate">終了日時</label>
                            <input type="datetime-local" id="endDate" name="endDate" 
                                   class="form-control"
                                   value="<%= editMode && medication != null && medication.getEndDate() != null ? medication.getEndDate().toString().substring(0, 16) : "" %>">
                            <small class="form-text">※継続中の場合は空欄</small>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="notes">備考</label>
                        <textarea id="notes" name="notes" class="form-control" 
                                  rows="4" placeholder="服用時の注意事項など"><%= editMode && medication != null && medication.getNotes() != null ? medication.getNotes() : "" %></textarea>
                    </div>

                    <div class="button-group">
                        <button type="submit" class="btn btn-primary">
                            <%= editMode ? "更新する" : "登録する" %>
                        </button>
                        <a href="patientDetail?id=<%= patient.getPatientId() %>&tab=medication" 
                           class="btn btn-cancel">キャンセル</a>
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