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
	
	public SubdialogoProgresoSincronizacion(Context contexto) 
	{
		this.contexto = contexto;
		
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
			sincronizaRuteros(incrementoTablaRutero);
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
		JSONObject jsonTotales = new JSONObject(invocaWebService(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionTotalRegistros(this.contexto)));
		
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
		
		db.abrir();
		
		//1 - Obtenemos los datos de los articulos de intraza
		JSONArray jsonArrayArticulos = new JSONArray(invocaWebService(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionArticulo(this.contexto)));
		
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
		
		db.abrir();
		
		//1 - Obtenemos los datos de los clientes de intraza
		JSONArray jsonArrayClientes = new JSONArray(invocaWebService(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionCliente(this.contexto)));
		
		if (jsonArrayClientes.length()>0)
		{
			//2 - Borramos los clientes anteriores que tenemos en la BD de la tablet
			db.borrarTodosLosClientes();
			
			//3 - Procesamos el JSON para obtener los datos de cada cliente y guardarlos en la BD de la tablet
			incrementoActual = (float)incrementoTabla/(float)jsonArrayClientes.length();
			for (int i=0; i<jsonArrayClientes.length(); i++)
			{
				JSONObject jsonCliente = jsonArrayClientes.getJSONObject(i);
			
				db.insertarCliente(jsonCliente.getInt("idCliente"), jsonCliente.getString("nombreCliente"), jsonCliente.getString("pedidoObs"));
				
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
	private void sincronizaRuteros(int incrementoTabla) throws Exception
	{
		AdaptadorBD db = new AdaptadorBD(this.contexto);
		float incrementoActual = 0;
		int incrementoTotal = 0;
		
		db.abrir();
		
		//1 - Obtenemos los datos de los ruteros de intraza
		JSONArray jsonArrayRuteros = new JSONArray(invocaWebService(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionRutero(this.contexto)));
		
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
								  jsonRutero.getString("fechaPedido"), jsonRutero.getDouble("peso"), jsonRutero.getDouble("pesoTotalAnio"),
								  jsonRutero.getDouble("precio"), jsonRutero.getDouble("precioCliente"), jsonRutero.getString("observacionesItem"));
				
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
		
		db.abrir();
		
		//1 - Obtenemos los datos de las observaciones de intraza
		JSONArray jsonArrayObservaciones = new JSONArray(invocaWebService(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesSincronizacionObservacion(this.contexto)));
		
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
			throw new Exception("Se ha producido una excepcion en la tarea de sincronizacion al recuperar las observaciones.");
		}
		
		db.cerrar();
	}	
	
	/**
	 * Invoca un WebService REST.
	 * 
	 * @param la URL de invocacion al Web Service
	 * @return Una cadena con el resultado de la invocacion al Web Service
	 */
	private String invocaWebService(int segundosTimeout, String urlWebServiceRest) throws Exception
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

			connection.setSSLSocketFactory(ctx.getSocketFactory());
			//	connection.setRequestMethod("POST");
			connection.setDoOutput(true);
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