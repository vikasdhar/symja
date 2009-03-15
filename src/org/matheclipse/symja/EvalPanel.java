package org.matheclipse.symja;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.matheclipse.core.eval.CompletionLists;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.MathMLUtilities;
import org.matheclipse.core.eval.TimeConstrainedEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.core.util.WriterOutputStream;

public class EvalPanel extends JPanel implements DocumentListener {
	private static final String COMMIT_ACTION = "commit";

	private static enum Mode {
		INSERT, COMPLETION
	};

	private String fReplacement;
	private int fW;

	private Mode mode = Mode.INSERT;
	private final List<String> fWords = new ArrayList<String>(2048);
	private final List<String> fReplaceWords = new ArrayList<String>(2048);

	private JTextArea jInputArea = null;

	private JScrollPane jScrollInputPane = null;

	private JScrollPane jScrollOutputPane = null;

	private JTextPane jOutputPane = null;

	/* custom created attributes */
	final static long serialVersionUID = 0x000000001;

	private final static String versionStr = "Keyboard shortcuts\n" + "- Ctrl+ENTER - for symbolic evaluation\n"
			+ "- Cursor up - previoues input\n" + "- Cursor down - next input\n";

	private DefaultStyledDocument jOutputDoc;

	private SimpleAttributeSet outputAtr, errorAtr, outputOpAtr, outputStringAtr;

	private SimpleAttributeSet outputNum1Atr, outputNum2Atr, outputBracketAtr, outputCommentAtr;

	private int fontSize;

	private final String commandHistory[] = new String[20];

	private int commandHistoryStoreIndex = 0;

	private int commandHistoryReadIndex = 0;

	private Component popupSource;

	public static EvalEngine EVAL_ENGINE;

	public static TimeConstrainedEvaluator EVAL;

	final static String bracketChars = "[](){}";

	final static String operatorChars = "~+*;,.#'-:<>|&/=!^@";

	final static String numericBreakCharacters = operatorChars + bracketChars;

	private Color cColor[]; /* selected color */

	private final Color cColorDefault[] /* default colors */= { Color.BLUE, Color.BLACK, new Color(100, 100, 255), Color.GRAY,
			new Color(255, 100, 0), new Color(100, 0, 50), new Color(50, 150, 0), Color.RED };

	private Thread fInitThread = null;

	/**
	 * If a string is on the system clipboard, this method returns it; otherwise
	 * it returns null.
	 */
	private static String getClipboard() {
		final Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

		try {
			if ((t != null) && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				final String text = (String) t.getTransferData(DataFlavor.stringFlavor);
				return text;
			}
		} catch (final UnsupportedFlavorException e) {
		} catch (final IOException e) {
		}
		return null;
	}

	// This method writes a string to the system clipboard.
	// otherwise it returns null.
	private static void setClipboard(final String str) {
		final StringSelection ss = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}

	/**
	 * Calculation thread implemented as internal class
	 */
	class CalcThread extends Thread {
		String command;

		/**
		 * Pass command string to calculation thread. Must be called before starting
		 * the thread
		 * 
		 * @param cmd
		 *          Command string to parse
		 */
		public void setCommand(final String cmd) {
			command = cmd;
		}

