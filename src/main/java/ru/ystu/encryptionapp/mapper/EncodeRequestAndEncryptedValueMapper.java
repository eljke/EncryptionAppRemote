package ru.ystu.encryptionapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ystu.encryptionapp.dto.EncodeRequestAndEncryptedValueDTO;
import ru.ystu.encryptionapp.entity.EncodeRequestAndEncryptedValue;
import ru.ystu.encryptionapp.entity.UserEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mapper
public interface EncodeRequestAndEncryptedValueMapper {
    EncodeRequestAndEncryptedValueMapper INSTANCE = Mappers.getMapper(EncodeRequestAndEncryptedValueMapper.class);

    @Mapping(target = "encodeRequest.algorithmType", source = "algorithmType")
    @Mapping(target = "encodeRequest.params", source = "params")
    @Mapping(target = "encodeRequest.valueToEncode", source = "valueToEncode")
    @Mapping(target = "encodedValue")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "date", source = "date")
    EncodeRequestAndEncryptedValueDTO encodeRequestAndEncryptedValueToDTO(EncodeRequestAndEncryptedValue entity);

    default List<Object> mapParams(String value) {
        return Collections.singletonList(Arrays.stream(value.split(", "))
                .map(String::trim)
                .toList());
    }

    default String mapUser(UserEntity user) {
        return user.getUsername();
    }

    default String mapDate(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return date.format(dateTimeFormatter);
    }
}
