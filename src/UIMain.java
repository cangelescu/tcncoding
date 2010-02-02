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
import flanagan.integration.*;

public class UIMain extends javax.swing.JFrame {
    //blocks (classes)
    SourceCoder currentSourceCoder = null;
    ChannelCoder currentChannelCoder = null;
    Modulator currentModulator = null;
    Channel currentChannel = null;
    ChannelSqr currentChannelSqr = null;
    EnergyComputator currentEnergyComputator = null;
    ErrorsComputator currentErrorsComputator = null;
    Multiplier currentMultiplier0 = null, currentMultiplier1 = null;
    Integrator currentIntegrator0 = null, currentIntegrator1 = null;
    Summator currentSummator = null;

    //UI
    enum Blocks {message_source, source_coder, channel_coder, modulator, channel, receiver};
    Blocks selectedBlock = Blocks.message_source;

    //tools
    DataVizualizator currentModulatorVizualizator = null;
    DataVizualizator currentChannelVizualizator = null;
    DataVizualizator currentMultiplierVizualizator0 = null;
    DataVizualizator currentMultiplierVizualizator1 = null;
    DataVizualizator currentIntegratorVizualizator0 = null;
    DataVizualizator currentIntegratorVizualizator1 = null;
    DataVizualizator currentSummatorVizualizator = null;

    //source message
    String message = "";

    //using codes and other parameters
    SourceCoder.sourceCoderCode sourceCode = SourceCoder.sourceCoderCode.mtk2;
    ChannelCoder.channelCoderCode channelCode = ChannelCoder.channelCoderCode.parity_bit;
    Modulator.ModulationType modulationType = Modulator.ModulationType.AMn;

    //message in binary symbols
    Vector<BinaryNumber> source_symbols = new Vector();
    Vector<BinaryNumber> channel_symbols = new Vector();

    //Modulator data
    Vector<ModulatorSignal> modulator_data = null;
    Vector<DataVizualizatorProvider> modulator_data_provider = null;

    //Channel data
    Vector<ChannelSignal> channel_output = null;
    Vector<ChannelSignalSqr> channel_sqr_output = null;
    Vector<DataVizualizatorProvider> channel_output_provider = null;
    double channel_output_energy = 0;
    double errors_probability = 0;

    //multipliers data
    Vector<MultiplierSignal> multiplier_0_output = null;
    Vector<MultiplierSignal> multiplier_1_output = null;
    Vector<DataVizualizatorProvider> multiplier_0_output_provider = null;
    Vector<DataVizualizatorProvider> multiplier_1_output_provider = null;

    //integrators data
    Vector<Vector<FunctionStep>> integrator_0_output = null;
    Vector<Vector<FunctionStep>> integrator_1_output = null;
    Vector<DataVizualizatorProvider> integrator_0_output_provider = null;
    Vector<DataVizualizatorProvider> integrator_1_output_provider = null;

    //Summator data
    Vector<Vector<FunctionStep>> summator_output = null;
    Vector<DataVizualizatorProvider> summator_output_provider = null;

    //acts on choosing code of source
    void updateChosenCodeSource()
    {
	switch (sourceCodesChooser.getSelectedIndex())
	{
	    case 0:
		sourceCode = SourceCoder.sourceCoderCode.mtk2;
		break;
	    case 1:
		sourceCode = SourceCoder.sourceCoderCode.mtk5;
		break;
	    case 2:
		sourceCode = SourceCoder.sourceCoderCode.koi8u;
		break;
	    case 3:
		sourceCode = SourceCoder.sourceCoderCode.morse;
		break;
	    case 4:
		sourceCode = SourceCoder.sourceCoderCode.shannon;
		break;
	    default:
		break;
	}
    }

    //acts on choosing code of Channel
    void updateChosenCodeChannel()
    {
	switch (channelCodesChooser.getSelectedIndex())
	{
	    case 0:
		channelCode = ChannelCoder.channelCoderCode.parity_bit;
		break;
	    case 1:
		channelCode = ChannelCoder.channelCoderCode.inversed;
		break;
	    case 2:
		channelCode = ChannelCoder.channelCoderCode.manchester;
		break;
	    case 3:
		channelCode = ChannelCoder.channelCoderCode.hamming;
		break;
	    default:
		break;
	}
    }

