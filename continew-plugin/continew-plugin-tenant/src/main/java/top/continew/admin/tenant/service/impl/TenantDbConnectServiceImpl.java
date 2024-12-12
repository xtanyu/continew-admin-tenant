package top.continew.admin.tenant.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.tenant.mapper.TenantDbConnectMapper;
import top.continew.admin.tenant.model.entity.TenantDbConnectDO;
import top.continew.admin.tenant.model.enums.TenantConnectTypeEnum;
import top.continew.admin.tenant.model.query.TenantDbConnectQuery;
import top.continew.admin.tenant.model.req.TenantDbConnectReq;
import top.continew.admin.tenant.model.resp.TenantDbConnectDetailResp;
import top.continew.admin.tenant.model.resp.TenantDbConnectResp;
import top.continew.admin.tenant.service.TenantDbConnectService;
import top.continew.starter.core.exception.BusinessException;
import top.continew.starter.core.validation.CheckUtils;
import top.continew.starter.extension.crud.service.BaseServiceImpl;

import javax.sql.DataSource;

/**
 * 租户数据连接业务实现
 *
 * @author 小熊
 * @since 2024/12/12 19:13
 */
@Service
@RequiredArgsConstructor
public class TenantDbConnectServiceImpl extends BaseServiceImpl<TenantDbConnectMapper, TenantDbConnectDO, TenantDbConnectResp, TenantDbConnectDetailResp, TenantDbConnectQuery, TenantDbConnectReq> implements TenantDbConnectService {

    @Override
    protected void beforeAdd(TenantDbConnectReq req) {
        if (req.getType().equals(TenantConnectTypeEnum.MYSQL)) {
            checkMysqlConnect(req);
            checkRepeat(req, null);
        }
    }

    /**
     * 验证mysql连接
     */
    private void checkMysqlConnect(TenantDbConnectReq req) {
        try {
            HikariConfig configuration = new HikariConfig();
            configuration.setJdbcUrl(StrUtil.format("jdbc:mysql://{}:{}", req.getHost(), req.getPort()));
            configuration.setDriverClassName("com.mysql.cj.jdbc.Driver");
            configuration.setUsername(req.getUsername());
            configuration.setPassword(req.getPassword());
            DataSource dataSource = new HikariDataSource(configuration);
            dataSource.getConnection();
        } catch (Exception e) {
            throw new BusinessException("数据库连接失败,请基础配置信息");
        }
    }

    /**
     * 验证重复数据
     */
    private void checkRepeat(TenantDbConnectReq req, Long id) {
        CheckUtils.throwIf(baseMapper.exists(Wrappers.lambdaQuery(TenantDbConnectDO.class)
                .eq(TenantDbConnectDO::getHost, req.getHost())
                .eq(TenantDbConnectDO::getPort, req.getPort())
                .eq(TenantDbConnectDO::getUsername, req.getUsername())
                .ne(id != null, TenantDbConnectDO::getId, id)
        ), "数据库连接已存在");
    }

    @Override
    protected void beforeUpdate(TenantDbConnectReq req, Long id) {
        if (req.getType().equals(TenantConnectTypeEnum.MYSQL)) {
            checkMysqlConnect(req);
            checkRepeat(req, id);
        }
    }
}