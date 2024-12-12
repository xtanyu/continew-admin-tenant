package top.continew.admin.tenant.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.continew.starter.core.exception.BusinessException;

@Getter
@AllArgsConstructor
public enum TenantConnectTypeEnum {

    MYSQL;


    public static TenantConnectTypeEnum getByValue(Integer ordinal) {
        for (TenantConnectTypeEnum item : TenantConnectTypeEnum.values()) {
            if (item.ordinal() == ordinal) {
                return item;
            }
        }
        throw new BusinessException("未知的连接类型");
    }

}
