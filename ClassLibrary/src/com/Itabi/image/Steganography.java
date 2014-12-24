package com.Itabi.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Random;

import com.Itabi.util.Convert;
/**
 * Stellt ein Object zum speichern von Daten in einem <code>BufferedImage</code> dar.
 * @author Dorian Czichotzki
 * @version 1.0
 *
 */
@SuppressWarnings("unused")
public class Steganography {


	private BufferedImage img;
	private Random random;
	private File file; 
	private String text;
	private StegaType type;
	private int maxStorage;
	private byte[] storage = null;

	/**
	 * Konstruktor legt ein neues <code>Steganography</code> Object an.
	 * @param img Das zu manipulierende Bild.
	 * @param type Gibt die Art, der zu speichernden Daten, an.(Derzeit nur <code>StegaType.String</code>)
	 */
	public Steganography(BufferedImage img, StegaType type) {
		this.img = img;
		this.type = type;
		random = new Random();
		maxStorage = (img.getHeight() * img.getWidth()) / 3;
		storage = new byte[maxStorage];
		// TODO Auto-generated constructor stub
	}
	/**
	 * Veranlasst das <code>Steganography</code>-Object die gegebenen Daten in das <code>BufferedImage</code> zu schreiben.
	 */
	public void writeToImage(){
		prepareImage();
		if(type == StegaType.String){
			byte[] data = text.getBytes(Charset.forName("US-ASCII"));
			insertData(data,0);
			writeImageArr();
		}
	}
	/**
	 * Veranlasst das <code>Steganography</code>-Object, Daten aus dem <code>BufferedImage</code> zu lesen.
	 * @return Gibt an, ob der Vorgang erfolgreich war.
	 */
	public boolean readFromImage(){
		readImageArr();
		if(storage[0] == 1){
			byte[] l = {storage[5],storage[6],storage[7],storage[8]};
			int length= Convert.byteArrayToInt(l);
			byte[] data = new byte[length];
			for(int i =0;i< length;i++){
				data[i] = storage[i+9];
			}
			text = new String(data);
			type = StegaType.String;
			return true;
		}
		else
			return false;
	}
	/**
	 * Liefert den Text des Objekts.
	 * @return
	 */
	public String getText() {
		return text;
	}
	/**
	 * Gibt das <code>BufferedImage</code> des Objekts zurück.
	 * @return
	 */
	public BufferedImage getImg() {
		return img;
	}
	/**
	 * Übergiebt das <code>BufferedImage</code> ,in welches geschrieben werden soll.
	 * @param img
	 */
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	/**
	 * Setzt den zu speichernden Text.
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	private void prepareImage() {
		if (type == StegaType.String) {
			readImageArr();
			storage = new byte[9+text.length()];
			storage[0] = 1;
			storage[1] = 0;
			storage[2] = 0;
			storage[3] = 0;
			storage[4] = 0;
			byte[] bytes = Convert.intToByteArray(text.length());
			storage[5] = bytes[0];
			storage[6] = bytes[1];
			storage[7] = bytes[2];
			storage[8] = bytes[3];

		}
		if (type == StegaType.File) {
			//TODO: Pildvorbereitung für File einfügen.
		}
	}

	private void writeImageArr() {
		int arrIndex = 0;
		int counterPoint = 0;

		for (int i = 0; i < img.getWidth(); i++) {
			for (int a = 0; a < img.getHeight(); a++) {
				if(arrIndex >= text.length()+9){
					break;
				}
				if (counterPoint == 0) {
					int temp = (int)storage[arrIndex];
					Color c = new Color(img.getRGB(i, a));
					int r = zeroLowBits(c.getRed());
					int g = zeroLowBits(c.getGreen());
					int b = zeroLowBits(c.getBlue());
					r = r ^ getSingleBit(temp, 7);
					g = g ^ getSingleBit(temp, 6);
					b = b ^ getSingleBit(temp, 5);
					c = new Color(r, g, b);
					img.setRGB(i, a, c.getRGB());
					counterPoint++;
				} else if (counterPoint == 1) {
					int temp = (int)storage[arrIndex];
					Color c = new Color(img.getRGB(i, a));
					int r = zeroLowBits(c.getRed());
					int g = zeroLowBits(c.getGreen());
					int b = zeroLowBits(c.getBlue());
					r = r ^ getSingleBit(temp, 4);
					g = g ^ getSingleBit(temp, 3);
					b = b ^ getSingleBit(temp, 2);
					c = new Color(r, g, b);
					img.setRGB(i, a, c.getRGB());
					counterPoint++;
				} else if (counterPoint == 2) {
					int temp = (int)storage[arrIndex];
					Color c = new Color(img.getRGB(i, a));
					int r = zeroLowBits(c.getRed());
					int g = zeroLowBits(c.getGreen());
					int b = zeroLowBits(c.getBlue());
					r = r ^ getSingleBit(temp, 1);
					g = g ^ getSingleBit(temp, 0);
					b = b ^ random.nextInt(2);
					c = new Color(r, g, b);
					img.setRGB(i, a, c.getRGB());
					arrIndex++;
					counterPoint = 0;
				}
			}
		}
	}

	private void readImageArr() {
		int p1 = 0, p2 = 0, p3 = 0;
		int arrIndex = 0;
		int counterPoint = 0;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int a = 0; a < img.getHeight(); a++) {
				if(arrIndex >= 8){
					byte[] l = {storage[5],storage[6],storage[7],storage[8]};
					int length= Convert.byteArrayToInt(l);
					if(arrIndex >= 9+ length){
						break;
					}
				}
				if (counterPoint == 0) {
					p1 = img.getRGB(i, a);
					p1 = getLowBits(p1);
					p1 <<= 5;
					counterPoint++;
				} else if (counterPoint == 1) {
					p2 = img.getRGB(i, a);
					p2 = getLowBits(p2);
					p2 <<= 2;
					counterPoint++;
				} else if (counterPoint == 2) {
					p3 = img.getRGB(i, a);
					p3 = getLowBits(p3);
					p3 >>>= 1;
					int temp = p1 ^ p2;
					temp ^= p3;
					storage[arrIndex] = Integer.valueOf(temp).byteValue();
					arrIndex++;
					counterPoint = 0;
				}

			}
		}
	}

	private static int getLowBits(int rgb) {
		int a, b, c;
		a = rgb << 15;
		a >>>= 31;
		a <<=2;
		b = rgb << 23;
		b >>>= 31;
		b <<= 1;
		c = rgb << 31;
		c >>>= 31;
		int temp = a ^ b;
		temp ^= c;
		return temp;
	}

	private static int zeroLowBits(int a) {
		int b = a >>> 1;
		b <<= 1;
		return b;
	}

	private static int getSingleBit(int b, int position) {
		b <<= 7 - position;
		return b >>>= 7;
	}

	private void insertData(byte[] data, int offset) {
		for (int i = 0; i < data.length; i++) {
			storage[i + 9 + offset] = data[i];
		}
	}
}
