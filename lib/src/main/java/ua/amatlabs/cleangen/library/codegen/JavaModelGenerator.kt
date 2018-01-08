package ua.amatlabs.cleangen.library.codegen

import ua.amatlabs.cleangen.library.annotations.MutableField
import ua.amatlabs.cleangen.library.annotations.Skip
import ua.amatlabs.cleangen.library.annotations.getTargetClassSimpleName
import ua.amatlabs.cleangen.library.codegen.JavaTokens.CLASS
import ua.amatlabs.cleangen.library.codegen.JavaTokens.PACKAGE
import ua.amatlabs.cleangen.library.codegen.JavaTokens.PUBLIC
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter


/**
 * Created by amatsegor on 1/4/18.
 */
class JavaModelGenerator(private val environment: ProcessingEnvironment) {

    fun generateModelCode(element: TypeElement): String {
        val classPackage = environment.elementUtils.getPackageOf(element).toString()

        val stringBuilder = StringBuilder(PACKAGE).append(" ").append(classPackage)
        stringBuilder.append(";").append("\n\n")

        val className = getTargetClassSimpleName(element)

        stringBuilder.append("$PUBLIC $CLASS $className {").append('\n')

        val classElements = environment.elementUtils.getAllMembers(element)
        val classFields = ElementFilter.fieldsIn(classElements)

        classFields
                .filter { it.getAnnotation(Skip::class.java) == null }
                .forEach {
                    val fieldName = Utils.getTargetFieldName(it)
                    stringBuilder.append('\t').append(generateFieldString(it, fieldName))
                            .append('\n').append(generateFieldAccessors(it, fieldName))
                }

        stringBuilder.append("}")

        return stringBuilder.toString()
    }

    private fun generateFieldString(variableElement: VariableElement, fieldName: CharSequence): String {
        val fieldClass = Utils.getTargetFieldType(variableElement, environment.typeUtils)
        return "$fieldClass $fieldName;"
    }

    private fun generateFieldAccessors(variableElement: VariableElement, fieldName: CharSequence): String {
        val fieldClass = Utils.getTargetFieldType(variableElement, environment.typeUtils)

        val stringBuilder = StringBuilder()
        stringBuilder.append("\t$PUBLIC $fieldClass get${fieldName.toString().capitalize()}() {").append('\n')
        stringBuilder.append("\t\t").append("return $fieldName;").append('\n')
        stringBuilder.append("\t}\n\n")

        val mutable = variableElement.getAnnotation(MutableField::class.java) != null

        if (!mutable) return stringBuilder.toString()

        stringBuilder.append("\t$PUBLIC void ")
                .append("set${fieldName.toString().capitalize()}(@org.jetbrains.annotations.NotNull $fieldClass $fieldName) {")
                .append("\n\t\t")
                .append("this.$fieldName = $fieldName;\n")
                .append("\t}\n\n")

        return stringBuilder.toString()
    }
}