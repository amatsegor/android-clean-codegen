package ua.amatlabs.cleangen.library

/**
 * Created by amatsegor on 1/6/18.
 */
interface Converter<Source, Target> {
    fun convert(source: Source): Target
}