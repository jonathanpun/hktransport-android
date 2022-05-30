package cs.hku.hktransportandroid.Entity

import android.os.LocaleList
import java.util.*

data class MultiLanguage(
    val en:String,
    val tc:String,
    val sc:String
){
    fun getWithLocale(locales: LocaleList):String{
        return when(locales.getFirstMatch(arrayOf(Locale.TRADITIONAL_CHINESE.toLanguageTag(),Locale.SIMPLIFIED_CHINESE.toLanguageTag(), Locale.ENGLISH.toLanguageTag()))){
            Locale.TRADITIONAL_CHINESE-> tc
            Locale.SIMPLIFIED_CHINESE->sc
            else->en
        }
    }
}