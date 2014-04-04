package com.twotoasters.multicolumnlistadapter.sample;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

public class MCLARobolectricTestRunner extends RobolectricTestRunner {

    public MCLARobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        System.setProperty("robolectric.logging", "stdout");
    }
}
