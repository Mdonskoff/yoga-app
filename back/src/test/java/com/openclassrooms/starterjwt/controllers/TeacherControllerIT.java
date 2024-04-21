package com.openclassrooms.starterjwt.controllers;


import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TeacherControllerIT {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
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
        teacher.setFirstName("Donald");
        teacher.setLastName("Duck");
        TeacherDto expectedTeacherDto = teacherMapper.toDto(teacher);

        when(teacherRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(teacher));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(expectedTeacherDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(expectedTeacherDto.getFirstName()));

        verify(teacherRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should return a not found status")
    void giveIdTeacher_thenFindTeacherById_shouldReturnNotFoundStatus() throws Exception {

        when(teacherRepository.findById(Long.parseLong(id))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(teacherRepository).findById(Long.parseLong(id));
    }
    @Test
    @DisplayName("Should return a bad request status")
    void giveIdTeacher_thenFindTeacherById_shouldReturnBadRequestStatus() throws Exception {

        when(teacherRepository.findById(Long.parseLong(id))).thenThrow(NumberFormatException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(teacherRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should return all teachers")
    void giveIdTeacher_thenFindTeacherById_shouldReturnAllTeachers() throws Exception {

        List<Teacher> teachers = List.of(new Teacher().setFirstName("Donald").setLastName("Duck"),
                new Teacher().setFirstName("Mickey").setLastName("Mousse"));

        when(teacherRepository.findAll()).thenReturn(teachers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Donald"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Mousse"));

        verify(teacherRepository).findAll();
    }




}
