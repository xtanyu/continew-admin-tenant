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
import top.continew.admin.auth.model.req.PhoneAuthReq;
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
 * 手机号认证处理器
 *
 * @author KAI
 * @since 2024/12/22 14:59
 */
@Component
@RequiredArgsConstructor
public class PhoneAuthHandler extends AbstractAuthHandler implements AuthHandler<PhoneAuthReq, LoginResp> {

    private final UserService userService;
    private final LoginService loginService;

    /**
     * 获取认证类型
     *
     * @return 手机号认证类型
     */
    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.PHONE;
    }

    /**
     * 校验手机号登录请求对象
     *
     * @param authReq 手机号登录请求参数
     */
    @Override
    public void validate(PhoneAuthReq authReq) {
        ValidationUtils.validate(authReq);
    }

    /**
     * 手机号登录
     *
     * @param authReq 手机号登录请求对象
     * @param request HTTP请求对象
     * @return 登录响应
     */
    @Override
    public LoginResp login(PhoneAuthReq authReq, ClientResp clientResp, HttpServletRequest request) {
        //校验参数
        this.validate(authReq);

        String phone = authReq.getPhone();
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + phone;
        String captcha = RedisUtils.get(captchaKey);
        ValidationUtils.throwIfBlank(captcha, AbstractAuthHandler.CAPTCHA_EXPIRED);
        ValidationUtils.throwIfNotEqualIgnoreCase(authReq.getCaptcha(), captcha, AbstractAuthHandler.CAPTCHA_ERROR);
        RedisUtils.delete(captchaKey);

        // 验证手机号
        UserDO user = userService.getByPhone(authReq.getPhone());
        ValidationUtils.throwIfNull(user, "此手机号未绑定本系统账号");

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