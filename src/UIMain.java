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
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * 
 * @author post-factum
 */
public class UIMain extends javax.swing.JFrame
{
    //UI parts
    SourceCoder currentSourceCoder = null;
    VideoCreator currentSourceVideoCreator = null;
    ChannelCoder currentChannelCoder = null;
    VideoCreator currentChannelVideoCreator = null;
    Modulator currentModulator = null;
    Channel currentChannel = null;
    EthalonGenerator currentEthalonGenerator0 = null, currentEthalonGenerator1 = null;
    Multiplier currentMultiplier0 = null, currentMultiplier1 = null;
    Integrator currentIntegrator0 = null, currentIntegrator1 = null;
    Summator currentSummator = null;
    Resolver currentResolver = null;
    VideoCreator currentResolverVideoCreator = null;
    ChannelDecoder currentChannelDecoder = null;
    VideoCreator currentChannelDecoderVideoCreator = null;
    SourceDecoder currentSourceDecoder = null;

    //UI blocks
    enum Blocks {MESSAGE_SOURCE, SOURCE_CODER, CHANNEL_CODER, MODULATOR, CHANNEL, ETHALON_GENERATOR0, ETHALON_GENERATOR1, MULTIPLIER0, MULTIPLIER1, INTEGRATOR0, INTEGRATOR1, SUMMATOR, RESOLVER, SOURCE_DECODER, CHANNEL_DECODER;};
    Blocks selectedBlock = Blocks.MESSAGE_SOURCE;

    //UI vizualization tools
    DataVizualizator currentSourceVideoSequenceVizualizator = null;
    DataVizualizator currentChannelVideoSequenceVizualizator = null;
    DataVizualizator currentModulatorVizualizator = null;
    DataVizualizator currentChannelVizualizator = null;
    DataVizualizator currentEthalonGeneratorVizualizator0 = null;
    DataVizualizator currentEthalonGeneratorVizualizator1 = null;
    DataVizualizator currentMultiplierVizualizator0 = null;
    DataVizualizator currentMultiplierVizualizator1 = null;
    DataVizualizator currentIntegratorVizualizator0 = null;
    DataVizualizator currentIntegratorVizualizator1 = null;
    DataVizualizator currentSummatorVizualizator = null;
    DataVizualizator currentResolverVideoSequenceVizualizator = null;
    DataVizualizator currentChannelDecoderVideoSequenceVizualizator = null;

    //source message
    String message = "";

    //simulating parameters
    SourceCoder.SourceCoderCode sourceCode = SourceCoder.SourceCoderCode.MTK2;
    ChannelCoder.ChannelCoderCode channelCode = ChannelCoder.ChannelCoderCode.PARITY_BIT;
    Modulator.ModulationType modulationType = Modulator.ModulationType.ASK;

    //source coder
    List<BinaryNumber> sourceSymbols = new ArrayList();
    List<Integer> lengthMap;
    boolean isCyr = true;

    //channel coder
    List<BinaryNumber> channelSymbols = new ArrayList();
    int headLength;
    boolean useChannelCoderTrigger = true;

    //source videosequence
    double sourceImpulseLength = 0;
    List<List<DigitalSignal>> sourceVideoSequence = null;
    List<DataVizualizatorProvider> sourceVideoSequenceSingleProvider = null;
    List<List<DataVizualizatorProvider>> sourceVideoSequenceProvider = null;

    //channel videosequence
    double channelImpulseLength = 0;
    List<List<DigitalSignal>> channelVideoSequence = null;
    List<List<DataVizualizatorProvider>> channelVideoSequenceProvider = null;

    //Modulator data
    List<List<ModulatorSignal>> modulatorData = null;
    List<List<DataVizualizatorProvider>> modulatorDataProvider = null;

    //Channel data
    List<List<ChannelSignal>> channelOutput = null;
    List<List<DataVizualizatorProvider>> channelOutputProvider = null;
    boolean useNoiseErrorsTrigger = true, forceErrorsTrigger = false, injectErrorsPerBlock = true;

    //ethalon generators data
    List<List<ModulatorSignal>> ethalonGenerator0Output = null;
    List<List<ModulatorSignal>> ethalonGenerator1Output = null;
    List<List<DataVizualizatorProvider>> ethalonGenerator0OutputProvider = null;
    List<List<DataVizualizatorProvider>> ethalonGenerator1OutputProvider = null;

    //multipliers data
    List<List<MultiplierSignal>> multiplier0Output = null;
    List<List<MultiplierSignal>> multiplier1Output = null;
    List<List<DataVizualizatorProvider>> multiplier0OutputProvider = null;
    List<List<DataVizualizatorProvider>> multiplier1OutputProvider = null;

    //integrators data
    double maxFrequency;
    List<List<DigitalSignal>> integrator0Output = null;
    List<List<DigitalSignal>> integrator1Output = null;
    List<List<DataVizualizatorProvider>> integrator0OutputProvider = null;
    List<List<DataVizualizatorProvider>> integrator1OutputProvider = null;

    //Summator data
    List<List<DigitalSignal>> summatorOutput = null;
    List<List<DataVizualizatorProvider>> summatorOutputProvider = null;

    //Resolver data
    List<BinaryNumber> resolverOutput = null;
    List<List<DigitalSignal>> resolverVideoSequence = null;
    List<List<DataVizualizatorProvider>> resolverVideoSequenceProvider = null;

    //Channel decoder data
    List<BinaryNumber> channelDecoderOutput = null;
    List<List<DigitalSignal>> channelDecoderVideoSequence = null;
    List<List<DataVizualizatorProvider>> channelDecoderVideoSequenceProvider = null;

    //acts on choosing code of source
    void updateChosenCodeSource()
    {
	switch (sourceCodesChooser.getSelectedIndex())
	{
	    case 0:
		sourceCode = SourceCoder.SourceCoderCode.MTK2;
		break;
	    case 1:
		sourceCode = SourceCoder.SourceCoderCode.MTK5;
		break;
	    case 2:
		sourceCode = SourceCoder.SourceCoderCode.KOI8U;
		break;
	    case 3:
		sourceCode = SourceCoder.SourceCoderCode.MORSE;
		break;
	    case 4:
		sourceCode = SourceCoder.SourceCoderCode.SHANNON;
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
		channelCode = ChannelCoder.ChannelCoderCode.PARITY_BIT;
		break;
	    case 1:
		channelCode = ChannelCoder.ChannelCoderCode.INVERSED;
		break;
	    case 2:
		channelCode = ChannelCoder.ChannelCoderCode.MANCHESTER;
		break;
	    case 3:
		channelCode = ChannelCoder.ChannelCoderCode.HAMMING;
		break;
	    default:
		break;
	}
    }

