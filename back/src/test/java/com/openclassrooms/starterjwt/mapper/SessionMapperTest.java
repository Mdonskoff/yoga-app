package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    public void testToEntity() {

        SessionDto sessionDto = null;
        List<SessionDto> sessionDtoList = null;

        Assertions.assertNull(sessionMapper.toEntity(sessionDto));
        Assertions.assertNull(sessionMapper.toEntity(sessionDtoList));

    }

    @Test
    public void testToDto() {

        Session session = null;
        List<Session> sessionList = null;

        Assertions.assertNull(sessionMapper.toDto(session));
        Assertions.assertNull(sessionMapper.toDto(sessionList));

    }

}
