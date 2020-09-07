package com.example.leasing.db;

import com.example.leasing.model.ApplicantTypeEnum;
import com.example.leasing.model.ApplicationRequest;
import com.example.leasing.model.LeasingStatusEnum;
import com.example.leasing.model.Person;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Data
public class ApplicationEntity {

    @Id
    @Column(columnDefinition = "UNIQUEIDENTIFIER")
    @Type(type = "uuid-char")
    private UUID applicationId;

    private LeasingStatusEnum status;

    private Instant submitTimestamp;

    private Long requestedFundingAmount;

    private String vinNumber;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private List<ApplicantEntity> applicants;

    public void setDataFromRequest(ApplicationRequest request) {
        this.requestedFundingAmount = request.getRequestedFundingAmount();
        this.vinNumber = request.getVehicle().getVinNumber();

        List<ApplicantEntity> applicants = new ArrayList<>();
        applicants.add(getApplicantEntityFromPerson(request.getApplicant(), ApplicantTypeEnum.APPLICANT));
        if (request.getCoApplicants() != null) {
            applicants.addAll(request.getCoApplicants().stream()
                    .map(person -> getApplicantEntityFromPerson(person, ApplicantTypeEnum.CO_APPLICANT))
                    .collect(Collectors.toList()));
        }
        this.applicants = applicants;
    }

    private ApplicantEntity getApplicantEntityFromPerson(Person person, ApplicantTypeEnum type) {
        return new ApplicantEntity()
                .setApplication(this)
                .setApplicantPersonalId(person.getPersonId())
                .setApplicantIncome(person.getPersonIncome())
                .setApplicantType(type);
    }
}
