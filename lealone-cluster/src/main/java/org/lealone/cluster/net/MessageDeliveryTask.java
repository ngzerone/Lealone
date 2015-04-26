/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.cluster.net;

import java.util.EnumSet;

import org.lealone.cluster.gms.Gossiper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MessageDeliveryTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MessageDeliveryTask.class);

    private final MessageIn message;
    private final long constructionTime;
    private final int id;

    public MessageDeliveryTask(MessageIn message, int id, long timestamp) {
        assert message != null;
        this.message = message;
        this.id = id;
        constructionTime = timestamp;
    }

    @Override
    public void run() {
        MessagingService.Verb verb = message.verb;
        if (MessagingService.DROPPABLE_VERBS.contains(verb)
                && System.currentTimeMillis() > constructionTime + message.getTimeout()) {
            MessagingService.instance().incrementDroppedMessages(verb);
            return;
        }

        IVerbHandler verbHandler = MessagingService.instance().getVerbHandler(verb);
        if (verbHandler == null) {
            if (logger.isDebugEnabled())
                logger.debug("Unknown verb {}", verb);
            return;
        }

        try {
            verbHandler.doVerb(message, id);
        } catch (Throwable t) {
            if (message.doCallbackOnFailure()) {
                MessageOut response = new MessageOut(MessagingService.Verb.INTERNAL_RESPONSE).withParameter(
                        MessagingService.FAILURE_RESPONSE_PARAM, MessagingService.ONE_BYTE);
                MessagingService.instance().sendReply(response, id, message.from);
            }

            throw t;
        }
        if (GOSSIP_VERBS.contains(message.verb))
            Gossiper.instance.setLastProcessedMessageAt(constructionTime);
    }

    EnumSet<MessagingService.Verb> GOSSIP_VERBS = EnumSet.of(MessagingService.Verb.GOSSIP_DIGEST_ACK,
            MessagingService.Verb.GOSSIP_DIGEST_ACK2, MessagingService.Verb.GOSSIP_DIGEST_SYN);
}
