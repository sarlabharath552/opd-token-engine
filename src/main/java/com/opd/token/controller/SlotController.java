package com.opd.token.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.opd.token.domain.entity.Slot;
import com.opd.token.service.SlotService;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping
    public Slot createSlot(
            @RequestParam Long doctorId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam int capacity) {

        return slotService.createSlot(
                doctorId,
                LocalTime.parse(startTime),
                LocalTime.parse(endTime),
                capacity
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Slot> getDoctorSlots(@PathVariable Long doctorId) {
        return slotService.getDoctorSlots(doctorId);
    }
}
