package com.example.leasing.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantEntityRepository extends CrudRepository<ApplicationEntity, Long> {
}
