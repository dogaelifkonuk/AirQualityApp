package com.example.air_pollution_app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "pollution_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@IdClass(PollutionDataId.class)
public class PollutionData
{
    @Id
    @Column(name = "city")
    private String city;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "co")
    private double co;

    @Column(name = "o3")
    private double o3;

    @Column(name = "so2")
    private double so2;
}
