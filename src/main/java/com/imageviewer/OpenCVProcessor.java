package com.imageviewer;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * OpenCV 圖像處理器
 */
public class OpenCVProcessor {
    /**
     * 根據參數處理圖像
     */
    public static Mat processImage(Mat sourceImage, OpenCVParams params) {
        if (sourceImage == null || sourceImage.empty()) {
            return null;
        }

        Mat result = sourceImage.clone();

        try {
            // 轉換為灰階（如果需要）
            if (params.isApplyGrayscale()) {
                if (result.channels() == 3) {
                    Imgproc.cvtColor(result, result, Imgproc.COLOR_BGR2GRAY);
                }
            }

            // 亮度對比度調整
            if (params.isApplyBrightnessContrast()) {
                result.convertTo(result, -1, params.getContrast(), params.getBrightness());
            }

            // 高斯模糊
            if (params.isApplyGaussianBlur()) {
                int kernelSize = params.getGaussianBlurKernelSize();
                if (kernelSize % 2 == 0) {
                    kernelSize++; // 確保為奇數
                }
                Imgproc.GaussianBlur(result, result, 
                    new Size(kernelSize, kernelSize), 
                    params.getGaussianBlurSigmaX());
            }

            // 雙邊濾波
            if (params.isApplyBilateralFilter()) {
                Imgproc.bilateralFilter(result, result, 
                    params.getBilateralFilterD(),
                    params.getBilateralFilterSigmaColor(),
                    params.getBilateralFilterSigmaSpace());
            }

            // 形態學操作
            if (params.isApplyMorphology()) {
                int kernelSize = params.getMorphologyKernelSize();
                Mat kernel = Imgproc.getStructuringElement(
                    Imgproc.MORPH_RECT, 
                    new Size(kernelSize, kernelSize));
                
                int morphType;
                switch (params.getMorphologyType()) {
                    case 0: // ERODE
                        morphType = Imgproc.MORPH_ERODE;
                        break;
                    case 1: // DILATE
                        morphType = Imgproc.MORPH_DILATE;
                        break;
                    case 2: // OPEN
                        morphType = Imgproc.MORPH_OPEN;
                        break;
                    case 3: // CLOSE
                        morphType = Imgproc.MORPH_CLOSE;
                        break;
                    default:
                        morphType = Imgproc.MORPH_ERODE;
                }
                
                Imgproc.morphologyEx(result, result, morphType, kernel);
                kernel.release();
            }

            // 閾值處理
            if (params.isApplyThreshold()) {
                int thresholdType;
                switch (params.getThresholdType()) {
                    case 0:
                        thresholdType = Imgproc.THRESH_BINARY;
                        break;
                    case 1:
                        thresholdType = Imgproc.THRESH_BINARY_INV;
                        break;
                    case 2:
                        thresholdType = Imgproc.THRESH_TRUNC;
                        break;
                    case 3:
                        thresholdType = Imgproc.THRESH_TOZERO;
                        break;
                    case 4:
                        thresholdType = Imgproc.THRESH_TOZERO_INV;
                        break;
                    default:
                        thresholdType = Imgproc.THRESH_BINARY;
                }
                
                // 如果是彩色圖像，先轉為灰階
                if (result.channels() == 3) {
                    Mat gray = new Mat();
                    Imgproc.cvtColor(result, gray, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.threshold(gray, result, 
                        params.getThresholdValue(), 255, thresholdType);
                    gray.release();
                } else {
                    Imgproc.threshold(result, result, 
                        params.getThresholdValue(), 255, thresholdType);
                }
            }

            // Canny 邊緣檢測（必須在最後，因為會轉為單通道）
            if (params.isApplyCanny()) {
                // 如果是彩色圖像，先轉為灰階
                if (result.channels() == 3) {
                    Mat gray = new Mat();
                    Imgproc.cvtColor(result, gray, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.Canny(gray, result, 
                        params.getCannyThreshold1(), 
                        params.getCannyThreshold2());
                    gray.release();
                } else {
                    Imgproc.Canny(result, result, 
                        params.getCannyThreshold1(), 
                        params.getCannyThreshold2());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.release();
            return sourceImage.clone();
        }

        return result;
    }

    /**
     * 載入圖像
     */
    public static Mat loadImage(String imagePath) {
        return Imgcodecs.imread(imagePath);
    }

    /**
     * 將 Mat 轉換為 BufferedImage（用於顯示）
     */
    public static java.awt.image.BufferedImage matToBufferedImage(Mat mat) {
        if (mat == null || mat.empty()) {
            return null;
        }

        int type = mat.channels() == 1 ? 
            java.awt.image.BufferedImage.TYPE_BYTE_GRAY : 
            java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.get(0, 0, buffer);

        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            mat.cols(), mat.rows(), type);
        
        final byte[] targetPixels = ((java.awt.image.DataBufferByte) 
            image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);

        return image;
    }
}
