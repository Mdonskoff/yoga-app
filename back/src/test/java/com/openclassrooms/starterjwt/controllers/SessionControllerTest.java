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

        Session savedSession = sessionRepository.save(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Description session");
        sessionDto.setTeacher_id(1L);

        SessionDto expectedSessionDto = sessionMapper.toDto(session);

        // Effectuez une demande GET pour obtenir la session par son ID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", expectedSessionDto.getId()))
                // Vérifiez que la réponse est un code de statut OK
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Vérifiez que le contenu de la réponse est de type JSON
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                // Vérifiez que le nom de la session dans le JSON de la réponse correspond au nom de la session enregistrée
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Yoga"))
                // Vérifiez que l'ID de la session dans le JSON de la réponse correspond à l'ID de la session enregistrée
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedSessionDto.getId()));

        /*
        ResponseEntity<?> responseEntity = sessionController.findById(String.valueOf(idSession));
        assertEquals(sessionDto.getName(), ((SessionDto)responseEntity.getBody()).getName());
        assertEquals(sessionDto.getDescription(), ((SessionDto)responseEntity.getBody()).getDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        */
    }

    @Test
    @DisplayName("Should return a not found status")
    void giveIdSession_thenFindSessionById_shouldNotFoundStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        /*
        ResponseEntity<?> responseEntity = sessionController.findById("1");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
         */

    }

    @Test //À VÉRIFIER
    @DisplayName("Should return a bad request status")
    void giveIdSession_thenFindSessionById_shouldBadRequestStatus() throws Exception {

        sessionController.findById("a");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", "a"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        /*
        ResponseEntity<?> responseEntity = sessionController.findById("a");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
         */

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

        //Créer une 2e session
        Session session2 = new Session();
        session2.setName("Zen");
        session2.setDate(new Date());
        session2.setDescription("Description session");

        sessionRepository.save(session2);

        //On crée une liste de sessionDto
        //List<SessionDto> sessions = List.of(this.sessionMapper.toDto(session));
        List<Session> sessions = List.of(new Session().setName("Yoga"), new Session().setName("Zen"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Yoga"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Zen"));

        /*
        //Renvoie une liste de sessionDto
        ResponseEntity<?> responseEntity = sessionController.findAll();
        //On compare le 1er nom de la liste des sessions du controller et ce qu'on a créé
        assertEquals(sessions.get(0).getName(), ((List<SessionDto>)responseEntity.getBody()).get(0).getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
         */

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
        /*
       ResponseEntity<?> responseEntity = sessionController.create(sessionDto);
       assertEquals(sessionDto.getName(), ((SessionDto)responseEntity.getBody()).getName());
       assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
         */
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

        /*
        ResponseEntity<?> responseEntity = sessionController.update(newSession.getId().toString(), sessionDto);
        assertEquals("Yoga", ((SessionDto) responseEntity.getBody()).getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
         */

    }

    @Test //À VÉRIFIER
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

        /*ResponseEntity<?> responseEntity = sessionController.update("a", sessionDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());*/

    }

    @Test
    @DisplayName("Should delete a session")
    void giveIdSession_thenFindSessionById_shouldDeleteSession() throws Exception {

        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");

        // Enregistrez la session dans la base de données
        Session savedSession = sessionRepository.save(session);

        // Obtenez l'ID de la session enregistrée
        Long sessionId = savedSession.getId();

        // Effectuez une demande DELETE vers la route /api/session/{id} avec l'ID de la session
        mockMvc.perform(delete("/api/session/{id}", sessionId))
                // Vérifiez que la réponse est un code de statut OK
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifiez que la session a été supprimée de la base de données
        assertFalse(sessionRepository.existsById(sessionId));

       /* ResponseEntity<?> responseEntity = sessionController.save(newSession.getId().toString());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); */

    }

    @Test
    @DisplayName("Should delete a session and return a not found status")
    void giveIdSession_thenFindSessionById_shouldReturnNotFoundStatus() throws Exception {

        mockMvc.perform(delete("/api/session/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        /* ResponseEntity<?> responseEntity = sessionController.save("1");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()); */
    }

    @Test //À VÉRIFIER
    @DisplayName("Should delete a session and return a bad request status")
    void giveIdSession_thenFindSessionById_shouldReturnBadRequestStatus() throws Exception {

        mockMvc.perform(delete("/api/session/{id}", "a"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        /* ResponseEntity<?> responseEntity = sessionController.save("a");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()); */
    }

    @Test
    @DisplayName("Should participate to a session")
    void giveIdSession_thenUserParticipateToASession_shouldParticipate() throws Exception {

        // Créez une session et enregistrez-la dans la base de données
        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");
        Session savedSession = sessionRepository.save(session);

        // Créez un utilisateur et enregistrez-le dans la base de données
        User user = new User("test@example.com", "Doe", "John", "1234", false);
        User savedUser = userRepository.save(user);

        // Obtenez l'ID de la session et de l'utilisateur enregistrés
        Long sessionId = savedSession.getId();
        Long userId = savedUser.getId();

        // Effectuez une demande POST vers la route /api/session/{id}/participate/{userId} avec les IDs de session et d'utilisateur
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{sessionId}/participate/{userId}", sessionId, userId))
                // Vérifiez que la réponse est un code de statut OK
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifiez que l'utilisateur participe maintenant à la session
        Session updatedSession = sessionRepository.findById(sessionId).orElse(null);
        assertNotNull(updatedSession);
        assertTrue(updatedSession.getUsers().contains(savedUser));

       /* ResponseEntity<?> responseEntity = sessionController.participate(newSession.getId().toString(), newUser.getId().toString());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); */
    }

    @Test //À VÉRIFIER
    @DisplayName("Should participate to a session but return a bad request status")
    void giveIdSession_thenUserParticipateToASession_shouldReturnBadRequestStatus() throws Exception {

        // Effectuez une demande POST vers la route /api/session/{id}/participate/{userId} avec des IDs invalides
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}", "a", "b"))
                // Vérifiez que la réponse est un code de statut BadRequest
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        /* ResponseEntity<?> responseEntity = sessionController.participate("a", "b");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()); */

    }

    @Test //À VÉRIFIER
    @DisplayName("Should no longer participate to a session")
    void giveIdSession_thenUserNoLongerParticipateToASession_shouldNoLongerParticipate() throws Exception {

        // Créez une session et enregistrez-la dans la base de données
        Session session = new Session();
        session.setName("Zen");
        session.setDate(new Date());
        session.setDescription("Description session");
        Session savedSession = sessionRepository.save(session);

        // Créez un utilisateur et enregistrez-le dans la base de données
        User user = new User("test@example.com", "Doe", "John", "1234", false);
        User savedUser = userRepository.save(user);

        //Créer une liste de users, y ajouter l'user créé à la session
        List<User> usersList = new ArrayList<>();
        usersList.add(savedUser);
        savedSession.setUsers(usersList);
        sessionRepository.save(savedSession);

        // Obtenez l'ID de la session et de l'utilisateur enregistrés
        Long sessionId = savedSession.getId();
        Long userId = savedUser.getId();

        // Effectuez une demande DELETE pour retirer l'utilisateur de la session
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{sessionId}/participate/{userId}", sessionId, userId))
                // Vérifiez que la réponse est un code de statut OK
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifiez que l'utilisateur ne participe plus à la session
        Session updatedSession = sessionRepository.findById(sessionId).orElse(null);
        assertNotNull(updatedSession);
        assertFalse(updatedSession.getUsers().contains(savedUser));

        /*
        // Ajoutez l'utilisateur à la session
        savedSession.getUsers().add(savedUser);
        sessionRepository.save(savedSession);

         // Obtenez l'ID de la session et de l'utilisateur
        Long sessionId = savedSession.getId();
        Long userId = savedUser.getId();

        // Effectuez une demande POST vers la route /api/session/{id}/participate/{userId} pour retirer l'utilisateur de la session
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{sessionId}/participate/{userId}", sessionId, userId))
                // Vérifiez que la réponse est un code de statut OK
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifiez que l'utilisateur ne participe plus à la session
        Session updatedSession = sessionRepository.findById(Long.valueOf(id)).orElse(null);
        assertNotNull(updatedSession);
        assertFalse(updatedSession.getUsers().contains(savedUser));

         */

        /* ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(newSession.getId().toString(), newUser.getId().toString());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); */

    }

    @Test //À VÉRIFIER
    @DisplayName("Should no longer participate to a session and return a bad request status")
    void giveIdSession_thenUserNoLongerParticipateToASession_shouldReturnBadRequestStatus() throws Exception {

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", "a", "b"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

       /* ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("a", "b");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()); */
    }

    @AfterEach
    public void cleanup() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
