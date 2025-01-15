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

package top.continew.admin.generator.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import top.continew.admin.generator.model.entity.FieldConfigDO;
import top.continew.admin.generator.model.entity.GenConfigDO;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成配置信息
 *
 * @author Charles7c
 * @since 2023/8/8 20:40
 */
@Data
@Schema(description = "代码生成配置信息")
public class GenConfigReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字段配置信息
     */
    @Valid
    @Schema(description = "字段配置信息")
    @NotEmpty(message = "字段配置不能为空")
    private List<FieldConfigDO> fieldConfigs = new ArrayList<>();

    /**
     * 生成配置信息
     */
    @Valid
    @Schema(description = "生成配置信息")
    @NotNull(message = "生成配置不能为空")
    private GenConfigDO genConfig;
}