		/**
		 * Thread run method
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				final StringBufferWriter printBuffer = new StringBufferWriter();
				final PrintStream pout = new PrintStream(new WriterOutputStream(printBuffer));

				EVAL_ENGINE.setOutPrintStream(pout);

				final StringBufferWriter buf = new StringBufferWriter();

				eval(buf, command);
				if (printBuffer.getBuffer().length() > 0) {
					printOut(printBuffer.toString() + "\n\n");
				}
				if (buf.getBuffer().length() > 0) {
					printOutColored("Out[" + commandHistoryStoreIndex + "]=" + buf.toString() + "\n");
				}
			} catch (final Throwable ex) {
				// ex.printStackTrace();
				String mess = ex.getMessage();
				if (mess == null) {
					printErr(ex.getClass().getName());
				} else {
					printErr(mess);
				}
			} finally {
				setBusy(false);
			}
		}

		protected double[][] eval(final StringBufferWriter buf, final String evalStr) throws Exception {
			final IExpr expr = EVAL.constrainedEval(buf, evalStr);
			if (expr instanceof IAST) {
				final IAST show = (IAST) expr;
				if ((show.size() == 2) && show.isAST("Show")) {
					final IAST graphics = (IAST) show.get(1);
					if (graphics.isAST("Graphics")) {
						// example: Plot[Sin[x],{x,0,10}]
						final IAST data = (IAST) graphics.get(1);
						if (data.isAST("Line") && data.get(1).isList()) {
							final IAST lineData = (IAST) data.get(1);
							IAST pair;
							final double[][] plotPoints = new double[lineData.size() - 1][2];
							for (int i = 1; i < lineData.size(); i++) {
								pair = (IAST) lineData.get(i);
								plotPoints[i - 1][0] = ((INum) pair.get(1)).getRealPart();
								plotPoints[i - 1][1] = ((INum) pair.get(2)).getRealPart();
								// plotPoints[1][i-1] = ((IDouble)pair.get(2)).getRealPart();
							}
							return plotPoints;

						}
					} else if (graphics.get(0).isAST("SurfaceGraphics")) {
						// Plot3D[Sin[x]*Cos[y],{x,-10,10},{y,-10,10}]
					}
				}
			}
			return null;
		}
	}

	/**
	 * Enable/disable busy cursor and command input
	 * 
	 * @param busy
	 *          true: enter busy state, false: leave busy state
	 */
	private void setBusy(final boolean busy) {
		if (busy) {
			jInputArea.setEditable(false);
			// jMenuItemBreak.setEnabled(true);
			// jPopupMenuItemBreak.setEnabled(true);
			jOutputPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			jInputArea.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		} else {
			jInputArea.setEditable(true);
			// jMenuItemBreak.setEnabled(false);
			// jPopupMenuItemBreak.setEnabled(false);
			jOutputPane.setCursor(Cursor.getDefaultCursor());
			jInputArea.setCursor(Cursor.getDefaultCursor());
			jInputArea.requestFocus();
		}
	}

	class PopupListener extends MouseAdapter {
		@Override
		public void mousePressed(final MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(final MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupSource = e.getComponent();
				final boolean canPaste = ((JTextComponent) popupSource).isEditable() && (getClipboard() != null);
				final boolean canCopy = ((JTextComponent) popupSource).getSelectedText() != null;
				// jPopupMenuItemPaste.setEnabled(canPaste);
				// jPopupMenuItemCopy.setEnabled(canCopy);
				// jPopupMenu.show(popupSource, e.getX(), e.getY());
			}
		}
	}

	/**
	 * Checks is a character is a valid (starting) character for a number
	 * 
	 * @param c
	 *          the char to be checked
	 * @return true (is numeric) or false (is not numeric)
	 */
	private boolean isNumeric(final char c) {
		return ("0123456789.".indexOf(c) != -1);
	}

