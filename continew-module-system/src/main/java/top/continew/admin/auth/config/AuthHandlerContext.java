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

package top.continew.admin.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.continew.admin.auth.model.req.AuthReq;
import top.continew.admin.auth.AuthHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录类型策略上文
 *
 * @author KAI
 * @since 2024/12/20 15:16:55
 */
@Component
public class AuthHandlerContext {
    private final Map<String, AuthHandler<?, ?>> handlerMap = new HashMap<>();

    @Autowired
    public AuthHandlerContext(List<AuthHandler<?, ?>> strategies) {
        for (AuthHandler<?, ?> strategy : strategies) {
            handlerMap.put(strategy.getAuthType().getValue(), strategy);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AuthReq, R> AuthHandler<T, R> getHandler(String type) {
        AuthHandler<?, ?> strategy = handlerMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No handler found for type: " + type);
        }
        return (AuthHandler<T, R>)strategy;
    }
}
