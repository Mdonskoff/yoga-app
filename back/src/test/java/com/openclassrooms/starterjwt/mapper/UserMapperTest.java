package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void whenUserEntityToUserDto_thenEquals() {
        // GIVEN
        User givenUser = new User();
        givenUser.setId(99L);
        givenUser.setEmail("find-by-id@mail.com");
        givenUser.setLastName("By-Id");
        givenUser.setFirstName("Find");
        givenUser.setPassword("pass");
        givenUser.setAdmin(true);
        givenUser.setCreatedAt(LocalDateTime.now());
        givenUser.setUpdatedAt(null);

        // WHEN
        UserDto userDto = userMapper.toDto(givenUser);

        // THEN
        assertEquals(givenUser.getId(), userDto.getId());
        assertEquals(givenUser.getEmail(), userDto.getEmail());
        assertEquals(givenUser.getLastName(), userDto.getLastName());
        assertEquals(givenUser.getFirstName(), userDto.getFirstName());
        assertEquals(givenUser.getPassword(), userDto.getPassword());
        assertEquals(givenUser.isAdmin(), userDto.isAdmin());
        assertEquals(givenUser.getCreatedAt(), userDto.getCreatedAt());
    }

    @Test
    public void whenUserDtoToUserEntity_thenEquals() {
        // GIVEN
        UserDto givenUserDto = new UserDto();
        givenUserDto.setId(98L);
        givenUserDto.setEmail("delete-by-id@mail.com");
        givenUserDto.setLastName("By-Id");
        givenUserDto.setFirstName("Delete");
        givenUserDto.setPassword("pass");
        givenUserDto.setAdmin(false);
        givenUserDto.setCreatedAt(LocalDateTime.now());
        givenUserDto.setUpdatedAt(null);

        // WHEN
        User user = userMapper.toEntity(givenUserDto);

        // THEN
        assertEquals(givenUserDto.getId(), user.getId());
        assertEquals(givenUserDto.getEmail(), user.getEmail());
        assertEquals(givenUserDto.getLastName(), user.getLastName());
        assertEquals(givenUserDto.getFirstName(), user.getFirstName());
        assertEquals(givenUserDto.getPassword(), user.getPassword());
        assertEquals(givenUserDto.isAdmin(), user.isAdmin());
        assertEquals(givenUserDto.getCreatedAt(), user.getCreatedAt());
    }

    @Test
    public void whenUserEntityListToUserDtoList_thenListsHaveSameSize() {

        // GIVEN
        User givenUser1 = new User();
        givenUser1.setId(99L);
        givenUser1.setEmail("find-by-id@mail.com");
        givenUser1.setLastName("By-Id");
        givenUser1.setFirstName("Find");
        givenUser1.setPassword("pass");
        givenUser1.setAdmin(true);
        givenUser1.setCreatedAt(LocalDateTime.now());
        givenUser1.setUpdatedAt(null);

        User givenUser2 = new User();
        givenUser2.setId(98L);
        givenUser2.setEmail("delete-by-id@mail.com");
        givenUser2.setLastName("By-Id");
        givenUser2.setFirstName("Delete");
        givenUser2.setPassword("pass");
        givenUser2.setAdmin(false);
        givenUser2.setCreatedAt(LocalDateTime.now());
        givenUser2.setUpdatedAt(null);

        List<User> users= new ArrayList<>();
        users.add(givenUser1);
        users.add(givenUser2);

        // WHEN
        List<UserDto> userDtoList = userMapper.toDto(users);

        // THEN
        assertEquals(users.size(), userDtoList.size());
    }

    @Test
    public void whenUserDtoListToUserEntityList_thenListsHaveSameSize() {
        // GIVEN
        UserDto givenUserDto1 = new UserDto();
        givenUserDto1.setId(99L);
        givenUserDto1.setEmail("find-by-id@mail.com");
        givenUserDto1.setLastName("By-Id");
        givenUserDto1.setFirstName("Find");
        givenUserDto1.setPassword("pass");
        givenUserDto1.setAdmin(true);
        givenUserDto1.setCreatedAt(LocalDateTime.now());
        givenUserDto1.setUpdatedAt(null);

        UserDto givenUserDto2 = new UserDto();
        givenUserDto2.setId(98L);
        givenUserDto2.setEmail("delete-by-id@mail.com");
        givenUserDto2.setLastName("By-Id");
        givenUserDto2.setFirstName("Delete");
        givenUserDto2.setPassword("pass");
        givenUserDto2.setAdmin(false);
        givenUserDto2.setCreatedAt(LocalDateTime.now());
        givenUserDto2.setUpdatedAt(null);

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(givenUserDto1);
        userDtos.add(givenUserDto2);

        // WHEN
        List<User> userList = userMapper.toEntity(userDtos);

        // THEN
        assertEquals(userDtos.size(), userList.size());
    }
}
