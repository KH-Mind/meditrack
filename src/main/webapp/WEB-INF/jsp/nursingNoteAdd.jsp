<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>看護メモ追加 - MediTrack</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>🏥 看護メモ追加</h1>
        
        <%
            Integer patientId = (Integer) request.getAttribute("patientId");
            String userName = (String) session.getAttribute("userName");
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        %>
        
        <div class="form-container">
            <form action="nursingNote" method="post">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="patientId" value="<%= patientId %>">
                
                <div class="form-group">
                    <label>日付:</label>
                    <input type="date" name="noteDate" value="<%= today %>" required>
                </div>
                
                <div class="form-group">
                    <label>時刻:</label>
                    <input type="time" name="noteTime" value="<%= now.format(timeFormatter) %>" required>
                </div>
                
                <div class="form-group">
                    <label>重要度:</label>
                    <select name="priority" required>
                        <option value="低">低</option>
                        <option value="中" selected>中</option>
                        <option value="高">高</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>メモ内容:</label>
                    <textarea name="content" rows="5" required></textarea>
                </div>
                
                <div class="form-group">
                    <label>記録者:</label>
                    <input type="text" value="<%= userName %>" readonly>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="btn btn-primary">登録</button>
                    <button type="button" class="btn btn-secondary" 
                            onclick="location.href='patientDetail?id=<%= patientId %>&tab=nursing'">
                        キャンセル
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>