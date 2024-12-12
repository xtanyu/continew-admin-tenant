package top.continew.admin.tenant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.continew.admin.tenant.service.TenantService;
import top.continew.starter.extension.tenant.config.TenantDataSource;
import top.continew.starter.extension.tenant.config.TenantDataSourceProvider;

/**
 * @description: 租户数据源提供者实现
 * @author: 小熊
 * @create: 2024-12-12 15:35
 */
@Service
@RequiredArgsConstructor
public class TenantDataSourceProviderImpl implements TenantDataSourceProvider {

    private final TenantService tenantService;

    @Override
    public TenantDataSource getByTenantId(String tenantId) {
        return null;
    }

    @Override
    public boolean isTenantIsolatedDataSource(String tenantId) {
        return false;
    }

}
