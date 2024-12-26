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
import org.springframework.stereotype.Component;
import top.continew.admin.auth.enums.AuthTypeEnum;
import top.continew.admin.auth.model.req.EmailAuthReq;
import top.continew.admin.auth.model.resp.LoginResp;
import top.continew.admin.auth.service.LoginService;
import top.continew.admin.auth.AuthHandler;
import top.continew.admin.common.constant.CacheConstants;
import top.continew.admin.system.model.entity.UserDO;
import top.continew.admin.system.model.resp.ClientResp;
import top.continew.admin.system.service.UserService;
import top.continew.starter.cache.redisson.util.RedisUtils;
import top.continew.starter.core.validation.ValidationUtils;

/**
 * 邮箱认证处理器
 *
 * @author KAI
 * @since 2024/12/22 14:58
 */
@Component
@RequiredArgsConstructor
public class EmailAuthHandler extends AbstractAuthHandler implements AuthHandler<EmailAuthReq, LoginResp> {

    private final UserService userService;
    private final LoginService loginService;

    /**
     * 获取认证类型
     *
     * @return 邮箱认证类型
     */
    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.EMAIL;
    }

    /**
     * 校验邮箱登录请求对象
     *
     * @param authReq 邮箱登录请求参数
     */
    @Override
    public void validate(EmailAuthReq authReq) {
        ValidationUtils.validate(authReq);
    }

    /**
     * 邮箱登录
     *
     * @param authReq 邮箱登录请求对象
     * @param request HTTP请求对象
     * @return 登录响应
     */
    @Override
    public LoginResp login(EmailAuthReq authReq, ClientResp clientResp, HttpServletRequest request) {
        this.validate(authReq);

        String email = authReq.getEmail();
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + email;
        String captcha = RedisUtils.get(captchaKey);
        ValidationUtils.throwIfBlank(captcha, AbstractAuthHandler.CAPTCHA_EXPIRED);
        ValidationUtils.throwIfNotEqualIgnoreCase(authReq.getCaptcha(), captcha, CAPTCHA_ERROR);
        RedisUtils.delete(captchaKey);
        // 验证邮箱
        UserDO user = userService.getByEmail(authReq.getEmail());
        ValidationUtils.throwIfNull(user, "此邮箱未绑定本系统账号");

        // 检查用户状态
        loginService.checkUserStatus(user);

        // 执行登录
        String token = this.authCertificate(user, clientResp);
        return LoginResp.builder().token(token).build();
    }

    /**
     * 获取登录凭证
     *
     * @param user       用户信息
     * @param clientResp 客户端信息
     * @return token 认证信息
     */
    @Override
    public String authCertificate(UserDO user, ClientResp clientResp) {
        return super.authCertificate(user, clientResp);
    }
}