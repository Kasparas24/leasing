package com.example.leasing.services;

import com.example.leasing.db.ApplicantEntity;
import com.example.leasing.db.ApplicationEntity;
import com.example.leasing.db.ApplicationEntityRepository;
import com.example.leasing.exceptions.NoApplicantFound;
import com.example.leasing.exceptions.NoSuchApplicationException;
import com.example.leasing.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeasingService {

    private ApplicationEntityRepository applicationEntityRepository;

    @Autowired
    public LeasingService(ApplicationEntityRepository applicationEntityRepository) {
        this.applicationEntityRepository = applicationEntityRepository;
    }

    public ApplicationResponse verifyApplication(ApplicationRequest applicationRequest) {
        ApplicationEntity application = new ApplicationEntity()
                .setApplicationId(UUID.randomUUID())
                .setSubmitTimestamp(Instant.now());
        application.setDataFromRequest(applicationRequest);

        long summedApplicantsIncome = 0;
        if (applicationRequest.getApplicant() != null) {
            summedApplicantsIncome += applicationRequest.getApplicant().getPersonIncome();
        }
        if (applicationRequest.getCoApplicants() != null) {
            summedApplicantsIncome += applicationRequest.getCoApplicants().stream()
                    .map(Person::getPersonIncome)
                    .mapToLong(Long::longValue)
                    .sum();
        }

        if (summedApplicantsIncome >= 600) {
            application.setStatus(LeasingStatusEnum.APPROVED);
        } else {
            application.setStatus(LeasingStatusEnum.REJECTED);
        }

        application = applicationEntityRepository.save(application);

        return convertApplicationEntityToResponse(application);
    }

    public ApplicationResponse getApplicationData(UUID applicationId) {
        return convertApplicationEntityToResponse(applicationEntityRepository.findById(applicationId)
                .orElseThrow(NoSuchApplicationException::new));
    }

    private ApplicationResponse convertApplicationEntityToResponse(ApplicationEntity application) {
        return new ApplicationResponse()
                .setApplicationId(application.getApplicationId())
                .setStatus(application.getStatus())
                .setSubmitTimestamp(application.getSubmitTimestamp())
                .setApplicationData(new ApplicationRequest()
                        .setRequestedFundingAmount(application.getRequestedFundingAmount())
                        .setVehicle(new Vehicle().setVinNumber(application.getVinNumber()))
                        .setApplicant(getMainPerson(application))
                        .setCoApplicants(getCoApplicantsPersons(application)));
    }

    private Person getMainPerson(ApplicationEntity application) {
        return convertApplicantToPerson(application.getApplicants()
                .stream()
                .filter(applicant -> applicant.getApplicantType() == ApplicantTypeEnum.APPLICANT)
                .findFirst().orElseThrow(NoApplicantFound::new));
    }

    private List<Person> getCoApplicantsPersons(ApplicationEntity application) {
        return application.getApplicants()
                .stream()
                .filter(applicant -> applicant.getApplicantType() == ApplicantTypeEnum.CO_APPLICANT)
                .map(applicant -> new Person()
                        .setPersonId(applicant.getApplicantPersonalId())
                        .setPersonIncome(applicant.getApplicantIncome()))
                .collect(Collectors.toList());
    }

    private Person convertApplicantToPerson(ApplicantEntity applicant) {
        return new Person()
                .setPersonId(applicant.getApplicantPersonalId())
                .setPersonIncome(applicant.getApplicantIncome());
    }
}
