package ru.ystu.encryptionapp.service;

import org.springframework.stereotype.Service;
import ru.ystu.encryptionapp.dto.EncodeRequestAndEncryptedValueDTO;
import ru.ystu.encryptionapp.entity.EncodeRequestAndEncryptedValue;
import ru.ystu.encryptionapp.entity.UserEntity;
import ru.ystu.encryptionapp.mapper.EncodeRequestAndEncryptedValueMapper;
import ru.ystu.encryptionapp.repository.EncodeRequestAndEncryptedValueRepository;

import java.util.List;

@Service
public class EncodeRequestAndEncryptedValueServiceImpl implements EncodeRequestAndEncryptedValueService {
    private final EncodeRequestAndEncryptedValueRepository repository;

    public EncodeRequestAndEncryptedValueServiceImpl(EncodeRequestAndEncryptedValueRepository repository) {
        this.repository = repository;
    }

    @Override
    public EncodeRequestAndEncryptedValueDTO save(EncodeRequestAndEncryptedValue encodeRequestAndEncryptedValue) {
        return EncodeRequestAndEncryptedValueMapper.INSTANCE.encodeRequestAndEncryptedValueToDTO(repository.save(encodeRequestAndEncryptedValue));
    }

    @Override
    public List<EncodeRequestAndEncryptedValueDTO> getAllByUser(UserEntity user) {
        return repository.findAllByUser(user).stream()
                .map(EncodeRequestAndEncryptedValueMapper.INSTANCE::encodeRequestAndEncryptedValueToDTO)
                .toList();
    }
}
