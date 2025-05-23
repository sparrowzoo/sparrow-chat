package com.sparrow.chat.domain.netty;

import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.protocol.LoginUser;
import com.sparrow.spring.starter.SpringContext;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserContainer {
    private static Logger logger = LoggerFactory.getLogger(UserContainer.class);
    /**
     * channel属性
     */
    public static final AttributeKey<String> USER_ID_KEY = AttributeKey.newInstance("userId");

    public static final AttributeKey<Long> LAST_ACTIVE_TIME = AttributeKey.newInstance("lastActiveTime");

    public static final AttributeKey<Long> LAST_STATUS_MONITOR_TIME = AttributeKey.newInstance("lastMonitorTime");



    private UserContainer() {
    }

    private static UserContainer userContainer = new UserContainer();

    public static UserContainer getContainer() {
        return userContainer;
    }

    private static final Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    public void refreshLastActiveTime(Channel channel) {
        channel.attr(LAST_ACTIVE_TIME).set(System.currentTimeMillis());
    }

    public void refreshLastMonitorStatusTime(Channel channel) {
        channel.attr(LAST_STATUS_MONITOR_TIME).set(System.currentTimeMillis());
    }

    public long getLastMonitorStatusTime(Channel channel) {
        Long lastMonitorTime= channel.attr(LAST_STATUS_MONITOR_TIME).get();
        if(lastMonitorTime == null){
            return 0L;
        }
        return lastMonitorTime;
    }

    public long getLastActiveTime(ChatUser chatUser) {
        Channel channel = channelMap.get(chatUser.key());
        Long lastActiveTime= channel.attr(LAST_ACTIVE_TIME).get();
        if(lastActiveTime == null){
            return 0L;
        }
        return lastActiveTime;
    }

    public ChatUser hasUser(Channel channel) {
        if (!channel.hasAttr(USER_ID_KEY)) {
            return null;
        }
        String user = channel.attr(USER_ID_KEY).get();
        if (user == null) {
            return null;
        }
        return ChatUser.parse(user);
    }

    public void online(Channel channel, LoginUser loginUser) {
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        Channel oldChannel = channelMap.get(chatUser.key());
        if (oldChannel != null) {
            oldChannel.close();
        }
        channelMap.put(chatUser.key(), channel);
        channel.attr(USER_ID_KEY).set(chatUser.key());
    }

    public Channel getChannel(ChatUser chatUser) {
        return channelMap.get(chatUser.key());
    }

    public Boolean online(ChatUser chatUser) {
        String key = chatUser.key();
        return channelMap.containsKey(key) && channelMap.get(key) != null;
    }

    public Channel offline(Channel channel) {
        Attribute<String> userId = channel.attr(USER_ID_KEY);
        return channelMap.remove(userId.get());
    }

    public List<Channel> getChannels(ChatSession chatSession, ChatUser currentUser) {
        if (chatSession.isOne2One()) {
            ChatUser oppositeUser = chatSession.getOppositeUser(currentUser);
            Channel targetChannel = this.getChannel(oppositeUser);
            return Collections.singletonList(targetChannel);
        }
        QunRepository qunRepository = SpringContext.getContext().getBean(QunRepository.class);
        String sessionKey = chatSession.getId();
        List<Long> userIds = qunRepository.getUserIdList(sessionKey);
        List<Channel> channels = new ArrayList<>(userIds.size());
        Long currentUserId = Long.parseLong(currentUser.getId());
        for (Long userId : userIds) {
            if (userId.equals(currentUserId)) {
                continue;
            }
            ChatUser chatUser = ChatUser.longUserId(userId, LoginUser.CATEGORY_REGISTER);
            Channel channel = this.getChannel(chatUser);
            if (channel != null) {
                logger.info("fetch user channel,session-key {},user-id {},channel {}", chatSession.getId(), userId, channel);
                channels.add(channel);
                continue;
            }
            logger.warn("user [{}] is offline ", chatUser);
        }
        return channels;
    }


    public Map<String, Channel> getChannelMap() {
        return channelMap;
    }
}
