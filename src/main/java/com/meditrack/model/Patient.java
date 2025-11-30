package com.meditrack.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * 患者情報を表すモデルクラス
 */
public class Patient {
    
    private int patientId;              // 患者ID
    private String name;                // 氏名
    private LocalDate birthDate;        // 生年月日
    private String gender;              // 性別
    private String roomNumber;          // 病室番号
    private LocalDate admissionDate;    // 入院日
    private String doctorName;          // 主治医名
    
    /**
     * デフォルトコンストラクタ
     */
    public Patient() {
    }
    
    /**
     * 全項目を設定するコンストラクタ
     */
    public Patient(int patientId, String name, LocalDate birthDate, String gender,
                   String roomNumber, LocalDate admissionDate, String doctorName) {
        this.patientId = patientId;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.roomNumber = roomNumber;
        this.admissionDate = admissionDate;
        this.doctorName = doctorName;
    }
    
    /**
     * 年齢を計算して取得
     * @return 年齢
     */
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    /**
     * 入院日数を計算して取得
     * @return 入院日数
     */
    public int getDaysInHospital() {
        if (admissionDate == null) {
            return 0;
        }
        return Period.between(admissionDate, LocalDate.now()).getDays();
    }
    
    // ========================================
    // ゲッター・セッター
    // ========================================
    
    public int getPatientId() {
        return patientId;
    }
    
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public LocalDate getAdmissionDate() {
        return admissionDate;
    }
    
    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", age=" + getAge() +
                ", gender='" + gender + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }
}