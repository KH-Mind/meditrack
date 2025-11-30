package com.meditrack.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.meditrack.dao.NursingNoteDAO;
import com.meditrack.model.NursingNote;

@WebServlet("/nursingNote")
public class NursingNoteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NursingNoteDAO nursingNoteDAO;

    @Override
    public void init() throws ServletException {
        nursingNoteDAO = new NursingNoteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        int patientId = Integer.parseInt(request.getParameter("patientId"));

        try {
            if ("list".equals(action)) {
                List<NursingNote> notes = nursingNoteDAO.findByPatientId(patientId);
                request.setAttribute("notes", notes);
                request.setAttribute("patientId", patientId);
                request.getRequestDispatcher("/WEB-INF/jsp/nursingNote.jsp").forward(request, response);  // ← views → jsp に変更
                
            } else if ("add".equals(action)) {
                request.setAttribute("patientId", patientId);
                request.getRequestDispatcher("/WEB-INF/jsp/nursingNoteAdd.jsp").forward(request, response);  // ← views → jsp に変更
                
            } else if ("edit".equals(action)) {
                int noteId = Integer.parseInt(request.getParameter("noteId"));
                NursingNote note = nursingNoteDAO.findById(noteId);
                request.setAttribute("note", note);
                request.setAttribute("patientId", patientId);
                request.getRequestDispatcher("/WEB-INF/jsp/nursingNoteEdit.jsp").forward(request, response);  // ← views → jsp に変更
                
            } else if ("delete".equals(action)) {
                int noteId = Integer.parseInt(request.getParameter("noteId"));
                nursingNoteDAO.delete(noteId);
                response.sendRedirect("patientDetail?id=" + patientId + "&tab=nursing");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        int patientId = Integer.parseInt(request.getParameter("patientId"));

        try {
            HttpSession session = request.getSession();
            String nurseName = (String) session.getAttribute("userName");

            if ("add".equals(action)) {
                // 看護メモ追加
                String content = request.getParameter("content");
                String priority = request.getParameter("priority");
                String dateStr = request.getParameter("noteDate");
                String timeStr = request.getParameter("noteTime");

                LocalDate noteDate = (dateStr != null && !dateStr.isEmpty()) 
                    ? LocalDate.parse(dateStr) 
                    : LocalDate.now();
                    
                LocalTime noteTime = (timeStr != null && !timeStr.isEmpty()) 
                    ? LocalTime.parse(timeStr) 
                    : LocalTime.now();

                NursingNote note = new NursingNote(patientId, noteDate, noteTime, 
                                                   content, priority, nurseName);
                nursingNoteDAO.insert(note);
                
                response.sendRedirect("patientDetail?id=" + patientId + "&tab=nursing");
                
            } else if ("update".equals(action)) {
                // 看護メモ更新
                int noteId = Integer.parseInt(request.getParameter("noteId"));
                String content = request.getParameter("content");
                String priority = request.getParameter("priority");
                String dateStr = request.getParameter("noteDate");
                String timeStr = request.getParameter("noteTime");

                NursingNote note = nursingNoteDAO.findById(noteId);
                if (note != null) {
                    note.setNoteDate(LocalDate.parse(dateStr));
                    note.setNoteTime(LocalTime.parse(timeStr));
                    note.setContent(content);
                    note.setPriority(priority);
                    nursingNoteDAO.update(note);
                }
                
                response.sendRedirect("patientDetail?id=" + patientId + "&tab=nursing");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}