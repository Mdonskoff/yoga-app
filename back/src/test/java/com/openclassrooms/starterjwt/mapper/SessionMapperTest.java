package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionMapperTest {

    @Autowired
    private SessionMapperImpl sessionMapper;

    @Test
    public void whenSessionToSessionDto_thenEquals() {
        // GIVEN
        Session givenSession = new Session();
        givenSession.setId(111L);
        givenSession.setName("Session-By-Id");
        givenSession.setDate(new Date());
        givenSession.setTeacher(new Teacher(121L, "Of the Session", "Teacher", LocalDateTime.now(), null));
        givenSession.setDescription("Get the session");
        givenSession.setCreatedAt(LocalDateTime.now());
        givenSession.setUpdatedAt(null);

        User givenUser = new User();
        givenUser.setId(99L);
        givenUser.setEmail("find-by-id@mail.com");
        givenUser.setLastName("By-Id");
        givenUser.setFirstName("Find");
        givenUser.setPassword("pass");
        givenUser.setAdmin(true);
        givenUser.setCreatedAt(LocalDateTime.now());
        givenUser.setUpdatedAt(null);

        ArrayList<User>users=new ArrayList<>();
        users.add(givenUser);

        givenSession.setUsers(users);

        // WHEN
        SessionDto sessionDto = sessionMapper.toDto(givenSession);

        // THEN
        assertEquals(givenSession.getId(), sessionDto.getId());
        assertEquals(givenSession.getName(), sessionDto.getName());
        assertEquals(givenSession.getDescription(), sessionDto.getDescription());
        assertEquals(givenSession.getCreatedAt(), sessionDto.getCreatedAt());
        assertEquals(givenSession.getUsers().size(), sessionDto.getUsers().size());
        assertEquals(givenSession.getTeacher().getId(), sessionDto.getTeacher_id());
    }

    @Test
    public void whenSessionDtoToSession_thenEquals() {
        // GIVEN
        SessionDto givenSessionDto = new SessionDto();
        givenSessionDto.setId(111L);
        givenSessionDto.setName("Session-By-Id");
        givenSessionDto.setDate(new Date());
        givenSessionDto.setTeacher_id(121L);
        givenSessionDto.setDescription("Get the session");
        givenSessionDto.setCreatedAt(LocalDateTime.now());
        givenSessionDto.setUpdatedAt(null);

        User givenUser = new User();
        givenUser.setId(99L);
        givenUser.setEmail("find-by-id@mail.com");
        givenUser.setLastName("By-Id");
        givenUser.setFirstName("Find");
        givenUser.setPassword("pass");
        givenUser.setAdmin(true);
        givenUser.setCreatedAt(LocalDateTime.now());
        givenUser.setUpdatedAt(null);

        ArrayList<Long>userIds = new ArrayList<>();
        userIds.add(givenUser.getId());

        givenSessionDto.setUsers(userIds);

        // WHEN
        Session session = sessionMapper.toEntity(givenSessionDto);

        // THEN
        assertEquals(givenSessionDto.getId(), session.getId());
        assertEquals(givenSessionDto.getName(), session.getName());
        assertEquals(givenSessionDto.getDescription(), session.getDescription());
        assertEquals(givenSessionDto.getCreatedAt(), session.getCreatedAt());
        assertEquals(givenSessionDto.getUsers().size(), session.getUsers().size());
    }
}