    //acts on choosing type of modulation
    void updateChosenModulationType()
    {
	//disable bearer frequency deviation options
	bearerFrequencyDeviation.setEnabled(false);
	bearerFrequencyDeviationLabel.setEnabled(false);
	switch (modulationTypeChooser.getSelectedIndex())
	{
	    case 0:
		modulationType = Modulator.ModulationType.ASK;
		break;
	    case 1:
		modulationType = Modulator.ModulationType.FSK;
		//enable bearer frequency deviation options
		bearerFrequencyDeviation.setEnabled(true);
		bearerFrequencyDeviationLabel.setEnabled(true);
		break;
	    case 2:
		modulationType = Modulator.ModulationType.PSK;
		break;
	    case 3:
		modulationType = Modulator.ModulationType.RPSK;
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
	ethalonGenerator0Button.setBackground(new Color(240, 240, 240));
	ethalonGenerator1Button.setBackground(new Color(240, 240, 240));
	multiplier0Button.setBackground(new Color(240, 240, 240));
	multiplier1Button.setBackground(new Color(240, 240, 240));
	integrator0Button.setBackground(new Color(240, 240, 240));
	integrator1Button.setBackground(new Color(240, 240, 240));
	summatorButton.setBackground(new Color(240, 240, 240));
	resolverButton.setBackground(new Color(240, 240, 240));
	channelDecoderButton.setBackground(new Color(240, 240, 240));
	sourceDecoderButton.setBackground(new Color(240, 240, 240));
	//highlight selected block
	switch (selectedBlock)
	{
	    case MESSAGE_SOURCE:
		TCSTabs.setSelectedComponent(blockMessageSource);
		messageSourceButton.setBackground(new Color(200, 200, 200));
		break;
	    case SOURCE_CODER:
		TCSTabs.setSelectedComponent(blockSourceCoder);
		sourceCoderButton.setBackground(new Color(200, 200, 200));
		break;
	    case CHANNEL_CODER:
		TCSTabs.setSelectedComponent(blockChannelCoder);
		channelCoderButton.setBackground(new Color(200, 200, 200));
		break;
	    case MODULATOR:
		TCSTabs.setSelectedComponent(blockModulator);
		modulatorButton.setBackground(new Color(200, 200, 200));
		break;
	    case CHANNEL:
		TCSTabs.setSelectedComponent(blockChannel);
		channelButton.setBackground(new Color(200, 200, 200));
		break;
	    case ETHALON_GENERATOR0:
		TCSTabs.setSelectedComponent(blockEthalonGenerator0);
		ethalonGenerator0Button.setBackground(new Color(200, 200, 200));
		break;
	    case ETHALON_GENERATOR1:
		TCSTabs.setSelectedComponent(blockEthalonGenerator1);
		ethalonGenerator1Button.setBackground(new Color(200, 200, 200));
		break;
	    case MULTIPLIER0:
		TCSTabs.setSelectedComponent(blockMultiplier0);
		multiplier0Button.setBackground(new Color(200, 200, 200));
		break;
	    case MULTIPLIER1:
		TCSTabs.setSelectedComponent(blockMultiplier1);
		multiplier1Button.setBackground(new Color(200, 200, 200));
		break;
	    case INTEGRATOR0:
		TCSTabs.setSelectedComponent(blockIntegrator0);
		integrator0Button.setBackground(new Color(200, 200, 200));
		break;
	    case INTEGRATOR1:
		TCSTabs.setSelectedComponent(blockIntegrator1);
		integrator1Button.setBackground(new Color(200, 200, 200));
		break;
	    case SUMMATOR:
		TCSTabs.setSelectedComponent(blockSummator);
		summatorButton.setBackground(new Color(200, 200, 200));
		break;
	    case RESOLVER:
		TCSTabs.setSelectedComponent(blockResolver);
		resolverButton.setBackground(new Color(200, 200, 200));
		break;
	    case CHANNEL_DECODER:
		TCSTabs.setSelectedComponent(blockChannelDecoder);
		channelDecoderButton.setBackground(new Color(200, 200, 200));
		break;
	    case SOURCE_DECODER:
		TCSTabs.setSelectedComponent(blockSourceDecoder);
		sourceDecoderButton.setBackground(new Color(200, 200, 200));
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
	sourceSymbols = currentSourceCoder.getSequence();
	lengthMap = currentSourceCoder.getLengthMap();
	isCyr = currentSourceCoder.isCyrillic();
	blockSourceCoderOutput.setText(currentSourceCoder.getStringSequence());
    }

    //encodes source code with selected Channel code
    void doChannelCoding()
    {
	currentChannelCoder = new ChannelCoder(sourceSymbols, channelCode, useChannelCoderTrigger);
	currentChannelCoder.doEncode();
	channelSymbols = currentChannelCoder.getSequence();
	blockChannelCoderOutput.setText(currentChannelCoder.getHTMLReport());
	headLength = currentChannelCoder.getHeadLength();
    }

    //shows source videosequence
    void doSourceCodingVideoSequence()
    {
	//calculates source impulse length according to chosen informational speed
	sourceImpulseLength = 1 / (Double)informationalSpeed.getValue();

	currentSourceVideoCreator = new VideoCreator(sourceSymbols, sourceImpulseLength, 1);
	currentSourceVideoCreator.doVideoSequence();
	sourceVideoSequence = currentSourceVideoCreator.getVideoSequence();
	if (currentSourceVideoSequenceVizualizator != null)
	{
	    blockSourceVideoSequenceOutputField.remove(currentSourceVideoSequenceVizualizator);
	    currentSourceVideoSequenceVizualizator = null;
	}
	sourceVideoSequenceProvider = new ArrayList<List<DataVizualizatorProvider>>();
	sourceVideoSequenceSingleProvider = (new DigitalVizualizatorConverter(sourceVideoSequence, "Вихідна відеопослідовність", Color.BLUE)).getProvided();
	sourceVideoSequenceProvider.add(sourceVideoSequenceSingleProvider);
	int cx = blockSourceVideoSequenceOutputField.getWidth();
	int cy = blockSourceVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentSourceVideoSequenceVizualizator = new DataVizualizator(sourceVideoSequenceProvider, cx, cy, "t, с", "Sv(t), В");

	//shows chart
	currentSourceVideoSequenceVizualizator.setVisible(true);

	blockSourceVideoSequenceOutputField.add(currentSourceVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentSourceVideoSequenceVizualizator.repaint();
    }

    //shows channel videosequence
    void doChannelCodingVideoSequence()
    {
	//calculates channel impulse length according to chosen code
	channelImpulseLength = sourceImpulseLength * ((double)currentSourceCoder.getSequenceLength() / (double)currentChannelCoder.getSequenceLength());

	currentChannelVideoCreator = new VideoCreator(channelSymbols, channelImpulseLength, 0.75);
	currentChannelVideoCreator.doVideoSequence();
	channelVideoSequence = currentChannelVideoCreator.getVideoSequence();
	if (currentChannelVideoSequenceVizualizator != null)
	{
	    blockChannelVideoSequenceOutputField.remove(currentChannelVideoSequenceVizualizator);
	    currentChannelVideoSequenceVizualizator = null;
	}
	channelVideoSequenceProvider = new ArrayList<List<DataVizualizatorProvider>>();
	channelVideoSequenceProvider.add(sourceVideoSequenceSingleProvider);
	channelVideoSequenceProvider.add((new DigitalVizualizatorConverter(channelVideoSequence, "Кодована відеопослідовність", Color.RED)).getProvided());
	int cx = blockChannelVideoSequenceOutputField.getWidth();
	int cy = blockChannelVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentChannelVideoSequenceVizualizator = new DataVizualizator(channelVideoSequenceProvider, cx, cy, "t, с", "Sv(t), В");

	//shows chart
	currentChannelVideoSequenceVizualizator.setVisible(true);
	blockChannelVideoSequenceOutputField.add(currentChannelVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentChannelVideoSequenceVizualizator.repaint();
    }

    //modulates sinusoidal bearer with channel code using selected modulation type
    void doModulating()
    {
	//gets modulator output signals
	currentModulator = new Modulator(modulationType, (Double)bearerAmplitude.getValue(), (Double)bearerFrequency.getValue(), (Double)bearerFrequencyDeviation.getValue(), channelSymbols, channelImpulseLength);
	currentModulator.doModulation();
	modulatorData = currentModulator.getSignals();

	//removes old vizualizator if exists
	if (currentModulatorVizualizator != null)
	{
	    modulatorOutputField.remove(currentModulatorVizualizator);
	    currentModulatorVizualizator = null;
	}
	//creates new vizualizator data provider
	modulatorDataProvider = new ArrayList<List<DataVizualizatorProvider>>();
	modulatorDataProvider.add((new ModulatorVizualizatorConverter(modulatorData, "Сигнал на виході модулятора", Color.BLUE)).getProvided());
	//gets chart width and height
	int cx = modulatorOutputField.getWidth();
	int cy = modulatorOutputField.getHeight();
	//creates new vizualizator
	currentModulatorVizualizator = new DataVizualizator(modulatorDataProvider, cx, cy, "t, с", "S(t), В");
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
	currentChannel = new Channel(modulatorData, (Double)noisePower.getValue());
	currentChannel.doNoising();
	channelOutput = currentChannel.getSignals();

	//removes old vizualizator if exists
	if (currentChannelVizualizator != null)
	{
	    channelOutputField.remove(currentChannelVizualizator);
	    currentChannelVizualizator = null;
	}
	//creates new vizualizator data provider
	channelOutputProvider = new ArrayList<List<DataVizualizatorProvider>>();
	channelOutputProvider.add((new ChannelVizualizatorConverter(channelOutput, "Сигнал на виході каналу", Color.BLUE)).getProvided());
	//gets chart width and height
	int cx = channelOutputField.getWidth();
	int cy = channelOutputField.getHeight();
	//creates new vizualizator
	currentChannelVizualizator = new DataVizualizator(channelOutputProvider, cx, cy, "t, с", "S'(t), В");

	//shows chart
	currentChannelVizualizator.setVisible(true);
	channelOutputField.add(currentChannelVizualizator);

	//repaints chart to show it if MODULATOR block is active
	currentChannelVizualizator.repaint();
    }

    //generates ethalon signals
    void doGenerating()
    {
	switch (modulationType)
	{
	    case ASK:
		maxFrequency = (Double)bearerFrequency.getValue();
		currentEthalonGenerator0 = new EthalonGenerator(maxFrequency, 0, 0, channelOutput);
		currentEthalonGenerator1 = new EthalonGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), 0, channelOutput);
		break;
	    case FSK:
		maxFrequency = (Double)bearerFrequency.getValue() + (Double)bearerFrequencyDeviation.getValue();
		currentEthalonGenerator0 = new EthalonGenerator(maxFrequency - 2 * (Double)bearerFrequencyDeviation.getValue(), (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentEthalonGenerator1 = new EthalonGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), 0, channelOutput);
		break;
	    case PSK:
		maxFrequency = (Double)bearerFrequency.getValue();
		currentEthalonGenerator0 = new EthalonGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentEthalonGenerator1 = new EthalonGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), -Math.PI, channelOutput);
		break;
	    case RPSK:
		maxFrequency = (Double)bearerFrequency.getValue();
		currentEthalonGenerator0 = new EthalonGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentEthalonGenerator1 = new EthalonGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), -Math.PI, channelOutput);
		break;
	    default:
		break;
	}
	currentEthalonGenerator0.generate();
	currentEthalonGenerator1.generate();
	ethalonGenerator0Output = currentEthalonGenerator0.getSignals();
	ethalonGenerator1Output = currentEthalonGenerator1.getSignals();

	//prepares vizualizator
	if (currentEthalonGeneratorVizualizator0 != null)
	{
	    ethalonGeneratorOutputField0.remove(currentEthalonGeneratorVizualizator0);
	    currentEthalonGeneratorVizualizator0 = null;
	}
	int cx0 = ethalonGeneratorOutputField0.getWidth();
	int cy0 = ethalonGeneratorOutputField0.getHeight();

	//vizualizes signal
	ethalonGenerator0OutputProvider = new ArrayList<List<DataVizualizatorProvider>>();
	ethalonGenerator0OutputProvider.add((new ModulatorVizualizatorConverter(ethalonGenerator0Output, "Сигнал на виході 0-го опорного генератора", Color.BLUE)).getProvided());
	currentEthalonGeneratorVizualizator0 = new DataVizualizator(ethalonGenerator0OutputProvider, cx0, cy0, "t, с", "So0(t), В");
	currentEthalonGeneratorVizualizator0.setVisible(true);
	ethalonGeneratorOutputField0.add(currentEthalonGeneratorVizualizator0);
	currentEthalonGeneratorVizualizator0.repaint();

	//does the same for second generator
	if (currentEthalonGeneratorVizualizator1 != null)
	{
	    ethalonGeneratorOutputField1.remove(currentEthalonGeneratorVizualizator1);
	    currentEthalonGeneratorVizualizator1 = null;
	}
	int cx1 = ethalonGeneratorOutputField1.getWidth();
	int cy1 = ethalonGeneratorOutputField1.getHeight();

	//shows multipliers charts
	ethalonGenerator1OutputProvider = new ArrayList<List<DataVizualizatorProvider>>();
	ethalonGenerator1OutputProvider.add((new ModulatorVizualizatorConverter(ethalonGenerator1Output, "Сигнал на виході 1-го опорного генератора", Color.BLUE)).getProvided());
	currentEthalonGeneratorVizualizator1 = new DataVizualizator(ethalonGenerator1OutputProvider, cx1, cy1, "t, с", "So1(t), В");
	currentEthalonGeneratorVizualizator1.setVisible(true);
	ethalonGeneratorOutputField1.add(currentEthalonGeneratorVizualizator1);
	currentEthalonGeneratorVizualizator1.repaint();
    }

    //multiplies signals and ethalons
    void doMultiplying()
    {
	//create multipliers
	currentMultiplier0 = new Multiplier(channelOutput, ethalonGenerator0Output);
	currentMultiplier1 = new Multiplier(channelOutput, ethalonGenerator1Output);

	//multiplies
	currentMultiplier0.doMultiply();
	currentMultiplier1.doMultiply();
	multiplier0Output = currentMultiplier0.getSignals();
	multiplier1Output = currentMultiplier1.getSignals();

	//prepares vizualizator
	if (currentMultiplierVizualizator0 != null)
	{
	    multiplierOutputField0.remove(currentMultiplierVizualizator0);
	    currentMultiplierVizualizator0 = null;
	}
	int cx0 = multiplierOutputField0.getWidth();
	int cy0 = multiplierOutputField0.getHeight();

	//vizualizes signal
	multiplier0OutputProvider = new ArrayList<List<DataVizualizatorProvider>>();
	multiplier0OutputProvider.add((new MultiplierVizualizatorConverter(multiplier0Output, "Сигнал на виході 0-го помножувача", Color.BLUE)).getProvided());
	currentMultiplierVizualizator0 = new DataVizualizator(multiplier0OutputProvider, cx0, cy0, "t, с", "Sm0(t), В");
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

	//shows multipliers charts
	multiplier1OutputProvider = new ArrayList<List<DataVizualizatorProvider>>();
	multiplier1OutputProvider.add((new MultiplierVizualizatorConverter(multiplier1Output, "Сигнал на виході 1-го помножувача", Color.BLUE)).getProvided());
	currentMultiplierVizualizator1 = new DataVizualizator(multiplier1OutputProvider, cx1, cy1, "t, с", "Sm1(t), В");
	currentMultiplierVizualizator1.setVisible(true);
	multiplierOutputField1.add(currentMultiplierVizualizator1);
	currentMultiplierVizualizator1.repaint();
    }

    //integrates signals from multipliers
    void doIntegrating()
    {
	//integrates multipliers output
	currentIntegrator0 = new Integrator(multiplier0Output, maxFrequency, integratorOutputField0.getWidth());
	currentIntegrator1 = new Integrator(multiplier1Output, maxFrequency, integratorOutputField0.getWidth());
	currentIntegrator0.doIntegrating();
	currentIntegrator1.doIntegrating();
	integrator0Output = currentIntegrator0.getIntegrals();
	integrator1Output = currentIntegrator1.getIntegrals();

	//shows zero integrator chart
	if (currentIntegratorVizualizator0 != null)
	{
	    integratorOutputField0.remove(currentIntegratorVizualizator0);
	    currentIntegratorVizualizator0 = null;
	}
	int cx0 = integratorOutputField0.getWidth();
	int cy0 = integratorOutputField0.getHeight();

	integrator0OutputProvider = new ArrayList<List<DataVizualizatorProvider>>();
	integrator0OutputProvider.add((new DigitalVizualizatorConverter(integrator0Output, "Сигнал на виході 0-го інтегратора", Color.BLUE)).getProvided());
	currentIntegratorVizualizator0 = new DataVizualizator(integrator0OutputProvider, cx0, cy0, "t, с", "Si0(t), В");
	currentIntegratorVizualizator0.setVisible(true);
	integratorOutputField0.add(currentIntegratorVizualizator0);
	currentIntegratorVizualizator0.repaint();

	//shows first integrator chart
	if (currentIntegratorVizualizator1 != null)
	{
	    integratorOutputField1.remove(currentIntegratorVizualizator1);
	    currentIntegratorVizualizator1 = null;
	}
	int cx1 = integratorOutputField1.getWidth();
	int cy1 = integratorOutputField1.getHeight();

	integrator1OutputProvider = new ArrayList<List<DataVizualizatorProvider>>();
	integrator1OutputProvider.add((new DigitalVizualizatorConverter(integrator1Output, "Сигнал на виході 1-го інтегратора", Color.BLUE)).getProvided());
	currentIntegratorVizualizator1 = new DataVizualizator(integrator1OutputProvider, cx1, cy1, "t, с", "Si1(t), В");
	currentIntegratorVizualizator1.setVisible(true);
	integratorOutputField1.add(currentIntegratorVizualizator1);
	currentIntegratorVizualizator1.repaint();
    }

    //sums signals from integrators
    void doSumming()
    {
	//gets sum from two integrators
	currentSummator = new Summator(integrator0Output, integrator1Output);
	currentSummator.doSumming();
	summatorOutput = currentSummator.getSum();

	//shows chart
	if (currentSummatorVizualizator != null)
	{
	    blockSummatorOutputField.remove(currentSummatorVizualizator);
	    currentSummatorVizualizator = null;
	}
	int cx1 = blockSummatorOutputField.getWidth();
	int cy1 = blockSummatorOutputField.getHeight();

	summatorOutputProvider = new ArrayList<List<DataVizualizatorProvider>>();
	summatorOutputProvider.add((new DigitalVizualizatorConverter(summatorOutput, "Сигнал на виході суматора", Color.BLUE)).getProvided());
	currentSummatorVizualizator = new DataVizualizator(summatorOutputProvider, cx1, cy1, "t, с", "Ssum(t), В");
	currentSummatorVizualizator.setVisible(true);
	blockSummatorOutputField.add(currentSummatorVizualizator);
	currentSummatorVizualizator.repaint();
    }

    void doResolving()
    {
	double threshold;
	switch (modulationType)
	{
	    case ASK:
	    double w = 2 * Math.PI * (Double)bearerFrequency.getValue();
	    threshold = -0.25 * (Math.pow((Double)bearerAmplitude.getValue(), 2) * (Math.cos(w * channelImpulseLength) * Math.sin(w * channelImpulseLength) - w * channelImpulseLength)) / w;
	    break;
	case FSK:
	    threshold = 0;
	    break;
	case PSK:
	    threshold = 0;
	    break;
	case RPSK:
	    threshold = 0;
	    break;
	default:
	    threshold = 0;
	    break;
	}

	int errorsCount = 0;
	if (forceErrorsTrigger)
	    errorsCount = (Integer) forceErrorsCount.getValue();
	currentResolver = new Resolver(summatorOutput, threshold, modulationType, useNoiseErrorsTrigger, forceErrorsTrigger, errorsCount, injectErrorsPerBlock, channelSymbols);
	currentResolver.doResolving();
	resolverOutput = currentResolver.getBinaryNumbers();
	blockResolverOutput.setText("<html>" + currentResolver.getStringSequence());

	currentResolverVideoCreator = new VideoCreator(resolverOutput, channelImpulseLength, 1);
	currentResolverVideoCreator.doVideoSequence();
	resolverVideoSequence = currentResolverVideoCreator.getVideoSequence();
	if (currentResolverVideoSequenceVizualizator != null)
	{
	    blockResolverVideoSequenceOutputField.remove(currentResolverVideoSequenceVizualizator);
	    currentResolverVideoSequenceVizualizator = null;
	}
	resolverVideoSequenceProvider = new ArrayList<List<DataVizualizatorProvider>>();
	resolverVideoSequenceProvider.add((new DigitalVizualizatorConverter(resolverVideoSequence, "Вхідна відеопослідовність", Color.RED)).getProvided());
	int cx = blockResolverVideoSequenceOutputField.getWidth();
	int cy = blockResolverVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentResolverVideoSequenceVizualizator = new DataVizualizator(resolverVideoSequenceProvider, cx, cy, "t, с", "Sv(t), В");

	//shows chart
	currentResolverVideoSequenceVizualizator.setVisible(true);
	blockResolverVideoSequenceOutputField.add(currentResolverVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentResolverVideoSequenceVizualizator.repaint();
    }

    //decodes source code with selected Channel code
    void doChannelDecoding()
    {
	currentChannelDecoder = new ChannelDecoder(resolverOutput, channelCode, headLength, lengthMap, useChannelCoderTrigger);
	currentChannelDecoder.doDecode();
	channelDecoderOutput = currentChannelDecoder.getSequence();
	String text = "<html>Отримана послідовність: <br/>" + currentResolver.getStringSequence() + "<br/>";
	text += currentChannelDecoder.getHTMLReport();
	blockChannelDecoderOutput.setText(text);
    }

    //decodes source code with selected Channel code
    void doChannelDecodingVideoSequence()
    {
	currentChannelDecoderVideoCreator = new VideoCreator(channelDecoderOutput, sourceImpulseLength, 1);
	currentChannelDecoderVideoCreator.doVideoSequence();
	channelDecoderVideoSequence = currentChannelDecoderVideoCreator.getVideoSequence();
	if (currentChannelDecoderVideoSequenceVizualizator != null)
	{
	    blockChannelDecoderVideoSequenceOutputField.remove(currentChannelDecoderVideoSequenceVizualizator);
	    currentChannelDecoderVideoSequenceVizualizator = null;
	}
	channelDecoderVideoSequenceProvider = new ArrayList<List<DataVizualizatorProvider>>();
	channelDecoderVideoSequenceProvider.add((new DigitalVizualizatorConverter(channelDecoderVideoSequence, "Декодована відеопослідовність", Color.RED)).getProvided());
	int cx = blockChannelDecoderVideoSequenceOutputField.getWidth();
	int cy = blockChannelDecoderVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentChannelDecoderVideoSequenceVizualizator = new DataVizualizator(channelDecoderVideoSequenceProvider, cx, cy, "t, с", "Sv(t), В");

	//shows chart
	currentChannelDecoderVideoSequenceVizualizator.setVisible(true);
	blockChannelDecoderVideoSequenceOutputField.add(currentChannelDecoderVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentChannelDecoderVideoSequenceVizualizator.repaint();
    }

    void doSourceDecoding()
    {
	currentSourceDecoder = new SourceDecoder(channelDecoderOutput, sourceCode, isCyr);
	currentSourceDecoder.doDecode();
	receivedMessageArea.setText(currentSourceDecoder.getMessage());
    }

    /**
     * 
     */
    public UIMain()
    {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        aboutDialog = new javax.swing.JDialog();
        aboutDialogClose = new javax.swing.JButton();
        programNameLabel = new javax.swing.JLabel();
        instituteNameLabel = new javax.swing.JLabel();
        copyrightLabel = new javax.swing.JLabel();
        licenseLabel = new javax.swing.JLabel();
        showLicense = new javax.swing.JButton();
        modellingOptionsDialog = new javax.swing.JDialog();
        optionsTabs = new javax.swing.JTabbedPane();
        sourceCoderTab = new javax.swing.JPanel();
        sourceCodesChooser = new javax.swing.JComboBox();
        sourceCodesChooserLabel = new javax.swing.JLabel();
        informationalSpeedLabel = new javax.swing.JLabel();
        informationalSpeed = new javax.swing.JSpinner();
        bpsLabel = new javax.swing.JLabel();
        channelCoderTab = new javax.swing.JPanel();
        channelCodesChooserLabel = new javax.swing.JLabel();
        channelCodesChooser = new javax.swing.JComboBox();
        useChannelCoder = new javax.swing.JCheckBox();
        modulatorTab = new javax.swing.JPanel();
        modulationTypeLabel = new javax.swing.JLabel();
        modulationTypeChooser = new javax.swing.JComboBox();
        bearerAmplitudeLabel = new javax.swing.JLabel();
        bearerAmplitude = new javax.swing.JSpinner();
        bearerFrequencyDeviationLabel = new javax.swing.JLabel();
        bearerFrequencyLabel = new javax.swing.JLabel();
        bearerFrequencyDeviation = new javax.swing.JSpinner();
        bearerFrequency = new javax.swing.JSpinner();
        voltsLabel = new javax.swing.JLabel();
        hzBearerLabel = new javax.swing.JLabel();
        hzDeviationLabel = new javax.swing.JLabel();
        channelTab = new javax.swing.JPanel();
        noisePowerLabel = new javax.swing.JLabel();
        noisePower = new javax.swing.JSpinner();
        noisePowerWattLabel = new javax.swing.JLabel();
        useNoiseErrors = new javax.swing.JCheckBox();
        forceErrors = new javax.swing.JCheckBox();
        forceErrorsCount = new javax.swing.JSpinner();
        errorsInjectorPerBlock = new javax.swing.JRadioButton();
        errorsInjectorPerSequence = new javax.swing.JRadioButton();
        errorsInjectorTypeChooserGroup = new javax.swing.ButtonGroup();
        licenseDialog = new javax.swing.JDialog();
        jScrollPane7 = new javax.swing.JScrollPane();
        licenseTextArea = new javax.swing.JTextArea();
        closeLicenseWindowButton = new javax.swing.JButton();
        TCSTabs = new javax.swing.JTabbedPane();
        blockMessageSource = new javax.swing.JPanel();
        sourceMessagePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        blockMessageArea = new javax.swing.JTextArea();
        blockSourceCoder = new javax.swing.JPanel();
        blockSourceCoderTabs = new javax.swing.JTabbedPane();
        blockSourceCoderBinarySequence = new javax.swing.JPanel();
        blockSourceCoderOutputPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        blockSourceCoderOutput = new javax.swing.JTextPane();
        blockSourceCoderVideoSequence = new javax.swing.JPanel();
        blockSourceVideoSequenceOutputField = new javax.swing.JPanel();
        blockChannelCoder = new javax.swing.JPanel();
        blockChannelCoderTabs = new javax.swing.JTabbedPane();
        blockChannelCoderBinarySequence = new javax.swing.JPanel();
        blockChannelCoderOutputPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        blockChannelCoderOutput = new javax.swing.JTextPane();
        blockChannelCoderVideoSequence = new javax.swing.JPanel();
        blockChannelVideoSequenceOutputField = new javax.swing.JPanel();
        blockModulator = new javax.swing.JPanel();
        modulatorOutputPanel = new javax.swing.JPanel();
        modulatorOutputField = new javax.swing.JPanel();
        blockChannel = new javax.swing.JPanel();
        channelOutputPanel = new javax.swing.JPanel();
        channelOutputField = new javax.swing.JPanel();
        blockEthalonGenerator0 = new javax.swing.JPanel();
        ethalonGeneratorOutputPanel0 = new javax.swing.JPanel();
        ethalonGeneratorOutputField0 = new javax.swing.JPanel();
        blockMultiplier0 = new javax.swing.JPanel();
        multiplierOutputPanel0 = new javax.swing.JPanel();
        multiplierOutputField0 = new javax.swing.JPanel();
        blockEthalonGenerator1 = new javax.swing.JPanel();
        ethalonGeneratorOutputPanel1 = new javax.swing.JPanel();
        ethalonGeneratorOutputField1 = new javax.swing.JPanel();
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
        blockSummatorOutputPanel = new javax.swing.JPanel();
        blockSummatorOutputField = new javax.swing.JPanel();
        blockResolver = new javax.swing.JPanel();
        blockResolverTabs = new javax.swing.JTabbedPane();
        blockResolverBinarySequence = new javax.swing.JPanel();
        blockResolverOutputPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        blockResolverOutput = new javax.swing.JTextPane();
        blockResolverVideoSequence = new javax.swing.JPanel();
        blockResolverVideoSequenceOutputField = new javax.swing.JPanel();
        blockChannelDecoder = new javax.swing.JPanel();
        blockChannelDecoderTabs = new javax.swing.JTabbedPane();
        blockChannelDecoderBinarySequence = new javax.swing.JPanel();
        blockChannelDecoderOutputPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        blockChannelDecoderOutput = new javax.swing.JTextPane();
        blockChannelDecoderVideoSequence = new javax.swing.JPanel();
        blockChannelDecoderVideoSequenceOutputField = new javax.swing.JPanel();
        blockSourceDecoder = new javax.swing.JPanel();
        sourceDecoderPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        receivedMessageArea = new javax.swing.JTextArea();
        systemScheme = new javax.swing.JPanel();
        channelCoderButton = new javax.swing.JButton();
        modulatorButton = new javax.swing.JButton();
        channelButton = new javax.swing.JButton();
        integrator0Button = new javax.swing.JButton();
        sourceCoderButton = new javax.swing.JButton();
        multiplier0Button = new javax.swing.JButton();
        multiplier1Button = new javax.swing.JButton();
        messageSourceButton = new javax.swing.JButton();
        integrator1Button = new javax.swing.JButton();
        summatorButton = new javax.swing.JButton();
        drArrowLabel = new javax.swing.JLabel();
        leftTripleLabel = new javax.swing.JLabel();
        dlArrowLabel = new javax.swing.JLabel();
        ulArrowLabel = new javax.swing.JLabel();
        urArrowLabel = new javax.swing.JLabel();
        rightTripleLabel = new javax.swing.JLabel();
        rightArrowLabel2 = new javax.swing.JLabel();
        rightArrowLabel3 = new javax.swing.JLabel();
        rightArrowLabel4 = new javax.swing.JLabel();
        rightArrowLabel6 = new javax.swing.JLabel();
        rightArrowLabel1 = new javax.swing.JLabel();
        resolverButton = new javax.swing.JButton();
        rightArrowLabel7 = new javax.swing.JLabel();
        rightArrowLabel8 = new javax.swing.JLabel();
        rightArrowLabel9 = new javax.swing.JLabel();
        upArrowLabel = new javax.swing.JLabel();
        channelDecoderButton = new javax.swing.JButton();
        sourceDecoderButton = new javax.swing.JButton();
        ethalonGenerator0Button = new javax.swing.JButton();
        ethalonGenerator1Button = new javax.swing.JButton();
        downArrowLabel = new javax.swing.JLabel();
        rightArrowLabel12 = new javax.swing.JLabel();
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitItem = new javax.swing.JMenuItem();
        modellingMenu = new javax.swing.JMenu();
        doModellingOptionsItem = new javax.swing.JMenuItem();
        doModellingItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutItem = new javax.swing.JMenuItem();

        aboutDialog.setTitle("Про програму");
        aboutDialog.setResizable(false);

        aboutDialogClose.setText("Закрити");
        aboutDialogClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutDialogCloseActionPerformed(evt);
            }
        });

        programNameLabel.setText("Модель цифрової системи зв'язку");

        instituteNameLabel.setText("НД ІТС НТУУ «КПІ»");

        copyrightLabel.setText("© 2009-2010, Олександр Ігорович Наталенко");

        licenseLabel.setText("Програма розповсюджується згідно умовам ліцензії UPLv4");

        showLicense.setText("Показати ліцензію");
        showLicense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showLicenseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutDialogLayout.createSequentialGroup()
                .addContainerGap(312, Short.MAX_VALUE)
                .addComponent(aboutDialogClose))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(programNameLabel)
                .addContainerGap(170, Short.MAX_VALUE))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(instituteNameLabel)
                .addContainerGap(265, Short.MAX_VALUE))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(copyrightLabel)
                .addContainerGap(92, Short.MAX_VALUE))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(licenseLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showLicense)
                .addContainerGap(234, Short.MAX_VALUE))
        );
        aboutDialogLayout.setVerticalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(programNameLabel)
                .addGap(18, 18, 18)
                .addComponent(instituteNameLabel)
                .addGap(18, 18, 18)
                .addComponent(copyrightLabel)
                .addGap(18, 18, 18)
                .addComponent(licenseLabel)
                .addGap(18, 18, 18)
                .addComponent(showLicense)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aboutDialogClose))
        );

        modellingOptionsDialog.setTitle("Налаштування моделювання");
        modellingOptionsDialog.setResizable(false);

        sourceCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "МТК-2", "МТК-5", "KOI8-U", "Морзе", "Шенона-Фано" }));
        sourceCodesChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sourceCodesChooserItemStateChanged(evt);
            }
        });

        sourceCodesChooserLabel.setLabelFor(sourceCodesChooser);
        sourceCodesChooserLabel.setText("Код:");

        informationalSpeedLabel.setText("Інформаційна швидкість:");

        informationalSpeed.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(166666.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        bpsLabel.setText("біт/с");

        javax.swing.GroupLayout sourceCoderTabLayout = new javax.swing.GroupLayout(sourceCoderTab);
        sourceCoderTab.setLayout(sourceCoderTabLayout);
        sourceCoderTabLayout.setHorizontalGroup(
            sourceCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourceCoderTabLayout.createSequentialGroup()
                .addGroup(sourceCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(informationalSpeedLabel)
                    .addComponent(sourceCodesChooserLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sourceCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourceCodesChooser, 0, 481, Short.MAX_VALUE)
                    .addGroup(sourceCoderTabLayout.createSequentialGroup()
                        .addComponent(informationalSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bpsLabel)
                        .addGap(267, 267, 267))))
        );
        sourceCoderTabLayout.setVerticalGroup(
            sourceCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourceCoderTabLayout.createSequentialGroup()
                .addGroup(sourceCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourceCodesChooserLabel)
                    .addComponent(sourceCodesChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sourceCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(informationalSpeedLabel)
                    .addComponent(informationalSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bpsLabel))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        optionsTabs.addTab("Кодер джерела", sourceCoderTab);

        channelCodesChooserLabel.setLabelFor(channelCodesChooser);
        channelCodesChooserLabel.setText("Код:");

        channelCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "із перевіркою на парність", "інверсний", "манчестерський", "Хемінга" }));
        channelCodesChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                channelCodesChooserItemStateChanged(evt);
            }
        });

        useChannelCoder.setSelected(true);
        useChannelCoder.setText("Використовувати надлишкове кодування");
        useChannelCoder.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useChannelCoderItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout channelCoderTabLayout = new javax.swing.GroupLayout(channelCoderTab);
        channelCoderTab.setLayout(channelCoderTabLayout);
        channelCoderTabLayout.setHorizontalGroup(
            channelCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelCoderTabLayout.createSequentialGroup()
                .addGroup(channelCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(channelCoderTabLayout.createSequentialGroup()
                        .addComponent(channelCodesChooserLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(channelCodesChooser, 0, 602, Short.MAX_VALUE))
                    .addComponent(useChannelCoder))
                .addContainerGap())
        );
        channelCoderTabLayout.setVerticalGroup(
            channelCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelCoderTabLayout.createSequentialGroup()
                .addComponent(useChannelCoder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(channelCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(channelCodesChooserLabel)
                    .addComponent(channelCodesChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        optionsTabs.addTab("Кодер каналу", channelCoderTab);

        modulationTypeLabel.setLabelFor(modulationTypeChooser);
        modulationTypeLabel.setText("Вид модуляції:");

        modulationTypeChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "АМн (амплітудна маніпуляція)", "ЧМн (частотна маніпуляція)", "ФМн (фазова маніпуляція)", "ВФМн (відносна фазова маніпуляція)" }));
        modulationTypeChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                modulationTypeChooserItemStateChanged(evt);
            }
        });

        bearerAmplitudeLabel.setText("Амплітуда несучої:");

        bearerAmplitude.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(10.0d), Double.valueOf(0.0d), null, Double.valueOf(0.5d)));

        bearerFrequencyDeviationLabel.setText("Девіація частоти:");
        bearerFrequencyDeviationLabel.setEnabled(false);

        bearerFrequencyLabel.setText("Частота несучої:");

        bearerFrequencyDeviation.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(20000.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        bearerFrequencyDeviation.setEnabled(false);

        bearerFrequency.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(200000.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        voltsLabel.setText("В");

        hzBearerLabel.setText("Гц");

        hzDeviationLabel.setText("Гц");

        javax.swing.GroupLayout modulatorTabLayout = new javax.swing.GroupLayout(modulatorTab);
        modulatorTab.setLayout(modulatorTabLayout);
        modulatorTabLayout.setHorizontalGroup(
            modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modulatorTabLayout.createSequentialGroup()
                .addGroup(modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bearerAmplitudeLabel)
                    .addComponent(modulationTypeLabel)
                    .addComponent(bearerFrequencyDeviationLabel)
                    .addComponent(bearerFrequencyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(modulatorTabLayout.createSequentialGroup()
                        .addGroup(modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bearerFrequencyDeviation)
                            .addComponent(bearerFrequency)
                            .addComponent(bearerAmplitude, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hzDeviationLabel)
                            .addComponent(hzBearerLabel)
                            .addComponent(voltsLabel)))
                    .addComponent(modulationTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        modulatorTabLayout.setVerticalGroup(
            modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modulatorTabLayout.createSequentialGroup()
                .addGroup(modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modulationTypeLabel)
                    .addComponent(modulationTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bearerAmplitudeLabel)
                    .addComponent(bearerAmplitude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(voltsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bearerFrequencyLabel)
                    .addComponent(bearerFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hzBearerLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modulatorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bearerFrequencyDeviationLabel)
                    .addComponent(bearerFrequencyDeviation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hzDeviationLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        optionsTabs.addTab("Модулятор", modulatorTab);

        noisePowerLabel.setText("Максимальна потужність шуму:");

        noisePower.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.0d), null, Double.valueOf(0.1d)));

        noisePowerWattLabel.setText("Вт");

        useNoiseErrors.setSelected(true);
        useNoiseErrors.setText("Вносити помилки внаслідок дії шуму");
        useNoiseErrors.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useNoiseErrorsItemStateChanged(evt);
            }
        });

        forceErrors.setText("Примусово вносити помилку кратністю");
        forceErrors.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                forceErrorsItemStateChanged(evt);
            }
        });

        forceErrorsCount.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        forceErrorsCount.setEnabled(false);

        errorsInjectorTypeChooserGroup.add(errorsInjectorPerBlock);
        errorsInjectorPerBlock.setSelected(true);
        errorsInjectorPerBlock.setText("поблоково");
        errorsInjectorPerBlock.setEnabled(false);
        errorsInjectorPerBlock.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                errorsInjectorPerBlockItemStateChanged(evt);
            }
        });

        errorsInjectorTypeChooserGroup.add(errorsInjectorPerSequence);
        errorsInjectorPerSequence.setText("у всю послідовність");
        errorsInjectorPerSequence.setEnabled(false);
        errorsInjectorPerSequence.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                errorsInjectorPerSequenceItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout channelTabLayout = new javax.swing.GroupLayout(channelTab);
        channelTab.setLayout(channelTabLayout);
        channelTabLayout.setHorizontalGroup(
            channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelTabLayout.createSequentialGroup()
                .addGroup(channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(channelTabLayout.createSequentialGroup()
                        .addComponent(noisePowerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noisePower, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noisePowerWattLabel))
                    .addComponent(useNoiseErrors)
                    .addGroup(channelTabLayout.createSequentialGroup()
                        .addComponent(forceErrors)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(forceErrorsCount)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(errorsInjectorPerSequence)
                    .addComponent(errorsInjectorPerBlock))
                .addContainerGap(154, Short.MAX_VALUE))
        );
        channelTabLayout.setVerticalGroup(
            channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelTabLayout.createSequentialGroup()
                .addGroup(channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noisePowerLabel)
                    .addComponent(noisePower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noisePowerWattLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(useNoiseErrors)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forceErrors)
                    .addComponent(forceErrorsCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(errorsInjectorPerBlock))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorsInjectorPerSequence)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        optionsTabs.addTab("Канал", channelTab);

        javax.swing.GroupLayout modellingOptionsDialogLayout = new javax.swing.GroupLayout(modellingOptionsDialog.getContentPane());
        modellingOptionsDialog.getContentPane().setLayout(modellingOptionsDialogLayout);
        modellingOptionsDialogLayout.setHorizontalGroup(
            modellingOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(optionsTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
        );
        modellingOptionsDialogLayout.setVerticalGroup(
            modellingOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(optionsTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
        );

        licenseDialog.setTitle("Ліцензія");
        licenseDialog.setResizable(false);

        licenseTextArea.setColumns(20);
        licenseTextArea.setEditable(false);
        licenseTextArea.setRows(5);
        jScrollPane7.setViewportView(licenseTextArea);

        closeLicenseWindowButton.setText("Закрити");
        closeLicenseWindowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeLicenseWindowButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout licenseDialogLayout = new javax.swing.GroupLayout(licenseDialog.getContentPane());
        licenseDialog.getContentPane().setLayout(licenseDialogLayout);
        licenseDialogLayout.setHorizontalGroup(
            licenseDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(licenseDialogLayout.createSequentialGroup()
                .addContainerGap(650, Short.MAX_VALUE)
                .addComponent(closeLicenseWindowButton))
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
        );
        licenseDialogLayout.setVerticalGroup(
            licenseDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, licenseDialogLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeLicenseWindowButton))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Система зв'язку");

        TCSTabs.setFont(new java.awt.Font("Dialog", 1, 16));

        blockMessageSource.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMessageSourceComponentShown(evt);
            }
        });

        sourceMessagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Повідомлення"));

        blockMessageArea.setColumns(20);
        blockMessageArea.setFont(new java.awt.Font("Dialog", 0, 24));
        blockMessageArea.setRows(5);
        blockMessageArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(blockMessageArea);

        javax.swing.GroupLayout sourceMessagePanelLayout = new javax.swing.GroupLayout(sourceMessagePanel);
        sourceMessagePanel.setLayout(sourceMessagePanelLayout);
        sourceMessagePanelLayout.setHorizontalGroup(
            sourceMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        sourceMessagePanelLayout.setVerticalGroup(
            sourceMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
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

        blockSourceCoderOutput.setContentType("text/html");
        blockSourceCoderOutput.setEditable(false);
        blockSourceCoderOutput.setFont(new java.awt.Font("Dialog", 0, 24));
        jScrollPane2.setViewportView(blockSourceCoderOutput);

        javax.swing.GroupLayout blockSourceCoderOutputPanelLayout = new javax.swing.GroupLayout(blockSourceCoderOutputPanel);
        blockSourceCoderOutputPanel.setLayout(blockSourceCoderOutputPanelLayout);
        blockSourceCoderOutputPanelLayout.setHorizontalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockSourceCoderOutputPanelLayout.setVerticalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceCoderBinarySequenceLayout = new javax.swing.GroupLayout(blockSourceCoderBinarySequence);
        blockSourceCoderBinarySequence.setLayout(blockSourceCoderBinarySequenceLayout);
        blockSourceCoderBinarySequenceLayout.setHorizontalGroup(
            blockSourceCoderBinarySequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceCoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSourceCoderBinarySequenceLayout.setVerticalGroup(
            blockSourceCoderBinarySequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceCoderOutputPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        blockSourceCoderTabs.addTab("Бінарна послідовність", blockSourceCoderBinarySequence);

        blockSourceVideoSequenceOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockSourceCoderVideoSequenceLayout = new javax.swing.GroupLayout(blockSourceCoderVideoSequence);
        blockSourceCoderVideoSequence.setLayout(blockSourceCoderVideoSequenceLayout);
        blockSourceCoderVideoSequenceLayout.setHorizontalGroup(
            blockSourceCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockSourceCoderVideoSequenceLayout.setVerticalGroup(
            blockSourceCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        blockSourceCoderTabs.addTab("Відеопослідовність", blockSourceCoderVideoSequence);

        javax.swing.GroupLayout blockSourceCoderLayout = new javax.swing.GroupLayout(blockSourceCoder);
        blockSourceCoder.setLayout(blockSourceCoderLayout);
        blockSourceCoderLayout.setHorizontalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceCoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 977, Short.MAX_VALUE)
        );
        blockSourceCoderLayout.setVerticalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceCoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Кодер джерела", blockSourceCoder);

        blockChannelCoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelCoderComponentShown(evt);
            }
        });

        blockChannelCoderOutput.setContentType("text/html");
        blockChannelCoderOutput.setEditable(false);
        blockChannelCoderOutput.setFont(new java.awt.Font("Dialog", 0, 24));
        jScrollPane3.setViewportView(blockChannelCoderOutput);

        javax.swing.GroupLayout blockChannelCoderOutputPanelLayout = new javax.swing.GroupLayout(blockChannelCoderOutputPanel);
        blockChannelCoderOutputPanel.setLayout(blockChannelCoderOutputPanelLayout);
        blockChannelCoderOutputPanelLayout.setHorizontalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockChannelCoderOutputPanelLayout.setVerticalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelCoderBinarySequenceLayout = new javax.swing.GroupLayout(blockChannelCoderBinarySequence);
        blockChannelCoderBinarySequence.setLayout(blockChannelCoderBinarySequenceLayout);
        blockChannelCoderBinarySequenceLayout.setHorizontalGroup(
            blockChannelCoderBinarySequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelCoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelCoderBinarySequenceLayout.setVerticalGroup(
            blockChannelCoderBinarySequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelCoderOutputPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        blockChannelCoderTabs.addTab("Бінарна послідовність", blockChannelCoderBinarySequence);

        blockChannelVideoSequenceOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockChannelCoderVideoSequenceLayout = new javax.swing.GroupLayout(blockChannelCoderVideoSequence);
        blockChannelCoderVideoSequence.setLayout(blockChannelCoderVideoSequenceLayout);
        blockChannelCoderVideoSequenceLayout.setHorizontalGroup(
            blockChannelCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockChannelCoderVideoSequenceLayout.setVerticalGroup(
            blockChannelCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        blockChannelCoderTabs.addTab("Відеопослідовність", blockChannelCoderVideoSequence);

        javax.swing.GroupLayout blockChannelCoderLayout = new javax.swing.GroupLayout(blockChannelCoder);
        blockChannelCoder.setLayout(blockChannelCoderLayout);
        blockChannelCoderLayout.setHorizontalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelCoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 977, Short.MAX_VALUE)
        );
        blockChannelCoderLayout.setVerticalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelCoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Кодер каналу", blockChannelCoder);

        blockModulator.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockModulatorComponentShown(evt);
            }
        });

        modulatorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід модулятора"));

        modulatorOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout modulatorOutputPanelLayout = new javax.swing.GroupLayout(modulatorOutputPanel);
        modulatorOutputPanel.setLayout(modulatorOutputPanelLayout);
        modulatorOutputPanelLayout.setHorizontalGroup(
            modulatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        modulatorOutputPanelLayout.setVerticalGroup(
            modulatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
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

        channelOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout channelOutputPanelLayout = new javax.swing.GroupLayout(channelOutputPanel);
        channelOutputPanel.setLayout(channelOutputPanelLayout);
        channelOutputPanelLayout.setHorizontalGroup(
            channelOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(channelOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        channelOutputPanelLayout.setVerticalGroup(
            channelOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(channelOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
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

        blockEthalonGenerator0.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockEthalonGenerator0ComponentShown(evt);
            }
        });

        ethalonGeneratorOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід опорного генератора 0"));

        javax.swing.GroupLayout ethalonGeneratorOutputField0Layout = new javax.swing.GroupLayout(ethalonGeneratorOutputField0);
        ethalonGeneratorOutputField0.setLayout(ethalonGeneratorOutputField0Layout);
        ethalonGeneratorOutputField0Layout.setHorizontalGroup(
            ethalonGeneratorOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 967, Short.MAX_VALUE)
        );
        ethalonGeneratorOutputField0Layout.setVerticalGroup(
            ethalonGeneratorOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ethalonGeneratorOutputPanel0Layout = new javax.swing.GroupLayout(ethalonGeneratorOutputPanel0);
        ethalonGeneratorOutputPanel0.setLayout(ethalonGeneratorOutputPanel0Layout);
        ethalonGeneratorOutputPanel0Layout.setHorizontalGroup(
            ethalonGeneratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ethalonGeneratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ethalonGeneratorOutputPanel0Layout.setVerticalGroup(
            ethalonGeneratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ethalonGeneratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockEthalonGenerator0Layout = new javax.swing.GroupLayout(blockEthalonGenerator0);
        blockEthalonGenerator0.setLayout(blockEthalonGenerator0Layout);
        blockEthalonGenerator0Layout.setHorizontalGroup(
            blockEthalonGenerator0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ethalonGeneratorOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockEthalonGenerator0Layout.setVerticalGroup(
            blockEthalonGenerator0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ethalonGeneratorOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Опорний генератор 0", blockEthalonGenerator0);

        blockMultiplier0.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMultiplier0ComponentShown(evt);
            }
        });

        multiplierOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід помножувача 0"));

        multiplierOutputField0.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout multiplierOutputPanel0Layout = new javax.swing.GroupLayout(multiplierOutputPanel0);
        multiplierOutputPanel0.setLayout(multiplierOutputPanel0Layout);
        multiplierOutputPanel0Layout.setHorizontalGroup(
            multiplierOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        multiplierOutputPanel0Layout.setVerticalGroup(
            multiplierOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
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

        blockEthalonGenerator1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockEthalonGenerator1ComponentShown(evt);
            }
        });

        ethalonGeneratorOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід опорного генератора 1"));

        javax.swing.GroupLayout ethalonGeneratorOutputField1Layout = new javax.swing.GroupLayout(ethalonGeneratorOutputField1);
        ethalonGeneratorOutputField1.setLayout(ethalonGeneratorOutputField1Layout);
        ethalonGeneratorOutputField1Layout.setHorizontalGroup(
            ethalonGeneratorOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 967, Short.MAX_VALUE)
        );
        ethalonGeneratorOutputField1Layout.setVerticalGroup(
            ethalonGeneratorOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ethalonGeneratorOutputPanel1Layout = new javax.swing.GroupLayout(ethalonGeneratorOutputPanel1);
        ethalonGeneratorOutputPanel1.setLayout(ethalonGeneratorOutputPanel1Layout);
        ethalonGeneratorOutputPanel1Layout.setHorizontalGroup(
            ethalonGeneratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ethalonGeneratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ethalonGeneratorOutputPanel1Layout.setVerticalGroup(
            ethalonGeneratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ethalonGeneratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockEthalonGenerator1Layout = new javax.swing.GroupLayout(blockEthalonGenerator1);
        blockEthalonGenerator1.setLayout(blockEthalonGenerator1Layout);
        blockEthalonGenerator1Layout.setHorizontalGroup(
            blockEthalonGenerator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ethalonGeneratorOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockEthalonGenerator1Layout.setVerticalGroup(
            blockEthalonGenerator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ethalonGeneratorOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Опорний генератор 1", blockEthalonGenerator1);

        blockMultiplier1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMultiplier1ComponentShown(evt);
            }
        });

        multiplierOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід помножувача 1"));

        multiplierOutputField1.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout multiplierOutputPanel1Layout = new javax.swing.GroupLayout(multiplierOutputPanel1);
        multiplierOutputPanel1.setLayout(multiplierOutputPanel1Layout);
        multiplierOutputPanel1Layout.setHorizontalGroup(
            multiplierOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        multiplierOutputPanel1Layout.setVerticalGroup(
            multiplierOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(multiplierOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
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

        blockIntegrator0.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockIntegrator0ComponentShown(evt);
            }
        });

        integratorOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід інтегратора 0"));

        integratorOutputField0.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout integratorOutputPanel0Layout = new javax.swing.GroupLayout(integratorOutputPanel0);
        integratorOutputPanel0.setLayout(integratorOutputPanel0Layout);
        integratorOutputPanel0Layout.setHorizontalGroup(
            integratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        integratorOutputPanel0Layout.setVerticalGroup(
            integratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
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

        blockIntegrator1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockIntegrator1ComponentShown(evt);
            }
        });

        integratorOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід інтегратора 1"));

        integratorOutputField1.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout integratorOutputPanel1Layout = new javax.swing.GroupLayout(integratorOutputPanel1);
        integratorOutputPanel1.setLayout(integratorOutputPanel1Layout);
        integratorOutputPanel1Layout.setHorizontalGroup(
            integratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        integratorOutputPanel1Layout.setVerticalGroup(
            integratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(integratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
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

        blockSummator.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockSummatorComponentShown(evt);
            }
        });

        blockSummatorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід суматора"));

        blockSummatorOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockSummatorOutputPanelLayout = new javax.swing.GroupLayout(blockSummatorOutputPanel);
        blockSummatorOutputPanel.setLayout(blockSummatorOutputPanelLayout);
        blockSummatorOutputPanelLayout.setHorizontalGroup(
            blockSummatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSummatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockSummatorOutputPanelLayout.setVerticalGroup(
            blockSummatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSummatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSummatorLayout = new javax.swing.GroupLayout(blockSummator);
        blockSummator.setLayout(blockSummatorLayout);
        blockSummatorLayout.setHorizontalGroup(
            blockSummatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSummatorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSummatorLayout.setVerticalGroup(
            blockSummatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSummatorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Суматор", blockSummator);

        blockResolver.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockResolverComponentShown(evt);
            }
        });

        blockResolverOutput.setContentType("text/html");
        blockResolverOutput.setEditable(false);
        blockResolverOutput.setFont(new java.awt.Font("Dialog", 0, 24));
        jScrollPane5.setViewportView(blockResolverOutput);

        javax.swing.GroupLayout blockResolverOutputPanelLayout = new javax.swing.GroupLayout(blockResolverOutputPanel);
        blockResolverOutputPanel.setLayout(blockResolverOutputPanelLayout);
        blockResolverOutputPanelLayout.setHorizontalGroup(
            blockResolverOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockResolverOutputPanelLayout.setVerticalGroup(
            blockResolverOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockResolverBinarySequenceLayout = new javax.swing.GroupLayout(blockResolverBinarySequence);
        blockResolverBinarySequence.setLayout(blockResolverBinarySequenceLayout);
        blockResolverBinarySequenceLayout.setHorizontalGroup(
            blockResolverBinarySequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockResolverBinarySequenceLayout.setVerticalGroup(
            blockResolverBinarySequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        blockResolverTabs.addTab("Бінарна послідовність", blockResolverBinarySequence);

        blockResolverVideoSequenceOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockResolverVideoSequenceLayout = new javax.swing.GroupLayout(blockResolverVideoSequence);
        blockResolverVideoSequence.setLayout(blockResolverVideoSequenceLayout);
        blockResolverVideoSequenceLayout.setHorizontalGroup(
            blockResolverVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockResolverVideoSequenceLayout.setVerticalGroup(
            blockResolverVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        blockResolverTabs.addTab("Відеопослідовність", blockResolverVideoSequence);

        javax.swing.GroupLayout blockResolverLayout = new javax.swing.GroupLayout(blockResolver);
        blockResolver.setLayout(blockResolverLayout);
        blockResolverLayout.setHorizontalGroup(
            blockResolverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 977, Short.MAX_VALUE)
        );
        blockResolverLayout.setVerticalGroup(
            blockResolverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Вирішуючий пристрій", blockResolver);

        blockChannelDecoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelDecoderComponentShown(evt);
            }
        });

        blockChannelDecoderOutput.setContentType("text/html");
        blockChannelDecoderOutput.setEditable(false);
        blockChannelDecoderOutput.setFont(new java.awt.Font("Dialog", 0, 24));
        jScrollPane6.setViewportView(blockChannelDecoderOutput);

        javax.swing.GroupLayout blockChannelDecoderOutputPanelLayout = new javax.swing.GroupLayout(blockChannelDecoderOutputPanel);
        blockChannelDecoderOutputPanel.setLayout(blockChannelDecoderOutputPanelLayout);
        blockChannelDecoderOutputPanelLayout.setHorizontalGroup(
            blockChannelDecoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockChannelDecoderOutputPanelLayout.setVerticalGroup(
            blockChannelDecoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelDecoderBinarySequenceLayout = new javax.swing.GroupLayout(blockChannelDecoderBinarySequence);
        blockChannelDecoderBinarySequence.setLayout(blockChannelDecoderBinarySequenceLayout);
        blockChannelDecoderBinarySequenceLayout.setHorizontalGroup(
            blockChannelDecoderBinarySequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelDecoderBinarySequenceLayout.setVerticalGroup(
            blockChannelDecoderBinarySequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        blockChannelDecoderTabs.addTab("Бінарна послідовність", blockChannelDecoderBinarySequence);

        blockChannelDecoderVideoSequenceOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockChannelDecoderVideoSequenceLayout = new javax.swing.GroupLayout(blockChannelDecoderVideoSequence);
        blockChannelDecoderVideoSequence.setLayout(blockChannelDecoderVideoSequenceLayout);
        blockChannelDecoderVideoSequenceLayout.setHorizontalGroup(
            blockChannelDecoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockChannelDecoderVideoSequenceLayout.setVerticalGroup(
            blockChannelDecoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
        );

        blockChannelDecoderTabs.addTab("Відеопослідовність", blockChannelDecoderVideoSequence);

        javax.swing.GroupLayout blockChannelDecoderLayout = new javax.swing.GroupLayout(blockChannelDecoder);
        blockChannelDecoder.setLayout(blockChannelDecoderLayout);
        blockChannelDecoderLayout.setHorizontalGroup(
            blockChannelDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 977, Short.MAX_VALUE)
        );
        blockChannelDecoderLayout.setVerticalGroup(
            blockChannelDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Декодер каналу", blockChannelDecoder);

        blockSourceDecoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockSourceDecoderComponentShown(evt);
            }
        });

        sourceDecoderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Отримане повідомлення"));

        receivedMessageArea.setColumns(20);
        receivedMessageArea.setEditable(false);
        receivedMessageArea.setFont(new java.awt.Font("Dialog", 0, 24));
        receivedMessageArea.setRows(5);
        jScrollPane4.setViewportView(receivedMessageArea);

        javax.swing.GroupLayout sourceDecoderPanelLayout = new javax.swing.GroupLayout(sourceDecoderPanel);
        sourceDecoderPanel.setLayout(sourceDecoderPanelLayout);
        sourceDecoderPanelLayout.setHorizontalGroup(
            sourceDecoderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        sourceDecoderPanelLayout.setVerticalGroup(
            sourceDecoderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceDecoderLayout = new javax.swing.GroupLayout(blockSourceDecoder);
        blockSourceDecoder.setLayout(blockSourceDecoderLayout);
        blockSourceDecoderLayout.setHorizontalGroup(
            blockSourceDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourceDecoderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSourceDecoderLayout.setVerticalGroup(
            blockSourceDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourceDecoderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Декодер джерела", blockSourceDecoder);

        systemScheme.setBorder(javax.swing.BorderFactory.createTitledBorder("Структурна схема"));
        systemScheme.setLayout(new java.awt.GridBagLayout());

        channelCoderButton.setBackground(new java.awt.Color(240, 240, 240));
        channelCoderButton.setText("КК");
        channelCoderButton.setToolTipText("Кодер каналу");
        channelCoderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channelCoderButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        systemScheme.add(channelCoderButton, gridBagConstraints);

        modulatorButton.setBackground(new java.awt.Color(240, 240, 240));
        modulatorButton.setText("М");
        modulatorButton.setToolTipText("Модулятор");
        modulatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulatorButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        systemScheme.add(modulatorButton, gridBagConstraints);

        channelButton.setBackground(new java.awt.Color(240, 240, 240));
        channelButton.setText("К");
        channelButton.setToolTipText("Канал");
        channelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        systemScheme.add(channelButton, gridBagConstraints);

        integrator0Button.setBackground(new java.awt.Color(240, 240, 240));
        integrator0Button.setText("І0");
        integrator0Button.setToolTipText("Інтегратор 0");
        integrator0Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrator0ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 2;
        systemScheme.add(integrator0Button, gridBagConstraints);

        sourceCoderButton.setBackground(new java.awt.Color(240, 240, 240));
        sourceCoderButton.setText("КДж");
        sourceCoderButton.setToolTipText("Кодер джерела повідомлень");
        sourceCoderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceCoderButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        systemScheme.add(sourceCoderButton, gridBagConstraints);

        multiplier0Button.setBackground(new java.awt.Color(240, 240, 240));
        multiplier0Button.setText("П0");
        multiplier0Button.setToolTipText("Помножувач 0");
        multiplier0Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiplier0ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        systemScheme.add(multiplier0Button, gridBagConstraints);

        multiplier1Button.setBackground(new java.awt.Color(240, 240, 240));
        multiplier1Button.setText("П1");
        multiplier1Button.setToolTipText("Помножувач 1");
        multiplier1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiplier1ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 4;
        systemScheme.add(multiplier1Button, gridBagConstraints);

        messageSourceButton.setBackground(new java.awt.Color(200, 200, 200));
        messageSourceButton.setText("ДжП");
        messageSourceButton.setToolTipText("Джерело повідомлень");
        messageSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageSourceButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        systemScheme.add(messageSourceButton, gridBagConstraints);

        integrator1Button.setBackground(new java.awt.Color(240, 240, 240));
        integrator1Button.setText("І1");
        integrator1Button.setToolTipText("Інтегратор 1");
        integrator1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrator1ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 4;
        systemScheme.add(integrator1Button, gridBagConstraints);

        summatorButton.setBackground(new java.awt.Color(240, 240, 240));
        summatorButton.setText("С");
        summatorButton.setToolTipText("Суматор");
        summatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summatorButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 3;
        systemScheme.add(summatorButton, gridBagConstraints);

        drArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dr.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 4;
        systemScheme.add(drArrowLabel, gridBagConstraints);

        leftTripleLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/triplel.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        systemScheme.add(leftTripleLabel, gridBagConstraints);

        dlArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dl.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 4;
        systemScheme.add(dlArrowLabel, gridBagConstraints);

        ulArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ul.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 2;
        systemScheme.add(ulArrowLabel, gridBagConstraints);

        urArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ur.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        systemScheme.add(urArrowLabel, gridBagConstraints);

        rightTripleLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tripler.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightTripleLabel, gridBagConstraints);

        rightArrowLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel2, gridBagConstraints);

        rightArrowLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel3, gridBagConstraints);

        rightArrowLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel4, gridBagConstraints);

        rightArrowLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel6, gridBagConstraints);

        rightArrowLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel1, gridBagConstraints);

        resolverButton.setBackground(new java.awt.Color(240, 240, 240));
        resolverButton.setText("ВП");
        resolverButton.setToolTipText("Пристрій прийняття рішень");
        resolverButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resolverButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 3;
        systemScheme.add(resolverButton, gridBagConstraints);

        rightArrowLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 4;
        systemScheme.add(rightArrowLabel7, gridBagConstraints);

        rightArrowLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel8, gridBagConstraints);

        rightArrowLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel9, gridBagConstraints);

        upArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/up_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 5;
        systemScheme.add(upArrowLabel, gridBagConstraints);

        channelDecoderButton.setBackground(new java.awt.Color(240, 240, 240));
        channelDecoderButton.setText("ДКК");
        channelDecoderButton.setToolTipText("Декодер каналу");
        channelDecoderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channelDecoderButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 3;
        systemScheme.add(channelDecoderButton, gridBagConstraints);

        sourceDecoderButton.setBackground(new java.awt.Color(240, 240, 240));
        sourceDecoderButton.setText("ДКДж");
        sourceDecoderButton.setToolTipText("Декодер джерела");
        sourceDecoderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceDecoderButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 3;
        systemScheme.add(sourceDecoderButton, gridBagConstraints);

        ethalonGenerator0Button.setBackground(new java.awt.Color(240, 240, 240));
        ethalonGenerator0Button.setText("Г0");
        ethalonGenerator0Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ethalonGenerator0ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        systemScheme.add(ethalonGenerator0Button, gridBagConstraints);

        ethalonGenerator1Button.setBackground(new java.awt.Color(240, 240, 240));
        ethalonGenerator1Button.setText("Г1");
        ethalonGenerator1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ethalonGenerator1ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 6;
        systemScheme.add(ethalonGenerator1Button, gridBagConstraints);

        downArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/down_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        systemScheme.add(downArrowLabel, gridBagConstraints);

        rightArrowLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 2;
        systemScheme.add(rightArrowLabel12, gridBagConstraints);

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

        doModellingOptionsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        doModellingOptionsItem.setText("Налаштування моделювання…");
        doModellingOptionsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doModellingOptionsItemActionPerformed(evt);
            }
        });
        modellingMenu.add(doModellingOptionsItem);

        doModellingItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        doModellingItem.setText("Виконати моделювання");
        doModellingItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doModellingItemActionPerformed(evt);
            }
        });
        modellingMenu.add(doModellingItem);

        mainMenu.add(modellingMenu);

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
            .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 982, Short.MAX_VALUE)
            .addComponent(systemScheme, javax.swing.GroupLayout.DEFAULT_SIZE, 982, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(systemScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitItemActionPerformed
    {//GEN-HEADEREND:event_exitItemActionPerformed
	System.exit(0);
    }//GEN-LAST:event_exitItemActionPerformed

    private void sourceCodesChooserItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_sourceCodesChooserItemStateChanged
    {//GEN-HEADEREND:event_sourceCodesChooserItemStateChanged
	updateChosenCodeSource();
    }//GEN-LAST:event_sourceCodesChooserItemStateChanged

    //calls all modelling methods step-by-step
    private void doModellingItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_doModellingItemActionPerformed
    {//GEN-HEADEREND:event_doModellingItemActionPerformed
	message = blockMessageArea.getText();
	if (!message.isEmpty())
	{
	    doSourceCoding();
	    doSourceCodingVideoSequence();
	    doChannelCoding();
	    doChannelCodingVideoSequence();
	    doModulating();
	    doChannel();
	    doGenerating();
	    doMultiplying();
	    doIntegrating();
	    doSumming();
	    doResolving();
	    doChannelDecoding();
	    doChannelDecodingVideoSequence();
	    doSourceDecoding();
	}
    }//GEN-LAST:event_doModellingItemActionPerformed

    private void channelCodesChooserItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_channelCodesChooserItemStateChanged
    {//GEN-HEADEREND:event_channelCodesChooserItemStateChanged
	updateChosenCodeChannel();
    }//GEN-LAST:event_channelCodesChooserItemStateChanged

    //shows about dialog
    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutItemActionPerformed
    {//GEN-HEADEREND:event_aboutItemActionPerformed
	aboutDialog.setSize(410, 230);
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
	selectedBlock = Blocks.MESSAGE_SOURCE;
	updateChosenBlock();
    }//GEN-LAST:event_messageSourceButtonActionPerformed

    private void sourceCoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sourceCoderButtonActionPerformed
    {//GEN-HEADEREND:event_sourceCoderButtonActionPerformed
	selectedBlock = Blocks.SOURCE_CODER;
	updateChosenBlock();
    }//GEN-LAST:event_sourceCoderButtonActionPerformed

    private void channelCoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_channelCoderButtonActionPerformed
    {//GEN-HEADEREND:event_channelCoderButtonActionPerformed
	selectedBlock = Blocks.CHANNEL_CODER;
	updateChosenBlock();
    }//GEN-LAST:event_channelCoderButtonActionPerformed

    private void modulatorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_modulatorButtonActionPerformed
    {//GEN-HEADEREND:event_modulatorButtonActionPerformed
	selectedBlock = Blocks.MODULATOR;
	updateChosenBlock();
    }//GEN-LAST:event_modulatorButtonActionPerformed

    private void channelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_channelButtonActionPerformed
    {//GEN-HEADEREND:event_channelButtonActionPerformed
	selectedBlock = Blocks.CHANNEL;
	updateChosenBlock();
    }//GEN-LAST:event_channelButtonActionPerformed

    private void blockMessageSourceComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockMessageSourceComponentShown
    {//GEN-HEADEREND:event_blockMessageSourceComponentShown
	selectedBlock = Blocks.MESSAGE_SOURCE;
	updateChosenBlock();
    }//GEN-LAST:event_blockMessageSourceComponentShown

    private void blockModulatorComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockModulatorComponentShown
    {//GEN-HEADEREND:event_blockModulatorComponentShown
	selectedBlock = Blocks.MODULATOR;
	updateChosenBlock();
    }//GEN-LAST:event_blockModulatorComponentShown

    private void blockChannelComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelComponentShown
    {//GEN-HEADEREND:event_blockChannelComponentShown
	selectedBlock = Blocks.CHANNEL;
	updateChosenBlock();
    }//GEN-LAST:event_blockChannelComponentShown

    private void multiplier0ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_multiplier0ButtonActionPerformed
    {//GEN-HEADEREND:event_multiplier0ButtonActionPerformed
	selectedBlock = Blocks.MULTIPLIER0;
	updateChosenBlock();
    }//GEN-LAST:event_multiplier0ButtonActionPerformed

    private void multiplier1ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_multiplier1ButtonActionPerformed
    {//GEN-HEADEREND:event_multiplier1ButtonActionPerformed
	selectedBlock = Blocks.MULTIPLIER1;
	updateChosenBlock();
    }//GEN-LAST:event_multiplier1ButtonActionPerformed

    private void integrator0ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_integrator0ButtonActionPerformed
    {//GEN-HEADEREND:event_integrator0ButtonActionPerformed
	selectedBlock = Blocks.INTEGRATOR0;
	updateChosenBlock();
    }//GEN-LAST:event_integrator0ButtonActionPerformed

    private void integrator1ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_integrator1ButtonActionPerformed
    {//GEN-HEADEREND:event_integrator1ButtonActionPerformed
	selectedBlock = Blocks.INTEGRATOR1;
	updateChosenBlock();
    }//GEN-LAST:event_integrator1ButtonActionPerformed

    private void summatorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_summatorButtonActionPerformed
    {//GEN-HEADEREND:event_summatorButtonActionPerformed
	selectedBlock = Blocks.SUMMATOR;
	updateChosenBlock();
    }//GEN-LAST:event_summatorButtonActionPerformed

    private void blockMultiplier0ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockMultiplier0ComponentShown
    {//GEN-HEADEREND:event_blockMultiplier0ComponentShown
	selectedBlock = Blocks.MULTIPLIER0;
	updateChosenBlock();
    }//GEN-LAST:event_blockMultiplier0ComponentShown

    private void blockMultiplier1ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockMultiplier1ComponentShown
    {//GEN-HEADEREND:event_blockMultiplier1ComponentShown
	selectedBlock = Blocks.MULTIPLIER1;
	updateChosenBlock();
    }//GEN-LAST:event_blockMultiplier1ComponentShown

    private void blockIntegrator0ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockIntegrator0ComponentShown
    {//GEN-HEADEREND:event_blockIntegrator0ComponentShown
	selectedBlock = Blocks.INTEGRATOR0;
	updateChosenBlock();
    }//GEN-LAST:event_blockIntegrator0ComponentShown

    private void blockIntegrator1ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockIntegrator1ComponentShown
    {//GEN-HEADEREND:event_blockIntegrator1ComponentShown
	selectedBlock = Blocks.INTEGRATOR1;
	updateChosenBlock();
    }//GEN-LAST:event_blockIntegrator1ComponentShown

    private void blockSummatorComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockSummatorComponentShown
    {//GEN-HEADEREND:event_blockSummatorComponentShown
	selectedBlock = Blocks.SUMMATOR;
	updateChosenBlock();
    }//GEN-LAST:event_blockSummatorComponentShown

    private void doModellingOptionsItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_doModellingOptionsItemActionPerformed
    {//GEN-HEADEREND:event_doModellingOptionsItemActionPerformed
	modellingOptionsDialog.setSize(670, 180);
	modellingOptionsDialog.setVisible(true);
    }//GEN-LAST:event_doModellingOptionsItemActionPerformed

    private void blockSourceDecoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockSourceDecoderComponentShown
    {//GEN-HEADEREND:event_blockSourceDecoderComponentShown
	selectedBlock = Blocks.SOURCE_DECODER;
	updateChosenBlock();
    }//GEN-LAST:event_blockSourceDecoderComponentShown

    private void forceErrorsItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_forceErrorsItemStateChanged
    {//GEN-HEADEREND:event_forceErrorsItemStateChanged
	boolean newState = evt.getStateChange() == ItemEvent.SELECTED;
	forceErrorsCount.setEnabled(newState);
	errorsInjectorPerBlock.setEnabled(newState);
	errorsInjectorPerSequence.setEnabled(newState);
	forceErrorsTrigger = newState;
    }//GEN-LAST:event_forceErrorsItemStateChanged

    private void useNoiseErrorsItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_useNoiseErrorsItemStateChanged
    {//GEN-HEADEREND:event_useNoiseErrorsItemStateChanged
	useNoiseErrorsTrigger = evt.getStateChange() == ItemEvent.SELECTED;
    }//GEN-LAST:event_useNoiseErrorsItemStateChanged

    private void useChannelCoderItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_useChannelCoderItemStateChanged
    {//GEN-HEADEREND:event_useChannelCoderItemStateChanged
	useChannelCoderTrigger = evt.getStateChange() == ItemEvent.SELECTED;
	channelCodesChooser.setEnabled(useChannelCoderTrigger);
	channelCodesChooserLabel.setEnabled(useChannelCoderTrigger);
    }//GEN-LAST:event_useChannelCoderItemStateChanged

    private void errorsInjectorPerBlockItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_errorsInjectorPerBlockItemStateChanged
    {//GEN-HEADEREND:event_errorsInjectorPerBlockItemStateChanged
	injectErrorsPerBlock = evt.getStateChange() == ItemEvent.SELECTED;
    }//GEN-LAST:event_errorsInjectorPerBlockItemStateChanged

    private void errorsInjectorPerSequenceItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_errorsInjectorPerSequenceItemStateChanged
    {//GEN-HEADEREND:event_errorsInjectorPerSequenceItemStateChanged
	injectErrorsPerBlock = !(evt.getStateChange() == ItemEvent.SELECTED);
    }//GEN-LAST:event_errorsInjectorPerSequenceItemStateChanged

    private void blockSourceCoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockSourceCoderComponentShown
    {//GEN-HEADEREND:event_blockSourceCoderComponentShown
	selectedBlock = Blocks.SOURCE_CODER;
	updateChosenBlock();
    }//GEN-LAST:event_blockSourceCoderComponentShown

    private void blockChannelCoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelCoderComponentShown
    {//GEN-HEADEREND:event_blockChannelCoderComponentShown
	selectedBlock = Blocks.CHANNEL_CODER;
	updateChosenBlock();
    }//GEN-LAST:event_blockChannelCoderComponentShown

    private void blockResolverComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockResolverComponentShown
    {//GEN-HEADEREND:event_blockResolverComponentShown
	selectedBlock = Blocks.RESOLVER;
	updateChosenBlock();
    }//GEN-LAST:event_blockResolverComponentShown

    private void blockChannelDecoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelDecoderComponentShown
    {//GEN-HEADEREND:event_blockChannelDecoderComponentShown
	selectedBlock = Blocks.CHANNEL_DECODER;
	updateChosenBlock();
    }//GEN-LAST:event_blockChannelDecoderComponentShown

    private void resolverButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resolverButtonActionPerformed
    {//GEN-HEADEREND:event_resolverButtonActionPerformed
	selectedBlock = Blocks.RESOLVER;
	updateChosenBlock();
    }//GEN-LAST:event_resolverButtonActionPerformed

    private void channelDecoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_channelDecoderButtonActionPerformed
    {//GEN-HEADEREND:event_channelDecoderButtonActionPerformed
	selectedBlock = Blocks.CHANNEL_DECODER;
	updateChosenBlock();
    }//GEN-LAST:event_channelDecoderButtonActionPerformed

    private void sourceDecoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sourceDecoderButtonActionPerformed
    {//GEN-HEADEREND:event_sourceDecoderButtonActionPerformed
	selectedBlock = Blocks.SOURCE_DECODER;
	updateChosenBlock();
    }//GEN-LAST:event_sourceDecoderButtonActionPerformed

    private void blockEthalonGenerator0ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockEthalonGenerator0ComponentShown
    {//GEN-HEADEREND:event_blockEthalonGenerator0ComponentShown
	selectedBlock = Blocks.ETHALON_GENERATOR0;
	updateChosenBlock();
    }//GEN-LAST:event_blockEthalonGenerator0ComponentShown

    private void blockEthalonGenerator1ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockEthalonGenerator1ComponentShown
    {//GEN-HEADEREND:event_blockEthalonGenerator1ComponentShown
	selectedBlock = Blocks.ETHALON_GENERATOR1;
	updateChosenBlock();
    }//GEN-LAST:event_blockEthalonGenerator1ComponentShown

    private void ethalonGenerator0ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ethalonGenerator0ButtonActionPerformed
    {//GEN-HEADEREND:event_ethalonGenerator0ButtonActionPerformed
	selectedBlock = Blocks.ETHALON_GENERATOR0;
	updateChosenBlock();
    }//GEN-LAST:event_ethalonGenerator0ButtonActionPerformed

    private void ethalonGenerator1ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ethalonGenerator1ButtonActionPerformed
    {//GEN-HEADEREND:event_ethalonGenerator1ButtonActionPerformed
	selectedBlock = Blocks.ETHALON_GENERATOR1;
	updateChosenBlock();
    }//GEN-LAST:event_ethalonGenerator1ButtonActionPerformed

    private void showLicenseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showLicenseActionPerformed
    {//GEN-HEADEREND:event_showLicenseActionPerformed
	licenseDialog.setSize(750, 520);
	licenseTextArea.setText("");
	try
	{
	    FileReader fr = new FileReader("COPYING");
	    BufferedReader bfr = new BufferedReader(fr);
	    String line;
	    while((line = bfr.readLine()) != null)
	    {
		licenseTextArea.setText(licenseTextArea.getText() + line + "\n");
	    }
	}
	catch (Exception ex)
	{
	    System.err.println(ex.getLocalizedMessage());
	}
	licenseTextArea.setSelectionStart(0);
	licenseTextArea.setSelectionEnd(0);
	licenseDialog.setVisible(true);
    }//GEN-LAST:event_showLicenseActionPerformed

    private void closeLicenseWindowButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeLicenseWindowButtonActionPerformed
    {//GEN-HEADEREND:event_closeLicenseWindowButtonActionPerformed
	licenseDialog.setVisible(false);
    }//GEN-LAST:event_closeLicenseWindowButtonActionPerformed

    /**
     * 
     * @param args
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
		JFrame mainForm = new UIMain();
		//main window starts maximized
		mainForm.setExtendedState(MAXIMIZED_BOTH);
		mainForm.setVisible(true);
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
    private javax.swing.JSpinner bearerFrequency;
    private javax.swing.JSpinner bearerFrequencyDeviation;
    private javax.swing.JLabel bearerFrequencyDeviationLabel;
    private javax.swing.JLabel bearerFrequencyLabel;
    private javax.swing.JPanel blockChannel;
    private javax.swing.JPanel blockChannelCoder;
    private javax.swing.JPanel blockChannelCoderBinarySequence;
    private javax.swing.JTextPane blockChannelCoderOutput;
    private javax.swing.JPanel blockChannelCoderOutputPanel;
    private javax.swing.JTabbedPane blockChannelCoderTabs;
    private javax.swing.JPanel blockChannelCoderVideoSequence;
    private javax.swing.JPanel blockChannelDecoder;
    private javax.swing.JPanel blockChannelDecoderBinarySequence;
    private javax.swing.JTextPane blockChannelDecoderOutput;
    private javax.swing.JPanel blockChannelDecoderOutputPanel;
    private javax.swing.JTabbedPane blockChannelDecoderTabs;
    private javax.swing.JPanel blockChannelDecoderVideoSequence;
    private javax.swing.JPanel blockChannelDecoderVideoSequenceOutputField;
    private javax.swing.JPanel blockChannelVideoSequenceOutputField;
    private javax.swing.JPanel blockEthalonGenerator0;
    private javax.swing.JPanel blockEthalonGenerator1;
    private javax.swing.JPanel blockIntegrator0;
    private javax.swing.JPanel blockIntegrator1;
    private javax.swing.JTextArea blockMessageArea;
    private javax.swing.JPanel blockMessageSource;
    private javax.swing.JPanel blockModulator;
    private javax.swing.JPanel blockMultiplier0;
    private javax.swing.JPanel blockMultiplier1;
    private javax.swing.JPanel blockResolver;
    private javax.swing.JPanel blockResolverBinarySequence;
    private javax.swing.JTextPane blockResolverOutput;
    private javax.swing.JPanel blockResolverOutputPanel;
    private javax.swing.JTabbedPane blockResolverTabs;
    private javax.swing.JPanel blockResolverVideoSequence;
    private javax.swing.JPanel blockResolverVideoSequenceOutputField;
    private javax.swing.JPanel blockSourceCoder;
    private javax.swing.JPanel blockSourceCoderBinarySequence;
    private javax.swing.JTextPane blockSourceCoderOutput;
    private javax.swing.JPanel blockSourceCoderOutputPanel;
    private javax.swing.JTabbedPane blockSourceCoderTabs;
    private javax.swing.JPanel blockSourceCoderVideoSequence;
    private javax.swing.JPanel blockSourceDecoder;
    private javax.swing.JPanel blockSourceVideoSequenceOutputField;
    private javax.swing.JPanel blockSummator;
    private javax.swing.JPanel blockSummatorOutputField;
    private javax.swing.JPanel blockSummatorOutputPanel;
    private javax.swing.JLabel bpsLabel;
    private javax.swing.JButton channelButton;
    private javax.swing.JButton channelCoderButton;
    private javax.swing.JPanel channelCoderTab;
    private javax.swing.JComboBox channelCodesChooser;
    private javax.swing.JLabel channelCodesChooserLabel;
    private javax.swing.JButton channelDecoderButton;
    private javax.swing.JPanel channelOutputField;
    private javax.swing.JPanel channelOutputPanel;
    private javax.swing.JPanel channelTab;
    private javax.swing.JButton closeLicenseWindowButton;
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.JLabel dlArrowLabel;
    private javax.swing.JMenuItem doModellingItem;
    private javax.swing.JMenuItem doModellingOptionsItem;
    private javax.swing.JLabel downArrowLabel;
    private javax.swing.JLabel drArrowLabel;
    private javax.swing.JRadioButton errorsInjectorPerBlock;
    private javax.swing.JRadioButton errorsInjectorPerSequence;
    private javax.swing.ButtonGroup errorsInjectorTypeChooserGroup;
    private javax.swing.JButton ethalonGenerator0Button;
    private javax.swing.JButton ethalonGenerator1Button;
    private javax.swing.JPanel ethalonGeneratorOutputField0;
    private javax.swing.JPanel ethalonGeneratorOutputField1;
    private javax.swing.JPanel ethalonGeneratorOutputPanel0;
    private javax.swing.JPanel ethalonGeneratorOutputPanel1;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JCheckBox forceErrors;
    private javax.swing.JSpinner forceErrorsCount;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel hzBearerLabel;
    private javax.swing.JLabel hzDeviationLabel;
    private javax.swing.JSpinner informationalSpeed;
    private javax.swing.JLabel informationalSpeedLabel;
    private javax.swing.JLabel instituteNameLabel;
    private javax.swing.JButton integrator0Button;
    private javax.swing.JButton integrator1Button;
    private javax.swing.JPanel integratorOutputField0;
    private javax.swing.JPanel integratorOutputField1;
    private javax.swing.JPanel integratorOutputPanel0;
    private javax.swing.JPanel integratorOutputPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel leftTripleLabel;
    private javax.swing.JDialog licenseDialog;
    private javax.swing.JLabel licenseLabel;
    private javax.swing.JTextArea licenseTextArea;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JButton messageSourceButton;
    private javax.swing.JMenu modellingMenu;
    private javax.swing.JDialog modellingOptionsDialog;
    private javax.swing.JComboBox modulationTypeChooser;
    private javax.swing.JLabel modulationTypeLabel;
    private javax.swing.JButton modulatorButton;
    private javax.swing.JPanel modulatorOutputField;
    private javax.swing.JPanel modulatorOutputPanel;
    private javax.swing.JPanel modulatorTab;
    private javax.swing.JButton multiplier0Button;
    private javax.swing.JButton multiplier1Button;
    private javax.swing.JPanel multiplierOutputField0;
    private javax.swing.JPanel multiplierOutputField1;
    private javax.swing.JPanel multiplierOutputPanel0;
    private javax.swing.JPanel multiplierOutputPanel1;
    private javax.swing.JSpinner noisePower;
    private javax.swing.JLabel noisePowerLabel;
    private javax.swing.JLabel noisePowerWattLabel;
    private javax.swing.JTabbedPane optionsTabs;
    private javax.swing.JLabel programNameLabel;
    private javax.swing.JTextArea receivedMessageArea;
    private javax.swing.JButton resolverButton;
    private javax.swing.JLabel rightArrowLabel1;
    private javax.swing.JLabel rightArrowLabel12;
    private javax.swing.JLabel rightArrowLabel2;
    private javax.swing.JLabel rightArrowLabel3;
    private javax.swing.JLabel rightArrowLabel4;
    private javax.swing.JLabel rightArrowLabel6;
    private javax.swing.JLabel rightArrowLabel7;
    private javax.swing.JLabel rightArrowLabel8;
    private javax.swing.JLabel rightArrowLabel9;
    private javax.swing.JLabel rightTripleLabel;
    private javax.swing.JButton showLicense;
    private javax.swing.JButton sourceCoderButton;
    private javax.swing.JPanel sourceCoderTab;
    private javax.swing.JComboBox sourceCodesChooser;
    private javax.swing.JLabel sourceCodesChooserLabel;
    private javax.swing.JButton sourceDecoderButton;
    private javax.swing.JPanel sourceDecoderPanel;
    private javax.swing.JPanel sourceMessagePanel;
    private javax.swing.JButton summatorButton;
    private javax.swing.JPanel systemScheme;
    private javax.swing.JLabel ulArrowLabel;
    private javax.swing.JLabel upArrowLabel;
    private javax.swing.JLabel urArrowLabel;
    private javax.swing.JCheckBox useChannelCoder;
    private javax.swing.JCheckBox useNoiseErrors;
    private javax.swing.JLabel voltsLabel;
    // End of variables declaration//GEN-END:variables

}
