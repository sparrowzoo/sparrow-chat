package com.sparrow.chat.dao;

import com.sparrow.chat.enums.BusinessType;
import com.sparrow.orm.type.TypeHandler;
import com.sparrow.orm.type.TypeHandlerRegistry;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Named;
import org.springframework.beans.factory.InitializingBean;

@Named
public class BusinessTypeHandler implements TypeHandler<BusinessType>, InitializingBean {

    public void setParameter(PreparedStatement ps, int i, BusinessType parameter) throws SQLException {
        ps.setInt(i, parameter.getBusinessType());
    }

    public BusinessType getResult(ResultSet rs, String columnName) throws SQLException {
        return BusinessType.getBusinessType(rs.getInt(columnName));
    }

    public BusinessType getResult(ResultSet rs, int columnIndex) throws SQLException {
        return BusinessType.getBusinessType(rs.getInt(columnIndex));
    }

    public BusinessType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return BusinessType.getBusinessType(cs.getInt(columnIndex));
    }

    @Override public void afterPropertiesSet() {
        TypeHandlerRegistry typeHandlerRegistry = TypeHandlerRegistry.getInstance();
        typeHandlerRegistry.register(this);
        typeHandlerRegistry.register(BusinessType.class, BusinessTypeHandler.class);
    }
}
