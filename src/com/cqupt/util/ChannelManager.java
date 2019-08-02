package com.cqupt.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.nio.channels.SocketChannel;

public class ChannelManager {
    //单例
    private static ChannelManager channelManager;
    private BiMap<String, SocketChannel> ownerChannelMap = HashBiMap.create();

    private ChannelManager() {
    }

    public static synchronized ChannelManager create() {
        if (channelManager == null)
            channelManager = new ChannelManager();
        return channelManager;
    }

    public synchronized void addOwnerChannel(String id, SocketChannel client) {
        if (id == null || "".equals(id)) return;
        ownerChannelMap.put(id, client);
    }

    public synchronized SocketChannel getOwnerChannel(String id) {
        return ownerChannelMap.get(id);
    }

    public synchronized void removeChannel(SocketChannel client) {
        ownerChannelMap.inverse().remove(client);
    }
}
