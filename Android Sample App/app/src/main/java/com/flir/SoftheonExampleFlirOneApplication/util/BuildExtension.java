package com.flir.SoftheonExampleFlirOneApplication.util;

import android.os.Build;

import java.lang.reflect.Field;

public class BuildExtension extends android.os.Build {

    public static String getOperatingSystem() {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(VERSION.RELEASE);

        Field[] fields = VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }

        return builder.toString();
    }
}
