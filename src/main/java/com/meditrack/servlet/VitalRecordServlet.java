package com.meditrack.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.meditrack.dao.VitalSignDAO;
import com.meditrack.model.Patient;
import com.meditrack.model.VitalSign;
import com.meditrack.util.ValidationUtil;

/**
 * バイタルサイン記録処理を行うサーブレット
 */
@WebServlet("/vitalRecord")
public class VitalRecordServlet extends HttpServlet {
    
    /**
     * バイタル記録画面を表示（GET）
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
        
        // 患者情報を取得
        PatientDAO patientDAO = new PatientDAO();
        VitalSignDAO vitalSignDAO = new VitalSignDAO();
        
        try {
            Patient patient = patientDAO.findById(patientId);
            
            if (patient == null) {
                session.setAttribute("errorMessage", "患者が見つかりませんでした");
                response.sendRedirect("patientList");
                return;
            }
            
            // 最新のバイタル記録を取得（入力の参考用）
            VitalSign latestVital = vitalSignDAO.findLatestByPatientId(patientId);
            
            // 患者情報をリクエストスコープに設定
            request.setAttribute("patient", patient);
            request.setAttribute("latestVital", latestVital);
            
            // 現在日時をデフォルト値として設定
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            request.setAttribute("defaultDateTime", now.format(formatter));
            
            // バイタル記録画面を表示
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/vitalRecord.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラーが発生しました");
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp");
            dispatcher.forward(request, response);
        } finally {
            patientDAO.closeConnection();
            vitalSignDAO.closeConnection();
        }
    }
    
    /**
     * バイタル記録登録処理（POST）
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
        String patientIdStr = request.getParameter("patientId");
        String measuredAtStr = request.getParameter("measuredAt");
        String temperatureStr = request.getParameter("temperature");
        String bloodPressureHighStr = request.getParameter("bloodPressureHigh");
        String bloodPressureLowStr = request.getParameter("bloodPressureLow");
        String pulseStr = request.getParameter("pulse");
        String spo2Str = request.getParameter("spo2");
        String memo = request.getParameter("memo");
        
        // 患者IDの検証
        int patientId;
        try {
            patientId = Integer.parseInt(patientIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("patientList");
            return;
        }
        
        // バリデーション
        List<String> errors = new ArrayList<>();
        
        // 測定日時のチェック
        LocalDateTime measuredAt = null;
        if (!ValidationUtil.isValidString(measuredAtStr)) {
            errors.add("測定日時を入力してください");
        } else {
            try {
                measuredAt = LocalDateTime.parse(measuredAtStr);
                if (measuredAt.isAfter(LocalDateTime.now())) {
                    errors.add("測定日時が未来の日時です");
                }
            } catch (Exception e) {
                errors.add("測定日時の形式が正しくありません");
            }
        }
        
        // 少なくとも1つの値が入力されているかチェック
        if (!ValidationUtil.isValidString(temperatureStr) &&
            !ValidationUtil.isValidString(bloodPressureHighStr) &&
            !ValidationUtil.isValidString(bloodPressureLowStr) &&
            !ValidationUtil.isValidString(pulseStr) &&
            !ValidationUtil.isValidString(spo2Str)) {
            errors.add("少なくとも1つの測定値を入力してください");
        }
        
        // 体温のバリデーション
        Double temperature = null;
        if (ValidationUtil.isValidString(temperatureStr)) {
            if (!ValidationUtil.isValidNumber(temperatureStr)) {
                errors.add("体温は数値で入力してください");
            } else {
                temperature = Double.parseDouble(temperatureStr);
                if (!ValidationUtil.isValidTemperature(temperature)) {
                    errors.add("体温は34.0〜42.0℃の範囲で入力してください");
                }
            }
        }
        
        // 血圧のバリデーション
        Integer bloodPressureHigh = null;
        Integer bloodPressureLow = null;
        if (ValidationUtil.isValidString(bloodPressureHighStr)) {
            if (!ValidationUtil.isValidInteger(bloodPressureHighStr)) {
                errors.add("収縮期血圧は整数で入力してください");
            } else {
                bloodPressureHigh = Integer.parseInt(bloodPressureHighStr);
                if (!ValidationUtil.isValidBloodPressure(bloodPressureHigh)) {
                    errors.add("収縮期血圧は40〜250の範囲で入力してください");
                }
            }
        }
        
        if (ValidationUtil.isValidString(bloodPressureLowStr)) {
            if (!ValidationUtil.isValidInteger(bloodPressureLowStr)) {
                errors.add("拡張期血圧は整数で入力してください");
            } else {
                bloodPressureLow = Integer.parseInt(bloodPressureLowStr);
                if (!ValidationUtil.isValidBloodPressure(bloodPressureLow)) {
                    errors.add("拡張期血圧は40〜250の範囲で入力してください");
                }
            }
        }
        
        // 血圧の整合性チェック
        if (bloodPressureHigh != null && bloodPressureLow != null) {
            if (bloodPressureHigh <= bloodPressureLow) {
                errors.add("収縮期血圧は拡張期血圧より高い値を入力してください");
            }
        }
        
        // 脈拍のバリデーション
        Integer pulse = null;
        if (ValidationUtil.isValidString(pulseStr)) {
            if (!ValidationUtil.isValidInteger(pulseStr)) {
                errors.add("脈拍は整数で入力してください");
            } else {
                pulse = Integer.parseInt(pulseStr);
                if (!ValidationUtil.isValidPulse(pulse)) {
                    errors.add("脈拍は30〜200の範囲で入力してください");
                }
            }
        }
        
        // SpO2のバリデーション
        Integer spo2 = null;
        if (ValidationUtil.isValidString(spo2Str)) {
            if (!ValidationUtil.isValidInteger(spo2Str)) {
                errors.add("SpO2は整数で入力してください");
            } else {
                spo2 = Integer.parseInt(spo2Str);
                if (!ValidationUtil.isValidSpO2(spo2)) {
                    errors.add("SpO2は70〜100の範囲で入力してください");
                }
            }
        }
        
        // エラーがある場合は記録画面に戻る
        if (!errors.isEmpty()) {
            PatientDAO patientDAO = new PatientDAO();
            try {
                Patient patient = patientDAO.findById(patientId);
                request.setAttribute("patient", patient);
                request.setAttribute("errors", errors);
                
                // 入力値を保持
                request.setAttribute("measuredAt", measuredAtStr);
                request.setAttribute("temperature", temperatureStr);
                request.setAttribute("bloodPressureHigh", bloodPressureHighStr);
                request.setAttribute("bloodPressureLow", bloodPressureLowStr);
                request.setAttribute("pulse", pulseStr);
                request.setAttribute("spo2", spo2Str);
                request.setAttribute("memo", memo);
                
                RequestDispatcher dispatcher = 
                    request.getRequestDispatcher("/WEB-INF/jsp/vitalRecord.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                patientDAO.closeConnection();
            }
        }
        
        // VitalSignオブジェクト作成
        VitalSign vitalSign = new VitalSign();
        vitalSign.setPatientId(patientId);
        vitalSign.setMeasuredAt(measuredAt);
        vitalSign.setTemperature(temperature);
        vitalSign.setBloodPressureHigh(bloodPressureHigh);
        vitalSign.setBloodPressureLow(bloodPressureLow);
        vitalSign.setPulse(pulse);
        vitalSign.setSpo2(spo2);
        vitalSign.setMemo(memo);
        
        // データベースに登録
        VitalSignDAO vitalSignDAO = new VitalSignDAO();
        try {
            boolean success = vitalSignDAO.insert(vitalSign);
            
            if (success) {
                System.out.println("✅ バイタル記録成功: 患者ID=" + patientId);
                
                // 成功メッセージをセッションに保存
                session.setAttribute("successMessage", "バイタルサインを記録しました");
                
                // 患者詳細画面（容態確認タブ）へリダイレクト
                response.sendRedirect("patientDetail?id=" + patientId + "&tab=vital");
            } else {
                errors.add("バイタルサインの記録に失敗しました");
                request.setAttribute("errors", errors);
                
                RequestDispatcher dispatcher = 
                    request.getRequestDispatcher("/WEB-INF/jsp/vitalRecord.jsp");
                dispatcher.forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            errors.add("データベースエラーが発生しました");
            request.setAttribute("errors", errors);
            
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("/WEB-INF/jsp/vitalRecord.jsp");
            dispatcher.forward(request, response);
        } finally {
            vitalSignDAO.closeConnection();
        }
    }
}