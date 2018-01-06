package ua.amatlabs.cleangen.sampleapp

import ua.amatlabs.cleangen.library.Converter

/**
 * Created by amatsegor on 1/6/18.
 */
class IntToStringConverter : Converter<Int, String> {
    override fun convert(source: Int): String = source.toString()
}