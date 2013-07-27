package com.some;

import java.util.HashMap;

/**
 * Created by mqddb on 13-6-3.
 */
public class ContentTree {

    public final static String ROOT_ID = "0";
    public static final String VIDEO_ID = "1";
    public final static String AUDIO_ID = "2";
    public final static String IMAGE_ID = "3";
    public final static String VIDEO_PREFIX = "video-item-";
    public final static String AUDIO_PREFIX = "audio-item-";
    public final static String IMAGE_PREFIX = "image-item-";
    private static HashMap<String, ContentNode> contentMap = new HashMap<String, ContentNode>();

    private static ContentNode rootNode = createRootNode();

    protected static ContentNode createRootNode() {
        ContentNode rootNode = null;

        return rootNode;
    }


    public static ContentNode getNode(String objectID) {
        if (contentMap.containsKey(objectID)) {
            return contentMap.get(objectID);
        }
        return null;
    }

    public static ContentNode getRootNode() {
        return rootNode;
    }

    public static void addNode(String id, ContentNode node) {
        contentMap.put(id, node);
    }
}
