package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testToEntity() {

        UserDto userDto = null;
        List<UserDto> userDtoList = null;

        assertNull(userMapper.toEntity(userDto));
        assertNull(userMapper.toEntity(userDtoList));
    }

    @Test
    public void testToDto() {

        User user = null;
        List<User> userList = null;

        assertNull(userMapper.toDto(user));
        assertNull(userMapper.toDto(userList));
    }
}
