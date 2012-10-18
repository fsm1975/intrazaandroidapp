package com.technicalnorms.intraza.interfaz;

import com.technicalnorms.intraza.Configuracion;
import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.interfaz.datos.DatosConsultaPedidos;
import com.technicalnorms.intraza.interfaz.datos.DatosDialogoMensaje;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.LineaPedidoBD;
import com.technicalnorms.intraza.interfaz.datosBD.PedidoBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaArticulo;
import com.technicalnorms.intraza.interfaz.datosBD.TablaCliente;
import com.technicalnorms.intraza.interfaz.datosBD.TablaPrepedido;
import com.technicalnorms.intraza.interfaz.datosBD.TablaPrepedidoItem;
import com.technicalnorms.intraza.task.JsonLineaPedido;
import com.technicalnorms.intraza.task.JsonPedido;
import com.technicalnorms.intraza.task.MyBase64;
import com.technicalnorms.intraza.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

public class PantallaListaPedidos extends Activity
{
	//Codigos de los subdialogos que se usan en la Activity
	private static final int DIALOGO_MENSAJE_SIN_PEDIDOS_CONSULTA = 0;
	private static final int DIALOGO_CONFIRMACION_BORRAR_PEDIDOS = 1;
	private static final int DIALOGO_CONFIRMACION_ENVIAR_PEDIDOS = 2;
	private static final int DIALOGO_MODIFICAR_PEDIDO = 3;
	private static final int DIALOGO_PIDE_DATOS_CONSULTA_PEDIDOS = 4;
	
	//Almacena los datos de la consulta de los pedidos a presentar en pantalla
	DatosConsultaPedidos datosConsultaPedidos = null;
	
	//Almacena los pedidos obtenidos en la consulta a la BD
	Vector<PedidoBD> pedidosBD = null;
	
	//Contendra todos los TextView que forman la tabla de pedidos del cliente y los datos de la linea de pedido, la key sera el ID de la View
	private Hashtable<Integer, Object> viewsTablaPedidos = null;
	
