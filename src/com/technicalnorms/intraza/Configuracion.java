package com.technicalnorms.intraza;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;

import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaConfiguracion;

/**
 * Clase utilizada para recuperar los parametros de configuracion, de un fichero
 * 
 * @author JLZS
 *
 */
public class Configuracion
{
	//Valores para la tabla configuracion de la BD
	
	//Parametro ULTIMA_FECHA_SINCRONIZACION
	public static final String NOMBRE_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION = "ULTIMA_FECHA_SINCRONIZACION";
	public static final String VALOR_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION = "--/--/-- --:--:--";
	public static final String DESCRIPCION_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION = "Fecha de la ultima sincronizacion con InTraza";
	public static final boolean ES_EDITABLE_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION = false;
	
	//Parametro TIMEOUT_WEB_SERVICES_SINCRONIZACION
	public static final String NOMBRE_PARAMETRO_TIMEOUT_WEB_SERVICES_SINCRONIZACION = "TIMEOUT_WEB_SERVICES_SINCRONIZACION";
	public static final String VALOR_PARAMETRO_TIMEOUT_WEB_SERVICE_SINCRONIZACION = "1800";
	public static final String DESCRIPCION_PARAMETRO_TIMEOUT_WEB_SERVICE_SINCRONIZACION = "Timeout en segundos, para la conexion con los Web Service de sincronizacion y envio de pedidos a InTraza";
	public static final boolean ES_EDITABLE_PARAMETRO_TIMEOUT_WEB_SERVICE_SINCRONIZACION = true;
	
	//Parametro URI_WEB_SERVICES_SINCRONIZACION_3G
	public static final String NOMBRE_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_3G = "URI_WEB_SERVICES_SINCRONIZACION_3G";
	public static final String VALOR_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_3G = "https://213.0.78.111:8069/InTrazaWeb/rest/sincroniza";
	public static final String DESCRIPCION_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_3G = "URI de solicitud de los Web Service para la sincronizacion y envio de pedidos a InTraza a traves de 3G";
	public static final boolean ES_EDITABLE_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_3G= true;
	
	//Parametro URI_WEB_SERVICES_SINCRONIZACION_WIFI
	public static final String NOMBRE_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_WIFI = "URI_WEB_SERVICES_SINCRONIZACION_WIFI";
	public static final String VALOR_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_WIFI = "https://10.14.1.190:8069/InTrazaWeb/rest/sincroniza";
	public static final String DESCRIPCION_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_WIFI = "URI de solicitud de los Web Service para la sincronizacion a InTraza a traves de WIFI";
	public static final boolean ES_EDITABLE_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_WIFI= true;
	
	//Parametro PERMITIR_PRECIO_0
	public static final String NOMBRE_PARAMETRO_PERMITIR_PRECIO_0 = "PERMITIR_PRECIO_0";
	public static final String VALOR_PARAMETRO_PERMITIR_PRECIO_0 = "SI";
	public static final String DESCRIPCION_PARAMETRO_PERMITIR_PRECIO_0 = "Puede tomar los valores SI o NO, indica si esta permitido crear lineas de pedido con precio 0";
	public static final boolean ES_EDITABLE_PARAMETRO_PERMITIR_PRECIO_0 = true;
	
	//Parametro NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO
	public static final String NOMBRE_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO = "NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO";
	public static final String VALOR_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO = "15";
	public static final String DESCRIPCION_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO = "Indica el numero de dias, para que se considere que una tarifa es reciente. Segun el valor de este parametro, se pondra una marca en la tarifa general en el rutero, para informar al comercial";
	public static final boolean ES_EDITABLE_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO = true;
	
	//Parametro USUARIO_WS
	public static final String NOMBRE_PARAMETRO_USUARIO_WS = "USUARIO_WS";
	public static final String VALOR_PARAMETRO_USUARIO_WS = "intraza";
	public static final String DESCRIPCION_PARAMETRO_USUARIO_WS = "Usuario para la validacion SSL de los WS REST";
	public static final boolean ES_EDITABLE_PARAMETRO_USUARIO_WS = true;
	
	//Parametro PASSWORD_WS
	public static final String NOMBRE_PARAMETRO_PASSWORD_WS = "PASSWORD_WS";
	public static final String VALOR_PARAMETRO_PASSWORD_WS = "intrazaviper";
	public static final String DESCRIPCION_PARAMETRO_PASSWORD_WS = "Password para la validacion SSL de los WS REST";
	public static final boolean ES_EDITABLE_PARAMETRO_PASSWORD_WS = true;
	
