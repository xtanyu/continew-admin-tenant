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

package top.continew.admin.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.continew.admin.common.enums.DataScopeEnum;
import top.continew.starter.extension.crud.annotation.DictField;
import top.continew.admin.common.model.entity.BaseDO;

import java.io.Serial;

/**
 * 角色实体
 *
 * @author Charles7c
 * @since 2023/2/8 22:54
 */
@Data
@DictField
@TableName("sys_role")
public class RoleDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 数据权限
     */
    private DataScopeEnum dataScope;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否为系统内置数据
     */
    private Boolean isSystem;

    /**
     * 菜单选择是否父子节点关联
     */
    private Boolean menuCheckStrictly;

    /**
     * 部门选择是否父子节点关联
     */
    private Boolean deptCheckStrictly;
}
