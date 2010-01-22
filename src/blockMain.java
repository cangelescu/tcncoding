/*

 Copyright (C) 2009-2010 Oleksandr Natalenko aka post-factum

 This program is free software; you can redistribute it and/or modify
 it under the terms of the Universal Program License as published by
 Oleksandr Natalenko aka post-factum; see file COPYING for details.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

 You should have received a copy of the Universal Program
 License along with this program; if not, write to
 pfactum@gmail.com

*/

import java.awt.Color;
import java.util.Vector;

public class blockMain extends javax.swing.JFrame {
    //blocks (classes)
    coderOfSource currentSourceCoder = null;
    coderOfChannel currentChannelCoder = null;
    modulator currentModulator = null;
    errorsSource currentErrorSource = null;

    //UI
    enum Blocks {message_source, source_coder, channel_coder, modulator, channel};
    Blocks selectedBlock = Blocks.message_source;

    //tools
    dataVizualizator currentModulatorVizualizator = null;
    dataVizualizator currentChannelVizualizator = null;

    //source message
    String message = "";

    //using codes and other parameters
    coderOfSource.sourceCoderCode sourceCode = coderOfSource.sourceCoderCode.mtk2;
    coderOfChannel.channelCoderCode channelCode = coderOfChannel.channelCoderCode.parity_bit;
    modulator.ModulationType modulationType = modulator.ModulationType.AMn;

    //message in binary symbols
    Vector source_symbols = new Vector();
    Vector channel_symbols = new Vector();

    //modulator data
    Vector<FunctionStep> modulator_data = null;

    //channel data
    Vector<FunctionStep> noise = null;
    Vector<FunctionStep> channel_output = null;

    //acts on choosing code of source
    void updateChosenCodeSource()
    {
	switch (sourceCodesChooser.getSelectedIndex())
	{
	    case 0:
		sourceCode = coderOfSource.sourceCoderCode.mtk2;
		break;
	    case 1:
		sourceCode = coderOfSource.sourceCoderCode.mtk5;
		break;
	    case 2:
		sourceCode = coderOfSource.sourceCoderCode.morze;
	    default:
		break;
	}
    }

    //acts on choosing code of channel
    void updateChosenCodeChannel()
    {
	switch (channelCodesChooser.getSelectedIndex())
	{
	    case 0:
		channelCode = coderOfChannel.channelCoderCode.parity_bit;
		break;
	    case 1:
		channelCode = coderOfChannel.channelCoderCode.inversed;
		break;
	    case 2:
		channelCode = coderOfChannel.channelCoderCode.hamming;
		break;
	    default:
		break;
	}
    }

    //acts on choosing type of modulation
    void updateChosenModulationType()
    {
	bearerFrequency2.setEnabled(false);
	bearerFrequency2Label.setEnabled(false);
	switch (modulationTypeChooser.getSelectedIndex())
	{
	    case 0:
		modulationType = modulator.ModulationType.AMn;
		break;
	    case 1:
		modulationType = modulator.ModulationType.FMn;
		bearerFrequency2.setEnabled(true);
		bearerFrequency2Label.setEnabled(true);
		break;
	    case 2:
		modulationType = modulator.ModulationType.PMn;
		break;
	    case 3:
		modulationType = modulator.ModulationType.RPMn;
		break;
	    default:
		break;
	}
    }

    void updateChosenBlock()
    {
	messageSourceButton.setBackground(new Color(240, 240, 240));
	sourceCoderButton.setBackground(new Color(240, 240, 240));
	channelCoderButton.setBackground(new Color(240, 240, 240));
	modulatorButton.setBackground(new Color(240, 240, 240));
	channelButton.setBackground(new Color(240, 240, 240));
	switch (selectedBlock)
	{
	    case message_source:
		TCSTabs.setSelectedComponent(blockMessageSource);
		messageSourceButton.setBackground(new Color(200, 200, 200));
		break;
	    case source_coder:
		TCSTabs.setSelectedComponent(blockSourceCoder);
		sourceCoderButton.setBackground(new Color(200, 200, 200));
		break;
	    case channel_coder:
		TCSTabs.setSelectedComponent(blockChannelCoder);
		channelCoderButton.setBackground(new Color(200, 200, 200));
		break;
	    case modulator:
		TCSTabs.setSelectedComponent(blockModulator);
		modulatorButton.setBackground(new Color(200, 200, 200));
		break;
	    case channel:
		TCSTabs.setSelectedComponent(blockChannel);
		channelButton.setBackground(new Color(200, 200, 200));
		break;
	    default:
		break;
	}
    }

