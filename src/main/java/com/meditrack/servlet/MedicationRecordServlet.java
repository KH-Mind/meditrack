package com.meditrack.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.meditrack.dao.MedicationDAO;
import com.meditrack.dao.PatientDAO;
import com.meditrack.model.Medication;
import com.meditrack.model.Patient;

/**
 * 投薬記録の登録・編集を処理するサーブレット
 */
@WebServlet("/medicationRecord")
public class MedicationRecordServlet extends HttpServlet {

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
        String patientIdStr = request.getParameter("patientId");
        if (patientIdStr == null || patientIdStr.trim().isEmpty()) {
            response.sendRedirect("patientList");
            return;
        }
        
        int patientId;
        try {
            patientId = Integer.parseInt(patientIdStr);
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
            
            request.setAttribute("patient", patient);
            
            // 編集モードの場合、既存の投薬記録を取得
            String medicationIdStr = request.getParameter("medicationId");
            if (medicationIdStr != null && !medicationIdStr.trim().isEmpty()) {
                int medicationId = Integer.parseInt(medicationIdStr);
                MedicationDAO medicationDAO = new MedicationDAO();
                try {
                    Medication medication = medicationDAO.findById(medicationId);
                    if (medication != null) {
                        request.setAttribute("medication", medication);
                        request.setAttribute("isEdit", true);
                    }
                } finally {
                    medicationDAO.closeConnection();
                }
            }
            
            // JSPにフォワード
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/medicationRecord.jsp");
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ログインチェック
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        // パラメータを取得
        String patientIdStr = request.getParameter("patientId");
        String medicationIdStr = request.getParameter("medicationId");
        String medicineName = request.getParameter("medicineName");
        String dosage = request.getParameter("dosage");
        String frequency = request.getParameter("frequency");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String notes = request.getParameter("notes");
        
        // バリデーション
        if (patientIdStr == null || medicineName == null || medicineName.trim().isEmpty() ||
            dosage == null || dosage.trim().isEmpty() ||
            frequency == null || frequency.trim().isEmpty() ||
            startDateStr == null || startDateStr.trim().isEmpty()) {
            
            session.setAttribute("errorMessage", "必須項目を入力してください");
            response.sendRedirect("medicationRecord?patientId=" + patientIdStr);
            return;
        }
        
        try {
            int patientId = Integer.parseInt(patientIdStr);
            
            // 日時をパース
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = null;
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                endDate = LocalDateTime.parse(endDateStr, formatter);
            }
            
            // Medicationオブジェクトを作成
            Medication medication = new Medication();
            medication.setPatientId(patientId);
            medication.setMedicineName(medicineName.trim());
            medication.setDosage(dosage.trim());
            medication.setFrequency(frequency.trim());
            medication.setStartDate(startDate);
            medication.setEndDate(endDate);
            medication.setNotes(notes != null ? notes.trim() : "");
            
            MedicationDAO medicationDAO = new MedicationDAO();
            try {
                // 編集 or 新規登録
                if (medicationIdStr != null && !medicationIdStr.trim().isEmpty()) {
                    // 編集
                    medication.setMedicationId(Integer.parseInt(medicationIdStr));
                    medicationDAO.update(medication);
                    session.setAttribute("successMessage", "投薬記録を更新しました");
                } else {
                    // 新規登録
                    medicationDAO.insert(medication);
                    session.setAttribute("successMessage", "投薬記録を登録しました");
                }
                
                // 患者詳細画面（投薬タブ）にリダイレクト
                response.sendRedirect("patientDetail?id=" + patientId + "&tab=medication");
                
            } catch (SQLException e) {
                e.printStackTrace();
                session.setAttribute("errorMessage", "投薬記録の登録に失敗しました");
                response.sendRedirect("medicationRecord?patientId=" + patientId);
            } finally {
                medicationDAO.closeConnection();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "入力内容に誤りがあります");
            response.sendRedirect("medicationRecord?patientId=" + patientIdStr);
        }
    }
}