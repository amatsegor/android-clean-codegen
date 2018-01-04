package ua.amatlabs.cleangen.sampleapp

import android.util.SparseArray
import ua.amatlabs.cleangen.library.annotations.GenerateApiModel

/**
 * Created by amatsegor on 1/4/18.
 */
@GenerateApiModel
class ApiKlass {
    private val intField: Int = 0
    private lateinit var stringField: String
    private val genericField: SparseArray<String> = SparseArray()
}