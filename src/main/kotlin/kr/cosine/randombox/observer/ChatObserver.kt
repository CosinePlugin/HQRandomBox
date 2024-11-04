package kr.cosine.randombox.observer

import org.bukkit.event.player.AsyncPlayerChatEvent

interface ChatObserver {
    fun onChat(event: AsyncPlayerChatEvent)
}