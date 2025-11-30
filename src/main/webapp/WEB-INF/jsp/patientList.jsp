<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.meditrack.model.Patient" %>
<%@ page import="com.meditrack.util.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>患者一覧 - <%= DBConnection.getProperty("app.display.name") %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
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
            <div class="flex-between mb-20">
                <h2 class="page-title">患者一覧</h2>
                <a href="patientRegister" class="btn btn-primary">新規患者登録</a>
            </div>
            
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
            
            <!-- エラーメッセージ -->
            <%
                String errorMessage = (String) session.getAttribute("errorMessage");
                if (errorMessage != null) {
                    session.removeAttribute("errorMessage");
            %>
                <div style="background-color: #fee; color: #c33; padding: 15px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #c33;">
                    ⚠ <%= errorMessage %>
                </div>
            <% } %>
            <!-- 検索フォーム -->
            <form method="get" action="patientList" class="search-box">
                <input type="text" 
                       name="searchName" 
                       placeholder="患者名で検索"
                       value="<%= request.getAttribute("searchName") != null ? request.getAttribute("searchName") : "" %>">
                <input type="text" 
                       name="searchRoom" 
                       placeholder="病室番号で検索"
                       value="<%= request.getAttribute("searchRoom") != null ? request.getAttribute("searchRoom") : "" %>">
                <button type="submit" class="btn btn-primary">検索</button>
                <% if (request.getAttribute("searchName") != null || request.getAttribute("searchRoom") != null) { %>
                    <a href="patientList" class="btn btn-secondary">クリア</a>
                <% } %>
            </form>
            
            <!-- 件数表示 -->
            <p style="color: #7f8c8d; margin-bottom: 10px;">
                <%= request.getAttribute("patientCount") %> 件の患者が見つかりました
            </p>
            
            <!-- 患者一覧テーブル -->
            <%
                @SuppressWarnings("unchecked")
                List<Patient> patients = (List<Patient>) request.getAttribute("patients");
                if (patients != null && !patients.isEmpty()) {
            %>
                <table class="table">
                    <thead>
                        <tr>
                            <th>患者ID</th>
                            <th>氏名</th>
                            <th>年齢</th>
                            <th>性別</th>
                            <th>病室</th>
                            <th>入院日</th>
                            <th>主治医</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Patient patient : patients) { %>
                            <tr>
                                <td><%= patient.getPatientId() %></td>
                                <td>
                                    <a href="patientDetail?id=<%= patient.getPatientId() %>">
                                        <%= patient.getName() %>
                                    </a>
                                </td>
                                <td><%= patient.getAge() %> 歳</td>
                                <td><%= patient.getGender() %></td>
                                <td><%= patient.getRoomNumber() %></td>
                                <td><%= patient.getAdmissionDate() %></td>
                                <td><%= patient.getDoctorName() %></td>
                                <td>
                                    <a href="patientDetail?id=<%= patient.getPatientId() %>" 
                                       class="btn btn-primary btn-small">詳細</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <%
                } else {
            %>
                <div class="empty-state">
                    <div class="empty-state-icon">📋</div>
                    <div class="empty-state-text">
                        患者が登録されていません
                        <br><br>
                        <a href="patientRegister" class="btn btn-primary">新規患者を登録する</a>
                    </div>
                </div>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>