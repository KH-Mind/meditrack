package com.meditrack.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
 * 患者一覧を表示するサーブレット
 */
@WebServlet("/patientList")
public class PatientListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ログインチェック
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        PatientDAO patientDAO = new PatientDAO();
        
        try {
            // 検索パラメータ取得
            String searchName = request.getParameter("searchName");
            String searchRoom = request.getParameter("searchRoom");
            
            List<Patient> patients;
            
            // 検索条件がある場合は検索、なければ全件取得
            if ((searchName != null && !searchName.trim().isEmpty()) ||
                (searchRoom != null && !searchRoom.trim().isEmpty())) {
                
                patients = patientDAO.search(searchName, searchRoom);
                request.setAttribute("searchName", searchName);
                request.setAttribute("searchRoom", searchRoom);
                
            } else {
                patients = patientDAO.findAll();
            }
            
            // 患者リストをリクエストスコープに設定
            request.setAttribute("patients", patients);
            request.setAttribute("patientCount", patients.size());
            
            System.out.println("✅ 患者一覧を表示: " + patients.size() + "件");
            
            // JSPにフォワード
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/patientList.jsp");
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 検索処理もGETメソッドで処理
        doGet(request, response);
    }
}