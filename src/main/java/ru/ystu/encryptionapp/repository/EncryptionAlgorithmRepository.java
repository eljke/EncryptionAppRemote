package ru.ystu.encryptionapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ystu.encryptionapp.entity.EncryptionAlgorithm;

import java.util.Optional;

@Repository
public interface EncryptionAlgorithmRepository extends JpaRepository<EncryptionAlgorithm, Long> {
    Optional<EncryptionAlgorithm> findByName(String name);
    void deleteByName(String name);
}
