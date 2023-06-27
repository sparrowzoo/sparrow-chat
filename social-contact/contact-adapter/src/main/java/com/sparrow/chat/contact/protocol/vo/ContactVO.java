package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;

import java.util.List;

public class ContactVO implements VO {

    public ContactVO(List<QunVO> quns, List<UserVO> users) {
        this.quns = quns;
        this.users = users;
    }

    private List<QunVO> quns;
    private List<UserVO> users;

    public List<QunVO> getQuns() {
        return quns;
    }

    public void setQuns(List<QunVO> quns) {
        this.quns = quns;
    }

    public List<UserVO> getUsers() {
        return users;
    }

    public void setUsers(List<UserVO> users) {
        this.users = users;
    }
}
