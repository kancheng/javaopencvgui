package com.imageviewer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 圖像檢視器 GUI 主界面
 */
public class ImageViewerGUI extends JFrame {
    private JTextField directoryPathField;
    private JList<String> imageList;
    private DefaultListModel<String> listModel;
    private JLabel imagePreviewLabel;
    private JTextArea imageInfoArea;
    private JTextArea statisticsArea;
    
    private List<File> currentImageFiles;
    private List<ImageInfo> currentImageInfos;
    private int currentImageIndex = -1;
    private Mat currentOriginalImage;
    private Mat currentProcessedImage;
    private OpenCVParams currentParams;

    // OpenCV 參數控制組件
    private JCheckBox grayscaleCheckBox;
    private JCheckBox gaussianBlurCheckBox;
    private JSpinner gaussianBlurKernelSpinner;
    private JSpinner gaussianBlurSigmaSpinner;
    private JCheckBox cannyCheckBox;
    private JSpinner cannyThreshold1Spinner;
    private JSpinner cannyThreshold2Spinner;
    private JCheckBox bilateralFilterCheckBox;
    private JSpinner bilateralFilterDSpinner;
    private JSpinner bilateralFilterSigmaColorSpinner;
    private JSpinner bilateralFilterSigmaSpaceSpinner;
    private JCheckBox thresholdCheckBox;
    private JSpinner thresholdValueSpinner;
    private JComboBox<String> thresholdTypeCombo;
    private JCheckBox morphologyCheckBox;
    private JComboBox<String> morphologyTypeCombo;
    private JSpinner morphologyKernelSpinner;
    private JCheckBox brightnessContrastCheckBox;
    private JSpinner brightnessSpinner;
    private JSpinner contrastSpinner;

    public ImageViewerGUI() {
        initializeOpenCV();
        initializeGUI();
        currentParams = new OpenCVParams();
    }

    /**
     * 初始化 OpenCV
     */
    private void initializeOpenCV() {
        try {
            OpenCV.loadLocally();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "無法載入 OpenCV 庫: " + e.getMessage(), 
                "錯誤", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 初始化 GUI 界面
     */
    private void initializeGUI() {
        setTitle("OpenCV 圖像檢視器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 頂部：目錄選擇
        JPanel topPanel = createDirectoryPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 中間：左側圖像列表，中間預覽，右側資訊
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        
        // 左側：圖像列表
        JPanel leftPanel = createImageListPanel();
        centerPanel.add(leftPanel, BorderLayout.WEST);

        // 中間：圖像預覽
        JPanel previewPanel = createPreviewPanel();
        centerPanel.add(previewPanel, BorderLayout.CENTER);

        // 右側：資訊和參數
        JPanel rightPanel = createInfoAndParamsPanel();
        centerPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 底部：按鈕
        JPanel bottomPanel = createButtonPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * 創建目錄選擇面板
     */
    private JPanel createDirectoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder("目錄選擇"));

        directoryPathField = new JTextField();
        directoryPathField.setEditable(false);
        panel.add(directoryPathField, BorderLayout.CENTER);

        JButton browseButton = new JButton("瀏覽目錄");
        browseButton.addActionListener(e -> browseDirectory());
        panel.add(browseButton, BorderLayout.EAST);

        return panel;
    }

    /**
     * 創建圖像列表面板
     */
    private JPanel createImageListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("圖像列表"));
        panel.setPreferredSize(new Dimension(250, 0));

        listModel = new DefaultListModel<>();
        imageList = new JList<>(listModel);
        imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        imageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = imageList.getSelectedIndex();
                if (index >= 0 && currentImageFiles != null && index < currentImageFiles.size()) {
                    loadImage(index);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(imageList);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 創建預覽面板
     */
    private JPanel createPreviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("圖像預覽"));

        imagePreviewLabel = new JLabel("請選擇目錄和圖像", JLabel.CENTER);
        imagePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreviewLabel.setVerticalAlignment(JLabel.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(600, 400));

        JScrollPane scrollPane = new JScrollPane(imagePreviewLabel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 創建資訊和參數面板
     */
    private JPanel createInfoAndParamsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 0));

        // 圖像資訊
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(new TitledBorder("圖像資訊"));
        imageInfoArea = new JTextArea(8, 30);
        imageInfoArea.setEditable(false);
        imageInfoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        infoPanel.add(new JScrollPane(imageInfoArea), BorderLayout.CENTER);

        // 統計資訊
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(new TitledBorder("目錄統計"));
        statisticsArea = new JTextArea(6, 30);
        statisticsArea.setEditable(false);
        statisticsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statsPanel.add(new JScrollPane(statisticsArea), BorderLayout.CENTER);

        // OpenCV 參數面板
        JScrollPane paramsPanel = createOpenCVParamsPanel();

