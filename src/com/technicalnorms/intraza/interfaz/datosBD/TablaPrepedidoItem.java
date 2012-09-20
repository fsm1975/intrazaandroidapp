package com.technicalnorms.intraza.interfaz.datosBD;

/**
 * 
 * @author JLZS
 * 
 * Define la tabla "prepedido item" en la BD
 *
 *
 */

public class TablaPrepedidoItem 
{
	public static final String NOMBRE_TABLA = "prepedido_item";
	
	public static final String KEY_CAMPO_ID_PREPEDIDO = "id_prepedido";
	public static final int POS_CAMPO_ID_PREPEDIDO = 0;
	public static final String KEY_CAMPO_CODIGO_ARTICULO = "codigo_art";
	public static final int POS_CAMPO_CODIGO_ARTICULO = 1;
	public static final String CAMPO_CANTIDAD = "cantidad";
	public static final int POS_CAMPO_CANTIDAD = 2;
	public static final String CAMPO_PRECIO = "precio";
	public static final int POS_CAMPO_PRECIO = 3;
	public static final String CAMPO_OBSERVACIONES = "observaciones";
	public static final int POS_CAMPO_OBSERVACIONES = 4;
	public static final String CAMPO_FIJAR_PRECIO = "fijar_precio";
	public static final int POS_CAMPO_FIJAR_PRECIO = 5;
	public static final String CAMPO_FIJAR_ARTICULO = "fijar_articulo";
	public static final int POS_CAMPO_FIJAR_ARTICULO = 6;
	public static final String CAMPO_FIJAR_OBSERVACIONES = "fijar_observaciones";
	public static final int POS_CAMPO_FIJAR_OBSERVACIONES = 7;
	public static final int MAX_POS = 7;
	public static final int NUM_CAMPOS = 8;

	public static final String SQL_CREA_TABLA = "create table "+NOMBRE_TABLA+" \n"+
												 "( \n"+												 
												 KEY_CAMPO_ID_PREPEDIDO+" INTEGER NOT NULL, \n"+
												 KEY_CAMPO_CODIGO_ARTICULO+" NVARCHAR(10) NOT NULL, \n"+
												 CAMPO_CANTIDAD+" REAL NOT NULL NOT NULL, \n"+
												 CAMPO_PRECIO+" REAL NOT NULL, \n"+
												 CAMPO_OBSERVACIONES+" TEXT NULL, \n"+
												 CAMPO_FIJAR_PRECIO+" BOOLEAN DEFAULT FALSE, \n"+
												 CAMPO_FIJAR_ARTICULO+" BOOLEAN DEFAULT TRUE, \n"+
												 CAMPO_FIJAR_OBSERVACIONES+" BOOLEAN DEFAULT FALSE, \n"+
												 "PRIMARY KEY ("+KEY_CAMPO_ID_PREPEDIDO+", "+KEY_CAMPO_CODIGO_ARTICULO+"), \n"+
												 "FOREIGN KEY("+KEY_CAMPO_ID_PREPEDIDO+") REFERENCES "+TablaPrepedido.NOMBRE_TABLA+"("+TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO+"), \n"+
												 "FOREIGN KEY("+KEY_CAMPO_CODIGO_ARTICULO+") REFERENCES "+TablaArticulo.NOMBRE_TABLA+"("+TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO+") \n"+
												 ");";
	
	public static final String SQL_BORRA_TABLA = "drop table "+NOMBRE_TABLA+";";
}
