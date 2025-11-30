<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meditrack.model.NursingNote" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>看護メモ編集 - MediTrack</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>🏥 看護メモ編集</h1>
        
        <%
            NursingNote note = (NursingNote) request.getAttribute("note");
            Integer patientId = (Integer) request.getAttribute("patientId");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        %>
        
        <div class="form-container">
            <form action="nursingNote" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="noteId" value="<%= note.getNoteId() %>">
                <input type="hidden" name="patientId" value="<%= patientId %>">
                
                <div class="form-group">
                    <label>日付:</label>
                    <input type="date" name="noteDate" 
                           value="<%= note.getNoteDate().format(dateFormatter) %>" required>
                </div>
                
                <div class="form-group">
                    <label>時刻:</label>
                    <input type="time" name="noteTime" 
                           value="<%= note.getNoteTime().format(timeFormatter) %>" required>
                </div>
                
                <div class="form-group">
                    <label>重要度:</label>
                    <select name="priority" required>
                        <option value="低" <%= "低".equals(note.getPriority()) ? "selected" : "" %>>低</option>
                        <option value="中" <%= "中".equals(note.getPriority()) ? "selected" : "" %>>中</option>
                        <option value="高" <%= "高".equals(note.getPriority()) ? "selected" : "" %>>高</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>メモ内容:</label>
                    <textarea name="content" rows="5" required><%= note.getContent() %></textarea>
                </div>
                
                <div class="form-group">
                    <label>記録者:</label>
                    <input type="text" value="<%= note.getNurseName() %>" readonly>
                </div>
                
                <div class="button-group">
                    <button type="submit" class="btn-primary">更新</button>
                    <button type="button" class="btn-secondary" 
                            onclick="location.href='patientDetail?id=<%= patientId %>&tab=nursing'">
                        キャンセル
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>