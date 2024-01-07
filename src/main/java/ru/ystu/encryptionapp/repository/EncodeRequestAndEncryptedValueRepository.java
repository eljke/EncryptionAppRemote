package ru.ystu.encryptionapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ystu.encryptionapp.entity.EncodeRequestAndEncryptedValue;
import ru.ystu.encryptionapp.entity.UserEntity;

import java.util.List;

@Repository
public interface EncodeRequestAndEncryptedValueRepository extends JpaRepository<EncodeRequestAndEncryptedValue, Long> {
    List<EncodeRequestAndEncryptedValue> findAllByUser(UserEntity user);
}
