package com.Itabi.gui;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class JAdvancedTextArea extends JTextArea {
	/**
	 * 
	 */
	private int linePosition;
	private static final long serialVersionUID = 1L;
	
	public JAdvancedTextArea(){
		super();
		linePosition = 0;
	}
	/**
	 * Die Methode kann dazu verwendet werden die TextArea Zeile für Zeile auszulesen.
	 * @return Gibt die nächste Zeile als String zurück.
	 * @throws BadLocationException - Wenn keine Zeile gelesen werden konnte
	 */
	public String getNextLine() throws BadLocationException{
		int maxLines = this.getLineCount() - 1;
		if(linePosition > maxLines){
			throw new BadLocationException("No more Lines in this TextArea",0);
		}
		String output = new String();
		int start,end;
		start = this.getLineStartOffset(linePosition);
		end= this.getLineEndOffset(linePosition);
		output= this.getText(start, end-start);
		output.replaceAll("\n", "");
		linePosition++;
		return output;
	}
	/**
	 * Setzt internen Cursor auf eine beliebige Position.
	 * @param index Index der Zeile
	 */
	public void setLinePosition(int index){
		linePosition = index;
	}
	/**
	 * Setzt internen Cursor auf 0.
	 */
	public void resetLinePosition(){
		this.setLinePosition(0);
	}
}