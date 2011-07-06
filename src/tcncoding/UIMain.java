/*

 Copyright (C) 2009-2011 Oleksandr Natalenko aka post-factum

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

package tcncoding;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Main UI
 * @author post-factum
 */
public class UIMain extends javax.swing.JFrame
{
    //UI parts
    SourceCoderController currentSourceCoder;
    VideoCreator currentSourceVideoCreator;
    ChannelCoderController currentChannelCoder;
    VideoCreator currentChannelVideoCreator;
    ModulatorController currentModulator;
    NoiseGenerator currentNoiseGenerator;
    Channel currentChannel;
    ReferenceGenerator currentReferenceGenerator0, currentReferenceGenerator1;
    Multiplier currentMultiplier0, currentMultiplier1;
    Digitizer currentDigitizer0, currentDigitizer1;
    Integrator currentIntegrator0, currentIntegrator1;
    Summator currentSummator;
    Resolver currentResolver;
    VideoCreator currentResolverVideoCreator;
    ChannelDecoderController currentChannelDecoder;
    VideoCreator currentChannelDecoderVideoCreator;
    SourceDecoderController currentSourceDecoder;

    //UI blocks
    enum Blocks {MESSAGE_SOURCE, SOURCE_CODER, CHANNEL_CODER, MODULATOR, NOISE_GENERATOR, CHANNEL, REFERENCE_GENERATOR0, REFERENCE_GENERATOR1, MULTIPLIER0, MULTIPLIER1, INTEGRATOR0, INTEGRATOR1, SUMMATOR, RESOLVER, SOURCE_DECODER, CHANNEL_DECODER;};

    //UI vizualization tools
    DataVizualizator currentSourceVideoSequenceVizualizator;
    DataVizualizator currentChannelVideoSequenceVizualizator;
    DataVizualizator currentModulatorVizualizator;
    DataVizualizator currentNoiseGeneratorVizualizator;
    DataVizualizator currentChannelVizualizator;
    DataVizualizator currentReferenceGeneratorVizualizator0;
    DataVizualizator currentReferenceGeneratorVizualizator1;
    DataVizualizator currentMultiplierVizualizator0;
    DataVizualizator currentMultiplierVizualizator1;
    DataVizualizator currentIntegratorVizualizator0;
    DataVizualizator currentIntegratorVizualizator1;
    DataVizualizator currentSummatorVizualizator;
    DataVizualizator currentResolverVideoSequenceVizualizator;
    DataVizualizator currentChannelDecoderVideoSequenceVizualizator;

    //source message
    String message = "";

    //simulating parameters
    SourceCoderController.SourceCoderCode sourceCode = SourceCoderController.SourceCoderCode.MTK2;
    ChannelCoderController.ChannelCoderCode channelCode = ChannelCoderController.ChannelCoderCode.PARITY_BIT;
    ModulatorController.ModulationType modulationType = ModulatorController.ModulationType.ASK;

    //source coder
    List<BinaryNumber> sourceSymbols;
    List<Integer> lengthMap;
    boolean isCyr = true;

    //channel coder
    List<BinaryNumber> channelSymbols;
    int headLength;
    boolean useChannelCoderTrigger = true;

    //source videosequence
    double sourceImpulseLength;
    List<DigitalSignal> sourceVideoSequence;
    DataVizualizatorProvider sourceVideoSequenceSingleProvider;
    List<DataVizualizatorProvider> sourceVideoSequenceProvider;

    //channel videosequence
    double channelImpulseLength;
    List<DigitalSignal> channelVideoSequence;
    List<DataVizualizatorProvider> channelVideoSequenceProvider;

    //ModulatorController data
    List<ModulatorSignal> modulatorData;
    List<DataVizualizatorProvider> modulatorDataProvider;

    //NoiseGenerator data
    List<NoiseSignal> noiseSignals;
    List<DataVizualizatorProvider> noiseGeneratorDataProvider;

    //Channel data
    List<ChannelSignal> channelOutput;
    List<DataVizualizatorProvider> channelOutputProvider;
    boolean useNoiseErrorsTrigger = true, forceErrorsTrigger = false, injectErrorsPerBlock = true;

    //reference generators data
    List<ModulatorSignal> referenceGenerator0Output;
    List<ModulatorSignal> referenceGenerator1Output;
    List<DataVizualizatorProvider> referenceGenerator0OutputProvider;
    List<DataVizualizatorProvider> referenceGenerator1OutputProvider;

    //multipliers data
    List<MultiplierSignal> multiplier0Output;
    List<MultiplierSignal> multiplier1Output;
    List<DataVizualizatorProvider> multiplier0OutputProvider;
    List<DataVizualizatorProvider> multiplier1OutputProvider;
    
    //digitizers data
    List<DigitalSignal> digitizer0Output;
    List<DigitalSignal> digitizer1Output;

    //integrators data
    double maxFrequency;
    List<DigitalSignal> integrator0Output;
    List<DigitalSignal> integrator1Output;
    List<DataVizualizatorProvider> integrator0OutputProvider;
    List<DataVizualizatorProvider> integrator1OutputProvider;

    //Summator data
    List<DigitalSignal> summatorOutput;
    List<DataVizualizatorProvider> summatorOutputProvider;

    //Resolver data
    List<Boolean> resolverOutput;
    List<DigitalSignal> resolverVideoSequence;
    List<DataVizualizatorProvider> resolverVideoSequenceProvider;

    //Channel decoder data
    List<BinaryNumber> channelDecoderOutput;
    List<DigitalSignal> channelDecoderVideoSequence;
    List<DataVizualizatorProvider> channelDecoderVideoSequenceProvider;

    //acts on choosing code of source
    void updateChosenCodeSource()
    {
	switch (sourceCodesChooser.getSelectedIndex())
	{
	    case 0:
		sourceCode = SourceCoderController.SourceCoderCode.MTK2;
		break;
	    case 1:
		sourceCode = SourceCoderController.SourceCoderCode.MTK5;
		break;
	    case 2:
		sourceCode = SourceCoderController.SourceCoderCode.KOI8U;
		break;
	    case 3:
		sourceCode = SourceCoderController.SourceCoderCode.MORSE;
		break;
	    case 4:
		sourceCode = SourceCoderController.SourceCoderCode.SHANNON;
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
		channelCode = ChannelCoderController.ChannelCoderCode.PARITY_BIT;
		break;
	    case 1:
		channelCode = ChannelCoderController.ChannelCoderCode.INVERSED;
		break;
	    case 2:
		channelCode = ChannelCoderController.ChannelCoderCode.MANCHESTER;
		break;
	    case 3:
		channelCode = ChannelCoderController.ChannelCoderCode.HAMMING;
		break;
            case 4:
		channelCode = ChannelCoderController.ChannelCoderCode.CYCLIC;
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
		modulationType = ModulatorController.ModulationType.ASK;
		break;
	    case 1:
		modulationType = ModulatorController.ModulationType.FSK;
		//enable bearer frequency deviation options
		bearerFrequencyDeviation.setEnabled(true);
		bearerFrequencyDeviationLabel.setEnabled(true);
		break;
	    case 2:
		modulationType = ModulatorController.ModulationType.PSK;
		break;
	    case 3:
		modulationType = ModulatorController.ModulationType.RPSK;
		break;
	    default:
		break;
	}
    }

    //acts on block changing
    void updateChosenBlock(Blocks _block)
    {
        Color inactiveColor = new Color(240, 240, 240);
        Color activeColor = new Color(200, 200, 200);
	//make all buttons unselected
	messageSourceButton.setBackground(inactiveColor);
	sourceCoderButton.setBackground(inactiveColor);
	channelCoderButton.setBackground(inactiveColor);
	modulatorButton.setBackground(inactiveColor);
        noiseButton.setBackground(inactiveColor);
	channelButton.setBackground(inactiveColor);
	referenceGenerator0Button.setBackground(inactiveColor);
	referenceGenerator1Button.setBackground(inactiveColor);
	multiplier0Button.setBackground(inactiveColor);
	multiplier1Button.setBackground(inactiveColor);
	integrator0Button.setBackground(inactiveColor);
	integrator1Button.setBackground(inactiveColor);
	summatorButton.setBackground(inactiveColor);
	resolverButton.setBackground(inactiveColor);
	channelDecoderButton.setBackground(inactiveColor);
	sourceDecoderButton.setBackground(inactiveColor);
	//highlight selected block
	switch (_block)
	{
	    case MESSAGE_SOURCE:
		TCSTabs.setSelectedComponent(blockMessageSource);
		messageSourceButton.setBackground(activeColor);
		break;
	    case SOURCE_CODER:
		TCSTabs.setSelectedComponent(blockSourceCoder);
		sourceCoderButton.setBackground(activeColor);
		break;
	    case CHANNEL_CODER:
		TCSTabs.setSelectedComponent(blockChannelCoder);
		channelCoderButton.setBackground(activeColor);
		break;
	    case MODULATOR:
		TCSTabs.setSelectedComponent(blockModulator);
		modulatorButton.setBackground(activeColor);
		break;
            case NOISE_GENERATOR:
                TCSTabs.setSelectedComponent(blockNoiseGenerator);
                noiseButton.setBackground(activeColor);
                break;
	    case CHANNEL:
		TCSTabs.setSelectedComponent(blockChannel);
		channelButton.setBackground(activeColor);
		break;
	    case REFERENCE_GENERATOR0:
		TCSTabs.setSelectedComponent(blockReferenceGenerator0);
		referenceGenerator0Button.setBackground(activeColor);
		break;
	    case REFERENCE_GENERATOR1:
		TCSTabs.setSelectedComponent(blockReferenceGenerator1);
		referenceGenerator1Button.setBackground(activeColor);
		break;
	    case MULTIPLIER0:
		TCSTabs.setSelectedComponent(blockMultiplier0);
		multiplier0Button.setBackground(activeColor);
		break;
	    case MULTIPLIER1:
		TCSTabs.setSelectedComponent(blockMultiplier1);
		multiplier1Button.setBackground(activeColor);
		break;
	    case INTEGRATOR0:
		TCSTabs.setSelectedComponent(blockIntegrator0);
		integrator0Button.setBackground(activeColor);
		break;
	    case INTEGRATOR1:
		TCSTabs.setSelectedComponent(blockIntegrator1);
		integrator1Button.setBackground(activeColor);
		break;
	    case SUMMATOR:
		TCSTabs.setSelectedComponent(blockSummator);
		summatorButton.setBackground(activeColor);
		break;
	    case RESOLVER:
		TCSTabs.setSelectedComponent(blockResolver);
		resolverButton.setBackground(activeColor);
		break;
	    case CHANNEL_DECODER:
		TCSTabs.setSelectedComponent(blockChannelDecoder);
		channelDecoderButton.setBackground(activeColor);
		break;
	    case SOURCE_DECODER:
		TCSTabs.setSelectedComponent(blockSourceDecoder);
		sourceDecoderButton.setBackground(activeColor);
		break;
	    default:
		break;
	}
    }

