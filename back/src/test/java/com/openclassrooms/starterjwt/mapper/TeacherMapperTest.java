package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    TeacherMapper teacherMapper;

    @Test
    public void testToEntity() {

        TeacherDto teacherDto = null;
        List<TeacherDto> teacherDtoList = null;

        assertNull(teacherMapper.toEntity(teacherDto));
        assertNull(teacherMapper.toEntity(teacherDtoList));
    }

    @Test
    public void testToDto() {

        Teacher teacher = null;
        List<Teacher> teacherList = null;

        assertNull(teacherMapper.toDto(teacher));
        assertNull(teacherMapper.toDto(teacherList));
    }

}
