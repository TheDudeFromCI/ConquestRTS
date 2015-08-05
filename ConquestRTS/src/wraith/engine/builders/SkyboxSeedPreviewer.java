package wraith.engine.builders;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxClouds;
import com.wraithavens.conquest.SinglePlayer.Skybox.SkyboxBuilder;

public class SkyboxSeedPreviewer{
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception exception){
			exception.printStackTrace();
		}
		JFrame frame = new JFrame();
		JPanel panel = new JPanel(){
			@Override
			public void paintComponent(Graphics g){
				g.drawImage(image, 0, 0, null);
				g.dispose();
			}
		};
		panel.setPreferredSize(new Dimension(SkyboxClouds.TextureSize, SkyboxClouds.TextureSize));
		frame.getContentPane().add(panel);
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		JList list = new JList();
		list.setFixedCellWidth(100);
		list.setVisibleRowCount(5);
		list.setMaximumSize(new Dimension(0, 100));
		list.setModel(new AbstractListModel(){
			String[] values = new String[]{
				"Layer 0"
			};
			public Object getElementAt(int index){
				return values[index];
			}
			public int getSize(){
				return values.length;
			}
		});
		list.setSelectedIndex(0);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane pane = new JScrollPane(list);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel_1.add(pane);
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		JButton btnRedraw = new JButton("Redraw");
		btnRedraw.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				redraw();
				panel.repaint();
			}
		});
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		JLabel lblSeed = new JLabel("Seed");
		lblSeed.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblSeed, BorderLayout.NORTH);
		JSpinner spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(90, 20));
		spinner.setModel(new SpinnerNumberModel(new Short((short)0), null, null, new Short((short)1)));
		panel_3.add(spinner, BorderLayout.CENTER);
		Button button = new Button("Randomize");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				spinner.setValue((int)(Math.random()*Short.MAX_VALUE));
			}
		});
		panel_3.add(button, BorderLayout.EAST);
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		JLabel lblSmoothness = new JLabel("Smoothness");
		lblSmoothness.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(lblSmoothness, BorderLayout.NORTH);
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(new Float(1), new Float(1), null, new Float(0)));
		panel_4.add(spinner_1, BorderLayout.CENTER);
		JPanel panel_5 = new JPanel();
		panel_2.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		JLabel lblDetail = new JLabel("Detail");
		lblDetail.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(lblDetail, BorderLayout.NORTH);
		JSpinner spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerNumberModel(1, 1, 20, 1));
		panel_5.add(spinner_2, BorderLayout.CENTER);
		JPanel panel_6 = new JPanel();
		panel_2.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));
		JLabel lblInterpolationFunction = new JLabel("Interpolation Function");
		lblInterpolationFunction.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(lblInterpolationFunction, BorderLayout.NORTH);
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[]{
			"Linear", "Cosine"
		}));
		panel_6.add(comboBox, BorderLayout.CENTER);
		JPanel panel_7 = new JPanel();
		panel_2.add(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));
		JLabel lblLowInfluenceColor = new JLabel("Influence Color");
		lblLowInfluenceColor.setHorizontalAlignment(SwingConstants.CENTER);
		panel_7.add(lblLowInfluenceColor, BorderLayout.NORTH);
		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		panel_10.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				Color c = JColorChooser.showDialog(null, "Influence Color", panel_10.getBackground());
				panel_10.setBackground(c);
			}
		});
		panel_10.setBackground(Color.WHITE);
		panel_7.add(panel_10, BorderLayout.CENTER);
		JPanel panel_9 = new JPanel();
		panel_2.add(panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));
		JLabel lblColorInterpolationFunction = new JLabel("Color Interpolation Function");
		lblColorInterpolationFunction.setHorizontalAlignment(SwingConstants.CENTER);
		panel_9.add(lblColorInterpolationFunction, BorderLayout.NORTH);
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[]{
			"Linear", "Cosine"
		}));
		panel_9.add(comboBox_1, BorderLayout.CENTER);
		panel_2.add(btnRedraw);
		JButton btnExport = new JButton("Export");
		panel_2.add(btnExport);
		frame.setMinimumSize(new Dimension(320, 240));
		frame.setSize(500, 500);
		frame.setTitle("Skybox Seed Previewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	private static void redraw(){
		// TODO
	}
	private static SkyboxBuilder builder = new SkyboxBuilder();
	private static BufferedImage image = new BufferedImage(SkyboxClouds.TextureSize, SkyboxClouds.TextureSize,
		BufferedImage.TYPE_INT_ARGB);
}
