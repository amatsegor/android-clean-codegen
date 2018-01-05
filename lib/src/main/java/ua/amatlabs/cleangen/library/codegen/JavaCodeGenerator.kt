package ua.amatlabs.cleangen.library.codegen

import ua.amatlabs.cleangen.library.annotations.FieldName
import ua.amatlabs.cleangen.library.annotations.MutableField
import ua.amatlabs.cleangen.library.annotations.Skip
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.util.ElementFilter


/**
 * Created by amatsegor on 1/4/18.
 */
class JavaCodeGenerator(private val environment: ProcessingEnvironment) {

    fun generateModelCode(element: TypeElement): String {
        val classPackage = environment.elementUtils.getPackageOf(element).toString()

        val stringBuilder = StringBuilder(PACKAGE).append(" ").append(classPackage)
        stringBuilder.append(";").append("\n\n")

        val className = "${element.simpleName}Gen"

        stringBuilder.append("$PUBLIC $CLASS $className {").append('\n')

        val classElements = environment.elementUtils.getAllMembers(element)
        val classFields = ElementFilter.fieldsIn(classElements)

        classFields
                .filter { it.getAnnotation(Skip::class.java) == null }
                .forEach {
                    val fieldNameAnnotation = it.getAnnotation(FieldName::class.java)
                    val fieldName = fieldNameAnnotation?.value ?: it.simpleName

                    stringBuilder.append('\t').append(generateFieldString(it, fieldName)).append('\n')
                    stringBuilder.append(generateFieldAccessors(it, fieldName))
                }

        stringBuilder.append("}")

        return stringBuilder.toString()
    }

    private fun generateFieldString(variableElement: VariableElement, fieldName: CharSequence): String {
        val fieldClass = getFullFieldType(variableElement)

        return "$PRIVATE $fieldClass $fieldName;"
    }

    private fun generateFieldAccessors(variableElement: VariableElement, fieldName: CharSequence): String {
        val fieldClass = getFullFieldType(variableElement)

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

    private fun getFullFieldType(variableElement: VariableElement): String {
        val fieldClass = variableElement.asType()
        var fieldClassString = fieldClass.toString()

        if (fieldClass == TypeKind.DECLARED && fieldClass is DeclaredType) {
            val typeArgs = fieldClass.typeArguments
            fieldClassString = "$fieldClass<${typeArgs[0]}>"
        }
        return fieldClassString
    }

    fun generateConverter(typeElement: TypeElement): String {
        val className = typeElement.simpleName.toString() + "Converter"
        val classPackage = environment.elementUtils.getPackageOf(typeElement).toString() + ".converters"

        val stringBuilder = StringBuilder()
        stringBuilder.append("package $classPackage;")
                .append("\n\n")
                .append("$PUBLIC $CLASS $className ")
                .append("implements ua.amatlabs.cleangen.library.codegen.Converter<")
                .append(typeElement.qualifiedName).append(", ").append("${typeElement.qualifiedName}Gen")
                .append("> {").append('\n')
                .append("\t$PUBLIC ${typeElement.qualifiedName}Gen convert(${typeElement.qualifiedName} object) {")

        ElementFilter.fieldsIn(environment.elementUtils.getAllMembers(typeElement))
                .forEach {
                    stringBuilder.append("\n\t\t").append("this.${it.simpleName} = object.${it.simpleName};")
                }

        stringBuilder.append("\n\t\treturn object;").append('\n')
                .append("\t}")
                .append("\n}")

        return stringBuilder.toString()
    }

    companion object {
        const val PACKAGE = "package"

        const val CLASS = "class"
        const val INTERFACE = "interface"

        const val PUBLIC = "public"
        const val PROTECTED = "protected"
        const val PRIVATE = "private"
    }
}