	/**
	 * Color text in output pane depending on the contents of the inserted string
	 * 
	 * @param s
	 *          String that was inserted into the output pane
	 * @param offset
	 *          Start offset where string was inserted
	 */
	private void colorOutput(final String s, final int offset) {
		int startIdx = 0;
		char c;
		for (int idx = 0; idx < s.length(); idx++) {
			c = s.charAt(idx);
			startIdx = idx;
			// check for line comments
			if ((c == '/') && (idx < s.length() - 1) && (s.charAt(idx + 1) == '/')) {
				startIdx = idx;
				idx++;
				while ((++idx < s.length()) && ((c = s.charAt(idx)) != '\n')) {
					;
				}
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx, outputCommentAtr, true);
			}
			// check for operators
			startIdx = idx;
			while ((operatorChars.indexOf(c) != -1) && (idx < s.length())) {
				c = s.charAt(++idx);
			}
			if (idx != startIdx) {
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx, outputOpAtr, true);
			}
			// check for brackets (no own state, since there's usually only one
			// bracket in a row)
			if (bracketChars.indexOf(c) != -1) {
				jOutputDoc.setCharacterAttributes(offset + idx, 1, outputBracketAtr, true);
				continue;
			}
			// check for strings
			if (c == '"') {
				startIdx = idx;
				while ((++idx < s.length()) && ((c = s.charAt(idx)) != '"')) {
					;
				}
				jOutputDoc.setCharacterAttributes(offset + startIdx, idx - startIdx + 1, outputStringAtr, true);
			}
			// check for numbers
			if (isNumeric(c)) {
				int len, step, point = -1;
				boolean color1 = true;
				startIdx = idx;
				// search for break character
				while ((++idx < s.length()) && (numericBreakCharacters.indexOf(c = s.charAt(idx)) == -1)) {
					if (c == '.') {
						point = idx - startIdx; // position of point inside number
					}
				}
				if (idx != s.length()) {
					idx--; // idx on last numeric character
				}
				len = idx - startIdx + 1;
				step = 3;
				// check kind of number
				if (len > 1) {
					if (s.charAt(startIdx) == '0') {
						// special cases: octal, binary, hexadecimal
						switch (s.charAt(startIdx + 1)) {
						case 'x':
							step = 2;
							startIdx += 2;
							len -= 2;
							break; // octal
						case 'b':
							step = 4;
							startIdx += 2;
							len -= 2;
							break; // octal
						case '.':
							step = 3;
							break; // decimal
						default:
							step = 2;
							break; // octal
						}
					}
				}

				if (point != -1) {
					int step2 = step;
					// special case: decimal point
					// first color characters after the point from left to right
					for (int pos = point + 1; pos < len; pos += step2) {
						if (pos + step2 >= len) {
							step2 = len - pos; // don't color outside number
						}
						jOutputDoc.setCharacterAttributes(offset + startIdx + pos, step2, color1 ? outputNum1Atr : outputNum2Atr, true);
						color1 = !color1; // toggle
					}
					// then color characters before the point from right to left
					len = point;
					color1 = true;
				}
				for (int pos = len; pos > 0; pos -= step) {
					if (pos - step < 0) {
						step = pos;
					}
					jOutputDoc.setCharacterAttributes(offset + startIdx + pos - step, step, color1 ? outputNum1Atr : outputNum2Atr, true);
					color1 = !color1; // toggle
				}

			}
		}
	}

	/**
	 * Print text to output pane with syntax coloring
	 * 
	 * @param s
	 *          text to print
	 */
	public void printOutColored(final String s) {
		try {
			jOutputDoc.insertString(0, s, outputAtr);
			// now start the coloring
			colorOutput(s, 0);
			jOutputPane.setCaretPosition(0);// jOutputDoc.getLength());
		} catch (final BadLocationException ble) {
			System.out.println("Couldn't write to Output Pane");
		}
	}

	/**
	 * Print text to output pane
	 * 
	 * @param s
	 *          text to print
	 */
	public void printOut(final String s) {
		try {
			jOutputDoc.insertString(0, s, outputAtr);
			jOutputPane.setCaretPosition(0);// jOutputDoc.getLength());
		} catch (final BadLocationException ble) {
			System.out.println("Couldn't write to Output Pane");
		}
	}

	/**
	 * Print text to error pane
	 * 
	 * @param s
	 *          text to print
	 */
	public void printErr(final String s) {
		if (s == null) {
			return;
		}
		try {
			jOutputDoc.insertString(0, s + "\n", errorAtr);
			jOutputPane.setCaretPosition(0);// jOutputDoc.getLength());
		} catch (final BadLocationException ble) {
			System.out.println("Couldn't write to Output Pane");
		}
	}

	public class InitThread extends Thread {
		@Override
		public void run() {
			F.initSymbols();
			EVAL_ENGINE = new EvalEngine();
			EVAL = new TimeConstrainedEvaluator(EVAL_ENGINE, false, 360000);
			new CompletionLists(fWords, fReplaceWords);
		}
	}

	private JScrollPane getJScrollInputPane() {
		if (jScrollInputPane == null) {
			jScrollInputPane = new JScrollPane();
			jScrollInputPane.setViewportView(getJInputArea());
		}
		return jScrollInputPane;
	}

	/**
	 * This method initializes jInputField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextArea getJInputArea() {
		if (jInputArea == null) {
			jInputArea = new JTextArea(4, 80);
			jInputArea.setEditable(false);
			jInputArea.setText("Loading library...");
			jInputArea.getDocument().addDocumentListener(this);
			InputMap im = jInputArea.getInputMap();
			ActionMap am = jInputArea.getActionMap();
			im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
			am.put(COMMIT_ACTION, new CommitAction());

			jInputArea.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyPressed(final java.awt.event.KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
						evalSymbolicInputField();
						return;
					}
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						commandHistoryReadIndex--;
						if (commandHistoryReadIndex < 0) {
							commandHistoryReadIndex = commandHistory.length - 1;
						}
						jInputArea.setText(commandHistory[commandHistoryReadIndex]);
						break;
					case KeyEvent.VK_DOWN:
						commandHistoryReadIndex++;
						if (commandHistoryReadIndex >= commandHistory.length) {
							commandHistoryReadIndex = 0;
						}
						jInputArea.setText(commandHistory[commandHistoryReadIndex]);
						break;
					}
				}
			});

		}
		return jInputArea;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollOutputPane() {
		if (jScrollOutputPane == null) {
			jScrollOutputPane = new JScrollPane();
			jScrollOutputPane.setViewportView(getJOutputPane());
		}
		return jScrollOutputPane;
	}

	/**
	 * This method initializes jOutputPane
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JTextPane getJOutputPane() {
		if (jOutputPane == null) {
			jOutputPane = new JTextPane();
			jOutputPane.setEditable(false);
		}
		return jOutputPane;
	}

	private void evalSymbolicInputField() {
		final String cmd = jInputArea.getText();
		jInputArea.setText("");
		if (cmd.length() > 0) {
			evalSymbolic(cmd);
		}
	}

	private void evalSymbolic(final String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		jInputArea.setText("");
		printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd + "\n\n");
		setBusy(true);
		CalcThread calcThread = new CalcThread();
		calcThread.setCommand(cmd);
		EventQueue.invokeLater(calcThread);
	}

	private void evalNumeric(String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		cmd = "N[" + cmd + "]";
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		jInputArea.setText("");
		printOutColored("In[" + commandHistoryStoreIndex + "]=" + cmd + "\n\n");
		setBusy(true);
		CalcThread calcThread = new CalcThread();
		calcThread.setCommand(cmd);
		calcThread.start();
	}

	protected void createMathML(final String cmd) {
		if (fInitThread != null) {
			try {
				fInitThread.join();
			} catch (InterruptedException e) {
			}
		}
		commandHistory[commandHistoryStoreIndex++] = cmd;
		if (commandHistoryStoreIndex >= commandHistory.length) {
			commandHistoryStoreIndex = 0;
		}
		commandHistoryReadIndex = commandHistoryStoreIndex;
		final MathMLUtilities mathUtil = new MathMLUtilities(EVAL_ENGINE, false);
		final StringBufferWriter buf = new StringBufferWriter();
		try {
			mathUtil.toMathML(cmd, buf);
			printOutColored("MathML:\n" + buf.toString() + "\n\n");
		} catch (final Exception e) {
			e.printStackTrace();
			String mess = e.getMessage();
			if (mess == null) {
				printOutColored(e.getClass().getName());
			} else {
				printOutColored(e.getMessage());
			}
		}
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return JPanel
	 */
	public EvalPanel() {
		super();

		final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		final GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		setLayout(new GridBagLayout());
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 0.25;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;

		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;

		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints3.insets = new Insets(3, 3, 3, 3);
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.weighty = 0.75;
		gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;

		final JPanel bPanel = new JPanel(); // implicit FlowLayout
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 3, 5, 5));
		final JButton b1 = new JButton("Symbolic");
		b1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				evalSymbolicInputField();
			}
		});
		buttonsPanel.add(b1);
		final JButton b2 = new JButton("Numeric");
		b2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				final String cmd = jInputArea.getText();
				jInputArea.setText("");
				if (cmd.length() > 0) {
					evalNumeric(cmd);
				}
			}
		});
		buttonsPanel.add(b2);
		final JButton b3 = new JButton("MathML");
		b3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				final String cmd = jInputArea.getText();
				jInputArea.setText("");
				if (cmd.length() > 0) {
					createMathML(cmd);
				}
			}
		});
		buttonsPanel.add(b3);
		bPanel.add(buttonsPanel);

		add(getJScrollInputPane(), gridBagConstraints1);
		add(bPanel, gridBagConstraints2);
		add(getJScrollOutputPane(), gridBagConstraints3);
		
		fInitThread = new InitThread();
		fInitThread.start();
		
		fontSize = 12;
		cColor = new Color[8];
		cColor[Colors.OUTPUT] = new Color(cColorDefault[Colors.OUTPUT].getRGB());
		cColor[Colors.ERROR] = new Color(cColorDefault[Colors.ERROR].getRGB());
		cColor[Colors.OPERATOR] = new Color(cColorDefault[Colors.OPERATOR].getRGB());
		cColor[Colors.STRING] = new Color(cColorDefault[Colors.STRING].getRGB());
		cColor[Colors.NUM1] = new Color(cColorDefault[Colors.NUM1].getRGB());
		cColor[Colors.NUM2] = new Color(cColorDefault[Colors.NUM2].getRGB());
		cColor[Colors.BRACKET] = new Color(cColorDefault[Colors.BRACKET].getRGB());
		cColor[Colors.COMMENT] = new Color(cColorDefault[Colors.COMMENT].getRGB());
		
		
		int width, height, posX, posY;
		width = 300;
		height = 200;
		this.setSize(width, height);
		final Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		p.x -= 300 / 2;
		p.y -= 200 / 2;
		posX = p.x > 0 ? p.x : 0;
		posY = p.y > 0 ? p.y : 0;
		this.setLocation(posX, posY);
		validate(); // force redraw
		setVisible(true);

		jOutputDoc = new DefaultStyledDocument();
		jOutputPane.setDocument(jOutputDoc);
		final Font f = new Font("Monospaced", Font.PLAIN, fontSize);
		jOutputPane.setFont(f);
		jInputArea.setFont(f);
		// height = jInputArea.getGraphics().getFontMetrics().getHeight();
		jInputArea.setBounds(jInputArea.getBounds().x, getHeight() - height, jInputArea.getWidth(), height);
		doLayout();

		final MouseListener popupListener = new PopupListener();
		jOutputPane.addMouseListener(popupListener);
		jInputArea.addMouseListener(popupListener);

		// error
		errorAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(errorAtr, cColor[Colors.ERROR]);
		// normal output
		outputAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputAtr, cColor[Colors.OUTPUT]);
		// operator output
		outputOpAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputOpAtr, cColor[Colors.OPERATOR]);
		// string output
		outputStringAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputStringAtr, cColor[Colors.STRING]);
		// number 1 output
		outputNum1Atr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputNum1Atr, cColor[Colors.NUM1]);
		// number 2 output
		outputNum2Atr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputNum2Atr, cColor[Colors.NUM2]);
		// bracket output
		outputBracketAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputBracketAtr, cColor[Colors.BRACKET]);
		// comment output
		outputCommentAtr = new SimpleAttributeSet();
		StyleConstants.setForeground(outputCommentAtr, cColor[Colors.STRING]);

		printOut(versionStr + "\n");

		// request Focus
		jInputArea.requestFocus();

		jInputArea.setText("");
		jInputArea.setEditable(true);
	}

	private class Colors {
		final static int OUTPUT = 0;

		final static int NUM1 = 1;

		final static int NUM2 = 2;

		final static int COMMENT = 3;

		final static int OPERATOR = 4;

		final static int BRACKET = 5;

		final static int STRING = 6;

		final static int ERROR = 7;
	}

	// Listener methods

	public void changedUpdate(DocumentEvent ev) {
	}

	public void removeUpdate(DocumentEvent ev) {
	}

	public void insertUpdate(DocumentEvent ev) {
		if (ev.getLength() != 1) {
			return;
		}

		int pos = ev.getOffset();
		String content = null;
		try {
			content = jInputArea.getText(0, pos + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		// Find where the word starts
		int w;
		for (w = pos; w >= 0; w--) {
			if (!Character.isLetter(content.charAt(w))) {
				break;
			}
		}
		if (pos - w < 2) {
			// Too few chars
			return;
		}

		String prefix = content.substring(w + 1).toLowerCase();
		int n = Collections.binarySearch(fWords, prefix);
		if (n < 0 && -n <= fWords.size()) {
			String match = fWords.get(-n - 1);
			if (match.startsWith(prefix)) {
				// A completion is found
				String completion = match.substring(pos - w);
				// We cannot modify Document from within notification,
				// so we submit a task that does the change later
				SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1, w + 1, fReplaceWords.get(-n - 1)));
			}
		} else {
			// Nothing found
			mode = Mode.INSERT;
		}
	}

	private class CompletionTask implements Runnable {
		String completion;
		int position;
		int w;
		String replacement;

		CompletionTask(String completion, int position, int w, String replacement) {
			this.completion = completion;
			this.position = position;
			this.w = w;
			this.replacement = replacement.substring(0, position - w);
		}

		public void run() {
			jInputArea.insert(completion, position);
			jInputArea.setCaretPosition(position + completion.length());
			jInputArea.moveCaretPosition(position);
			fW = w;
			fReplacement = replacement;
			mode = Mode.COMPLETION;
		}
	}

	private class CommitAction extends AbstractAction {
		public void actionPerformed(ActionEvent ev) {
			if (mode == Mode.COMPLETION) {
				int pos = jInputArea.getSelectionEnd();
				jInputArea.replaceRange(fReplacement, fW, fW + fReplacement.length());
				// jInputField.insert(" ", pos);
				try {
					String endChar = jInputArea.getText(pos - 1, 1);
					if ("]".equals(endChar)) {
						jInputArea.setCaretPosition(pos - 1);
					} else {
						jInputArea.setCaretPosition(pos);
					}
				} catch (BadLocationException e) {
					jInputArea.setCaretPosition(pos);
				}
				mode = Mode.INSERT;
			} else {
				jInputArea.replaceSelection("\n");
			}
		}
	}

}
