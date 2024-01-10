package ru.ystu.encryptionapp.mapper;

import javax.annotation.processing.Generated;
import ru.ystu.encryptionapp.dto.EncryptionAlgorithmDTO;
import ru.ystu.encryptionapp.entity.EncryptionAlgorithm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-10T03:04:25+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
public class EncryptionAlgorithmMapperImpl implements EncryptionAlgorithmMapper {

    @Override
    public EncryptionAlgorithmDTO algorithmToAlgorithmDTO(EncryptionAlgorithm algorithm) {
        if ( algorithm == null ) {
            return null;
        }

        EncryptionAlgorithmDTO encryptionAlgorithmDTO = new EncryptionAlgorithmDTO();

        encryptionAlgorithmDTO.setName( algorithm.getName() );
        encryptionAlgorithmDTO.setDescription( algorithm.getDescription() );

        return encryptionAlgorithmDTO;
    }
}
