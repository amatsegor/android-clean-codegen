package ua.amatlabs.cleangen.library.codegen

import ua.amatlabs.cleangen.library.annotations.FieldType
import ua.amatlabs.cleangen.library.annotations.getConverterName
import ua.amatlabs.cleangen.library.codegen.JavaTokens.CLASS
import ua.amatlabs.cleangen.library.codegen.JavaTokens.PRIVATE
import ua.amatlabs.cleangen.library.codegen.JavaTokens.PUBLIC
import ua.amatlabs.cleangen.library.codegen.JavaTokens.STATIC
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter

/**
 * Created by amatsegor on 1/5/18.
 */
class JavaConverterGenerator(private val environment: ProcessingEnvironment) {

    fun generateConverter(typeElement: TypeElement): String {
        val className = getClassName(typeElement)
        val classPackage = getClassPackage(typeElement)

        val stringBuilder = StringBuilder()
        stringBuilder.append("package $classPackage;")
                .append("\n\n")
                .append("$PUBLIC $CLASS $className {").append('\n')
                .append("\t")
                .append(generatePrivateConstructor(typeElement)).append("\n\n")
                .append(generateConvertMethod(typeElement))
                .append("\n}")

        return stringBuilder.toString()
    }

    private fun generatePrivateConstructor(typeElement: TypeElement) = "$PRIVATE ${getClassName(typeElement)}(){}"

    private fun generateConvertMethod(typeElement: TypeElement): String {
        val stringBuilder = StringBuilder()
                .append("\t$PUBLIC $STATIC ${typeElement.qualifiedName}Gen convert(${typeElement.qualifiedName} object) {")
                .append("\n\t\t")
                .append("${typeElement.qualifiedName}Gen result = new ${typeElement.qualifiedName}Gen();")

        ElementFilter.fieldsIn(environment.elementUtils.getAllMembers(typeElement))
                .forEach {
                    val resultFieldName = Utils.getTargetFieldName(it)
                    stringBuilder.append("\n\t\t")

                    val fieldConverter = findConverterForField(it)
                    if (fieldConverter.isEmpty()) {
                        stringBuilder.append("result.$resultFieldName = object.${it.simpleName};")
                    } else {
                        stringBuilder.append("result.$resultFieldName = new $fieldConverter().convert(object.${it.simpleName});")
                    }
                }

        stringBuilder.append("\n\t\t")
                .append("return result;\n")
                .append("\t}")
        return stringBuilder.toString()
    }

    private fun getClassPackage(typeElement: TypeElement) = environment.elementUtils.getPackageOf(typeElement).toString()

    private fun getClassName(typeElement: TypeElement) = "${typeElement.simpleName}Converter"

    private fun findConverterForField(variableElement: VariableElement): String {
        val fieldTypeAnnotation = variableElement.getAnnotation(FieldType::class.java) ?: return ""

        val converterClassName = getConverterName(fieldTypeAnnotation, environment.typeUtils)
        return converterClassName ?: ""
    }
}