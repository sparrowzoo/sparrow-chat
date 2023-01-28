package com.sparrow.chat.dao;

import com.sparrow.chat.enums.AuditStatus;
import com.sparrow.orm.type.TypeHandler;
import com.sparrow.orm.type.TypeHandlerRegistry;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Named;
import org.springframework.beans.factory.InitializingBean;

@Named
public class AuditStatusHandler implements TypeHandler<AuditStatus>, InitializingBean {

    public void setParameter(PreparedStatement ps, int i, AuditStatus parameter) throws SQLException {
        ps.setInt(i, parameter.getStatus());
    }

    public AuditStatus getResult(ResultSet rs, String columnName) throws SQLException {
        return AuditStatus.getStatus(rs.getInt(columnName));
    }

    public AuditStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
        return AuditStatus.getStatus(rs.getInt(columnIndex));
    }

    public AuditStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return AuditStatus.getStatus(cs.getInt(columnIndex));
    }

    @Override public void afterPropertiesSet() {
        TypeHandlerRegistry typeHandlerRegistry = TypeHandlerRegistry.getInstance();
        typeHandlerRegistry.register(this);
        typeHandlerRegistry.register(AuditStatus.class, AuditStatusHandler.class);
    }
}
