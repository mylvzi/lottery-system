package org.example.lottery_system.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import org.example.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.example.lottery_system.common.exception.ServiceException;
import org.example.lottery_system.common.utils.JWTUtil;
import org.example.lottery_system.common.utils.RegexUtil;
import org.example.lottery_system.controller.param.ShortMessageLoginParam;
import org.example.lottery_system.controller.param.UserLoginParam;
import org.example.lottery_system.controller.param.UserPasswordLoginParam;
import org.example.lottery_system.controller.param.UserRegisterParam;
import org.example.lottery_system.dao.dataobject.Encrypt;
import org.example.lottery_system.dao.dataobject.UserDO;
import org.example.lottery_system.dao.mapper.UserMapper;
import org.example.lottery_system.service.UserService;
import org.example.lottery_system.service.VerificationCodeService;
import org.example.lottery_system.service.dto.UserDTO;
import org.example.lottery_system.service.dto.UserLoginDTO;
import org.example.lottery_system.service.dto.UserRegisterDTO;
import org.example.lottery_system.service.enums.UserIdentityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VerificationCodeService verificationCodeService;

    @Override
    public UserRegisterDTO register(UserRegisterParam param) {

        // 校验注册信息
        checkRegisterInfo(param);

        // 加密私密数据（构造dao层请求）
        UserDO userDO = new UserDO();
        userDO.setUserName(param.getName());
        userDO.setEmail(param.getMail());
        userDO.setPhoneNumber(new Encrypt(param.getPhoneNumber()));// 手机号需要传入加密后的数据
        userDO.setIdentity(param.getIdentity());
        if (StringUtils.hasText(param.getPassword())) {// 判断不为空
            userDO.setPassword(DigestUtil.sha256Hex(param.getPassword()));// 密码加密
        }
        // 保存数据  入库
        userMapper.insert(userDO);

        // 构造返回  入库成功
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUserId(userDO.getId());
        return userRegisterDTO;
    }


    @Override
    public UserLoginDTO login(UserLoginParam param) {
        UserLoginDTO userLoginDTO;

        // 类型检查与类型转换，java 14及以上版本  判断+转换
        if (param instanceof UserPasswordLoginParam loginParam) {
            // 密码登录流程
            userLoginDTO = loginByUserPassword(loginParam);
        } else if (param instanceof ShortMessageLoginParam loginParam) {
            // 短信验证码登录流程
            userLoginDTO = loginByShortMessage(loginParam);
        } else {
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_INFO_NOT_EXIST);
        }

        return userLoginDTO;
    }

    @Override
    public List<UserDTO> findUserInfo(UserIdentityEnum identity) {
        String identityString =  null == identity ? null : identity.name();
        // 查表
        List<UserDO> userDOList = userMapper.selectUserListByIdentity(identityString);
        List<UserDTO> userDTOList = userDOList.stream()
                .map(userDO -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUserId(userDO.getId());
                    userDTO.setUserName(userDO.getUserName());
                    userDTO.setEmail(userDO.getEmail());
                    userDTO.setPhoneNumber(userDO.getPhoneNumber().getValue());
                    userDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
                    return userDTO;
                }).collect(Collectors.toList());
        return userDTOList;
    }

    /**
     * 验证码登录
     *
     * @param loginParam
     * @return
     */
    private UserLoginDTO loginByShortMessage(ShortMessageLoginParam loginParam) {
        if (!RegexUtil.checkMobile(loginParam.getLoginMobile())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }

        // 获取用户数据
        UserDO userDO = userMapper.selectByPhoneNumber(
                new Encrypt(loginParam.getLoginMobile()));
        if (null == userDO) {
            throw new ServiceException(ServiceErrorCodeConstants.USER_INFO_IS_EMPTY);
        } else if (StringUtils.hasText(loginParam.getMandatoryIdentity())
                && !loginParam.getMandatoryIdentity()
                .equalsIgnoreCase(userDO.getIdentity())) {
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }

        // 校验验证码
        String code = verificationCodeService.getVerificationCode(
                loginParam.getLoginMobile());
        if (!loginParam.getVerificationCode().equals(code)) {
            throw new ServiceException(ServiceErrorCodeConstants.VERIFICATION_CODE_ERROR);
        }

        // 塞入返回值（JWT）
        Map<String, Object> claim = new HashMap<>();
        claim.put("id", userDO.getId());
        claim.put("identity", userDO.getIdentity());
        String token = JWTUtil.genJwt(claim);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
        return userLoginDTO;


    }

    /**
     * 密码登录
     *
     * @param loginParam
     * @return
     */
    private UserLoginDTO loginByUserPassword(UserPasswordLoginParam loginParam) {

        UserDO userDO = null;
        // 判断手机登录还是邮箱登录
        if (RegexUtil.checkMail(loginParam.getLoginName())) {
            // 邮箱登录
            // 根据邮箱查询用户表
            userDO = userMapper.selectByMail(loginParam.getLoginName());
        } else if (RegexUtil.checkMobile(loginParam.getLoginName())) {
            // 手机号登录
            // 根据手机号查询用户表
            userDO = userMapper.selectByPhoneNumber(new Encrypt(loginParam.getLoginName()));
        } else {// 登录方式不存在
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_NOT_EXIST);
        }

        // 校验登录信息
        // 主要校验两部分：1.用户的身份信息是否合理  2.密码是否一致
        if (null == userDO) {// 用户不存在
            throw new ServiceException(ServiceErrorCodeConstants.USER_INFO_IS_EMPTY);
        } else if (StringUtils.hasText(loginParam.getMandatoryIdentity())
                && !loginParam.getMandatoryIdentity()
                .equalsIgnoreCase(userDO.getIdentity())) {
            // 强制身份登录，身份校验不通过
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        } else if (!DigestUtil.sha256Hex(loginParam.getPassword())
                .equals(userDO.getPassword())) {
            // 校验密码不同
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
        }

        /**
         * 构造返回值
         * jwt
         * identity
         */
        // 塞入返回值（JWT）
        Map<String, Object> claim = new HashMap<>();
        claim.put("id", userDO.getId());
        claim.put("identity", userDO.getIdentity());
        String token = JWTUtil.genJwt(claim);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
        return userLoginDTO;

    }

    private void checkRegisterInfo(UserRegisterParam param) {
        if (null == param) {
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_INFO_IS_EMPTY);
        }
        // 校验邮箱格式 xxx@xxx.xxx  邮箱格式和手机号格式都是通过正则表达式进行校验
        if (!RegexUtil.checkMail(param.getMail())) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        // 校验手机号格式
        if (!RegexUtil.checkMobile(param.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }

        // 校验身份信息  枚举类型维护  ：管理员  普通用户
        if (null == UserIdentityEnum.forName(param.getIdentity())) {
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }

        // 校验管理员密码必填  为空直接抛出异常
        if (param.getIdentity().equalsIgnoreCase(UserIdentityEnum.ADMIN.name())
                && !StringUtils.hasText(param.getPassword())) {
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_IS_EMPTY);
        }

        // 密码校验，至少6位  只有管理员才会传输密码
        if (StringUtils.hasText(param.getPassword())
                && !RegexUtil.checkPassword(param.getPassword())) {
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
        }

        // 校验邮箱是否被使用
        if (checkMailUsed(param.getMail())) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_USED);
        }

        // 校验手机号是否被使用  手机号是私密数据  加密存储在数据库中  从数据库中取出需要解密
        if (checkPhoneNumberUsed(param.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_USED);
        }
    }

    /**
     * 校验手机号是否被使用
     *
     * @param phoneNumber
     * @return
     */
    private boolean checkPhoneNumberUsed(String phoneNumber) {
        int count = userMapper.countByPhone(new Encrypt(phoneNumber));
        return count > 0;

    }

    /**
     * 校验邮箱是否被使用
     *
     * @param mail
     * @return
     */
    private boolean checkMailUsed(String mail) {
        int count = userMapper.countByMail(mail);
        return count > 0;
    }
}