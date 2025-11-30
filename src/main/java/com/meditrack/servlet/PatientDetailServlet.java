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

import com.meditrack.dao.MedicationDAO;
import com.meditrack.dao.NursingNoteDAO;
import com.meditrack.dao.PatientDAO;
import com.meditrack.dao.VitalSignDAO;
import com.meditrack.model.Medication;
import com.meditrack.model.NursingNote;
import com.meditrack.model.Patient;
import com.meditrack.model.VitalSign;
/**
 * 患者詳細を表示するサーブレット
 */
@WebServlet("/patientDetail")
public class PatientDetailServlet extends HttpServlet {

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
                // 患者が見つからない場合
                session.setAttribute("errorMessage", "患者が見つかりませんでした");
                response.sendRedirect("patientList");
                return;
            }
            
            // 患者情報をリクエストスコープに設定
            request.setAttribute("patient", patient);
            
            // 現在のタブを取得（デフォルトは基本情報）
            String activeTab = request.getParameter("tab");
            if (activeTab == null || activeTab.trim().isEmpty()) {
                activeTab = "basic";
            }
            request.setAttribute("activeTab", activeTab);
            
            // バイタル記録を常に取得（容態確認タブで表示）
            VitalSignDAO vitalSignDAO = new VitalSignDAO();
            try {
                List<VitalSign> vitalSigns = vitalSignDAO.findByPatientId(patientId);
                request.setAttribute("vitalSigns", vitalSigns);
                System.out.println("✅ バイタル記録を取得: " + vitalSigns.size() + " 件");
            } finally {
                vitalSignDAO.closeConnection();
            }
            
            // 投薬記録を常に取得（投薬管理タブで表示）
            MedicationDAO medicationDAO = new MedicationDAO();
            try {
                List<Medication> medications = medicationDAO.findByPatientId(patientId);
                request.setAttribute("medications", medications);
                System.out.println("✅ 投薬記録を取得: " + medications.size() + " 件");
            } finally {
                medicationDAO.closeConnection();
            }
            
           // 看護メモを常に取得（看護メモタブで表示）
            NursingNoteDAO nursingNoteDAO = new NursingNoteDAO();
            try {
                List<NursingNote> nursingNotes = nursingNoteDAO.findByPatientId(patientId);
                request.setAttribute("nursingNotes", nursingNotes);
                System.out.println("✅ 看護メモを取得: " + nursingNotes.size() + " 件");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("⚠️ 看護メモ取得エラー: " + e.getMessage());
            }
            
            
            System.out.println("✅ 患者詳細を表示: " + patient.getName());
            
            // JSPにフォワード
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/patientDetail.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラーが発生しました");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/error.jsp");
            dispatcher.forward(request, response);
        } finally {
            patientDAO.closeConnection();
        }
    }
}