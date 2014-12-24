package com.Itabi.image;


import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Stellt ein Bild in einer Swingkomponente dar.
 * 
 * @author Dorian Czichotzki
 * @version 1.0
 *
 */

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5766401315635450924L;

	private BufferedImage img, original;
	private boolean fixedSize;
	private boolean dropSizeFixed = true;
	private JPopupMenu menu;
	private JMenuItem saveButton, openButton, showOriginal;

	/**
	 * Der Konstruktor inizialisiert das ImagePanel und muss vor der Nutzung
	 * aufgerufen werden.
	 */
	public ImagePanel() {
		this.Init();
	}

	/**
	 * 
	 */
	public void paint(Graphics gr) {
		gr.setColor(this.getBackground());
		gr.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (img != null) {
			Graphics2D g = (Graphics2D) gr;
			g.setColor(this.getBackground());
			g.fill(g.getClipBounds());
			if (fixedSize == false) {
				g.drawImage(img, 0, 0, null);
				g.dispose();
			} else {
				if (img.getWidth() <= this.getWidth()
						&& img.getHeight() <= this.getHeight()) {
					g.drawImage(img, (this.getWidth() - img.getWidth()) / 2,
							(this.getHeight() - img.getHeight()) / 2, null);
					g.dispose();
				} else {
					if (img.getWidth() > this.getWidth()
							&& img.getHeight() < this.getHeight()) {
						int newHeight = (img.getHeight() * this.getWidth())
								/ img.getWidth();
						g.drawImage(img, 0, (this.getHeight() - newHeight) / 2,
								this.getWidth(), newHeight, null);
						g.dispose();
					}
					if (img.getWidth() < this.getWidth()
							&& img.getHeight() > this.getHeight()) {
						int newWidth = (img.getWidth() * this.getHeight())
								/ img.getHeight();
						g.drawImage(img, (this.getWidth() - newWidth) / 2, 0,
								newWidth, this.getHeight(), null);
						g.dispose();
					}
					if (img.getWidth() > this.getWidth()
							&& img.getHeight() > this.getHeight()) {
						if (img.getWidth() - this.getWidth() < img.getHeight()
								- this.getHeight()) {
							int newWidth = (img.getWidth() * this.getHeight())
									/ img.getHeight();
							g.drawImage(img, (this.getWidth() - newWidth) / 2,
									0, newWidth, this.getHeight(), null);
							g.dispose();
						}
						if (img.getWidth() - this.getWidth() > img.getHeight()
								- this.getHeight()) {
							int newHeight = (img.getHeight() * this.getWidth())
									/ img.getWidth();
							g.drawImage(img, 0,
									(this.getHeight() - newHeight) / 2,
									this.getWidth(), newHeight, null);
							g.dispose();
						}
					}

				}
			}
		}
	}

	private void Init() {
		setLayout(null);
		menu = new JPopupMenu();
		openButton = new JMenuItem("÷ffnen...");
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadImage();
			}

		});
		saveButton = new JMenuItem("Speichern unter...");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveImage(original);
			}

		});
		showOriginal = new JMenuItem("In Fenster anzeigen");
		showOriginal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showInFrame();
			}

		});
		showOriginal.setEnabled(false);
		menu.add(openButton);
		menu.add(saveButton);
		menu.add(new JSeparator());
		menu.add(showOriginal);
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && original != null) {
					showInFrame();
				}
				if (e.getButton() == 3) {
					menu.show(ImagePanel.this, e.getX(), e.getY());
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});
	}

	/**
	 * Setzt das entsprechende <code>BufferedImage</code> im
	 * <code>ImagePanel</code>
	 * 
	 * @param image
	 *            Zu ladendes <code>BufferedImage</code>.
	 * @param fixedSize
	 *            Legt fest, ob die Gr√∂√üe des <code>BufferedImage</code> auf
	 *            das <code>ImagePanel</code> angepasst wird.
	 * 
	 */
	public void setImage(BufferedImage image, boolean fixedSize) {
		this.img = image;
		this.original = deepCopy(image);
		this.fixedSize = fixedSize;
		showOriginal.setEnabled(false);

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage img2 = gc.createCompatibleImage(img.getWidth(),
				img.getHeight(), Transparency.TRANSLUCENT);
		Graphics2D g = img2.createGraphics();

		if (fixedSize == false) {
			// this.setPreferredSize(new Dimension(img.getWidth(),
			// img.getHeight()));
			this.setBounds(this.getX(), this.getY(), img.getWidth(),
					img.getHeight());
			g.drawImage(img, 0, 0, null);
			g.dispose();
			img = img2;
		} else {
			g.drawImage(img, 0, 0, null);
			g.dispose();
			img = img2;
		}

		this.repaint();
	}

	/**
	 * L√§dt ein Bild mit Hilfe eines Pfades.
	 * 
	 * @param path
	 *            Pfad der Datei.
	 * @return Geladenes Bild
	 */
	public static BufferedImage load(String path) {
		try {
			BufferedImage img = ImageIO.read(new File(path));
			return img;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * L√§dt ein Bild mit Hilfe einer File Instanz.
	 * 
	 * @param file
	 *            File Instanz.
	 * @return Geladenes Bild.
	 */
	public static BufferedImage load(File file) {
		try {
			BufferedImage img = ImageIO.read(file);
			return img;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gibt das aktuelle Bild in Originalgr√∂√üe zur√ºck.
	 * 
	 * @return Aktuelles Bild
	 */
	public BufferedImage getImage() {
		return original;
	}

	/**
	 * Fertigt eine Tiefenkopie eines <code>BufferedImage</code> an.
	 * 
	 * @param picture
	 *            Zu kopierendes <code>BufferedImage</code>.
	 * @return Kopiertes <code>BufferedImage</code>.
	 * 
	 *         <p>
	 *         <b>Beispiel:</b>
	 *         <p>
	 *         <code>BufferedImage newImage = ImagePanel.deepCopy(old Image);</code>
	 */
	public static BufferedImage deepCopy(BufferedImage picture) {
		ColorModel cm = picture.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = picture.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);

	}

	/**
	 * Gibt den aktuellen Status von <code>DropSizeFixed</code> zur√ºck.
	 * <code>DropSizeFixed</code> gibt an, ob sich das
	 * <code>BufferedImage</code>,beim Dropdown, dem <code>ImagePanel</code>
	 * anpasst.
	 * 
	 * @return <code>true</code> wenn das <code>BufferedImage</code> dem
	 *         <code>ImagePanel</code> angepasst wird; <code>false</code> wenn
	 *         das <code>ImagePanel</code> dem <code>BufferedImage</code>
	 *         angepasst wird.
	 */
	public boolean isDropSizeFixed() {
		return dropSizeFixed;
	}

	/**
	 * Setzt den aktuellen Status von <code>DropSizeFixed</code>.
	 * <code>DropSizeFixed</code> gibt an, ob sich das
	 * <code>BufferedImage</code>,beim Dropdown, dem <code>ImagePanel</code>
	 * anpasst.
	 * 
	 * @param dropSizeMode
	 *            Der gew√ºnschte Zustand.
	 * 
	 *            <p>
	 *            Es wird empfohlen <code>dropSizeFixed</code> auf
	 *            <code>true</code> zu setzten, um unerw√ºnchtes Ausbreiten des
	 *            <code>ImagePanel</code> zu verhindern.
	 */
	public void setDropSizeFixed(boolean dropSizeMode) {
		this.dropSizeFixed = dropSizeMode;
	}

	/**
	 * Sorgt daf¸r, dass der Inhalt des <code>ImagePanel</code> in einem
	 * Fenster in Originalgrˆﬂe angezeigt wird.
	 */
	public void showInFrame() {
		JFrame frame = new JFrame();

		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(original)));
		frame.getContentPane().setMaximumSize(
				Toolkit.getDefaultToolkit().getScreenSize());

		frame.pack();
		frame.setLocationRelativeTo(ImagePanel.this);
		frame.setResizable(false);
		frame.setMaximizedBounds(new Rectangle(0, 0, (int) Toolkit
				.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit
				.getDefaultToolkit().getScreenSize().getHeight()));
		frame.setVisible(true);
	}

	/**
	 * Speichert ein Bild unter Zuhilfenahme des <code>JFileChooser</code> an
	 * einem angegebenen Ort.
	 * 
	 * @param simg
	 *            Zu speicherndes Bild.
	 */
	public static void saveImage(BufferedImage simg) {
		if (simg != null) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter bmp = new FileNameExtensionFilter("Bmp",
					"bmp");
			chooser.addChoosableFileFilter(bmp);
			chooser.setFileFilter(bmp);
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
			chooser.setDialogTitle("Speichern unter...");
			chooser.setVisible(true);

			int result = chooser.showSaveDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getAbsolutePath();
				path += "bmp";
				File pic = new File(path);
				try {
					ImageIO.write(simg, "bmp", pic);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else
			JOptionPane.showMessageDialog(null,
					"Kein Bild zum speichern vorhanden",
					"Aktion nicht mˆglich", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * L‰dt ein Bild mit einem <code>JFileChooser</code> in das
	 * <code>ImagePanel</code>
	 */
	public void loadImage() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Bild",
				"bmp");
		JFileChooser opendialog = new JFileChooser();
		opendialog.addChoosableFileFilter(filter);
		opendialog.setFileFilter(filter);

		if (opendialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File pic = opendialog.getSelectedFile();
			try {
				this.setImage(ImageIO.read(pic), true);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
