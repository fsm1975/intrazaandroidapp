package com.technicalnorms.intraza.interfaz.datosBD;

/**
 * 
 * @author JLZS
 * 
 * Define la tabla "configuracion" en la BD
 *
 *
 */

public class TablaConfiguracion 
{
	public static final String NOMBRE_TABLA = "configuracion";
	
	public static final String KEY_CAMPO_NOMBRE_PARAMETRO = "nombre_parametro";
	public static final int POS_KEY_CAMPO_NOMBRE_PARAMETRO = 0;
	public static final String CAMPO_VALOR = "valor";
	public static final int POS_CAMPO_VALOR = 1;
	public static final String CAMPO_DESCRIPCION = "descripcion";
	public static final int POS_CAMPO_DESCRIPCION = 2;
	public static final int NUM_CAMPOS = 3;

	public static final String SQL_CREA_TABLA = "create table "+NOMBRE_TABLA+" \n"+
												 "( \n"+
												 KEY_CAMPO_NOMBRE_PARAMETRO+" TEXT PRIMARY KEY NOT NULL, \n"+
												 CAMPO_VALOR+" TEXT NOT NULL, \n"+
												 CAMPO_DESCRIPCION+" TEXT NOT NULL \n"+
												 ");";
	
	public static final String SQL_BORRA_TABLA = "drop table "+NOMBRE_TABLA+";";
}
