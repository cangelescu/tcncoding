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
import flanagan.integration.*;
import java.awt.GridLayout;
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
    ChannelSqr currentChannelSqr = null;
    EnergyComputator currentEnergyComputator = null;
    ErrorsComputator currentErrorsComputator = null;
    Multiplier currentMultiplier0 = null, currentMultiplier1 = null;
    Integrator currentIntegrator0 = null, currentIntegrator1 = null;
    Summator currentSummator = null;
    Resolver currentResolver = null;
    VideoCreator currentResolverVideoCreator = null;
    ChannelDecoder currentChannelDecoder = null;
    VideoCreator currentChannelDecoderVideoCreator = null;

    //UI blocks
    enum Blocks {MESSAGE_SOURCE, SOURCE_CODER, SOURCE_VIDEOSEQUENCE, CHANNEL_CODER, CHANNEL_VIDEOSEQUENCE, MODULATOR, CHANNEL, MULTIPLIER0, MULTIPLIER1, INTEGRATOR0, INTEGRATOR1, SUMMATOR, RESOLVER, INPUT_VIDEOSEQUENCE, MESSAGE_RECEIVER, CHANNEL_DECODER, CHANNEL_DECODER_VIDEOSEQUENCE;};
    Blocks selectedBlock = Blocks.MESSAGE_SOURCE;

    //UI vizualization tools
    DataVizualizator currentSourceVideoSequenceVizualizator = null;
    DataVizualizator currentChannelVideoSequenceVizualizator = null;
    DataVizualizator currentModulatorVizualizator = null;
    DataVizualizator currentChannelVizualizator = null;
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

    //channel coder
    List<BinaryNumber> channelSymbols = new ArrayList();
    int headLength;

    //source videosequence
    double sourceImpulseLength = 0;
    List<List<List<FunctionStep>>> sourceVideoSequence = null;
    List<DataVizualizatorProvider> sourceVideoSequenceSingleProvider = null;
    List<List<DataVizualizatorProvider>> sourceVideoSequenceProvider = null;

    //channel videosequence
    double channelImpulseLength = 0;
    List<List<List<FunctionStep>>> channelVideoSequence = null;
    List<List<DataVizualizatorProvider>> channelVideoSequenceProvider = null;

    //Modulator data
    List<List<ModulatorSignal>> modulatorData = null;
    List<List<DataVizualizatorProvider>> modulatorDataProvider = null;

    //Channel data
    List<List<ChannelSignal>> channelOutput = null;
    List<List<ChannelSignalSqr>> channelSqrOutput = null;
    List<List<DataVizualizatorProvider>> channelOutputProvider = null;
    //TODO: EXPERIMENTAL
    double channelOutputEnergy = 0;
    double errorsProbability = 0;

    //multipliers data
    List<List<MultiplierSignal>> multiplier0Output = null;
    List<List<MultiplierSignal>> multiplier1Output = null;
    List<List<DataVizualizatorProvider>> multiplier0OutputProvider = null;
    List<List<DataVizualizatorProvider>> multiplier1OutputProvider = null;

    //integrators data
    List<List<List<FunctionStep>>> integrator0Output = null;
    List<List<List<FunctionStep>>> integrator1Output = null;
    List<List<DataVizualizatorProvider>> integrator0OutputProvider = null;
    List<List<DataVizualizatorProvider>> integrator1OutputProvider = null;

    //Summator data
    List<List<List<FunctionStep>>> summatorOutput = null;
    List<List<DataVizualizatorProvider>> summatorOutputProvider = null;

    //Resolver data
    List<BinaryNumber> resolverOutput = null;
    List<List<List<FunctionStep>>> resolverVideoSequence = null;
    List<List<DataVizualizatorProvider>> resolverVideoSequenceProvider = null;

    //Channel decoder data
    List<BinaryNumber> channelDecoderOutput = null;
    List<List<List<FunctionStep>>> channelDecoderVideoSequence = null;
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
	multiplier0Button.setBackground(new Color(240, 240, 240));
	multiplier1Button.setBackground(new Color(240, 240, 240));
	integrator0Button.setBackground(new Color(240, 240, 240));
	integrator1Button.setBackground(new Color(240, 240, 240));
	summatorButton.setBackground(new Color(240, 240, 240));
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
	    case SOURCE_VIDEOSEQUENCE:
		TCSTabs.setSelectedComponent(blockSourceCoderVideoSequence);
		break;
	    case CHANNEL_CODER:
		TCSTabs.setSelectedComponent(blockChannelCoder);
		channelCoderButton.setBackground(new Color(200, 200, 200));
		break;
	    case CHANNEL_VIDEOSEQUENCE:
		TCSTabs.setSelectedComponent(blockChannelCoderVideoSequence);
		break;
	    case MODULATOR:
		TCSTabs.setSelectedComponent(blockModulator);
		modulatorButton.setBackground(new Color(200, 200, 200));
		break;
	    case CHANNEL:
		TCSTabs.setSelectedComponent(blockChannel);
		channelButton.setBackground(new Color(200, 200, 200));
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
		break;
	    case INPUT_VIDEOSEQUENCE:
		TCSTabs.setSelectedComponent(blockResolverVideoSequence);
		break;
	    case MESSAGE_RECEIVER:
		TCSTabs.setSelectedComponent(blockMessageReceiver);
		break;
	    case CHANNEL_DECODER:
		TCSTabs.setSelectedComponent(blockChannelDecoder);
		break;
	    case CHANNEL_DECODER_VIDEOSEQUENCE:
		TCSTabs.setSelectedComponent(blockChannelDecoderVideoSequence);
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
	blockSourceCoderOutput.setText(currentSourceCoder.getStringSequence());
    }

    //encodes source code with selected Channel code
    void doChannelCoding()
    {
	currentChannelCoder = new ChannelCoder(sourceSymbols, channelCode);
	currentChannelCoder.doEncode();
	channelSymbols = currentChannelCoder.getSequence();
	blockChannelCoderOutput.setText(currentChannelCoder.getStringSequence());
	headLength = currentChannelCoder.getHeadLength();
    }

    //shows source videosequence
    void doSourceVideoSequence()
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
	sourceVideoSequenceSingleProvider = (new TabulatedVizualizatorConverter(sourceVideoSequence, "Вихідна відеопослідовність", Color.BLUE)).getProvided();
	sourceVideoSequenceProvider.add(sourceVideoSequenceSingleProvider);
	int cx = blockSourceVideoSequenceOutputField.getWidth();
	int cy = blockSourceVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentSourceVideoSequenceVizualizator = new DataVizualizator(sourceVideoSequenceProvider, cx, cy, "t, с", "Sv(t), В");

	//shows chart
	currentSourceVideoSequenceVizualizator.setVisible(true);

	//sets GridLayout to ensure charts to be resized on window resizing
	blockSourceVideoSequenceOutputField.setLayout(new GridLayout());
	blockSourceVideoSequenceOutputField.add(currentSourceVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentSourceVideoSequenceVizualizator.repaint();
    }

    //shows channel videosequence
    void doChannelVideoSequence()
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
	channelVideoSequenceProvider.add((new TabulatedVizualizatorConverter(channelVideoSequence, "Кодована відеопослідовність", Color.RED)).getProvided());
	int cx = blockChannelVideoSequenceOutputField.getWidth();
	int cy = blockChannelVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentChannelVideoSequenceVizualizator = new DataVizualizator(channelVideoSequenceProvider, cx, cy, "t, с", "Sv(t), В");

	//shows chart
	currentChannelVideoSequenceVizualizator.setVisible(true);
	blockChannelVideoSequenceOutputField.setLayout(new GridLayout());
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
	modulatorOutputField.setLayout(new GridLayout());
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

	//TODO: EXPERIMENTAL
	//gets channel output signal energy
	currentChannelSqr = new ChannelSqr(modulatorData, (Double)noisePower.getValue());
	currentChannelSqr.doNoising();
	channelSqrOutput = currentChannelSqr.getSignals();
	currentEnergyComputator = new EnergyComputator(channelSqrOutput);
	currentEnergyComputator.computeEnergy();
	channelOutputEnergy = currentEnergyComputator.getEnergy();
	//computes errors probability
	currentErrorsComputator = new ErrorsComputator(channelOutputEnergy, 1.0E-2, modulationType);
	errorsProbability = currentErrorsComputator.getErrorProbability();

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
	channelOutputField.setLayout(new GridLayout());
	channelOutputField.add(currentChannelVizualizator);

	//repaints chart to show it if MODULATOR block is active
	currentChannelVizualizator.repaint();
    }

    //multiplies signals and ethalons
    void doMultiplying()
    {
	//create multipliers according to modulation type
	switch (modulationType)
	{
	    case ASK:
		currentMultiplier0 = new Multiplier((Double)bearerFrequency.getValue(), 0, 0, channelOutput);
		currentMultiplier1 = new Multiplier((Double)bearerFrequency.getValue(), (Double)bearerAmplitude.getValue(), 0, channelOutput);
		break;
	    case FSK:
		currentMultiplier0 = new Multiplier((Double)bearerFrequency.getValue() - (Double)bearerFrequencyDeviation.getValue(), (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentMultiplier1 = new Multiplier((Double)bearerFrequency.getValue() + (Double)bearerFrequencyDeviation.getValue(), (Double)bearerAmplitude.getValue(), 0, channelOutput);
		break;
	    case PSK:
		currentMultiplier0 = new Multiplier((Double)bearerFrequency.getValue(), (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentMultiplier1 = new Multiplier((Double)bearerFrequency.getValue(), (Double)bearerAmplitude.getValue(), -Math.PI, channelOutput);
		break;
	    case RPSK:
		currentMultiplier0 = new Multiplier((Double)bearerFrequency.getValue(), (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentMultiplier1 = new Multiplier((Double)bearerFrequency.getValue(), (Double)bearerAmplitude.getValue(), -Math.PI, channelOutput);
		break;
	}

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
	multiplierOutputField0.setLayout(new GridLayout());
	multiplierOutputField0.add(currentMultiplierVizualizator0);
	currentMultiplierVizualizator0.repaint();

	//does the same for second MULTIPLIER0
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
	multiplierOutputField1.setLayout(new GridLayout());
	multiplierOutputField1.add(currentMultiplierVizualizator1);
	currentMultiplierVizualizator1.repaint();
    }

    //integrates signals from multipliers
    void doIntegrating()
    {
	//integrates multipliers output
	currentIntegrator0 = new Integrator(multiplier0Output);
	currentIntegrator1 = new Integrator(multiplier1Output);
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
	integrator0OutputProvider.add((new TabulatedVizualizatorConverter(integrator0Output, "Сигнал на виході 0-го інтегратора", Color.BLUE)).getProvided());
	currentIntegratorVizualizator0 = new DataVizualizator(integrator0OutputProvider, cx0, cy0, "t, с", "Si0(t), В");
	currentIntegratorVizualizator0.setVisible(true);
	integratorOutputField0.setLayout(new GridLayout());
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
	integrator1OutputProvider.add((new TabulatedVizualizatorConverter(integrator1Output, "Сигнал на виході 1-го інтегратора", Color.BLUE)).getProvided());
	currentIntegratorVizualizator1 = new DataVizualizator(integrator1OutputProvider, cx1, cy1, "t, с", "Si1(t), В");
	currentIntegratorVizualizator1.setVisible(true);
	integratorOutputField1.setLayout(new GridLayout());
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
	summatorOutputProvider.add((new TabulatedVizualizatorConverter(summatorOutput, "Сигнал на виході суматора", Color.BLUE)).getProvided());
	currentSummatorVizualizator = new DataVizualizator(summatorOutputProvider, cx1, cy1, "t, с", "Ssum(t), В");
	currentSummatorVizualizator.setVisible(true);
	blockSummatorOutputField.setLayout(new GridLayout());
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

	currentResolver = new Resolver(summatorOutput, threshold, modulationType);
	currentResolver.doResolving();
	resolverOutput = currentResolver.getBinaryNumbers();
	blockResolverOutput.setText(currentResolver.getStringSequence(channelSymbols));

	currentResolverVideoCreator = new VideoCreator(resolverOutput, channelImpulseLength, 1);
	currentResolverVideoCreator.doVideoSequence();
	resolverVideoSequence = currentResolverVideoCreator.getVideoSequence();
	if (currentResolverVideoSequenceVizualizator != null)
	{
	    blockResolverVideoSequenceOutputField.remove(currentResolverVideoSequenceVizualizator);
	    currentResolverVideoSequenceVizualizator = null;
	}
	resolverVideoSequenceProvider = new ArrayList<List<DataVizualizatorProvider>>();
	resolverVideoSequenceProvider.add((new TabulatedVizualizatorConverter(resolverVideoSequence, "Вхідна відеопослідовність", Color.RED)).getProvided());
	int cx = blockResolverVideoSequenceOutputField.getWidth();
	int cy = blockResolverVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentResolverVideoSequenceVizualizator = new DataVizualizator(resolverVideoSequenceProvider, cx, cy, "t, с", "Sv(t), В");

	//shows chart
	currentResolverVideoSequenceVizualizator.setVisible(true);
	blockResolverVideoSequenceOutputField.setLayout(new GridLayout());
	blockResolverVideoSequenceOutputField.add(currentResolverVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentResolverVideoSequenceVizualizator.repaint();
    }

    //decodes source code with selected Channel code
    void doChannelDecoding()
    {
	currentChannelDecoder = new ChannelDecoder(resolverOutput, channelCode, headLength, lengthMap);
	currentChannelDecoder.doDecode();
	channelDecoderOutput = currentChannelDecoder.getSequence();
	blockChannelDecoderOutput.setText(currentChannelDecoder.getStringSequence());

	currentChannelDecoderVideoCreator = new VideoCreator(channelDecoderOutput, sourceImpulseLength, 1);
	currentChannelDecoderVideoCreator.doVideoSequence();
	channelDecoderVideoSequence = currentChannelDecoderVideoCreator.getVideoSequence();
	if (currentChannelDecoderVideoSequenceVizualizator != null)
	{
	    blockChannelDecoderVideoSequenceOutputField.remove(currentChannelDecoderVideoSequenceVizualizator);
	    currentChannelDecoderVideoSequenceVizualizator = null;
	}
	channelDecoderVideoSequenceProvider = new ArrayList<List<DataVizualizatorProvider>>();
	channelDecoderVideoSequenceProvider.add((new TabulatedVizualizatorConverter(channelDecoderVideoSequence, "Декодована відеопослідовність", Color.RED)).getProvided());
	int cx = blockChannelDecoderVideoSequenceOutputField.getWidth();
	int cy = blockChannelDecoderVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentChannelDecoderVideoSequenceVizualizator = new DataVizualizator(channelDecoderVideoSequenceProvider, cx, cy, "t, с", "Sv(t), В");

	//shows chart
	currentChannelDecoderVideoSequenceVizualizator.setVisible(true);
	blockChannelDecoderVideoSequenceOutputField.setLayout(new GridLayout());
	blockChannelDecoderVideoSequenceOutputField.add(currentChannelDecoderVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentChannelDecoderVideoSequenceVizualizator.repaint();
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

        aboutDialog = new javax.swing.JDialog();
        aboutDialogClose = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
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
        TCSTabs = new javax.swing.JTabbedPane();
        blockMessageSource = new javax.swing.JPanel();
        sourceMessagePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        blockMessageArea = new javax.swing.JTextArea();
        blockSourceCoder = new javax.swing.JPanel();
        blockSourceCoderOutputPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        blockSourceCoderOutput = new javax.swing.JTextPane();
        blockSourceCoderVideoSequence = new javax.swing.JPanel();
        blockSourceVideoSequenceOutputPanel = new javax.swing.JPanel();
        blockSourceVideoSequenceOutputField = new javax.swing.JPanel();
        blockChannelCoder = new javax.swing.JPanel();
        blockChannelCoderOutputPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        blockChannelCoderOutput = new javax.swing.JTextPane();
        blockChannelCoderVideoSequence = new javax.swing.JPanel();
        blockChannelVideoSequenceOutputPanel = new javax.swing.JPanel();
        blockChannelVideoSequenceOutputField = new javax.swing.JPanel();
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
        blockSummatorOutputPanel = new javax.swing.JPanel();
        blockSummatorOutputField = new javax.swing.JPanel();
        blockResolver = new javax.swing.JPanel();
        blockResolverOutputPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        blockResolverOutput = new javax.swing.JTextPane();
        blockResolverVideoSequence = new javax.swing.JPanel();
        blockResolverVideoSequenceOutputPanel = new javax.swing.JPanel();
        blockResolverVideoSequenceOutputField = new javax.swing.JPanel();
        blockChannelDecoder = new javax.swing.JPanel();
        blockChannelDecoderOutputPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        blockChannelDecoderOutput = new javax.swing.JTextPane();
        blockChannelDecoderVideoSequence = new javax.swing.JPanel();
        blockChannelDecoderVideoSequenceOutputPanel = new javax.swing.JPanel();
        blockChannelDecoderVideoSequenceOutputField = new javax.swing.JPanel();
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
        multiplier0Button = new javax.swing.JButton();
        multiplier1Button = new javax.swing.JButton();
        integrator0Button = new javax.swing.JButton();
        integrator1Button = new javax.swing.JButton();
        summatorButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitItem = new javax.swing.JMenuItem();
        modellingMenu = new javax.swing.JMenu();
        doModellingOptionsItem = new javax.swing.JMenuItem();
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

        javax.swing.GroupLayout channelCoderTabLayout = new javax.swing.GroupLayout(channelCoderTab);
        channelCoderTab.setLayout(channelCoderTabLayout);
        channelCoderTabLayout.setHorizontalGroup(
            channelCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelCoderTabLayout.createSequentialGroup()
                .addComponent(channelCodesChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelCodesChooser, 0, 614, Short.MAX_VALUE))
        );
        channelCoderTabLayout.setVerticalGroup(
            channelCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelCoderTabLayout.createSequentialGroup()
                .addGroup(channelCoderTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(channelCodesChooserLabel)
                    .addComponent(channelCodesChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(102, Short.MAX_VALUE))
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

        javax.swing.GroupLayout channelTabLayout = new javax.swing.GroupLayout(channelTab);
        channelTab.setLayout(channelTabLayout);
        channelTabLayout.setHorizontalGroup(
            channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelTabLayout.createSequentialGroup()
                .addComponent(noisePowerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noisePower, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noisePowerWattLabel)
                .addContainerGap(313, Short.MAX_VALUE))
        );
        channelTabLayout.setVerticalGroup(
            channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelTabLayout.createSequentialGroup()
                .addGroup(channelTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noisePowerLabel)
                    .addComponent(noisePower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noisePowerWattLabel))
                .addContainerGap(106, Short.MAX_VALUE))
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Система зв'язку");

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE)
        );
        sourceMessagePanelLayout.setVerticalGroup(
            sourceMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
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

        blockSourceCoderOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід кодера джерела повідомлень"));

        blockSourceCoderOutput.setContentType("text/html");
        blockSourceCoderOutput.setEditable(false);
        blockSourceCoderOutput.setFont(new java.awt.Font("Dialog", 0, 24));
        jScrollPane2.setViewportView(blockSourceCoderOutput);

        javax.swing.GroupLayout blockSourceCoderOutputPanelLayout = new javax.swing.GroupLayout(blockSourceCoderOutputPanel);
        blockSourceCoderOutputPanel.setLayout(blockSourceCoderOutputPanelLayout);
        blockSourceCoderOutputPanelLayout.setHorizontalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE)
        );
        blockSourceCoderOutputPanelLayout.setVerticalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceCoderLayout = new javax.swing.GroupLayout(blockSourceCoder);
        blockSourceCoder.setLayout(blockSourceCoderLayout);
        blockSourceCoderLayout.setHorizontalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceCoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSourceCoderLayout.setVerticalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceCoderOutputPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Кодер джерела повідомлень", blockSourceCoder);

        blockSourceCoderVideoSequence.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockSourceCoderVideoSequenceComponentShown(evt);
            }
        });

        blockSourceVideoSequenceOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Відеопослідовність"));

        javax.swing.GroupLayout blockSourceVideoSequenceOutputFieldLayout = new javax.swing.GroupLayout(blockSourceVideoSequenceOutputField);
        blockSourceVideoSequenceOutputField.setLayout(blockSourceVideoSequenceOutputFieldLayout);
        blockSourceVideoSequenceOutputFieldLayout.setHorizontalGroup(
            blockSourceVideoSequenceOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        blockSourceVideoSequenceOutputFieldLayout.setVerticalGroup(
            blockSourceVideoSequenceOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceVideoSequenceOutputPanelLayout = new javax.swing.GroupLayout(blockSourceVideoSequenceOutputPanel);
        blockSourceVideoSequenceOutputPanel.setLayout(blockSourceVideoSequenceOutputPanelLayout);
        blockSourceVideoSequenceOutputPanelLayout.setHorizontalGroup(
            blockSourceVideoSequenceOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSourceVideoSequenceOutputPanelLayout.setVerticalGroup(
            blockSourceVideoSequenceOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceCoderVideoSequenceLayout = new javax.swing.GroupLayout(blockSourceCoderVideoSequence);
        blockSourceCoderVideoSequence.setLayout(blockSourceCoderVideoSequenceLayout);
        blockSourceCoderVideoSequenceLayout.setHorizontalGroup(
            blockSourceCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceVideoSequenceOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSourceCoderVideoSequenceLayout.setVerticalGroup(
            blockSourceCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceVideoSequenceOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Вихідна відеопослідовність", blockSourceCoderVideoSequence);

        blockChannelCoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelCoderComponentShown(evt);
            }
        });

        blockChannelCoderOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід кодера каналу"));

        blockChannelCoderOutput.setContentType("text/html");
        blockChannelCoderOutput.setEditable(false);
        blockChannelCoderOutput.setFont(new java.awt.Font("Dialog", 0, 24));
        jScrollPane3.setViewportView(blockChannelCoderOutput);

        javax.swing.GroupLayout blockChannelCoderOutputPanelLayout = new javax.swing.GroupLayout(blockChannelCoderOutputPanel);
        blockChannelCoderOutputPanel.setLayout(blockChannelCoderOutputPanelLayout);
        blockChannelCoderOutputPanelLayout.setHorizontalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE)
        );
        blockChannelCoderOutputPanelLayout.setVerticalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelCoderLayout = new javax.swing.GroupLayout(blockChannelCoder);
        blockChannelCoder.setLayout(blockChannelCoderLayout);
        blockChannelCoderLayout.setHorizontalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelCoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelCoderLayout.setVerticalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelCoderOutputPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Кодер каналу", blockChannelCoder);

        blockChannelCoderVideoSequence.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelCoderVideoSequenceComponentShown(evt);
            }
        });

        blockChannelVideoSequenceOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Відеопослідовність"));

        javax.swing.GroupLayout blockChannelVideoSequenceOutputFieldLayout = new javax.swing.GroupLayout(blockChannelVideoSequenceOutputField);
        blockChannelVideoSequenceOutputField.setLayout(blockChannelVideoSequenceOutputFieldLayout);
        blockChannelVideoSequenceOutputFieldLayout.setHorizontalGroup(
            blockChannelVideoSequenceOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        blockChannelVideoSequenceOutputFieldLayout.setVerticalGroup(
            blockChannelVideoSequenceOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelVideoSequenceOutputPanelLayout = new javax.swing.GroupLayout(blockChannelVideoSequenceOutputPanel);
        blockChannelVideoSequenceOutputPanel.setLayout(blockChannelVideoSequenceOutputPanelLayout);
        blockChannelVideoSequenceOutputPanelLayout.setHorizontalGroup(
            blockChannelVideoSequenceOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelVideoSequenceOutputPanelLayout.setVerticalGroup(
            blockChannelVideoSequenceOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelCoderVideoSequenceLayout = new javax.swing.GroupLayout(blockChannelCoderVideoSequence);
        blockChannelCoderVideoSequence.setLayout(blockChannelCoderVideoSequenceLayout);
        blockChannelCoderVideoSequenceLayout.setHorizontalGroup(
            blockChannelCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelVideoSequenceOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelCoderVideoSequenceLayout.setVerticalGroup(
            blockChannelCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelVideoSequenceOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Кодована відеопослідовність", blockChannelCoderVideoSequence);

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
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        modulatorOutputFieldLayout.setVerticalGroup(
            modulatorOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
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
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        channelOutputFieldLayout.setVerticalGroup(
            channelOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
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

        blockMultiplier0.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMultiplier0ComponentShown(evt);
            }
        });

        multiplierOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід помножувача 0"));

        javax.swing.GroupLayout multiplierOutputField0Layout = new javax.swing.GroupLayout(multiplierOutputField0);
        multiplierOutputField0.setLayout(multiplierOutputField0Layout);
        multiplierOutputField0Layout.setHorizontalGroup(
            multiplierOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        multiplierOutputField0Layout.setVerticalGroup(
            multiplierOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
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

        blockMultiplier1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMultiplier1ComponentShown(evt);
            }
        });

        multiplierOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід помножувача 1"));

        javax.swing.GroupLayout multiplierOutputField1Layout = new javax.swing.GroupLayout(multiplierOutputField1);
        multiplierOutputField1.setLayout(multiplierOutputField1Layout);
        multiplierOutputField1Layout.setHorizontalGroup(
            multiplierOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        multiplierOutputField1Layout.setVerticalGroup(
            multiplierOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
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

        blockIntegrator0.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockIntegrator0ComponentShown(evt);
            }
        });

        integratorOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід інтегратора 0"));

        javax.swing.GroupLayout integratorOutputField0Layout = new javax.swing.GroupLayout(integratorOutputField0);
        integratorOutputField0.setLayout(integratorOutputField0Layout);
        integratorOutputField0Layout.setHorizontalGroup(
            integratorOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        integratorOutputField0Layout.setVerticalGroup(
            integratorOutputField0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
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

        blockIntegrator1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockIntegrator1ComponentShown(evt);
            }
        });

        integratorOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід інтегратора 1"));

        javax.swing.GroupLayout integratorOutputField1Layout = new javax.swing.GroupLayout(integratorOutputField1);
        integratorOutputField1.setLayout(integratorOutputField1Layout);
        integratorOutputField1Layout.setHorizontalGroup(
            integratorOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        integratorOutputField1Layout.setVerticalGroup(
            integratorOutputField1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
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

        blockSummator.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockSummatorComponentShown(evt);
            }
        });

        blockSummatorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід суматора"));

        javax.swing.GroupLayout blockSummatorOutputFieldLayout = new javax.swing.GroupLayout(blockSummatorOutputField);
        blockSummatorOutputField.setLayout(blockSummatorOutputFieldLayout);
        blockSummatorOutputFieldLayout.setHorizontalGroup(
            blockSummatorOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        blockSummatorOutputFieldLayout.setVerticalGroup(
            blockSummatorOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSummatorOutputPanelLayout = new javax.swing.GroupLayout(blockSummatorOutputPanel);
        blockSummatorOutputPanel.setLayout(blockSummatorOutputPanelLayout);
        blockSummatorOutputPanelLayout.setHorizontalGroup(
            blockSummatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSummatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSummatorOutputPanelLayout.setVerticalGroup(
            blockSummatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSummatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        blockResolverOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід вирішуючого пристрою"));

        blockResolverOutput.setContentType("text/html");
        blockResolverOutput.setEditable(false);
        blockResolverOutput.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jScrollPane5.setViewportView(blockResolverOutput);

        javax.swing.GroupLayout blockResolverOutputPanelLayout = new javax.swing.GroupLayout(blockResolverOutputPanel);
        blockResolverOutputPanel.setLayout(blockResolverOutputPanelLayout);
        blockResolverOutputPanelLayout.setHorizontalGroup(
            blockResolverOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE)
        );
        blockResolverOutputPanelLayout.setVerticalGroup(
            blockResolverOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockResolverLayout = new javax.swing.GroupLayout(blockResolver);
        blockResolver.setLayout(blockResolverLayout);
        blockResolverLayout.setHorizontalGroup(
            blockResolverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockResolverLayout.setVerticalGroup(
            blockResolverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Вирішуючий пристрій", blockResolver);

        blockResolverVideoSequence.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockResolverVideoSequenceComponentShown(evt);
            }
        });

        blockResolverVideoSequenceOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Відеопослідовність на виході вирішуючого пристрою"));

        javax.swing.GroupLayout blockResolverVideoSequenceOutputFieldLayout = new javax.swing.GroupLayout(blockResolverVideoSequenceOutputField);
        blockResolverVideoSequenceOutputField.setLayout(blockResolverVideoSequenceOutputFieldLayout);
        blockResolverVideoSequenceOutputFieldLayout.setHorizontalGroup(
            blockResolverVideoSequenceOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        blockResolverVideoSequenceOutputFieldLayout.setVerticalGroup(
            blockResolverVideoSequenceOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockResolverVideoSequenceOutputPanelLayout = new javax.swing.GroupLayout(blockResolverVideoSequenceOutputPanel);
        blockResolverVideoSequenceOutputPanel.setLayout(blockResolverVideoSequenceOutputPanelLayout);
        blockResolverVideoSequenceOutputPanelLayout.setHorizontalGroup(
            blockResolverVideoSequenceOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockResolverVideoSequenceOutputPanelLayout.setVerticalGroup(
            blockResolverVideoSequenceOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockResolverVideoSequenceLayout = new javax.swing.GroupLayout(blockResolverVideoSequence);
        blockResolverVideoSequence.setLayout(blockResolverVideoSequenceLayout);
        blockResolverVideoSequenceLayout.setHorizontalGroup(
            blockResolverVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverVideoSequenceOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockResolverVideoSequenceLayout.setVerticalGroup(
            blockResolverVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverVideoSequenceOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Вхідна відеопослідовність", blockResolverVideoSequence);

        blockChannelDecoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelDecoderComponentShown(evt);
            }
        });

        blockChannelDecoderOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід декодера каналу"));

        blockChannelDecoderOutput.setContentType("text/html");
        blockChannelDecoderOutput.setEditable(false);
        blockChannelDecoderOutput.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jScrollPane6.setViewportView(blockChannelDecoderOutput);

        javax.swing.GroupLayout blockChannelDecoderOutputPanelLayout = new javax.swing.GroupLayout(blockChannelDecoderOutputPanel);
        blockChannelDecoderOutputPanel.setLayout(blockChannelDecoderOutputPanelLayout);
        blockChannelDecoderOutputPanelLayout.setHorizontalGroup(
            blockChannelDecoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE)
        );
        blockChannelDecoderOutputPanelLayout.setVerticalGroup(
            blockChannelDecoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelDecoderLayout = new javax.swing.GroupLayout(blockChannelDecoder);
        blockChannelDecoder.setLayout(blockChannelDecoderLayout);
        blockChannelDecoderLayout.setHorizontalGroup(
            blockChannelDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelDecoderLayout.setVerticalGroup(
            blockChannelDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Декодер каналу", blockChannelDecoder);

        blockChannelDecoderVideoSequence.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelDecoderVideoSequenceComponentShown(evt);
            }
        });

        blockChannelDecoderVideoSequenceOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Декодована відеопослідовність"));

        javax.swing.GroupLayout blockChannelDecoderVideoSequenceOutputFieldLayout = new javax.swing.GroupLayout(blockChannelDecoderVideoSequenceOutputField);
        blockChannelDecoderVideoSequenceOutputField.setLayout(blockChannelDecoderVideoSequenceOutputFieldLayout);
        blockChannelDecoderVideoSequenceOutputFieldLayout.setHorizontalGroup(
            blockChannelDecoderVideoSequenceOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        blockChannelDecoderVideoSequenceOutputFieldLayout.setVerticalGroup(
            blockChannelDecoderVideoSequenceOutputFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelDecoderVideoSequenceOutputPanelLayout = new javax.swing.GroupLayout(blockChannelDecoderVideoSequenceOutputPanel);
        blockChannelDecoderVideoSequenceOutputPanel.setLayout(blockChannelDecoderVideoSequenceOutputPanelLayout);
        blockChannelDecoderVideoSequenceOutputPanelLayout.setHorizontalGroup(
            blockChannelDecoderVideoSequenceOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelDecoderVideoSequenceOutputPanelLayout.setVerticalGroup(
            blockChannelDecoderVideoSequenceOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelDecoderVideoSequenceLayout = new javax.swing.GroupLayout(blockChannelDecoderVideoSequence);
        blockChannelDecoderVideoSequence.setLayout(blockChannelDecoderVideoSequenceLayout);
        blockChannelDecoderVideoSequenceLayout.setHorizontalGroup(
            blockChannelDecoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderVideoSequenceOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelDecoderVideoSequenceLayout.setVerticalGroup(
            blockChannelDecoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderVideoSequenceOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Декодована відеопослідовність", blockChannelDecoderVideoSequence);

        blockMessageReceiver.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMessageReceiverComponentShown(evt);
            }
        });

        receivedMessagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Повідомлення"));

        receivedMessageArea.setContentType("text/html");
        receivedMessageArea.setEditable(false);
        receivedMessageArea.setFont(new java.awt.Font("Dialog", 0, 24));
        jScrollPane4.setViewportView(receivedMessageArea);

        javax.swing.GroupLayout receivedMessagePanelLayout = new javax.swing.GroupLayout(receivedMessagePanel);
        receivedMessagePanel.setLayout(receivedMessagePanelLayout);
        receivedMessagePanelLayout.setHorizontalGroup(
            receivedMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE)
        );
        receivedMessagePanelLayout.setVerticalGroup(
            receivedMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
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

        multiplier0Button.setBackground(new java.awt.Color(240, 240, 240));
        multiplier0Button.setText("П0");
        multiplier0Button.setToolTipText("Помножувач 0");
        multiplier0Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiplier0ButtonActionPerformed(evt);
            }
        });

        multiplier1Button.setBackground(new java.awt.Color(240, 240, 240));
        multiplier1Button.setText("П1");
        multiplier1Button.setToolTipText("Помножувач 1");
        multiplier1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiplier1ButtonActionPerformed(evt);
            }
        });

        integrator0Button.setBackground(new java.awt.Color(240, 240, 240));
        integrator0Button.setText("І0");
        integrator0Button.setToolTipText("Інтегратор 0");
        integrator0Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrator0ButtonActionPerformed(evt);
            }
        });

        integrator1Button.setBackground(new java.awt.Color(240, 240, 240));
        integrator1Button.setText("І1");
        integrator1Button.setToolTipText("Інтегратор 1");
        integrator1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrator1ButtonActionPerformed(evt);
            }
        });

        summatorButton.setBackground(new java.awt.Color(240, 240, 240));
        summatorButton.setText("С");
        summatorButton.setToolTipText("Суматор");
        summatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summatorButtonActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/right_arrow.png"))); // NOI18N

        javax.swing.GroupLayout systemSchemeLayout = new javax.swing.GroupLayout(systemScheme);
        systemScheme.setLayout(systemSchemeLayout);
        systemSchemeLayout.setHorizontalGroup(
            systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemSchemeLayout.createSequentialGroup()
                .addComponent(messageSourceButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceCoderButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelCoderButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemSchemeLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modulatorButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(channelButton)
                        .addGap(232, 232, 232)
                        .addComponent(summatorButton))
                    .addGroup(systemSchemeLayout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(multiplier0Button)
                            .addComponent(multiplier1Button))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(integrator1Button)
                            .addComponent(integrator0Button))))
                .addContainerGap(302, Short.MAX_VALUE))
        );
        systemSchemeLayout.setVerticalGroup(
            systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemSchemeLayout.createSequentialGroup()
                .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemSchemeLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(multiplier0Button)
                            .addComponent(jLabel14)
                            .addComponent(integrator0Button)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel11)
                            .addComponent(modulatorButton)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel13)
                            .addComponent(multiplier1Button)
                            .addComponent(jLabel15)
                            .addComponent(integrator1Button)))
                    .addGroup(systemSchemeLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8)
                            .addComponent(messageSourceButton)
                            .addComponent(sourceCoderButton)
                            .addComponent(jLabel9)
                            .addComponent(channelCoderButton)))
                    .addGroup(systemSchemeLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(systemSchemeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(channelButton)
                            .addComponent(summatorButton))))
                .addContainerGap(32, Short.MAX_VALUE))
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
            .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 1043, Short.MAX_VALUE)
            .addComponent(systemScheme, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(systemScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitItemActionPerformed
    {//GEN-HEADEREND:event_exitItemActionPerformed
	System.exit(0);
    }//GEN-LAST:event_exitItemActionPerformed

    //checks summing by module 2
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
	message = blockMessageArea.getText();
	if (!message.isEmpty())
	{
	    doSourceCoding();
	    doSourceVideoSequence();
	    doChannelCoding();
	    doChannelVideoSequence();
	    doModulating();
	    doChannel();
	    doMultiplying();
	    doIntegrating();
	    doSumming();
	    doResolving();
	    doChannelDecoding();
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

    //implements test function to integrate
    private class TFun implements IntegralFunction
    {
	public double function(double x)
	{
	    return Math.abs(Math.sin(x)) + Math.exp(x);
	}
    }

    //integrating demo
    private void integrateItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_integrateItemActionPerformed
    {//GEN-HEADEREND:event_integrateItemActionPerformed
	TFun testFunction = new TFun();
	double result = Integration.gaussQuad(testFunction, 0, 2 * Math.PI, 128);
	System.out.printf("Integral of |sin(x)|+e^x from 0 to 2pi: %1.4f\n", result);
    }//GEN-LAST:event_integrateItemActionPerformed

    //blocking demo
    private void blockingItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_blockingItemActionPerformed
    {//GEN-HEADEREND:event_blockingItemActionPerformed
	BinaryNumber test1 = new BinaryNumber("11011010001101");
	BinaryNumber test2 = new BinaryNumber("01011");
	BinaryNumber test3 = new BinaryNumber("100111");
	List<BinaryNumber> testList = new ArrayList<BinaryNumber>();
	testList.add(test1);
	testList.add(test2);
	testList.add(test3);
	Splitter test_blocker = new Splitter(testList, 4);
	test_blocker.doSplitting();
	List<BinaryNumber> blocks = test_blocker.getBlocks();
	for (BinaryNumber bn: blocks)
	    System.out.println(bn.getStringSequence());
    }//GEN-LAST:event_blockingItemActionPerformed

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

    private void blockChannelCoderVideoSequenceComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelCoderVideoSequenceComponentShown
    {//GEN-HEADEREND:event_blockChannelCoderVideoSequenceComponentShown
	selectedBlock = Blocks.CHANNEL_VIDEOSEQUENCE;
	updateChosenBlock();
    }//GEN-LAST:event_blockChannelCoderVideoSequenceComponentShown

    private void doModellingOptionsItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_doModellingOptionsItemActionPerformed
    {//GEN-HEADEREND:event_doModellingOptionsItemActionPerformed
	modellingOptionsDialog.setSize(670, 180);
	modellingOptionsDialog.setVisible(true);
    }//GEN-LAST:event_doModellingOptionsItemActionPerformed

    private void blockSourceCoderVideoSequenceComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockSourceCoderVideoSequenceComponentShown
    {//GEN-HEADEREND:event_blockSourceCoderVideoSequenceComponentShown
	selectedBlock = Blocks.SOURCE_VIDEOSEQUENCE;
	updateChosenBlock();
    }//GEN-LAST:event_blockSourceCoderVideoSequenceComponentShown

    private void blockResolverComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockResolverComponentShown
    {//GEN-HEADEREND:event_blockResolverComponentShown
	selectedBlock = Blocks.RESOLVER;
	updateChosenBlock();
    }//GEN-LAST:event_blockResolverComponentShown

    private void blockMessageReceiverComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockMessageReceiverComponentShown
    {//GEN-HEADEREND:event_blockMessageReceiverComponentShown
	selectedBlock = Blocks.MESSAGE_RECEIVER;
	updateChosenBlock();
    }//GEN-LAST:event_blockMessageReceiverComponentShown

    private void blockResolverVideoSequenceComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockResolverVideoSequenceComponentShown
    {//GEN-HEADEREND:event_blockResolverVideoSequenceComponentShown
	selectedBlock = Blocks.INPUT_VIDEOSEQUENCE;
	updateChosenBlock();
    }//GEN-LAST:event_blockResolverVideoSequenceComponentShown

    private void blockChannelDecoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelDecoderComponentShown
    {//GEN-HEADEREND:event_blockChannelDecoderComponentShown
	selectedBlock = Blocks.CHANNEL_DECODER;
	updateChosenBlock();
    }//GEN-LAST:event_blockChannelDecoderComponentShown

    private void blockChannelDecoderVideoSequenceComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelDecoderVideoSequenceComponentShown
    {//GEN-HEADEREND:event_blockChannelDecoderVideoSequenceComponentShown
	selectedBlock = Blocks.CHANNEL_DECODER_VIDEOSEQUENCE;
	updateChosenBlock();
    }//GEN-LAST:event_blockChannelDecoderVideoSequenceComponentShown

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
    private javax.swing.JTextPane blockChannelCoderOutput;
    private javax.swing.JPanel blockChannelCoderOutputPanel;
    private javax.swing.JPanel blockChannelCoderVideoSequence;
    private javax.swing.JPanel blockChannelDecoder;
    private javax.swing.JTextPane blockChannelDecoderOutput;
    private javax.swing.JPanel blockChannelDecoderOutputPanel;
    private javax.swing.JPanel blockChannelDecoderVideoSequence;
    private javax.swing.JPanel blockChannelDecoderVideoSequenceOutputField;
    private javax.swing.JPanel blockChannelDecoderVideoSequenceOutputPanel;
    private javax.swing.JPanel blockChannelVideoSequenceOutputField;
    private javax.swing.JPanel blockChannelVideoSequenceOutputPanel;
    private javax.swing.JPanel blockIntegrator0;
    private javax.swing.JPanel blockIntegrator1;
    private javax.swing.JTextArea blockMessageArea;
    private javax.swing.JPanel blockMessageReceiver;
    private javax.swing.JPanel blockMessageSource;
    private javax.swing.JPanel blockModulator;
    private javax.swing.JPanel blockMultiplier0;
    private javax.swing.JPanel blockMultiplier1;
    private javax.swing.JPanel blockResolver;
    private javax.swing.JTextPane blockResolverOutput;
    private javax.swing.JPanel blockResolverOutputPanel;
    private javax.swing.JPanel blockResolverVideoSequence;
    private javax.swing.JPanel blockResolverVideoSequenceOutputField;
    private javax.swing.JPanel blockResolverVideoSequenceOutputPanel;
    private javax.swing.JPanel blockSourceCoder;
    private javax.swing.JTextPane blockSourceCoderOutput;
    private javax.swing.JPanel blockSourceCoderOutputPanel;
    private javax.swing.JPanel blockSourceCoderVideoSequence;
    private javax.swing.JPanel blockSourceVideoSequenceOutputField;
    private javax.swing.JPanel blockSourceVideoSequenceOutputPanel;
    private javax.swing.JPanel blockSummator;
    private javax.swing.JPanel blockSummatorOutputField;
    private javax.swing.JPanel blockSummatorOutputPanel;
    private javax.swing.JMenuItem blockingItem;
    private javax.swing.JLabel bpsLabel;
    private javax.swing.JButton channelButton;
    private javax.swing.JButton channelCoderButton;
    private javax.swing.JPanel channelCoderTab;
    private javax.swing.JComboBox channelCodesChooser;
    private javax.swing.JLabel channelCodesChooserLabel;
    private javax.swing.JPanel channelOutputField;
    private javax.swing.JPanel channelOutputPanel;
    private javax.swing.JPanel channelTab;
    private javax.swing.JMenu developerMenu;
    private javax.swing.JMenuItem doModellingItem;
    private javax.swing.JMenuItem doModellingOptionsItem;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel hzBearerLabel;
    private javax.swing.JLabel hzDeviationLabel;
    private javax.swing.JSpinner informationalSpeed;
    private javax.swing.JLabel informationalSpeedLabel;
    private javax.swing.JMenuItem integrateItem;
    private javax.swing.JButton integrator0Button;
    private javax.swing.JButton integrator1Button;
    private javax.swing.JPanel integratorOutputField0;
    private javax.swing.JPanel integratorOutputField1;
    private javax.swing.JPanel integratorOutputPanel0;
    private javax.swing.JPanel integratorOutputPanel1;
    private javax.swing.JMenuItem inversionItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
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
    private javax.swing.JTextPane receivedMessageArea;
    private javax.swing.JPanel receivedMessagePanel;
    private javax.swing.JMenuItem shl2Item;
    private javax.swing.JButton sourceCoderButton;
    private javax.swing.JPanel sourceCoderTab;
    private javax.swing.JComboBox sourceCodesChooser;
    private javax.swing.JLabel sourceCodesChooserLabel;
    private javax.swing.JPanel sourceMessagePanel;
    private javax.swing.JMenuItem sum2Item;
    private javax.swing.JButton summatorButton;
    private javax.swing.JPanel systemScheme;
    private javax.swing.JLabel voltsLabel;
    private javax.swing.JMenuItem weightItem;
    // End of variables declaration//GEN-END:variables

}
