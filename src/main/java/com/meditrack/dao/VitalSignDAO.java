package com.meditrack.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.meditrack.model.VitalSign;

/**
 * バイタルサイン情報のデータベースアクセスクラス
 */
public class VitalSignDAO extends BaseDAO<VitalSign> {
    
    /**
     * 特定の患者のバイタル記録を全件取得（新しい順）
     * @param patientId 患者ID
     * @return バイタルサインのリスト
     * @throws SQLException
     */
    public List<VitalSign> findByPatientId(int patientId) throws SQLException {
        List<VitalSign> vitalSigns = new ArrayList<>();
        String sql = "SELECT * FROM vital_signs WHERE patient_id = ? ORDER BY measured_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vitalSigns.add(mapResultSetToVitalSign(rs));
                }
            }
        }
        return vitalSigns;
    }
    
    /**
     * 特定の患者の最新バイタル記録を取得
     * @param patientId 患者ID
     * @return 最新のバイタルサイン（存在しない場合はnull）
     * @throws SQLException
     */
    public VitalSign findLatestByPatientId(int patientId) throws SQLException {
        String sql = "SELECT * FROM vital_signs WHERE patient_id = ? ORDER BY measured_at DESC LIMIT 1";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVitalSign(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * 特定の患者の直近N件のバイタル記録を取得
     * @param patientId 患者ID
     * @param limit 取得件数
     * @return バイタルサインのリスト
     * @throws SQLException
     */
    public List<VitalSign> findRecentByPatientId(int patientId, int limit) throws SQLException {
        List<VitalSign> vitalSigns = new ArrayList<>();
        String sql = "SELECT * FROM vital_signs WHERE patient_id = ? ORDER BY measured_at DESC LIMIT ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vitalSigns.add(mapResultSetToVitalSign(rs));
                }
            }
        }
        return vitalSigns;
    }
    
    @Override
    public List<VitalSign> findAll() throws SQLException {
        List<VitalSign> vitalSigns = new ArrayList<>();
        String sql = "SELECT * FROM vital_signs ORDER BY measured_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                vitalSigns.add(mapResultSetToVitalSign(rs));
            }
        }
        return vitalSigns;
    }
    
    @Override
    public VitalSign findById(int id) throws SQLException {
        String sql = "SELECT * FROM vital_signs WHERE vital_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVitalSign(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean insert(VitalSign vitalSign) throws SQLException {
        String sql = "INSERT INTO vital_signs (patient_id, measured_at, temperature, " +
                     "blood_pressure_high, blood_pressure_low, pulse, spo2, memo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, vitalSign.getPatientId());
            ps.setTimestamp(2, Timestamp.valueOf(vitalSign.getMeasuredAt()));
            
            // NULL許容のフィールドは、nullの場合にsetNullを使う
            if (vitalSign.getTemperature() != null) {
                ps.setDouble(3, vitalSign.getTemperature());
            } else {
                ps.setNull(3, java.sql.Types.DOUBLE);
            }
            
            if (vitalSign.getBloodPressureHigh() != null) {
                ps.setInt(4, vitalSign.getBloodPressureHigh());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            if (vitalSign.getBloodPressureLow() != null) {
                ps.setInt(5, vitalSign.getBloodPressureLow());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            
            if (vitalSign.getPulse() != null) {
                ps.setInt(6, vitalSign.getPulse());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            
            if (vitalSign.getSpo2() != null) {
                ps.setInt(7, vitalSign.getSpo2());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            
            ps.setString(8, vitalSign.getMemo());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean update(VitalSign vitalSign) throws SQLException {
        String sql = "UPDATE vital_signs SET measured_at = ?, temperature = ?, " +
                     "blood_pressure_high = ?, blood_pressure_low = ?, pulse = ?, " +
                     "spo2 = ?, memo = ? WHERE vital_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(vitalSign.getMeasuredAt()));
            
            if (vitalSign.getTemperature() != null) {
                ps.setDouble(2, vitalSign.getTemperature());
            } else {
                ps.setNull(2, java.sql.Types.DOUBLE);
            }
            
            if (vitalSign.getBloodPressureHigh() != null) {
                ps.setInt(3, vitalSign.getBloodPressureHigh());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            if (vitalSign.getBloodPressureLow() != null) {
                ps.setInt(4, vitalSign.getBloodPressureLow());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            if (vitalSign.getPulse() != null) {
                ps.setInt(5, vitalSign.getPulse());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            
            if (vitalSign.getSpo2() != null) {
                ps.setInt(6, vitalSign.getSpo2());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            
            ps.setString(7, vitalSign.getMemo());
            ps.setInt(8, vitalSign.getVitalId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM vital_signs WHERE vital_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * 特定の患者のバイタル記録件数を取得
     * @param patientId 患者ID
     * @return 記録件数
     * @throws SQLException
     */
    public int countByPatientId(int patientId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vital_signs WHERE patient_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * ResultSetからVitalSignオブジェクトへマッピング
     * @param rs ResultSet
     * @return VitalSign
     * @throws SQLException
     */
    private VitalSign mapResultSetToVitalSign(ResultSet rs) throws SQLException {
        VitalSign vitalSign = new VitalSign();
        vitalSign.setVitalId(rs.getInt("vital_id"));
        vitalSign.setPatientId(rs.getInt("patient_id"));
        
        Timestamp timestamp = rs.getTimestamp("measured_at");
        if (timestamp != null) {
            vitalSign.setMeasuredAt(timestamp.toLocalDateTime());
        }
        
        // NULL許容フィールドの処理
        Double temperature = rs.getDouble("temperature");
        vitalSign.setTemperature(rs.wasNull() ? null : temperature);
        
        int bloodPressureHigh = rs.getInt("blood_pressure_high");
        vitalSign.setBloodPressureHigh(rs.wasNull() ? null : bloodPressureHigh);
        
        int bloodPressureLow = rs.getInt("blood_pressure_low");
        vitalSign.setBloodPressureLow(rs.wasNull() ? null : bloodPressureLow);
        
        int pulse = rs.getInt("pulse");
        vitalSign.setPulse(rs.wasNull() ? null : pulse);
        
        int spo2 = rs.getInt("spo2");
        vitalSign.setSpo2(rs.wasNull() ? null : spo2);
        
        vitalSign.setMemo(rs.getString("memo"));
        
        return vitalSign;
    }
}