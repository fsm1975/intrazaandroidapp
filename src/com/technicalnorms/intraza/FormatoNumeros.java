package com.technicalnorms.intraza;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * 
 * @author JLZS
 *
 * Clase que defina los formatos para la presentacion de los numeros en las pantallas de la tablet
 * 
 */
public class FormatoNumeros 
{
	private static final char SIMBOLO_DECIMAL = '.';
	
	private static final String FORMATO_2_DECIMALES = "####.##";
	private static final String FORMATO_3_DECIMALES = "####.###";
	
	public static DecimalFormat dameFormato2Decimales()
	{
		DecimalFormatSymbols simbolosDecimales = new DecimalFormatSymbols();
		simbolosDecimales.setDecimalSeparator(SIMBOLO_DECIMAL);
		
		return new DecimalFormat(FORMATO_2_DECIMALES, simbolosDecimales);
	}
	
	public static DecimalFormat dameFormato3Decimales()
	{
		DecimalFormatSymbols simbolosDecimales = new DecimalFormatSymbols();
		simbolosDecimales.setDecimalSeparator(SIMBOLO_DECIMAL);
		
		return new DecimalFormat(FORMATO_3_DECIMALES, simbolosDecimales);
	}
}
