package com.zkc.async;

import java.util.List;

/**
 * Created by zkc on 17/7/22.
 */
public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
