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

import java.util.Vector;

public class blockMain extends javax.swing.JFrame {
    //blocks (classes)
    coderOfSource currentSourceCoder = null;
    coderOfChannel currentChannelCoder = null;
    modulator currentModulator = null;

    //source message
    String message = "";

    //using codes and other parameters
    coderOfSource.sourceCoderCode sourceCode = coderOfSource.sourceCoderCode.mtk2;
    coderOfChannel.channelCoderCode channelCode = coderOfChannel.channelCoderCode.parity_bit;
    modulator.ModulationType modulationType = modulator.ModulationType.AMn;

    //message in binary symbols
    Vector source_symbols = new Vector();
    Vector channel_symbols = new Vector();

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
	switch (modulationTypeChooser.getSelectedIndex())
	{
	    case 0:
		modulationType = modulator.ModulationType.AMn;
		break;
	    case 1:
		modulationType = modulator.ModulationType.FMn;
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
	modulatorOutput.getGraphics().clearRect(10, 10, modulatorOutput.getWidth() - 10, modulatorOutput.getHeight() - 10);
	currentModulator = new modulator(modulationType, modulatorOutput, channel_symbols, currentChannelCoder.alignment);
	currentModulator.makeImage();
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
        blockSourceCoderOutput = new javax.swing.JTextArea();
        blockChannelCoder = new javax.swing.JPanel();
        channelCodesChooserLabel = new javax.swing.JLabel();
        channelCodesChooser = new javax.swing.JComboBox();
        blockChannelCoderOutputPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        blockChannelCoderOutput = new javax.swing.JTextArea();
        blockModulator = new javax.swing.JPanel();
        modulationTypeLabel = new javax.swing.JLabel();
        modulationTypeChooser = new javax.swing.JComboBox();
        modulatorOutputPanel = new javax.swing.JPanel();
        modulatorOutput = new java.awt.Canvas();
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

        messageArea.setColumns(20);
        messageArea.setRows(5);
        jScrollPane1.setViewportView(messageArea);

        messageLabel.setLabelFor(messageLabel);
        messageLabel.setText("Повідомлення:");

        javax.swing.GroupLayout blockMessageSourceLayout = new javax.swing.GroupLayout(blockMessageSource);
        blockMessageSource.setLayout(blockMessageSourceLayout);
        blockMessageSourceLayout.setHorizontalGroup(
            blockMessageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockMessageSourceLayout.createSequentialGroup()
                .addComponent(messageLabel)
                .addContainerGap(511, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
        );
        blockMessageSourceLayout.setVerticalGroup(
            blockMessageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockMessageSourceLayout.createSequentialGroup()
                .addComponent(messageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
        );

        TCSTabs.addTab("Джерело повідомлень", blockMessageSource);

        sourceCodesChooserLabel.setLabelFor(sourceCodesChooser);
        sourceCodesChooserLabel.setText("Код:");

        sourceCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "МТК-2", "МТК-5", "Морзе" }));
        sourceCodesChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sourceCodesChooserItemStateChanged(evt);
            }
        });

        blockSourceCoderOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід кодера джерела повідомлень"));

        blockSourceCoderOutput.setColumns(20);
        blockSourceCoderOutput.setEditable(false);
        blockSourceCoderOutput.setLineWrap(true);
        blockSourceCoderOutput.setRows(5);
        blockSourceCoderOutput.setWrapStyleWord(true);
        jScrollPane2.setViewportView(blockSourceCoderOutput);

        javax.swing.GroupLayout blockSourceCoderOutputPanelLayout = new javax.swing.GroupLayout(blockSourceCoderOutputPanel);
        blockSourceCoderOutputPanel.setLayout(blockSourceCoderOutputPanelLayout);
        blockSourceCoderOutputPanelLayout.setHorizontalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
        );
        blockSourceCoderOutputPanelLayout.setVerticalGroup(
            blockSourceCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockSourceCoderLayout = new javax.swing.GroupLayout(blockSourceCoder);
        blockSourceCoder.setLayout(blockSourceCoderLayout);
        blockSourceCoderLayout.setHorizontalGroup(
            blockSourceCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockSourceCoderLayout.createSequentialGroup()
                .addComponent(sourceCodesChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceCodesChooser, 0, 566, Short.MAX_VALUE))
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

        channelCodesChooserLabel.setLabelFor(channelCodesChooser);
        channelCodesChooserLabel.setText("Код:");

        channelCodesChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "із перевіркою на парність", "інверсний", "Хемінга" }));
        channelCodesChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                channelCodesChooserItemStateChanged(evt);
            }
        });

        blockChannelCoderOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід кодера каналу"));

        blockChannelCoderOutput.setColumns(20);
        blockChannelCoderOutput.setEditable(false);
        blockChannelCoderOutput.setLineWrap(true);
        blockChannelCoderOutput.setRows(5);
        blockChannelCoderOutput.setWrapStyleWord(true);
        jScrollPane3.setViewportView(blockChannelCoderOutput);

        javax.swing.GroupLayout blockChannelCoderOutputPanelLayout = new javax.swing.GroupLayout(blockChannelCoderOutputPanel);
        blockChannelCoderOutputPanel.setLayout(blockChannelCoderOutputPanelLayout);
        blockChannelCoderOutputPanelLayout.setHorizontalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
        );
        blockChannelCoderOutputPanelLayout.setVerticalGroup(
            blockChannelCoderOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockChannelCoderLayout = new javax.swing.GroupLayout(blockChannelCoder);
        blockChannelCoder.setLayout(blockChannelCoderLayout);
        blockChannelCoderLayout.setHorizontalGroup(
            blockChannelCoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockChannelCoderLayout.createSequentialGroup()
                .addComponent(channelCodesChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelCodesChooser, 0, 566, Short.MAX_VALUE))
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

        modulatorOutputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Вихід модулятора"));

        javax.swing.GroupLayout modulatorOutputPanelLayout = new javax.swing.GroupLayout(modulatorOutputPanel);
        modulatorOutputPanel.setLayout(modulatorOutputPanelLayout);
        modulatorOutputPanelLayout.setHorizontalGroup(
            modulatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulatorOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
        );
        modulatorOutputPanelLayout.setVerticalGroup(
            modulatorOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modulatorOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout blockModulatorLayout = new javax.swing.GroupLayout(blockModulator);
        blockModulator.setLayout(blockModulatorLayout);
        blockModulatorLayout.setHorizontalGroup(
            blockModulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockModulatorLayout.createSequentialGroup()
                .addComponent(modulationTypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modulationTypeChooser, 0, 499, Short.MAX_VALUE))
            .addComponent(modulatorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        blockModulatorLayout.setVerticalGroup(
            blockModulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(blockModulatorLayout.createSequentialGroup()
                .addGroup(blockModulatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modulationTypeLabel)
                    .addComponent(modulationTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(modulatorOutputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TCSTabs.addTab("Модулятор", blockModulator);

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
            .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TCSTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
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

    //check binary number shifting
    private void shl2ItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shl2ItemActionPerformed
    {//GEN-HEADEREND:event_shl2ItemActionPerformed
	binaryNumber num1 = new binaryNumber(10);
	System.out.println("shifted " +
		num1.getString(0) +
		" == " +
		num1.shl2().getString(0));
    }//GEN-LAST:event_shl2ItemActionPerformed

    //check getting weight of binary number
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

    private void aboutDialogCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutDialogCloseActionPerformed
    {//GEN-HEADEREND:event_aboutDialogCloseActionPerformed
	aboutDialog.setVisible(false);
    }//GEN-LAST:event_aboutDialogCloseActionPerformed

    private void modulationTypeChooserItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_modulationTypeChooserItemStateChanged
    {//GEN-HEADEREND:event_modulationTypeChooserItemStateChanged
	updateChosenModulationType();
    }//GEN-LAST:event_modulationTypeChooserItemStateChanged

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
    private javax.swing.JPanel blockChannelCoder;
    private javax.swing.JTextArea blockChannelCoderOutput;
    private javax.swing.JPanel blockChannelCoderOutputPanel;
    private javax.swing.JPanel blockMessageSource;
    private javax.swing.JPanel blockModulator;
    private javax.swing.JPanel blockSourceCoder;
    private javax.swing.JTextArea blockSourceCoderOutput;
    private javax.swing.JPanel blockSourceCoderOutputPanel;
    private javax.swing.JComboBox channelCodesChooser;
    private javax.swing.JLabel channelCodesChooserLabel;
    private javax.swing.JMenu developerMenu;
    private javax.swing.JMenuItem doModellingItem;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem inversionItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JTextArea messageArea;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JMenu modellingMenu;
    private javax.swing.JComboBox modulationTypeChooser;
    private javax.swing.JLabel modulationTypeLabel;
    private java.awt.Canvas modulatorOutput;
    private javax.swing.JPanel modulatorOutputPanel;
    private javax.swing.JMenuItem shl2Item;
    private javax.swing.JComboBox sourceCodesChooser;
    private javax.swing.JLabel sourceCodesChooserLabel;
    private javax.swing.JMenuItem sum2Item;
    private javax.swing.JMenuItem weightItem;
    // End of variables declaration//GEN-END:variables

}
