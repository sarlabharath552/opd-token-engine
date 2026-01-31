package com.opd.token.domain.entity;

import java.time.LocalTime;

import com.opd.token.domain.enums.SlotStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "slots",
       uniqueConstraints = @UniqueConstraint(columnNames = {
           "doctor_id", "start_time", "end_time"
       }))
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private LocalTime startTime;
    private LocalTime endTime;

    private int maxCapacity;
    private int currentCapacity = 0;

    @Enumerated(EnumType.STRING)
    private SlotStatus status = SlotStatus.OPEN;

    private int delayMinutes = 0;

    // getters & setters
    public Long getId() { return id; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getCurrentCapacity() { return currentCapacity; }
    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public SlotStatus getStatus() { return status; }
    public void setStatus(SlotStatus status) { this.status = status; }

    public int getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }
}
