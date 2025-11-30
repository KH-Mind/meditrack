package com.meditrack.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 投薬記録を表すモデルクラス
 */
public class Medication {
    private int medicationId;           // 投薬ID
    private int patientId;              // 患者ID
    private String medicineName;        // 薬剤名
    private String dosage;              // 用量（例：1錠、10mg）
    private String frequency;           // 頻度（例：1日3回、朝夕2回）
    private LocalDateTime startDate;    // 開始日時
    private LocalDateTime endDate;      // 終了日時
    private String notes;               // 備考
    private LocalDateTime createdAt;    // 登録日時
    
    /**
     * デフォルトコンストラクタ
     */
    public Medication() {
    }
    
    /**
     * 全項目を設定するコンストラクタ
     */
    public Medication(int medicationId, int patientId, String medicineName,
                     String dosage, String frequency, LocalDateTime startDate,
                     LocalDateTime endDate, String notes, LocalDateTime createdAt) {
        this.medicationId = medicationId;
        this.patientId = patientId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.createdAt = createdAt;
    }
    
    // Getter/Setter
    public int getMedicationId() {
        return medicationId;
    }
    
    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }
    
    public int getPatientId() {
        return patientId;
    }
    
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    
    public String getMedicineName() {
        return medicineName;
    }
    
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
    
    public String getDosage() {
        return dosage;
    }
    
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * 開始日時を表示用フォーマットで取得
     * @return yyyy/MM/dd HH:mm形式
     */
    public String getFormattedStartDate() {
        if (startDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return startDate.format(formatter);
    }
    
    /**
     * 終了日時を表示用フォーマットで取得
     * @return yyyy/MM/dd HH:mm形式
     */
    public String getFormattedEndDate() {
        if (endDate == null) {
            return "継続中";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return endDate.format(formatter);
    }
    
    /**
     * 投薬が現在有効かどうかを判定
     * @return 有効ならtrue
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return startDate.isBefore(now) && (endDate == null || endDate.isAfter(now));
    }
}