package com.meditrack.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NursingNote {
    private int noteId;
    private int patientId;
    private LocalDate noteDate;
    private LocalTime noteTime;
    private String content;
    private String priority;
    private String nurseName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // コンストラクタ
    public NursingNote() {}

    public NursingNote(int patientId, LocalDate noteDate, LocalTime noteTime,
                      String content, String priority, String nurseName) {
        this.patientId = patientId;
        this.noteDate = noteDate;
        this.noteTime = noteTime;
        this.content = content;
        this.priority = priority;
        this.nurseName = nurseName;
    }

    // Getter/Setter
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public LocalDate getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(LocalDate noteDate) {
        this.noteDate = noteDate;
    }

    public LocalTime getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(LocalTime noteTime) {
        this.noteTime = noteTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}