package com.meditrack.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.meditrack.model.Patient;

/**
 * 患者情報のデータベースアクセスクラス
 */
public class PatientDAO extends BaseDAO<Patient> {
    
    @Override
    public List<Patient> findAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY room_number, name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        }
        return patients;
    }
    
    @Override
    public Patient findById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * 患者名と病室番号で検索
     * @param name 患者名（部分一致）
     * @param roomNumber 病室番号（部分一致）
     * @return 検索結果のリスト
     * @throws SQLException
     */
    public List<Patient> search(String name, String roomNumber) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM patients WHERE 1=1");
        
        // 動的にWHERE句を構築
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
        }
        if (roomNumber != null && !roomNumber.trim().isEmpty()) {
            sql.append(" AND room_number LIKE ?");
        }
        sql.append(" ORDER BY room_number, name");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (name != null && !name.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + name + "%");
            }
            if (roomNumber != null && !roomNumber.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + roomNumber + "%");
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }
        }
        return patients;
    }
    
    @Override
    public boolean insert(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (name, birth_date, gender, room_number, " +
                     "admission_date, doctor_name) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, patient.getName());
            ps.setDate(2, Date.valueOf(patient.getBirthDate()));
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getRoomNumber());
            ps.setDate(5, Date.valueOf(patient.getAdmissionDate()));
            ps.setString(6, patient.getDoctorName());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean update(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET name = ?, birth_date = ?, gender = ?, " +
                     "room_number = ?, admission_date = ?, doctor_name = ? " +
                     "WHERE patient_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, patient.getName());
            ps.setDate(2, Date.valueOf(patient.getBirthDate()));
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getRoomNumber());
            ps.setDate(5, Date.valueOf(patient.getAdmissionDate()));
            ps.setString(6, patient.getDoctorName());
            ps.setInt(7, patient.getPatientId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * 患者の総数を取得
     * @return 患者数
     * @throws SQLException
     */
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM patients";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    /**
     * ResultSetからPatientオブジェクトへマッピング
     * @param rs ResultSet
     * @return Patient
     * @throws SQLException
     */
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setName(rs.getString("name"));
        patient.setBirthDate(rs.getDate("birth_date").toLocalDate());
        patient.setGender(rs.getString("gender"));
        patient.setRoomNumber(rs.getString("room_number"));
        patient.setAdmissionDate(rs.getDate("admission_date").toLocalDate());
        patient.setDoctorName(rs.getString("doctor_name"));
        return patient;
    }
}