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

package top.continew.admin.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import me.zhyd.oauth.model.AuthUser;
import top.continew.admin.auth.model.resp.RouteResp;
import top.continew.admin.system.model.entity.UserDO;

import java.util.List;

/**
 * 登录业务接口
 *
 * @author Charles7c
 * @since 2022/12/21 21:48
 */
public interface LoginService {

    /**
     * 检查用户状态
     *
     * @param user 用户信息
     */
    void checkUserStatus(UserDO user);

    /**
     * 检查用户是否被锁定
     *
     * @param username 用户名
     * @param request  请求对象
     * @param isError  是否登录错误
     */
    void checkUserLocked(String username, HttpServletRequest request, boolean isError);

    /**
     * 执行登录操作
     *
     * @param user 用户信息
     * @return token
     */
    String login(UserDO user);

    /**
     * 三方账号登录
     *
     * @param authUser 三方账号信息
     * @return 令牌
     */
    String socialLogin(AuthUser authUser);

    /**
     * 构建路由树
     *
     * @param userId 用户 ID
     * @return 路由树
     */
    List<RouteResp> buildRouteTree(Long userId);

    /**
     * 发送安全消息
     * 
     * @param user 用户信息
     */
    void sendSecurityMsg(UserDO user);
}
