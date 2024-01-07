package ru.ystu.encryptionapp.service;

import org.springframework.stereotype.Service;
import ru.ystu.encryptionapp.dto.DecodeRequestDTO;
import ru.ystu.encryptionapp.dto.EncodeRequestDTO;
import ru.ystu.encryptionapp.dto.EncryptionAlgorithmDTO;
import ru.ystu.encryptionapp.entity.*;
import ru.ystu.encryptionapp.enumeration.AlgorithmType;
import ru.ystu.encryptionapp.exception.EncryptionAlgorithmNotFoundException;
import ru.ystu.encryptionapp.exception.NonDecryptableException;
import ru.ystu.encryptionapp.mapper.EncryptionAlgorithmMapper;
import ru.ystu.encryptionapp.repository.EncryptionAlgorithmRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EncryptionServiceImpl implements EncryptionService {
    private final EncryptionAlgorithmRepository algorithmRepository;

    public EncryptionServiceImpl(EncryptionAlgorithmRepository algorithmRepository) {
        this.algorithmRepository = algorithmRepository;
    }

    @Override
    public Object encode(EncodeRequestDTO encodeRequestDTO) {
        try {
            EncryptionAlgorithm encryptionAlgorithm = getAlgorithm(encodeRequestDTO.getAlgorithmType(), encodeRequestDTO.getParams());
            String valueToEncode = encodeRequestDTO.getValueToEncode();

            return encryptionAlgorithm.encode(valueToEncode);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Object decode(DecodeRequestDTO decodeRequestDTO) {
        try {
            EncryptionAlgorithm encryptionAlgorithm = getAlgorithm(decodeRequestDTO.getAlgorithmType(), decodeRequestDTO.getParams());
            String valueToDecode = decodeRequestDTO.getValueToDecode();

            return encryptionAlgorithm.decode(valueToDecode);
        } catch (Exception e) {
            if (e instanceof NonDecryptableException) {
                throw new NonDecryptableException();
            } else {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    private EncryptionAlgorithm getAlgorithm(AlgorithmType algorithmType, List<Object> params) {
        switch (algorithmType) {
            case CAESAR:
                return new CaesarEncryptionAlgorithm((Integer) params.get(0));
            case HYPOTENUSE:
                Integer param1 = (params.size() > 1 && params.get(1) != null) ? (Integer) params.get(1)
                        : (!params.isEmpty() && params.get(0) != null) ? (Integer) params.get(0) : null;
                Integer param2 = (params.size() > 2 && params.get(2) != null) ? (Integer) params.get(2) : 1;
                return new HypotenuseEncryptionAlgorithm((Integer) params.get(0), param1, param2, (String) params.get(3));
            case BCRYPT:
                return new BCryptEncryptionAlgorithm((Integer) params.get(0));
            case VIGENERE:
                return new VigenereEncryptionAlgorithm((String) params.get(0));
            case SUBSTITUTION:
                Map<Character, Character> substitutionParams = getSubstitutionParams(params);

                return new SubstitutionEncryptionAlgorithm(substitutionParams);
            case TABULAR_ROUTE_SHUFFLE:
                return new TabularRouteShuffleEncryptionAlgorithm((Integer) params.get(0), (Integer) params.get(1),
                        (String) params.get(2));
            case POLYBIUS_SQUARE:
                return new PolybiusSquareEncryptionAlgorithm((String) params.get(0));
            default:
                throw new IllegalArgumentException("Unsupported algorithm type: " + algorithmType);
        }
    }

    private static Map<Character, Character> getSubstitutionParams(List<Object> params) {
        Map<Character, Character> substitutionParams = new HashMap<>();
        for (Object param : params) {
            if (param instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Character key = entry.getKey().toString().charAt(0);
                    Character value = entry.getValue().toString().charAt(0);
                    substitutionParams.put(key, value);
                }
            }
        }

        return substitutionParams;
    }

    @Override
    public EncryptionAlgorithmDTO save(EncryptionAlgorithm algorithm) {
        return EncryptionAlgorithmMapper.INSTANCE.algorithmToAlgorithmDTO(algorithmRepository.save(algorithm));
    }

    @Override
    public List<EncryptionAlgorithmDTO> getAll() {
        return algorithmRepository.findAll().stream()
                .map(EncryptionAlgorithmMapper.INSTANCE::algorithmToAlgorithmDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EncryptionAlgorithmDTO getByName(String name) {
        return EncryptionAlgorithmMapper.INSTANCE.algorithmToAlgorithmDTO(algorithmRepository.findByName(name)
                .orElseThrow(() -> new EncryptionAlgorithmNotFoundException(name)));
    }

    @Override
    public EncryptionAlgorithmDTO getById(String id) {
        return EncryptionAlgorithmMapper.INSTANCE.algorithmToAlgorithmDTO(algorithmRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EncryptionAlgorithmNotFoundException(id)));
    }

    @Override
    public EncryptionAlgorithmDTO updateByName(String name, EncryptionAlgorithm algorithm) {
        EncryptionAlgorithm savedAlgorithm = algorithmRepository.findByName(name)
                .orElseThrow(() -> new EncryptionAlgorithmNotFoundException(name));

        return updateAlgorithmFields(algorithm, savedAlgorithm);
    }

    @Override
    public EncryptionAlgorithmDTO updateById(String id, EncryptionAlgorithm algorithm) {
        EncryptionAlgorithm savedAlgorithm = algorithmRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EncryptionAlgorithmNotFoundException(id));

        return updateAlgorithmFields(algorithm, savedAlgorithm);
    }

    private EncryptionAlgorithmDTO updateAlgorithmFields(EncryptionAlgorithm algorithm, EncryptionAlgorithm savedAlgorithm) {
        savedAlgorithm.setId(algorithm.getId() != null ? algorithm.getId() : savedAlgorithm.getId());
        savedAlgorithm.setName(algorithm.getName() != null ? algorithm.getName() : savedAlgorithm.getName());
        savedAlgorithm.setDescription(algorithm.getDescription() != null ? algorithm.getDescription() : savedAlgorithm.getDescription());
        savedAlgorithm.setDecryptionDirection(algorithm.getDecryptionDirection() != null ? algorithm.getDecryptionDirection() : savedAlgorithm.getDecryptionDirection());

        return EncryptionAlgorithmMapper.INSTANCE.algorithmToAlgorithmDTO(algorithmRepository.save(savedAlgorithm));
    }

    @Override
    public boolean deleteByName(String name) {
        if (algorithmRepository.findByName(name).isPresent()) {
            algorithmRepository.deleteByName(name);

            return algorithmRepository.findByName(name).isEmpty();
        } else {
            throw new EncryptionAlgorithmNotFoundException(name);
        }
    }

    @Override
    public boolean deleteById(String id) {
        if (algorithmRepository.findById(Long.valueOf(id)).isPresent()) {
            algorithmRepository.deleteById(Long.valueOf(id));

            return algorithmRepository.findById(Long.valueOf(id)).isEmpty();
        } else {
            throw new EncryptionAlgorithmNotFoundException(id);
        }
    }
}