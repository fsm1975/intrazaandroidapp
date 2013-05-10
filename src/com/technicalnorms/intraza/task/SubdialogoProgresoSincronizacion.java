package com.technicalnorms.intraza.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.technicalnorms.intraza.Configuracion;
import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class SubdialogoProgresoSincronizacion extends AsyncTask<Void, Void, Void> 
{
	private int maximoValorBarraProgreso = 100;
	private ProgressDialog dialog = null;
	private Context contexto = null;
	
	//Almacena los registros totales a sincronizar en cada tabla, se usa para incrementar la barra de progreso
	private int totalRegistrosArticulo = 0;
	private int totalRegistrosCliente = 0;
	private int totalRegistrosRutero = 0;
	private int totalRegistrosObservacion = 0;
	
	//Indica los pesos de cada tabla segun el tamaño del registro, se usa para incrementar la barra de progreso
	private final int PESO_REGISTRO_ARTICULO = 1;
	private final int PESO_REGISTRO_CLIENTE = 1;
	private final int PESO_REGISTRO_RUTERO = 3;
	private final int PESO_REGISTRO_OBSERVACION = 1;
	
	//Indica si se ha producido un error durante el proceso de sincronizacion
	private boolean hayErrorSincronizacion = false;
	
	private boolean esSincronizacion3G = false;
	
	public SubdialogoProgresoSincronizacion(Context contexto, boolean usar3G) 
	{
		this.contexto = contexto;
		this.esSincronizacion3G = usar3G;
		
		this.dialog = new ProgressDialog(contexto);
    }
	
	protected void onPreExecute() 
	{
		this.dialog.setMessage("Sincronizando datos ...");
		this.dialog.setCancelable(false);
		this.dialog.setCanceledOnTouchOutside(false);
		this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		this.dialog.setProgress(0);
		this.dialog.setMax(this.maximoValorBarraProgreso);
		
		this.dialog.show();
	}
	
	protected Void doInBackground(final Void ... args) 
	{
		int pesoIncrementoTablaArticulo = 0;
		int pesoIncrementoTablaCliente = 0;
		int pesoIncrementoTablaRutero = 0;
		int pesoIncrementoTablaObservacion = 0;
		int pesoTotalTablas = 0;
		int incrementoTablaArticulo = 0;
		int incrementoTablaCliente = 0;
		int incrementoTablaRutero = 0;
		int incrementoTablaObservacion = 0;
		
		try 
		{
			//Obtenemos los totales de los registro que hay que sincronizar, para actualizar la barra de progreso con el procentaje sincronizado
			registrosTotalesParaSincronizar();
			
			pesoIncrementoTablaArticulo = this.totalRegistrosArticulo * PESO_REGISTRO_ARTICULO;
			pesoIncrementoTablaCliente = this.totalRegistrosCliente * PESO_REGISTRO_CLIENTE;
			pesoIncrementoTablaRutero = this.totalRegistrosRutero * PESO_REGISTRO_RUTERO;
			pesoIncrementoTablaObservacion = this.totalRegistrosObservacion * PESO_REGISTRO_OBSERVACION;
			
			pesoTotalTablas = pesoIncrementoTablaArticulo + pesoIncrementoTablaCliente + pesoIncrementoTablaRutero + pesoIncrementoTablaObservacion;
			
			incrementoTablaArticulo = (pesoIncrementoTablaArticulo * 100) / pesoTotalTablas;
			incrementoTablaCliente = (pesoIncrementoTablaCliente * 100) / pesoTotalTablas;
			incrementoTablaRutero = (pesoIncrementoTablaRutero * 100) / pesoTotalTablas;
			
			//Para asegurarnos que llegamos al 100% ya que se pierden los decimales al ser division entera
			incrementoTablaObservacion = 100 - (incrementoTablaArticulo + incrementoTablaCliente + incrementoTablaRutero);
			
			//Hacemos la sincronizacion
			sincronizaArticulos(incrementoTablaArticulo);
			sincronizaClientes(incrementoTablaCliente);
			
			//Al sincronizar por 3G hay problema pues a los 5 minutos movistart corta la conexion, por lo que hay que usar WS mas ligeros
			if (esSincronizacion3G)
			{
				sincronizaRuterosDatos(incrementoTablaRutero);
			}
			else
			{
				sincronizaRuterosTotal(incrementoTablaRutero);
			}
			sincronizaObservaciones(incrementoTablaObservacion);
			
			//Hemos finalizado la sincronizacion
			//this.dialog.setMessage("Datos sincronizados.");
		} 
		catch (Exception e) 
		{
			this.hayErrorSincronizacion = true;
			
			Log.d("Sincronizacion", "TRAZA - Excepcion ("+e.getMessage()+")");
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(final Void success) 
	{
		Configuracion.ponFechaActualComoUltimaFechaSincronizacion(this.contexto);
		
		if (this.hayErrorSincronizacion)
		{
			alertaMensajeError();
		}
		else
		{
			alertaMensajeSincronizacionCorrecta();
		}
		
		if (this.dialog!=null)
		{
			this.dialog.dismiss();
		}		
	}
	
	private void alertaMensajeError()
	{
		AlertDialog.Builder popup=new AlertDialog.Builder(this.contexto);
		popup.setTitle(Constantes.TITULO_ALERT_ERROR_SINCRONIZAR);
		popup.setMessage(Constantes.MENSAJE_ALERT_ERROR_SINCRONIZAR);
		popup.setPositiveButton("ACEPTAR", null);
		popup.show();
	}
	
	private void alertaMensajeSincronizacionCorrecta()
	{
		AlertDialog.Builder popup=new AlertDialog.Builder(this.contexto);
		popup.setTitle(Constantes.TITULO_ALERT_SINCRONIZACION_CORRECTA);
		popup.setMessage(Constantes.MENSAJE_ALERT_SINCRONIZACION_CORRECTA);
		popup.setPositiveButton("ACEPTAR", null);
		popup.show();
	}
	
	/**
	 * Obtenemos los registros totales a sincronizar de articulos, clientes, ruteros y observaciones.
	 * 
	 * @throws Exception
	 */
	private void registrosTotalesParaSincronizar() throws Exception
	{
		JSONObject jsonTotales = null;
		
		if (esSincronizacion3G)
		{
			jsonTotales = new JSONObject(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionTotalRegistros3G(this.contexto)));
		}
		else
		{
			jsonTotales = new JSONObject(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionTotalRegistrosWIFI(this.contexto)));
		}
		
		//Chequeamos que la ejecucion fue correcta
		if (jsonTotales.getInt("totalArticulos")==-1)
		{
			throw new Exception("Se ha producido una excepcion en la tarea de sincronizacion al recuperar los registros totales a sincronizar.");
		}
		
		//El resultado lo almacenamos en variables globales
		this.totalRegistrosArticulo = jsonTotales.getInt("totalArticulos");
		this.totalRegistrosCliente = jsonTotales.getInt("totalClientes");
		this.totalRegistrosRutero = jsonTotales.getInt("totalRuteros");
		this.totalRegistrosObservacion = jsonTotales.getInt("totalObservaciones");
	}
	
	/**
	 * Sincroniza los datos de articulos de la BD de intraza con los de la tablet:
	 * 1 - Obtiene los datos de intraza invocando a un WebService REST que devuelve un JSON.
	 * 2 - Borra todos los registros de articulos de la BD de tablet
	 * 3 - Procesa el JSON obtenido almacenando los datos de los articulos en la BD de la tablet.
	 * 
	 * @param incremento total de la tabla en la barra de progreso
	 * @throws Exception
	 */
	private void sincronizaArticulos(int incrementoTabla) throws Exception
	{
		AdaptadorBD db = new AdaptadorBD(this.contexto);
		float incrementoActual = 0;
		int incrementoTotal = 0;
		JSONArray jsonArrayArticulos = null;
		
		db.abrir();
		
		//1 - Obtenemos los datos de los articulos de intraza
		if (esSincronizacion3G)
		{
			jsonArrayArticulos = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionArticulo3G(this.contexto)));
		}
		else
		{
			jsonArrayArticulos = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionArticuloWIFI(this.contexto)));
		}
		
		if (jsonArrayArticulos.length()>0)
		{
			//2 - Borramos los articulos anteriores que tenemos en la BD de la tablet
			db.borrarTodosLosArticulos();
			
			//3 - Procesamos el JSON para obtener los datos de cada articulo y guardarlos en la BD de la tablet
			incrementoActual = (float)incrementoTabla/(float)jsonArrayArticulos.length();
			for (int i=0; i<jsonArrayArticulos.length(); i++)
			{
				JSONObject jsonArticulo = jsonArrayArticulos.getJSONObject(i);
			
				db.insertarArticulo(jsonArticulo.getString("id"), jsonArticulo.getString("name"), jsonArticulo.getBoolean("esKg"), jsonArticulo.getBoolean("esCongelado"), (float)jsonArticulo.getDouble("precioDefecto"), jsonArticulo.getString("fechaCambioPrecioDefecto"));

				if ((new Float(incrementoActual)).intValue()<1)
				{
					incrementoActual += (float)incrementoTabla/(float)jsonArrayArticulos.length();
				}
				else
				{
					//Para segurarnos que no incrementamos de mas
					if (incrementoTotal+(new Float(incrementoActual)).intValue() <= incrementoTabla)
					{
						this.dialog.incrementProgressBy((new Float(incrementoActual)).intValue());
						incrementoTotal += (new Float(incrementoActual)).intValue();
					}
					incrementoActual = (float)incrementoTabla/(float)jsonArrayArticulos.length();
				}
			}
			
			//Para asegurarnos que llegamos al incremento asignado a la tabla
			if ((incrementoTabla - incrementoTotal) > 0)
			{
				this.dialog.incrementProgressBy(incrementoTabla - incrementoTotal);
			}
		}
		else
		{
			throw new Exception("Se ha producido una excepcion en la tarea de sincronizacion al recuperar los articulos.");
		}
		
		db.cerrar();
	}
	
	/**
	 * Sincroniza los datos de clientes de la BD de intraza con los de la tablet:
	 * 1 - Obtiene los datos de intraza invocando a un WebService REST que devuelve un JSON.
	 * 2 - Borra todos los registros de clientes de la BD de tablet
	 * 3 - Procesa el JSON obtenido almacenando los datos de los clientes en la BD de la tablet.
	 * 
	 * @param incremento total de la tabla en la barra de progreso
	 * @throws Exception
	 */
	private void sincronizaClientes(int incrementoTabla) throws Exception
	{
		AdaptadorBD db = new AdaptadorBD(this.contexto);
		float incrementoActual = 0;
		int incrementoTotal = 0;
		JSONArray jsonArrayClientes = null;
		
		db.abrir();
		
		//1 - Obtenemos los datos de los clientes de intraza
		if (esSincronizacion3G)
		{
			jsonArrayClientes = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionCliente3G(this.contexto)));
		}
		else
		{
			jsonArrayClientes = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionClienteWIFI(this.contexto)));
		}
		
		if (jsonArrayClientes.length()>0)
		{
			//2 - Borramos los clientes anteriores que tenemos en la BD de la tablet
			db.borrarTodosLosClientes();
			
			//3 - Procesamos el JSON para obtener los datos de cada cliente y guardarlos en la BD de la tablet
			incrementoActual = (float)incrementoTabla/(float)jsonArrayClientes.length();
			for (int i=0; i<jsonArrayClientes.length(); i++)
			{
				JSONObject jsonCliente = jsonArrayClientes.getJSONObject(i);
			
				db.insertarCliente(jsonCliente.getInt("idCliente"), jsonCliente.getString("nombreCliente"), jsonCliente.getString("pedidoObs"), jsonCliente.getString("telefono"));
				
				if ((new Float(incrementoActual)).intValue()<1)
				{
					incrementoActual += (float)incrementoTabla/(float)jsonArrayClientes.length();
				}
				else
				{
					//Para segurarnos que no incrementamos de mas
					if (incrementoTotal+(new Float(incrementoActual)).intValue() <= incrementoTabla)
					{
						this.dialog.incrementProgressBy((new Float(incrementoActual)).intValue());
						incrementoTotal += (new Float(incrementoActual)).intValue();
					}
					incrementoActual = (float)incrementoTabla/(float)jsonArrayClientes.length();
				}
			}
			
			//Para asegurarnos que llegamos al incremento asignado a la tabla
			if ((incrementoTabla - incrementoTotal) > 0)
			{
				this.dialog.incrementProgressBy(incrementoTabla - incrementoTotal);
			}
		}
		else
		{
			throw new Exception("Se ha producido una excepcion en la tarea de sincronizacion al recuperar los clientes.");
		}
		
		db.cerrar();
	}
	
    /**
     * Sincroniza los datos de ruteros de la BD de intraza con los de la tablet:
     * 1 - Obtiene los datos de intraza invocando a un WebService REST que devuelve un JSON.
     * 2 - Borra todos los registros de ruteros de la BD de tablet
     * 3 - Procesa el JSON obtenido almacenando los datos de los ruteros en la BD de la tablet.
     *
     * @param incremento total de la tabla en la barra de progreso
     * @throws Exception
     */
    private void sincronizaRuterosTotal(int incrementoTabla) throws Exception
    {
            AdaptadorBD db = new AdaptadorBD(this.contexto);
            float incrementoActual = 0;
            int incrementoTotal = 0;
           
            db.abrir();
           
            //1 - Obtenemos los datos de los ruteros de intraza
            JSONArray jsonArrayRuteros = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionRuteroTotalWIFI(this.contexto)));
           
            if (jsonArrayRuteros.length()>0)
            {
                    //2 - Borramos los ruteros anteriores que tenemos en la BD de la tablet
                    db.borrarTodosLosRuteros();
                   
                    //3 - Procesamos el JSON para obtener los datos de cada rutero y guardarlos en la BD de la tablet
                    incrementoActual = (float)incrementoTabla/(float)jsonArrayRuteros.length();
                    for (int i=0; i<jsonArrayRuteros.length(); i++)
                    {
                            JSONObject jsonRutero = jsonArrayRuteros.getJSONObject(i);
                   
                            db.insertarRutero(jsonRutero.getString("codigoArticulo"), jsonRutero.getInt("idCliente"),
                                                              jsonRutero.getString("fechaPedido"), jsonRutero.getInt("unidades"), jsonRutero.getDouble("peso"), jsonRutero.getInt("unidadesTotalAnio"), jsonRutero.getDouble("pesoTotalAnio"),
                                                              jsonRutero.getDouble("precio"), jsonRutero.getDouble("precioCliente"), jsonRutero.getString("observacionesItem"), jsonRutero.getInt("status"));
                           
                            if ((new Float(incrementoActual)).intValue()<1)
                            {
                                    incrementoActual += (float)incrementoTabla/(float)jsonArrayRuteros.length();
                            }
                            else
                            {
                                    //Para segurarnos que no incrementamos de mas
                                    if (incrementoTotal+(new Float(incrementoActual)).intValue() <= incrementoTabla)
                                    {
                                            this.dialog.incrementProgressBy((new Float(incrementoActual)).intValue());
                                            incrementoTotal += (new Float(incrementoActual)).intValue();
                                    }
                                    incrementoActual = (float)incrementoTabla/(float)jsonArrayRuteros.length();
                            }
                    }
                   
                    //Para asegurarnos que llegamos al incremento asignado a la tabla
                    if ((incrementoTabla - incrementoTotal) > 0)
                    {
                            this.dialog.incrementProgressBy(incrementoTabla - incrementoTotal);
                    }
            }
            else
            {
                    throw new Exception("Se ha producido una excepcion en la tarea de sincronizacion al recuperar los ruteros.");
            }
           
            db.cerrar();
    }

	
	/**
	 * Sincroniza los datos de ruteros de la BD de intraza con los de la tablet:
	 * 1 - Obtiene los datos de intraza invocando a un WebService REST que devuelve un JSON.
	 * 2 - Borra todos los registros de ruteros de la BD de tablet
	 * 3 - Procesa el JSON obtenido almacenando los datos de los ruteros en la BD de la tablet.
	 * 
	 * @param incremento total de la tabla en la barra de progreso
	 * @throws Exception
	 */
	private void sincronizaRuteros(int incrementoTabla) throws Exception
	{
		AdaptadorBD db = new AdaptadorBD(this.contexto);
		float incrementoActual = 0;
		int incrementoTotal = 0;
		double precioCliente = 0;
		int unidadesTotalAnio = 0;
		double pesoTotalAnio = 0;
		
		Log.d("Sincronizacion", "TRAZA - Sincronizacion para 3G");
		
		db.abrir();
		
		//1 - Obtenemos los datos de los ruteros de intraza
		JSONArray jsonArrayRuteros = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionRutero3G(this.contexto)));
		
		if (jsonArrayRuteros.length()>0)
		{
			//2 - Borramos los ruteros anteriores que tenemos en la BD de la tablet
			db.borrarTodosLosRuteros();
			
			//3 - Procesamos el JSON para obtener los datos de cada rutero y guardarlos en la BD de la tablet
			incrementoActual = (float)incrementoTabla/(float)jsonArrayRuteros.length();
			for (int i=0; i<jsonArrayRuteros.length(); i++)
			{
				JSONObject jsonRutero = jsonArrayRuteros.getJSONObject(i);
				
				//*** OBTENEMOS LA TARIFA CLIENTE DE LA LINEA DE RUTERO
				JSONObject jsonResultadoTC = new JSONObject(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionRuteroTarifaCliente3G(this.contexto, jsonRutero.getInt("idCliente"), jsonRutero.getString("codigoArticulo"))));
				
				//Si no hay tarifa cliente tenemos que obtener la tarifa defecto
				if (jsonResultadoTC.getInt("codigoError") == 0 && jsonResultadoTC.getDouble("dato")!=-1)
				{
					precioCliente = jsonResultadoTC.getDouble("dato");
				}	
				else
				{
					JSONObject jsonResultadoTD = new JSONObject(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionRuteroTarifaDefecto3G(this.contexto, jsonRutero.getString("codigoArticulo"))));
					
					//Si no hay tarifa cliente tenemos que obtener la tarifa defecto
					if (jsonResultadoTD.getInt("codigoError") == 0 && jsonResultadoTD.getDouble("dato")!=-1)
					{
						precioCliente = jsonResultadoTD.getDouble("dato");
					}	
					else
					{
						precioCliente = 0;
						
						Log.d("Sincronizacion", "TRAZA - Tarifa para rutero 0. idCliente ("+jsonRutero.getInt("idCliente")+") codigoArticulo ("+jsonRutero.getString("codigoArticulo")+")");
					}
				}
				
				//*** OBTENEMOS EL PESO TOTAL ANUAL DE LA LINEA DE RUTERO
				JSONObject jsonResultadoPTA = new JSONObject(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionRuteroPesoTotalAnio3G(this.contexto, jsonRutero.getInt("idCliente"), jsonRutero.getString("codigoArticulo"))));
				
				if (jsonResultadoPTA.getInt("codigoError") == 0)
				{
					pesoTotalAnio = jsonResultadoTC.getDouble("dato");
					unidadesTotalAnio = jsonResultadoTC.getInt("dato2");
				}	
				else
				{
					pesoTotalAnio = 0;
					unidadesTotalAnio = 0;
					
					Log.e("Sincronizacion", "TRAZA - Error al obtener el peso total al anio. idCliente ("+jsonRutero.getInt("idCliente")+") codigoArticulo ("+jsonRutero.getString("codigoArticulo")+") error ("+jsonResultadoPTA.getString("descripcionError")+")");
				}
			
				db.insertarRutero(jsonRutero.getString("codigoArticulo"), jsonRutero.getInt("idCliente"),
								  jsonRutero.getString("fechaPedido"), jsonRutero.getInt("unidades"), jsonRutero.getDouble("peso"), unidadesTotalAnio, pesoTotalAnio,
								  jsonRutero.getDouble("precio"), precioCliente, jsonRutero.getString("observacionesItem"), jsonRutero.getInt("status"));
				
				if ((new Float(incrementoActual)).intValue()<1)
				{
					incrementoActual += (float)incrementoTabla/(float)jsonArrayRuteros.length();
				}
				else
				{
					//Para segurarnos que no incrementamos de mas
					if (incrementoTotal+(new Float(incrementoActual)).intValue() <= incrementoTabla)
					{
						this.dialog.incrementProgressBy((new Float(incrementoActual)).intValue());
						incrementoTotal += (new Float(incrementoActual)).intValue();
					}
					incrementoActual = (float)incrementoTabla/(float)jsonArrayRuteros.length();
				}
			}
			
			//Para asegurarnos que llegamos al incremento asignado a la tabla
			if ((incrementoTabla - incrementoTotal) > 0)
			{
				this.dialog.incrementProgressBy(incrementoTabla - incrementoTotal);
			}
		}
		else
		{
			throw new Exception("Se ha producido una excepcion en la tarea de sincronizacion al recuperar los ruteros.");
		}
		
		db.cerrar();
	}
	
	/**
	 * Sincroniza los datos de ruteros de la BD de intraza con los de la tablet:
	 * 1 - Obtiene los datos de intraza invocando a un WebService REST que devuelve un JSON.
	 * 2 - Borra todos los registros de ruteros de la BD de tablet
	 * 3 - Procesa el JSON obtenido almacenando los datos de los ruteros en la BD de la tablet.
	 * 
	 * @param incremento total de la tabla en la barra de progreso
	 * @throws Exception
	 */
	private void sincronizaRuterosDatos(int incrementoTabla) throws Exception
	{
		AdaptadorBD db = new AdaptadorBD(this.contexto);
		float incrementoActual = 0;
		int incrementoTotal = 0;
		double precioCliente = 0;
		int unidadesTotalAnio = 0;
		double pesoTotalAnio = 0;
		
		Log.d("Sincronizacion", "TRAZA - Sincronizacion para 3G");
		
		db.abrir();
		
		//1 - Obtenemos los datos de los ruteros de intraza
		JSONArray jsonArrayRuteros = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionRutero3G(this.contexto)));
		
		if (jsonArrayRuteros.length()>0)
		{
			//2 - Borramos los ruteros anteriores que tenemos en la BD de la tablet
			db.borrarTodosLosRuteros();
			
			//3 - Procesamos el JSON para obtener los datos de cada rutero y guardarlos en la BD de la tablet
			incrementoActual = (float)incrementoTabla/(float)jsonArrayRuteros.length();
			for (int i=0; i<jsonArrayRuteros.length(); i++)
			{
				JSONObject jsonRutero = jsonArrayRuteros.getJSONObject(i);
				
				//*** OBTENEMOS LOS DATOS DE LA LINEA DE RUTERO
				JSONObject jsonResultadoDR = new JSONObject(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionRuteroDatos3G(this.contexto, jsonRutero.getInt("idCliente"), jsonRutero.getString("codigoArticulo"))));
				
				if (jsonResultadoDR.getInt("codigoError") == 0)
				{
					precioCliente = jsonResultadoDR.getDouble("tarifaCliente");
					pesoTotalAnio = jsonResultadoDR.getDouble("pesoTotalAnio");
					unidadesTotalAnio = jsonResultadoDR.getInt("unidadesTotalAnio");
				}	
				else
				{
					throw new Exception("Error al obtener datos de rutero para idCliente ("+jsonRutero.getInt("idCliente")+") codigoArticulo ("+jsonRutero.getString("codigoArticulo")+"): ("+jsonResultadoDR.getInt("descripcionError")+")");
				}
			
				db.insertarRutero(jsonRutero.getString("codigoArticulo"), jsonRutero.getInt("idCliente"),
								  jsonRutero.getString("fechaPedido"), jsonRutero.getInt("unidades"), jsonRutero.getDouble("peso"), unidadesTotalAnio, pesoTotalAnio,
								  jsonRutero.getDouble("precio"), precioCliente, jsonRutero.getString("observacionesItem"), jsonRutero.getInt("status"));
				
				if ((new Float(incrementoActual)).intValue()<1)
				{
					incrementoActual += (float)incrementoTabla/(float)jsonArrayRuteros.length();
				}
				else
				{
					//Para segurarnos que no incrementamos de mas
					if (incrementoTotal+(new Float(incrementoActual)).intValue() <= incrementoTabla)
					{
						this.dialog.incrementProgressBy((new Float(incrementoActual)).intValue());
						incrementoTotal += (new Float(incrementoActual)).intValue();
					}
					incrementoActual = (float)incrementoTabla/(float)jsonArrayRuteros.length();
				}
			}
			
			//Para asegurarnos que llegamos al incremento asignado a la tabla
			if ((incrementoTabla - incrementoTotal) > 0)
			{
				this.dialog.incrementProgressBy(incrementoTabla - incrementoTotal);
			}
		}
		else
		{
			throw new Exception("Se ha producido una excepcion en la tarea de sincronizacion al recuperar los ruteros.");
		}
		
		db.cerrar();
	}
	
	/**
	 * Sincroniza los datos de observaciones de la BD de intraza con los de la tablet:
	 * 1 - Obtiene los datos de intraza invocando a un WebService REST que devuelve un JSON.
	 * 2 - Borra todos los registros de observaciones de la BD de tablet
	 * 3 - Procesa el JSON obtenido almacenando los datos de las observaciones en la BD de la tablet.
	 * 
	 * @param incremento total de la tabla en la barra de progreso
	 * @throws Exception
	 */
	private void sincronizaObservaciones(int incrementoTabla) throws Exception
	{
		AdaptadorBD db = new AdaptadorBD(this.contexto);
		float incrementoActual = 0;
		int incrementoTotal = 0;
		JSONArray jsonArrayObservaciones = null;
		
		db.abrir();
		
		//1 - Obtenemos los datos de las observaciones de intraza
		if (esSincronizacion3G)
		{
			jsonArrayObservaciones = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionObservacion3G(this.contexto)));
		}
		else
		{
			jsonArrayObservaciones = new JSONArray(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionObservacionWIFI(this.contexto)));
		}
		
		if (jsonArrayObservaciones.length()>0)
		{
			//2 - Borramos las observaciones anteriores que tenemos en la BD de la tablet
			db.borrarTodasLasObservaciones();
			
			//3 - Procesamos el JSON para obtener los datos de cada observacion y guardarlos en la BD de la tablet
			incrementoActual = (float)incrementoTabla/(float)jsonArrayObservaciones.length();
			for (int i=0; i<jsonArrayObservaciones.length(); i++)
			{
				JSONObject jsonObservacion= jsonArrayObservaciones.getJSONObject(i);
			
				db.insertarObservacion(jsonObservacion.getInt("id"), jsonObservacion.getInt("tipo"), jsonObservacion.getString("descripcion"));
				
				if ((new Float(incrementoActual)).intValue()<1)
				{
					incrementoActual += (float)incrementoTabla/(float)jsonArrayObservaciones.length();
				}
				else
				{
					//Para segurarnos que no incrementamos de mas
					if (incrementoTotal+(new Float(incrementoActual)).intValue() <= incrementoTabla)
					{
						this.dialog.incrementProgressBy((new Float(incrementoActual)).intValue());
						incrementoTotal += (new Float(incrementoActual)).intValue();
					}
					incrementoActual = (float)incrementoTabla/(float)jsonArrayObservaciones.length();
				}
			}
			
			//Para asegurarnos que llegamos al incremento asignado a la tabla
			if ((incrementoTabla - incrementoTotal) > 0)
			{
				this.dialog.incrementProgressBy(incrementoTabla - incrementoTotal);
			}
		}
		else
		{
			Log.d("Sincronizacion", "TRAZA - No hay observaciones que sincronizar");
		}
		
		db.cerrar();
	}	
	
	/**
	 * Invoca un WebService REST.
	 * 
	 * @param la URL de invocacion al Web Service
	 * @return Una cadena con el resultado de la invocacion al Web Service
	 */
	private String invocaWebServiceHttp(int segundosTimeout, String urlWebServiceRest) throws Exception
	{  
		Log.d("Sincronizacion", "TRAZA - URL ("+urlWebServiceRest+") timeout ("+segundosTimeout+")");
		//HttpParams params = null;
		HttpClient httpclient = null;  
		HttpGet request = null;  
		String result = "";
		BufferedReader rd = null;
		BasicHttpResponse response = null;
        
		try 
		{  
			//params = new BasicHttpParams();
			//HttpConnectionParams.setConnectionTimeout(params, segundosTimeout * 1000);
			//HttpConnectionParams.setSoTimeout(params, segundosTimeout * 1000);
			//httpclient = new DefaultHttpClient(params);
			httpclient = new DefaultHttpClient();
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), segundosTimeout * 1000);
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), segundosTimeout * 1000); 
			
			request = new HttpGet(urlWebServiceRest); 			
			request.addHeader("accept", "application/json");
			
			response = (BasicHttpResponse)httpclient.execute(request); 
            
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) 
			{
				result += line;
			}
		} 
		catch (Exception e) 
		{  
			result = e.getMessage();
			e.printStackTrace();
			
			throw e;
		} 
		finally
		{
			rd.close();
			httpclient.getConnectionManager().shutdown();
		}
		
		Log.d("Sincronizacion", "TRAZA - Resultado WS ("+result+")");
        
		return result;
	}
	
	/**
	 * Invoca un WebService REST.
	 * 
	 * @param la URL de invocacion al Web Service
	 * @return Una cadena con el resultado de la invocacion al Web Service
	 */
	private String invocaWebServiceHttps(int segundosTimeout, String urlWebServiceRest) throws Exception
	{  
		String result = "";
		 
		try 
		{
			Log.d("Sincronizacion", "TRAZA - URL ("+urlWebServiceRest+") segundo timeout ("+segundosTimeout+")");
			
			X509TrustManager tm = new X509TrustManager()
			{
				@Override
				public X509Certificate[] getAcceptedIssuers() 
				{
					return null;
				}

				@Override
				public void checkServerTrusted (X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException 
				{
				}

				@Override
				public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException 
				{
				}
			};
			
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { tm }, null);
			URL url = new URL(urlWebServiceRest);
				
			String encoding = MyBase64.encode(Configuracion.dameUsuarioWS(this.contexto)+":"+Configuracion.damePasswordWS(this.contexto));
			
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			
			connection.setConnectTimeout(segundosTimeout*1000);
			connection.setReadTimeout(segundosTimeout*1000);

			connection.setSSLSocketFactory(ctx.getSocketFactory());
			//	connection.setRequestMethod("POST");
			//connection.setDoOutput(true);
			connection.setHostnameVerifier(new HostnameVerifier()
				{
					
						@Override
						public boolean verify(String paramString, SSLSession paramSSLSession) 
						{
							return true;
						}
					}
			);
					
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content));
				
			String line;
			while ((line = in.readLine()) != null) 
			{
				result+=line;
			}
		} 
		catch (Exception e) 
		{
			throw e;
		}
		
		return result;
	}			 
}