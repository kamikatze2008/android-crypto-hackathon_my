package com.bux.crypto.domain;

import com.bux.crypto.internal.core.websocket.Message;
import com.bux.crypto.internal.websocket.Subscribable;

import java.util.HashSet;
import java.util.Set;

public class SubscribeMessage implements Message {
    private String[] subscribeTo;
    private String[] unsubscribeFrom;

    private SubscribeMessage(String[] subscribeTo, String[] unsubscribeFrom) {
        this.subscribeTo = subscribeTo;
        this.unsubscribeFrom = unsubscribeFrom;
    }

    public String[] getSubscribeTo() {
        return subscribeTo;
    }

    public String[] getUnsubscribeFrom() {
        return unsubscribeFrom;
    }

    public static class Builder {
        private Set<String> subscribeTo = new HashSet<>();
        private Set<String> unsubscribeFrom = new HashSet<>();

        public Builder addSubscription(Subscribable subscription) {
            String subscriptionId = subscription.getSubscriptionId();
            if (unsubscribeFrom.contains(subscriptionId)) {
                unsubscribeFrom.remove(subscriptionId);
            }
            subscribeTo.add(subscriptionId);
            return this;
        }

        public Builder removeSubscription(Subscribable subscription) {
            String subscriptionId = subscription.getSubscriptionId();
            if (subscribeTo.contains(subscriptionId)) {
                subscribeTo.remove(subscriptionId);
            }
            unsubscribeFrom.add(subscriptionId);
            return this;
        }

        public SubscribeMessage build() {
            return new SubscribeMessage(subscribeTo.toArray(new String[subscribeTo.size()]),
                                        unsubscribeFrom.toArray(new String[unsubscribeFrom.size()]));
        }
    }

}
