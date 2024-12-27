/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.auth.handler;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONUtil;
import com.xkcoding.justauth.AuthRequestFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Component;
import top.continew.admin.auth.AuthHandler;
import top.continew.admin.auth.enums.AuthTypeEnum;
import top.continew.admin.auth.model.req.SocialAuthReq;
import top.continew.admin.auth.model.resp.LoginResp;
import top.continew.admin.auth.service.LoginService;
import top.continew.admin.common.constant.RegexConstants;
import top.continew.admin.common.constant.SysConstants;
import top.continew.admin.common.enums.GenderEnum;
import top.continew.admin.system.model.entity.RoleDO;
import top.continew.admin.system.model.entity.UserDO;
import top.continew.admin.system.model.entity.UserSocialDO;
import top.continew.admin.system.model.resp.ClientResp;
import top.continew.admin.system.service.RoleService;
import top.continew.admin.system.service.UserRoleService;
import top.continew.admin.system.service.UserService;
import top.continew.admin.system.service.UserSocialService;
import top.continew.starter.core.exception.BadRequestException;
import top.continew.starter.core.validation.ValidationUtils;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * 手机号认证处理器
 *
 * @author KAI
 * @since 2024/12/25 14:21
 */
@Component
@RequiredArgsConstructor
public class SocialAuthHandler extends AbstractAuthHandler implements AuthHandler<SocialAuthReq, LoginResp> {
    private final AuthRequestFactory authRequestFactory;
    private final UserSocialService userSocialService;
    private final UserService userService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final LoginService loginService;

    /**
     * 获取认证类型
     *
     * @return 第三方认证类型
     */
    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.SOCIAL_AUTH;
    }

    /**
     * 校验第三方登录请求对象
     *
     * @param authReq 登录请求参数
     */
    @Override
    public void validate(SocialAuthReq authReq) {
        ValidationUtils.validate(authReq);
    }

    /**
     * 第三方登录
     *
     * @param authReq 第三方登录请求对象
     * @param request HTTP请求对象
     * @return 登录响应
     */
    @Override
    public LoginResp login(SocialAuthReq authReq, ClientResp clientResp, HttpServletRequest request) {
        this.validate(authReq);
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
        AuthRequest authRequest = this.getAuthRequest(authReq.getSource());
        AuthCallback callback = new AuthCallback();
        callback.setCode(authReq.getCode());
        callback.setState(authReq.getState());
        AuthResponse<AuthUser> response = authRequest.login(callback);
        ValidationUtils.throwIf(!response.ok(), response.getMsg());
        AuthUser authUser = response.getData();
        String source = authUser.getSource();
        String openId = authUser.getUuid();
        UserSocialDO userSocial = userSocialService.getBySourceAndOpenId(source, openId);
        UserDO user;
        if (null == userSocial) {
            String username = authUser.getUsername();
            String nickname = authUser.getNickname();
            UserDO existsUser = userService.getByUsername(username);
            String randomStr = RandomUtil.randomString(RandomUtil.BASE_CHAR, 5);
            if (null != existsUser || !ReUtil.isMatch(RegexConstants.USERNAME, username)) {
                username = randomStr + IdUtil.fastSimpleUUID();
            }
            if (!ReUtil.isMatch(RegexConstants.GENERAL_NAME, nickname)) {
                nickname = source.toLowerCase() + randomStr;
            }
            user = new UserDO();
            user.setUsername(username);
            user.setNickname(nickname);
            user.setGender(GenderEnum.valueOf(authUser.getGender().name()));
            user.setAvatar(authUser.getAvatar());
            user.setDeptId(SysConstants.SUPER_DEPT_ID);
            Long userId = userService.add(user);
            RoleDO role = roleService.getByCode(SysConstants.SUPER_ROLE_CODE);
            userRoleService.assignRolesToUser(Collections.singletonList(role.getId()), userId);
            userSocial = new UserSocialDO();
            userSocial.setUserId(userId);
            userSocial.setSource(source);
            userSocial.setOpenId(openId);
            loginService.sendSecurityMsg(user);
        } else {
            user = BeanUtil.copyProperties(userService.getById(userSocial.getUserId()), UserDO.class);
        }
        loginService.checkUserStatus(user);
        userSocial.setMetaJson(JSONUtil.toJsonStr(authUser));
        userSocial.setLastLoginTime(LocalDateTime.now());
        userSocialService.saveOrUpdate(userSocial);
        // 执行登录
        String token = this.authCertificate(user, clientResp);
        return LoginResp.builder().token(token).build();
    }

    private AuthRequest getAuthRequest(String source) {
        try {
            return authRequestFactory.get(source);
        } catch (Exception e) {
            throw new BadRequestException("暂不支持 [%s] 平台账号登录".formatted(source));
        }
    }

    /**
     * 获取认证信息
     *
     * @param user       用户信息
     * @param clientResp 客户端信息
     * @return 认证信息
     */
    @Override
    protected String authCertificate(UserDO user, ClientResp clientResp) {
        return super.authCertificate(user, clientResp);
    }

}
