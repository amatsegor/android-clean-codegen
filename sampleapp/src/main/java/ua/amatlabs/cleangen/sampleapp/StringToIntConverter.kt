package ua.amatlabs.cleangen.sampleapp

import ua.amatlabs.cleangen.library.Converter

/**
 * Created by amatsegor on 1/6/18.
 */
class StringToIntConverter : Converter<String, Int> {
    override fun convert(source: String): Int {
        return try {
            source.toInt()
        } catch (nfe: NumberFormatException) {
            return -1
        }
    }
}