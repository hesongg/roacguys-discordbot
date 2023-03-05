package hs.dcb.roacguys.listener.abstract

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class AbstractMessageListener : ListenerAdapter() {

    abstract fun execute(event: MessageReceivedEvent)

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (isBotMessage(event)) return
        if (doReturnIsDirectMessage(event)) return
        if (isNotCommandMessage(event)) return

        execute(event)
    }

    private fun isBotMessage(event: MessageReceivedEvent): Boolean {
        return event.author.isBot
    }

    private fun doReturnIsDirectMessage(event: MessageReceivedEvent): Boolean {
        return event.isFromType(ChannelType.PRIVATE)
    }

    private fun isNotCommandMessage(event: MessageReceivedEvent): Boolean {
        val contentRaw = event.message.contentRaw
        return !contentRaw.startsWith("!")
    }

    protected fun sendMessage(event: MessageReceivedEvent, msg: String) {
        event.channel.sendMessage(msg).queue()
    }

    protected fun sendMessage(eventChannel: MessageChannelUnion, msg: String) {
        eventChannel.sendMessage(msg).queue()
    }

    protected fun sendEmbedMessage(eventChannel: MessageChannelUnion, eb: EmbedBuilder) {
        eventChannel.sendMessageEmbeds(eb.build()).queue()
    }
}