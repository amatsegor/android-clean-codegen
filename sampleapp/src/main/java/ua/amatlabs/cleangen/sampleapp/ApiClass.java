package ua.amatlabs.cleangen.sampleapp;

import android.util.SparseArray;

import ua.amatlabs.cleangen.library.annotations.FieldName;
import ua.amatlabs.cleangen.library.annotations.FieldType;
import ua.amatlabs.cleangen.library.annotations.GenerateApiModel;
import ua.amatlabs.cleangen.library.annotations.MutableField;

/**
 * Created by amatsegor on 1/4/18.
 */

@GenerateApiModel
public class ApiClass {

    @FieldName("customStringField")
    @FieldType(type = String.class, converter = IntToStringConverter.class)
    int intField;

    @MutableField
    String stringField;

    @FieldName("customFieldName")
    SparseArray<String> genericField;
}
