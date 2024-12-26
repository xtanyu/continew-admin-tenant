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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import top.continew.admin.auth.enums.AuthTypeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证参数基类
 *
 * @author KAI
 * @author Charles7c
 * @since 2024/12/22 15:16
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "authType", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = AccountAuthReq.class, name = "ACCOUNT"),
    @JsonSubTypes.Type(value = EmailAuthReq.class, name = "EMAIL"),
    @JsonSubTypes.Type(value = PhoneAuthReq.class, name = "PHONE"),
    @JsonSubTypes.Type(value = SocialAuthReq.class, name = "SOCIAL")})
public class AuthReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户端 ID
     */
    @Schema(description = "客户端 ID")
    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    /**
     * 认证类型
     */
    @Schema(description = "认证类型")
    @NotNull(message = "认证类型非法")
    private AuthTypeEnum authType;
}