    //acts on choosing type of modulation
    void updateChosenModulationType()
    {
	//disable zero bearer options
	bearerFrequency0.setEnabled(false);
	bearerFrequency0Label.setEnabled(false);
	switch (modulationTypeChooser.getSelectedIndex())
	{
	    case 0:
		modulationType = Modulator.ModulationType.AMn;
		break;
	    case 1:
		modulationType = Modulator.ModulationType.FMn;
		//enable zero bearer options
		bearerFrequency0.setEnabled(true);
		bearerFrequency0Label.setEnabled(true);
		break;
	    case 2:
		modulationType = Modulator.ModulationType.PMn;
		break;
	    case 3:
		modulationType = Modulator.ModulationType.RPMn;
		break;
	    default:
		break;
	}
    }

    //acts on block changing
    void updateChosenBlock()
    {
	//make all buttons unselected
	messageSourceButton.setBackground(new Color(240, 240, 240));
	sourceCoderButton.setBackground(new Color(240, 240, 240));
	channelCoderButton.setBackground(new Color(240, 240, 240));
	modulatorButton.setBackground(new Color(240, 240, 240));
	channelButton.setBackground(new Color(240, 240, 240));
	//highlight selected block
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
	    case receiver:
		TCSTabs.setSelectedComponent(blockMessageReceiver);
		break;
	    default:
		break;
	}
    }

    //encodes source message with selected source code
    void doSourceCoding()
    {
	currentSourceCoder = new SourceCoder(sourceCode, message);
	currentSourceCoder.doEncode();
	source_symbols = currentSourceCoder.getSequence();
	blockSourceCoderOutput.setText(currentSourceCoder.getStringSequence());
    }

    //encodes source code with selected Channel code
    void doChannelCoding()
    {
	currentChannelCoder = new ChannelCoder(source_symbols, channelCode);
	currentChannelCoder.doEncode();
	channel_symbols = currentChannelCoder.getSequence();
	blockChannelCoderOutput.setText(currentChannelCoder.getStringSequence());
    }

    //modulates sinusoidal signal with Channel code using selected modulation type
    void doModulating()
    {
	//gets modulator output signals
	currentModulator = new Modulator(modulationType, Double.valueOf(bearerAmplitude.getValue().toString()), Double.valueOf(bearerFrequency0.getValue().toString()), Double.valueOf(bearerFrequency1.getValue().toString()), channel_symbols);
	currentModulator.doModulation();
	this.modulator_data = currentModulator.getSignals();

	//removes old vizualizator if exists
	if (currentModulatorVizualizator != null)
	{
	    modulatorOutputField.remove(currentModulatorVizualizator);
	    currentModulatorVizualizator = null;
	}
	//creates new vizualizator data provider
	modulator_data_provider = (new DataVizualizatorConverter(modulator_data, DataVizualizatorProvider.SignalType.modulator)).getProvided();
	//gets chart width and height
	int cx = modulatorOutputField.getWidth();
	int cy = modulatorOutputField.getHeight();
	//creates new vizualizator
	currentModulatorVizualizator = new DataVizualizator(modulator_data_provider, cx, cy, "t", "S(t), В");
	//shows chart
	currentModulatorVizualizator.setVisible(true);
	modulatorOutputField.add(currentModulatorVizualizator);
	//repaints chart to show it if modulator block is active
	currentModulatorVizualizator.repaint();
    }

    //adds noise
    void doChannel()
    {
	//gets channel output signal
	currentChannel = new Channel(this.modulator_data);
	currentChannel.doNoising();
	this.channel_output = currentChannel.getSignals();

	//gets channel output signal energy
	currentChannelSqr = new ChannelSqr(this.modulator_data);
	currentChannelSqr.doNoising();
	this.channel_sqr_output = currentChannelSqr.getSignals();
	currentEnergyComputator = new EnergyComputator(this.channel_sqr_output);
	currentEnergyComputator.computeEnergy();
	this.channel_output_energy = currentEnergyComputator.getEnergy();

	//computes errors probability
	currentErrorsComputator = new ErrorsComputator(channel_output_energy, 1.0E-2, modulationType);
	this.errors_probability = currentErrorsComputator.getErrorProbability();

	//removes old vizualizator if exists
	if (currentChannelVizualizator != null)
	{
	    channelOutputField.remove(currentChannelVizualizator);
	    currentChannelVizualizator = null;
	}
	//creates new vizualizator data provider
	channel_output_provider = (new DataVizualizatorConverter(channel_output, DataVizualizatorProvider.SignalType.channel)).getProvided();
	//gets chart width and height
	int cx = channelOutputField.getWidth();
	int cy = channelOutputField.getHeight();
	//creates new vizualizator
	currentChannelVizualizator = new DataVizualizator(channel_output_provider, cx, cy, "t", "S'(t), В");
	//shows chart
	currentChannelVizualizator.setVisible(true);
	channelOutputField.add(currentChannelVizualizator);
	//repaints chart to show it if modulator block is active
	currentChannelVizualizator.repaint();
    }

    //multiplies signals and ethalons
    void doMultiplying()
    {
	//create multipliers according to modulation type
	switch (this.modulationType)
	{
	    case AMn:
		currentMultiplier0 = new Multiplier(Double.valueOf(bearerFrequency1.getValue().toString()), 0, 0, this.channel_output);
		currentMultiplier1 = new Multiplier(Double.valueOf(bearerFrequency1.getValue().toString()), Double.valueOf(bearerAmplitude.getValue().toString()), 0, this.channel_output);
		break;
	    case FMn:
		currentMultiplier0 = new Multiplier(Double.valueOf(bearerFrequency0.getValue().toString()), Double.valueOf(bearerAmplitude.getValue().toString()), 0, this.channel_output);
		currentMultiplier1 = new Multiplier(Double.valueOf(bearerFrequency1.getValue().toString()), Double.valueOf(bearerAmplitude.getValue().toString()), 0, this.channel_output);
		break;
	    case PMn:
		currentMultiplier0 = new Multiplier(Double.valueOf(bearerFrequency1.getValue().toString()), Double.valueOf(bearerAmplitude.getValue().toString()), 0, this.channel_output);
		currentMultiplier1 = new Multiplier(Double.valueOf(bearerFrequency1.getValue().toString()), Double.valueOf(bearerAmplitude.getValue().toString()), -Math.PI, this.channel_output);
		break;
	    case RPMn:
		currentMultiplier0 = new Multiplier(Double.valueOf(bearerFrequency1.getValue().toString()), Double.valueOf(bearerAmplitude.getValue().toString()), 0, this.channel_output);
		currentMultiplier1 = new Multiplier(Double.valueOf(bearerFrequency1.getValue().toString()), Double.valueOf(bearerAmplitude.getValue().toString()), -Math.PI, this.channel_output);
		break;
	}

	//multiplies
	currentMultiplier0.doMultiply();
	currentMultiplier1.doMultiply();
	multiplier_0_output = currentMultiplier0.getSignals();
	multiplier_1_output = currentMultiplier1.getSignals();

	//prepares vizualizator
	if (currentMultiplierVizualizator0 != null)
	{
	    multiplierOutputField0.remove(currentMultiplierVizualizator0);
	    currentMultiplierVizualizator0 = null;
	}
	int cx0 = multiplierOutputField0.getWidth();
	int cy0 = multiplierOutputField0.getHeight();

	//vizualizes signal
	multiplier_0_output_provider = (new DataVizualizatorConverter(multiplier_0_output, DataVizualizatorProvider.SignalType.multiplier)).getProvided();
	currentMultiplierVizualizator0 = new DataVizualizator(multiplier_0_output_provider, cx0, cy0, "t", "Sm0(t), В");
	currentMultiplierVizualizator0.setVisible(true);
	multiplierOutputField0.add(currentMultiplierVizualizator0);
	currentMultiplierVizualizator0.repaint();

	//does the same for second multiplier
	if (currentMultiplierVizualizator1 != null)
	{
	    multiplierOutputField1.remove(currentMultiplierVizualizator1);
	    currentMultiplierVizualizator1 = null;
	}
	int cx1 = multiplierOutputField1.getWidth();
	int cy1 = multiplierOutputField1.getHeight();
	
	multiplier_1_output_provider = (new DataVizualizatorConverter(multiplier_1_output, DataVizualizatorProvider.SignalType.multiplier)).getProvided();
	currentMultiplierVizualizator1 = new DataVizualizator(multiplier_1_output_provider, cx1, cy1, "t", "Sm1(t), В");
	currentMultiplierVizualizator1.setVisible(true);
	multiplierOutputField1.add(currentMultiplierVizualizator1);
	currentMultiplierVizualizator1.repaint();
    }

    //integrates signals from multipliers
    void doIntegrating()
    {
	currentIntegrator0 = new Integrator(multiplier_0_output);
	currentIntegrator1 = new Integrator(multiplier_1_output);
	currentIntegrator0.doIntegrating();
	currentIntegrator1.doIntegrating();
	integrator_0_output = currentIntegrator0.getIntegrals();
	integrator_1_output = currentIntegrator1.getIntegrals();

	if (currentIntegratorVizualizator0 != null)
	{
	    integratorOutputField0.remove(currentIntegratorVizualizator0);
	    currentIntegratorVizualizator0 = null;
	}
	int cx0 = integratorOutputField0.getWidth();
	int cy0 = integratorOutputField0.getHeight();

	integrator_0_output_provider = (new DataVizualizatorConverter(integrator_0_output, DataVizualizatorProvider.SignalType.tabulated)).getProvided();
	currentIntegratorVizualizator0 = new DataVizualizator(integrator_0_output_provider, cx0, cy0, "t", "Si0(t), В");
	currentIntegratorVizualizator0.setVisible(true);
	integratorOutputField0.add(currentIntegratorVizualizator0);
	currentIntegratorVizualizator0.repaint();

	if (currentIntegratorVizualizator1 != null)
	{
	    integratorOutputField1.remove(currentIntegratorVizualizator1);
	    currentIntegratorVizualizator1 = null;
	}
	int cx1 = integratorOutputField1.getWidth();
	int cy1 = integratorOutputField1.getHeight();

	integrator_1_output_provider = (new DataVizualizatorConverter(integrator_1_output, DataVizualizatorProvider.SignalType.tabulated)).getProvided();
	currentIntegratorVizualizator1 = new DataVizualizator(integrator_1_output_provider, cx1, cy1, "t", "Si1(t), В");
	currentIntegratorVizualizator1.setVisible(true);
	integratorOutputField1.add(currentIntegratorVizualizator1);
	currentIntegratorVizualizator1.repaint();
    }

    //sums signals from integrators
    void doSumming()
    {
	currentSummator = new Summator(integrator_0_output, integrator_1_output);
	currentSummator.doSumming();
	summator_output = currentSummator.getSum();

	if (currentSummatorVizualizator != null)
	{
	    summatorOutputField.remove(currentSummatorVizualizator);
	    currentSummatorVizualizator = null;
	}
	int cx1 = summatorOutputField.getWidth();
	int cy1 = summatorOutputField.getHeight();

	summator_output_provider = (new DataVizualizatorConverter(summator_output, DataVizualizatorProvider.SignalType.tabulated)).getProvided();
	currentSummatorVizualizator = new DataVizualizator(summator_output_provider, cx1, cy1, "t", "Ssum(t), В");
	currentSummatorVizualizator.setVisible(true);
	summatorOutputField.add(currentSummatorVizualizator);
	currentSummatorVizualizator.repaint();
    }

    public UIMain() {
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
        sourceMessagePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
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
        bearerFrequency0Label = new javax.swing.JLabel();
        bearerFrequency1Label = new javax.swing.JLabel();
        bearerFrequency0 = new javax.swing.JSpinner();
        bearerFrequency1 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        blockModulator = new javax.swing.JPanel();
        modulatorOutputPanel = new javax.swing.JPanel();
        modulatorOutputField = new javax.swing.JPanel();
        blockChannel = new javax.swing.JPanel();
        channelOutputPanel = new javax.swing.JPanel();
        channelOutputField = new javax.swing.JPanel();
        blockMultiplier0 = new javax.swing.JPanel();
        multiplierOutputPanel0 = new javax.swing.JPanel();
        multiplierOutputField0 = new javax.swing.JPanel();
        blockMultiplier1 = new javax.swing.JPanel();
        multiplierOutputPanel1 = new javax.swing.JPanel();
        multiplierOutputField1 = new javax.swing.JPanel();
        blockIntegrator0 = new javax.swing.JPanel();
        integratorOutputPanel0 = new javax.swing.JPanel();
        integratorOutputField0 = new javax.swing.JPanel();
        blockIntegrator1 = new javax.swing.JPanel();
        integratorOutputPanel1 = new javax.swing.JPanel();
        integratorOutputField1 = new javax.swing.JPanel();
        blockSummator = new javax.swing.JPanel();
        summatorOutputPanel = new javax.swing.JPanel();
        summatorOutputField = new javax.swing.JPanel();
        blockMessageReceiver = new javax.swing.JPanel();
        receivedMessagePanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        receivedMessageArea = new javax.swing.JTextPane();
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
        integrateItem = new javax.swing.JMenuItem();
        blockingItem = new javax.swing.JMenuItem();
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

        sourceMessagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Повідомлення"));

        messageArea.setColumns(20);
        messageArea.setRows(5);
        messageArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(messageArea);

        javax.swing.GroupLayout sourceMessagePanelLayout = new javax.swing.GroupLayout(sourceMessagePanel);
        sourceMessagePanel.setLayout(sourceMessagePanelLayout);
        sourceMessagePanelLayout.setHorizontalGroup(
            sourceMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE)
        );
        sourceMessagePanelLayout.setVerticalGroup(
            sourceMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockMessageSourceLayout = new javax.swing.GroupLayout(blockMessageSource);
        blockMessageSource.setLayout(blockMessageSourceLayout);
        blockMessageSourceLayout.setHorizontalGroup(
            blockMessageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourceMessagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockMessageSourceLayout.setVerticalGroup(
            blockMessageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourceMessagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Джерело повідомлень", blockMessageSource);

        blockSourceCoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockSourceCoderComponentShown(evt);
            }
        });

        sourceCodesChooserLabel.setLabelFor(sourceCodesChooser);
        sourceCodesChooserLabel.setText("Код:");

        sourceCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "МТК-2", "МТК-5", "KOI8-U", "Морзе", "Шенона-Фано" }));
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE)
        );
        blockSourceCoderOutputPanelLayout.setVerticalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceCoderLayout = new javax.swing.GroupLayout(blockSourceCoder);
        blockSourceCoder.setLayout(blockSourceCoderLayout);
        blockSourceCoderLayout.setHorizontalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockSourceCoderLayout.createSequentialGroup()
                .addComponent(sourceCodesChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceCodesChooser, 0, 955, Short.MAX_VALUE))
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

        channelCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "із перевіркою на парність", "інверсний", "манчестерський", "Хемінга" }));
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
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE)
        );
        blockChannelCoderOutputPanelLayout.setVerticalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelCoderLayout = new javax.swing.GroupLayout(blockChannelCoder);
        blockChannelCoder.setLayout(blockChannelCoderLayout);
        blockChannelCoderLayout.setHorizontalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockChannelCoderLayout.createSequentialGroup()
                .addComponent(channelCodesChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelCodesChooser, 0, 955, Short.MAX_VALUE))
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

        bearerFrequency0Label.setText("Частота несучої 0:");
        bearerFrequency0Label.setEnabled(false);

        bearerFrequency1Label.setText("Частота несучої 1:");

        bearerFrequency0.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(100000.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        bearerFrequency0.setEnabled(false);

        bearerFrequency1.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(200000.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

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
                                    .addComponent(bearerFrequency1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bearerFrequency0, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bearerAmplitude, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)))
                            .addComponent(modulationTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(bearerFrequency1Label, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bearerFrequency0Label, javax.swing.GroupLayout.Alignment.LEADING))
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
                    .addComponent(bearerFrequency0Label)
                    .addComponent(bearerFrequency0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(blockModulatorOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bearerFrequency1Label)
                    .addComponent(bearerFrequency1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(235, Short.MAX_VALUE))
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
            .addGap(0, 984, Short.MAX_VALUE)
        );
        modulatorOutputFieldLayout.setVerticalGroup(
            modulatorOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
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
            .addGap(0, 984, Short.MAX_VALUE)
        );
        channelOutputFieldLayout.setVerticalGroup(
            channelOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
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

        multiplierOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід помножувача 0"));

        javax.swing.GroupLayout multiplierOutputField0Layout = new javax.swing.GroupLayout(multiplierOutputField0);
        multiplierOutputField0.setLayout(multiplierOutputField0Layout);
        multiplierOutputField0Layout.setHorizontalGroup(
            multiplierOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 984, Short.MAX_VALUE)
        );
        multiplierOutputField0Layout.setVerticalGroup(
            multiplierOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout multiplierOutputPanel0Layout = new javax.swing.GroupLayout(multiplierOutputPanel0);
        multiplierOutputPanel0.setLayout(multiplierOutputPanel0Layout);
        multiplierOutputPanel0Layout.setHorizontalGroup(
            multiplierOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        multiplierOutputPanel0Layout.setVerticalGroup(
            multiplierOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockMultiplier0Layout = new javax.swing.GroupLayout(blockMultiplier0);
        blockMultiplier0.setLayout(blockMultiplier0Layout);
        blockMultiplier0Layout.setHorizontalGroup(
            blockMultiplier0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockMultiplier0Layout.setVerticalGroup(
            blockMultiplier0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Помножувач 0", blockMultiplier0);

        multiplierOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід помножувача 1"));

        javax.swing.GroupLayout multiplierOutputField1Layout = new javax.swing.GroupLayout(multiplierOutputField1);
        multiplierOutputField1.setLayout(multiplierOutputField1Layout);
        multiplierOutputField1Layout.setHorizontalGroup(
            multiplierOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 984, Short.MAX_VALUE)
        );
        multiplierOutputField1Layout.setVerticalGroup(
            multiplierOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout multiplierOutputPanel1Layout = new javax.swing.GroupLayout(multiplierOutputPanel1);
        multiplierOutputPanel1.setLayout(multiplierOutputPanel1Layout);
        multiplierOutputPanel1Layout.setHorizontalGroup(
            multiplierOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        multiplierOutputPanel1Layout.setVerticalGroup(
            multiplierOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockMultiplier1Layout = new javax.swing.GroupLayout(blockMultiplier1);
        blockMultiplier1.setLayout(blockMultiplier1Layout);
        blockMultiplier1Layout.setHorizontalGroup(
            blockMultiplier1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockMultiplier1Layout.setVerticalGroup(
            blockMultiplier1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Помножувач 1", blockMultiplier1);

        integratorOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід інтегратора 0"));

        javax.swing.GroupLayout integratorOutputField0Layout = new javax.swing.GroupLayout(integratorOutputField0);
        integratorOutputField0.setLayout(integratorOutputField0Layout);
        integratorOutputField0Layout.setHorizontalGroup(
            integratorOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 984, Short.MAX_VALUE)
        );
        integratorOutputField0Layout.setVerticalGroup(
            integratorOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout integratorOutputPanel0Layout = new javax.swing.GroupLayout(integratorOutputPanel0);
        integratorOutputPanel0.setLayout(integratorOutputPanel0Layout);
        integratorOutputPanel0Layout.setHorizontalGroup(
            integratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        integratorOutputPanel0Layout.setVerticalGroup(
            integratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockIntegrator0Layout = new javax.swing.GroupLayout(blockIntegrator0);
        blockIntegrator0.setLayout(blockIntegrator0Layout);
        blockIntegrator0Layout.setHorizontalGroup(
            blockIntegrator0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockIntegrator0Layout.setVerticalGroup(
            blockIntegrator0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Інтегратор 0", blockIntegrator0);

        integratorOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід інтегратора 1"));

        javax.swing.GroupLayout integratorOutputField1Layout = new javax.swing.GroupLayout(integratorOutputField1);
        integratorOutputField1.setLayout(integratorOutputField1Layout);
        integratorOutputField1Layout.setHorizontalGroup(
            integratorOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 984, Short.MAX_VALUE)
        );
        integratorOutputField1Layout.setVerticalGroup(
            integratorOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout integratorOutputPanel1Layout = new javax.swing.GroupLayout(integratorOutputPanel1);
        integratorOutputPanel1.setLayout(integratorOutputPanel1Layout);
        integratorOutputPanel1Layout.setHorizontalGroup(
            integratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        integratorOutputPanel1Layout.setVerticalGroup(
            integratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockIntegrator1Layout = new javax.swing.GroupLayout(blockIntegrator1);
        blockIntegrator1.setLayout(blockIntegrator1Layout);
        blockIntegrator1Layout.setHorizontalGroup(
            blockIntegrator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockIntegrator1Layout.setVerticalGroup(
            blockIntegrator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Інтегратор 1", blockIntegrator1);

        summatorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід суматора"));

        javax.swing.GroupLayout summatorOutputFieldLayout = new javax.swing.GroupLayout(summatorOutputField);
        summatorOutputField.setLayout(summatorOutputFieldLayout);
        summatorOutputFieldLayout.setHorizontalGroup(
            summatorOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 984, Short.MAX_VALUE)
        );
        summatorOutputFieldLayout.setVerticalGroup(
            summatorOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout summatorOutputPanelLayout = new javax.swing.GroupLayout(summatorOutputPanel);
        summatorOutputPanel.setLayout(summatorOutputPanelLayout);
        summatorOutputPanelLayout.setHorizontalGroup(
            summatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(summatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        summatorOutputPanelLayout.setVerticalGroup(
            summatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(summatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSummatorLayout = new javax.swing.GroupLayout(blockSummator);
        blockSummator.setLayout(blockSummatorLayout);
        blockSummatorLayout.setHorizontalGroup(
            blockSummatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(summatorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSummatorLayout.setVerticalGroup(
            blockSummatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(summatorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Суматор", blockSummator);

        receivedMessagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Повідомлення"));

        receivedMessageArea.setContentType("text/html");
        receivedMessageArea.setEditable(false);
        jScrollPane4.setViewportView(receivedMessageArea);

        javax.swing.GroupLayout receivedMessagePanelLayout = new javax.swing.GroupLayout(receivedMessagePanel);
        receivedMessagePanel.setLayout(receivedMessagePanelLayout);
        receivedMessagePanelLayout.setHorizontalGroup(
            receivedMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 984, Short.MAX_VALUE)
        );
        receivedMessagePanelLayout.setVerticalGroup(
            receivedMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockMessageReceiverLayout = new javax.swing.GroupLayout(blockMessageReceiver);
        blockMessageReceiver.setLayout(blockMessageReceiverLayout);
        blockMessageReceiverLayout.setHorizontalGroup(
            blockMessageReceiverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(receivedMessagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockMessageReceiverLayout.setVerticalGroup(
            blockMessageReceiverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(receivedMessagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Отримувач повідомлень", blockMessageReceiver);

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
                .addContainerGap(657, Short.MAX_VALUE))
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

        integrateItem.setText("Інтегрування функції");
        integrateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrateItemActionPerformed(evt);
            }
        });
        developerMenu.add(integrateItem);

        blockingItem.setText("Розбиття на блоки");
        blockingItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockingItemActionPerformed(evt);
            }
        });
        developerMenu.add(blockingItem);

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
            .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 999, Short.MAX_VALUE)
            .addComponent(systemScheme, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(systemScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE))
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
	BinaryNumber num1 = new BinaryNumber(10);
	BinaryNumber num2 = new BinaryNumber(20);
	BinaryNumber num3 = num1.sum2(num2);
	System.out.println(num1.toInt() +
		" + " +
		num2.toInt() + 
		" == " +
		num3.getStringSequence());
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
	    doMultiplying();
	    doIntegrating();
	    doSumming();
	}
    }//GEN-LAST:event_doModellingItemActionPerformed

    //checks binary number inversion
    private void inversionItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_inversionItemActionPerformed
    {//GEN-HEADEREND:event_inversionItemActionPerformed
	BinaryNumber num1 = new BinaryNumber(10);
	System.out.println("inversed " +
		num1.getStringSequence() +
		" == " +
		num1.not2().getStringSequence());
    }//GEN-LAST:event_inversionItemActionPerformed

    //checks binary number shifting
    private void shl2ItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shl2ItemActionPerformed
    {//GEN-HEADEREND:event_shl2ItemActionPerformed
	BinaryNumber num1 = new BinaryNumber(10);
	System.out.println("shifted " +
		num1.getStringSequence() +
		" == " +
		num1.shl2().getStringSequence());
    }//GEN-LAST:event_shl2ItemActionPerformed

    //checks getting weight of binary number
    private void weightItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_weightItemActionPerformed
    {//GEN-HEADEREND:event_weightItemActionPerformed
	BinaryNumber num1 = new BinaryNumber(10);
	System.out.println("weight " +
		num1.getStringSequence() +
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

    //implements test function to integrate
    private class tfun implements IntegralFunction
    {
	public double function(double x)
	{
	    return Math.abs(Math.sin(x)) + Math.exp(x);
	}
    }

    private void integrateItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_integrateItemActionPerformed
    {//GEN-HEADEREND:event_integrateItemActionPerformed
	tfun testfunction = new tfun();
	double result = Integration.gaussQuad(testfunction, 0, 2 * Math.PI, 128);
	System.out.printf("Integral of |sin(x)|+e^x from 0 to 2pi: %1.4f\n", result);
    }//GEN-LAST:event_integrateItemActionPerformed

    private void blockingItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_blockingItemActionPerformed
    {//GEN-HEADEREND:event_blockingItemActionPerformed
	BinaryNumber test1 = new BinaryNumber("11011010001101");
	BinaryNumber test2 = new BinaryNumber("01011");
	BinaryNumber test3 = new BinaryNumber("100111");
	Vector<BinaryNumber> test_vector = new Vector<BinaryNumber>();
	test_vector.add(test1);
	test_vector.add(test2);
	test_vector.add(test3);
	Blocker test_blocker = new Blocker(test_vector, 4);
	test_blocker.doBlocking();
	Vector<BinaryNumber> blocks = test_blocker.getBlocks();
	for (BinaryNumber bn: blocks)
	    System.out.println(bn.getStringSequence());
    }//GEN-LAST:event_blockingItemActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UIMain().setVisible(true);
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
    private javax.swing.JSpinner bearerFrequency0;
    private javax.swing.JLabel bearerFrequency0Label;
    private javax.swing.JSpinner bearerFrequency1;
    private javax.swing.JLabel bearerFrequency1Label;
    private javax.swing.JPanel blockChannel;
    private javax.swing.JPanel blockChannelCoder;
    private javax.swing.JTextPane blockChannelCoderOutput;
    private javax.swing.JPanel blockChannelCoderOutputPanel;
    private javax.swing.JPanel blockIntegrator0;
    private javax.swing.JPanel blockIntegrator1;
    private javax.swing.JPanel blockMessageReceiver;
    private javax.swing.JPanel blockMessageSource;
    private javax.swing.JPanel blockModulator;
    private javax.swing.JPanel blockModulatorOptions;
    private javax.swing.JPanel blockMultiplier0;
    private javax.swing.JPanel blockMultiplier1;
    private javax.swing.JPanel blockSourceCoder;
    private javax.swing.JTextPane blockSourceCoderOutput;
    private javax.swing.JPanel blockSourceCoderOutputPanel;
    private javax.swing.JPanel blockSummator;
    private javax.swing.JMenuItem blockingItem;
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
    private javax.swing.JPanel integratorOutputField0;
    private javax.swing.JPanel integratorOutputField1;
    private javax.swing.JPanel integratorOutputPanel0;
    private javax.swing.JPanel integratorOutputPanel1;
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JButton messageSourceButton;
    private javax.swing.JMenu modellingMenu;
    private javax.swing.JComboBox modulationTypeChooser;
    private javax.swing.JLabel modulationTypeLabel;
    private javax.swing.JButton modulatorButton;
    private javax.swing.JPanel modulatorOutputField;
    private javax.swing.JPanel modulatorOutputPanel;
    private javax.swing.JPanel multiplierOutputField0;
    private javax.swing.JPanel multiplierOutputField1;
    private javax.swing.JPanel multiplierOutputPanel0;
    private javax.swing.JPanel multiplierOutputPanel1;
    private javax.swing.JTextPane receivedMessageArea;
    private javax.swing.JPanel receivedMessagePanel;
    private javax.swing.JMenuItem shl2Item;
    private javax.swing.JButton sourceCoderButton;
    private javax.swing.JComboBox sourceCodesChooser;
    private javax.swing.JLabel sourceCodesChooserLabel;
    private javax.swing.JPanel sourceMessagePanel;
    private javax.swing.JMenuItem sum2Item;
    private javax.swing.JPanel summatorOutputField;
    private javax.swing.JPanel summatorOutputPanel;
    private javax.swing.JPanel systemScheme;
    private javax.swing.JMenuItem weightItem;
    // End of variables declaration//GEN-END:variables

}
