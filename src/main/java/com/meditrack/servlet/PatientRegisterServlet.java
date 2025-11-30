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
 * 患者登録処理を行うサーブレット
 */
@WebServlet("/patientRegister")
public class PatientRegisterServlet extends HttpServlet {
    
    /**
     * 登録画面を表示（GET）
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
        
        // 登録画面を表示
        RequestDispatcher dispatcher = 
            request.getRequestDispatcher("/WEB-INF/jsp/patientRegister.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 患者登録処理（POST）
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
        String name = request.getParameter("name");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String roomNumber = request.getParameter("roomNumber");
        String admissionDateStr = request.getParameter("admissionDate");
        String doctorName = request.getParameter("doctorName");
        
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
        
        // エラーがある場合は登録画面に戻る
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("name", name);
            request.setAttribute("birthDate", birthDateStr);
            request.setAttribute("gender", gender);
            request.setAttribute("roomNumber", roomNumber);
            request.setAttribute("admissionDate", admissionDateStr);
            request.setAttribute("doctorName", doctorName);
            
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/patientRegister.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // Patientオブジェクト作成
        Patient patient = new Patient();
        patient.setName(name);
        patient.setBirthDate(LocalDate.parse(birthDateStr));
        patient.setGender(gender);
        patient.setRoomNumber(roomNumber);
        patient.setAdmissionDate(LocalDate.parse(admissionDateStr));
        patient.setDoctorName(doctorName);
        
        // データベースに登録
        PatientDAO patientDAO = new PatientDAO();
        try {
            boolean success = patientDAO.insert(patient);
            
            if (success) {
                System.out.println("✅ 患者登録成功: " + name);
                
                // 成功メッセージをセッションに保存
                session.setAttribute("successMessage", "患者「" + name + "」を登録しました");
                
                // 患者一覧へリダイレクト
                response.sendRedirect("patientList");
            } else {
                errors.add("患者の登録に失敗しました");
                request.setAttribute("errors", errors);
                
                RequestDispatcher dispatcher = 
                    request.getRequestDispatcher("/WEB-INF/jsp/patientRegister.jsp");
                dispatcher.forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            errors.add("データベースエラーが発生しました");
            request.setAttribute("errors", errors);
            
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/patientRegister.jsp");
            dispatcher.forward(request, response);
            
        } finally {
            patientDAO.closeConnection();
        }
    }
}