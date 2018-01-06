package ua.amatlabs.cleangen.library.annotations

import ua.amatlabs.cleangen.library.Converter
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.util.Types
import kotlin.reflect.KClass

/**
 * Created by amatsegor on 1/6/18.
 */
annotation class FieldType(val type: KClass<*>, val converter: KClass<out Converter<*, *>>)

fun getTypeName(fieldTypeAnnotation: FieldType?, typeUtils: Types): String? {
    return try {
        fieldTypeAnnotation?.type?.qualifiedName
    } catch (mte: MirroredTypeException) {
        (typeUtils.asElement(mte.typeMirror) as? TypeElement)?.qualifiedName?.toString() ?: ""
    } catch (npe: NullPointerException) {
        null
    }
}

fun getConverterName(fieldTypeAnnotation: FieldType?, typeUtils: Types): String? {
    return try {
        fieldTypeAnnotation?.converter?.qualifiedName
    } catch (mte: MirroredTypeException) {
        (typeUtils.asElement(mte.typeMirror) as? TypeElement)?.qualifiedName?.toString() ?: ""
    } catch (npe: NullPointerException) {
        null
    }
}