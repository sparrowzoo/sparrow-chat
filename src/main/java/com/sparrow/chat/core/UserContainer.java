package com.sparrow.chat.core;

import com.sparrow.chat.protocol.Protocol;
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

    public boolean hasUser(Channel channel) {
        return (channel.hasAttr(USER_ID_KEY) || channel.attr(USER_ID_KEY).get() != null);
    }

    public void online(Channel channel, String userId) {
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

    public List<Channel> getChannels(Protocol protocol) {
        if (protocol.isOne2One()) {
            Channel targetChannel = this.getChannelByUserId(protocol.getTargetUserId() + "");
            return Collections.singletonList(targetChannel);
        }
        QunRepository qunRepository = SpringContext.getContext().getBean(QunRepository.class);
        String sessionKey = protocol.getSession();
        List<Integer> userIds = qunRepository.getUserIdList(sessionKey);
        List<Channel> channels = new ArrayList<Channel>();
        for (Integer userId : userIds) {
            Channel channel = this.getChannelByUserId(userId + "");
            if (channel != null) {
                channels.add(channel);
                continue;
            }
            logger.warn("user [{}] is offline ", userId);
        }
        return channels;
    }
}
