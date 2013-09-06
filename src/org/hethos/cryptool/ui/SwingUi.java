package org.hethos.cryptool.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.security.NoSuchAlgorithmException;

public class SwingUi extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingUi frame = new SwingUi();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SwingUi() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JTextArea taLog = new JTextArea();
		taLog.setText("Log");
		taLog.setEditable(false);
		taLog.setBounds(10, 155, 424, 106);
		contentPane.add(taLog);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 424, 133);
		contentPane.add(tabbedPane);
		
		JPanel pnlGenerate = new JPanel();
		tabbedPane.addTab("Generate Key", null, pnlGenerate, null);
		pnlGenerate.setLayout(null);
		
		JButton btnNewButton = new JButton("Generate");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 // create a key generator based upon the Blowfish cipher
			    KeyGenerator keygenerator;
				try {
					keygenerator = KeyGenerator.getInstance("Blowfish");
					
					// create a key
				    SecretKey secretkey = keygenerator.generateKey();
					
				    System.out.println("Generated Key:"+new String(Base64.encode(secretkey.getEncoded())));
				    taLog.append(Base64.encode(secretkey.getEncoded()));
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Error - no such algorithm");
				    taLog.append("Error - no such algorithm");
				}

			    
			}
		});
		btnNewButton.setBounds(10, 11, 89, 23);
		pnlGenerate.add(btnNewButton);
		
		JPanel pnlEncrypt = new JPanel();
		tabbedPane.addTab("Encrypt", null, pnlEncrypt, null);
		pnlEncrypt.setLayout(null);
		
		JLabel lblClearText = new JLabel("Clear Text:");
		lblClearText.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClearText.setBounds(10, 11, 60, 14);
		pnlEncrypt.add(lblClearText);
		
		textField = new JTextField();
		textField.setBounds(78, 8, 275, 20);
		pnlEncrypt.add(textField);
		textField.setColumns(10);
		
		JLabel lblCipherText = new JLabel("Cipher Text:");
		lblCipherText.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCipherText.setBounds(10, 35, 60, 14);
		pnlEncrypt.add(lblCipherText);
		
		textField_1 = new JTextField();
		textField_1.setBounds(78, 32, 275, 20);
		pnlEncrypt.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(78, 60, 275, 20);
		pnlEncrypt.add(textField_2);
		
		JLabel lblKey = new JLabel("Key:");
		lblKey.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKey.setBounds(10, 63, 60, 14);
		pnlEncrypt.add(lblKey);
		
		JPanel pnlDecrypt = new JPanel();
		tabbedPane.addTab("Decrypt", null, pnlDecrypt, null);
		pnlDecrypt.setLayout(null);
		
		JLabel label = new JLabel("Clear Text:");
		label.setBounds(16, 36, 54, 14);
		pnlDecrypt.add(label);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textField_3 = new JTextField();
		textField_3.setBounds(84, 36, 281, 20);
		pnlDecrypt.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel label_1 = new JLabel("Cipher Text:");
		label_1.setBounds(10, 11, 60, 14);
		pnlDecrypt.add(label_1);
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textField_4 = new JTextField();
		textField_4.setBounds(84, 11, 281, 20);
		pnlDecrypt.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(84, 61, 281, 20);
		pnlDecrypt.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel label_2 = new JLabel("Key:");
		label_2.setBounds(48, 61, 22, 14);
		pnlDecrypt.add(label_2);
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JPanel pnlBruteForce = new JPanel();
		tabbedPane.addTab("Crack", null, pnlBruteForce, null);
		pnlBruteForce.setLayout(null);
		
		textField_6 = new JTextField();
		textField_6.setBounds(80, 5, 266, 20);
		textField_6.setColumns(10);
		pnlBruteForce.add(textField_6);
		
		JLabel label_3 = new JLabel("Cipher Text:");
		label_3.setBounds(10, 8, 60, 14);
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlBruteForce.add(label_3);
	}
}
