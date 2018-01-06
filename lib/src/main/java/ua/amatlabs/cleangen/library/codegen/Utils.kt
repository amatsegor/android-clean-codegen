package ua.amatlabs.cleangen.library.codegen

import ua.amatlabs.cleangen.library.annotations.FieldName
import ua.amatlabs.cleangen.library.annotations.FieldType
import ua.amatlabs.cleangen.library.annotations.getTypeName
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Types

/**
 * Created by amatsegor on 1/5/18.
 */

object Utils {

    private fun getFullFieldType(variableElement: VariableElement): String {
        val fieldClass = variableElement.asType()
        var fieldClassString = fieldClass.toString()

        if (fieldClass == TypeKind.DECLARED && fieldClass is DeclaredType) {
            val typeArgs = fieldClass.typeArguments
            fieldClassString = "$fieldClass<${typeArgs[0]}>"
        }

        return fieldClassString
    }

    fun getTargetFieldName(variableElement: VariableElement): String {
        val fieldNameAnnotation = variableElement.getAnnotation(FieldName::class.java)
        return fieldNameAnnotation?.value ?: variableElement.simpleName.toString()
    }

    fun getTargetFieldType(variableElement: VariableElement, typeUtils: Types): String {
        val fieldTypeAnnotation = variableElement.getAnnotation(FieldType::class.java)
        val annotationName = getTypeName(fieldTypeAnnotation, typeUtils)
        return annotationName ?: getFullFieldType(variableElement)
    }

}