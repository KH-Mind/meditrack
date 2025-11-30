package com.meditrack.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.meditrack.model.Medication;

/**
 * 投薬記録のデータアクセスオブジェクト
 */
public class MedicationDAO extends BaseDAO<Medication> {

    /**
     * 患者IDで投薬記録を検索
     * @param patientId 患者ID
     * @return 投薬記録のリスト
     * @throws SQLException
     */
    public List<Medication> findByPatientId(int patientId) throws SQLException {
        List<Medication> medications = new ArrayList<>();
        String sql = "SELECT * FROM medications WHERE patient_id = ? ORDER BY start_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medications.add(mapResultSetToMedication(rs));
                }
            }
        }
        
        System.out.println("✅ 患者ID " + patientId + " の投薬記録を " + medications.size() + " 件取得しました");
        return medications;
    }
    
    /**
     * 現在有効な投薬記録を取得
     * @param patientId 患者ID
     * @return 有効な投薬記録のリスト
     * @throws SQLException
     */
    public List<Medication> findActiveByPatientId(int patientId) throws SQLException {
        List<Medication> medications = new ArrayList<>();
        String sql = "SELECT * FROM medications WHERE patient_id = ? " +
                    "AND start_date <= NOW() " +
                    "AND (end_date IS NULL OR end_date >= NOW()) " +
                    "ORDER BY start_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medications.add(mapResultSetToMedication(rs));
                }
            }
        }
        
        System.out.println("✅ 患者ID " + patientId + " の有効な投薬記録を " + medications.size() + " 件取得しました");
        return medications;
    }
    
    
    @Override
    public List<Medication> findAll() throws SQLException {
        List<Medication> medications = new ArrayList<>();
        String sql = "SELECT * FROM medications ORDER BY start_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medications.add(mapResultSetToMedication(rs));
            }
        }
        return medications;
    }

    @Override
    public boolean insert(Medication medication) throws SQLException {
        String sql = "INSERT INTO medications (patient_id, medicine_name, dosage, frequency, " +
                     "start_date, end_date, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, medication.getPatientId());
            stmt.setString(2, medication.getMedicineName());
            stmt.setString(3, medication.getDosage());
            stmt.setString(4, medication.getFrequency());
            stmt.setTimestamp(5, Timestamp.valueOf(medication.getStartDate()));
            
            if (medication.getEndDate() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(medication.getEndDate()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            
            stmt.setString(7, medication.getNotes());
            
            return stmt.executeUpdate() > 0;  // ← int から boolean に変更
        }
    }
    @Override
    public boolean update(Medication medication) throws SQLException {
        String sql = "UPDATE medications SET medicine_name = ?, dosage = ?, frequency = ?, " +
                     "start_date = ?, end_date = ?, notes = ? WHERE medication_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medication.getMedicineName());
            stmt.setString(2, medication.getDosage());
            stmt.setString(3, medication.getFrequency());
            stmt.setTimestamp(4, Timestamp.valueOf(medication.getStartDate()));
            
            if (medication.getEndDate() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(medication.getEndDate()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            
            stmt.setString(6, medication.getNotes());
            stmt.setInt(7, medication.getMedicationId());
            
            return stmt.executeUpdate() > 0;  // ← int から boolean に変更
        }
    }

    @Override
    public boolean delete(int medicationId) throws SQLException {
        String sql = "DELETE FROM medications WHERE medication_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medicationId);
            return stmt.executeUpdate() > 0;  // ← int から boolean に変更
        }
    }
    
    
    
    /**
     * IDで投薬記録を検索
     * @param medicationId 投薬ID
     * @return 投薬記録（見つからない場合null）
     * @throws SQLException
     */
    public Medication findById(int medicationId) throws SQLException {
        String sql = "SELECT * FROM medications WHERE medication_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medicationId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedication(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * ResultSetからMedicationオブジェクトにマッピング
     * @param rs ResultSet
     * @return Medication
     * @throws SQLException
     */
    private Medication mapResultSetToMedication(ResultSet rs) throws SQLException {
        Medication medication = new Medication();
        medication.setMedicationId(rs.getInt("medication_id"));
        medication.setPatientId(rs.getInt("patient_id"));
        medication.setMedicineName(rs.getString("medicine_name"));
        medication.setDosage(rs.getString("dosage"));
        medication.setFrequency(rs.getString("frequency"));
        
        Timestamp startDate = rs.getTimestamp("start_date");
        if (startDate != null) {
            medication.setStartDate(startDate.toLocalDateTime());
        }
        
        Timestamp endDate = rs.getTimestamp("end_date");
        if (endDate != null) {
            medication.setEndDate(endDate.toLocalDateTime());
        }
        
        medication.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            medication.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return medication;
    }
}