package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void whenTeacherEntityToTeacherDto_thenEquals() {
        // GIVEN
        Teacher givenTeacher = new Teacher();
        givenTeacher.setId(90L);
        givenTeacher.setLastName("By-Id");
        givenTeacher.setFirstName("Find");
        givenTeacher.setCreatedAt(LocalDateTime.now());
        givenTeacher.setUpdatedAt(null);

        // WHEN
        TeacherDto teacherDto = teacherMapper.toDto(givenTeacher);

        // THEN
        assertEquals(givenTeacher.getId(), teacherDto.getId());
        assertEquals(givenTeacher.getLastName(), teacherDto.getLastName());
        assertEquals(givenTeacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(givenTeacher.getCreatedAt(), teacherDto.getCreatedAt());
    }

    @Test
    public void whenTeacherDtoToTeacherEntity_ThenEquals() {
        // GIVEN
        TeacherDto givenTeacherDto = new TeacherDto();
        givenTeacherDto.setId(90L);
        givenTeacherDto.setLastName("By-Id");
        givenTeacherDto.setFirstName("Find");
        givenTeacherDto.setCreatedAt(LocalDateTime.now());
        givenTeacherDto.setUpdatedAt(null);

        // WHEN
        Teacher teacher = teacherMapper.toEntity(givenTeacherDto);

        // THEN
        assertEquals(givenTeacherDto.getId(), teacher.getId());
        assertEquals(givenTeacherDto.getLastName(), teacher.getLastName());
        assertEquals(givenTeacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(givenTeacherDto.getCreatedAt(), teacher.getCreatedAt());
    }

    @Test
    public void whenTeacherEntityListToTeacherDtoList_thenListsHaveSameSize() {
        // GIVEN
        List<Teacher> givenTeachers = new ArrayList<>();

        givenTeachers.add(new Teacher(91L, "Teacher", "First", LocalDateTime.now(), null));
        givenTeachers.add(new Teacher(92L, "Teacher", "Second", LocalDateTime.now(), null));
        givenTeachers.add(new Teacher(93L, "Teacher", "Third", LocalDateTime.now(), null));

        // WHEN
        List<TeacherDto> teacherDtos = teacherMapper.toDto(givenTeachers);

        // THEN
        assertEquals(givenTeachers.size(), teacherDtos.size());
    }

    @Test
    public void whenTeacherDtoListToTeacherEntityList_thenListsHaveSameSize() {
        // GIVEN
        List<TeacherDto> givenTeacherDtos = new ArrayList<>();

        givenTeacherDtos.add(new TeacherDto(91L, "Teacher", "First", LocalDateTime.now(), null));
        givenTeacherDtos.add(new TeacherDto(92L, "Teacher", "Second", LocalDateTime.now(), null));
        givenTeacherDtos.add(new TeacherDto(93L, "Teacher", "Third", LocalDateTime.now(), null));

        // WHEN
        List<Teacher> teachers= teacherMapper.toEntity(givenTeacherDtos);

        // THEN
        assertEquals(givenTeacherDtos.size(), teachers.size());
    }
}
