package com.flir.SoftheonExampleFlirOneApplication.Softheon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by jpennisi_admin on 2/7/2017.
 */

public class Profile {

    final private static int stringCount = 32;

    final private static int integerCount = 8;

    final private static int doubleCount = 4;

    final private static int dateCount = 4;

    @JsonProperty("Strings")
    public String[] strings = new String[stringCount];

    @JsonProperty("Integers")
    public int[] integers = new int[integerCount];

    @JsonProperty("Doubles")
    public double[] doubles = new double[doubleCount];

    @JsonProperty("Dates")
    public Date[] dates = new Date[dateCount];

    @JsonProperty("Type")
    final public int type;

    @JsonProperty("Acl")
    final public int acl = -1;

    public Profile(int type) {
        this.type = type;
    }

    protected String getString(int index) {
        if(index >= stringCount) {
            throw new IndexOutOfBoundsException(String.format("Only %s strings are allowed!", stringCount));
        }

        return strings[index];
    }

    protected void setString(int index, String value) {
        if(index >= stringCount) {
            throw new IndexOutOfBoundsException(String.format("Only %s strings are allowed!", stringCount));
        }

        strings[index] = value;
    }

    protected int getInteger(int index) {
        if (index >= integerCount) {
            throw new IndexOutOfBoundsException(String.format("Only %s strings are allowed!", integerCount));
        }

        return integers[index];
    }

    protected void setInteger(int index, int value) {
        if (index >= integerCount) {
            throw new IndexOutOfBoundsException(String.format("Only %s strings are allowed!", integerCount));
        }

        integers[index] = value;
    }

    protected double getDouble(int index) {
        if (index >= doubleCount) {
            throw new IndexOutOfBoundsException(String.format("Only %s strings are allowed!", doubleCount));
        }

        return doubles[index];
    }

    protected void setDouble(int index, double value) {
        if (index >= doubleCount) {
            throw new IndexOutOfBoundsException(String.format("Only %s strings are allowed!", doubleCount));
        }

        doubles[index] = value;
    }

    protected Date getDate(int index) {
        if (index >= dateCount) {
            throw new IndexOutOfBoundsException(String.format("Only %s strings are allowed!", dateCount));
        }

        return dates[index];
    }

    protected void setDate(int index, Date value) {
        if (index >= dateCount) {
            throw new IndexOutOfBoundsException(String.format("Only %s strings are allowed!", dateCount));
        }

        dates[index] = value;
    }
}
