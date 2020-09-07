package com.example.leasing.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicationEntityRepository extends CrudRepository<ApplicationEntity, UUID> {
}