    //encodes source message with selected source code
    void doSourceCoding()
    {
	currentSourceCoder = new coderOfSource(sourceCode, message);
	currentSourceCoder.doEncode();
	source_symbols = currentSourceCoder.getSequence();
	blockSourceCoderOutput.setText(currentSourceCoder.getStringSequence());
    }

    //encodes source code with selected channel code
    void doChannelCoding()
    {
	currentChannelCoder = new coderOfChannel(source_symbols, channelCode, currentSourceCoder.alignment);
	currentChannelCoder.doEncode();
	channel_symbols = currentChannelCoder.getSequence();
	blockChannelCoderOutput.setText(currentChannelCoder.getStringSequence());
    }

    //modulates sinusoidal signal with channel code using selected modulation type
    void doModulating()
    {
	currentModulator = new modulator(modulationType, Double.valueOf(bearerAmplitude.getValue().toString()), Double.valueOf(bearerFrequency1.getValue().toString()), Double.valueOf(bearerFrequency2.getValue().toString()), channel_symbols, currentChannelCoder.alignment);
	currentModulator.doModulation();
	this.modulator_data = currentModulator.getModulatedArray();

	if (currentModulatorVizualizator != null)
	{
	    modulatorOutputField.remove(currentModulatorVizualizator);
	    currentModulatorVizualizator = null;
	}
	int cx = modulatorOutputField.getWidth();
	int cy = modulatorOutputField.getHeight();
	currentModulatorVizualizator = new dataVizualizator(modulator_data, cx, cy, "t", "S(t), В");
	currentModulatorVizualizator.setVisible(true);
	modulatorOutputField.add(currentModulatorVizualizator);
	currentModulatorVizualizator.repaint();
    }

    //adds noise
    void doChannel()
    {
	currentErrorSource = new errorsSource();
	this.channel_output = currentErrorSource.getNoisedChannel(this.modulator_data);

	if (currentChannelVizualizator != null)
	{
	    channelOutputField.remove(currentChannelVizualizator);
	    currentChannelVizualizator = null;
	}
	int cx = channelOutputField.getWidth();
	int cy = channelOutputField.getHeight();
	currentChannelVizualizator = new dataVizualizator(channel_output, cx, cy, "t", "S'(t), В");
	currentChannelVizualizator.setVisible(true);
	channelOutputField.add(currentChannelVizualizator);
	currentChannelVizualizator.repaint();
    }

    public blockMain() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aboutDialog = new javax.swing.JDialog();
        aboutDialogClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TCSTabs = new javax.swing.JTabbedPane();
        blockMessageSource = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        messageLabel = new javax.swing.JLabel();
        blockSourceCoder = new javax.swing.JPanel();
        sourceCodesChooserLabel = new javax.swing.JLabel();
        sourceCodesChooser = new javax.swing.JComboBox();
        blockSourceCoderOutputPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        blockSourceCoderOutput = new javax.swing.JTextPane();
        blockChannelCoder = new javax.swing.JPanel();
        channelCodesChooserLabel = new javax.swing.JLabel();
        channelCodesChooser = new javax.swing.JComboBox();
        blockChannelCoderOutputPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        blockChannelCoderOutput = new javax.swing.JTextPane();
        blockModulatorOptions = new javax.swing.JPanel();
        modulationTypeLabel = new javax.swing.JLabel();
        modulationTypeChooser = new javax.swing.JComboBox();
        bearerAmplitudeLabel = new javax.swing.JLabel();
        bearerAmplitude = new javax.swing.JSpinner();
        bearerFrequency1Label = new javax.swing.JLabel();
        bearerFrequency2Label = new javax.swing.JLabel();
        bearerFrequency1 = new javax.swing.JSpinner();
        bearerFrequency2 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        blockModulator = new javax.swing.JPanel();
        modulatorOutputPanel = new javax.swing.JPanel();
        modulatorOutputField = new javax.swing.JPanel();
        blockChannel = new javax.swing.JPanel();
        channelOutputPanel = new javax.swing.JPanel();
        channelOutputField = new javax.swing.JPanel();
        systemScheme = new javax.swing.JPanel();
        messageSourceButton = new javax.swing.JButton();
        sourceCoderButton = new javax.swing.JButton();
        channelCoderButton = new javax.swing.JButton();
        modulatorButton = new javax.swing.JButton();
        channelButton = new javax.swing.JButton();
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitItem = new javax.swing.JMenuItem();
        modellingMenu = new javax.swing.JMenu();
        doModellingItem = new javax.swing.JMenuItem();
        developerMenu = new javax.swing.JMenu();
        sum2Item = new javax.swing.JMenuItem();
        inversionItem = new javax.swing.JMenuItem();
        shl2Item = new javax.swing.JMenuItem();
        weightItem = new javax.swing.JMenuItem();
        tabulateItem = new javax.swing.JMenuItem();
        integrateItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutItem = new javax.swing.JMenuItem();

