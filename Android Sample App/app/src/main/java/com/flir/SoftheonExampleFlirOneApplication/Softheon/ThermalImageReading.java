package com.flir.SoftheonExampleFlirOneApplication.Softheon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flir.SoftheonExampleFlirOneApplication.util.BuildExtension;

/**
 * Thermal Image Reading Folder
 */
public class ThermalImageReading extends Folder {

    public ThermalImageReading(String folderName) {
        super(0, folderName, 800800);

        this.folderName = folderName;
        this.init();
    }

    private void init() {
        this.profiles = new Profile[2];
        this.profiles[0] = new Profile(1);
        this.profiles[1] = new Profile(2);
        this.setOperatingSystem(BuildExtension.getOperatingSystem());
        this.setDevice(BuildExtension.DEVICE);
        this.setModel(BuildExtension.MODEL);
        this.setManufacture(BuildExtension.MANUFACTURER);
        this.setSerialNumber(BuildExtension.SERIAL);
    }

    public String getOperatingSystem() {
        return this.profiles[0].getString(0);
    }

    public void setOperatingSystem(String operatingSystem) {
        this.profiles[0].setString(0, operatingSystem);
    }

    public String getDevice() {
        return this.profiles[0].getString(1);
    }

    public void setDevice(String device) {
        this.profiles[0].setString(1, device);
    }

    public String getModel() {
        return this.profiles[0].getString(2);
    }

    public void setModel(String model) {
        this.profiles[0].setString(2, model);
    }

    public String getManufacture() {
        return this.profiles[0].getString(3);
    }

    public void setManufacture(String manufacture) {
        this.profiles[0].setString(3, manufacture);
    }

    public String getSerialNumber() {
        return this.profiles[0].getString(4);
    }

    public void setSerialNumber(String serialNumber) {
        this.profiles[0].setString(4, serialNumber);
    }

    public double getKelvin() {
        return this.profiles[1].getDouble(0);
    }

    public void setKelvin(double kelvin) {
        this.profiles[1].setDouble(0, kelvin);
    }

    public double getCelsius() {
        return this.profiles[1].getDouble(1);
    }

    public void setCelsius(double celsius) {
        this.profiles[1].setDouble(1, celsius);
    }

    public double getFahrenheit() {
        return this.profiles[1].getDouble(2);
    }

    public void setFahrenheit(double fahrenheit) {
        this.profiles[1].setDouble(2, fahrenheit);
    }
}
