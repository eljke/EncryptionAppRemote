package ru.ystu.encryptionapp.service;

import ru.ystu.encryptionapp.dto.DecodeRequestDTO;
import ru.ystu.encryptionapp.dto.EncodeRequestDTO;
import ru.ystu.encryptionapp.dto.EncryptionAlgorithmDTO;
import ru.ystu.encryptionapp.entity.EncryptionAlgorithm;

import java.util.List;

public interface EncryptionService {
    Object encode(EncodeRequestDTO encodeRequestDTO);

    Object decode(DecodeRequestDTO decodeRequestDTO);

    EncryptionAlgorithmDTO save(EncryptionAlgorithm algorithm);

    List<EncryptionAlgorithmDTO> getAll();
    EncryptionAlgorithmDTO getByName(String name);
    EncryptionAlgorithmDTO getById(String id);

    EncryptionAlgorithmDTO updateByName(String name, EncryptionAlgorithm algorithm);
    EncryptionAlgorithmDTO updateById(String id, EncryptionAlgorithm algorithm);

    boolean deleteByName(String name);
    boolean deleteById(String id);
}
