package hs.dcb.roacguys.listener.abstract

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class AbstractMessageListener : ListenerAdapter() {

    abstract fun execute(event: MessageReceivedEvent)

    override fun onMessageReceived(event: MessageReceivedEvent) {
        execute(event)
    }
}