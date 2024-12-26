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

package top.continew.admin.auth.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 第三方登录参数
 *
 * @author KAI
 * @since 2024/12/25 15:43
 */
@Data
@Schema(description = "第三方登录参数")
public class SocialAuthReq extends AuthReq {
    /**
     * 第三方登录平台
     */
    @NotBlank(message = "第三方登录平台不能为空")
    private String source;

    /**
     * 第三方登录code
     */
    @NotBlank(message = "第三方登录code不能为空")
    private String code;

    /**
     * 第三方登录state
     */
    @NotBlank(message = "第三方登录state不能为空")
    private String state;
}
