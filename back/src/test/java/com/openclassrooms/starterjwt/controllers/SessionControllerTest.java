package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionController sessionController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    private final String id = "10";

    @BeforeEach
    void setUp(){
        sessionService = new SessionService(sessionRepository, userRepository);
        sessionController = new SessionController(sessionService, sessionMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return a session")
    void giveIdSession_thenFindSessionById_shouldReturnASession() throws Exception {

        Session session = new Session();
        session.setName("Yoga");
        session.setDate(new Date());
        session.setDescription("Description session");

        sessionRepository.save(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);

        SessionDto expectedSessionDto = sessionMapper.toDto(session);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", expectedSessionDto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Yoga"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedSessionDto.getId()));

    }

    @Test
    @DisplayName("Should return a not found status")
    void giveIdSession_thenFindSessionById_shouldNotFoundStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("Should return a bad request status")
    void giveIdSession_thenFindSessionById_shouldBadRequestStatus() throws Exception {

        sessionController.findById("a");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", "a"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("Should return all sessions")
    void giveIdSession_thenFindSessionById_shouldReturnAllSessions() throws Exception {

        Session session = new Session();
        session.setName("Yoga");
        session.setDate(new Date());
        session.setDescription("Description session");

        sessionRepository.save(session);

        Session session2 = new Session();
        session2.setName("Zen");
        session2.setDate(new Date());
        session2.setDescription("Description session");

        sessionRepository.save(session2);

        List<Session> sessions = List.of(new Session().setName("Yoga"), new Session().setName("Zen"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Yoga"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Zen"));

    }

    @Test
    @DisplayName("Should create a session")
    void giveIdSession_thenFindSessionById_shouldCreateSession() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);

        Session session = sessionMapper.toEntity(sessionDto);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(session.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(session.getDescription()));
    }

    @Test
    @DisplayName("Should update a session")
    void giveIdSession_thenFindSessionById_shouldUpdateSession() throws Exception {

        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);

        sessionRepository.save(session);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(sessionDto.getName()));

    }

    @Test
    @DisplayName("Should update a session and return a bad request status")
    void giveIdSession_thenUpdateSession_shouldBadRequestStatus() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", "a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("Should delete a session")
    void giveIdSession_thenFindSessionById_shouldDeleteSession() throws Exception {

        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");

        Session savedSession = sessionRepository.save(session);

        Long sessionId = savedSession.getId();

        mockMvc.perform(delete("/api/session/{id}", sessionId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertFalse(sessionRepository.existsById(sessionId));

    }

    @Test
    @DisplayName("Should delete a session and return a not found status")
    void giveIdSession_thenFindSessionById_shouldReturnNotFoundStatus() throws Exception {

        mockMvc.perform(delete("/api/session/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("Should delete a session and return a bad request status")
    void giveIdSession_thenFindSessionById_shouldReturnBadRequestStatus() throws Exception {

        mockMvc.perform(delete("/api/session/{id}", "a"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("Should participate to a session")
    void giveIdSession_thenUserParticipateToASession_shouldParticipate() throws Exception {

        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");
        Session savedSession = sessionRepository.save(session);

        User user = new User("test@example.com", "Doe", "John", "1234", false);
        User savedUser = userRepository.save(user);

        Long sessionId = savedSession.getId();
        Long userId = savedUser.getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{sessionId}/participate/{userId}", sessionId, userId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Session updatedSession = sessionRepository.findById(sessionId).orElse(null);
        assertNotNull(updatedSession);
        assertTrue(updatedSession.getUsers().contains(savedUser));

    }

    @Test
    @DisplayName("Should participate to a session but return a bad request status")
    void giveIdSession_thenUserParticipateToASession_shouldReturnBadRequestStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}", "a", "b"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("Should no longer participate to a session")
    void giveIdSession_thenUserNoLongerParticipateToASession_shouldNoLongerParticipate() throws Exception {

        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");
        Session savedSession = sessionRepository.save(session);

        User user = new User("test@example.com", "Doe", "John", "1234", false);
        User savedUser = userRepository.save(user);

        List<User> usersList = new ArrayList<>();
        usersList.add(savedUser);
        savedSession.setUsers(usersList);
        sessionRepository.save(savedSession);

        Long sessionId = savedSession.getId();
        Long userId = savedUser.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{sessionId}/participate/{userId}", sessionId, userId))
                // Vérifiez que la réponse est un code de statut OK
                .andExpect(MockMvcResultMatchers.status().isOk());

        Session updatedSession = sessionRepository.findById(sessionId).orElse(null);
        assertNotNull(updatedSession);
        assertFalse(updatedSession.getUsers().contains(savedUser));

    }

    @Test
    @DisplayName("Should no longer participate to a session and return a bad request status")
    void giveIdSession_thenUserNoLongerParticipateToASession_shouldReturnBadRequestStatus() throws Exception {

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", "a", "b"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @AfterEach
    public void cleanup() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
