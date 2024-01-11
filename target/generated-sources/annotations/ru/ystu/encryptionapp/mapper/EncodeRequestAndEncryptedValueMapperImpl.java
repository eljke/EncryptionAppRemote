package ru.ystu.encryptionapp.mapper;

import javax.annotation.processing.Generated;
import ru.ystu.encryptionapp.dto.EncodeRequestAndEncryptedValueDTO;
import ru.ystu.encryptionapp.dto.EncodeRequestDTO;
import ru.ystu.encryptionapp.entity.EncodeRequestAndEncryptedValue;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-11T18:14:26+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
public class EncodeRequestAndEncryptedValueMapperImpl implements EncodeRequestAndEncryptedValueMapper {

    @Override
    public EncodeRequestAndEncryptedValueDTO encodeRequestAndEncryptedValueToDTO(EncodeRequestAndEncryptedValue entity) {
        if ( entity == null ) {
            return null;
        }

        EncodeRequestAndEncryptedValueDTO encodeRequestAndEncryptedValueDTO = new EncodeRequestAndEncryptedValueDTO();

        encodeRequestAndEncryptedValueDTO.setEncodeRequest( encodeRequestAndEncryptedValueToEncodeRequestDTO( entity ) );
        encodeRequestAndEncryptedValueDTO.setEncodedValue( entity.getEncodedValue() );
        encodeRequestAndEncryptedValueDTO.setUser( mapUser( entity.getUser() ) );
        encodeRequestAndEncryptedValueDTO.setDate( mapDate( entity.getDate() ) );
        encodeRequestAndEncryptedValueDTO.setId( entity.getId() );

        return encodeRequestAndEncryptedValueDTO;
    }

    protected EncodeRequestDTO encodeRequestAndEncryptedValueToEncodeRequestDTO(EncodeRequestAndEncryptedValue encodeRequestAndEncryptedValue) {
        if ( encodeRequestAndEncryptedValue == null ) {
            return null;
        }

        EncodeRequestDTO encodeRequestDTO = new EncodeRequestDTO();

        encodeRequestDTO.setAlgorithmType( encodeRequestAndEncryptedValue.getAlgorithmType() );
        encodeRequestDTO.setParams( mapParams( encodeRequestAndEncryptedValue.getParams() ) );
        encodeRequestDTO.setValueToEncode( encodeRequestAndEncryptedValue.getValueToEncode() );

        return encodeRequestDTO;
    }
}
