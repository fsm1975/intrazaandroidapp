package com.technicalnorms.intraza.interfaz.datosBD;

/**
 * 
 * @author JLZS
 * 
 * Define la tabla "rutero" en la BD
 *
 *
 */

public class TablaRutero 
{
	public static final String NOMBRE_TABLA = "rutero";
	
	public static final String KEY_CAMPO_CODIGO_ARTICULO = "codigo_art";
	public static final int POS_KEY_CAMPO_CODIGO_ARTICULO = 0;
	public static final String KEY_CAMPO_ID_CLIENTE = "id_cliente";
	public static final int POS_KEY_CAMPO_ID_CLIENTE = 1;
	public static final String CAMPO_FECHA_ULTIMA_COMPRA = "fecha_ultima_compra";
	public static final int POS_CAMPO_FECHA_ULTIMA_COMPRA = 2;
	public static final String CAMPO_UNIDADES_ULTIMA_COMPRA = "unidades_ultima_compra";
	public static final int POS_CAMPO_UNIDADES_ULTIMA_COMPRA = 3;
	public static final String CAMPO_CANTIDAD_ULTIMA_COMPRA = "cantidad_ultima_compra";
	public static final int POS_CAMPO_CANTIDAD_ULTIMA_COMPRA = 4;
	public static final String CAMPO_UNIDADES_TOTAL_ANIO = "unidades_total_anio";
	public static final int POS_CAMPO_UNIDADES_TOTAL_ANIO = 5;
	public static final String CAMPO_CANTIDAD_TOTAL_ANIO = "cantidad_total_anio";
	public static final int POS_CAMPO_CANTIDAD_TOTAL_ANIO = 6;
	public static final String CAMPO_TARIFA_ULTIMA_COMPRA = "tarifa_ultima_compra";
	public static final int POS_CAMPO_TARIFA_ULTIMA_COMPRA = 7;
	public static final String CAMPO_TARIFA_CLIENTE = "tarifa_cliente";
	public static final int POS_CAMPO_TARIFA_CLIENTE = 8;
	public static final String CAMPO_OBSERVACIONES = "observaciones";
	public static final int POS_CAMPO_OBSERVACIONES = 9;
	public static final String CAMPO_STATUS = "status";
	public static final int POS_CAMPO_STATUS = 10;
	public static final int NUM_CAMPOS = 11;

	public static final String SQL_CREA_TABLA = "create table "+NOMBRE_TABLA+" \n"+
												 "( \n"+
												 KEY_CAMPO_CODIGO_ARTICULO+" NVARCHAR(10) NOT NULL, \n"+
												 KEY_CAMPO_ID_CLIENTE+" INTEGER NOT NULL, \n"+
												 CAMPO_FECHA_ULTIMA_COMPRA+" TEXT NOT NULL, \n"+
												 CAMPO_UNIDADES_ULTIMA_COMPRA+" INTEGER NOT NULL NOT NULL, \n"+
												 CAMPO_CANTIDAD_ULTIMA_COMPRA+" REAL NOT NULL NOT NULL, \n"+
												 CAMPO_UNIDADES_TOTAL_ANIO+" INTEGER NOT NULL NOT NULL, \n"+
												 CAMPO_CANTIDAD_TOTAL_ANIO+" REAL NOT NULL NOT NULL, \n"+
												 CAMPO_TARIFA_ULTIMA_COMPRA+" REAL NOT NULL, \n"+
												 CAMPO_TARIFA_CLIENTE+" REAL NOT NULL, \n"+
												 CAMPO_OBSERVACIONES+" TEXT NOT NULL, \n"+
												 CAMPO_STATUS+" INTEGER NOT NULL, \n"+
												 "PRIMARY KEY ("+KEY_CAMPO_CODIGO_ARTICULO+", "+KEY_CAMPO_ID_CLIENTE+"), \n"+
												 "FOREIGN KEY("+KEY_CAMPO_CODIGO_ARTICULO+") REFERENCES "+TablaArticulo.NOMBRE_TABLA+"("+TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO+"), \n"+
												 "FOREIGN KEY("+KEY_CAMPO_ID_CLIENTE+") REFERENCES "+TablaCliente.NOMBRE_TABLA+"("+TablaCliente.KEY_CAMPO_ID_CLIENTE+") \n"+
												 ");";
	
	public static final String SQL_BORRA_TABLA = "drop table "+NOMBRE_TABLA+";";
}
