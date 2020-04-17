package com.podo.helloprice.crawl.agent.job;

import java.util.ArrayList;
import java.util.List;

public class CrawlProductJobStore{

    private final List<NotifyEvent> notifyEvents = new ArrayList<>();

    public void addNotifyEvent(NotifyEvent notifyEvent){
        this.notifyEvents.add(notifyEvent);
    }

    public List<NotifyEvent> getNotifyEvents() {
        return new ArrayList<>(notifyEvents);
    }
}
