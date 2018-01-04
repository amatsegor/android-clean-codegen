package ua.amatlabs.cleangen.sampleapp;

import android.util.SparseArray;

import ua.amatlabs.cleangen.library.annotations.FieldName;
import ua.amatlabs.cleangen.library.annotations.GenerateApiModel;
import ua.amatlabs.cleangen.library.annotations.MutableField;

/**
 * Created by amatsegor on 1/4/18.
 */

@GenerateApiModel
public class ApiClass {
    private int intField;

    @MutableField
    private String stringField;

    @FieldName("customFieldName")
    private SparseArray<String> genericField;
}
