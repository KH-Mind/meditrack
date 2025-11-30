package com.meditrack.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.meditrack.dao.PatientDAO;
import com.meditrack.model.Patient;

/**
 * 患者削除処理を行うサーブレット
 */
@WebServlet("/patientDelete")
public class PatientDeleteServlet extends HttpServlet {
    
    /**
     * 削除確認画面を表示（GET）
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ログインチェック
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        // 患者IDを取得
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("patientList");
            return;
        }
        
        int patientId;
        try {
            patientId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("patientList");
            return;
        }
        
        // 患者情報を取得
        PatientDAO patientDAO = new PatientDAO();
        try {
            Patient patient = patientDAO.findById(patientId);
            
            if (patient == null) {
                session.setAttribute("errorMessage", "患者が見つかりませんでした");
                response.sendRedirect("patientList");
                return;
            }
            
            // 患者情報をリクエストスコープに設定
            request.setAttribute("patient", patient);
            
            // 削除確認画面を表示
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/patientDelete.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラーが発生しました");
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp");
            dispatcher.forward(request, response);
        } finally {
            patientDAO.closeConnection();
        }
    }
    
    /**
     * 患者削除処理（POST）
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ログインチェック
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        // 患者IDを取得
        String idStr = request.getParameter("patientId");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("patientList");
            return;
        }
        
        int patientId;
        try {
            patientId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("patientList");
            return;
        }
        
        // 患者情報を取得（削除前に名前を保存）
        PatientDAO patientDAO = new PatientDAO();
        String patientName = "";
        
        try {
            Patient patient = patientDAO.findById(patientId);
            if (patient != null) {
                patientName = patient.getName();
            }
            
            // 患者を削除
            boolean success = patientDAO.delete(patientId);
            
            if (success) {
                System.out.println("✅ 患者削除成功: " + patientName + " (ID: " + patientId + ")");
                
                // 成功メッセージをセッションに保存
                session.setAttribute("successMessage", 
                    "患者「" + patientName + "」を削除しました");
                
                // 患者一覧へリダイレクト
                response.sendRedirect("patientList");
            } else {
                session.setAttribute("errorMessage", "患者の削除に失敗しました");
                response.sendRedirect("patientDetail?id=" + patientId);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "データベースエラーが発生しました");
            response.sendRedirect("patientList");
        } finally {
            patientDAO.closeConnection();
        }
    }
}