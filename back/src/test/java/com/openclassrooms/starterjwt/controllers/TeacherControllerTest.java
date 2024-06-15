package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
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

import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherController teacherController;

    private MockMvc mockMvc;

    private final String id = "10";

    @BeforeEach
    void setUp(){
        teacherService = new TeacherService(teacherRepository);
        teacherController = new TeacherController(teacherService, teacherMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    @DisplayName("Should return a teacher")
    void giveIdTeacher_thenFindTeacherById_shouldReturnATeacherDto() throws Exception {

        Teacher teacher = new Teacher();
        teacher.setFirstName("valery");
        teacher.setLastName("Dupont");
        TeacherDto expectedTeacherDto = teacherMapper.toDto(teacher);

        Teacher newTeacher = teacherRepository.save(teacher);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", newTeacher.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(expectedTeacherDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(expectedTeacherDto.getFirstName()));

    }

    @Test
    @DisplayName("Should return a not found status")
    void giveIdTeacher_thenFindTeacherById_shouldReturnNotFoundStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
    @Test
    @DisplayName("Should return a bad request status")
    void giveIdTeacher_thenFindTeacherById_shouldReturnBadRequestStatus() throws Exception {

        teacherController.findById("a");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", "a"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("Should return all teachers")
    void giveIdTeacher_thenFindTeacherById_shouldReturnAllTeachers() throws Exception {

        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("Mickey");
        teacher1.setLastName("Mousse");
        teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Donald");
        teacher2.setLastName("Duck");
        teacherRepository.save(teacher2);

        List.of(this.teacherMapper.toDto(teacher1));
        List.of(this.teacherMapper.toDto(teacher2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("Mickey"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value("Mousse"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].firstName").value("Donald"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].lastName").value("Duck"));

    }

    @AfterEach
    public void cleanup() {
        teacherRepository.deleteAll();
    }




}
