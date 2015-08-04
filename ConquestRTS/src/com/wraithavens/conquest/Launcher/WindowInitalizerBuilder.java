package com.wraithavens.conquest.Launcher;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import com.wraithavens.conquest.Utility.BinaryFile;

class WindowInitalizerBuilder extends JFrame{
	private JCheckBox fullScreen;
	private JCheckBox vSync;
	private JComboBox comboBox;
	private boolean done;
	private JButton btnAccept;
	private JButton btnExit;
	public WindowInitalizerBuilder(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				init();
				addComponents();
				setVisible(true);
			}
		});
	}
	private void addComponents(){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{
			50, 50, 50, 60, 60, 0
		};
		gridBagLayout.rowHeights = new int[]{
			200, 20, 20, 0, 20, 0
		};
		gridBagLayout.columnWeights = new double[]{
			0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE
		};
		gridBagLayout.rowWeights = new double[]{
			0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE
		};
		getContentPane().setLayout(gridBagLayout);
		BufferedImage splashImage = null;
		try{
			splashImage = ImageIO.read(new File(WraithavensConquest.assetFolder, "Splash.png"));
		}catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
		ImagePanel imagePanel = new ImagePanel(splashImage);
		GridBagConstraints gbc_imagePanel = new GridBagConstraints();
		gbc_imagePanel.gridwidth = 5;
		gbc_imagePanel.insets = new Insets(5, 5, 5, 5);
		gbc_imagePanel.fill = GridBagConstraints.BOTH;
		gbc_imagePanel.gridx = 0;
		gbc_imagePanel.gridy = 0;
		getContentPane().add(imagePanel, gbc_imagePanel);
		JLabel lblResolution = new JLabel("Resolution:");
		GridBagConstraints gbc_lblResolution = new GridBagConstraints();
		gbc_lblResolution.anchor = GridBagConstraints.EAST;
		gbc_lblResolution.insets = new Insets(5, 5, 5, 5);
		gbc_lblResolution.gridx = 0;
		gbc_lblResolution.gridy = 1;
		getContentPane().add(lblResolution, gbc_lblResolution);
		comboBox = new JComboBox();
		comboBox.setFont(new Font("Courier New", Font.PLAIN, 14));
		comboBox.setModel(new DefaultComboBoxModel(ResolutionSize.generateSizes()));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 4;
		gbc_comboBox.insets = new Insets(5, 5, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		getContentPane().add(comboBox, gbc_comboBox);
		fullScreen = new JCheckBox("Full Screen");
		fullScreen.setSelected(true);
		GridBagConstraints gbc_chckbxFullScreen = new GridBagConstraints();
		gbc_chckbxFullScreen.insets = new Insets(5, 5, 5, 5);
		gbc_chckbxFullScreen.gridx = 0;
		gbc_chckbxFullScreen.gridy = 2;
		getContentPane().add(fullScreen, gbc_chckbxFullScreen);
		vSync = new JCheckBox("VSync");
		GridBagConstraints gbc_chckbxVsync = new GridBagConstraints();
		gbc_chckbxVsync.insets = new Insets(5, 5, 5, 5);
		gbc_chckbxVsync.gridx = 1;
		gbc_chckbxVsync.gridy = 2;
		getContentPane().add(vSync, gbc_chckbxVsync);
		btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				done = true;
				dispose();
			}
		});
		GridBagConstraints gbc_btnAccept = new GridBagConstraints();
		gbc_btnAccept.insets = new Insets(5, 5, 5, 5);
		gbc_btnAccept.gridx = 3;
		gbc_btnAccept.gridy = 4;
		getContentPane().add(btnAccept, gbc_btnAccept);
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.insets = new Insets(5, 5, 5, 5);
		gbc_btnExit.gridx = 4;
		gbc_btnExit.gridy = 4;
		getContentPane().add(btnExit, gbc_btnExit);
		loadSettings();
	}
	private void init(){
		setTitle("Game Visual Properties");
		setSize(365, 340);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private void loadSettings(){
		File file = new File(WraithavensConquest.saveFolder, "Settings.dat");
		if(file.exists()&&file.length()>0){
			BinaryFile bin = new BinaryFile(file);
			int id = bin.getByte();
			for(int i = 0; i<comboBox.getItemCount(); i++)
				if(((ResolutionSize)comboBox.getItemAt(i)).id==id){
					comboBox.setSelectedIndex(i);
					break;
				}
			fullScreen.setSelected(bin.getBoolean());
			vSync.setSelected(bin.getBoolean());
		}
	}
	private void saveSettings(){
		BinaryFile bin = new BinaryFile(3);
		bin.addByte((byte)((ResolutionSize)comboBox.getSelectedItem()).id);
		bin.addBoolean(fullScreen.isSelected());
		bin.addBoolean(vSync.isSelected());
		bin.compile(new File(WraithavensConquest.saveFolder, "Settings.dat"));
	}
	WindowInitalizer build(){
		for(int i = 0; i<10000; i++){
			if(done){
				saveSettings();
				WindowInitalizer init = new WindowInitalizer();
				ResolutionSize resolution = (ResolutionSize)comboBox.getSelectedItem();
				init.width = resolution.width;
				init.height = resolution.height;
				init.fullscreen = fullScreen.isSelected();
				init.vSync = vSync.isSelected();
				return init;
			}
			if(isVisible())
				i = 0; // Break loop if frame is not visible for 10 seconds.
			try{
				Thread.sleep(1);
			}catch(Exception exception){}
		}
		System.exit(0);
		return null;
	}
}