    //encodes source message with selected source code
    void doSourceCoding()
    {
	currentSourceCoder = new SourceCoderController(sourceCode, message);
	sourceSymbols = currentSourceCoder.getSequence();
	lengthMap = currentSourceCoder.getLengthMap();
	isCyr = currentSourceCoder.isCyrillic();
	blockSourceCoderOutput.setText(currentSourceCoder.getHTMLStringSequence());
    }

    //encodes source code with selected Channel code
    void doChannelCoding()
    {
	currentChannelCoder = new ChannelCoderController(sourceSymbols, channelCode, useChannelCoderTrigger);
	channelSymbols = currentChannelCoder.getSequence();
	blockChannelCoderOutput.setText(currentChannelCoder.getHTMLReport());
	headLength = currentChannelCoder.getHeadLength();
    }

    //shows source videosequence
    void doSourceCodingVideoSequence()
    {
	//calculates source impulse length according to chosen informational speed
	sourceImpulseLength = 1 / (Double)informationalSpeed.getValue();

        BitsRectifier sourceRectifier = new BitsRectifier(sourceSymbols);
	currentSourceVideoCreator = new VideoCreator(sourceRectifier.getBits(), sourceImpulseLength, 1);
	sourceVideoSequence = currentSourceVideoCreator.getVideoSequence();
	if (currentSourceVideoSequenceVizualizator != null)
	{
	    blockSourceVideoSequenceOutputField.remove(currentSourceVideoSequenceVizualizator);
	    currentSourceVideoSequenceVizualizator = null;
	}
	sourceVideoSequenceProvider = new ArrayList<DataVizualizatorProvider>();
	sourceVideoSequenceSingleProvider = new DataVizualizatorProvider(sourceVideoSequence, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SOURCE VIDEOSEQUENCE"), Color.BLUE);
	sourceVideoSequenceProvider.add(sourceVideoSequenceSingleProvider);
	int cx = blockSourceVideoSequenceOutputField.getWidth();
	int cy = blockSourceVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentSourceVideoSequenceVizualizator = new DataVizualizator(sourceVideoSequenceProvider, cx, cy, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SSV(T), V"), (Integer)linesWidth.getValue());

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

        BitsRectifier channelRectifier = new BitsRectifier(channelSymbols);
        currentChannelVideoCreator = new VideoCreator(channelRectifier.getBits(), channelImpulseLength, 0.75);
	channelVideoSequence = currentChannelVideoCreator.getVideoSequence();
	if (currentChannelVideoSequenceVizualizator != null)
	{
	    blockChannelVideoSequenceOutputField.remove(currentChannelVideoSequenceVizualizator);
	    currentChannelVideoSequenceVizualizator = null;
	}
	channelVideoSequenceProvider = new ArrayList<DataVizualizatorProvider>();
	channelVideoSequenceProvider.add(sourceVideoSequenceSingleProvider);
	channelVideoSequenceProvider.add(new DataVizualizatorProvider(channelVideoSequence, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("CHANNEL VIDEOSEQUENCE"), Color.RED));
	int cx = blockChannelVideoSequenceOutputField.getWidth();
	int cy = blockChannelVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentChannelVideoSequenceVizualizator = new DataVizualizator(channelVideoSequenceProvider, cx, cy, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SCHV(T), V"), (Integer)linesWidth.getValue());

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
	currentModulator = new ModulatorController(modulationType, (Double)bearerAmplitude.getValue(), (Double)bearerFrequency.getValue(), (Double)bearerFrequencyDeviation.getValue(), channelSymbols, channelImpulseLength);
	modulatorData = currentModulator.getSignals();

	//removes old vizualizator if exists
	if (currentModulatorVizualizator != null)
	{
	    blockModulatorOutputField.remove(currentModulatorVizualizator);
	    currentModulatorVizualizator = null;
	}
	//creates new vizualizator data provider
	modulatorDataProvider = new ArrayList<DataVizualizatorProvider>();
	modulatorDataProvider.add(new DataVizualizatorProvider(modulatorData, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("MODULATOR OUTPUT SIGNAL"), Color.BLUE));
	//gets chart width and height
	int cx = blockModulatorOutputField.getWidth();
	int cy = blockModulatorOutputField.getHeight();
	//creates new vizualizator
	currentModulatorVizualizator = new DataVizualizator(modulatorDataProvider, cx, cy, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SMOD(T), V"), (Integer)linesWidth.getValue());
	//shows chart
	currentModulatorVizualizator.setVisible(true);
	blockModulatorOutputField.add(currentModulatorVizualizator);
	
	//repaints chart to show it if modulator block is active
	currentModulatorVizualizator.repaint();
    }

    //generates noise
    void doNoiseCreating()
    {
        //gets noise generator data
        currentNoiseGenerator = new NoiseGenerator(modulatorData, (Double)noisePower.getValue());
        noiseSignals = currentNoiseGenerator.getSignals();

        //removes old vizualizator if exists
	if (currentNoiseGeneratorVizualizator != null)
	{
	    blockNoiseGeneratorOutputField.remove(currentNoiseGeneratorVizualizator);
	    currentNoiseGeneratorVizualizator = null;
	}
	//creates new vizualizator data provider
	noiseGeneratorDataProvider = new ArrayList<DataVizualizatorProvider>();
	noiseGeneratorDataProvider.add(new DataVizualizatorProvider(noiseSignals, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("NOISE GENERATOR OUTPUT SIGNAL"), Color.BLUE));
	//gets chart width and height
	int cx = blockNoiseGeneratorOutputField.getWidth();
	int cy = blockNoiseGeneratorOutputField.getHeight();
	//creates new vizualizator
	currentNoiseGeneratorVizualizator = new DataVizualizator(noiseGeneratorDataProvider, cx, cy, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("Sn(T), V"), (Integer)linesWidth.getValue());

	//shows chart
	currentNoiseGeneratorVizualizator.setVisible(true);
	blockNoiseGeneratorOutputField.add(currentNoiseGeneratorVizualizator);

	//repaints chart to show it if noise generator block is active
	currentNoiseGeneratorVizualizator.repaint();
    }

    //adds noise
    void doChannel()
    {
	//gets channel output signal
	currentChannel = new Channel(modulatorData, noiseSignals);
	channelOutput = currentChannel.getSignals();

	//removes old vizualizator if exists
	if (currentChannelVizualizator != null)
	{
	    blockChannelOutputField.remove(currentChannelVizualizator);
	    currentChannelVizualizator = null;
	}
	//creates new vizualizator data provider
	channelOutputProvider = new ArrayList<DataVizualizatorProvider>();
	channelOutputProvider.add(new DataVizualizatorProvider(channelOutput, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("CHANNEL OUTPUT SIGNAL"), Color.BLUE));
	//gets chart width and height
	int cx = blockChannelOutputField.getWidth();
	int cy = blockChannelOutputField.getHeight();
	//creates new vizualizator
	currentChannelVizualizator = new DataVizualizator(channelOutputProvider, cx, cy, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("S'(T), V"), (Integer)linesWidth.getValue());

	//shows chart
	currentChannelVizualizator.setVisible(true);
	blockChannelOutputField.add(currentChannelVizualizator);

	//repaints chart to show it if channel block is active
	currentChannelVizualizator.repaint();
    }

    //generates reference signals
    void doGenerating()
    {
	switch (modulationType)
	{
	    case ASK:
		maxFrequency = (Double)bearerFrequency.getValue();
		currentReferenceGenerator0 = new ReferenceGenerator(maxFrequency, 0, 0, channelOutput);
		currentReferenceGenerator1 = new ReferenceGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), 0, channelOutput);
		break;
	    case FSK:
		maxFrequency = (Double)bearerFrequency.getValue() + (Double)bearerFrequencyDeviation.getValue();
		currentReferenceGenerator0 = new ReferenceGenerator(maxFrequency - 2 * (Double)bearerFrequencyDeviation.getValue(), (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentReferenceGenerator1 = new ReferenceGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), 0, channelOutput);
		break;
	    case PSK:
		maxFrequency = (Double)bearerFrequency.getValue();
		currentReferenceGenerator0 = new ReferenceGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentReferenceGenerator1 = new ReferenceGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), -Math.PI, channelOutput);
		break;
	    case RPSK:
		maxFrequency = (Double)bearerFrequency.getValue();
		currentReferenceGenerator0 = new ReferenceGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), 0, channelOutput);
		currentReferenceGenerator1 = new ReferenceGenerator(maxFrequency, (Double)bearerAmplitude.getValue(), -Math.PI, channelOutput);
		break;
	    default:
		break;
	}
	referenceGenerator0Output = currentReferenceGenerator0.getSignals();
	referenceGenerator1Output = currentReferenceGenerator1.getSignals();

	//prepares vizualizator
	if (currentReferenceGeneratorVizualizator0 != null)
	{
	    blockReferenceGeneratorOutputField0.remove(currentReferenceGeneratorVizualizator0);
	    currentReferenceGeneratorVizualizator0 = null;
	}
	int cx0 = blockReferenceGeneratorOutputField0.getWidth();
	int cy0 = blockReferenceGeneratorOutputField0.getHeight();

	//vizualizes signal
	referenceGenerator0OutputProvider = new ArrayList<DataVizualizatorProvider>();
	referenceGenerator0OutputProvider.add(new DataVizualizatorProvider(referenceGenerator0Output, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("REFERENCE GENERATOR 0 OUTPUT SIGNAL"), Color.BLUE));
	currentReferenceGeneratorVizualizator0 = new DataVizualizator(referenceGenerator0OutputProvider, cx0, cy0, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SRG0(T), V"), (Integer)linesWidth.getValue());
	currentReferenceGeneratorVizualizator0.setVisible(true);
	blockReferenceGeneratorOutputField0.add(currentReferenceGeneratorVizualizator0);
	currentReferenceGeneratorVizualizator0.repaint();

	//does the same for second generator
	if (currentReferenceGeneratorVizualizator1 != null)
	{
	    blockReferenceGeneratorOutputField1.remove(currentReferenceGeneratorVizualizator1);
	    currentReferenceGeneratorVizualizator1 = null;
	}
	int cx1 = blockReferenceGeneratorOutputField1.getWidth();
	int cy1 = blockReferenceGeneratorOutputField1.getHeight();

	//shows multipliers charts
	referenceGenerator1OutputProvider = new ArrayList<DataVizualizatorProvider>();
	referenceGenerator1OutputProvider.add(new DataVizualizatorProvider(referenceGenerator1Output, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("REFERENCE GENERATOR 1 OUTPUT SIGNAL"), Color.BLUE));
	currentReferenceGeneratorVizualizator1 = new DataVizualizator(referenceGenerator1OutputProvider, cx1, cy1, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SRG1(T), V"), (Integer)linesWidth.getValue());
	currentReferenceGeneratorVizualizator1.setVisible(true);
	blockReferenceGeneratorOutputField1.add(currentReferenceGeneratorVizualizator1);
	currentReferenceGeneratorVizualizator1.repaint();
    }

    //multiplies channel signals and reference signals
    void doMultiplying()
    {
	//create multipliers
	currentMultiplier0 = new Multiplier(channelOutput, referenceGenerator0Output);
	currentMultiplier1 = new Multiplier(channelOutput, referenceGenerator1Output);

	//multiplies
	multiplier0Output = currentMultiplier0.getSignals();
	multiplier1Output = currentMultiplier1.getSignals();

	//prepares vizualizator
	if (currentMultiplierVizualizator0 != null)
	{
	    blockMultiplierOutputField0.remove(currentMultiplierVizualizator0);
	    currentMultiplierVizualizator0 = null;
	}
	int cx0 = blockMultiplierOutputField0.getWidth();
	int cy0 = blockMultiplierOutputField0.getHeight();

	//vizualizes signal
	multiplier0OutputProvider = new ArrayList<DataVizualizatorProvider>();
	multiplier0OutputProvider.add(new DataVizualizatorProvider(multiplier0Output, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("MULTIPLIER 0 OUTPUT SIGNAL"), Color.BLUE));
	currentMultiplierVizualizator0 = new DataVizualizator(multiplier0OutputProvider, cx0, cy0, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SM0(T), V"), (Integer)linesWidth.getValue());
	currentMultiplierVizualizator0.setVisible(true);
	blockMultiplierOutputField0.add(currentMultiplierVizualizator0);
	currentMultiplierVizualizator0.repaint();

	//does the same for second multiplier
	if (currentMultiplierVizualizator1 != null)
	{
	    blockMultiplierOutputField1.remove(currentMultiplierVizualizator1);
	    currentMultiplierVizualizator1 = null;
	}
	int cx1 = blockMultiplierOutputField1.getWidth();
	int cy1 = blockMultiplierOutputField1.getHeight();

	//shows multipliers charts
	multiplier1OutputProvider = new ArrayList<DataVizualizatorProvider>();
	multiplier1OutputProvider.add(new DataVizualizatorProvider(multiplier1Output, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("MULTIPLIER 1 OUTPUT SIGNAL"), Color.BLUE));
	currentMultiplierVizualizator1 = new DataVizualizator(multiplier1OutputProvider, cx1, cy1, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SM1(T), V"), (Integer)linesWidth.getValue());
	currentMultiplierVizualizator1.setVisible(true);
	blockMultiplierOutputField1.add(currentMultiplierVizualizator1);
	currentMultiplierVizualizator1.repaint();
    }
    
    //converts analog signal to digital
    void doDigitizing()
    {
	double end = multiplier0Output.get(multiplier0Output.size() - 1).getEnd();
        double step = Math.min(end / blockIntegratorOutputField0.getWidth(), 1 / (3 * maxFrequency));
        currentDigitizer0 = new Digitizer(multiplier0Output, step);
        currentDigitizer1 = new Digitizer(multiplier1Output, step);
        digitizer0Output = currentDigitizer0.getDigitalSignal();
        digitizer1Output = currentDigitizer1.getDigitalSignal();
    }

    //integrates signals from multipliers
    void doIntegrating()
    {
	//integrates multipliers output
	currentIntegrator0 = new Integrator(digitizer0Output);
	currentIntegrator1 = new Integrator(digitizer1Output);
	integrator0Output = currentIntegrator0.getIntegrals();
	integrator1Output = currentIntegrator1.getIntegrals();

	//shows zero integrator chart
	if (currentIntegratorVizualizator0 != null)
	{
	    blockIntegratorOutputField0.remove(currentIntegratorVizualizator0);
	    currentIntegratorVizualizator0 = null;
	}
	int cx0 = blockIntegratorOutputField0.getWidth();
	int cy0 = blockIntegratorOutputField0.getHeight();

	integrator0OutputProvider = new ArrayList<DataVizualizatorProvider>();
	integrator0OutputProvider.add(new DataVizualizatorProvider(integrator0Output, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("INTEGRATOR 0 OUTPUT SIGNAL"), Color.BLUE));
	currentIntegratorVizualizator0 = new DataVizualizator(integrator0OutputProvider, cx0, cy0, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SI0(T), V"), (Integer)linesWidth.getValue());
	currentIntegratorVizualizator0.setVisible(true);
	blockIntegratorOutputField0.add(currentIntegratorVizualizator0);
	currentIntegratorVizualizator0.repaint();

	//shows first integrator chart
	if (currentIntegratorVizualizator1 != null)
	{
	    blockIntegratorOutputField1.remove(currentIntegratorVizualizator1);
	    currentIntegratorVizualizator1 = null;
	}
	int cx1 = blockIntegratorOutputField1.getWidth();
	int cy1 = blockIntegratorOutputField1.getHeight();

	integrator1OutputProvider = new ArrayList<DataVizualizatorProvider>();
	integrator1OutputProvider.add(new DataVizualizatorProvider(integrator1Output, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("INTEGRATOR 1 OUTPUT SIGNAL"), Color.BLUE));
	currentIntegratorVizualizator1 = new DataVizualizator(integrator1OutputProvider, cx1, cy1, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SI1(T), V"), (Integer)linesWidth.getValue());
	currentIntegratorVizualizator1.setVisible(true);
	blockIntegratorOutputField1.add(currentIntegratorVizualizator1);
	currentIntegratorVizualizator1.repaint();
    }

    //sums signals from integrators
    void doSumming()
    {
	//gets sum from two integrators
	currentSummator = new Summator(integrator0Output, integrator1Output);
	summatorOutput = currentSummator.getSum();

	//shows chart
	if (currentSummatorVizualizator != null)
	{
	    blockSummatorOutputField.remove(currentSummatorVizualizator);
	    currentSummatorVizualizator = null;
	}
	int cx1 = blockSummatorOutputField.getWidth();
	int cy1 = blockSummatorOutputField.getHeight();

	summatorOutputProvider = new ArrayList<DataVizualizatorProvider>();
	summatorOutputProvider.add(new DataVizualizatorProvider(summatorOutput, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SUMMATOR OUTPUT SIGNAL"), Color.BLUE));
	currentSummatorVizualizator = new DataVizualizator(summatorOutputProvider, cx1, cy1, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SSUM(T), V"), (Integer)linesWidth.getValue());
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

	int errorsCount = forceErrorsTrigger ? (Integer) forceErrorsCount.getValue() : 0;

        currentResolver = new Resolver(summatorOutput, threshold, modulationType, useNoiseErrorsTrigger, forceErrorsTrigger, errorsCount, injectErrorsPerBlock, channelSymbols);
	resolverOutput = currentResolver.getBinaryNumbers();
	blockResolverOutput.setText("<html>" + currentResolver.getStringSequence());

	currentResolverVideoCreator = new VideoCreator(resolverOutput, channelImpulseLength, 1);
	resolverVideoSequence = currentResolverVideoCreator.getVideoSequence();
	if (currentResolverVideoSequenceVizualizator != null)
	{
	    blockResolverVideoSequenceOutputField.remove(currentResolverVideoSequenceVizualizator);
	    currentResolverVideoSequenceVizualizator = null;
	}
	resolverVideoSequenceProvider = new ArrayList<DataVizualizatorProvider>();
	resolverVideoSequenceProvider.add(new DataVizualizatorProvider(resolverVideoSequence, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("RESOLVER VIDEOSEQUENCE"), Color.RED));
	int cx = blockResolverVideoSequenceOutputField.getWidth();
	int cy = blockResolverVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentResolverVideoSequenceVizualizator = new DataVizualizator(resolverVideoSequenceProvider, cx, cy, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SRES(T), V"), (Integer)linesWidth.getValue());

	//shows chart
	currentResolverVideoSequenceVizualizator.setVisible(true);
	blockResolverVideoSequenceOutputField.add(currentResolverVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentResolverVideoSequenceVizualizator.repaint();
    }

    //decodes source code with selected Channel code
    void doChannelDecoding()
    {
	currentChannelDecoder = new ChannelDecoderController(resolverOutput, channelCode, headLength, lengthMap, useChannelCoderTrigger);
	channelDecoderOutput = currentChannelDecoder.getSequence();
	String text = "<html> " + java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("RECEIVED SEQUENCE:") + " <br/>" + currentResolver.getStringSequence() + "<br/>";
	text += currentChannelDecoder.getHTMLReport();
	blockChannelDecoderOutput.setText(text);
    }

    //decodes source code with selected Channel code
    void doChannelDecodingVideoSequence()
    {
        BitsRectifier channelRectifier = new BitsRectifier(channelDecoderOutput);
	currentChannelDecoderVideoCreator = new VideoCreator(channelRectifier.getBits(), sourceImpulseLength, 1);
	channelDecoderVideoSequence = currentChannelDecoderVideoCreator.getVideoSequence();
	if (currentChannelDecoderVideoSequenceVizualizator != null)
	{
	    blockChannelDecoderVideoSequenceOutputField.remove(currentChannelDecoderVideoSequenceVizualizator);
	    currentChannelDecoderVideoSequenceVizualizator = null;
	}
	channelDecoderVideoSequenceProvider = new ArrayList<DataVizualizatorProvider>();
	channelDecoderVideoSequenceProvider.add(new DataVizualizatorProvider(channelDecoderVideoSequence, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("CHANNEL DECODER VIDEOSEQUENCE"), Color.RED));
	int cx = blockChannelDecoderVideoSequenceOutputField.getWidth();
	int cy = blockChannelDecoderVideoSequenceOutputField.getHeight();

	//creates new vizualizator
	currentChannelDecoderVideoSequenceVizualizator = new DataVizualizator(channelDecoderVideoSequenceProvider, cx, cy, java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("T, S"), java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian").getString("SCHDV(T), V"), (Integer)linesWidth.getValue());

	//shows chart
	currentChannelDecoderVideoSequenceVizualizator.setVisible(true);
	blockChannelDecoderVideoSequenceOutputField.add(currentChannelDecoderVideoSequenceVizualizator);

	//repaints chart to show it if VideoSequence block is active
	currentChannelDecoderVideoSequenceVizualizator.repaint();
    }

    void doSourceDecoding()
    {
	currentSourceDecoder = new SourceDecoderController(channelDecoderOutput, sourceCode, isCyr);
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
        appearanceOptionsDialog = new javax.swing.JDialog();
        appearanceOptionsTabs = new javax.swing.JTabbedPane();
        chartsOptionsTab = new javax.swing.JPanel();
        linesWidthLabel = new javax.swing.JLabel();
        linesWidth = new javax.swing.JSpinner();
        pixelsLabel = new javax.swing.JLabel();
        TCSTabs = new javax.swing.JTabbedPane();
        blockMessageSource = new javax.swing.JPanel();
        blockSourceMessagePanel = new javax.swing.JPanel();
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
        blockModulatorOutputPanel = new javax.swing.JPanel();
        blockModulatorOutputField = new javax.swing.JPanel();
        blockNoiseGenerator = new javax.swing.JPanel();
        blockNoiseGeneratorOutputPanel = new javax.swing.JPanel();
        blockNoiseGeneratorOutputField = new javax.swing.JPanel();
        blockChannel = new javax.swing.JPanel();
        blockChannelOutputPanel = new javax.swing.JPanel();
        blockChannelOutputField = new javax.swing.JPanel();
        blockReferenceGenerator0 = new javax.swing.JPanel();
        blockReferenceGeneratorOutputPanel0 = new javax.swing.JPanel();
        blockReferenceGeneratorOutputField0 = new javax.swing.JPanel();
        blockMultiplier0 = new javax.swing.JPanel();
        blockMultiplierOutputPanel0 = new javax.swing.JPanel();
        blockMultiplierOutputField0 = new javax.swing.JPanel();
        blockReferenceGenerator1 = new javax.swing.JPanel();
        blockReferenceGeneratorOutputPanel1 = new javax.swing.JPanel();
        blockReferenceGeneratorOutputField1 = new javax.swing.JPanel();
        blockMultiplier1 = new javax.swing.JPanel();
        blockMultiplierOutputPanel1 = new javax.swing.JPanel();
        blockMultiplierOutputField1 = new javax.swing.JPanel();
        blockIntegrator0 = new javax.swing.JPanel();
        blockIntegratorOutputPanel0 = new javax.swing.JPanel();
        blockIntegratorOutputField0 = new javax.swing.JPanel();
        blockIntegrator1 = new javax.swing.JPanel();
        blockIntegratorOutputPanel1 = new javax.swing.JPanel();
        blockIntegratorOutputField1 = new javax.swing.JPanel();
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
        blockSourceDecoderPanel = new javax.swing.JPanel();
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
        upArrowLabel1 = new javax.swing.JLabel();
        channelDecoderButton = new javax.swing.JButton();
        sourceDecoderButton = new javax.swing.JButton();
        referenceGenerator0Button = new javax.swing.JButton();
        referenceGenerator1Button = new javax.swing.JButton();
        downArrowLabel = new javax.swing.JLabel();
        rightArrowLabel12 = new javax.swing.JLabel();
        upArrowLabel2 = new javax.swing.JLabel();
        noiseButton = new javax.swing.JButton();
        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitItem = new javax.swing.JMenuItem();
        modellingMenu = new javax.swing.JMenu();
        doModellingOptionsItem = new javax.swing.JMenuItem();
        doModellingItem = new javax.swing.JMenuItem();
        optionsMenu = new javax.swing.JMenu();
        appearanceOptionsItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutItem = new javax.swing.JMenuItem();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("tcncoding/LanguageUkrainian"); // NOI18N
        aboutDialog.setTitle(bundle.getString("ABOUT")); // NOI18N
        aboutDialog.setMinimumSize(new java.awt.Dimension(326, 132));
        aboutDialog.setResizable(false);
        aboutDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        programNameLabel.setText(bundle.getString("DIGITAL TELECOMMUNICATION SYSTEM MODEL")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        aboutDialog.getContentPane().add(programNameLabel, gridBagConstraints);

        instituteNameLabel.setText(bundle.getString("ITS NTUU «KPI»")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        aboutDialog.getContentPane().add(instituteNameLabel, gridBagConstraints);

        copyrightLabel.setText(bundle.getString("© 2009-2010, OLEKSANDR NATALENKO")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        aboutDialog.getContentPane().add(copyrightLabel, gridBagConstraints);

        licenseLabel.setText(bundle.getString("DISTRIBUTED UNDER TERMS AND CONDITIONS OF UPLV4.1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        aboutDialog.getContentPane().add(licenseLabel, gridBagConstraints);

        showLicense.setText(bundle.getString("SHOW LICENSE")); // NOI18N
        showLicense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showLicenseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        aboutDialog.getContentPane().add(showLicense, gridBagConstraints);

        modellingOptionsDialog.setTitle(bundle.getString("MODELLING OPTIONS")); // NOI18N
        modellingOptionsDialog.setMinimumSize(new java.awt.Dimension(595, 145));
        modellingOptionsDialog.setResizable(false);

        optionsTabs.setMinimumSize(new java.awt.Dimension(0, 0));

        sourceCoderTab.setLayout(new java.awt.GridBagLayout());

        sourceCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { bundle.getString("ITC-2"), bundle.getString("ITC-5"), bundle.getString("KOI8-U"), bundle.getString("MORSE"), bundle.getString("SHANNON-FANO") }));
        sourceCodesChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sourceCodesChooserItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sourceCoderTab.add(sourceCodesChooser, gridBagConstraints);

        sourceCodesChooserLabel.setLabelFor(sourceCodesChooser);
        sourceCodesChooserLabel.setText(bundle.getString("CODE:")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sourceCoderTab.add(sourceCodesChooserLabel, gridBagConstraints);

        informationalSpeedLabel.setText(bundle.getString("INFORMATIONAL RATE:")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sourceCoderTab.add(informationalSpeedLabel, gridBagConstraints);

        informationalSpeed.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(166666.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sourceCoderTab.add(informationalSpeed, gridBagConstraints);

        bpsLabel.setText(bundle.getString("BPS")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        sourceCoderTab.add(bpsLabel, gridBagConstraints);

        optionsTabs.addTab(bundle.getString("SOURCE CODER"), sourceCoderTab); // NOI18N

        channelCoderTab.setLayout(new java.awt.GridBagLayout());

        channelCodesChooserLabel.setLabelFor(channelCodesChooser);
        channelCodesChooserLabel.setText(bundle.getString("CODE:")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelCoderTab.add(channelCodesChooserLabel, gridBagConstraints);

        channelCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { bundle.getString("WITH PARITY BIT CHECKING"), bundle.getString("INVERSED"), bundle.getString("MANCHESTER"), bundle.getString("HAMMING (7, 4)"), bundle.getString("CYCLIC (8, 5)")}));
        channelCodesChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                channelCodesChooserItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelCoderTab.add(channelCodesChooser, gridBagConstraints);

        useChannelCoder.setSelected(true);
        useChannelCoder.setText(bundle.getString("USE REDUNDAND CODING")); // NOI18N
        useChannelCoder.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useChannelCoderItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelCoderTab.add(useChannelCoder, gridBagConstraints);

        optionsTabs.addTab(bundle.getString("CHANNEL CODER"), channelCoderTab); // NOI18N

        modulatorTab.setLayout(new java.awt.GridBagLayout());

        modulationTypeLabel.setLabelFor(modulationTypeChooser);
        modulationTypeLabel.setText(bundle.getString("MODULATION TYPE:")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(modulationTypeLabel, gridBagConstraints);

        modulationTypeChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { bundle.getString("ASK"), bundle.getString("FSK"), bundle.getString("PSK"), bundle.getString("RPSK")}));
        modulationTypeChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                modulationTypeChooserItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(modulationTypeChooser, gridBagConstraints);

        bearerAmplitudeLabel.setText(bundle.getString("BEARER AMPLITUDE:")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(bearerAmplitudeLabel, gridBagConstraints);

        bearerAmplitude.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(10.0d), Double.valueOf(0.0d), null, Double.valueOf(0.5d)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(bearerAmplitude, gridBagConstraints);

        bearerFrequencyDeviationLabel.setText(bundle.getString("FREQUENCY DEVIATION:")); // NOI18N
        bearerFrequencyDeviationLabel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(bearerFrequencyDeviationLabel, gridBagConstraints);

        bearerFrequencyLabel.setText(bundle.getString("BEARER FREQUENCY:")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(bearerFrequencyLabel, gridBagConstraints);

        bearerFrequencyDeviation.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(20000.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        bearerFrequencyDeviation.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(bearerFrequencyDeviation, gridBagConstraints);

        bearerFrequency.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(200000.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(bearerFrequency, gridBagConstraints);

        voltsLabel.setText(bundle.getString("V")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(voltsLabel, gridBagConstraints);

        hzBearerLabel.setText(bundle.getString("HZ")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(hzBearerLabel, gridBagConstraints);

        hzDeviationLabel.setText(bundle.getString("HZ")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        modulatorTab.add(hzDeviationLabel, gridBagConstraints);

        optionsTabs.addTab(bundle.getString("MODULATOR"), modulatorTab); // NOI18N

        channelTab.setLayout(new java.awt.GridBagLayout());

        noisePowerLabel.setText(bundle.getString("MAXIMUM NOISE POWER:")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelTab.add(noisePowerLabel, gridBagConstraints);

        noisePower.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.0d), null, Double.valueOf(0.1d)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 71;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelTab.add(noisePower, gridBagConstraints);

        noisePowerWattLabel.setText(bundle.getString("W")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelTab.add(noisePowerWattLabel, gridBagConstraints);

        useNoiseErrors.setSelected(true);
        useNoiseErrors.setText(bundle.getString("INJECT ERRORS CAUSED BY CHANNEL NOISE")); // NOI18N
        useNoiseErrors.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useNoiseErrorsItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelTab.add(useNoiseErrors, gridBagConstraints);

        forceErrors.setText(bundle.getString("FORCE INJECTING ERRORS OF FACTOR")); // NOI18N
        forceErrors.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                forceErrorsItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelTab.add(forceErrors, gridBagConstraints);

        forceErrorsCount.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        forceErrorsCount.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 71;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelTab.add(forceErrorsCount, gridBagConstraints);

        errorsInjectorTypeChooserGroup.add(errorsInjectorPerBlock);
        errorsInjectorPerBlock.setSelected(true);
        errorsInjectorPerBlock.setText(bundle.getString("BY BLOCK")); // NOI18N
        errorsInjectorPerBlock.setEnabled(false);
        errorsInjectorPerBlock.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                errorsInjectorPerBlockItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelTab.add(errorsInjectorPerBlock, gridBagConstraints);

        errorsInjectorTypeChooserGroup.add(errorsInjectorPerSequence);
        errorsInjectorPerSequence.setText(bundle.getString("TO WHOLE SEQUENCE")); // NOI18N
        errorsInjectorPerSequence.setEnabled(false);
        errorsInjectorPerSequence.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                errorsInjectorPerSequenceItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        channelTab.add(errorsInjectorPerSequence, gridBagConstraints);

        optionsTabs.addTab(bundle.getString("CHANNEL"), channelTab); // NOI18N

        javax.swing.GroupLayout modellingOptionsDialogLayout = new javax.swing.GroupLayout(modellingOptionsDialog.getContentPane());
        modellingOptionsDialog.getContentPane().setLayout(modellingOptionsDialogLayout);
        modellingOptionsDialogLayout.setHorizontalGroup(
            modellingOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(optionsTabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        modellingOptionsDialogLayout.setVerticalGroup(
            modellingOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(optionsTabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        licenseDialog.setTitle(bundle.getString("LICENSE")); // NOI18N
        licenseDialog.setMinimumSize(new java.awt.Dimension(700, 600));
        licenseDialog.setResizable(false);
        licenseDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        licenseTextArea.setColumns(20);
        licenseTextArea.setEditable(false);
        licenseTextArea.setLineWrap(true);
        licenseTextArea.setRows(5);
        licenseTextArea.setWrapStyleWord(true);
        jScrollPane7.setViewportView(licenseTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 678;
        gridBagConstraints.ipady = 578;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        licenseDialog.getContentPane().add(jScrollPane7, gridBagConstraints);

        appearanceOptionsDialog.setTitle(bundle.getString("APPEARANCE OPTIONS")); // NOI18N
        appearanceOptionsDialog.setMinimumSize(new java.awt.Dimension(169, 74));
        appearanceOptionsDialog.setResizable(false);

        chartsOptionsTab.setLayout(new java.awt.GridBagLayout());

        linesWidthLabel.setText(bundle.getString("LINES WIDTH:")); // NOI18N
        chartsOptionsTab.add(linesWidthLabel, new java.awt.GridBagConstraints());

        linesWidth.setModel(new javax.swing.SpinnerNumberModel(2, 1, 5, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        chartsOptionsTab.add(linesWidth, gridBagConstraints);

        pixelsLabel.setText(bundle.getString("PIX.")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        chartsOptionsTab.add(pixelsLabel, gridBagConstraints);

        appearanceOptionsTabs.addTab(bundle.getString("CHARTS"), chartsOptionsTab); // NOI18N

        javax.swing.GroupLayout appearanceOptionsDialogLayout = new javax.swing.GroupLayout(appearanceOptionsDialog.getContentPane());
        appearanceOptionsDialog.getContentPane().setLayout(appearanceOptionsDialogLayout);
        appearanceOptionsDialogLayout.setHorizontalGroup(
            appearanceOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(appearanceOptionsTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
        );
        appearanceOptionsDialogLayout.setVerticalGroup(
            appearanceOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(appearanceOptionsTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(bundle.getString("DIGITAL TELECOMMUNICATION SYSTEM")); // NOI18N

        TCSTabs.setFont(new java.awt.Font("Dialog", 1, 16));

        blockMessageSource.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMessageSourceComponentShown(evt);
            }
        });

        blockSourceMessagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MESSAGE"))); // NOI18N

        blockMessageArea.setColumns(20);
        blockMessageArea.setFont(new java.awt.Font("Dialog", 0, 24));
        blockMessageArea.setRows(5);
        blockMessageArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(blockMessageArea);

        javax.swing.GroupLayout blockSourceMessagePanelLayout = new javax.swing.GroupLayout(blockSourceMessagePanel);
        blockSourceMessagePanel.setLayout(blockSourceMessagePanelLayout);
        blockSourceMessagePanelLayout.setHorizontalGroup(
            blockSourceMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockSourceMessagePanelLayout.setVerticalGroup(
            blockSourceMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockMessageSourceLayout = new javax.swing.GroupLayout(blockMessageSource);
        blockMessageSource.setLayout(blockMessageSourceLayout);
        blockMessageSourceLayout.setHorizontalGroup(
            blockMessageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceMessagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockMessageSourceLayout.setVerticalGroup(
            blockMessageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceMessagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("MESSAGE SOURCE"), blockMessageSource); // NOI18N

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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
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

        blockSourceCoderTabs.addTab(bundle.getString("BINARY SEQUENCE"), blockSourceCoderBinarySequence); // NOI18N

        blockSourceVideoSequenceOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockSourceCoderVideoSequenceLayout = new javax.swing.GroupLayout(blockSourceCoderVideoSequence);
        blockSourceCoderVideoSequence.setLayout(blockSourceCoderVideoSequenceLayout);
        blockSourceCoderVideoSequenceLayout.setHorizontalGroup(
            blockSourceCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockSourceCoderVideoSequenceLayout.setVerticalGroup(
            blockSourceCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
        );

        blockSourceCoderTabs.addTab(bundle.getString("VIDEOSEQUENCE"), blockSourceCoderVideoSequence); // NOI18N

        javax.swing.GroupLayout blockSourceCoderLayout = new javax.swing.GroupLayout(blockSourceCoder);
        blockSourceCoder.setLayout(blockSourceCoderLayout);
        blockSourceCoderLayout.setHorizontalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceCoderTabs)
        );
        blockSourceCoderLayout.setVerticalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceCoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("SOURCE CODER"), blockSourceCoder); // NOI18N

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
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
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

        blockChannelCoderTabs.addTab(bundle.getString("BINARY SEQUENCE"), blockChannelCoderBinarySequence); // NOI18N

        blockChannelVideoSequenceOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockChannelCoderVideoSequenceLayout = new javax.swing.GroupLayout(blockChannelCoderVideoSequence);
        blockChannelCoderVideoSequence.setLayout(blockChannelCoderVideoSequenceLayout);
        blockChannelCoderVideoSequenceLayout.setHorizontalGroup(
            blockChannelCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockChannelCoderVideoSequenceLayout.setVerticalGroup(
            blockChannelCoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
        );

        blockChannelCoderTabs.addTab(bundle.getString("VIDEOSEQUENCE"), blockChannelCoderVideoSequence); // NOI18N

        javax.swing.GroupLayout blockChannelCoderLayout = new javax.swing.GroupLayout(blockChannelCoder);
        blockChannelCoder.setLayout(blockChannelCoderLayout);
        blockChannelCoderLayout.setHorizontalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelCoderTabs)
        );
        blockChannelCoderLayout.setVerticalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelCoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("CHANNEL CODER"), blockChannelCoder); // NOI18N

        blockModulator.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockModulatorComponentShown(evt);
            }
        });

        blockModulatorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MODULATOR OUTPUT"))); // NOI18N

        blockModulatorOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockModulatorOutputPanelLayout = new javax.swing.GroupLayout(blockModulatorOutputPanel);
        blockModulatorOutputPanel.setLayout(blockModulatorOutputPanelLayout);
        blockModulatorOutputPanelLayout.setHorizontalGroup(
            blockModulatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockModulatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockModulatorOutputPanelLayout.setVerticalGroup(
            blockModulatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockModulatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockModulatorLayout = new javax.swing.GroupLayout(blockModulator);
        blockModulator.setLayout(blockModulatorLayout);
        blockModulatorLayout.setHorizontalGroup(
            blockModulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockModulatorOutputPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockModulatorLayout.setVerticalGroup(
            blockModulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockModulatorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("MODULATOR"), blockModulator); // NOI18N

        blockNoiseGenerator.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockNoiseGeneratorComponentShown(evt);
            }
        });

        blockNoiseGeneratorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("NOISE GENERATOR OUTPUT"))); // NOI18N

        blockNoiseGeneratorOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockNoiseGeneratorOutputPanelLayout = new javax.swing.GroupLayout(blockNoiseGeneratorOutputPanel);
        blockNoiseGeneratorOutputPanel.setLayout(blockNoiseGeneratorOutputPanelLayout);
        blockNoiseGeneratorOutputPanelLayout.setHorizontalGroup(
            blockNoiseGeneratorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockNoiseGeneratorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockNoiseGeneratorOutputPanelLayout.setVerticalGroup(
            blockNoiseGeneratorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockNoiseGeneratorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockNoiseGeneratorLayout = new javax.swing.GroupLayout(blockNoiseGenerator);
        blockNoiseGenerator.setLayout(blockNoiseGeneratorLayout);
        blockNoiseGeneratorLayout.setHorizontalGroup(
            blockNoiseGeneratorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockNoiseGeneratorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockNoiseGeneratorLayout.setVerticalGroup(
            blockNoiseGeneratorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockNoiseGeneratorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab("Завади", blockNoiseGenerator);

        blockChannel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockChannelComponentShown(evt);
            }
        });

        blockChannelOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("CHANNEL OUTPUT"))); // NOI18N

        blockChannelOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockChannelOutputPanelLayout = new javax.swing.GroupLayout(blockChannelOutputPanel);
        blockChannelOutputPanel.setLayout(blockChannelOutputPanelLayout);
        blockChannelOutputPanelLayout.setHorizontalGroup(
            blockChannelOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockChannelOutputPanelLayout.setVerticalGroup(
            blockChannelOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelLayout = new javax.swing.GroupLayout(blockChannel);
        blockChannel.setLayout(blockChannelLayout);
        blockChannelLayout.setHorizontalGroup(
            blockChannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockChannelLayout.setVerticalGroup(
            blockChannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("CHANNEL"), blockChannel); // NOI18N

        blockReferenceGenerator0.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockReferenceGenerator0ComponentShown(evt);
            }
        });

        blockReferenceGeneratorOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("REFERENCE GENERATOR 0 OUTPUT"))); // NOI18N

        blockReferenceGeneratorOutputField0.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockReferenceGeneratorOutputPanel0Layout = new javax.swing.GroupLayout(blockReferenceGeneratorOutputPanel0);
        blockReferenceGeneratorOutputPanel0.setLayout(blockReferenceGeneratorOutputPanel0Layout);
        blockReferenceGeneratorOutputPanel0Layout.setHorizontalGroup(
            blockReferenceGeneratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockReferenceGeneratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockReferenceGeneratorOutputPanel0Layout.setVerticalGroup(
            blockReferenceGeneratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockReferenceGeneratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockReferenceGenerator0Layout = new javax.swing.GroupLayout(blockReferenceGenerator0);
        blockReferenceGenerator0.setLayout(blockReferenceGenerator0Layout);
        blockReferenceGenerator0Layout.setHorizontalGroup(
            blockReferenceGenerator0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockReferenceGeneratorOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockReferenceGenerator0Layout.setVerticalGroup(
            blockReferenceGenerator0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockReferenceGeneratorOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("REFERENCE GENERATOR 0"), blockReferenceGenerator0); // NOI18N

        blockMultiplier0.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMultiplier0ComponentShown(evt);
            }
        });

        blockMultiplierOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MULTIPLIER 0 OUTPUT"))); // NOI18N

        blockMultiplierOutputField0.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockMultiplierOutputPanel0Layout = new javax.swing.GroupLayout(blockMultiplierOutputPanel0);
        blockMultiplierOutputPanel0.setLayout(blockMultiplierOutputPanel0Layout);
        blockMultiplierOutputPanel0Layout.setHorizontalGroup(
            blockMultiplierOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockMultiplierOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockMultiplierOutputPanel0Layout.setVerticalGroup(
            blockMultiplierOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockMultiplierOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockMultiplier0Layout = new javax.swing.GroupLayout(blockMultiplier0);
        blockMultiplier0.setLayout(blockMultiplier0Layout);
        blockMultiplier0Layout.setHorizontalGroup(
            blockMultiplier0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockMultiplierOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockMultiplier0Layout.setVerticalGroup(
            blockMultiplier0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockMultiplierOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("MULTIPLIER 0"), blockMultiplier0); // NOI18N

        blockReferenceGenerator1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockReferenceGenerator1ComponentShown(evt);
            }
        });

        blockReferenceGeneratorOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("REFERENCE GENERATOR 1 OUTPUT"))); // NOI18N

        blockReferenceGeneratorOutputField1.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockReferenceGeneratorOutputPanel1Layout = new javax.swing.GroupLayout(blockReferenceGeneratorOutputPanel1);
        blockReferenceGeneratorOutputPanel1.setLayout(blockReferenceGeneratorOutputPanel1Layout);
        blockReferenceGeneratorOutputPanel1Layout.setHorizontalGroup(
            blockReferenceGeneratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockReferenceGeneratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockReferenceGeneratorOutputPanel1Layout.setVerticalGroup(
            blockReferenceGeneratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockReferenceGeneratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockReferenceGenerator1Layout = new javax.swing.GroupLayout(blockReferenceGenerator1);
        blockReferenceGenerator1.setLayout(blockReferenceGenerator1Layout);
        blockReferenceGenerator1Layout.setHorizontalGroup(
            blockReferenceGenerator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockReferenceGeneratorOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockReferenceGenerator1Layout.setVerticalGroup(
            blockReferenceGenerator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockReferenceGeneratorOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("REFERENCE GENERATOR 1"), blockReferenceGenerator1); // NOI18N

        blockMultiplier1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockMultiplier1ComponentShown(evt);
            }
        });

        blockMultiplierOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("MULTIPLIER 1 OUTPUT"))); // NOI18N

        blockMultiplierOutputField1.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockMultiplierOutputPanel1Layout = new javax.swing.GroupLayout(blockMultiplierOutputPanel1);
        blockMultiplierOutputPanel1.setLayout(blockMultiplierOutputPanel1Layout);
        blockMultiplierOutputPanel1Layout.setHorizontalGroup(
            blockMultiplierOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockMultiplierOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockMultiplierOutputPanel1Layout.setVerticalGroup(
            blockMultiplierOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockMultiplierOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockMultiplier1Layout = new javax.swing.GroupLayout(blockMultiplier1);
        blockMultiplier1.setLayout(blockMultiplier1Layout);
        blockMultiplier1Layout.setHorizontalGroup(
            blockMultiplier1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockMultiplierOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockMultiplier1Layout.setVerticalGroup(
            blockMultiplier1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockMultiplierOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("MULTIPLIER 1"), blockMultiplier1); // NOI18N

        blockIntegrator0.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockIntegrator0ComponentShown(evt);
            }
        });

        blockIntegratorOutputPanel0.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("INTEGRATOR 0 OUTPUT"))); // NOI18N

        blockIntegratorOutputField0.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockIntegratorOutputPanel0Layout = new javax.swing.GroupLayout(blockIntegratorOutputPanel0);
        blockIntegratorOutputPanel0.setLayout(blockIntegratorOutputPanel0Layout);
        blockIntegratorOutputPanel0Layout.setHorizontalGroup(
            blockIntegratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockIntegratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockIntegratorOutputPanel0Layout.setVerticalGroup(
            blockIntegratorOutputPanel0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockIntegratorOutputField0, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockIntegrator0Layout = new javax.swing.GroupLayout(blockIntegrator0);
        blockIntegrator0.setLayout(blockIntegrator0Layout);
        blockIntegrator0Layout.setHorizontalGroup(
            blockIntegrator0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockIntegratorOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockIntegrator0Layout.setVerticalGroup(
            blockIntegrator0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockIntegratorOutputPanel0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("INTEGRATOR 0"), blockIntegrator0); // NOI18N

        blockIntegrator1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockIntegrator1ComponentShown(evt);
            }
        });

        blockIntegratorOutputPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("INTEGRATOR 1 OUTPUT"))); // NOI18N

        blockIntegratorOutputField1.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockIntegratorOutputPanel1Layout = new javax.swing.GroupLayout(blockIntegratorOutputPanel1);
        blockIntegratorOutputPanel1.setLayout(blockIntegratorOutputPanel1Layout);
        blockIntegratorOutputPanel1Layout.setHorizontalGroup(
            blockIntegratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockIntegratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockIntegratorOutputPanel1Layout.setVerticalGroup(
            blockIntegratorOutputPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockIntegratorOutputField1, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockIntegrator1Layout = new javax.swing.GroupLayout(blockIntegrator1);
        blockIntegrator1.setLayout(blockIntegrator1Layout);
        blockIntegrator1Layout.setHorizontalGroup(
            blockIntegrator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockIntegratorOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockIntegrator1Layout.setVerticalGroup(
            blockIntegrator1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockIntegratorOutputPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("INTEGRATOR 1"), blockIntegrator1); // NOI18N

        blockSummator.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockSummatorComponentShown(evt);
            }
        });

        blockSummatorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("SUMMATOR OUTPUT"))); // NOI18N

        blockSummatorOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockSummatorOutputPanelLayout = new javax.swing.GroupLayout(blockSummatorOutputPanel);
        blockSummatorOutputPanel.setLayout(blockSummatorOutputPanelLayout);
        blockSummatorOutputPanelLayout.setHorizontalGroup(
            blockSummatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSummatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockSummatorOutputPanelLayout.setVerticalGroup(
            blockSummatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSummatorOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
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

        TCSTabs.addTab(bundle.getString("SUMMATOR"), blockSummator); // NOI18N

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
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
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

        blockResolverTabs.addTab(bundle.getString("BINARY SEQUENCE"), blockResolverBinarySequence); // NOI18N

        blockResolverVideoSequenceOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockResolverVideoSequenceLayout = new javax.swing.GroupLayout(blockResolverVideoSequence);
        blockResolverVideoSequence.setLayout(blockResolverVideoSequenceLayout);
        blockResolverVideoSequenceLayout.setHorizontalGroup(
            blockResolverVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockResolverVideoSequenceLayout.setVerticalGroup(
            blockResolverVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
        );

        blockResolverTabs.addTab(bundle.getString("VIDEOSEQUENCE"), blockResolverVideoSequence); // NOI18N

        javax.swing.GroupLayout blockResolverLayout = new javax.swing.GroupLayout(blockResolver);
        blockResolver.setLayout(blockResolverLayout);
        blockResolverLayout.setHorizontalGroup(
            blockResolverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverTabs)
        );
        blockResolverLayout.setVerticalGroup(
            blockResolverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockResolverTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("RESOLVER"), blockResolver); // NOI18N

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
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
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

        blockChannelDecoderTabs.addTab(bundle.getString("BINARY SEQUENCE"), blockChannelDecoderBinarySequence); // NOI18N

        blockChannelDecoderVideoSequenceOutputField.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout blockChannelDecoderVideoSequenceLayout = new javax.swing.GroupLayout(blockChannelDecoderVideoSequence);
        blockChannelDecoderVideoSequence.setLayout(blockChannelDecoderVideoSequenceLayout);
        blockChannelDecoderVideoSequenceLayout.setHorizontalGroup(
            blockChannelDecoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
        );
        blockChannelDecoderVideoSequenceLayout.setVerticalGroup(
            blockChannelDecoderVideoSequenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderVideoSequenceOutputField, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
        );

        blockChannelDecoderTabs.addTab(bundle.getString("VIDEOSEQUENCE"), blockChannelDecoderVideoSequence); // NOI18N

        javax.swing.GroupLayout blockChannelDecoderLayout = new javax.swing.GroupLayout(blockChannelDecoder);
        blockChannelDecoder.setLayout(blockChannelDecoderLayout);
        blockChannelDecoderLayout.setHorizontalGroup(
            blockChannelDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderTabs)
        );
        blockChannelDecoderLayout.setVerticalGroup(
            blockChannelDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockChannelDecoderTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("CHANNEL DECODER"), blockChannelDecoder); // NOI18N

        blockSourceDecoder.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                blockSourceDecoderComponentShown(evt);
            }
        });

        blockSourceDecoderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("RECEIVED MESSAGE"))); // NOI18N

        receivedMessageArea.setColumns(20);
        receivedMessageArea.setEditable(false);
        receivedMessageArea.setFont(new java.awt.Font("Dialog", 0, 24));
        receivedMessageArea.setRows(5);
        jScrollPane4.setViewportView(receivedMessageArea);

        javax.swing.GroupLayout blockSourceDecoderPanelLayout = new javax.swing.GroupLayout(blockSourceDecoderPanel);
        blockSourceDecoderPanel.setLayout(blockSourceDecoderPanelLayout);
        blockSourceDecoderPanelLayout.setHorizontalGroup(
            blockSourceDecoderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        blockSourceDecoderPanelLayout.setVerticalGroup(
            blockSourceDecoderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceDecoderLayout = new javax.swing.GroupLayout(blockSourceDecoder);
        blockSourceDecoder.setLayout(blockSourceDecoderLayout);
        blockSourceDecoderLayout.setHorizontalGroup(
            blockSourceDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceDecoderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockSourceDecoderLayout.setVerticalGroup(
            blockSourceDecoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blockSourceDecoderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TCSTabs.addTab(bundle.getString("SOURCE DECODER"), blockSourceDecoder); // NOI18N

        systemScheme.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("STRUCTURED SCHEME"))); // NOI18N
        systemScheme.setLayout(new java.awt.GridBagLayout());

        channelCoderButton.setBackground(new java.awt.Color(240, 240, 240));
        channelCoderButton.setText(bundle.getString("CHC")); // NOI18N
        channelCoderButton.setToolTipText(bundle.getString("CHANNEL CODER")); // NOI18N
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
        modulatorButton.setText(bundle.getString("MOD")); // NOI18N
        modulatorButton.setToolTipText(bundle.getString("MODULATOR")); // NOI18N
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
        channelButton.setText(bundle.getString("CH")); // NOI18N
        channelButton.setToolTipText(bundle.getString("CHANNEL")); // NOI18N
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
        integrator0Button.setText(bundle.getString("I0")); // NOI18N
        integrator0Button.setToolTipText(bundle.getString("INTEGRATOR 0")); // NOI18N
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
        sourceCoderButton.setText(bundle.getString("SC")); // NOI18N
        sourceCoderButton.setToolTipText(bundle.getString("SOURCE CODER")); // NOI18N
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
        multiplier0Button.setText(bundle.getString("M0")); // NOI18N
        multiplier0Button.setToolTipText(bundle.getString("MULTIPLIER 0")); // NOI18N
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
        multiplier1Button.setText(bundle.getString("M1")); // NOI18N
        multiplier1Button.setToolTipText(bundle.getString("MULTIPLIER 1")); // NOI18N
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
        messageSourceButton.setText(bundle.getString("MS")); // NOI18N
        messageSourceButton.setToolTipText(bundle.getString("MESSAGE SOURCE")); // NOI18N
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
        integrator1Button.setText(bundle.getString("I1")); // NOI18N
        integrator1Button.setToolTipText(bundle.getString("INTEGRATOR 1")); // NOI18N
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
        summatorButton.setText(bundle.getString("SUM (-)")); // NOI18N
        summatorButton.setToolTipText(bundle.getString("SUMMATOR")); // NOI18N
        summatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summatorButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 3;
        systemScheme.add(summatorButton, gridBagConstraints);

        drArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/dr.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 4;
        systemScheme.add(drArrowLabel, gridBagConstraints);

        leftTripleLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/triplel.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        systemScheme.add(leftTripleLabel, gridBagConstraints);

        dlArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/dl.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 4;
        systemScheme.add(dlArrowLabel, gridBagConstraints);

        ulArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/ul.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 2;
        systemScheme.add(ulArrowLabel, gridBagConstraints);

        urArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/ur.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        systemScheme.add(urArrowLabel, gridBagConstraints);

        rightTripleLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/tripler.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightTripleLabel, gridBagConstraints);

        rightArrowLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel2, gridBagConstraints);

        rightArrowLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel3, gridBagConstraints);

        rightArrowLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel4, gridBagConstraints);

        rightArrowLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel6, gridBagConstraints);

        rightArrowLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel1, gridBagConstraints);

        resolverButton.setBackground(new java.awt.Color(240, 240, 240));
        resolverButton.setText(bundle.getString("RES")); // NOI18N
        resolverButton.setToolTipText(bundle.getString("RESOLVER")); // NOI18N
        resolverButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resolverButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 16;
        gridBagConstraints.gridy = 3;
        systemScheme.add(resolverButton, gridBagConstraints);

        rightArrowLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 4;
        systemScheme.add(rightArrowLabel7, gridBagConstraints);

        rightArrowLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel8, gridBagConstraints);

        rightArrowLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 3;
        systemScheme.add(rightArrowLabel9, gridBagConstraints);

        upArrowLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/up_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 5;
        systemScheme.add(upArrowLabel1, gridBagConstraints);

        channelDecoderButton.setBackground(new java.awt.Color(240, 240, 240));
        channelDecoderButton.setText(bundle.getString("CHD")); // NOI18N
        channelDecoderButton.setToolTipText(bundle.getString("CHANNEL DECODER")); // NOI18N
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
        sourceDecoderButton.setText(bundle.getString("SD")); // NOI18N
        sourceDecoderButton.setToolTipText(bundle.getString("SOURCE DECODER")); // NOI18N
        sourceDecoderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceDecoderButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 3;
        systemScheme.add(sourceDecoderButton, gridBagConstraints);

        referenceGenerator0Button.setBackground(new java.awt.Color(240, 240, 240));
        referenceGenerator0Button.setText(bundle.getString("G0")); // NOI18N
        referenceGenerator0Button.setToolTipText(bundle.getString("REFERENCE GENERATOR 0")); // NOI18N
        referenceGenerator0Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referenceGenerator0ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        systemScheme.add(referenceGenerator0Button, gridBagConstraints);

        referenceGenerator1Button.setBackground(new java.awt.Color(240, 240, 240));
        referenceGenerator1Button.setText(bundle.getString("G1")); // NOI18N
        referenceGenerator1Button.setToolTipText(bundle.getString("REFERENCE GENERATOR 1")); // NOI18N
        referenceGenerator1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referenceGenerator1ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 6;
        systemScheme.add(referenceGenerator1Button, gridBagConstraints);

        downArrowLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/down_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        systemScheme.add(downArrowLabel, gridBagConstraints);

        rightArrowLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/right_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 2;
        systemScheme.add(rightArrowLabel12, gridBagConstraints);

        upArrowLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tcncoding/up_arrow.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        systemScheme.add(upArrowLabel2, gridBagConstraints);

        noiseButton.setBackground(new java.awt.Color(240, 240, 240));
        noiseButton.setText(bundle.getString("NOISEINT")); // NOI18N
        noiseButton.setToolTipText(bundle.getString("NOISEINTTOOLTIP")); // NOI18N
        noiseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noiseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 5;
        systemScheme.add(noiseButton, gridBagConstraints);

        fileMenu.setText(bundle.getString("FILE")); // NOI18N

        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        exitItem.setText(bundle.getString("EXIT")); // NOI18N
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);

        mainMenu.add(fileMenu);

        modellingMenu.setText(bundle.getString("MODELLING")); // NOI18N

        doModellingOptionsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        doModellingOptionsItem.setText(bundle.getString("OPTIONS…")); // NOI18N
        doModellingOptionsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doModellingOptionsItemActionPerformed(evt);
            }
        });
        modellingMenu.add(doModellingOptionsItem);

        doModellingItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        doModellingItem.setText(bundle.getString("RUN")); // NOI18N
        doModellingItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doModellingItemActionPerformed(evt);
            }
        });
        modellingMenu.add(doModellingItem);

        mainMenu.add(modellingMenu);

        optionsMenu.setText(bundle.getString("OPTIONS")); // NOI18N

        appearanceOptionsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        appearanceOptionsItem.setText("Зовнішній вигляд…");
        appearanceOptionsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appearanceOptionsItemActionPerformed(evt);
            }
        });
        optionsMenu.add(appearanceOptionsItem);

        mainMenu.add(optionsMenu);

        helpMenu.setText(bundle.getString("HELP")); // NOI18N

        aboutItem.setText(bundle.getString("ABOUT…")); // NOI18N
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
            .addComponent(systemScheme, javax.swing.GroupLayout.DEFAULT_SIZE, 982, Short.MAX_VALUE)
            .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 982, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(systemScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
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
            doNoiseCreating();
	    doChannel();
	    doGenerating();
	    doMultiplying();
            doDigitizing();
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
	aboutDialog.setVisible(true);
    }//GEN-LAST:event_aboutItemActionPerformed

    //hides about dialog
    //updates modulation type on choosing it from combobox
    private void modulationTypeChooserItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_modulationTypeChooserItemStateChanged
    {//GEN-HEADEREND:event_modulationTypeChooserItemStateChanged
	updateChosenModulationType();
    }//GEN-LAST:event_modulationTypeChooserItemStateChanged

    private void messageSourceButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_messageSourceButtonActionPerformed
    {//GEN-HEADEREND:event_messageSourceButtonActionPerformed
	updateChosenBlock(Blocks.MESSAGE_SOURCE);
    }//GEN-LAST:event_messageSourceButtonActionPerformed

    private void sourceCoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sourceCoderButtonActionPerformed
    {//GEN-HEADEREND:event_sourceCoderButtonActionPerformed
	updateChosenBlock(Blocks.SOURCE_CODER);
    }//GEN-LAST:event_sourceCoderButtonActionPerformed

    private void channelCoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_channelCoderButtonActionPerformed
    {//GEN-HEADEREND:event_channelCoderButtonActionPerformed
	updateChosenBlock(Blocks.CHANNEL_CODER);
    }//GEN-LAST:event_channelCoderButtonActionPerformed

    private void modulatorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_modulatorButtonActionPerformed
    {//GEN-HEADEREND:event_modulatorButtonActionPerformed
	updateChosenBlock(Blocks.MODULATOR);
    }//GEN-LAST:event_modulatorButtonActionPerformed

    private void channelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_channelButtonActionPerformed
    {//GEN-HEADEREND:event_channelButtonActionPerformed
	updateChosenBlock(Blocks.CHANNEL);
    }//GEN-LAST:event_channelButtonActionPerformed

    private void blockMessageSourceComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockMessageSourceComponentShown
    {//GEN-HEADEREND:event_blockMessageSourceComponentShown
	updateChosenBlock(Blocks.MESSAGE_SOURCE);
    }//GEN-LAST:event_blockMessageSourceComponentShown

    private void blockModulatorComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockModulatorComponentShown
    {//GEN-HEADEREND:event_blockModulatorComponentShown
	updateChosenBlock(Blocks.MODULATOR);
    }//GEN-LAST:event_blockModulatorComponentShown

    private void blockChannelComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelComponentShown
    {//GEN-HEADEREND:event_blockChannelComponentShown
	updateChosenBlock(Blocks.CHANNEL);
    }//GEN-LAST:event_blockChannelComponentShown

    private void multiplier0ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_multiplier0ButtonActionPerformed
    {//GEN-HEADEREND:event_multiplier0ButtonActionPerformed
	updateChosenBlock(Blocks.MULTIPLIER0);
    }//GEN-LAST:event_multiplier0ButtonActionPerformed

    private void multiplier1ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_multiplier1ButtonActionPerformed
    {//GEN-HEADEREND:event_multiplier1ButtonActionPerformed
	updateChosenBlock(Blocks.MULTIPLIER1);
    }//GEN-LAST:event_multiplier1ButtonActionPerformed

    private void integrator0ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_integrator0ButtonActionPerformed
    {//GEN-HEADEREND:event_integrator0ButtonActionPerformed
	updateChosenBlock(Blocks.INTEGRATOR0);
    }//GEN-LAST:event_integrator0ButtonActionPerformed

    private void integrator1ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_integrator1ButtonActionPerformed
    {//GEN-HEADEREND:event_integrator1ButtonActionPerformed
	updateChosenBlock(Blocks.INTEGRATOR1);
    }//GEN-LAST:event_integrator1ButtonActionPerformed

    private void summatorButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_summatorButtonActionPerformed
    {//GEN-HEADEREND:event_summatorButtonActionPerformed
	updateChosenBlock(Blocks.SUMMATOR);
    }//GEN-LAST:event_summatorButtonActionPerformed

    private void blockMultiplier0ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockMultiplier0ComponentShown
    {//GEN-HEADEREND:event_blockMultiplier0ComponentShown
	updateChosenBlock(Blocks.MULTIPLIER0);
    }//GEN-LAST:event_blockMultiplier0ComponentShown

    private void blockMultiplier1ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockMultiplier1ComponentShown
    {//GEN-HEADEREND:event_blockMultiplier1ComponentShown
	updateChosenBlock(Blocks.MULTIPLIER1);
    }//GEN-LAST:event_blockMultiplier1ComponentShown

    private void blockIntegrator0ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockIntegrator0ComponentShown
    {//GEN-HEADEREND:event_blockIntegrator0ComponentShown
	updateChosenBlock(Blocks.INTEGRATOR0);
    }//GEN-LAST:event_blockIntegrator0ComponentShown

    private void blockIntegrator1ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockIntegrator1ComponentShown
    {//GEN-HEADEREND:event_blockIntegrator1ComponentShown
	updateChosenBlock(Blocks.INTEGRATOR1);
    }//GEN-LAST:event_blockIntegrator1ComponentShown

    private void blockSummatorComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockSummatorComponentShown
    {//GEN-HEADEREND:event_blockSummatorComponentShown
	updateChosenBlock(Blocks.SUMMATOR);
    }//GEN-LAST:event_blockSummatorComponentShown

    private void doModellingOptionsItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_doModellingOptionsItemActionPerformed
    {//GEN-HEADEREND:event_doModellingOptionsItemActionPerformed
	modellingOptionsDialog.setVisible(true);
    }//GEN-LAST:event_doModellingOptionsItemActionPerformed

    private void blockSourceDecoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockSourceDecoderComponentShown
    {//GEN-HEADEREND:event_blockSourceDecoderComponentShown
	updateChosenBlock(Blocks.SOURCE_DECODER);
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
	updateChosenBlock(Blocks.SOURCE_CODER);
    }//GEN-LAST:event_blockSourceCoderComponentShown

    private void blockChannelCoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelCoderComponentShown
    {//GEN-HEADEREND:event_blockChannelCoderComponentShown
	updateChosenBlock(Blocks.CHANNEL_CODER);
    }//GEN-LAST:event_blockChannelCoderComponentShown

    private void blockResolverComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockResolverComponentShown
    {//GEN-HEADEREND:event_blockResolverComponentShown
	updateChosenBlock(Blocks.RESOLVER);
    }//GEN-LAST:event_blockResolverComponentShown

    private void blockChannelDecoderComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockChannelDecoderComponentShown
    {//GEN-HEADEREND:event_blockChannelDecoderComponentShown
	updateChosenBlock(Blocks.CHANNEL_DECODER);
    }//GEN-LAST:event_blockChannelDecoderComponentShown

    private void resolverButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resolverButtonActionPerformed
    {//GEN-HEADEREND:event_resolverButtonActionPerformed
	updateChosenBlock(Blocks.RESOLVER);
    }//GEN-LAST:event_resolverButtonActionPerformed

    private void channelDecoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_channelDecoderButtonActionPerformed
    {//GEN-HEADEREND:event_channelDecoderButtonActionPerformed
	updateChosenBlock(Blocks.CHANNEL_DECODER);
    }//GEN-LAST:event_channelDecoderButtonActionPerformed

    private void sourceDecoderButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sourceDecoderButtonActionPerformed
    {//GEN-HEADEREND:event_sourceDecoderButtonActionPerformed
	updateChosenBlock(Blocks.SOURCE_DECODER);
    }//GEN-LAST:event_sourceDecoderButtonActionPerformed

    private void blockReferenceGenerator0ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockReferenceGenerator0ComponentShown
    {//GEN-HEADEREND:event_blockReferenceGenerator0ComponentShown
	updateChosenBlock(Blocks.REFERENCE_GENERATOR0);
    }//GEN-LAST:event_blockReferenceGenerator0ComponentShown

    private void blockReferenceGenerator1ComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_blockReferenceGenerator1ComponentShown
    {//GEN-HEADEREND:event_blockReferenceGenerator1ComponentShown
	updateChosenBlock(Blocks.REFERENCE_GENERATOR1);
    }//GEN-LAST:event_blockReferenceGenerator1ComponentShown

    private void referenceGenerator0ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_referenceGenerator0ButtonActionPerformed
    {//GEN-HEADEREND:event_referenceGenerator0ButtonActionPerformed
	updateChosenBlock(Blocks.REFERENCE_GENERATOR0);
    }//GEN-LAST:event_referenceGenerator0ButtonActionPerformed

    private void referenceGenerator1ButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_referenceGenerator1ButtonActionPerformed
    {//GEN-HEADEREND:event_referenceGenerator1ButtonActionPerformed
	updateChosenBlock(Blocks.REFERENCE_GENERATOR1);
    }//GEN-LAST:event_referenceGenerator1ButtonActionPerformed

    private void showLicenseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showLicenseActionPerformed
    {//GEN-HEADEREND:event_showLicenseActionPerformed
	licenseTextArea.setText("");
	try
	{
	    FileReader fr = new FileReader("COPYING");
	    BufferedReader bfr = new BufferedReader(fr);
	    String line;
	    while((line = bfr.readLine()) != null)
		licenseTextArea.setText(licenseTextArea.getText() + line + "\n");
	}
	catch (Exception ex)
	{
	    System.err.println(ex.getLocalizedMessage());
	}
	licenseTextArea.setSelectionStart(0);
	licenseTextArea.setSelectionEnd(0);
	licenseDialog.setVisible(true);
    }//GEN-LAST:event_showLicenseActionPerformed

    private void noiseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noiseButtonActionPerformed
	updateChosenBlock(Blocks.NOISE_GENERATOR);
    }//GEN-LAST:event_noiseButtonActionPerformed

    private void blockNoiseGeneratorComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_blockNoiseGeneratorComponentShown
	updateChosenBlock(Blocks.NOISE_GENERATOR);
    }//GEN-LAST:event_blockNoiseGeneratorComponentShown

    private void appearanceOptionsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appearanceOptionsItemActionPerformed
        appearanceOptionsDialog.setVisible(true);
    }//GEN-LAST:event_appearanceOptionsItemActionPerformed

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
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JDialog appearanceOptionsDialog;
    private javax.swing.JMenuItem appearanceOptionsItem;
    private javax.swing.JTabbedPane appearanceOptionsTabs;
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
    private javax.swing.JPanel blockChannelOutputField;
    private javax.swing.JPanel blockChannelOutputPanel;
    private javax.swing.JPanel blockChannelVideoSequenceOutputField;
    private javax.swing.JPanel blockIntegrator0;
    private javax.swing.JPanel blockIntegrator1;
    private javax.swing.JPanel blockIntegratorOutputField0;
    private javax.swing.JPanel blockIntegratorOutputField1;
    private javax.swing.JPanel blockIntegratorOutputPanel0;
    private javax.swing.JPanel blockIntegratorOutputPanel1;
    private javax.swing.JTextArea blockMessageArea;
    private javax.swing.JPanel blockMessageSource;
    private javax.swing.JPanel blockModulator;
    private javax.swing.JPanel blockModulatorOutputField;
    private javax.swing.JPanel blockModulatorOutputPanel;
    private javax.swing.JPanel blockMultiplier0;
    private javax.swing.JPanel blockMultiplier1;
    private javax.swing.JPanel blockMultiplierOutputField0;
    private javax.swing.JPanel blockMultiplierOutputField1;
    private javax.swing.JPanel blockMultiplierOutputPanel0;
    private javax.swing.JPanel blockMultiplierOutputPanel1;
    private javax.swing.JPanel blockNoiseGenerator;
    private javax.swing.JPanel blockNoiseGeneratorOutputField;
    private javax.swing.JPanel blockNoiseGeneratorOutputPanel;
    private javax.swing.JPanel blockReferenceGenerator0;
    private javax.swing.JPanel blockReferenceGenerator1;
    private javax.swing.JPanel blockReferenceGeneratorOutputField0;
    private javax.swing.JPanel blockReferenceGeneratorOutputField1;
    private javax.swing.JPanel blockReferenceGeneratorOutputPanel0;
    private javax.swing.JPanel blockReferenceGeneratorOutputPanel1;
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
    private javax.swing.JPanel blockSourceDecoderPanel;
    private javax.swing.JPanel blockSourceMessagePanel;
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
    private javax.swing.JPanel channelTab;
    private javax.swing.JPanel chartsOptionsTab;
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.JLabel dlArrowLabel;
    private javax.swing.JMenuItem doModellingItem;
    private javax.swing.JMenuItem doModellingOptionsItem;
    private javax.swing.JLabel downArrowLabel;
    private javax.swing.JLabel drArrowLabel;
    private javax.swing.JRadioButton errorsInjectorPerBlock;
    private javax.swing.JRadioButton errorsInjectorPerSequence;
    private javax.swing.ButtonGroup errorsInjectorTypeChooserGroup;
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
    private javax.swing.JSpinner linesWidth;
    private javax.swing.JLabel linesWidthLabel;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JButton messageSourceButton;
    private javax.swing.JMenu modellingMenu;
    private javax.swing.JDialog modellingOptionsDialog;
    private javax.swing.JComboBox modulationTypeChooser;
    private javax.swing.JLabel modulationTypeLabel;
    private javax.swing.JButton modulatorButton;
    private javax.swing.JPanel modulatorTab;
    private javax.swing.JButton multiplier0Button;
    private javax.swing.JButton multiplier1Button;
    private javax.swing.JButton noiseButton;
    private javax.swing.JSpinner noisePower;
    private javax.swing.JLabel noisePowerLabel;
    private javax.swing.JLabel noisePowerWattLabel;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JTabbedPane optionsTabs;
    private javax.swing.JLabel pixelsLabel;
    private javax.swing.JLabel programNameLabel;
    private javax.swing.JTextArea receivedMessageArea;
    private javax.swing.JButton referenceGenerator0Button;
    private javax.swing.JButton referenceGenerator1Button;
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
    private javax.swing.JButton summatorButton;
    private javax.swing.JPanel systemScheme;
    private javax.swing.JLabel ulArrowLabel;
    private javax.swing.JLabel upArrowLabel1;
    private javax.swing.JLabel upArrowLabel2;
    private javax.swing.JLabel urArrowLabel;
    private javax.swing.JCheckBox useChannelCoder;
    private javax.swing.JCheckBox useNoiseErrors;
    private javax.swing.JLabel voltsLabel;
    // End of variables declaration//GEN-END:variables

}