	//El widget que forma la tabla de pedidos en pantalla
	private TableLayout tablaPedidos = null;
	private boolean colorFilaClaro = true;
	
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_pedidos);
		
		//Inicializamos las variables miembro necesarias
		this.viewsTablaPedidos = new Hashtable<Integer, Object>();
		this.tablaPedidos = (TableLayout)findViewById(R.id.pedidosTableP);
		
		//Obtenemos los datos de la consulta
		this.datosConsultaPedidos = this.getIntent().getParcelableExtra("DATOS_CONSULTA_PEDIDOS");
		this.pedidosBD = consultaPedidosBD(this.datosConsultaPedidos);
		
		//Si no hemos obtenido ningun pedido en la consulta, informamos al usuario y le devolvemos a la pantalla principal.
		if (this.pedidosBD.size()==0)
		{
			subdialogoMensajeSinPedidos();
		}
		else
		{			
			//Mostramos la lista de pedidos en la pantalla
			cargaPedidosEnPantalla();
			
			//Definimos los eventos onClick de los botones
			((Button)findViewById(R.id.botonNuevaConsultaPLP)).setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{	
					habilitaClickEnActivity(false);
					
					subdialogoDatosConsultaPedidos();
				}
			});
			
			//Definimos los eventos onClick de los botones
			((Button)findViewById(R.id.botonEnviarPLP)).setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{	
					habilitaClickEnActivity(false);
					
					subdialogoEnviarPedidos();
				}
			});
			
			//Definimos los eventos onClick de los botones
			((Button)findViewById(R.id.botonBorrarPLP)).setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{	
					habilitaClickEnActivity(false);
					
					subdialogoBorrarPedidos();
				}
			});
			
			//Si hay id pedido en la consulta es que solo hay una linea de pedido a mostrar, por lo que hacemos invisibles los checkBox de la cabecera
			if (datosConsultaPedidos.hayDatoIdPedido())
			{
				((CheckBox)findViewById(R.id.checkColumnaEnviar)).setVisibility(View.INVISIBLE);
				((CheckBox)findViewById(R.id.checkColumnaSuprimir)).setVisibility(View.INVISIBLE);
			}
			else
			{
				//Definimos los eventos de los CheckBox de la cabecera de la tabla
				((CheckBox)findViewById(R.id.checkColumnaEnviar)).setOnCheckedChangeListener(
						new CheckBox.OnCheckedChangeListener() 
						{
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
							{
								if (isChecked) 
								{
									//Desmarcamos el check de la cabecera de la tabla de borrar
									((CheckBox)findViewById(R.id.checkColumnaSuprimir)).setChecked(false);
								
									if (viewsTablaPedidos.size()!=0)
									{
										//En todos los pedidos chequeamos el envio y deschequeamos el borrado por si estubiera marcado
										for (int i=1; i<=viewsTablaPedidos.size()/Constantes.COLUMNAS_TOTALES_P; i++)
										{
											setCheckBoxEnviar((CheckBox)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_PENDIENTE_ENVIAR_P)), true);
										}
									}
								}
								else
								{
									if (viewsTablaPedidos.size()!=0)
									{
										//Deschequeamos los check de envio en todos los pedidos
										for (int i=1; i<=viewsTablaPedidos.size()/Constantes.COLUMNAS_TOTALES_P; i++)
										{
											setCheckBoxEnviar((CheckBox)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_PENDIENTE_ENVIAR_P)), false);
										}
									}
								}
							}
						});
			
				((CheckBox)findViewById(R.id.checkColumnaSuprimir)).setOnCheckedChangeListener(
						new CheckBox.OnCheckedChangeListener() 
						{
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
							{
								if (isChecked) 
								{
									//Desmarcamos el check de la cabecera de la tabla de envio
									((CheckBox)findViewById(R.id.checkColumnaEnviar)).setChecked(false);
									
									if (viewsTablaPedidos.size()!=0)
									{
										//En todos los pedidos chequeamos el borrado y deschequeamos el envio por si estubiera marcado
										for (int i=1; i<=viewsTablaPedidos.size()/Constantes.COLUMNAS_TOTALES_P; i++)
										{
											setCheckBoxBorrar((CheckBox)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_SUPRIMIR_P)), true);
										}
									}
								}
								else
								{
									if (viewsTablaPedidos.size()!=0)
									{
										//Deschequeamos los check de envio en todos los pedidos
										for (int i=1; i<=viewsTablaPedidos.size()/Constantes.COLUMNAS_TOTALES_P; i++)
										{
											setCheckBoxBorrar((CheckBox)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_SUPRIMIR_P)), false);
										}
									}
								}
							}
						});
			}
		}
	}
	
	/**
	 * Deshabilita o no, todo los eventos onClick de la activity para evitar ejecutar dos click seguidos
	 * @param deshabilita
	 */
	private void habilitaClickEnActivity(boolean habilita)
	{
		((Button)findViewById(R.id.botonNuevaConsultaPLP)).setClickable(habilita);
		((Button)findViewById(R.id.botonEnviarPLP)).setClickable(habilita);
		((Button)findViewById(R.id.botonBorrarPLP)).setClickable(habilita);
		
		for (int i=1; i<=viewsTablaPedidos.size()/Constantes.COLUMNAS_TOTALES_P; i++)
		{
			((TextView)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_ID_PEDIDO_P))).setClickable(habilita);
		}
	}
	
	/**
	 * Cambia el checkBox enviar en la view del pedido y actualiza el valor en el pedido
	 * 
	 * @param checkBox
	 * @param valor para la propiedad checked checkBox
	 */
	private void setCheckBoxEnviar(CheckBox checkBox, boolean checked)
	{
		checkBox.setChecked(checked);
		
		dameDatosPedido(checkBox).setCheckBoxEnviar(checked);
	}
	
	/**
	 * Cambia el checkBox borrar en la view del pedido y actualiza el valor en el pedido
	 * 
	 * @param checkBox
	 * @param valor para la propiedad checked checkBox
	 */
	private void setCheckBoxBorrar(CheckBox checkBox, boolean checked)
	{
		checkBox.setChecked(checked);
		dameDatosPedido(checkBox).setCheckBoxBorrar(checked);
	}
	
	/**
	 * Consultamos en la BD de la tablet los pedidos
	 * 
	 * @param Datos de la consulta
	 * @return un Vector con los datos de los pedidos consultados
	 */
	private Vector<PedidoBD> consultaPedidosBD(DatosConsultaPedidos dcp)
	{
		Vector<PedidoBD> pedidosConsultados = new Vector<PedidoBD>();
		Vector<LineaPedidoBD> lineasPedido = null;
		PedidoBD pedido = null;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorPedidos = null;
		Cursor cursorLineasPedido = null;
		Cursor cursorArticulo = null;
		//Datos para pedido
		int idPrepedidoP = 0;
		int idClienteP = 0;
		String clienteP = null;
		int diaFechaPedidoP = 0;
		int mesFechaPedidoP = 0;
		int anioFechaPedidoP = 0;
		int diaFechaEntregaP = 0;
		int mesFechaEntregaP = 0;
		int anioFechaEntregaP = 0;
		String observacionesP = null;
		boolean fijarObservacionesP = false;
		//Datos para la linea de pedido
		int idPrepedidoLP = 0;
		String codArticuloLP = null;
		boolean esMedidaKgLP = false;
		boolean esCongeladoLP = false;
		float cantidadKgLP = -1;
		int cantidadUdLP = 0;
		float tarifaClienteLP = 0; 
		String observacionesLP = null;
		boolean fijarTarifaLP = false;
		boolean fijarArticuloLP = false;
		boolean fijarObservacionesLP = false;
		
		//Contiene los datos de una fecha descompuesta, es decir, para una fecha DD-MM-YYYY, contiene 3 elementos, 
		//en la posicion 0 tiene DD, en la posicion 1 tiene MM y en la posicion 2 tiene YYYY.
		String[] fechaDescompuesta = null;
		
		db.abrir();
		
		// Hay que obtener los datos del pedido segun su ID
		if (dcp.hayDatoIdPedido())
		{
			cursorPedidos = db.obtenerPrepedidoConDatosCliente(dcp.getIdPedido());	
		}
		else
		{
			//DEPENDIENDO SEA UNA CONSULTA DE TODOS O UN CLIENTE
			if (dcp.getCliente().equals(Constantes.SPINNER_TODO))
			{
				cursorPedidos = db.obtenerTodosLosPrepedidosConDatosCliente();
			}
			else
			{
				cursorPedidos = db.obtenerTodosLosPrepedidosDelClienteConDatosCliente(dcp.getIdCliente());
			}
		}
		
		//Si tenemos resultado de la consulta...
		if (cursorPedidos.moveToFirst())
		{	
			//Recorremos el cursor para obtener los datos de los pedidos recuperados
			do 
			{
				idPrepedidoP = cursorPedidos.getInt(TablaPrepedido.POS_KEY_CAMPO_ID_PREPEDIDO);
				idClienteP = cursorPedidos.getInt(TablaPrepedido.POS_CAMPO_ID_CLIENTE);
				clienteP = cursorPedidos.getString(TablaPrepedido.NUM_CAMPOS+TablaCliente.POS_CAMPO_NOMBRE_CLIENTE);
				fechaDescompuesta = cursorPedidos.getString(TablaPrepedido.POS_CAMPO_FECHA_PREPEDIDO).split(Constantes.SEPARADOR_FECHA);
				diaFechaPedidoP = Integer.parseInt(fechaDescompuesta[0]);
				mesFechaPedidoP = Integer.parseInt(fechaDescompuesta[1]);
				anioFechaPedidoP = Integer.parseInt(fechaDescompuesta[2]);
				fechaDescompuesta = cursorPedidos.getString(TablaPrepedido.POS_CAMPO_FECHA_ENTREGA).split(Constantes.SEPARADOR_FECHA);
				diaFechaEntregaP = Integer.parseInt(fechaDescompuesta[0]);
				mesFechaEntregaP = Integer.parseInt(fechaDescompuesta[1]);
				anioFechaEntregaP = Integer.parseInt(fechaDescompuesta[2]);
				observacionesP = cursorPedidos.getString(TablaPrepedido.POS_CAMPO_OBSERVACIONES);
				if (cursorPedidos.getInt(TablaPrepedido.POS_CAMPO_FIJAR_OBSERVACIONES) == 1)
				{
					fijarObservacionesP = true;
				}
				else
				{
					fijarObservacionesP = false;
				}
				
				//Ahora para cada pedido recuperamos sus lineas de pedido
				lineasPedido = new Vector<LineaPedidoBD>();
				cursorLineasPedido = db.obtenerTodosLosPrepedidosItemDelPrepedido(idPrepedidoP);
				
				if (cursorLineasPedido.moveToFirst())
				{	
					//Recorremos el cursor para obtener los datos de las lineas de pedido
					do 
					{
						idPrepedidoLP = cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_ID_PREPEDIDO);
						codArticuloLP = cursorLineasPedido.getString(TablaPrepedidoItem.POS_CAMPO_CODIGO_ARTICULO);
						cantidadKgLP = cursorLineasPedido.getFloat(TablaPrepedidoItem.POS_CAMPO_CANTIDAD_KG);
						cantidadUdLP = cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_CANTIDAD_UD);
						tarifaClienteLP = cursorLineasPedido.getFloat(TablaPrepedidoItem.POS_CAMPO_PRECIO);
						observacionesLP = cursorLineasPedido.getString(TablaPrepedidoItem.POS_CAMPO_OBSERVACIONES);
						if (cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_FIJAR_PRECIO) == 1)
						{
							fijarTarifaLP = true;
						}
						else
						{
							fijarTarifaLP = false;
						}
						
						if (cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_FIJAR_ARTICULO) == 1)
						{
							fijarArticuloLP = true;
						}
						else
						{
							fijarArticuloLP = false;
						}
						
						if (cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_FIJAR_OBSERVACIONES) == 1)
						{
							fijarObservacionesLP = true;
						}
						else
						{
							fijarObservacionesLP = false;
						}
						
						//Hacemos una consulta al articulo para obtener su medida por defecto
						//Por si esta clonado, le quitamos la marca
						cursorArticulo = db.obtenerArticulo(codArticuloLP.split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[0]);
						
						if (cursorArticulo.getInt(TablaArticulo.POS_CAMPO_ES_KG) == 1)
						{
							esMedidaKgLP = true;
						}
						else
						{
							esMedidaKgLP = false;
						}
						
						//Comprobamos si el articulo es congelado
						if (cursorArticulo.getInt(TablaArticulo.POS_CAMPO_ES_CONGELADO) == 1)
						{
							esCongeladoLP = true;
						}
						else
						{
							esCongeladoLP = false;
						}
						
						lineasPedido.add(new LineaPedidoBD(idPrepedidoLP, codArticuloLP, esMedidaKgLP, esCongeladoLP, cantidadKgLP, cantidadUdLP, tarifaClienteLP, observacionesLP, fijarTarifaLP, fijarArticuloLP, fijarObservacionesLP));
					} while (cursorLineasPedido.moveToNext());
				}
				
				pedido = new PedidoBD(idPrepedidoP, idClienteP, clienteP, diaFechaPedidoP, mesFechaPedidoP, anioFechaPedidoP, diaFechaEntregaP, mesFechaEntregaP, anioFechaEntregaP, observacionesP, fijarObservacionesP);
				pedido.setLineasPedido(lineasPedido);
				pedidosConsultados.add(pedido);

			} while (cursorPedidos.moveToNext());
		}		

		db.cerrar();
		
		//Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_CONSULTA_PEDIDOS_TERMINADA, Toast.LENGTH_SHORT).show();		
		
		return pedidosConsultados;
	}
	
	/**
	 * Muestra un mensaje al usuario indicando que no se ha obtenido ningun pedido como resultado de la consulta
	 */
	private void subdialogoMensajeSinPedidos()
	{
		Intent intent = null;
		DatosDialogoMensaje datosMensaje = null;
		
		datosMensaje = new DatosDialogoMensaje("com.zingenieros.intraza.interfaz.PantallaListaPedidos", Constantes.TITULO_SIN_PEDIDOS_CONSULTA, Constantes.INFORMACION_SIN_PEDIDOS_CONSULTA);
				
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoMensaje.class);
		intent.putExtra("DATOS_MENSAJE", datosMensaje);
			
		startActivityForResult(intent, DIALOGO_MENSAJE_SIN_PEDIDOS_CONSULTA);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) 
		{
			case (DIALOGO_MENSAJE_SIN_PEDIDOS_CONSULTA): 
			{
				//Cuando el usuario pulsa el boton ACEPTAR, para crear un nueva linea de pedido con un nuevo articulo
				if (resultCode == Activity.RESULT_OK) 
				{
					//Si no hay pedidos, no hay nada que mostar, finalizamos la activity, para volver a la activity principal
					finish();
				}
												
				break;
			}
			
			case (DIALOGO_CONFIRMACION_BORRAR_PEDIDOS): 
			{
				//Cuando el usuario pulsa el boton SI, para confirmar que desea borrar los pedidos
				if (resultCode == Activity.RESULT_OK) 
				{					
					//Borramos en todas las lineas de pedido y volvemos a mostrar la lista de pedidos en pantalla
					borrarPedidos(data.getExtras().getStringArray("ARRAY_ID_PEDIDOS"));
					
					Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_PEDIDOS_BORRADOS, Toast.LENGTH_SHORT).show();
					
					//Deschequeamos el checkBox de la cabecera borrar
					((CheckBox)findViewById(R.id.checkColumnaSuprimir)).setChecked(false);
				}
												
				break;
			}
			
			case (DIALOGO_CONFIRMACION_ENVIAR_PEDIDOS): 
			{
				//Cuando el usuario pulsa el boton SI, para confirmar que desea enviar los pedidos
				if (resultCode == Activity.RESULT_OK) 
				{					
					//Recuperamos los IDs de los pedidos a enviar a intraza
					String[] idPedidos = data.getExtras().getStringArray("ARRAY_ID_PEDIDOS");
					
					//Recuperamos si se quiere enviar por WIFI
					boolean usarWIFI = data.getExtras().getBoolean("USAR_WIFI", false);
					
					//Enviamos los pedidos a intraza
					TareaEnviaPrepedidos enviaPrepedidos = new TareaEnviaPrepedidos(this, idPedidos, usarWIFI);
					enviaPrepedidos.execute();
										
					//Deschequeamos el checkBox de la cabecera enviar
					((CheckBox)findViewById(R.id.checkColumnaEnviar)).setChecked(false);
					
					//Cuando finalice la tarea, se invocara al metodo "finalTareaEnviaPedidos", que borrara los pedidos enviados correctamente a intraza
				}
												
				break;
			}
			
			case (DIALOGO_MODIFICAR_PEDIDO): 
			{
				//Se debe consultar en la BD los datos de pedido, para actualizar la lista de pedidos y refrescar la pantalla
				this.pedidosBD = consultaPedidosBD(this.datosConsultaPedidos);
				
				//Si no hemos obtenido ningun pedido en la consulta, informamos al usuario y le devolvemos a la pantalla principal.
				if (this.pedidosBD.size()==0)
				{
					subdialogoMensajeSinPedidos();
				}
				else
				{
					//Mostramos la lista de pedidos en la pantalla
					cargaPedidosEnPantalla();						
				}
												
				break;
			}
			
			case (DIALOGO_PIDE_DATOS_CONSULTA_PEDIDOS): 
			{
				//Cuando el usuario pulsa el boton CONSULTAR, se inicializan los datos
				if (resultCode == Activity.RESULT_OK) 
				{
					//Obtenemos del intent los datos de la consulta
					this.datosConsultaPedidos = data.getParcelableExtra("DATOS_CONSULTA_PEDIDOS");
					this.pedidosBD = consultaPedidosBD(this.datosConsultaPedidos);
					
					//Si no hemos obtenido ningun pedido en la consulta, informamos al usuario y le devolvemos a la pantalla principal.
					if (this.pedidosBD.size()==0)
					{
						subdialogoMensajeSinPedidos();
					}
					else
					{
						//Mostramos la lista de pedidos en la pantalla
						cargaPedidosEnPantalla();						
					}
				}
												
				break;
			}
		}
		
		//Habilitamos los eventos onClick
		habilitaClickEnActivity(true);
	}
	
	/**
	 * Este metodo es ejecutado cuando termina la tarea que envio de pedidos a intraza. Se encarga de borrar de la BD de la tablet, los pedidos enviados
	 * a intraza.
	 * 
	 * @param listaPedidosEnviados
	 */
	private void finalTareaEnviaPedidos(ArrayList<String> listaPedidosEnviados)
	{
		borrarPedidos(listaPedidosEnviados.toArray(new String[listaPedidosEnviados.size()]));
	}
	
	/**
	 * Borra los pedidos y lineas de pedido de la BD y de la tabla de pedidos mostrada en pantalla
	 * 
	 * @param array con los id pedidos a borrar
	 */
	private void borrarPedidos(String[] idPedidos)
	{	
		AdaptadorBD db = new AdaptadorBD(this);
		
		db.abrir();
		
		//Borramos los pedidos de la lista de pedidos
		for (int i=0; i<idPedidos.length; i++)
		{
			//Primero borramos los datos del pedido de la BD y si va bien los borramos de la tabla mostrada en pantalla
			if (!db.borrarPrepedidoConLosPrepedidosItem(Integer.parseInt(idPedidos[i])))
			{
				toastMensajeError(Constantes.ERROR_BORRAR_DATOS_PEDIDO+idPedidos[i]+".");
			}
			else
			{
				borraPedidoSegunId(Integer.parseInt(idPedidos[i]));
			}
		}
		
		db.cerrar();
		
		//Refrescamos la pantalla
		cargaPedidosEnPantalla();
	}
	
	/**
	 * Carga en la pantalla los pedidos del cliente
	 */
	private void cargaPedidosEnPantalla()
	{	
		this.colorFilaClaro = true;
		
		//Primero borramos las filas que puediera haber previamente en la tabla y todas las views de la tabla
		this.tablaPedidos.removeAllViews();
		this.viewsTablaPedidos.clear();
		
		for (int i=0; i<this.pedidosBD.size(); i++)
		{
			insertaLineaPedidoEnTabla(this.pedidosBD.elementAt(i));
		}
		
		//Posicionamos la tabla de pedidos en pantalla, al princio del scroll
		((ScrollView)findViewById(R.id.scrollTablaP)).scrollTo(0, 0);
	}
	
	/**
	 * Inserta una fila en la pantalla en la tabla de pedidos
	 * 
	 * @param pedido
	 */
	public void insertaLineaPedidoEnTabla(PedidoBD pedido)
	{
		int colorTextoFila = dameColorTextoFila();
		int colorFila = dameColorFila();
		TableRow filaP = new TableRow(this);   
		
		filaP.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		filaP.addView(creaVistaIdPedido(String.valueOf(pedido.getIdPedido()), colorFila, this.getResources().getColor(R.color.colorTextoConClickTablaP)));
		filaP.addView(creaVistaCliente(String.valueOf(pedido.getIdPedido()), pedido.getCliente(), colorFila, colorTextoFila));
		filaP.addView(creaVistaFechaPedido(String.valueOf(pedido.getIdPedido()), pedido.getFechaPedido(), colorFila, colorTextoFila));
		filaP.addView(creaVistaFechaEntrega(String.valueOf(pedido.getIdPedido()), pedido.getFechaEntrega(), colorFila, colorTextoFila));
		filaP.addView(creaVistaPrecioTotal(String.valueOf(pedido.getIdPedido()), pedido.getPrecioTotal(), colorFila, colorTextoFila));
		filaP.addView(creaVistaEnviar(String.valueOf(pedido.getIdPedido()), pedido.getCheckBoxEnviar(), colorFila, this.getResources().getColor(R.color.colorTextoConClickTablaP)));
		filaP.addView(creaVistaSuprimir(String.valueOf(pedido.getIdPedido()), pedido.getCheckBoxBorrar(), colorFila, this.getResources().getColor(R.color.colorTextoConClickTablaP)));
		
		//Insertamos en la tabla de pedidos de la pantalla el nuevo pedido
		this.tablaPedidos.addView(filaP, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * Crea un widget para mostrar el dato IdPedido en pantalla
	 * 
	 * @param idPedido
	 * @param estaPendiente
	 * @param colorFila
	 * @param colorTextoFila
	 * @return
	 */
	private View creaVistaIdPedido(String idPedido, int colorFila, int colorTextoFila)
	{
		TextView datoIdPedido = new TextView(this);
		
		datoIdPedido.setId(dameIdViewNuevo(Constantes.COLUMNA_ID_PEDIDO_P));
		datoIdPedido.setGravity(Gravity.CENTER);
		datoIdPedido.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoIdPedido.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColIdPedido));
		datoIdPedido.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoIdPedido.setBackgroundColor(colorFila);
		datoIdPedido.setTextColor(colorTextoFila);
		datoIdPedido.setText(idPedido);
		datoIdPedido.setMaxLines(1);
		
		//Guardamos el idPedido asociado con la view
		datoIdPedido.setContentDescription(idPedido);
		
		datoIdPedido.setClickable(true);
		datoIdPedido.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v)
			{
				habilitaClickEnActivity(false);
				
				subdialogoModificarPedido(v);
			}
		});
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaPedidos.put(datoIdPedido.getId(), datoIdPedido);
		
		return datoIdPedido;
	}
	
	/**
	 * Crea un widget para mostrar los datos del cliente en pantalla
	 * 
	 * @param idPedido
	 * @param cliente
	 * @param colorFila
	 * @param colorTextoFila
	 * @return
	 */
	private View creaVistaCliente(String idPedido, String cliente, int colorFila, int colorTextoFila)
	{
		TextView datoCliente = new TextView(this);

		datoCliente.setId(dameIdViewNuevo(Constantes.COLUMNA_CLIENTE_P));
		datoCliente.setGravity(Gravity.CENTER);
		datoCliente.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoCliente.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColCliente));
		datoCliente.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoCliente.setBackgroundColor(colorFila);
		datoCliente.setTextColor(colorTextoFila);
		datoCliente.setMaxLines(1);
		//El ancho maximo es de 35 caracteres
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(35);
		datoCliente.setFilters(filterArray);
		
		//Ponemos el dato
		datoCliente.setText(cliente);
		
		//Guardamos el idPedido asociado con la view
		datoCliente.setContentDescription(idPedido);
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaPedidos.put(datoCliente.getId(), datoCliente);
		
		return datoCliente;
	}
	
	/**
	 * Crea un widget para mostrar el dato fechaPedido en pantalla
	 * 
	 * @param idPedido
	 * @param fecha
	 * @param colorFila
	 * @param colorTextoFila
	 * @return
	 */
	private View creaVistaFechaPedido(String idPedido, String fecha, int colorFila, int colorTextoFila)
	{
		TextView datoFechaPedido = new TextView(this);

		datoFechaPedido.setId(dameIdViewNuevo(Constantes.COLUMNA_FECHA_PEDIDO_P));
		datoFechaPedido.setGravity(Gravity.CENTER);
		datoFechaPedido.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoFechaPedido.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColFechaPedido));
		datoFechaPedido.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoFechaPedido.setBackgroundColor(colorFila);
		datoFechaPedido.setTextColor(colorTextoFila);
		datoFechaPedido.setText(fecha);
		datoFechaPedido.setMaxLines(1);
		
		//Guardamos el idPedido asociado con la view
		datoFechaPedido.setContentDescription(idPedido);
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaPedidos.put(datoFechaPedido.getId(), datoFechaPedido);
		
		return datoFechaPedido;
	}
	
	/**
	 * Crea un widget para mostrar el dato fechaEntrega en pantalla
	 * 
	 * @param idPedido
	 * @param fecha
	 * @param colorFila
	 * @param colorTextoFila
	 * @return
	 */
	private View creaVistaFechaEntrega(String idPedido, String fecha, int colorFila, int colorTextoFila)
	{
		TextView datoFechaEntrega = new TextView(this);

		datoFechaEntrega.setId(dameIdViewNuevo(Constantes.COLUMNA_FECHA_ENTREGA_P));
		datoFechaEntrega.setGravity(Gravity.CENTER);
		datoFechaEntrega.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoFechaEntrega.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColFechaEntrega));
		datoFechaEntrega.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoFechaEntrega.setBackgroundColor(colorFila);
		datoFechaEntrega.setTextColor(colorTextoFila);
		datoFechaEntrega.setText(fecha);
		datoFechaEntrega.setMaxLines(1);
		
		//Guardamos el idPedido asociado con la view
		datoFechaEntrega.setContentDescription(idPedido);
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaPedidos.put(datoFechaEntrega.getId(), datoFechaEntrega);
		
		return datoFechaEntrega;
	}
	
	/**
	 * Crea un widget para mostrar un boton que al pulsar muestre el dato observaciones en pantalla
	 * 
	 * @param idPedido
	 * @param precioTotal
	 * @param colorFila
	 * @param colorTextoFila
	 * @return
	 */
	private View creaVistaPrecioTotal(String idPedido, float precioTotal, int colorFila, int colorTextoFila)
	{
		TextView datoPrecioTotal = new TextView(this);
		
		datoPrecioTotal.setId(dameIdViewNuevo(Constantes.COLUMNA_PRECIO_TOTAL_P));
		datoPrecioTotal.setGravity(Gravity.CENTER);
		datoPrecioTotal.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoPrecioTotal.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColPrecioTotal));
		datoPrecioTotal.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoPrecioTotal.setBackgroundColor(colorFila);
		datoPrecioTotal.setTextColor(colorTextoFila);		
		datoPrecioTotal.setText(Constantes.formatearFloat2Decimales.format(precioTotal)+Constantes.EURO);
		datoPrecioTotal.setMaxLines(1);		
		
		//Guardamos el idPedido asociado con la view
		datoPrecioTotal.setContentDescription(idPedido);
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaPedidos.put(datoPrecioTotal.getId(), datoPrecioTotal);
		
		return datoPrecioTotal;
	}	
	
	/**
	 * Crea un widget para mostrar en pantalla que el pedido si esta pendiente de enviar a intraza
	 * 
	 * @param idPedido
	 * @param checked
	 * @param estaPendiente
	 * @param colorFila
	 * @param colorTextoFila
	 * @return
	 */
	private View creaVistaEnviar(String idPedido, boolean checked, int colorFila, int colorTextoFila)
	{
		RelativeLayout enviarRL = new RelativeLayout(this);
		RelativeLayout.LayoutParams relativeLP = null;
		CheckBox checkBoxEnviar = new CheckBox(this);
		int idView = 0;
		
		idView = dameIdViewNuevo(Constantes.COLUMNA_PENDIENTE_ENVIAR_P);
		
		enviarRL.setId(idView);
		//Se deben usar los LayoutParams del tipo de view del padre
		enviarRL.setLayoutParams(new TableRow.LayoutParams(this.getResources().getDimensionPixelSize(R.dimen.widthColPendienteEnviar), this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla)));
		enviarRL.setBackgroundColor(colorFila);
		
		//Guardamos el idPedido asociado con la view
		enviarRL.setContentDescription(idPedido);
		
		//Se ponen los parametros del check
		checkBoxEnviar.setId(idView);
		checkBoxEnviar.setGravity(Gravity.CENTER);
		checkBoxEnviar.setChecked(checked);
		
		//Guardamos el idPedido asociado con la view
		checkBoxEnviar.setContentDescription(idPedido);
		
		checkBoxEnviar.setOnCheckedChangeListener(
			new CheckBox.OnCheckedChangeListener() 
			{
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if (isChecked) 
					{
						//Para que se guarde el estado del checkBox en los datos del pedido
						setCheckBoxEnviar((CheckBox)dameViewSegunId(buttonView.getId()), true);
						
						//Deschequeamos el borrado del pedido, por si estubiera marcado
						setCheckBoxBorrar((CheckBox)dameViewSegunId(buttonView.getId()+1), false);
					}
					else
					{
						//Para que se guarde el estado del checkBox en los datos del pedido
						setCheckBoxEnviar((CheckBox)dameViewSegunId(buttonView.getId()), false);
					}
				}
			});
		
		
		//Ponemos el boton en el layout que lo contiene, se deben usar los LayoutParams del tipo de view del padre
		relativeLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		relativeLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		enviarRL.addView(checkBoxEnviar, relativeLP);
		
		//Guardamos la nueva vista el widget que nos interesa para asi poder consultarlo posteriormente
		this.viewsTablaPedidos.put(checkBoxEnviar.getId(), checkBoxEnviar);
		
		return enviarRL;
	}
	
	/**
	 * Crea un widget para crear un boton que al pulsar muestra un dialogo para confirmar el borrado del pedido
	 * 
	 * @param idPedido
	 * @param checked
	 * @param estaPendiente
	 * @param colorFila
	 * @param colorTextoFila
	 * @return
	 */
	private View creaVistaSuprimir(String idPedido, boolean checked, int colorFila, int colorTextoFila)
	{
		RelativeLayout suprimirRL = new RelativeLayout(this);
		RelativeLayout.LayoutParams relativeLP = null;
		CheckBox checkBoxSuprimir = new CheckBox(this);
		int idView = 0;
		
		idView = dameIdViewNuevo(Constantes.COLUMNA_SUPRIMIR_P);
		
		suprimirRL.setId(idView);
		//Se deben usar los LayoutParams del tipo de view del padre
		suprimirRL.setLayoutParams(new TableRow.LayoutParams(this.getResources().getDimensionPixelSize(R.dimen.widthColSuprimir), this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla)));
		suprimirRL.setBackgroundColor(colorFila);
		
		//Guardamos el idPedido asociado con la view
		suprimirRL.setContentDescription(idPedido);
		
		//Se ponen los parametros del check
		checkBoxSuprimir.setId(idView);
		checkBoxSuprimir.setGravity(Gravity.CENTER);
		checkBoxSuprimir.setChecked(checked);
		
		//Guardamos el idPedido asociado con la view
		checkBoxSuprimir.setContentDescription(idPedido);
		
		checkBoxSuprimir.setOnCheckedChangeListener(
				new CheckBox.OnCheckedChangeListener() 
				{
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						if (isChecked) 
						{
							//Para que se guarde el estado del checkBox en los datos del pedido
							setCheckBoxBorrar((CheckBox)dameViewSegunId(buttonView.getId()), true);
							
							//Deschequeamos el envio del pedido, por si estuviera marcado
							//((CheckBox)dameViewSegunId(buttonView.getId()-1)).setChecked(false);
							setCheckBoxEnviar((CheckBox)dameViewSegunId(buttonView.getId()-1), false);
						}
						else
						{
							//Para que se guarde el estado del checkBox en los datos del pedido
							setCheckBoxBorrar((CheckBox)dameViewSegunId(buttonView.getId()), false);
						}
					}
				});
		
		//Ponemos el boton en el layout que lo contiene, se deben usar los LayoutParams del tipo de view del padre
		relativeLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		relativeLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		suprimirRL.addView(checkBoxSuprimir, relativeLP);				
		
		//Guardamos la nueva vista el widget que nos interesa para asi poder consultarlo posteriormente
		this.viewsTablaPedidos.put(checkBoxSuprimir.getId(), checkBoxSuprimir);
		
		return suprimirRL;
	}
	
	/**
	 * Muestra el subdialogo que permite realizar una nueva consulta
	 */
	private void subdialogoDatosConsultaPedidos()
	{
		Intent intent = null;
		Bundle bundle = null;
		
		//Para indicar que la activity no se llamada desde la activity principal
		bundle = new Bundle();
		bundle.putBoolean("ACTIVITY_PRINCIPAL", false);		
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoDatosConsultaPedidos.class);
		intent.putExtras(bundle);
		
		startActivityForResult(intent, DIALOGO_PIDE_DATOS_CONSULTA_PEDIDOS);
	}
	
	/**
	 * Borra los pedidos seleccionados por el usuario, previa a la confirmación del usuario, que se solicita en una nueva activity
	 */
	private void subdialogoBorrarPedidos()
	{
		Intent intent = null;
		Bundle bundle = null;
		PedidoBD datosPedido = null;
		Vector<String> idPedidos = new Vector<String>();
		Vector<String> clientePedidos = new Vector<String>();
		String[] arrayIdPedidos = null;
		String[] arrayClientePedidos = null;
		
		//Recorremos los checkBox de borrado, para obtener los datos de los pedidos a borrar y los guardamos en 2 vectores uno para el idPedido
		//y otra para el cliente
		for (int i=1; i<=viewsTablaPedidos.size()/Constantes.COLUMNAS_TOTALES_P; i++)
		{
			if (((CheckBox)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_SUPRIMIR_P))).isChecked())
			{
				//Localizamos los datos del pedido, si no hay, no invocamos el subdialogo
				datosPedido = dameDatosPedido((View)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_SUPRIMIR_P)));
				
				if (datosPedido != null)
				{
					idPedidos.addElement(String.valueOf(datosPedido.getIdPedido()));
					clientePedidos.addElement(datosPedido.getCliente());
				}
			}
		}
		
		//Si no tenemos ningun pedido que borrar no invocamos a la activity
		if (idPedidos.size() != 0)
		{
			//Formamos los array a pasar en el bundle
			arrayIdPedidos = new String[idPedidos.size()];
			arrayClientePedidos = new String[clientePedidos.size()];
			
			for (int i=0; i<idPedidos.size(); i++)
			{
				arrayIdPedidos[i] = idPedidos.elementAt(i);
				arrayClientePedidos[i] = clientePedidos.elementAt(i);
			}
			
			//Pasamos en el Intent los vectores como 2 array
			bundle = new Bundle();
			bundle.putStringArray("ARRAY_ID_PEDIDOS", arrayIdPedidos);
			bundle.putStringArray("ARRAY_CLIENTE_PEDIDOS", arrayClientePedidos);
		
			intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoConfirmacionBorrarPedidos.class);
			intent.putExtras(bundle);
		
			startActivityForResult(intent, DIALOGO_CONFIRMACION_BORRAR_PEDIDOS);
		}
		else
		{
			Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_NINGUN_PEDIDO_SELECCIONADO, Toast.LENGTH_SHORT).show();
			
			habilitaClickEnActivity(true);
		}
	}
	
	/**
	 * Envia los pedidos seleccionados por el usuario, previa a la confirmación del usuario, que se solicita en una nueva activity
	 */
	private void subdialogoEnviarPedidos()
	{
		Intent intent = null;
		Bundle bundle = null;
		PedidoBD datosPedido = null;
		Vector<String> idPedidos = new Vector<String>();
		Vector<String> clientePedidos = new Vector<String>();
		String[] arrayIdPedidos = null;
		String[] arrayClientePedidos = null;
		
		//Recorremos los checkBox de Envio, para obtener los datos de los pedidos a enviar y los guardamos en 2 vectores uno para el idPedido
		//y otra para el cliente
		for (int i=1; i<=viewsTablaPedidos.size()/Constantes.COLUMNAS_TOTALES_P; i++)
		{
			if (((CheckBox)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_PENDIENTE_ENVIAR_P))).isChecked())
			{
				//Localizamos los datos del pedido, si no hay, no invocamos el subdialogo
				datosPedido = dameDatosPedido((View)viewsTablaPedidos.get(calculaIdView(i, Constantes.COLUMNA_PENDIENTE_ENVIAR_P)));
				
				if (datosPedido != null)
				{
					idPedidos.addElement(String.valueOf(datosPedido.getIdPedido()));
					clientePedidos.addElement(datosPedido.getCliente());
				}
			}
		}
		
		//Si no tenemos ningun pedido que enviar no invocamos a la activity
		if (idPedidos.size() != 0)
		{
			//Formamos los array a pasar en el bundle
			arrayIdPedidos = new String[idPedidos.size()];
			arrayClientePedidos = new String[clientePedidos.size()];
			
			for (int i=0; i<idPedidos.size(); i++)
			{
				arrayIdPedidos[i] = idPedidos.elementAt(i);
				arrayClientePedidos[i] = clientePedidos.elementAt(i);
			}
			
			//Pasamos en el Intent los vectores como 2 array
			bundle = new Bundle();
			bundle.putStringArray("ARRAY_ID_PEDIDOS", arrayIdPedidos);
			bundle.putStringArray("ARRAY_CLIENTE_PEDIDOS", arrayClientePedidos);
		
			intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoConfirmacionEnviarPedidos.class);
			intent.putExtras(bundle);
		
			startActivityForResult(intent, DIALOGO_CONFIRMACION_ENVIAR_PEDIDOS);
		}
		else
		{
			Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_NINGUN_PEDIDO_SELECCIONADO, Toast.LENGTH_SHORT).show();
			
			habilitaClickEnActivity(true);
		}
	}
	
	/**
	 * Permite modificar el prepedido
	 * 
	 * @param v
	 */
	private void subdialogoModificarPedido(View v)
	{
		Intent intent = null;
		Bundle bundle = null;
		PedidoBD datosPedido = null;
		
		//Localizamos los datos del pedido, si no hay, no invocamos el subdialogo
		datosPedido = dameDatosPedido(v);
		
		if (datosPedido != null)
		{
			//Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_CARGAR_PEDIDO, Toast.LENGTH_SHORT).show();
			
			//Pasamos los datos del pedido para que sean modificado
			bundle = new Bundle();
			bundle.putBoolean("PANTALLA_MODIFICACION", true);
			bundle.putInt("ID_PEDIDO", datosPedido.getIdPedido());
		
			intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaRutero.class);
			intent.putExtras(bundle);
		
			startActivityForResult(intent, DIALOGO_MODIFICAR_PEDIDO);
		}
	}
	
	/**
	 * Dada una vista nos devuelve los datos del pedido asociados a la misma
	 * 
	 * @param v
	 * 
	 * @return los datos del pedidos, o null en caso de que no se hayan encontrado
	 */
	private PedidoBD dameDatosPedido(View v)
	{
		//En el contentDescription de la view tenemos guardado el idPedido
		return dameDatosPedidoSegunId(Integer.parseInt(v.getContentDescription().toString()));
	}
	
	/**
	 * Devuelve los datos del pedido, con el identificador pasado como parametro
	 * @param idPedido
	 * 
	 * @return datos del pedido, o null, si no hay ningun pedido con ese identificador
	 */
	private PedidoBD dameDatosPedidoSegunId(int idPedido)
	{
		PedidoBD resultado = null;
		
		//Recorremos la lista de pedidos hasta localizar el pedido
		for (int i=0; i<this.pedidosBD.size(); i++)
		{
			if (this.pedidosBD.elementAt(i).getIdPedido() == idPedido)
			{
				resultado = this.pedidosBD.elementAt(i);
				break;
			}
		}
		
		return resultado;
	}
	
	/**
	 * Borra uno de los pedidos de la lista, segun su id pedido
	 * @param idPedido
	 */
	private void borraPedidoSegunId(int idPedido)
	{	
		//Recorremos la lista de pedidos hasta localizar el pedido a borrar y lo borramos
		for (int i=0; i<this.pedidosBD.size(); i++)
		{
			if (this.pedidosBD.elementAt(i).getIdPedido() == idPedido)
			{
				this.pedidosBD.removeElementAt(i);
				break;
			}
		}
	}
	
	/**
	 * Busca una vista de la tabla de pedidos, dato su id.
	 * 
	 * @param idView
	 * @return la View buscada
	 */
	private View dameViewSegunId(int idView)
	{
		return (View)this.viewsTablaPedidos.get(idView);
	}
	
	/**
	 * Devuelve el ID de la view, que sera uno de los datos de la tabla. El ID debe ser entero y sera de la forma
	 * x..xy, donde "x...x" indica la fila e "y" la columna, el numero de filas no se sabe cual sera, pero el numero de
	 * columnas es fijo, siempre sera de 1 a 11. Asi para una vista con ID 2604, la fila sera "26" y la columna "4".
	 * 
	 * @param columnaTabla, la columna que ocupa la view, para la cual queremos obtener su ID.
	 * 
	 * @return el ID de la view calculada
	 */
	private int dameIdViewNuevo(int columnaTabla)
	{
		return ((this.tablaPedidos.getChildCount()+1) * 100) + columnaTabla;
	}
	
	/**
	 * Devuelve el id view de un widget de la tabla de pedidos
	 * 
	 * @param fila de la tabla, empieza a partir de la 1
	 * @param columna de la tabla, empieza a partir de la 1
	 * 
	 * @return el ID de la view calculada
	 */
	private int calculaIdView(int fila, int columnaTabla)
	{
		return (fila * 100) + columnaTabla;
	}
	
	/**
	 * Devuelve el color de la fila actual que vamos a insertar
	 * @return
	 */
	private int dameColorFila()
	{
		int resultado = this.getResources().getColor(R.color.colorFilaClaro);
		
		if (!this.colorFilaClaro)
		{
			resultado = this.getResources().getColor(R.color.colorFilaOscuro);
		}
		
		this.colorFilaClaro = !this.colorFilaClaro;
		
		return resultado;
	}
	
	/**
	 * Devuelve el color del texto de la fila actual que vamos a insertar
	 */
	private int dameColorTextoFila()
	{
		int resultado = this.getResources().getColor(R.color.colorFilaDatoAnterior);
		
		if (!this.colorFilaClaro)
		{
			resultado = this.getResources().getColor(R.color.colorFilaDatoAnterior);
		}
		
		return resultado;
	}
	
	private void log(String text)
	{
		Log.d("PantallaRutero", text);
	}
	
	/**
	 * Muestra un mensaje de error toast con el formato correspondiente. 
	 * 
	 * @param mensaje de error a mostrar
	 */
	private void toastMensajeError(String mensaje)
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_error,
		                               (ViewGroup) findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(mensaje);

		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
	
	public class TareaEnviaPrepedidos extends AsyncTask<Void, Void, Void> 
	{
		private int maximoValorBarraProgreso = 100;
		private ProgressDialog dialog = null;
		private Context contexto = null;
		private String[] idPedidos = null;
		
		//Indica si se ha producido un error durante el proceso de envio de los prepedidos
		private boolean hayErrorEnvioPrepedido = false;
		
		//Almacena el identificador de los prepedidos que ha sido enviado correctamente a intraza
		private ArrayList<String> listaPrepedidosEnviados = new ArrayList<String>();
		
		//Indica si se va ha utilizar WIFI en lugar de 3G para enviar el pedido
		private boolean usarWIFI = false;
		
		public TareaEnviaPrepedidos(Context contexto, String[] idPedidos, boolean usarWIFI) 
		{
			this.contexto = contexto;
			this.idPedidos = idPedidos;
			this.usarWIFI = usarWIFI;
			
			this.dialog = new ProgressDialog(contexto);
	    }
		
		protected void onPreExecute() 
		{
			this.dialog.setMessage("Enviando pedidos...");
			this.dialog.setCancelable(false);
			this.dialog.setCanceledOnTouchOutside(false);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			this.dialog.setProgress(0);
			this.dialog.setMax(this.maximoValorBarraProgreso);
			
			this.dialog.show();
		}
		
		protected Void doInBackground(final Void ... args) 
		{		
			try 
			{
				this.listaPrepedidosEnviados = enviaPrepedidos(this.idPedidos);
				
				//Si el numero de prepedidos enviados es menor que el que se indico que se queria enviar, es porque se ha producido un error el enviar todos
				//o alguno de los prepedidos
				if (this.listaPrepedidosEnviados.size()<this.idPedidos.length)
				{
					throw new Exception("Error al enviar datos de prepedidos a InTraza");
				}
			} 
			catch (Exception e) 
			{
				this.hayErrorEnvioPrepedido = true;
				
				Log.d("EnviarPrepedidos", "TRAZA - Excepcion ("+e.getMessage()+")");
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(final Void success) 
		{	
			if (this.hayErrorEnvioPrepedido)
			{
				alertaMensajeError();
			}
			else
			{
				alertaMensajeSincronizacionCorrecta();
			}
			
			//Ejecutamos el metodo de la activity, cuando la tarea ya ha terminado
			finalTareaEnviaPedidos(this.listaPrepedidosEnviados);
			
			if (this.dialog!=null)
			{
				this.dialog.dismiss();
			}	
		}
		
		private void alertaMensajeError()
		{
			AlertDialog.Builder popup=new AlertDialog.Builder(this.contexto);
			popup.setTitle(Constantes.TITULO_ALERT_ERROR_ENVIO_PREPEDIDOS);
			popup.setMessage(Constantes.MENSAJE_ALERT_ERROR_ENVIO_PREPEDIDOS);
			popup.setPositiveButton("ACEPTAR", null);
			popup.show();
		}
		
		private void alertaMensajeSincronizacionCorrecta()
		{
			AlertDialog.Builder popup=new AlertDialog.Builder(this.contexto);
			popup.setTitle(Constantes.TITULO_ALERT_ENVIO_PREPEDIDOS_CORRECTO);
			popup.setMessage(Constantes.MENSAJE_ALERT_ENVIO_PREPEDIDOS_CORRECTO);
			popup.setPositiveButton("ACEPTAR", null);
			popup.show();
		}
		
		/**
		 * Envia los prepedidos a intraza:
		 * 1 - Obtiene los datos de los prepedidos a enviar de la BD, mediante un cursor.
		 * 2 - Cada pedido del cursor se envia individualmente a InTraza.
		 * 3 - Se hace el tratamiento correspondiente, segun el prepedido se haya enviado correctamente o no.
		 * 
		 * @param Lista con los idPrepedidos a enviar a intraza
		 * 
		 * @return Lista de idPrepedidos que han sido enviados correctamente
		 * @throws Exception
		 */
		private ArrayList<String> enviaPrepedidos(String[] idPrepedidos) throws Exception
		{
			ArrayList<String> prepedidosEnviados = new ArrayList<String>();
			AdaptadorBD db = new AdaptadorBD(this.contexto);
			Cursor cursorPedidos = null;
			Cursor cursorLineasPedido = null;
			JsonPedido jsonPedido = null;
			float incrementoParcial = 0;
			int incrementoTotal = 0;
			JSONObject jsonResultado = null;
			
			db.abrir();
			
			if (idPrepedidos.length>0)
			{
				incrementoParcial = 100/idPrepedidos.length;
			}
			
			for (int i=0; i<idPrepedidos.length; i++)
			{
				//Obtenemos los datos del pedido
				cursorPedidos = db.obtenerPrepedidoConDatosCliente(Integer.parseInt(idPrepedidos[i]));
				
				//Obtenemos los datos de la linea de pedido
				cursorLineasPedido = db.obtenerTodosLosPrepedidosItemDelPrepedido(Integer.parseInt(idPrepedidos[i]));
				
				//Creamos un objeto con los datos del pedido a enviar al WS
				jsonPedido = creaObjetoJsonPedido(cursorPedidos, cursorLineasPedido, db);
			
				//Invocamos al WS para que introducir los datos del pedido en InTraza
				if (usarWIFI)
				{
					jsonResultado = new JSONObject(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesEnvioPrepedidoWIFI(this.contexto), jsonPedido));
				}
				else
				{
					jsonResultado = new JSONObject(invocaWebServiceHttps(Configuracion.dameTimeoutWebServices(this.contexto), Configuracion.dameUriWebServicesEnvioPrepedido3G(this.contexto), jsonPedido));
				}
			
				//Comprobamos si el envio fue correo
				if (jsonResultado.getInt("codigoError") != 0)
				{
					Log.d("EnviarPrepedidos", "TRAZA - ERROR al enviar los datos del prepedido ("+idPedidos[i]+") ("+jsonResultado.getString("descripcionError")+")");
				}	
				else
				{
					//Los pedidos enviados luego seran borrados por la activity de la BD de la tablet, para ello guardamos sus id's
					prepedidosEnviados.add(idPedidos[i]);
				}
				
				//Actualizamos la barra de progreso
				this.dialog.incrementProgressBy((new Float(incrementoParcial)).intValue());
				incrementoTotal += (new Float(incrementoParcial)).intValue();
			}
			
			//Para asegurarnos que llegamos al 100% de la barra de progreso
			if ((100 - incrementoTotal) > 0)
			{
				this.dialog.incrementProgressBy(100 - incrementoTotal);
			}
			
			db.cerrar();
			
			return prepedidosEnviados;
		}
		
		/**
		 * Dado 2 cursores de la BD con la informacion de un pedido y sus lineas de pedidos asociadas, devuelve un objeto con los datos del mismo.
		 * Para cada linea de pedido se debe obtener el nombre del articulo de la BD
		 * 
		 * @param cursorPedido
		 * @param cursorLineasPedido
		 * @param adaptadorBD
		 * 
		 * @return objeto con los datos del pedido y sus lineas de pedido
		 */
		private JsonPedido creaObjetoJsonPedido(Cursor cursorPedido, Cursor cursorLineasPedido, AdaptadorBD db)
		{
			JsonPedido jsonPedido = null;
			ArrayList<JsonLineaPedido> jsonLineasPedido = null;
			//Datos para pedido
			int idPrepedidoP = 0;
			int idClienteP = 0;
			String clienteP = null;
			int diaFechaPedidoP = 0;
			int mesFechaPedidoP = 0;
			int anioFechaPedidoP = 0;
			int diaFechaEntregaP = 0;
			int mesFechaEntregaP = 0;
			int anioFechaEntregaP = 0;
			String observacionesP = null;
			boolean fijarObservacionesP = false;
			int descuentoEspecialP = 0;
			//Datos para la linea de pedido
			int idPrepedidoLP = 0;
			String codArticuloLP = null;
			float cantidadKgLP = 0;
			int cantidadUdLP = 0;
			float tarifaClienteLP = 0; 
			String observacionesLP = null;
			boolean fijarTarifaLP = false;
			boolean fijarArticuloLP = false;
			boolean fijarObservacionesLP = false;
			Cursor cursorArticulo = null;
			String nombreArticuloLP = null;
			
			//Contiene los datos de una fecha descompuesta, es decir, para una fecha DD-MM-YYYY, contiene 3 elementos, 
			//en la posicion 0 tiene DD, en la posicion 1 tiene MM y en la posicion 2 tiene YYYY.
			String[] fechaDescompuesta = null;
			
			//Si tenemos resultado de la consulta...
			if (cursorPedido.moveToFirst())
			{	
				//Obtenemos los datos del pedido
				idPrepedidoP = cursorPedido.getInt(TablaPrepedido.POS_KEY_CAMPO_ID_PREPEDIDO);
				idClienteP = cursorPedido.getInt(TablaPrepedido.POS_CAMPO_ID_CLIENTE);
				clienteP = cursorPedido.getString(TablaPrepedido.NUM_CAMPOS+TablaCliente.POS_CAMPO_NOMBRE_CLIENTE);
				fechaDescompuesta = cursorPedido.getString(TablaPrepedido.POS_CAMPO_FECHA_PREPEDIDO).split(Constantes.SEPARADOR_FECHA);
				diaFechaPedidoP = Integer.parseInt(fechaDescompuesta[0]);
				mesFechaPedidoP = Integer.parseInt(fechaDescompuesta[1]);
				anioFechaPedidoP = Integer.parseInt(fechaDescompuesta[2]);
				fechaDescompuesta = cursorPedido.getString(TablaPrepedido.POS_CAMPO_FECHA_ENTREGA).split(Constantes.SEPARADOR_FECHA);
				diaFechaEntregaP = Integer.parseInt(fechaDescompuesta[0]);
				mesFechaEntregaP = Integer.parseInt(fechaDescompuesta[1]);
				anioFechaEntregaP = Integer.parseInt(fechaDescompuesta[2]);
				observacionesP = cursorPedido.getString(TablaPrepedido.POS_CAMPO_OBSERVACIONES);
				if (cursorPedido.getInt(TablaPrepedido.POS_CAMPO_FIJAR_OBSERVACIONES) == 1)
				{
					fijarObservacionesP = true;
				}
				else
				{
					fijarObservacionesP = false;
				}
				
				descuentoEspecialP = cursorPedido.getInt(TablaPrepedido.POS_CAMPO_DESCUENTO_ESPECIAL);
					
				//Ahora obtenemos los datos de las lineas de pedido
				jsonLineasPedido = new ArrayList<JsonLineaPedido>();
					
				if (cursorLineasPedido.moveToFirst())
				{	
					//Recorremos el cursor para obtener los datos de las lineas de pedido
					do 
					{
						idPrepedidoLP = cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_ID_PREPEDIDO);
						//Por si esta clonado, le quitamos la marca
						codArticuloLP = cursorLineasPedido.getString(TablaPrepedidoItem.POS_CAMPO_CODIGO_ARTICULO).split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[0];
						cantidadKgLP = cursorLineasPedido.getFloat(TablaPrepedidoItem.POS_CAMPO_CANTIDAD_KG);
						cantidadUdLP = cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_CANTIDAD_UD);
						tarifaClienteLP = cursorLineasPedido.getFloat(TablaPrepedidoItem.POS_CAMPO_PRECIO);
						observacionesLP = cursorLineasPedido.getString(TablaPrepedidoItem.POS_CAMPO_OBSERVACIONES);
						if (cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_FIJAR_PRECIO) == 1)
						{
							fijarTarifaLP = true;
						}
						else
						{
							fijarTarifaLP = false;
						}
							
						if (cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_FIJAR_ARTICULO) == 1)
						{
							fijarArticuloLP = true;
						}
						else
						{
							fijarArticuloLP = false;
						}
							
						if (cursorLineasPedido.getInt(TablaPrepedidoItem.POS_CAMPO_FIJAR_OBSERVACIONES) == 1)
						{
							fijarObservacionesLP = true;
						}
						else
						{
							fijarObservacionesLP = false;
						}
						
						//Obtenemos el nombre del articulo
						//Por si esta clonado, le quitamos la marca
						cursorArticulo = db.obtenerArticulo(codArticuloLP.split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[0]);
						cursorArticulo.moveToFirst();
						nombreArticuloLP = cursorArticulo.getString(TablaArticulo.POS_CAMPO_NOMBRE);			
						
						jsonLineasPedido.add(new JsonLineaPedido(idPrepedidoLP, codArticuloLP, nombreArticuloLP, cantidadKgLP, cantidadUdLP, tarifaClienteLP, observacionesLP, fijarTarifaLP, fijarArticuloLP, fijarObservacionesLP));
					} while (cursorLineasPedido.moveToNext());
				}
					
				jsonPedido = new JsonPedido(idPrepedidoP, idClienteP, clienteP, diaFechaPedidoP, mesFechaPedidoP, anioFechaPedidoP, diaFechaEntregaP, mesFechaEntregaP, anioFechaEntregaP, observacionesP, fijarObservacionesP, descuentoEspecialP);
				jsonPedido.setLineasPedido(jsonLineasPedido);
			}		
		
			return jsonPedido;
		}
			
		/**
		 * Invoca un WebService REST enviando los datos con un JSON.
		 * 
		 * @param timeout para recibir una respuesta
		 * @param la URL de invocacion al Web Service
		 * @param los datos del prepedido a enviar
		 * 
		 * @return Una cadena con el resultado de la invocacion al Web Service
		 */
		private String invocaWebServiceJ(int segundosTimeout, String urlWebServiceRest, JsonPedido pedido) throws Exception
		{  
			Log.d("EnvioPrepedido", "TRAZA - URL ("+urlWebServiceRest+") segundo timeout ("+segundosTimeout+")");
			
			String result = "";
			
			//Indicamos el timeout y la URL de conexion
			HttpParams httpParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParams, segundosTimeout * 1000);
		    HttpClient httpclient = new DefaultHttpClient(httpParams);
	        HttpPost request = new HttpPost(urlWebServiceRest);
	        
	        //Obtenemos el JSON del objeto a enviar
	        ObjectMapper mapper = new ObjectMapper();
	        String jsonPedido = mapper.writeValueAsString(pedido);
	        
			Log.d("EnvioPrepedido", "TRAZA - jsonUser ("+jsonPedido+")");
	        
	        //Indicamos la informacion del parametro a enviar
	        request.setEntity(new StringEntity(jsonPedido));
	    	request.setHeader("Accept", "application/json");
	    	request.setHeader("Content-type", "application/json");
	        
	        //Hacemos la peticion HTTP        
	        HttpResponse response = httpclient.execute(request); 
	        
	        //Creamos el buffer de lectura para obtener la respuesta
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	        //Obtenemos el resultado de la invocación del buffer de lectura
			String line = "";
			while ((line = rd.readLine()) != null) 
			{
				result += line;
			}
			
			//Cerramos el buffer
			rd.close();
	 
	        return result;
	    }
	}
	
	/**
	 * Invoca un WebService REST enviando los datos con un JSON.
	 * 
	 * @param timeout para recibir una respuesta
	 * @param la URL de invocacion al Web Service
	 * @param los datos del prepedido a enviar
	 * 
	 * @return Una cadena con el resultado de la invocacion al Web Service
	 */
	private String invocaWebServiceHttp(int segundosTimeout, String urlWebServiceRest, JsonPedido pedido) throws Exception
	{  
		Log.d("Sincronizacion", "TRAZA - URL ("+urlWebServiceRest+") timeout ("+segundosTimeout+")");
		//HttpParams params = new BasicHttpParams();
		HttpClient httpclient = null;  
		HttpPost request = null;  
		String result = "";
		BufferedReader rd = null;
        
		try 
		{  
			httpclient = new DefaultHttpClient();
			HttpConnectionParams.setSoTimeout(httpclient.getParams(), segundosTimeout * 1000);
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), segundosTimeout * 1000); 
			
			//Para convertir el objeto a un json
			ObjectMapper mapper = new ObjectMapper();			
			StringEntity input = new StringEntity(mapper.writeValueAsString(pedido));
			input.setContentType("application/json");
			
			request = new HttpPost(urlWebServiceRest);  
			request.setEntity(input);

			HttpResponse response = httpclient.execute(request); 
            
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
        
		return result;
	}
	
	/**
	 * Invoca un WebService REST enviando los datos con un JSON.
	 * 
	 * @param timeout para recibir una respuesta
	 * @param la URL de invocacion al Web Service
	 * @param los datos del prepedido a enviar
	 * 
	 * @return Una cadena con el resultado de la invocacion al Web Service
	 */
	private String invocaWebServiceHttps(int segundosTimeout, String urlWebServiceRest, JsonPedido pedido) throws Exception
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
				
			String encoding = MyBase64.encode(Configuracion.dameUsuarioWS(this)+":"+Configuracion.damePasswordWS(this));
			
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			
			connection.setConnectTimeout(segundosTimeout*1000);
			connection.setReadTimeout(segundosTimeout*1000);

			connection.setSSLSocketFactory(ctx.getSocketFactory());
			
			connection.setRequestMethod("POST");	        
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
			connection.setRequestProperty("Content-Type", "application/json");
			
			ObjectMapper mapper = new ObjectMapper();
	        String jsonPedido = mapper.writeValueAsString(pedido);
	        
	        OutputStream os = connection.getOutputStream();
			os.write(jsonPedido.getBytes());
			os.flush();
			
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
