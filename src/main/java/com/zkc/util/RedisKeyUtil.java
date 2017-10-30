package com.zkc.util;

/**
 * Created by zkc on 17/7/21.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    private static String BIZ_FOLLER = "FOLLOWER";
    private static String BIZ_FOLLEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";

    public static String getLikeKey(int entityType, int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }
    public static String getFollowerKey(int entityType, int entityId){
        return BIZ_FOLLER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    public static String getFolloweeKey(int entityType, int userId){
        return BIZ_FOLLEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
    public static String getTimelineKey(int userId){
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
}
