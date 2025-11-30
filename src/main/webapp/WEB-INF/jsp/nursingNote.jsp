<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.meditrack.model.NursingNote" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>看護メモ - MediTrack</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>🏥 看護メモ</h1>
        
        <%
            Integer patientId = (Integer) request.getAttribute("patientId");
            String userName = (String) session.getAttribute("userName");
        %>
        
        <!-- 看護メモ追加フォーム -->
        <div class="form-container">
            <h2>新規メモ追加</h2>
            <form action="nursingNote" method="post">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="patientId" value="<%= patientId %>">
                
                <div class="form-group">
                    <label>日付:</label>
                    <input type="date" name="noteDate" value="<%= java.time.LocalDate.now() %>" required>
                </div>
                
                <div class="form-group">
                    <label>時刻:</label>
                    <input type="time" name="noteTime" value="<%= java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) %>" required>
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
                    <button type="submit" class="btn-primary">登録</button>
                    <button type="button" class="btn-secondary" onclick="history.back()">戻る</button>
                </div>
            </form>
        </div>
        
        <!-- 看護メモ一覧 -->
        <div class="list-container">
            <h2>看護メモ一覧</h2>
            <%
                @SuppressWarnings("unchecked")
                List<NursingNote> notes = (List<NursingNote>) request.getAttribute("notes");
                
                if (notes != null && !notes.isEmpty()) {
            %>
            <table class="data-table">
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
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        
                        for (NursingNote note : notes) {
                            String priorityClass = "";
                            if ("高".equals(note.getPriority())) {
                                priorityClass = "priority-high";
                            } else if ("中".equals(note.getPriority())) {
                                priorityClass = "priority-medium";
                            } else {
                                priorityClass = "priority-low";
                            }
                    %>
                    <tr>
                        <td>
                            <%= note.getNoteDate().format(dateFormatter) %><br>
                            <%= note.getNoteTime().format(timeFormatter) %>
                        </td>
                        <td class="<%= priorityClass %>">
                            <%= note.getPriority() %>
                        </td>
                        <td style="text-align: left;">
                            <%= note.getContent() %>
                        </td>
                        <td><%= note.getNurseName() %></td>
                        <td>
                            <button class="btn-edit" 
                                onclick="location.href='nursingNote?action=edit&noteId=<%= note.getNoteId() %>&patientId=<%= patientId %>'">
                                編集
                            </button>
                            <button class="btn-delete" 
                                onclick="if(confirm('このメモを削除してもよろしいですか？')) location.href='nursingNote?action=delete&noteId=<%= note.getNoteId() %>&patientId=<%= patientId %>'">
                                削除
                            </button>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <% } else { %>
            <p class="no-data">看護メモはまだ登録されていません。</p>
            <% } %>
        </div>
    </div>
</body>
</html>