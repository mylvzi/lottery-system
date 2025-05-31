package org.example.lottery_system.common.filter;

import org.example.lottery_system.dao.dataobject.Encrypt;
import org.example.lottery_system.dao.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SqlTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void phoneCnt() {
        int cnt = userMapper.countByPhone(new Encrypt("131111111111"));
        System.out.println("手机号的数量为:" + cnt);
    }
}