        // 使用 TabbedPane 組織
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("圖像資訊", infoPanel);
        tabbedPane.addTab("目錄統計", statsPanel);
        tabbedPane.addTab("OpenCV 參數", paramsPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 創建 OpenCV 參數控制面板
     */
    private JScrollPane createOpenCVParamsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 灰階
        grayscaleCheckBox = new JCheckBox("轉換為灰階");
        grayscaleCheckBox.addActionListener(e -> updatePreview());
        panel.add(grayscaleCheckBox);
        panel.add(Box.createVerticalStrut(5));

        // 亮度對比度
        brightnessContrastCheckBox = new JCheckBox("亮度/對比度調整");
        brightnessContrastCheckBox.addActionListener(e -> updatePreview());
        panel.add(brightnessContrastCheckBox);
        
        JPanel bcPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bcPanel.add(new JLabel("亮度:"));
        brightnessSpinner = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 1.0));
        brightnessSpinner.addChangeListener(e -> updatePreview());
        bcPanel.add(brightnessSpinner);
        bcPanel.add(new JLabel("對比度:"));
        contrastSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 3.0, 0.1));
        contrastSpinner.addChangeListener(e -> updatePreview());
        bcPanel.add(contrastSpinner);
        panel.add(bcPanel);
        panel.add(Box.createVerticalStrut(5));

        // 高斯模糊
        gaussianBlurCheckBox = new JCheckBox("高斯模糊");
        gaussianBlurCheckBox.addActionListener(e -> updatePreview());
        panel.add(gaussianBlurCheckBox);
        
        JPanel gbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gbPanel.add(new JLabel("核大小:"));
        gaussianBlurKernelSpinner = new JSpinner(new SpinnerNumberModel(5, 3, 31, 2));
        gaussianBlurKernelSpinner.addChangeListener(e -> updatePreview());
        gbPanel.add(gaussianBlurKernelSpinner);
        gbPanel.add(new JLabel("Sigma:"));
        gaussianBlurSigmaSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
        gaussianBlurSigmaSpinner.addChangeListener(e -> updatePreview());
        gbPanel.add(gaussianBlurSigmaSpinner);
        panel.add(gbPanel);
        panel.add(Box.createVerticalStrut(5));

        // 雙邊濾波
        bilateralFilterCheckBox = new JCheckBox("雙邊濾波");
        bilateralFilterCheckBox.addActionListener(e -> updatePreview());
        panel.add(bilateralFilterCheckBox);
        
        JPanel bfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bfPanel.add(new JLabel("D:"));
        bilateralFilterDSpinner = new JSpinner(new SpinnerNumberModel(9, 1, 15, 1));
        bilateralFilterDSpinner.addChangeListener(e -> updatePreview());
        bfPanel.add(bilateralFilterDSpinner);
        bfPanel.add(new JLabel("Sigma Color:"));
        bilateralFilterSigmaColorSpinner = new JSpinner(new SpinnerNumberModel(75.0, 1.0, 200.0, 1.0));
        bilateralFilterSigmaColorSpinner.addChangeListener(e -> updatePreview());
        bfPanel.add(bilateralFilterSigmaColorSpinner);
        bfPanel.add(new JLabel("Sigma Space:"));
        bilateralFilterSigmaSpaceSpinner = new JSpinner(new SpinnerNumberModel(75.0, 1.0, 200.0, 1.0));
        bilateralFilterSigmaSpaceSpinner.addChangeListener(e -> updatePreview());
        bfPanel.add(bilateralFilterSigmaSpaceSpinner);
        panel.add(bfPanel);
        panel.add(Box.createVerticalStrut(5));

        // 形態學操作
        morphologyCheckBox = new JCheckBox("形態學操作");
        morphologyCheckBox.addActionListener(e -> updatePreview());
        panel.add(morphologyCheckBox);
        
        JPanel morphPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        morphPanel.add(new JLabel("類型:"));
        morphologyTypeCombo = new JComboBox<>(new String[]{"腐蝕", "膨脹", "開運算", "閉運算"});
        morphologyTypeCombo.addActionListener(e -> updatePreview());
        morphPanel.add(morphologyTypeCombo);
        morphPanel.add(new JLabel("核大小:"));
        morphologyKernelSpinner = new JSpinner(new SpinnerNumberModel(5, 3, 31, 2));
        morphologyKernelSpinner.addChangeListener(e -> updatePreview());
        morphPanel.add(morphologyKernelSpinner);
        panel.add(morphPanel);
        panel.add(Box.createVerticalStrut(5));

        // 閾值
        thresholdCheckBox = new JCheckBox("閾值處理");
        thresholdCheckBox.addActionListener(e -> updatePreview());
        panel.add(thresholdCheckBox);
        
        JPanel threshPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        threshPanel.add(new JLabel("值:"));
        thresholdValueSpinner = new JSpinner(new SpinnerNumberModel(127.0, 0.0, 255.0, 1.0));
        thresholdValueSpinner.addChangeListener(e -> updatePreview());
        threshPanel.add(thresholdValueSpinner);
        threshPanel.add(new JLabel("類型:"));
        thresholdTypeCombo = new JComboBox<>(new String[]{"BINARY", "BINARY_INV", "TRUNC", "TOZERO", "TOZERO_INV"});
        thresholdTypeCombo.addActionListener(e -> updatePreview());
        threshPanel.add(thresholdTypeCombo);
        panel.add(threshPanel);
        panel.add(Box.createVerticalStrut(5));

        // Canny 邊緣檢測
        cannyCheckBox = new JCheckBox("Canny 邊緣檢測");
        cannyCheckBox.addActionListener(e -> updatePreview());
        panel.add(cannyCheckBox);
        
        JPanel cannyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cannyPanel.add(new JLabel("閾值1:"));
        cannyThreshold1Spinner = new JSpinner(new SpinnerNumberModel(50.0, 0.0, 255.0, 1.0));
        cannyThreshold1Spinner.addChangeListener(e -> updatePreview());
        cannyPanel.add(cannyThreshold1Spinner);
        cannyPanel.add(new JLabel("閾值2:"));
        cannyThreshold2Spinner = new JSpinner(new SpinnerNumberModel(150.0, 0.0, 255.0, 1.0));
        cannyThreshold2Spinner.addChangeListener(e -> updatePreview());
        cannyPanel.add(cannyThreshold2Spinner);
        panel.add(cannyPanel);

        panel.add(Box.createVerticalGlue());

        return new JScrollPane(panel);
    }

    /**
     * 創建按鈕面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton prevButton = new JButton("上一張");
        prevButton.addActionListener(e -> navigateImage(-1));
        panel.add(prevButton);

        JButton nextButton = new JButton("下一張");
        nextButton.addActionListener(e -> navigateImage(1));
        panel.add(nextButton);

        JButton originalButton = new JButton("顯示原圖");
        originalButton.addActionListener(e -> showOriginalImage());
        panel.add(originalButton);

        JButton processedButton = new JButton("顯示處理後");
        processedButton.addActionListener(e -> showProcessedImage());
        panel.add(processedButton);

        JButton exportJsonButton = new JButton("導出 JSON 參數");
        exportJsonButton.addActionListener(e -> exportParamsToJson());
        panel.add(exportJsonButton);

        return panel;
    }

    /**
     * 瀏覽目錄
     */
    private void browseDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("選擇圖像目錄");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            String directoryPath = selectedDirectory.getAbsolutePath();
            directoryPathField.setText(directoryPath);
            loadDirectory(directoryPath);
        }
    }

    /**
     * 載入目錄
     */
    private void loadDirectory(String directoryPath) {
        listModel.clear();
        currentImageFiles = ImageAnalyzer.getImageFiles(directoryPath);
        currentImageInfos = ImageAnalyzer.analyzeDirectory(directoryPath);

        for (File file : currentImageFiles) {
            listModel.addElement(file.getName());
        }

        // 顯示統計資訊
        ImageAnalyzer.DirectoryStatistics stats = ImageAnalyzer.getDirectoryStatistics(directoryPath);
        statisticsArea.setText(stats.toString());

        // 如果有圖像，載入第一張
        if (!currentImageFiles.isEmpty()) {
            imageList.setSelectedIndex(0);
            loadImage(0);
        } else {
            imagePreviewLabel.setText("目錄中沒有找到圖像檔案");
            imageInfoArea.setText("");
        }
    }

    /**
     * 載入圖像
     */
    private void loadImage(int index) {
        if (currentImageFiles == null || index < 0 || index >= currentImageFiles.size()) {
            return;
        }

        currentImageIndex = index;
        File imageFile = currentImageFiles.get(index);

        // 載入原圖
        if (currentOriginalImage != null) {
            currentOriginalImage.release();
        }
        currentOriginalImage = OpenCVProcessor.loadImage(imageFile.getAbsolutePath());

        if (currentOriginalImage == null || currentOriginalImage.empty()) {
            imagePreviewLabel.setText("無法載入圖像");
            return;
        }

        // 顯示圖像資訊
        if (currentImageInfos != null && index < currentImageInfos.size()) {
            ImageInfo info = currentImageInfos.get(index);
            imageInfoArea.setText(info.toString());
        }

        // 更新預覽
        updatePreview();
    }

    /**
     * 更新預覽（應用 OpenCV 參數）
     */
    private void updatePreview() {
        if (currentOriginalImage == null || currentOriginalImage.empty()) {
            return;
        }

        // 更新參數
        updateParamsFromUI();

        // 處理圖像
        if (currentProcessedImage != null) {
            currentProcessedImage.release();
        }
        currentProcessedImage = OpenCVProcessor.processImage(currentOriginalImage, currentParams);

        // 顯示處理後的圖像
        showProcessedImage();
    }

    /**
     * 從 UI 更新參數
     */
    private void updateParamsFromUI() {
        currentParams.setApplyGrayscale(grayscaleCheckBox.isSelected());
        currentParams.setApplyBrightnessContrast(brightnessContrastCheckBox.isSelected());
        currentParams.setBrightness(((Number) brightnessSpinner.getValue()).doubleValue());
        currentParams.setContrast(((Number) contrastSpinner.getValue()).doubleValue());
        currentParams.setApplyGaussianBlur(gaussianBlurCheckBox.isSelected());
        currentParams.setGaussianBlurKernelSize(((Number) gaussianBlurKernelSpinner.getValue()).intValue());
        currentParams.setGaussianBlurSigmaX(((Number) gaussianBlurSigmaSpinner.getValue()).doubleValue());
        currentParams.setApplyBilateralFilter(bilateralFilterCheckBox.isSelected());
        currentParams.setBilateralFilterD(((Number) bilateralFilterDSpinner.getValue()).intValue());
        currentParams.setBilateralFilterSigmaColor(((Number) bilateralFilterSigmaColorSpinner.getValue()).doubleValue());
        currentParams.setBilateralFilterSigmaSpace(((Number) bilateralFilterSigmaSpaceSpinner.getValue()).doubleValue());
        currentParams.setApplyMorphology(morphologyCheckBox.isSelected());
        currentParams.setMorphologyType(morphologyTypeCombo.getSelectedIndex());
        currentParams.setMorphologyKernelSize(((Number) morphologyKernelSpinner.getValue()).intValue());
        currentParams.setApplyThreshold(thresholdCheckBox.isSelected());
        currentParams.setThresholdValue(((Number) thresholdValueSpinner.getValue()).doubleValue());
        currentParams.setThresholdType(thresholdTypeCombo.getSelectedIndex());
        currentParams.setApplyCanny(cannyCheckBox.isSelected());
        currentParams.setCannyThreshold1(((Number) cannyThreshold1Spinner.getValue()).doubleValue());
        currentParams.setCannyThreshold2(((Number) cannyThreshold2Spinner.getValue()).doubleValue());
    }

    /**
     * 顯示原圖
     */
    private void showOriginalImage() {
        if (currentOriginalImage != null && !currentOriginalImage.empty()) {
            BufferedImage bufferedImage = OpenCVProcessor.matToBufferedImage(currentOriginalImage);
            if (bufferedImage != null) {
                ImageIcon icon = new ImageIcon(scaleImage(bufferedImage, 600, 400));
                imagePreviewLabel.setIcon(icon);
                imagePreviewLabel.setText("");
            }
        }
    }

    /**
     * 顯示處理後的圖像
     */
    private void showProcessedImage() {
        if (currentProcessedImage != null && !currentProcessedImage.empty()) {
            BufferedImage bufferedImage = OpenCVProcessor.matToBufferedImage(currentProcessedImage);
            if (bufferedImage != null) {
                ImageIcon icon = new ImageIcon(scaleImage(bufferedImage, 600, 400));
                imagePreviewLabel.setIcon(icon);
                imagePreviewLabel.setText("");
            }
        } else if (currentOriginalImage != null && !currentOriginalImage.empty()) {
            showOriginalImage();
        }
    }

    /**
     * 縮放圖像以適應顯示區域
     */
    private BufferedImage scaleImage(BufferedImage original, int maxWidth, int maxHeight) {
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();

        double scale = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return scaled;
    }

    /**
     * 導航圖像
     */
    private void navigateImage(int direction) {
        if (currentImageFiles == null || currentImageFiles.isEmpty()) {
            return;
        }

        int newIndex = currentImageIndex + direction;
        if (newIndex < 0) {
            newIndex = currentImageFiles.size() - 1;
        } else if (newIndex >= currentImageFiles.size()) {
            newIndex = 0;
        }

        imageList.setSelectedIndex(newIndex);
    }

    /**
     * 導出參數到 JSON
     */
    private void exportParamsToJson() {
        if (currentParams == null) {
            JOptionPane.showMessageDialog(this, "沒有可導出的參數", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("儲存 JSON 參數檔案");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON 檔案", "json"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".json")) {
                filePath += ".json";
            }

            try {
                updateParamsFromUI();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(currentParams);

                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(json);
                }

                JOptionPane.showMessageDialog(this, 
                    "參數已成功導出到: " + filePath, 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "導出失敗: " + e.getMessage(), 
                    "錯誤", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void dispose() {
        if (currentOriginalImage != null) {
            currentOriginalImage.release();
        }
        if (currentProcessedImage != null) {
            currentProcessedImage.release();
        }
        super.dispose();
    }
}
