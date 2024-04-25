package com.openclassrooms.starterjwt.controllers;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionControllerIT {

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


    private final String id = "10";

    @BeforeEach
    void setUp(){
        sessionService = new SessionService(sessionRepository, userRepository);
        sessionController = new SessionController(sessionService, sessionMapper);
    }

    @Test
    @DisplayName("Should return a session")
    void giveIdSession_thenFindSessionById_shouldReturnASession() throws Exception {

        Session session = new Session();
        session.setName("Yoga");
        session.setDate(new Date());
        session.setDescription("Description session");

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);

        Session newSession = sessionRepository.save(session);
        Long idSession = newSession.getId();
        ResponseEntity<?> responseEntity = sessionController.findById(String.valueOf(idSession));
        assertEquals(sessionDto.getName(), ((SessionDto)responseEntity.getBody()).getName());
        assertEquals(sessionDto.getDescription(), ((SessionDto)responseEntity.getBody()).getDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Should return a not found status")
    void giveIdSession_thenFindSessionById_shouldNotFoundStatus() throws Exception {

        ResponseEntity<?> responseEntity = sessionController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Should return a bad request status")
    void giveIdSession_thenFindSessionById_shouldBadRequestStatus() throws Exception {

        ResponseEntity<?> responseEntity = sessionController.findById("a");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return all sessions")
    void giveIdSession_thenFindSessionById_shouldReturnAllSessions() throws Exception {

        //Créer une session
        Session session = new Session();
        session.setName("Yoga");
        session.setDate(new Date());
        session.setDescription("Description session");

        //Sauvegardée dans la BDD pour le test
        sessionRepository.save(session);

        //On crée une liste de sessionDto
        List<SessionDto> sessions = List.of(this.sessionMapper.toDto(session));
        //Renvoie une liste de sessionDto
        ResponseEntity<?> responseEntity = sessionController.findAll();
        //On compare le 1er nom de la liste des sessions du controller et ce qu'on a créé
        assertEquals(sessions.get(0).getName(), ((List<SessionDto>)responseEntity.getBody()).get(0).getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Should create a session")
    void giveIdSession_thenFindSessionById_shouldCreateSession() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);

       ResponseEntity<?> responseEntity = sessionController.create(sessionDto);
       assertEquals(sessionDto.getName(), ((SessionDto)responseEntity.getBody()).getName());
       assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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

        Session newSession = sessionRepository.save(session);
        ResponseEntity<?> responseEntity = sessionController.update(newSession.getId().toString(), sessionDto);

        assertEquals("Yoga", ((SessionDto) responseEntity.getBody()).getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Should update a session and return a bad request status")
    void giveIdSession_thenUpdateSession_shouldBadRequestStatus() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);

        ResponseEntity<?> responseEntity = sessionController.update("a", sessionDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Should delete a session")
    void giveIdSession_thenFindSessionById_shouldDeleteSession() throws Exception {

        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");

        Session newSession = sessionRepository.save(session);

        ResponseEntity<?> responseEntity = sessionController.save(newSession.getId().toString());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Should delete a session and return a not found status")
    void giveIdSession_thenFindSessionById_shouldReturnNotFoundStatus() throws Exception {

        ResponseEntity<?> responseEntity = sessionController.save("1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should delete a session and return a bad request status")
    void giveIdSession_thenFindSessionById_shouldReturnBadRequestStatus() throws Exception {

        ResponseEntity<?> responseEntity = sessionController.save("a");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should participate to a session")
    void giveIdSession_thenUserParticipateToASession_shouldParticipate() throws Exception {
        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");

        User user = new User("test@example.com", "Doe", "John", "1234", false);
        User newUser = userRepository.save(user);

        Session newSession = sessionRepository.save(session);

        ResponseEntity<?> responseEntity = sessionController.participate(newSession.getId().toString(), newUser.getId().toString());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should participate to a session but return a bad request status")
    void giveIdSession_thenUserParticipateToASession_shouldReturnBadRequestStatus() throws Exception {

        ResponseEntity<?> responseEntity = sessionController.participate("a", "b");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Should no longer participate to a session")
    void giveIdSession_thenUserNoLongerParticipateToASession_shouldNoLongerParticipate() throws Exception {

        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");

        User user = new User("test@example.com", "Doe", "John", "1234", false);
        User newUser = userRepository.save(user);

        List<User> usersList = List.of(newUser);
        session.setUsers(usersList);
        Session newSession = sessionRepository.save(session);

        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(newSession.getId().toString(), newUser.getId().toString());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


    }

    @Test
    @DisplayName("Should no longer participate to a session and return a bad request status")
    void giveIdSession_thenUserNoLongerParticipateToASession_shouldReturnBadRequestStatus() throws Exception {

        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("a", "b");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @AfterEach
    public void cleanup() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