	//Parametro PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES
	public static final String NOMBRE_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES = "PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES";
	public static final String VALOR_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES = "NO";
	public static final String DESCRIPCION_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES = "Indica si se permite la sintronizacion con InTraza, cuando hay pedidos pendientes de enviar.";
	public static final boolean ES_EDITABLE_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES = true;
	
	//Para invocar a los WS de sincronizacion con InTraza
	public static final String NOMBRE_WS_REST_TOTALES = "/totales";
	public static final String NOMBRE_WS_REST_ARTICULO = "/articulos";
	public static final String NOMBRE_WS_REST_CLIENTE = "/clientes";
	public static final String NOMBRE_WS_REST_RUTERO_TOTAL = "/ruteros_total";
	public static final String NOMBRE_WS_REST_RUTERO = "/ruteros";
	public static final String NOMBRE_WS_REST_RUTERO_TARIFA_CLIENTE = "/rutero_tarifa_cliente";
	public static final String NOMBRE_WS_REST_RUTERO_TARIFA_DEFECTO = "/rutero_tarifa_defecto";
	public static final String NOMBRE_WS_REST_RUTERO_PESO_TOTAL_ANIO = "/rutero_peso_total_anio";
	public static final String NOMBRE_WS_REST_RUTERO_DATOS = "/rutero_datos";
	
	public static final String NOMBRE_WS_REST_OBSERVACION = "/observaciones";
	
	//Para invocar a los WS de envio de informacion a InTraza
	public static final String NOMBRE_WS_REST_PREPEDIDO = "/prepedido";
	
