package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

@Data
public class TotalTicketsDTO {
    private String month;
    private int totalPublicTickets;
    private int totalInstitutionTickets;
    private int totalForeignerTickets;
    private int  totalTickets;

    public TotalTicketsDTO(int totalPublicTickets, int totalInstitutionTickets, int totalForeignerTickets, int totalTickets) {
        this.totalPublicTickets = totalPublicTickets;
        this.totalInstitutionTickets = totalInstitutionTickets;
        this.totalForeignerTickets = totalForeignerTickets;
        this.totalTickets = totalTickets;
    }

    public TotalTicketsDTO(String month, int totalPublicTickets, int totalInstitutionTickets, int totalForeignerTickets, int totalTickets) {
        this.month = month;
        this.totalPublicTickets = totalPublicTickets;
        this.totalInstitutionTickets = totalInstitutionTickets;
        this.totalForeignerTickets = totalForeignerTickets;
        this.totalTickets = totalTickets;
    }
}
