package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.dao.sparrow.query.session.MessageDBQuery;
import com.sparrow.protocol.dao.DaoSupport;
import com.sparrow.chat.im.po.Message;

import java.util.List;

public interface MessageDao extends DaoSupport<Message, Long> {
  List<Message> getHistoryMessage(MessageDBQuery query);
}
