package com.technicalnorms.intraza.interfaz.datosBD;

/**
 * 
 * @author JLZS
 * 
 * Define la tabla "articulo" en la BD
 *
 *
 */

public class TablaArticulo 
{
	public static final String NOMBRE_TABLA = "articulo";
	
	public static final String KEY_CAMPO_CODIGO_ARTICULO = "codigo_art";
	public static final int POS_KEY_CAMPO_CODIGO = 0;
	public static final String CAMPO_NOMBRE = "nombre_art";
	public static final int POS_CAMPO_NOMBRE = 1;
	public static final String CAMPO_ES_KG = "es_kg";
	public static final int POS_CAMPO_ES_KG = 2;
	public static final String CAMPO_ES_CONGELADO = "es_congelado";
	public static final int POS_CAMPO_ES_CONGELADO = 3;
	public static final String CAMPO_TARIFA_DEFECTO = "tarifa_defecto";
	public static final int POS_CAMPO_TARIFA_DEFECTO = 4;
	public static final String CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO = "fecha_cambio_tarifa_defecto";
	public static final int POS_CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO = 5;
	public static final int NUM_CAMPOS = 6;

	public static final String SQL_CREA_TABLA = "create table "+NOMBRE_TABLA+" \n"+
												 "( \n"+
												 KEY_CAMPO_CODIGO_ARTICULO+" NVARCHAR(10) PRIMARY KEY NOT NULL, \n"+
												 CAMPO_NOMBRE+" NVARCHAR(50) NOT NULL, \n"+
												 CAMPO_ES_KG+" BOOLEAN DEFAULT TRUE NOT NULL, \n"+
												 CAMPO_ES_CONGELADO+" BOOLEAN DEFAULT FALSE NOT NULL, \n"+
												 CAMPO_TARIFA_DEFECTO+" REAL NOT NULL, \n"+
												 CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO+" TEXT NULL \n"+
												 ");";
	
	public static final String SQL_BORRA_TABLA = "drop table "+NOMBRE_TABLA+";";
}
