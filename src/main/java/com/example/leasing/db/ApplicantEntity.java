package com.example.leasing.db;

import com.example.leasing.model.ApplicantTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Data
public class ApplicantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long applicantId;

    @ManyToOne
    private ApplicationEntity application;

    private String applicantPersonalId;

    private long applicantIncome;

    private ApplicantTypeEnum applicantType;

}
