package kr.cosine.randombox.registry

import kr.cosine.randombox.data.GiftBoxSetting
import kr.cosine.randombox.data.Sound
import kr.hqservice.framework.global.core.component.Bean

@Bean
class SettingRegistry {
    private var giftBoxSetting: GiftBoxSetting? = null

    private var pickSound: Sound? = null

    fun findGiftBoxSetting(): GiftBoxSetting? {
        return giftBoxSetting
    }

    fun setGiftBoxSetting(giftBoxSetting: GiftBoxSetting) {
        this.giftBoxSetting = giftBoxSetting
    }

    fun findPickSound(): Sound? {
        return pickSound
    }

    fun setPickSound(pickSound: Sound) {
        this.pickSound = pickSound
    }

    fun clear() {
        giftBoxSetting = null
        pickSound = null
    }
}