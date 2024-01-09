package ru.ystu.encryptionapp.service;

import ru.ystu.encryptionapp.dto.EncodeRequestAndEncryptedValueDTO;
import ru.ystu.encryptionapp.entity.EncodeRequestAndEncryptedValue;
import ru.ystu.encryptionapp.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface EncodeRequestAndEncryptedValueService {
    EncodeRequestAndEncryptedValueDTO save(EncodeRequestAndEncryptedValue encodeRequestAndEncryptedValue);
    List<EncodeRequestAndEncryptedValueDTO> getAllByUser(UserEntity user);
    boolean deleteById(UUID id);
}
