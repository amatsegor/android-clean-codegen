package ua.amatlabs.cleangen.library

import ua.amatlabs.cleangen.library.annotations.GenerateApiModel
import ua.amatlabs.cleangen.library.codegen.JavaCodeGenerator
import javax.annotation.processing.*
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

    override fun init(p0: ProcessingEnvironment) {
        super.init(p0)
        environment = p0
    }

    override fun process(elements: MutableSet<out TypeElement>, env: RoundEnvironment): Boolean {
        val annotatedElements = env.getElementsAnnotatedWith(GenerateApiModel::class.java)
        val javaCodeGenerator = JavaCodeGenerator(environment)
        ElementFilter.typesIn(annotatedElements)
                .forEach {
                    val modelCode = javaCodeGenerator.generateModelCode(it)
                    println(modelCode)
                    writeModelClassFile(it, modelCode)

                    val converterCode = javaCodeGenerator.generateConverter(it)
                    writeConverterClassFile(it, converterCode)
                }

        return true
    }

    private fun writeModelClassFile(element: TypeElement, sourceString: String) {
        val classPackage = environment.elementUtils.getPackageOf(element).qualifiedName.toString() + ".model"
        val className = element.simpleName
        writeSourceFile(element, "$classPackage.${className}Gen", sourceString)
    }

    private fun writeConverterClassFile(element: TypeElement, sourceString: String) {
        val classPackage = environment.elementUtils.getPackageOf(element).qualifiedName.toString() + ".converter"
        val className = element.simpleName
        writeSourceFile(element, "$classPackage.${className}Gen", sourceString)
    }

    private fun writeSourceFile(element: TypeElement, filename: String, sourceString: String) {
        val file = environment.filer.createSourceFile(filename, element)
        file.openWriter().use {
            it.write(sourceString)
        }
    }
}