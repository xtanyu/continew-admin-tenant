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

package top.continew.admin.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.system.mapper.*;
import top.continew.admin.system.model.entity.UserDO;
import top.continew.admin.system.service.FileService;
import top.continew.admin.system.service.TenantSysDataService;
import top.continew.starter.extension.crud.model.entity.BaseIdDO;

import java.util.List;

/**
 * @description: 多租户系统数据接口
 * @author: 小熊
 * @create: 2024-12-02 20:12
 */
@RequiredArgsConstructor
@Service
public class TenantSysDataServiceImpl implements TenantSysDataService {

    private final DeptMapper deptMapper;
    private final FileService fileService;
    private final LogMapper logMapper;
    private final MenuMapper menuMapper;
    private final MessageMapper messageMapper;
    private final MessageUserMapper messageUserMapper;
    private final NoticeMapper noticeMapper;
    private final RoleMapper roleMapper;
    private final RoleDeptMapper roleDeptMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final UserMapper userMapper;
    private final UserPasswordHistoryMapper userPasswordHistoryMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserSocialMapper userSocialMapper;

    @Override
    public void clear() {
        //所有用户退出
        List<UserDO> userDOS = userMapper.selectList(null);
        for (UserDO userDO : userDOS) {
            StpUtil.logout(userDO.getId());
        }
        //部门清除
        deptMapper.delete(null);
        //文件清除
        List<Long> fileIds = fileService.list().stream().map(BaseIdDO::getId).toList();
        if (!fileIds.isEmpty()) {
            fileService.delete(fileIds);
        }
        //日志清除
        logMapper.delete(null);
        //菜单清除
        menuMapper.delete(null);
        //消息清除
        messageMapper.delete(null);
        messageUserMapper.delete(null);
        //通知清除
        noticeMapper.delete(null);
        //角色相关数据清除
        roleMapper.delete(null);
        roleDeptMapper.delete(null);
        roleMenuMapper.delete(null);
        //用户数据清除
        userMapper.delete(null);
        userPasswordHistoryMapper.delete(null);
        userRoleMapper.delete(null);
        userSocialMapper.delete(null);
    }

}
