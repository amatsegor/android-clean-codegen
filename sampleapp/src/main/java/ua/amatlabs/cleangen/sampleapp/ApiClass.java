package ua.amatlabs.cleangen.sampleapp;

import android.util.SparseArray;

import ua.amatlabs.cleangen.library.annotations.FieldName;
import ua.amatlabs.cleangen.library.annotations.FieldType;
import ua.amatlabs.cleangen.library.annotations.GenerateApiModel;
import ua.amatlabs.cleangen.library.annotations.MutableField;
import ua.amatlabs.cleangen.library.annotations.Skip;

/**
 * Created by amatsegor on 1/4/18.
 */

@GenerateApiModel(className = "CustomClassName")
public class ApiClass {

    @FieldName("customStringField")
    @FieldType(type = String.class, converter = IntToStringConverter.class)
    int intField;

    @MutableField
    String stringField;

    @Skip
    SparseArray<String> genericField;
}
