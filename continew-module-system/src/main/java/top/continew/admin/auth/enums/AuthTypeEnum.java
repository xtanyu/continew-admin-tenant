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

package top.continew.admin.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.continew.admin.common.constant.UiConstants;
import top.continew.starter.core.enums.BaseEnum;

/**
 * 认证类型
 *
 * @author Charles7c
 * @since 2023/12/23 13:38
 */
@Getter
@RequiredArgsConstructor
public enum AuthTypeEnum implements BaseEnum<String> {

    /**
     * 账号
     */
    ACCOUNT("account", "账号", UiConstants.COLOR_ERROR),

    /**
     * 邮箱
     */
    EMAIL("email", "邮箱", UiConstants.COLOR_PRIMARY),

    /**
     * 手机号
     */
    PHONE("phone", "手机号", UiConstants.COLOR_SUCCESS),

    /**
     * 第三方授权
     */
    SOCIAL_AUTH("socialAuth", "第三方授权", UiConstants.COLOR_DEFAULT);

    private final String value;
    private final String description;
    private final String color;
}
