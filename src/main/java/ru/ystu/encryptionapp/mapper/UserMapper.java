package ru.ystu.encryptionapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ystu.encryptionapp.dto.UserDTO;
import ru.ystu.encryptionapp.entity.UserEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "joinDate", expression = "java(localDateToString(user.getJoinDate()))")
    UserDTO userToUserDTO(UserEntity user);

    default String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }

        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
