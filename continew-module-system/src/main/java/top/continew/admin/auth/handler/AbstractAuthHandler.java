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

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import top.continew.admin.common.context.RoleContext;
import top.continew.admin.common.context.UserContext;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.common.context.UserExtraContext;
import top.continew.admin.system.model.entity.UserDO;
import top.continew.admin.system.model.resp.ClientResp;
import top.continew.admin.system.service.OptionService;
import top.continew.admin.system.service.RoleService;
import top.continew.starter.web.util.SpringWebUtils;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static top.continew.admin.system.enums.PasswordPolicyEnum.PASSWORD_EXPIRATION_DAYS;

/**
 * 认证处理器抽象类
 *
 * @author KAI
 * @since 2024/12/22 14:52
 */
@Component
@RequiredArgsConstructor
public abstract class AbstractAuthHandler {
    @Resource
    private RoleService roleService;
    @Resource
    private OptionService optionService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public static final String CAPTCHA_EXPIRED = "验证码已失效";
    public static final String CAPTCHA_ERROR = "验证码错误";
    public static final String CLIENT_ID = "clientId";

    /**
     * 获取登录凭证
     *
     * @param user       用户信息
     * @param clientResp 客户端信息
     * @return token 认证信息
     */
    protected String authCertificate(UserDO user, ClientResp clientResp) {
        preLogin(user, clientResp);
        // 核心登录逻辑
        Long userId = user.getId();
        CompletableFuture<Set<String>> permissionFuture = CompletableFuture.supplyAsync(() -> roleService
            .listPermissionByUserId(userId), threadPoolTaskExecutor);
        CompletableFuture<Set<RoleContext>> roleFuture = CompletableFuture.supplyAsync(() -> roleService
            .listByUserId(userId), threadPoolTaskExecutor);
        CompletableFuture<Integer> passwordExpirationDaysFuture = CompletableFuture.supplyAsync(() -> optionService
            .getValueByCode2Int(PASSWORD_EXPIRATION_DAYS.name()));
        CompletableFuture.allOf(permissionFuture, roleFuture, passwordExpirationDaysFuture);

        UserContext userContext = new UserContext(permissionFuture.join(), roleFuture
            .join(), passwordExpirationDaysFuture.join());

        BeanUtil.copyProperties(user, userContext);
        SaLoginModel model = new SaLoginModel();
        // 设置登录 token 最低活跃频率 如未指定，则使用全局配置的 activeTimeout 值
        model.setActiveTimeout(clientResp.getActiveTimeout());
        // 设置登录 token 有效期，单位：秒 （如未指定，自动取全局配置的 timeout 值
        model.setTimeout(clientResp.getTimeout());
        // 设置设备类型
        model.setDevice(clientResp.getClientType());
        userContext.setClientType(clientResp.getClientType());
        // 设置客户端id
        userContext.setClientId(clientResp.getClientId());
        model.setExtra(CLIENT_ID, clientResp.getClientId());
        // 自定义用户上下文处理
        customizeUserContext(userContext, user, clientResp);

        // 登录并缓存用户信息
        StpUtil.login(userContext.getId(), model.setExtraData(BeanUtil.beanToMap(new UserExtraContext(SpringWebUtils
            .getRequest()))));
        UserContextHolder.setContext(userContext);

        // 后置处理
        String token = StpUtil.getTokenValue();
        postLogin(token, user, clientResp);
        return token;
    }

    /**
     * 登录前置处理
     *
     * @param user       用户信息
     * @param clientResp 客户端信息
     */
    private void preLogin(UserDO user, ClientResp clientResp) {
    }

    /**
     * 自定义用户上下文处理
     *
     * @param userContext 用户上下文
     * @param user        用户信息
     * @param clientResp  客户端信息
     */
    protected void customizeUserContext(UserContext userContext, UserDO user, ClientResp clientResp) {
    }

    /**
     * 登录后置处理
     *
     * @param token      登录令牌
     * @param user       用户信息
     * @param clientResp 客户端信息
     */
    protected void postLogin(String token, UserDO user, ClientResp clientResp) {
    }
}