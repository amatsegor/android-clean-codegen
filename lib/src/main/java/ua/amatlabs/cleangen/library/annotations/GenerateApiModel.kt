package ua.amatlabs.cleangen.library.annotations

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

/**
 * Created by amatsegor on 1/4/18.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateApiModel(val className: String = "")

fun getTargetClassSimpleName(element: TypeElement): CharSequence {
    val annotationClassName = element.getAnnotation(GenerateApiModel::class.java).className
    return if (annotationClassName.isEmpty()) "${element.simpleName}Gen" else annotationClassName
}

fun getTargetClassQualifiedName(element: TypeElement, environment: ProcessingEnvironment): CharSequence {
    val classPackage = environment.elementUtils.getPackageOf(element).qualifiedName.toString()
    val annotationClassName = element.getAnnotation(GenerateApiModel::class.java).className
    return if (annotationClassName.isEmpty()) "${element.qualifiedName}Gen" else "$classPackage.$annotationClassName"
}