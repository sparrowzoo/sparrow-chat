package com.sparrow.chat.core;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class SessionContainer {

    private static ConcurrentHashMap<String, int[]> sessions = new ConcurrentHashMap<String, int[]>();

    public static void bind(Integer currentId, Integer targetId) {
        int[] sessionArray = new int[] {currentId, targetId};
        Arrays.sort(sessionArray);
        String sessionKey = sessionArray[0] + "-" + sessionArray[1];
        sessions.put(sessionKey, sessionArray);
    }

    public static int[] getUserIds(String sessionKey) {
        return sessions.get(sessionKey);
    }
}
