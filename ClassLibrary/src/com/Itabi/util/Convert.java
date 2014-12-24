package com.Itabi.util;

/**
 * Bieetet eine Sammlung von Konvertierungsmethoden.
 * 
 * @author Dorian Czichotzki
 *
 */
public final class Convert {

	private Convert() {

	}

	/**
	 * Macht aus einem <code>Integer</code> eine <code>Byte</code>-Array.
	 * 
	 * @param value
	 *            <code>int</code>, der Konvertiert werden soll.
	 * @return Resultierendes <code>Byte</code>-Array.
	 */
	public static final byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}

	/**
	 * Macht aus einem <code>Byte</code>-Array einen <code>Integer</code>.
	 * <p>
	 * <b>Wichtig:</b> Nur die ersten vier Einträge des Array werden Gelesen!
	 * Wenn das Array kleiner ist resultiert das Ergebnis 0.
	 * </p>
	 * 
	 * @param b
	 *            Zu konvertierendes <code>Byte</code>-Array.
	 * @return Resultierender <code>Integer</code>.
	 */
	public static int byteArrayToInt(byte[] b) {
		int value = 0;
		if (b.length > 4) {
			return 0;
		}
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}
}
