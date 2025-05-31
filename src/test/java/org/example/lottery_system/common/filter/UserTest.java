package org.example.lottery_system.common.filter;

import org.example.lottery_system.service.UserService;
import org.example.lottery_system.service.dto.UserDTO;
import org.example.lottery_system.service.enums.UserIdentityEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    void findBaseUserList() {

        List<UserDTO> userDTOList = userService.findUserInfo(UserIdentityEnum.ADMIN);
        for (UserDTO userDTO : userDTOList) {
            System.out.println(userDTO.getUserId() + " " + userDTO.getUserName() + " " + userDTO.getIdentity().name());
        }
    }

}