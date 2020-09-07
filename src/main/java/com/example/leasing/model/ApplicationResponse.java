package com.example.leasing.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Accessors(chain = true)
@NoArgsConstructor
@Data
public class ApplicationResponse {

    private UUID applicationId;
    private LeasingStatusEnum status;
    private Instant submitTimestamp;
    private ApplicationRequest applicationData;
}
