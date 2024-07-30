package com.example.air_pollution_app.util;

public class ComponentCategory
{

    public static String categorizeCO(double CO) {
        CO = CO / 1000;
        if(0 <= CO && CO <= 1.0) {
            return "Good";
        }
        if(1.0 < CO && CO <= 2.0) {
            return "Satisfactory";
        }
        if(2.0 < CO && CO <= 10) {
            return "Moderate";
        }
        if(10 < CO && CO <= 17) {
            return "Poor";
        }
        if(17 < CO && CO <= 34) {
            return "Severe";
        }
        if(34 < CO) {
            return "Hazardous";
        }
        return "Unclassified";
    }

    public static String categorizeO3 (double O3) {
        if(0 <= O3 && O3 <= 50) {
            return "Good";
        }
        if(50 < O3 && O3 <= 100) {
            return "Satisfactory";
        }
        if(100 < O3 && O3 <= 168) {
            return "Moderate";
        }
        if(168 < O3 && O3 <= 208) {
            return "Poor";
        }
        if(208 < O3 && O3 <= 748) {
            return "Severe";
        }
        if(748 < O3) {
            return "Hazardaous";
        }
        return "Unclassified";
    }
    public static String categorizeSO2 (double SO2)
    {
        if(0 <= SO2 && SO2 <= 40) {
            return "Good";
        }
        if(40 < SO2 && SO2 <= 80) {
            return "Satisfactory";
        }
        if(80 < SO2 && SO2 <= 380) {
            return "Moderate";
        }
        if(380 < SO2 && SO2 <= 800) {
            return "Poor";
        }
        if(800 < SO2 && SO2 <= 1600) {
            return "Severe";
        }
        if(1600 < SO2) {
            return "Hazardous";
        }
        return "Unclassified";
    }
}