        aboutDialogClose.setText("Закрити");
        aboutDialogClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutDialogCloseActionPerformed(evt);
            }
        });

        jLabel1.setText("Модель цифрової системи зв'язку");

        jLabel2.setText("НД ІТС НТУУ «КПІ»");

        jLabel3.setText("Розробник: Олександр Ігорович Наталенко");

        jLabel4.setText("Програма розповсюджується згідно умовам ліцензії UPL.");

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutDialogLayout.createSequentialGroup()
                .addContainerGap(315, Short.MAX_VALUE)
                .addComponent(aboutDialogClose))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(173, Short.MAX_VALUE))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(268, Short.MAX_VALUE))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(113, Short.MAX_VALUE))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        aboutDialogLayout.setVerticalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addComponent(aboutDialogClose))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Система зв'язку");
        setResizable(false);

        blockMessageSource.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMessageSourceComponentShown(evt);
            }
        });

        messageArea.setColumns(20);
        messageArea.setRows(5);
        messageArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(messageArea);

        messageLabel.setLabelFor(messageLabel);
        messageLabel.setText("Повідомлення:");

        javax.swing.GroupLayout blockMessageSourceLayout = new javax.swing.GroupLayout(blockMessageSource);
        blockMessageSource.setLayout(blockMessageSourceLayout);
        blockMessageSourceLayout.setHorizontalGroup(
            blockMessageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockMessageSourceLayout.createSequentialGroup()
                .addComponent(messageLabel)
                .addContainerGap(741, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
        );
        blockMessageSourceLayout.setVerticalGroup(
            blockMessageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockMessageSourceLayout.createSequentialGroup()
                .addComponent(messageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
        );

        TCSTabs.addTab("Джерело повідомлень", blockMessageSource);

        blockSourceCoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockSourceCoderComponentShown(evt);
            }
        });

        sourceCodesChooserLabel.setLabelFor(sourceCodesChooser);
        sourceCodesChooserLabel.setText("Код:");

        sourceCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "МТК-2", "МТК-5", "Морзе" }));
        sourceCodesChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sourceCodesChooserItemStateChanged(evt);
            }
        });

        blockSourceCoderOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід кодера джерела повідомлень"));

        blockSourceCoderOutput.setContentType("text/html");
        blockSourceCoderOutput.setEditable(false);
        jScrollPane2.setViewportView(blockSourceCoderOutput);

        javax.swing.GroupLayout blockSourceCoderOutputPanelLayout = new javax.swing.GroupLayout(blockSourceCoderOutputPanel);
        blockSourceCoderOutputPanel.setLayout(blockSourceCoderOutputPanelLayout);
        blockSourceCoderOutputPanelLayout.setHorizontalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        blockSourceCoderOutputPanelLayout.setVerticalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceCoderLayout = new javax.swing.GroupLayout(blockSourceCoder);
        blockSourceCoder.setLayout(blockSourceCoderLayout);
        blockSourceCoderLayout.setHorizontalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockSourceCoderLayout.createSequentialGroup()
                .addComponent(sourceCodesChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceCodesChooser, 0, 796, Short.MAX_VALUE))
            .addComponent(blockSourceCoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSourceCoderLayout.setVerticalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockSourceCoderLayout.createSequentialGroup()
                .addGroup(blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourceCodesChooserLabel)
                    .addComponent(sourceCodesChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(blockSourceCoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TCSTabs.addTab("Кодер джерела повідомлень", blockSourceCoder);

        blockChannelCoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelCoderComponentShown(evt);
            }
        });

        channelCodesChooserLabel.setLabelFor(channelCodesChooser);
        channelCodesChooserLabel.setText("Код:");

        channelCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "із перевіркою на парність", "інверсний", "Хемінга" }));
        channelCodesChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                channelCodesChooserItemStateChanged(evt);
            }
        });

        blockChannelCoderOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід кодера каналу"));

        blockChannelCoderOutput.setContentType("text/html");
        blockChannelCoderOutput.setEditable(false);
        jScrollPane3.setViewportView(blockChannelCoderOutput);

        javax.swing.GroupLayout blockChannelCoderOutputPanelLayout = new javax.swing.GroupLayout(blockChannelCoderOutputPanel);
        blockChannelCoderOutputPanel.setLayout(blockChannelCoderOutputPanelLayout);
        blockChannelCoderOutputPanelLayout.setHorizontalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        blockChannelCoderOutputPanelLayout.setVerticalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelCoderLayout = new javax.swing.GroupLayout(blockChannelCoder);
        blockChannelCoder.setLayout(blockChannelCoderLayout);
        blockChannelCoderLayout.setHorizontalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockChannelCoderLayout.createSequentialGroup()
                .addComponent(channelCodesChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelCodesChooser, 0, 796, Short.MAX_VALUE))
            .addComponent(blockChannelCoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelCoderLayout.setVerticalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockChannelCoderLayout.createSequentialGroup()
                .addGroup(blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(channelCodesChooserLabel)
                    .addComponent(channelCodesChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(blockChannelCoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TCSTabs.addTab("Кодер каналу", blockChannelCoder);

        modulationTypeLabel.setLabelFor(modulationTypeChooser);
        modulationTypeLabel.setText("Вид модуляції:");

        modulationTypeChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "АМн (амплітудна маніпуляція)", "ЧМн (частотна маніпуляція)", "ФМн (фазова маніпуляція)", "ВФМн (відносна фазова маніпуляція)" }));
        modulationTypeChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                modulationTypeChooserItemStateChanged(evt);
            }
        });

        bearerAmplitudeLabel.setText("Амплітуда несучої:");

        bearerAmplitude.setModel(new javax.swing.SpinnerNumberModel(50.0d, 0.0d, 100.0d, 1.0d));

        bearerFrequency1Label.setText("Частота несучої 1:");

        bearerFrequency2Label.setText("Частота несучої 2:");
        bearerFrequency2Label.setEnabled(false);

        bearerFrequency1.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(100000.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        bearerFrequency2.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(200000.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        bearerFrequency2.setEnabled(false);

        jLabel5.setText("В");

        jLabel6.setText("Гц");

        jLabel7.setText("Гц");

        javax.swing.GroupLayout blockModulatorOptionsLayout = new javax.swing.GroupLayout(blockModulatorOptions);
        blockModulatorOptions.setLayout(blockModulatorOptionsLayout);
        blockModulatorOptionsLayout.setHorizontalGroup(
            blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, blockModulatorOptionsLayout.createSequentialGroup()
                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, blockModulatorOptionsLayout.createSequentialGroup()
                        .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bearerAmplitudeLabel)
                            .addComponent(modulationTypeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(blockModulatorOptionsLayout.createSequentialGroup()
                                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(bearerFrequency2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bearerFrequency1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bearerAmplitude, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)))
                            .addComponent(modulationTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(bearerFrequency2Label, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bearerFrequency1Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        blockModulatorOptionsLayout.setVerticalGroup(
            blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockModulatorOptionsLayout.createSequentialGroup()
                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modulationTypeLabel)
                    .addComponent(modulationTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bearerAmplitudeLabel)
                    .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bearerAmplitude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bearerFrequency1Label)
                    .addComponent(bearerFrequency1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bearerFrequency2Label)
                    .addComponent(bearerFrequency2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(247, Short.MAX_VALUE))
        );

        TCSTabs.addTab("Налаштування модулятора", blockModulatorOptions);

        blockModulator.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockModulatorComponentShown(evt);
            }
        });

        modulatorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід модулятора"));

        javax.swing.GroupLayout modulatorOutputFieldLayout = new javax.swing.GroupLayout(modulatorOutputField);
        modulatorOutputField.setLayout(modulatorOutputFieldLayout);
        modulatorOutputFieldLayout.setHorizontalGroup(
            modulatorOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
        );
        modulatorOutputFieldLayout.setVerticalGroup(
            modulatorOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout modulatorOutputPanelLayout = new javax.swing.GroupLayout(modulatorOutputPanel);
        modulatorOutputPanel.setLayout(modulatorOutputPanelLayout);
        modulatorOutputPanelLayout.setHorizontalGroup(
            modulatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        modulatorOutputPanelLayout.setVerticalGroup(
            modulatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockModulatorLayout = new javax.swing.GroupLayout(blockModulator);
        blockModulator.setLayout(blockModulatorLayout);
        blockModulatorLayout.setHorizontalGroup(
            blockModulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulatorOutputPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockModulatorLayout.setVerticalGroup(
            blockModulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulatorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Модулятор", blockModulator);

        blockChannel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelComponentShown(evt);
            }
        });

        channelOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід каналу"));

        javax.swing.GroupLayout channelOutputFieldLayout = new javax.swing.GroupLayout(channelOutputField);
        channelOutputField.setLayout(channelOutputFieldLayout);
        channelOutputFieldLayout.setHorizontalGroup(
            channelOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
        );
        channelOutputFieldLayout.setVerticalGroup(
            channelOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout channelOutputPanelLayout = new javax.swing.GroupLayout(channelOutputPanel);
        channelOutputPanel.setLayout(channelOutputPanelLayout);
        channelOutputPanelLayout.setHorizontalGroup(
            channelOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(channelOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        channelOutputPanelLayout.setVerticalGroup(
            channelOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(channelOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelLayout = new javax.swing.GroupLayout(blockChannel);
        blockChannel.setLayout(blockChannelLayout);
        blockChannelLayout.setHorizontalGroup(
            blockChannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(channelOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelLayout.setVerticalGroup(
            blockChannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(channelOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Канал", blockChannel);

        systemScheme.setBorder(javax.swing.BorderFactory.createTitledBorder("Структурна схема"));

        messageSourceButton.setBackground(new java.awt.Color(200, 200, 200));
        messageSourceButton.setText("ДжП");
        messageSourceButton.setToolTipText("Джерело повідомлень");
        messageSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageSourceButtonActionPerformed(evt);
            }
        });

        sourceCoderButton.setBackground(new java.awt.Color(240, 240, 240));
        sourceCoderButton.setText("КДж");
        sourceCoderButton.setToolTipText("Кодер джерела повідомлень");
        sourceCoderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceCoderButtonActionPerformed(evt);
            }
        });

        channelCoderButton.setBackground(new java.awt.Color(240, 240, 240));
        channelCoderButton.setText("КК");
        channelCoderButton.setToolTipText("Кодер каналу");
        channelCoderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channelCoderButtonActionPerformed(evt);
            }
        });

        modulatorButton.setBackground(new java.awt.Color(240, 240, 240));
        modulatorButton.setText("М");
        modulatorButton.setToolTipText("Модулятор");
        modulatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulatorButtonActionPerformed(evt);
            }
        });

        channelButton.setBackground(new java.awt.Color(240, 240, 240));
        channelButton.setText("К");
        channelButton.setToolTipText("Канал");
        channelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout systemSchemeLayout = new javax.swing.GroupLayout(systemScheme);
        systemScheme.setLayout(systemSchemeLayout);
        systemSchemeLayout.setHorizontalGroup(
            systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemSchemeLayout.createSequentialGroup()
                .addComponent(messageSourceButton)
                .addGap(18, 18, 18)
                .addComponent(sourceCoderButton)
                .addGap(18, 18, 18)
                .addComponent(channelCoderButton)
                .addGap(18, 18, 18)
                .addComponent(modulatorButton)
                .addGap(18, 18, 18)
                .addComponent(channelButton)
                .addContainerGap(498, Short.MAX_VALUE))
        );
        systemSchemeLayout.setVerticalGroup(
            systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(messageSourceButton)
                .addComponent(sourceCoderButton)
                .addComponent(channelCoderButton)
                .addComponent(modulatorButton)
                .addComponent(channelButton))
        );

        fileMenu.setText("Файл");

        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        exitItem.setText("Вихід");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);

        mainMenu.add(fileMenu);

        modellingMenu.setText("Моделювання");

        doModellingItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        doModellingItem.setText("Виконати моделювання");
        doModellingItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doModellingItemActionPerformed(evt);
            }
        });
        modellingMenu.add(doModellingItem);

        mainMenu.add(modellingMenu);

        developerMenu.setText("Розробка");

        sum2Item.setText("Сума по модулю 2");
        sum2Item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sum2ItemActionPerformed(evt);
            }
        });
        developerMenu.add(sum2Item);

        inversionItem.setText("Інверсія");
        inversionItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inversionItemActionPerformed(evt);
            }
        });
        developerMenu.add(inversionItem);

        shl2Item.setText("Зсув вліво на 1 розряд");
        shl2Item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shl2ItemActionPerformed(evt);
            }
        });
        developerMenu.add(shl2Item);

        weightItem.setText("Вага кодової комбінації");
        weightItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightItemActionPerformed(evt);
            }
        });
        developerMenu.add(weightItem);

        tabulateItem.setText("Табулювання функції");
        tabulateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tabulateItemActionPerformed(evt);
            }
        });
        developerMenu.add(tabulateItem);

        integrateItem.setText("Інтегрування функції");
        integrateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrateItemActionPerformed(evt);
            }
        });
        developerMenu.add(integrateItem);

        mainMenu.add(developerMenu);

        helpMenu.setText("Допомога");

        aboutItem.setText("Про програму…");
        aboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutItem);

        mainMenu.add(helpMenu);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
            .addComponent(systemScheme, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(systemScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitItemActionPerformed
    {//GEN-HEADEREND:event_exitItemActionPerformed
	System.exit(0);
    }//GEN-LAST:event_exitItemActionPerformed

    //checks summarizing by module 2
    private void sum2ItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sum2ItemActionPerformed
    {//GEN-HEADEREND:event_sum2ItemActionPerformed
	binaryNumber num1 = new binaryNumber(10);
	binaryNumber num2 = new binaryNumber(20);
	binaryNumber num3 = num1.sum2(num2);
	System.out.println(num1.toInt() +
		" + " +
		num2.toInt() + 
		" == " +
		num3.getString(0));
    }//GEN-LAST:event_sum2ItemActionPerformed

    private void sourceCodesChooserItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_sourceCodesChooserItemStateChanged
    {//GEN-HEADEREND:event_sourceCodesChooserItemStateChanged
	updateChosenCodeSource();
    }//GEN-LAST:event_sourceCodesChooserItemStateChanged

    //calls all modelling methods step-by-step
    private void doModellingItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_doModellingItemActionPerformed
    {//GEN-HEADEREND:event_doModellingItemActionPerformed
	message = messageArea.getText();
	if (!message.isEmpty())
	{
	    doSourceCoding();
	    doChannelCoding();
	    doModulating();
	    doChannel();
	}
    }//GEN-LAST:event_doModellingItemActionPerformed

    //checks binary number inversion
    private void inversionItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_inversionItemActionPerformed
    {//GEN-HEADEREND:event_inversionItemActionPerformed
	binaryNumber num1 = new binaryNumber(10);
	System.out.println("inversed " +
		num1.getString(0) +
		" == " +
		num1.not2().getString(0));
    }//GEN-LAST:event_inversionItemActionPerformed

    //checks binary number shifting
    private void shl2ItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shl2ItemActionPerformed
    {//GEN-HEADEREND:event_shl2ItemActionPerformed
	binaryNumber num1 = new binaryNumber(10);
	System.out.println("shifted " +
		num1.getString(0) +
		" == " +
		num1.shl2().getString(0));
    }//GEN-LAST:event_shl2ItemActionPerformed

    //checks getting weight of binary number
    private void weightItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_weightItemActionPerformed
    {//GEN-HEADEREND:event_weightItemActionPerformed
	binaryNumber num1 = new binaryNumber(10);
	System.out.println("weight " +
		num1.getString(0) +
		" == " +
		num1.getWeight());
    }//GEN-LAST:event_weightItemActionPerformed

    private void channelCodesChooserItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_channelCodesChooserItemStateChanged
    {//GEN-HEADEREND:event_channelCodesChooserItemStateChanged
	updateChosenCodeChannel();
    }//GEN-LAST:event_channelCodesChooserItemStateChanged

    //shows about dialog
    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutItemActionPerformed
    {//GEN-HEADEREND:event_aboutItemActionPerformed
	aboutDialog.setSize(400, 300);
	aboutDialog.setVisible(true);
    }//GEN-LAST:event_aboutItemActionPerformed

    //hides about dialog
    private void aboutDialogCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutDialogCloseActionPerformed
    {//GEN-HEADEREND:event_aboutDialogCloseActionPerformed
	aboutDialog.setVisible(false);
    }//GEN-LAST:event_aboutDialogCloseActionPerformed

    //updates modulation type on choosing it from combobox
    private void modulationTypeChooserItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_modulationTypeChooserItemStateChanged
    {//GEN-HEADEREND:event_modulationTypeChooserItemStateChanged
	updateChosenModulationType();
    }//GEN-LAST:event_modulationTypeChooserItemStateChanged

    //implements test function to tabulate
    private class sqrx implements MathToolsFunction
    {
	public double function(double x)
	{
	    return Math.pow(x, 2);
	}
    }
    
    //checks function tabulating
    private void tabulateItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_tabulateItemActionPerformed
    {//GEN-HEADEREND:event_tabulateItemActionPerformed
	sqrx testFunction = new sqrx();
	mathTools mtools = new mathTools(1.0E-3);
	Vector<FunctionStep> numbers = mtools.tabulate(testFunction, 0, 0.5);
	System.out.println("Tabulating function f(x)=x^2");
	for(FunctionStep current: numbers)
	{
	    FunctionStep current_step = current;
	    System.out.printf("f(%f)=%f\n", current_step.x, current_step.y);
	}
    }//GEN-LAST:event_tabulateItemActionPerformed

    //implements test function to integrate
    private class tfun implements MathToolsFunction
    {
	public double function(double x)
	{
	    return Math.abs(Math.sin(x)) + Math.exp(x);
	}
    }

    private void integrateItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_integrateItemActionPerformed
    {//GEN-HEADEREND:event_integrateItemActionPerformed
	tfun testFunction = new tfun();
	mathTools mtools = new mathTools(1.0E-3);
	double result = mtools.integrate(mtools.tabulate(testFunction, 0, 2 * Math.PI));
	System.out.printf("Integrate |sin(x)| + e^x from 0 to 2*pi: %f\n", result);
    }//GEN-LAST:event_integrateItemActionPerformed

    private void messageSourceButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_messageSourceButtonActionPerformed
    {//GEN-HEADEREND:event_messageSourceButtonActionPerformed
	selectedBlock = Blocks.message_source;
	updateChosenBlock();
    }//GEN-LAST:event_messageSourceButtonActionPerformed

    private void sourceCoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sourceCoderButtonActionPerformed
    {//GEN-HEADEREND:event_sourceCoderButtonActionPerformed
	selectedBlock = Blocks.source_coder;
	updateChosenBlock();
    }//GEN-LAST:event_sourceCoderButtonActionPerformed

    private void channelCoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_channelCoderButtonActionPerformed
    {//GEN-HEADEREND:event_channelCoderButtonActionPerformed
	selectedBlock = Blocks.channel_coder;
	updateChosenBlock();
    }//GEN-LAST:event_channelCoderButtonActionPerformed

    private void modulatorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_modulatorButtonActionPerformed
    {//GEN-HEADEREND:event_modulatorButtonActionPerformed
	selectedBlock = Blocks.modulator;
	updateChosenBlock();
    }//GEN-LAST:event_modulatorButtonActionPerformed

    private void channelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_channelButtonActionPerformed
    {//GEN-HEADEREND:event_channelButtonActionPerformed
	selectedBlock = Blocks.channel;
	updateChosenBlock();
    }//GEN-LAST:event_channelButtonActionPerformed

    private void blockMessageSourceComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockMessageSourceComponentShown
    {//GEN-HEADEREND:event_blockMessageSourceComponentShown
	selectedBlock = Blocks.message_source;
	updateChosenBlock();
    }//GEN-LAST:event_blockMessageSourceComponentShown

    private void blockSourceCoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockSourceCoderComponentShown
    {//GEN-HEADEREND:event_blockSourceCoderComponentShown
	selectedBlock = Blocks.source_coder;
	updateChosenBlock();
    }//GEN-LAST:event_blockSourceCoderComponentShown

    private void blockChannelCoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelCoderComponentShown
    {//GEN-HEADEREND:event_blockChannelCoderComponentShown
	selectedBlock = Blocks.channel_coder;
	updateChosenBlock();
    }//GEN-LAST:event_blockChannelCoderComponentShown

    private void blockModulatorComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockModulatorComponentShown
    {//GEN-HEADEREND:event_blockModulatorComponentShown
	selectedBlock = Blocks.modulator;
	updateChosenBlock();
    }//GEN-LAST:event_blockModulatorComponentShown

    private void blockChannelComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelComponentShown
    {//GEN-HEADEREND:event_blockChannelComponentShown
	selectedBlock = Blocks.channel;
	updateChosenBlock();
    }//GEN-LAST:event_blockChannelComponentShown

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new blockMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane TCSTabs;
    private javax.swing.JDialog aboutDialog;
    private javax.swing.JButton aboutDialogClose;
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JSpinner bearerAmplitude;
    private javax.swing.JLabel bearerAmplitudeLabel;
    private javax.swing.JSpinner bearerFrequency1;
    private javax.swing.JLabel bearerFrequency1Label;
    private javax.swing.JSpinner bearerFrequency2;
    private javax.swing.JLabel bearerFrequency2Label;
    private javax.swing.JPanel blockChannel;
    private javax.swing.JPanel blockChannelCoder;
    private javax.swing.JTextPane blockChannelCoderOutput;
    private javax.swing.JPanel blockChannelCoderOutputPanel;
    private javax.swing.JPanel blockMessageSource;
    private javax.swing.JPanel blockModulator;
    private javax.swing.JPanel blockModulatorOptions;
    private javax.swing.JPanel blockSourceCoder;
    private javax.swing.JTextPane blockSourceCoderOutput;
    private javax.swing.JPanel blockSourceCoderOutputPanel;
    private javax.swing.JButton channelButton;
    private javax.swing.JButton channelCoderButton;
    private javax.swing.JComboBox channelCodesChooser;
    private javax.swing.JLabel channelCodesChooserLabel;
    private javax.swing.JPanel channelOutputField;
    private javax.swing.JPanel channelOutputPanel;
    private javax.swing.JMenu developerMenu;
    private javax.swing.JMenuItem doModellingItem;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem integrateItem;
    private javax.swing.JMenuItem inversionItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton messageSourceButton;
    private javax.swing.JMenu modellingMenu;
    private javax.swing.JComboBox modulationTypeChooser;
    private javax.swing.JLabel modulationTypeLabel;
    private javax.swing.JButton modulatorButton;
    private javax.swing.JPanel modulatorOutputField;
    private javax.swing.JPanel modulatorOutputPanel;
    private javax.swing.JMenuItem shl2Item;
    private javax.swing.JButton sourceCoderButton;
    private javax.swing.JComboBox sourceCodesChooser;
    private javax.swing.JLabel sourceCodesChooserLabel;
    private javax.swing.JMenuItem sum2Item;
    private javax.swing.JPanel systemScheme;
    private javax.swing.JMenuItem tabulateItem;
    private javax.swing.JMenuItem weightItem;
    // End of variables declaration//GEN-END:variables

}
