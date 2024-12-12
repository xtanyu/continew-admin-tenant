package top.continew.admin.tenant.model.query;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import top.continew.starter.data.core.annotation.Query;
import top.continew.starter.data.core.enums.QueryType;

/**
 * 租户数据连接查询条件
 *
 * @author 小熊
 * @since 2024/12/12 19:13
 */
@Data
@Schema(description = "租户数据连接查询条件")
public class TenantDbConnectQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 连接名称
     */
    @Schema(description = "连接名称")
    @Query(type = QueryType.EQ)
    private String connectName;
}