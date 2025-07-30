package com.nepaltourism.nepal_tourism3.model;

import java.time.LocalDate;

public class Booking {
    private int id;
    private String tourist;
    private String attraction;
    private String guide;
    private LocalDate startDate;
    private LocalDate endDate;
    private int groupSize;
    private double totalPrice;
    private String status;
    private String festivalDiscount;

    public Booking(int id, String tourist, String attraction, String guide, LocalDate startDate,
                   LocalDate endDate, int groupSize, double totalPrice, String status, String festivalDiscount) {
        this.id = id;
        this.tourist = tourist;
        this.attraction = attraction;
        this.guide = guide;
        this.startDate = startDate;
        this.endDate = endDate;
        this.groupSize = groupSize;
        this.totalPrice = totalPrice;
        this.status = status;
        this.festivalDiscount = festivalDiscount;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTourist() { return tourist; }
    public void setTourist(String tourist) { this.tourist = tourist; }
    public String getAttraction() { return attraction; }
    public void setAttraction(String attraction) { this.attraction = attraction; }
    public String getGuide() { return guide; }
    public void setGuide(String guide) { this.guide = guide; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public int getGroupSize() { return groupSize; }
    public void setGroupSize(int groupSize) { this.groupSize = groupSize; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFestivalDiscount() { return festivalDiscount; }
    public void setFestivalDiscount(String festivalDiscount) { this.festivalDiscount = festivalDiscount; }
}