package com.sparrow.chat.repository;

import com.sparrow.chat.protocol.audit.FriendApplyParam;

public interface AuditRepository {
    Long applyFriend(Long currentUserId, FriendApplyParam friendApplyParam);
}
