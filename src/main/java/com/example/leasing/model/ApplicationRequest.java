package com.example.leasing.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@NoArgsConstructor
@Data
public class ApplicationRequest {

    private Vehicle vehicle;
    private Person applicant;
    private List<Person> coApplicants;
    private long requestedFundingAmount;

}
