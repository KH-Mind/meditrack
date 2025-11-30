package com.meditrack.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * バイタルサイン情報を表すモデルクラス
 */
public class VitalSign {
    private int vitalId;                    // バイタルID
    private int patientId;                  // 患者ID
    private LocalDateTime measuredAt;       // 測定日時
    private Double temperature;             // 体温（℃）
    private Integer bloodPressureHigh;      // 収縮期血圧（最高血圧）
    private Integer bloodPressureLow;       // 拡張期血圧（最低血圧）
    private Integer pulse;                  // 脈拍（回/分）
    private Integer spo2;                   // 酸素飽和度（%）
    private String memo;                    // 備考
    
    /**
     * デフォルトコンストラクタ
     */
    public VitalSign() {
    }
    
    /**
     * 全項目を設定するコンストラクタ
     */
    public VitalSign(int vitalId, int patientId, LocalDateTime measuredAt,
                     Double temperature, Integer bloodPressureHigh, Integer bloodPressureLow,
                     Integer pulse, Integer spo2, String memo) {
        this.vitalId = vitalId;
        this.patientId = patientId;
        this.measuredAt = measuredAt;
        this.temperature = temperature;
        this.bloodPressureHigh = bloodPressureHigh;
        this.bloodPressureLow = bloodPressureLow;
        this.pulse = pulse;
        this.spo2 = spo2;
        this.memo = memo;
    }
    
    /**
     * 測定日時を表示用フォーマットで取得
     * @return yyyy/MM/dd HH:mm 形式
     */
    public String getFormattedMeasuredAt() {
        if (measuredAt == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return measuredAt.format(formatter);
    }
    
    /**
     * 血圧を表示用フォーマットで取得
     * @return "120/80" 形式
     */
    public String getFormattedBloodPressure() {
        if (bloodPressureHigh == null || bloodPressureLow == null) {
            return "-";
        }
        return bloodPressureHigh + "/" + bloodPressureLow;
    }
    
    /**
     * 体温の異常判定
     * @return true: 異常値（37.5℃以上または35.0℃未満）
     */
    public boolean isAbnormalTemperature() {
        if (temperature == null) {
            return false;
        }
        return temperature >= 37.5 || temperature < 35.0;
    }
    
    /**
     * 血圧の異常判定
     * @return true: 異常値（最高血圧140以上または最低血圧90以上、または最高血圧100未満）
     */
    public boolean isAbnormalBloodPressure() {
        if (bloodPressureHigh == null || bloodPressureLow == null) {
            return false;
        }
        return bloodPressureHigh >= 140 || bloodPressureLow >= 90 || bloodPressureHigh < 100;
    }
    
    /**
     * SpO2の異常判定
     * @return true: 異常値（95%未満）
     */
    public boolean isAbnormalSpO2() {
        if (spo2 == null) {
            return false;
        }
        return spo2 < 95;
    }
    
    // ========================================
    // ゲッター・セッター
    // ========================================
    
    public int getVitalId() {
        return vitalId;
    }
    
    public void setVitalId(int vitalId) {
        this.vitalId = vitalId;
    }
    
    public int getPatientId() {
        return patientId;
    }
    
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    
    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }
    
    public void setMeasuredAt(LocalDateTime measuredAt) {
        this.measuredAt = measuredAt;
    }
    
    public Double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    
    public Integer getBloodPressureHigh() {
        return bloodPressureHigh;
    }
    
    public void setBloodPressureHigh(Integer bloodPressureHigh) {
        this.bloodPressureHigh = bloodPressureHigh;
    }
    
    public Integer getBloodPressureLow() {
        return bloodPressureLow;
    }
    
    public void setBloodPressureLow(Integer bloodPressureLow) {
        this.bloodPressureLow = bloodPressureLow;
    }
    
    public Integer getPulse() {
        return pulse;
    }
    
    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }
    
    public Integer getSpo2() {
        return spo2;
    }
    
    public void setSpo2(Integer spo2) {
        this.spo2 = spo2;
    }
    
    public String getMemo() {
        return memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    @Override
    public String toString() {
        return "VitalSign{" +
                "vitalId=" + vitalId +
                ", patientId=" + patientId +
                ", measuredAt=" + measuredAt +
                ", temperature=" + temperature +
                ", bloodPressure=" + getFormattedBloodPressure() +
                ", pulse=" + pulse +
                ", spo2=" + spo2 +
                '}';
    }
}