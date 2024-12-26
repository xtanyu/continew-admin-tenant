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

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import top.continew.admin.auth.enums.AuthTypeEnum;
import top.continew.admin.auth.model.req.AccountAuthReq;
import top.continew.admin.auth.model.resp.LoginResp;
import top.continew.admin.auth.service.LoginService;
import top.continew.admin.auth.AuthHandler;
import top.continew.admin.common.constant.SysConstants;
import top.continew.admin.common.util.SecureUtils;
import top.continew.admin.system.model.entity.UserDO;
import top.continew.admin.system.model.resp.ClientResp;
import top.continew.admin.system.service.OptionService;
import top.continew.admin.system.service.UserService;
import top.continew.starter.core.util.ExceptionUtils;
import top.continew.starter.core.validation.ValidationUtils;

/**
 * 账号认证处理器
 *
 * @author KAI
 * @since 2024/12/22 14:58:32
 */
@Component
@RequiredArgsConstructor
public class AccountAuthHandler extends AbstractAuthHandler implements AuthHandler<AccountAuthReq, LoginResp> {

    private final UserService userService;
    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;
    private final OptionService optionService;

    /**
     * 获取认证类型
     *
     * @return 账号认证类型
     */
    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.ACCOUNT;
    }

    /**
     * 校验账号登录请求对象
     *
     * @param authReq 登录请求参数
     */
    @Override
    public void validate(AccountAuthReq authReq) {
        // 获取验证码开关
        int enableCaptcha = optionService.getValueByCode2Int("LOGIN_CAPTCHA_ENABLED");

        ValidationUtils.validate(authReq);
        if (SysConstants.YES.equals(enableCaptcha)) {
            ValidationUtils.throwIfEmpty(authReq.getCaptcha(), "验证码不能为空");
            ValidationUtils.throwIfEmpty(authReq.getUuid(), "验证码标识不能为空");
        }
    }

    /**
     * 账号登录
     *
     * @param authReq 账号登录请求对象
     * @param request HTTP请求对象
     * @return 登录响应
     */
    @Override
    public LoginResp login(AccountAuthReq authReq, ClientResp clientResp, HttpServletRequest request) {
        this.validate(authReq);
        // 解密密码
        String rawPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(authReq.getPassword()));
        ValidationUtils.throwIfBlank(rawPassword, "密码解密失败");

        // 验证用户名密码
        UserDO user = userService.getByUsername(authReq.getUsername());
        boolean isError = user == null || !passwordEncoder.matches(rawPassword, user.getPassword());

        // 检查账号锁定状态
        loginService.checkUserLocked(authReq.getUsername(), request, isError);
        ValidationUtils.throwIf(isError, "用户名或密码错误");

        // 检查用户状态
        loginService.checkUserStatus(user);

        // 执行登录
        String token = this.authCertificate(user, clientResp);
        return LoginResp.builder().token(token).build();
    }

    /**
     * 获取认证信息
     *
     * @param user       用户信息
     * @param clientResp 客户端信息
     * @return 认证信息
     */
    @Override
    public String authCertificate(UserDO user, ClientResp clientResp) {
        return super.authCertificate(user, clientResp);
    }
}