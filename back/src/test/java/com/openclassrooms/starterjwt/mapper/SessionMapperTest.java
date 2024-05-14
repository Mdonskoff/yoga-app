package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    public void testToEntity() {

        SessionDto sessionDto = null;
        List<SessionDto> sessionDtoList = null;

        assertNull(sessionMapper.toEntity(sessionDto));
        assertNull(sessionMapper.toEntity(sessionDtoList));

    }

    @Test
    public void testToDto() {

        Session session = null;
        List<Session> sessionList = null;

        assertNull(sessionMapper.toDto(session));
        assertNull(sessionMapper.toDto(sessionList));

    }

    @Test
    public void testToEntityWithValidDtoList() {
        // Créer une liste de DTO
        List<SessionDto> dtoList = new ArrayList<>();
        SessionDto dto1 = new SessionDto();
        SessionDto dto2 = new SessionDto();
        dtoList.add(dto1);
        dtoList.add(dto2);

        // Appeler la méthode à tester
        List<Session> result = sessionMapper.toEntity(dtoList);

        // Vérifier si la liste résultante contient les entités converties
        assertNotNull(result);
        assertEquals(dtoList.size(), result.size());
    }


}
