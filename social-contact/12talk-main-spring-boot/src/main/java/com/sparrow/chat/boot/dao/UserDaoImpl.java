package com.sparrow.chat.boot.dao;

import com.sparrow.passport.dao.UserDAO;
import com.sparrow.passport.po.SecurityPrincipal;
import com.sparrow.passport.po.User;
import com.sparrow.protocol.dao.AggregateCriteria;
import com.sparrow.protocol.dao.DatabasePagerQuery;
import com.sparrow.protocol.dao.StatusCriteria;
import com.sparrow.protocol.dao.UniqueKeyCriteria;
import com.sparrow.protocol.enums.StatusRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDaoImpl implements UserDAO {
    @Autowired
    private User12TalkAdapterDaoImpl user12TalkAdapterDao;

    private User convert(User12Talk user12Talk) {
        if (user12Talk == null) {
            return null;
        }
        User user = new User();
        user.setUserId(user12Talk.getId());
        user.setEmail(user12Talk.getEmail());
        user.setUserName(user12Talk.getAccountCode());
        user.setPassword(user12Talk.getPassword());
        user.setNickName(user12Talk.getName());
        user.setPersonalSignature(user12Talk.getRemark());
        user.setCategory(UserCategory.valueOf(user12Talk.getType()).getId());
        user.setEnglishName(user12Talk.getEnName());
        StatusRecord statusRecord = StatusRecord.ENABLE;
        if (user12Talk.getDeleted() == 1 || user12Talk.getEnabled() == 0) {
            statusRecord = StatusRecord.DISABLE;
        }
        user.setStatus(statusRecord);
        return user;
    }

    @Override
    public void save(SecurityPrincipal securityPrincipal) {

    }

    @Override
    public void modifyAvatar(Long aLong, String s) {

    }

    @Override
    public Long insert(User user) {
        return null;
    }

    @Override
    public Integer update(User user) {
        return null;
    }

    @Override
    public Integer changeStatus(StatusCriteria statusCriteria) {
        return null;
    }

    @Override
    public Integer delete(Long aLong) {
        return null;
    }

    @Override
    public Integer batchDelete(String s) {
        return null;
    }

    @Override
    public User getEntity(Long aLong) {
        return null;
    }

    @Override
    public User getEntityByUnique(UniqueKeyCriteria uniqueKeyCriteria) {
        uniqueKeyCriteria = UniqueKeyCriteria.createUniqueCriteria(uniqueKeyCriteria.getKey(), "accountCode");
        return this.convert(this.user12TalkAdapterDao.getEntityByUnique(uniqueKeyCriteria));
    }

    @Override
    public List<User> getList() {
        return null;
    }

    @Override
    public List<User> getList(Collection<Long> collection) {
        List<User> userList = new ArrayList<>();
        List<User12Talk> user12TalkList = this.user12TalkAdapterDao.getList(collection);
        for (User12Talk user12Talk : user12TalkList) {
            userList.add(this.convert(user12Talk));
        }
        return userList;
    }

    @Override
    public Map<Long, User> getEntityMap(Collection<Long> collection) {
        Map<Long, User> userMap = new HashMap<>();
        Map<Long, User12Talk> user12TalkMap = this.user12TalkAdapterDao.getEntityMap(collection);
        for (Long id : user12TalkMap.keySet()) {
            userMap.put(id, this.convert(user12TalkMap.get(id)));
        }
        return userMap;
    }

    @Override
    public List<User> getList(DatabasePagerQuery databasePagerQuery) {
        return null;
    }

    @Override
    public Long getCountByUnique(UniqueKeyCriteria uniqueKeyCriteria) {
        return this.user12TalkAdapterDao.getCountByUnique(uniqueKeyCriteria);
    }

    @Override
    public Boolean exist(Long aLong) {
        return this.user12TalkAdapterDao.exist(aLong);
    }

    @Override
    public <X> X getFieldValueByUnique(UniqueKeyCriteria uniqueKeyCriteria) {
        return this.user12TalkAdapterDao.getFieldValueByUnique(uniqueKeyCriteria);
    }

    @Override
    public <X> X getAggregate(AggregateCriteria aggregateCriteria) {
        return null;
    }
}
