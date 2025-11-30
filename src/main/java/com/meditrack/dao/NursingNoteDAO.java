package com.meditrack.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.meditrack.model.NursingNote;
import com.meditrack.util.DBConnection;

public class NursingNoteDAO {

    // 患者IDで看護メモを取得
    public List<NursingNote> findByPatientId(int patientId) throws SQLException {
        List<NursingNote> notes = new ArrayList<>();
        String sql = "SELECT * FROM nursing_notes WHERE patient_id = ? " +
                     "ORDER BY note_date DESC, note_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                NursingNote note = new NursingNote();
                note.setNoteId(rs.getInt("note_id"));
                note.setPatientId(rs.getInt("patient_id"));
                note.setNoteDate(rs.getDate("note_date").toLocalDate());
                note.setNoteTime(rs.getTime("note_time").toLocalTime());
                note.setContent(rs.getString("content"));
                note.setPriority(rs.getString("priority"));
                note.setNurseName(rs.getString("nurse_name"));
                
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    note.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    note.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                
                notes.add(note);
            }
        }
        return notes;
    }

    // 看護メモを追加
    public boolean insert(NursingNote note) throws SQLException {
        String sql = "INSERT INTO nursing_notes (patient_id, note_date, note_time, " +
                     "content, priority, nurse_name) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, note.getPatientId());
            pstmt.setDate(2, Date.valueOf(note.getNoteDate()));
            pstmt.setTime(3, Time.valueOf(note.getNoteTime()));
            pstmt.setString(4, note.getContent());
            pstmt.setString(5, note.getPriority());
            pstmt.setString(6, note.getNurseName());

            return pstmt.executeUpdate() > 0;
        }
    }

    // 看護メモを更新
    public boolean update(NursingNote note) throws SQLException {
        String sql = "UPDATE nursing_notes SET note_date = ?, note_time = ?, " +
                     "content = ?, priority = ? WHERE note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(note.getNoteDate()));
            pstmt.setTime(2, Time.valueOf(note.getNoteTime()));
            pstmt.setString(3, note.getContent());
            pstmt.setString(4, note.getPriority());
            pstmt.setInt(5, note.getNoteId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // 看護メモを削除
    public boolean delete(int noteId) throws SQLException {
        String sql = "DELETE FROM nursing_notes WHERE note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, noteId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // IDで看護メモを取得
    public NursingNote findById(int noteId) throws SQLException {
        String sql = "SELECT * FROM nursing_notes WHERE note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, noteId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                NursingNote note = new NursingNote();
                note.setNoteId(rs.getInt("note_id"));
                note.setPatientId(rs.getInt("patient_id"));
                note.setNoteDate(rs.getDate("note_date").toLocalDate());
                note.setNoteTime(rs.getTime("note_time").toLocalTime());
                note.setContent(rs.getString("content"));
                note.setPriority(rs.getString("priority"));
                note.setNurseName(rs.getString("nurse_name"));
                
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    note.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    note.setUpdatedAt(updatedAt.toLocalDateTime());
                }
                
                return note;
            }
        }
        return null;
    }
}