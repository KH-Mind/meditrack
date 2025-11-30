package com.meditrack.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import com.meditrack.util.ValidationUtil;

/**
 * 患者編集処理を行うサーブレット
 */
@WebServlet("/patientEdit")
public class PatientEditServlet extends HttpServlet {
    
    /**
     * 編集画面を表示（GET）
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
            
            // 編集画面を表示
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/patientEdit.jsp");
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
     * 患者更新処理（POST）
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
        
        // パラメータ取得
        String idStr = request.getParameter("patientId");
        String name = request.getParameter("name");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String roomNumber = request.getParameter("roomNumber");
        String admissionDateStr = request.getParameter("admissionDate");
        String doctorName = request.getParameter("doctorName");
        
        // 患者IDの検証
        int patientId;
        try {
            patientId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("patientList");
            return;
        }
        
        // バリデーション
        List<String> errors = new ArrayList<>();
        
        if (!ValidationUtil.isValidString(name)) {
            errors.add("患者名を入力してください");
        }
        
        if (!ValidationUtil.isValidDate(birthDateStr)) {
            errors.add("生年月日を正しい形式で入力してください");
        } else {
            LocalDate birthDate = LocalDate.parse(birthDateStr);
            if (birthDate.isAfter(LocalDate.now())) {
                errors.add("生年月日が未来の日付です");
            }
        }
        
        if (!ValidationUtil.isValidString(gender)) {
            errors.add("性別を選択してください");
        }
        
        if (ValidationUtil.isValidString(roomNumber)) {
            if (!ValidationUtil.isValidRoomNumber(roomNumber)) {
                errors.add("病室番号は3桁の数字で入力してください（例: 301）");
            }
        }
        
        if (!ValidationUtil.isValidDate(admissionDateStr)) {
            errors.add("入院日を正しい形式で入力してください");
        } else {
            LocalDate admissionDate = LocalDate.parse(admissionDateStr);
            if (admissionDate.isAfter(LocalDate.now())) {
                errors.add("入院日が未来の日付です");
            }
        }
        
        // エラーがある場合は編集画面に戻る
        if (!errors.isEmpty()) {
            // 入力値を保持して編集画面に戻る
            Patient patient = new Patient();
            patient.setPatientId(patientId);
            patient.setName(name);
            patient.setBirthDate(LocalDate.parse(birthDateStr));
            patient.setGender(gender);
            patient.setRoomNumber(roomNumber);
            patient.setAdmissionDate(LocalDate.parse(admissionDateStr));
            patient.setDoctorName(doctorName);
            
            request.setAttribute("patient", patient);
            request.setAttribute("errors", errors);
            
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/patientEdit.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Patientオブジェクト作成
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setName(name);
        patient.setBirthDate(LocalDate.parse(birthDateStr));
        patient.setGender(gender);
        patient.setRoomNumber(roomNumber);
        patient.setAdmissionDate(LocalDate.parse(admissionDateStr));
        patient.setDoctorName(doctorName);
        
        // データベースを更新
        PatientDAO patientDAO = new PatientDAO();
        try {
            boolean success = patientDAO.update(patient);
            
            if (success) {
                System.out.println("✅ 患者情報更新成功: " + name);
                
                // 成功メッセージをセッションに保存
                session.setAttribute("successMessage", "患者「" + name + "」の情報を更新しました");
                
                // 患者詳細画面へリダイレクト
                response.sendRedirect("patientDetail?id=" + patientId);
            } else {
                errors.add("患者情報の更新に失敗しました");
                request.setAttribute("patient", patient);
                request.setAttribute("errors", errors);
                
                RequestDispatcher dispatcher = 
                    request.getRequestDispatcher("/WEB-INF/jsp/patientEdit.jsp");
                dispatcher.forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            errors.add("データベースエラーが発生しました");
            request.setAttribute("patient", patient);
            request.setAttribute("errors", errors);
            
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/patientEdit.jsp");
            dispatcher.forward(request, response);
        } finally {
            patientDAO.closeConnection();
        }
    }
}