	/**
	 * Devuelve la ultima fecha en que se sincronizo la BD de la tablet con la BD de InTraza.
	 * 
	 * @param context
	 * @return String con la fecha de sincronización en formato DD/MM/YYYY hh:mm:ss
	 */
	public static String dameUltimaFechaSincronizacion(Context context)
	{
		String fecha = VALOR_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION;
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			fecha = cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR);
		}		
		
		db.cerrar();
		
		return fecha;
	}
	
	/**
	 * Pone la fecha actual (en formato DD/MM/YYYY hh:mm:ss) como la ultima fecha de sincronizacion
	 * 
	 * @param context
	 */
	public static void ponFechaActualComoUltimaFechaSincronizacion(Context context)
	{
		AdaptadorBD db = new AdaptadorBD(context);
		
		db.abrir();
		
		db.actualizarParametroConfiguracion(NOMBRE_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), DESCRIPCION_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION, ES_EDITABLE_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION);
		
		db.cerrar();
		
		return;
	}
	
	/**
	 * Devuelve el timeout en segundos de los Web Service REST de sincronizacion con InTraza
	 * 
	 * @param context
	 * @return el timeout en segundos
	 */
	public static int dameTimeoutWebServices(Context context)
	{
		int timeout = 30;
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_TIMEOUT_WEB_SERVICES_SINCRONIZACION);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			timeout = Integer.parseInt(cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR));
		}		
		
		db.cerrar();
		
		return timeout;
	}
	
	public static String dameUriWebServicesSincronizacion3G(Context context)
	{
		String uri = VALOR_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_3G;
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_3G);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			uri = cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR);
		}		
		
		db.cerrar();
		
		return uri;
	}
	
	public static String dameUriWebServicesSincronizacionWIFI(Context context)
	{
		String uri = VALOR_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_WIFI;
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION_WIFI);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			uri = cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR);
		}		
		
		db.cerrar();
		
		return uri;
	}
	
	public static String dameUriWebServicesSincronizacionTotalRegistros3G(Context context)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_TOTALES;
	}
	
	public static String dameUriWebServicesSincronizacionTotalRegistrosWIFI(Context context)
	{
		return dameUriWebServicesSincronizacionWIFI(context)+NOMBRE_WS_REST_TOTALES;
	}
	
	public static String dameUriWebServicesSincronizacionArticulo3G(Context context)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_ARTICULO;
	}
	
	public static String dameUriWebServicesSincronizacionArticuloWIFI(Context context)
	{
		return dameUriWebServicesSincronizacionWIFI(context)+NOMBRE_WS_REST_ARTICULO;
	}
	
	public static String dameUriWebServicesSincronizacionCliente3G(Context context)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_CLIENTE;
	}
	
	public static String dameUriWebServicesSincronizacionClienteWIFI(Context context)
	{
		return dameUriWebServicesSincronizacionWIFI(context)+NOMBRE_WS_REST_CLIENTE;
	}
	
	public static String dameUriWebServicesSincronizacionRuteroTotalWIFI(Context context)
	{
		return dameUriWebServicesSincronizacionWIFI(context)+NOMBRE_WS_REST_RUTERO_TOTAL;
	}
	
	public static String dameUriWebServicesSincronizacionRutero3G(Context context)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_RUTERO;
	}
	
	public static String dameUriWebServicesSincronizacionRuteroTarifaCliente3G(Context context, int idCliente, String codigoArticulo)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_RUTERO_TARIFA_CLIENTE+"?idCliente="+idCliente+"&codigoArticulo="+codigoArticulo;
	}
	
	public static String dameUriWebServicesSincronizacionRuteroTarifaDefecto3G(Context context, String codigoArticulo)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_RUTERO_TARIFA_DEFECTO+"?codigoArticulo="+codigoArticulo;
	}
	
	public static String dameUriWebServicesSincronizacionRuteroPesoTotalAnio3G(Context context, int idCliente, String codigoArticulo)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_RUTERO_PESO_TOTAL_ANIO+"?idCliente="+idCliente+"&codigoArticulo="+codigoArticulo;
	}
	
	public static String dameUriWebServicesSincronizacionRuteroDatos3G(Context context, int idCliente, String codigoArticulo)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_RUTERO_DATOS+"?idCliente="+idCliente+"&codigoArticulo="+codigoArticulo;
	}
	
	public static String dameUriWebServicesSincronizacionObservacion3G(Context context)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_OBSERVACION;
	}
	
	public static String dameUriWebServicesSincronizacionObservacionWIFI(Context context)
	{
		return dameUriWebServicesSincronizacionWIFI(context)+NOMBRE_WS_REST_OBSERVACION;
	}
	
	public static String dameUriWebServicesEnvioPrepedido3G(Context context)
	{
		return dameUriWebServicesSincronizacion3G(context)+NOMBRE_WS_REST_PREPEDIDO;
	}
	
	public static String dameUriWebServicesEnvioPrepedidoWIFI(Context context)
	{
		return dameUriWebServicesSincronizacionWIFI(context)+NOMBRE_WS_REST_PREPEDIDO;
	}
	
	/**
	 * Devuelve si esta permitido o no crear una linea de pedido con precio 0
	 * 
	 * @param context
	 * @return true si esta permitido o false en caso contrario
	 */
	public static boolean estaPermitidoLineasPedidoConPrecio0(Context context)
	{
		boolean estaPermitido = false;
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_PERMITIR_PRECIO_0);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			estaPermitido = cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR).toUpperCase().equals("SI");
		}		
		
		db.cerrar();
		
		return estaPermitido;
	}
	
	/**
	 * Devuelve si esta permitido o no crear una linea de pedido con precio 0
	 * 
	 * @param context
	 * @return true si esta permitido o false en caso contrario
	 */
	public static int dameNumDiasAntiguedadMarcaTarifaDefecto(Context context)
	{
		int numDias = 0;
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			numDias = Integer.parseInt(cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR));
		}		
		
		db.cerrar();
		
		return numDias;
	}
	
	/**
	 * Devuelve el usuario para la validacion con lo WS Rest
	 * 
	 * @param context
	 * @return el usuario o null sino se puede recuperar
	 */
	public static String dameUsuarioWS(Context context)
	{
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		String usuario = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_USUARIO_WS);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			usuario = cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR);
		}		
		
		db.cerrar();
		
		return usuario;
	}
	
	/**
	 * Devuelve el password para la validacion con lo WS Rest
	 * 
	 * @param context
	 * @return el usuario o null sino se puede recuperar
	 */
	public static String damePasswordWS(Context context)
	{
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		String password = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_PASSWORD_WS);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			password = cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR);
		}		
		
		db.cerrar();
		
		return password;
	}
	
	/**
	 * Devuelve si esta permitido o no sincronizar con intraza cuando hay pedidos pendientes de enviar
	 * 
	 * @param context
	 * @return true si esta permitido o false en caso contrario
	 */
	public static boolean estaPermitidoSincronizacionConPedidosPendientes(Context context)
	{
		boolean estaPermitido = false;
		AdaptadorBD db = new AdaptadorBD(context);
		Cursor cursorParametro = null;
		
		db.abrir();
		
		cursorParametro = db.obtenerParametroConfiguracion(NOMBRE_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES);	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametro.moveToFirst())
		{	
			estaPermitido = cursorParametro.getString(TablaConfiguracion.POS_CAMPO_VALOR).toUpperCase().equals("SI");
		}		
		
		db.cerrar();
		
		return estaPermitido;
	}
	
}
