package com.imageviewer;

/**
 * OpenCV 處理參數類
 */
public class OpenCVParams {
    private boolean applyGaussianBlur = false;
    private int gaussianBlurKernelSize = 5;
    private double gaussianBlurSigmaX = 1.0;

    private boolean applyCanny = false;
    private double cannyThreshold1 = 50.0;
    private double cannyThreshold2 = 150.0;

    private boolean applyBilateralFilter = false;
    private int bilateralFilterD = 9;
    private double bilateralFilterSigmaColor = 75.0;
    private double bilateralFilterSigmaSpace = 75.0;

    private boolean applyThreshold = false;
    private double thresholdValue = 127.0;
    private int thresholdType = 0; // 0: BINARY, 1: BINARY_INV, etc.

    private boolean applyMorphology = false;
    private int morphologyType = 0; // 0: ERODE, 1: DILATE, 2: OPEN, 3: CLOSE
    private int morphologyKernelSize = 5;

    private boolean applyBrightnessContrast = false;
    private double brightness = 0.0;
    private double contrast = 1.0;

    private boolean applyGrayscale = false;

    // Getters and Setters
    public boolean isApplyGaussianBlur() {
        return applyGaussianBlur;
    }

    public void setApplyGaussianBlur(boolean applyGaussianBlur) {
        this.applyGaussianBlur = applyGaussianBlur;
    }

    public int getGaussianBlurKernelSize() {
        return gaussianBlurKernelSize;
    }

    public void setGaussianBlurKernelSize(int gaussianBlurKernelSize) {
        this.gaussianBlurKernelSize = gaussianBlurKernelSize;
    }

    public double getGaussianBlurSigmaX() {
        return gaussianBlurSigmaX;
    }

    public void setGaussianBlurSigmaX(double gaussianBlurSigmaX) {
        this.gaussianBlurSigmaX = gaussianBlurSigmaX;
    }

    public boolean isApplyCanny() {
        return applyCanny;
    }

    public void setApplyCanny(boolean applyCanny) {
        this.applyCanny = applyCanny;
    }

    public double getCannyThreshold1() {
        return cannyThreshold1;
    }

    public void setCannyThreshold1(double cannyThreshold1) {
        this.cannyThreshold1 = cannyThreshold1;
    }

    public double getCannyThreshold2() {
        return cannyThreshold2;
    }

    public void setCannyThreshold2(double cannyThreshold2) {
        this.cannyThreshold2 = cannyThreshold2;
    }

    public boolean isApplyBilateralFilter() {
        return applyBilateralFilter;
    }

    public void setApplyBilateralFilter(boolean applyBilateralFilter) {
        this.applyBilateralFilter = applyBilateralFilter;
    }

    public int getBilateralFilterD() {
        return bilateralFilterD;
    }

    public void setBilateralFilterD(int bilateralFilterD) {
        this.bilateralFilterD = bilateralFilterD;
    }

    public double getBilateralFilterSigmaColor() {
        return bilateralFilterSigmaColor;
    }

    public void setBilateralFilterSigmaColor(double bilateralFilterSigmaColor) {
        this.bilateralFilterSigmaColor = bilateralFilterSigmaColor;
    }

    public double getBilateralFilterSigmaSpace() {
        return bilateralFilterSigmaSpace;
    }

    public void setBilateralFilterSigmaSpace(double bilateralFilterSigmaSpace) {
        this.bilateralFilterSigmaSpace = bilateralFilterSigmaSpace;
    }

    public boolean isApplyThreshold() {
        return applyThreshold;
    }

    public void setApplyThreshold(boolean applyThreshold) {
        this.applyThreshold = applyThreshold;
    }

    public double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public int getThresholdType() {
        return thresholdType;
    }

    public void setThresholdType(int thresholdType) {
        this.thresholdType = thresholdType;
    }

    public boolean isApplyMorphology() {
        return applyMorphology;
    }

    public void setApplyMorphology(boolean applyMorphology) {
        this.applyMorphology = applyMorphology;
    }

    public int getMorphologyType() {
        return morphologyType;
    }

    public void setMorphologyType(int morphologyType) {
        this.morphologyType = morphologyType;
    }

    public int getMorphologyKernelSize() {
        return morphologyKernelSize;
    }

    public void setMorphologyKernelSize(int morphologyKernelSize) {
        this.morphologyKernelSize = morphologyKernelSize;
    }

    public boolean isApplyBrightnessContrast() {
        return applyBrightnessContrast;
    }

    public void setApplyBrightnessContrast(boolean applyBrightnessContrast) {
        this.applyBrightnessContrast = applyBrightnessContrast;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public double getContrast() {
        return contrast;
    }

    public void setContrast(double contrast) {
        this.contrast = contrast;
    }

    public boolean isApplyGrayscale() {
        return applyGrayscale;
    }

    public void setApplyGrayscale(boolean applyGrayscale) {
        this.applyGrayscale = applyGrayscale;
    }
}
