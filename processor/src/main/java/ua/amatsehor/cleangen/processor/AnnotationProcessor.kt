package ua.amatsehor.cleangen.processor

import ua.amatlabs.cleangen.library.annotations.GenerateApiModel
import ua.amatlabs.cleangen.library.annotations.getTargetClassQualifiedName
import ua.amatlabs.cleangen.library.annotations.getTargetClassSimpleName
import ua.amatsehor.cleangen.processor.codegen.JavaConverterGenerator
import ua.amatsehor.cleangen.processor.codegen.JavaModelGenerator
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

/**
 * Created by amatsegor on 1/4/18.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("ua.amatlabs.cleangen.library.annotations.GenerateApiModel")
class AnnotationProcessor : AbstractProcessor() {

    private lateinit var environment: ProcessingEnvironment

    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        environment = processingEnvironment
    }

    override fun process(elements: MutableSet<out TypeElement>, env: RoundEnvironment): Boolean {
        val annotatedElements = env.getElementsAnnotatedWith(GenerateApiModel::class.java)

        val javaModelGenerator =
            JavaModelGenerator(environment)
        val javaConverterGenerator =
            JavaConverterGenerator(environment)
        ElementFilter.typesIn(annotatedElements)
                .forEach {
                    val modelCode = javaModelGenerator.generateModelCode(it)
                    writeModelClassFile(it, modelCode)

                    val converterCode = javaConverterGenerator.generateConverter(it)
                    writeConverterClassFile(it, converterCode)
                }

        return true
    }

    private fun writeModelClassFile(element: TypeElement, sourceString: String) {
        val classPackage = "${environment.elementUtils.getPackageOf(element).qualifiedName}.model"

        val className = getTargetClassSimpleName(element)
        writeSourceFile(element, "$classPackage.$className", sourceString)
    }

    private fun writeConverterClassFile(element: TypeElement, sourceString: String) {
        val className = getTargetClassQualifiedName(element,environment)
        writeSourceFile(element, "${className}Converter", sourceString)
    }

    private fun writeSourceFile(element: TypeElement, filename: String, sourceString: String) {
        val file = environment.filer.createSourceFile(filename, element)
        file.openWriter().use {
            it.write(sourceString)
        }
    }
}