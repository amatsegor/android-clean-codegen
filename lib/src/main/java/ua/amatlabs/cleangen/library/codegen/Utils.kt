package ua.amatlabs.cleangen.library.codegen

import ua.amatlabs.cleangen.library.annotations.FieldName
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind

/**
 * Created by amatsegor on 1/5/18.
 */

object Utils {

    fun getFullFieldType(variableElement: VariableElement): String {
        val fieldClass = variableElement.asType()
        var fieldClassString = fieldClass.toString()

        if (fieldClass == TypeKind.DECLARED && fieldClass is DeclaredType) {
            val typeArgs = fieldClass.typeArguments
            fieldClassString = "$fieldClass<${typeArgs[0]}>"
        }
        return fieldClassString
    }

    fun getRealFieldName(variableElement: VariableElement): CharSequence {
        val fieldNameAnnotation = variableElement.getAnnotation(FieldName::class.java)
        val fieldName = fieldNameAnnotation?.value ?: variableElement.simpleName
        return fieldName
    }
}