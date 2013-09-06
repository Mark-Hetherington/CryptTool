package org.hethos.cryptool.ui;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.hethos.cryptool.util.Base64;
import org.hethos.cryptool.util.CrackThread;
import org.hethos.cryptool.util.MonitorThread;
import org.hethos.cryptool.util.WorkerThread;
import org.hethos.cryptool.util.crypto;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class SwingUi extends JFrame {

	private JPanel contentPane;
	private JTextField tfEncrypt_clear;
	private JTextField tfEncrypt_Key;
	private JTextField tfDecrypt_cipher;
	private JTextField tfDecrypt_key;
	private JTextField tfCrack_Cipher;
	final JTextArea taLog = new JTextArea();
	private JSpinner sThreads;
	private CrackThread cracker;
	private JLabel lblProgress;

	/**
	 * Create the frame.
	 */
	public SwingUi() {
		final SwingUi TheUi = this;
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		taLog.setText("Log\r\n");
		taLog.setEditable(false);
		taLog.setBounds(10, 155, 424, 106);
		JScrollPane sp = new JScrollPane(taLog); 
		sp.setBounds(10, 155, 424, 105);
		contentPane.add(sp);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 424, 133);
		contentPane.add(tabbedPane);
		
		JPanel pnlGenerate = new JPanel();
		tabbedPane.addTab("Generate Key", null, pnlGenerate, null);
		pnlGenerate.setLayout(null);
		
		JButton btnNewButton = new JButton("Generate");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String Key = crypto.GenKey();
					System.out.println("Generated Key:"+Key);
				    taLog.append("Generated key:"+Key+"\r\n");
				    tfEncrypt_Key.setText(Key);
				    tfDecrypt_key.setText(Key);
				} catch (NoSuchAlgorithmException e) {
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
		
		tfEncrypt_clear = new JTextField();
		tfEncrypt_clear.setText("Hello");
		tfEncrypt_clear.setBounds(78, 8, 275, 20);
		pnlEncrypt.add(tfEncrypt_clear);
		tfEncrypt_clear.setColumns(10);
		
		tfEncrypt_Key = new JTextField();
		tfEncrypt_Key.setColumns(10);
		tfEncrypt_Key.setBounds(78, 36, 275, 20);
		pnlEncrypt.add(tfEncrypt_Key);
		
		JLabel lblKey = new JLabel("Key:");
		lblKey.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKey.setBounds(10, 39, 60, 14);
		pnlEncrypt.add(lblKey);
		
		JButton btnEncrypt = new JButton("Encrypt");
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//We are assuming non binary input in this case.
					String CipherText = crypto.Encrypt(Base64.encodeBytes(tfEncrypt_clear.getText().getBytes()), tfEncrypt_Key.getText());
					Log("cipher:"+CipherText);
					tfDecrypt_cipher.setText(CipherText);
					tfCrack_Cipher.setText(CipherText);
				} catch (InvalidKeyException e) {
					Log("Invalid Key");
					e.printStackTrace();				
				} catch (NoSuchAlgorithmException e) {
					Log("No such encryption algorithm");
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					Log("Incorrect padding");
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					Log("Illegal block Size");
					e.printStackTrace();
				} catch (BadPaddingException e) {
					Log("Bad Padding");
					e.printStackTrace();
				} catch (IOException e) {
					Log("Base64 decode fail");					
					e.printStackTrace();
				}			
			}
		});
		btnEncrypt.setBounds(10, 67, 89, 23);
		pnlEncrypt.add(btnEncrypt);
		
		JPanel pnlDecrypt = new JPanel();
		tabbedPane.addTab("Decrypt", null, pnlDecrypt, null);
		pnlDecrypt.setLayout(null);
		
		JLabel lblCipherTextbase = new JLabel("Cipher Text (base64):");
		lblCipherTextbase.setBounds(10, 11, 108, 14);
		pnlDecrypt.add(lblCipherTextbase);
		lblCipherTextbase.setHorizontalAlignment(SwingConstants.RIGHT);
		
		tfDecrypt_cipher = new JTextField();
		tfDecrypt_cipher.setBounds(128, 11, 281, 20);
		pnlDecrypt.add(tfDecrypt_cipher);
		tfDecrypt_cipher.setColumns(10);
		
		tfDecrypt_key = new JTextField();
		tfDecrypt_key.setBounds(128, 36, 281, 20);
		pnlDecrypt.add(tfDecrypt_key);
		tfDecrypt_key.setColumns(10);
		
		JLabel label_2 = new JLabel("Key:");
		label_2.setBounds(48, 36, 70, 14);
		pnlDecrypt.add(label_2);
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnDecrypt = new JButton("Decrypt");
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String Clear64 = crypto.Decrypt(tfDecrypt_cipher.getText(), tfDecrypt_key.getText());
					Log("base64 cleartext:"+Clear64);
					Log("Clear text:"+new String(Base64.decode(Clear64)));
				} catch (InvalidKeyException e1) {
					Log("Invalid key");
					e1.printStackTrace();
				} catch (IOException e1) {
					Log("Base64 Decode failure");
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					Log("No such algorithm");
					e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {
					Log("Incorrect padding spec");
					e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {
					Log("Illegal block size");
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					Log("Bad Padding");
					e1.printStackTrace();
				}
			}
		});
		btnDecrypt.setBounds(10, 61, 89, 23);
		pnlDecrypt.add(btnDecrypt);
		
		JPanel pnlBruteForce = new JPanel();
		tabbedPane.addTab("Crack", null, pnlBruteForce, null);
		pnlBruteForce.setLayout(null);
		
		tfCrack_Cipher = new JTextField();
		tfCrack_Cipher.setBounds(80, 5, 266, 20);
		tfCrack_Cipher.setColumns(10);
		pnlBruteForce.add(tfCrack_Cipher);
		
		JLabel label_3 = new JLabel("Cipher Text:");
		label_3.setBounds(10, 8, 60, 14);
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlBruteForce.add(label_3);
		
		JButton btnCrack = new JButton("Crack");
		btnCrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//start the cracking thread
		        cracker = new CrackThread(TheUi, tfCrack_Cipher.getText(),(Integer)sThreads.getValue());
		        Thread CrackingThread = new Thread(cracker);
		        CrackingThread.start();
			}
		});
		btnCrack.setBounds(10, 71, 89, 23);
		pnlBruteForce.add(btnCrack);
		
		JLabel lblThreads = new JLabel("Threads:");
		lblThreads.setBounds(20, 33, 46, 14);
		pnlBruteForce.add(lblThreads);
		
		sThreads = new JSpinner();
		sThreads.setModel(new SpinnerNumberModel(new Integer(8), new Integer(0), null, new Integer(1)));
		sThreads.setBounds(70, 30, 29, 20);
		pnlBruteForce.add(sThreads);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cracker.shutdown();
			}
		});
		btnStop.setBounds(109, 71, 89, 23);
		pnlBruteForce.add(btnStop);
		
		lblProgress = new JLabel("");
		lblProgress.setBounds(109, 36, 300, 14);
		pnlBruteForce.add(lblProgress);
	}
	
	public void Log(String Message) {
		System.out.println(Message);
	    taLog.append(Message+"\r\n");
	}
	public void CrackProgress(double percentProgress,double keysPerSecond){
		lblProgress.setText(String.format("%1$,.2f%%, %1$,.2f Keys/s",percentProgress,keysPerSecond));
		System.out.println(String.format("%1$,.2f%%, %1$,.2f Keys/s",percentProgress,keysPerSecond));
	}
}
