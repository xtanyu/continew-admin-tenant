package top.continew.admin.tenant.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.continew.starter.extension.crud.model.entity.BaseDO;

import java.io.Serial;

/**
 * 租户数据连接实体
 *
 * @author 小熊
 * @since 2024/12/12 19:13
 */
@Data
@TableName("sys_tenant_db_connect")
public class TenantDbConnectDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 连接名称
     */
    private String connectName;

    /**
     * 连接类型
     */
    private Integer type;

    /**
     * 连接主机地址
     */
    private String host;

    /**
     * 连接端口
     */
    private Integer port;

    /**
     * 连接用户名
     */
    private String username;

    /**
     * 连接密码
     */
    private String password;
}