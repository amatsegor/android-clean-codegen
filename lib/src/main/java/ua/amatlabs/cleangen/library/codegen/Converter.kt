package ua.amatlabs.cleangen.library.codegen

/**
 * Created by amatsegor on 1/4/18.
 */
interface Converter<Source, Result> {
    fun convert(source: Source): Result
}