package com.sparrow.chat.domain.netty;

import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.repository.QunRepository;
import com.sparrow.spring.starter.SpringContext;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserContainer {
    private static Logger logger = LoggerFactory.getLogger(UserContainer.class);
    /**
     * channel属性
     */
    public static final AttributeKey<String> USER_ID_KEY = AttributeKey.newInstance("userId");

    private UserContainer() {
    }

    private static UserContainer userContainer = new UserContainer();

    public static UserContainer getContainer() {
        return userContainer;
    }

    public static final Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    public Integer hasUser(Channel channel) {
        if (!channel.hasAttr(USER_ID_KEY)) {
            return null;
        }
        String userId = channel.attr(USER_ID_KEY).get();
        if (userId == null) {
            return null;
        }
        return Integer.valueOf(userId);
    }

    public void online(Channel channel, String userId) {
        Channel oldChannel = channelMap.get(userId);
        if (oldChannel != null) {
            oldChannel.close();
        }
        channelMap.put(userId, channel);
        channel.attr(USER_ID_KEY).set(userId);
    }

    public Channel getChannelByUserId(String userId) {
        return channelMap.get(userId);
    }

    public Boolean online(String userId) {
        return channelMap.containsKey(userId) && channelMap.get(userId) != null;
    }

    public Channel offline(Channel channel) {
        Attribute<String> userId = channel.attr(USER_ID_KEY);
        return channelMap.remove(userId.get());
    }

    public List<Channel> getChannels(ChatSession chatSession,Integer currentUserId) {
        if (chatSession.isOne2One()) {
            Integer oppositeUser = chatSession.getOppositeUser(currentUserId);
            Channel targetChannel = this.getChannelByUserId(oppositeUser + "");
            return Collections.singletonList(targetChannel);
        }
        QunRepository qunRepository = SpringContext.getContext().getBean(QunRepository.class);
        String sessionKey = chatSession.getSessionKey();
        List<Integer> userIds = qunRepository.getUserIdList(sessionKey);
        List<Channel> channels = new ArrayList<>(userIds.size());
        for (Integer userId : userIds) {
            if (userId.equals(currentUserId)) {
                continue;
            }
            Channel channel = this.getChannelByUserId(userId + "");
            if (channel != null) {
                logger.info("fetch user channel,session-key {},user-id {},channel {}", chatSession.getSessionKey(), userId, channel);
                channels.add(channel);
                continue;
            }
            //logger.warn("user [{}] is offline ", userId);
        }
        return channels;
    }
}
