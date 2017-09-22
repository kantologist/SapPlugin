package com.sap.workshop.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sap.workshop.plugin.ScaleController;

/**
 * This window simulates a scale input
 * @author D060523
 *
 */
public class ScaleWindow extends JFrame {

	private static final long serialVersionUID = -940493037660636454L;
	
	private JButton scaleButton;
	private ScaleController scaleController;
	private JSlider scaleSlider;
	private JLabel scaleLabel;
	private BigDecimal scaleValue = BigDecimal.ZERO;
	private DecimalFormat format = new DecimalFormat("0.00");
	
	public ScaleWindow() {
		init();
	}
	
	/**
	 * Setup the window and create/add the components
	 */
	private void init(){		
		this.setLayout(new BorderLayout());
		
		scaleButton = new JButton("OK");
		scaleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scaleController.scaleInputDone(scaleValue);				
			}
		});
		
		scaleSlider = new JSlider(0, 1000);
		scaleSlider.setMajorTickSpacing(1);
		scaleSlider.setValue(0);
		
		scaleSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				scaleValue = BigDecimal.valueOf(scaleSlider.getValue()/1000D);
				updateScaleLabel(scaleValue.doubleValue());
			}
		});
		
		scaleLabel = new JLabel();
		scaleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		this.add(scaleLabel, BorderLayout.NORTH);
		this.add(scaleSlider, BorderLayout.CENTER);
		this.add(scaleButton, BorderLayout.SOUTH);
		
		this.setAlwaysOnTop(true);
		this.setSize(new Dimension(400, 200));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				scaleController.cancelScaleInput();
			}
		});
		
		updateScaleLabel(0D);
	}
	
	private void updateScaleLabel(double value){
		scaleLabel.setText("<html><body><center><font size=\"40\">" + format.format(value) + " KG</font></center></body></html>");
	}
	
	public void setScaleController(ScaleController scaleController) {
		this.scaleController = scaleController;
	}
}
