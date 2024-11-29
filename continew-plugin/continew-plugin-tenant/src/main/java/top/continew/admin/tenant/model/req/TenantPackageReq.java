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

package top.continew.admin.tenant.model.req;

import java.io.Serial;
import java.time.*;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import top.continew.starter.extension.crud.model.req.BaseReq;

/**
 * 创建或修改租户套餐参数
 *
 * @author 小熊
 * @since 2024/11/26 11:25
 */
@Data
@Schema(description = "创建或修改租户套餐参数")
public class TenantPackageReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 套餐名称
     */
    @Schema(description = "套餐名称")
    @NotBlank(message = "套餐名称不能为空")
    @Length(max = 64, message = "套餐名称长度不能超过 {max} 个字符")
    private String name;

    /**
     * 关联的菜单ids
     */
    @Schema(description = "关联的菜单ids")
    private Long[] menuIds;

    /**
     * 菜单选择是否父子节点关联
     */
    @Schema(description = "菜单选择是否父子节点关联")
    private Boolean menuCheckStrictly;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @NotNull(message = "状态不能为空")
    private Integer status;
}