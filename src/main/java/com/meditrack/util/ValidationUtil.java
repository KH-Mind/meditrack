package com.meditrack.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * 入力値のバリデーションを行うユーティリティクラス
 */
public class ValidationUtil {
    
    /**
     * 文字列が空でないかチェック
     * @param str チェックする文字列
     * @return 有効ならtrue
     */
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * 日付文字列が有効な形式かチェック
     * @param dateStr 日付文字列（yyyy-MM-dd形式）
     * @return 有効ならtrue
     */
    public static boolean isValidDate(String dateStr) {
        if (!isValidString(dateStr)) {
            return false;
        }
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * 数値文字列が有効かチェック
     * @param numStr 数値文字列
     * @return 有効ならtrue
     */
    public static boolean isValidNumber(String numStr) {
        if (!isValidString(numStr)) {
            return false;
        }
        try {
            Double.parseDouble(numStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 整数文字列が有効かチェック
     * @param numStr 整数文字列
     * @return 有効ならtrue
     */
    public static boolean isValidInteger(String numStr) {
        if (!isValidString(numStr)) {
            return false;
        }
        try {
            Integer.parseInt(numStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 数値が範囲内かチェック
     * @param value チェックする値
     * @param min 最小値
     * @param max 最大値
     * @return 範囲内ならtrue
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
    
    /**
     * XSS対策：HTMLタグをエスケープ
     * @param input 入力文字列
     * @return エスケープされた文字列
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;")
                    .replace("/", "&#x2F;");
    }
    
    /**
     * 病室番号の形式チェック（3桁の数字）
     * @param roomNumber 病室番号
     * @return 有効ならtrue
     */
    public static boolean isValidRoomNumber(String roomNumber) {
        if (!isValidString(roomNumber)) {
            return false;
        }
        return roomNumber.matches("^\\d{3}$");
    }
    
    /**
     * 体温の妥当性チェック（34.0〜42.0℃）
     * @param temperature 体温
     * @return 妥当ならtrue
     */
    public static boolean isValidTemperature(double temperature) {
        return isInRange(temperature, 34.0, 42.0);
    }
    
    /**
     * 血圧の妥当性チェック（40〜250 mmHg）
     * @param bloodPressure 血圧
     * @return 妥当ならtrue
     */
    public static boolean isValidBloodPressure(int bloodPressure) {
        return isInRange(bloodPressure, 40, 250);
    }
    
    /**
     * 脈拍の妥当性チェック（30〜200 bpm）
     * @param pulse 脈拍
     * @return 妥当ならtrue
     */
    public static boolean isValidPulse(int pulse) {
        return isInRange(pulse, 30, 200);
    }
    
    /**
     * SpO2の妥当性チェック（70〜100%）
     * @param spo2 SpO2
     * @return 妥当ならtrue
     */
    public static boolean isValidSpO2(int spo2) {
        return isInRange(spo2, 70, 100);
    }
}