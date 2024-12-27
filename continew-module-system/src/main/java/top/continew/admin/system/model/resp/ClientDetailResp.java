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

package top.continew.admin.system.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.common.base.BaseDetailResp;
import top.continew.admin.common.enums.DisEnableStatusEnum;

import java.io.Serial;
import java.util.List;

/**
 * 客户端详情信息
 *
 * @author KAI
 * @since 2024/12/03 16:04
 */
@Data
@Schema(description = "客户端详情信息")
public class ClientDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    @Schema(description = "客户端ID")
    private String clientId;

    /**
     * 客户端Key
     */
    @Schema(description = "客户端Key")
    private String clientKey;

    /**
     * 客户端秘钥
     */
    @Schema(description = "客户端秘钥")
    private String clientSecret;

    /**
     * 登录类型
     */
    @Schema(description = "登录类型")
    private List<String> authType;

    /**
     * 客户端类型
     */
    @Schema(description = "客户端类型")
    private String clientType;

    /**
     * Token最低活跃频率（-1为不限制）
     */
    @Schema(description = "Token最低活跃频率（-1为不限制）")
    private Integer activeTimeout;

    /**
     * Token有效期（默认30天，单位：秒）
     */
    @Schema(description = "Token有效期（默认30天，单位：秒）")
    private Integer timeout;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "1")
    private DisEnableStatusEnum status;
}