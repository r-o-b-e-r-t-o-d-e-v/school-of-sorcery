package com.liferay.infrastructure.outbounds.persistance.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
      name = "house_bedding",
      uniqueConstraints = {
            @UniqueConstraint(
                  name = "uk_bedding_house_year",
                  columnNames = {"house_id", "year"}
            )
      }
)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseBeddingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "house_id", nullable = false)
    private HouseEntity house;

    @Column(name = "total_beds", nullable = false)
    private Integer totalBeds;
}
