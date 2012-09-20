package com.technicalnorms.intraza.interfaz.datosBD;

/**
 * 
 * @author JLZS
 * 
 * Define la tabla "cliente" en la BD
 *
 *
 */

public class TablaCliente 
{
	public static final String NOMBRE_TABLA = "cliente";
	
	public static final String KEY_CAMPO_ID_CLIENTE = "id_cliente";
	public static final int POS_KEY_CAMPO_ID_CLIENTE = 0;
	public static final String CAMPO_NOMBRE_CLIENTE = "nombre_cliente";
	public static final int POS_CAMPO_NOMBRE_CLIENTE = 1;
	public static final String CAMPO_OBSERVACIONES_PREPEDIDO = "observaciones_defecto";
	public static final int POS_CAMPO_OBSERVACIONES_PREPEDIDO = 2;
	public static final int NUM_CAMPOS = 3;

	public static final String SQL_CREA_TABLA = "create table "+NOMBRE_TABLA+" \n"+
												 "( \n"+
												 KEY_CAMPO_ID_CLIENTE+" INTEGER PRIMARY KEY NOT NULL, \n"+
												 CAMPO_NOMBRE_CLIENTE+" NVARCHAR(50) NOT NULL, \n"+
												 CAMPO_OBSERVACIONES_PREPEDIDO+" TEXT NULL \n"+
												 ");";
	
	public static final String SQL_BORRA_TABLA = "drop table "+NOMBRE_TABLA+";";
}
