package ru.ystu.encryptionapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ystu.encryptionapp.dto.EncryptionAlgorithmDTO;
import ru.ystu.encryptionapp.entity.EncryptionAlgorithm;

@Mapper
public interface EncryptionAlgorithmMapper {
    EncryptionAlgorithmMapper INSTANCE = Mappers.getMapper(EncryptionAlgorithmMapper.class);

    @Mapping(target = "name")
    @Mapping(target = "description")
    EncryptionAlgorithmDTO algorithmToAlgorithmDTO(EncryptionAlgorithm algorithm);
}
