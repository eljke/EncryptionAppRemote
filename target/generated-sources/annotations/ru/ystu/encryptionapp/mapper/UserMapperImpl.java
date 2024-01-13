package ru.ystu.encryptionapp.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import ru.ystu.encryptionapp.dto.UserDTO;
import ru.ystu.encryptionapp.entity.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-13T10:03:49+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(UserEntity user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        if ( user.getId() != null ) {
            userDTO.setId( user.getId().toString() );
        }
        userDTO.setName( user.getName() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setEmail( user.getEmail() );

        userDTO.setJoinDate( localDateToString(user.getJoinDate()) );

        return userDTO;
    }

    @Override
    public UserEntity userDTOToUser(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        if ( dto.getId() != null ) {
            userEntity.setId( UUID.fromString( dto.getId() ) );
        }
        userEntity.setName( dto.getName() );
        userEntity.setUsername( dto.getUsername() );
        userEntity.setEmail( dto.getEmail() );
        userEntity.setJoinDate( stringToLocalDate( dto.getJoinDate() ) );

        return userEntity;
    }
}
