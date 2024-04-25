package com.openclassrooms.starterjwt.services;


import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private final long id = 1L;

    @BeforeEach
    public void setup() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    @DisplayName("Should find all teachers")
    public void giveVoid_thenFindAllTeachers_shouldReturnAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> newTeacher = teacherService.findAll();

        Mockito.verify(teacherRepository).findAll();
        assertEquals(teachers, newTeacher);
    }

    @Test
    @DisplayName("Should find teacher by ID")
    public void giveIdTeacher_thenFindTeachById_shouldATeacherById() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher newTeacher = teacherService.findById(id);

        Mockito.verify(teacherRepository).findById(id);
        assertEquals(teacher, newTeacher);
    }

    @Test
    @DisplayName("should return empty teacher")
    public void giveIDTeacher_thenFindUserById_shouldReturnEmpty() {
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        Teacher newTeacher = teacherService.findById(id);

        Mockito.verify(teacherRepository).findById(id);
        assertNull(newTeacher);
    }
}
