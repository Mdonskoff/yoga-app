package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import java.util.Optional;

import static java.lang.Long.parseLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionControllerIT {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
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

        when(sessionRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(session));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Yoga"));

        verify(sessionRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should return a not found status")
    void giveIdSession_thenFindSessionById_shouldNotFoundStatus() throws Exception {

        when(sessionRepository.findById(Long.parseLong(id))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(sessionRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should return a bad request status")
    void giveIdSession_thenFindSessionById_shouldBadRequestStatus() throws Exception {

        when(sessionRepository.findById(Long.parseLong(id))).thenThrow(NumberFormatException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(sessionRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should return all sessions")
    void giveIdSession_thenFindSessionById_shouldReturnAllSessions() throws Exception {

        List<Session> sessions = List.of(new Session().setName("Yoga"),
                new Session().setName("Zen"));

        when(sessionRepository.findAll()).thenReturn(sessions);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Yoga"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Zen"));

        verify(sessionRepository).findAll();
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

        when(sessionRepository.save(session)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(session.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(session.getDescription()));

        verify(sessionRepository).save(session);
    }

    @Test
    @DisplayName("Should update a session")
    void giveIdSession_thenFindSessionById_shouldUpdateSession() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);
        sessionDto.setId(Long.parseLong(id));

        Session session = sessionMapper.toEntity(sessionDto);

        when(sessionRepository.save(session)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(id)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(session.getName()));

        verify(sessionRepository).save(session);
    }

    @Test
    @DisplayName("Should update a session and return a bad request status")
    void giveIdSession_thenUpdateSession_shouldBadRequestStatus() throws Exception {

        Session session;
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);
        sessionDto.setId(Long.parseLong(id));

        session = sessionMapper.toEntity(sessionDto);

        when(sessionRepository.save(session)).thenThrow(NumberFormatException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(id)
                    .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(sessionRepository).save(session);
    }

    @Test
    @DisplayName("Should delete a session")
    void giveIdSession_thenFindSessionById_shouldDeleteSession() throws Exception {
        Session session = new Session();

        when(sessionRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(session));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", id))
                .andExpect((MockMvcResultMatchers.status().isOk()));

        verify(sessionRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should delete a session and return a not found status")
    void giveIdSession_thenFindSessionById_shouldReturnNotFoundStatus() throws Exception {

        when(sessionRepository.findById(Long.parseLong(id))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", id))
                .andExpect((MockMvcResultMatchers.status().isNotFound()));

        verify(sessionRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should delete a session and return a bad request status")
    void giveIdSession_thenFindSessionById_shouldReturnBadRequestStatus() throws Exception {

        when(sessionRepository.findById(Long.parseLong(id))).thenThrow(NumberFormatException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", id))
                .andExpect((MockMvcResultMatchers.status().isBadRequest()));

        verify(sessionRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should participate to a session")
    void giveIdSession_thenUserParticipateToASession_shouldParticipate() throws Exception {
        Session session = new Session();
        User user = new User();
        session.setId(Long.parseLong(id));
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(session));
        when(userRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}", id, id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(sessionRepository).findById(Long.parseLong(id));
        verify(userRepository).findById(Long.parseLong(id));
        assert(session.getUsers().contains(user));
    }

    @Test
    @DisplayName("Should participate to a session but return a bad request status")
    void giveIdSession_thenUserParticipateToASession_shouldReturnBadRequestStatus() throws Exception {

        when(sessionRepository.findById(Long.parseLong(id))).thenThrow(NumberFormatException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}",id, id))
                .andExpect((MockMvcResultMatchers.status().isBadRequest()));

        verify(sessionRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should no longer participate to a session")
    void giveIdSession_thenUserNoLongerParticipateToASession_shouldNoLongerParticipate() throws Exception {
        Session session = new Session();
        User user = new User();
        user.setId(Long.parseLong(id));
        session.setId(Long.parseLong(id));
        session.setUsers(List.of(user));

        when(sessionRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(session));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}/participate/{userId}", id, id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(sessionRepository).findById(Long.parseLong(id));

        assert(!session.getUsers().contains(user));
    }

    @Test
    @DisplayName("Should no longer participate to a session and return a bad request status")
    void giveIdSession_thenUserNoLongerParticipateToASession_shouldReturnBadRequestStatus() throws Exception {

        when(sessionRepository.findById(Long.parseLong(id))).thenThrow(NumberFormatException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}/participate/{userId}",id, id))
                .andExpect((MockMvcResultMatchers.status().isBadRequest()));

        verify(sessionRepository).findById(Long.parseLong(id));
    }
}
