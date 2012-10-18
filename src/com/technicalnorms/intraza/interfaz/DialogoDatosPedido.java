package com.technicalnorms.intraza.interfaz;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.interfaz.datos.DatosPedido;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaCliente;
import com.technicalnorms.intraza.interfaz.datosBD.TablaObservacion;
import com.technicalnorms.intraza.R;

/**
 * Activity que solicita al usuario los datos del nuevo pedido.
 * 
 * @author JLZS
 *
 */
public class DialogoDatosPedido extends Activity
{	
	//Almacena los datos del pedido
	private DatosPedido datosPedido = null;
	
	//Almacena los datos del pedido recividos al invocar al subdialogo
	private DatosPedido datosPedidoOriginal = null;
	
	//Widgeds que contienen los datos que debe introducir el usuario para el nuevo pedido
	private AutoCompleteTextView clienteView = null;
	private DatePicker fechaView = null;
	private EditText observacionesEdit = null;
	private CheckBox fijarObservaciones = null;
	private Spinner spinnerCliente = null;
	
	//Almacena los clientes que hay en la BD y que por tanto puede indicar el usuario
	private ArrayList<String> clienteArrayList = null;
	
	//Almacena la fecha del sistema, en el momento en que se crea el subdialogo
	private GregorianCalendar calendarFechaSistema = new GregorianCalendar();
	
	//Almacena las observaciones por defecto
	String observacionesDefecto = null;
	
	//Indica si el subdialo se invoco desde la activity principal es decir, desde el menu principal de la aplicacion
	private boolean invocacionDesdeActiviyPrincipal = false;
	
	//Almacena en cada posición un array de String de 3 elementos, guardaremos en la posicion 0 el id del cliente, en la 1 el nombre del cliente
	//y en la posicion 2 las observaciones por defecto, el orden corresponde al de clienteArrayList, asi cuando el usuario selecciona un cliente 
	//del ArrayList, tenemos sus datos y nos evitamos una consulta a la BD
	private Vector<String[]> vectorDatosClientes = null;
	
	//Indica si se ha iniciado el evento del spinner, para no inicializarlo 2 veces
	private boolean eventoSpinnerOnSelectedInicializado = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button incluirObservacionesBtn = null;
		Button quitarObservacionesBtn = null;
		Button aceptarBtn = null;
		Button cancelarBtn = null;
		Button abreSpinnerBtn = null;
		Button hoyBtn = null;
		Button lunesBtn = null;
		ArrayAdapter<String> adapter = null;
		Date fechaManiana = null;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_datos_pedido);
		
		//Inicializamos los miembros
		this.vectorDatosClientes = new Vector<String[]>();
		
		//Obtenemos la fecha de maniana, obtenemos la fecha de maniana para inicializar la fecha de entrega y pedido, 
		//ya que un pedido se solicita con fecha del dia siguiente a cuando se crea en la tablet
		GregorianCalendar calendarFechaManiana = (GregorianCalendar)this.calendarFechaSistema.clone();
		calendarFechaManiana.add(GregorianCalendar.DAY_OF_MONTH, 1);
		fechaManiana = calendarFechaManiana.getTime();
		
		//Comprobamos desde donde se solicito el subdialogo
		this.invocacionDesdeActiviyPrincipal = this.getIntent().getExtras().getBoolean("ACTIVITY_PRINCIPAL");
		
		//Inicializarmos los arrayList a utlizar en el ArrayAdapter
		inicializaDatosClientesBD();
		
		//Obtenemos la referencia a los widgets para obtener los datos del usuario
		this.fechaView = (DatePicker)findViewById(R.id.fechaEntregaDCNP);
		this.observacionesEdit = (EditText)findViewById(R.id.observacionesDCNP);
		this.fijarObservaciones = (CheckBox)findViewById(R.id.checkFijarObservacionesDCNP);
		this.clienteView = (AutoCompleteTextView)findViewById(R.id.clienteDCNP);
		this.spinnerCliente = (Spinner)findViewById(R.id.spinnerInvisibleClienteDCNP);
				
		//Si ya tenemos datos anteriores del pedido, los mostramos en la pantalla
		this.datosPedido = (DatosPedido) this.getIntent().getParcelableExtra("DATOS_PEDIDO");
		
		//Si aun no tenemos datos de pedido nos creamos uno con valores por defecto
		if (this.datosPedido==null)
		{
			//Se obtienen los datos para el TextView AutoComplete y Spinner
		    adapter = new ArrayAdapter<String>(this, R.layout.list_item, this.clienteArrayList);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    this.spinnerCliente.setAdapter(adapter);
			
			adapter = new ArrayAdapter<String>(this, R.layout.list_item, this.clienteArrayList);		
			this.clienteView.setAdapter(adapter);

			((TextView)findViewById(R.id.textoInfoDCNP)).setText(Constantes.MENSAJE_TITULO_DIALOGO_NUEVO_PEDIDO);
			
			int idPedidoTmp = new Long(new Date().getTime()).intValue();
			
			//Para controlar que el idPedido sea un numero entero positivo
			if (idPedidoTmp < 0)
			{
				idPedidoTmp = idPedidoTmp * -1;
			}
			
			this.datosPedido = new DatosPedido(idPedidoTmp,
												0,
												"",
												fechaManiana.getDate(), 
												fechaManiana.getMonth()+1, 
												fechaManiana.getYear()+1900, 
												fechaManiana.getDate(), 
												fechaManiana.getMonth()+1, 
												fechaManiana.getYear()+1900, 
												0,
												"",
												false,
												0);
			
		    this.clienteView.addTextChangedListener(new TextWatcher() {  
		    	
		    	/*
		    	 * Cuando tengamos un cliente valido actualizamos las observaciones por defecto
		    	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
		    	 */
		    	public void afterTextChanged(Editable s)  
		    	{  
		    		String[] datosCliente = dameDatosCliente(clienteView.getText().toString().trim());
		    		
		    		if (datosCliente!=null)
		    		{
		    			observacionesEdit.setText(datosCliente[2]);
		    			observacionesDefecto = datosCliente[2];
		    			
		    			//Al poner las observaciones por defecto, no tiene sentido que este chequeado el fijarlas
						fijarObservaciones.setChecked(false);
		    		}
		    	}  
		    	  
		    	public void beforeTextChanged(CharSequence s, int start, int count, int after)  
		    	{  
		    	}  
		    	  
		    	public void onTextChanged(CharSequence s, int start, int before, int count)  
		    	{  
		    	}  
		    });
		}
		else
		{
			((TextView)findViewById(R.id.textoInfoDCNP)).setText(Constantes.MENSAJE_TITULO_DIALOGO_MODIFICA_PEDIDO);
			
			//Obtenemos de los datos del cliente inicializados de la BD las observaciones por defecto
			this.observacionesDefecto = dameDatosClientePorId(new Integer(this.datosPedido.getIdCliente()).toString())[2];
			
			//Se deshabilita el widget que recoge el cliente ya que este dato no se puede modificar
			this.clienteView.setEnabled(false);
			((Button)findViewById(R.id.botonAbreSpinnerDCNP)).setEnabled(false);
			((Button)findViewById(R.id.botonAbreSpinnerDCNP)).setTextColor(this.getResources().getColor(R.color.colorTextoBotonDeshabilitadoTablaP));
		}
		
		//Tenemos que crear una nueva instancia del objeto, ya que sino se referencia al mismo objeto
		this.datosPedidoOriginal = new DatosPedido(this.datosPedido.getIdPedido(),
													this.datosPedido.getIdCliente(),
													this.datosPedido.getCliente(),
													this.datosPedido.getDiaFechaPedido(),
													this.datosPedido.getMesFechaPedido(),
													this.datosPedido.getAnioFechaPedido(),
													this.datosPedido.getDiaFechaEntrega(),
													this.datosPedido.getMesFechaEntrega(),
													this.datosPedido.getAnioFechaEntrega(),
													this.datosPedido.getPrecioTotal(),
													this.datosPedido.getObservaciones(),
													this.datosPedido.getFijarObservaciones(),
													this.datosPedido.getDescuentoEspecial());
			
		//Tambien recogemos el flag que nos indica si hay cambios y lineas de pedido en el pedido
		this.datosPedidoOriginal.setHayDatosPedidoSinGuardar(this.datosPedido.hayDatosPedidoSinGuardar());
		this.datosPedidoOriginal.setHayLineasPedido(this.datosPedido.hayLineasPedido());

		//Actualizamos los datos del pedido en la pantalla
		this.clienteView.setText(this.datosPedido.getCliente());
		this.fechaView.init(this.datosPedido.getAnioFechaEntrega(), this.datosPedido.getMesFechaEntrega()-1, this.datosPedido.getDiaFechaEntrega(), new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
			{
			    // No hacemos nada
			}
		});
		this.observacionesEdit.setText(this.datosPedido.getObservaciones());
		this.fijarObservaciones.setChecked(this.datosPedido.getFijarObservaciones());
		
		//Creamos los checkBox de las observaciones
		creaCheckBoxObservacionesBD();
		
		//*******
		//BOTONES
		//*******
		
		//Definimos el evento onClick para los botones del layout
		incluirObservacionesBtn = (Button)findViewById(R.id.botonIncluirObservacionesDCNP);
		incluirObservacionesBtn.setOnClickListener(new OnClickListener()
		{
			//Tenemos que añadir en las observaciones, los checkbox que esten seleccionados
			public void onClick(View v) 
			{	
				boolean seHanIncluido = false;
				
				//Recorremos todos los checkBox y añadimos el que este chequeado				
				for (int i=0; i<((RelativeLayout)findViewById(R.id.listaCheckBoxObservacionesDCNP)).getChildCount(); i++)
				{
					CheckBox observacionLista = (CheckBox)((RelativeLayout)findViewById(R.id.listaCheckBoxObservacionesDCNP)).getChildAt(i);

					if (observacionLista.isChecked())
					{		
						seHanIncluido = true;
						
						String observaciones = observacionesEdit.getText().toString();
						String observacion = observacionLista.getText().toString();
						
						//Añadimos el "." al final de la observación sino viene, para que quede mas claro.
						if (!observacion.endsWith("."))
						{
							observacion += ".";
						}
						
						if (observaciones.length()>0)
						{
							//Añadimos la observacion a las observaciones, insertandola con una linea nueva.							
							observaciones += " " + observacion;
						}
						else
						{
							observaciones += observacion;
						}
						
						//Actualizamos el widget
						observacionesEdit.setText(observaciones);
					}

					//Deschequeamos el CheckBox, pues ya hemos añadido la linea de comentario
					((CheckBox)((RelativeLayout)findViewById(R.id.listaCheckBoxObservacionesDCNP)).getChildAt(i)).setChecked(false);
				}
				
				if (seHanIncluido)
				{
					Toast.makeText(getBaseContext(), Constantes.MENSAJE_DATOS_INCLUIR_SELECCIONES, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		quitarObservacionesBtn = (Button)findViewById(R.id.botonQuitarObservacionesDCNP);
		quitarObservacionesBtn.setOnClickListener(new OnClickListener()
		{
			//Tenemos que quitar las observaciones que se hubieran añadido con los checkbox, es decir, ponemos las observaciones por defecto
			public void onClick(View v) 
			{	
				//Actualizamos el widget con las observaciones por defecto si no estan ya puestas
				if (!observacionesDefecto.trim().equals(observacionesEdit.getText().toString().trim()))
				{
					//Actualizamos el widget con las observaciones por defecto
					observacionesEdit.setText(observacionesDefecto);
				
					Toast.makeText(getBaseContext(), Constantes.MENSAJE_DATOS_QUITAR_SELECCIONES, Toast.LENGTH_SHORT).show();
				}
				
				//Al poner las observaciones por defecto, no tiene sentido que este chequeado el fijarlas
				fijarObservaciones.setChecked(false);
			}
		});
		
		aceptarBtn = (Button)findViewById(R.id.aceptarBotonDialogoDCNP);
		aceptarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{		
				String[] datosCliente = null;
				
				//Segun sea una modificacion de pedido o creacion de nuevo pedido, las comprobaciones seran distintas
				if (clienteView.isEnabled())
				{				
					//ESTAMOS EN CREACION DE NUEVO PEDIDO
					
					//Comprobamos si el cliente seleccionado es valido
					if (esClienteValido(clienteView.getText().toString()))
					{
						//Comprobamos que la fecha de entrega es correcta
						if (esFechaEntregaCorrecta(fechaView))
						{
							//Actualizamos los datos solicitados al usuario en la pantalla		
							datosCliente = dameDatosCliente(clienteView.getText().toString().trim());
			    		
							if (datosCliente!=null)
							{
								datosPedido.setIdCliente(Integer.parseInt(datosCliente[0]));
								datosPedido.setCliente(datosCliente[1]);
								datosPedido.setDiaFechaPedido(fechaView.getDayOfMonth());
								datosPedido.setMesFechaPedido(fechaView.getMonth()+1);
								datosPedido.setAnioFechaPedido(fechaView.getYear());
								datosPedido.setDiaFechaEntrega(fechaView.getDayOfMonth());
								datosPedido.setMesFechaEntrega(fechaView.getMonth()+1);
								datosPedido.setAnioFechaEntrega(fechaView.getYear());
								datosPedido.setObservaciones(observacionesEdit.getText().toString().trim());
								datosPedido.setFijarObservaciones(fijarObservaciones.isChecked());
							
								//Comprobamos si hay algun cambio en los datos del pedido
								if (!datosPedido.esIgual(datosPedidoOriginal))
								{
									datosPedido.setHayDatosPedidoSinGuardar(true);
								}
	
								returnDatosDCNP();
							}
							else
							{
								toastMensajeError(Constantes.AVISO_DATOS_DCNP_CLIENTE_NO_VALIDO);
							}
						}
						else
						{
							toastMensajeError(Constantes.AVISO_DATOS_DCNP_FECHA_NO_VALIDA);
						}
					}
					else
					{
						toastMensajeError(Constantes.AVISO_DATOS_DCNP_CLIENTE_NO_VALIDO);
					}
				}
				else
				{
					//ESTAMOS EN MODIFICACION DE PEDIDO
					
					//Comprobamos que la fecha de entrega es correcta
					if (esFechaEntregaCorrecta(fechaView))
					{
						datosPedido.setDiaFechaPedido(fechaView.getDayOfMonth());
						datosPedido.setMesFechaPedido(fechaView.getMonth()+1);
						datosPedido.setAnioFechaPedido(fechaView.getYear());
						datosPedido.setDiaFechaEntrega(fechaView.getDayOfMonth());
						datosPedido.setMesFechaEntrega(fechaView.getMonth()+1);
						datosPedido.setAnioFechaEntrega(fechaView.getYear());
						datosPedido.setObservaciones(observacionesEdit.getText().toString().trim());
						datosPedido.setFijarObservaciones(fijarObservaciones.isChecked());
							
						//Comprobamos si hay algun cambio en los datos del pedido
						if (!datosPedido.esIgual(datosPedidoOriginal))
						{
							datosPedido.setHayDatosPedidoSinGuardar(true);
						}
	
						returnDatosDCNP();
					}
					else
					{
						toastMensajeError(Constantes.AVISO_DATOS_DCNP_FECHA_NO_VALIDA);
					}
				}
			}
		});
		
		cancelarBtn = (Button)findViewById(R.id.cancelarBotonDialogoDCNP);
		cancelarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				cancelarDialogo();
			}
		});
		
		abreSpinnerBtn = (Button)findViewById(R.id.botonAbreSpinnerDCNP);
		abreSpinnerBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{		
				//El evento del spinner solo lo inicializamos una sola vez
				if (!eventoSpinnerOnSelectedInicializado)
				{
					eventoSpinnerOnSelectedInicializado = true;
					
					spinnerCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
					{
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int pos,long id)
						{
							clienteView.setText(vectorDatosClientes.elementAt(pos)[1]);
							clienteView.dismissDropDown();
							observacionesEdit.setText(vectorDatosClientes.elementAt(pos)[2]);
							observacionesDefecto = vectorDatosClientes.elementAt(pos)[2];
							
							//Al poner las observaciones por defecto, no tiene sentido que este chequeado el fijarlas
							fijarObservaciones.setChecked(false);
						}
		    	 
						@Override
						public void onNothingSelected(AdapterView<?> arg0)
						{
						}
					});
				}
			    spinnerCliente.performClick();
			}
		});
		
		hoyBtn = (Button)findViewById(R.id.botonFechaHoyDCNP);
		hoyBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				Date fechaHoy = calendarFechaSistema.getTime();
				fechaView.init(fechaHoy.getYear()+1900, fechaHoy.getMonth(), fechaHoy.getDate(), new OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
					{
					    // No hacemos nada
					}
				});
			}
		});
		
		lunesBtn = (Button)findViewById(R.id.botonFechaLunesDCNP);
		lunesBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				GregorianCalendar calendarFechaLunes = (GregorianCalendar)calendarFechaSistema.clone();
				int diaSemana = calendarFechaSistema.get(GregorianCalendar.DAY_OF_WEEK);
				if (diaSemana != GregorianCalendar.MONDAY)  
				{  
				    //Calcula cuantos dias se debe sumar al dia de la semana actual para llegar al Lunes, el 2 es la diferencia entre el Sabado y el Lunes
				    calendarFechaLunes.add(GregorianCalendar.DAY_OF_YEAR, (GregorianCalendar.SATURDAY - diaSemana + 2) % 7);  
				}
				else
				{
					//La fecha de hoy es Lunes y queremos el proximo lunes el de la semana que viene
					calendarFechaLunes.add(GregorianCalendar.DAY_OF_YEAR, 7); 
				}
				Date fechaLunes = calendarFechaLunes.getTime();
				fechaView.init(fechaLunes.getYear()+1900, fechaLunes.getMonth(), fechaLunes.getDate(), new OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
					{
					    // No hacemos nada
					}
				});
			}
		});
	}	
	
	/**
	 * Crea la lista de chekBox, obteniendo las datos de la lista de la BD
	 */
	private void creaCheckBoxObservacionesBD()
	{
		RelativeLayout listaCheckBox = (RelativeLayout)findViewById(R.id.listaCheckBoxObservacionesDCNP);
		RelativeLayout.LayoutParams paramsRelativeLayout = null;		
		CheckBox checkBox = null;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorObservaciones = null;
		int idViewAnterior = listaCheckBox.getId();
		
		//Consultamos todas las observaciones y creamos con  cada una de ellas un checkBox
		db.abrir();	
		
		cursorObservaciones = db.obtenerTodasLasObservacionesPrepedido();
		
		if (cursorObservaciones.moveToFirst())
		{
			do 
			{		
				checkBox = new CheckBox(this);
				//Hay que usar el RelativeLayout del padre que contiene al CheckBox
				paramsRelativeLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsRelativeLayout.addRule(RelativeLayout.BELOW, idViewAnterior);
				idViewAnterior = cursorObservaciones.getInt(TablaObservacion.POS_KEY_CAMPO_ID_OBSERVACION);
				checkBox.setId(idViewAnterior);
				checkBox.setLayoutParams(paramsRelativeLayout);
				checkBox.setText(cursorObservaciones.getString(TablaObservacion.POS_CAMPO_DESCRIPCION));
				
				listaCheckBox.addView(checkBox);
				
			} while (cursorObservaciones.moveToNext());
		}
		
		db.cerrar();
	}
	
	/**
	 * Dado un nombre de cliente obtiene los datos guardados en el vector de datos de clientes.
	 * 
	 * @param nombreCliente
	 * @return array de String con los datos del cliente (id, nombre, observaciones)
	 */
	private String[] dameDatosCliente(String nombreCliente)
	{
		String[] datosCliente = null;
		
		//Recorremos los datos de los clientes para obtener el cliente buscado
		for (int i=0; i<vectorDatosClientes.size(); i++)
		{
			if (vectorDatosClientes.elementAt(i)[1].equals(nombreCliente))
			{
				datosCliente = vectorDatosClientes.elementAt(i);
				break;
			}
		}
		
		return datosCliente;
	}
	
	/**
	 * Dado un id de cliente obtiene los datos guardados en el vector de datos de clientes.
	 * 
	 * @param idCliente
	 * @return array de String con los datos del cliente (id, nombre, observaciones)
	 */
	private String[] dameDatosClientePorId(String idCliente)
	{
		String[] datosCliente = null;
		
		//Recorremos los datos de los clientes para obtener el cliente buscado
		for (int i=0; i<vectorDatosClientes.size(); i++)
		{
			if (vectorDatosClientes.elementAt(i)[0].equals(idCliente))
			{
				datosCliente = vectorDatosClientes.elementAt(i);
				break;
			}
		}
		
		return datosCliente;
	}
	
	/**
	 * Devuelve un objeto ArrayList<String> con los clientes de la BD.
	 * 
	 * @return ArrayList con la lista de clientes
	 */
	private ArrayList<String> inicializaDatosClientesBD()
	{
		this.clienteArrayList = new ArrayList<String>();
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorClientes = null;
		String [] datosCliente = null;
		
		//Primero lo inicializamos a vacio
		this.clienteArrayList.add("");
		datosCliente = new String[3];
		datosCliente[0] = "";
		datosCliente[1] = "";
		datosCliente[2] = "";
		this.vectorDatosClientes.add(datosCliente);
		

		//Consultamos todos los clientes y metemos cada uno de ellos en el ArrayList, tambien guardamos sus datos pues los necesitaremos posteriormente
		db.abrir();	
		
		cursorClientes = db.obtenerTodosLosClientes();
		
		if (cursorClientes.moveToFirst())
		{
			do 
			{				
				this.clienteArrayList.add(cursorClientes.getString(TablaCliente.POS_CAMPO_NOMBRE_CLIENTE));
				
				datosCliente = new String[3];
				datosCliente[0] = cursorClientes.getString(TablaCliente.POS_KEY_CAMPO_ID_CLIENTE).trim();
				datosCliente[1] = cursorClientes.getString(TablaCliente.POS_CAMPO_NOMBRE_CLIENTE).trim();
				datosCliente[2] = cursorClientes.getString(TablaCliente.POS_CAMPO_OBSERVACIONES_PREPEDIDO).trim();
				this.vectorDatosClientes.add(datosCliente);
				
			} while (cursorClientes.moveToNext());
		}
		
		db.cerrar();
		
		return this.clienteArrayList;
	}
	
	/**
	 * Comprueba si el cliente pasado como parametro, es uno de los clientes de la lista.
	 * 
	 * @param cliente a comprobar si pertenece a la lista.
	 * @return true si el cliente pertenece a la lista de clientes o false en caso contrario.
	 */
	private boolean esClienteValido(String cliente)
	{
		boolean esValido = false;
		
		if (!cliente.trim().equals(""))
		{
			esValido = this.clienteArrayList.contains(cliente);
		}
		
		return esValido;
	}
	
	/**
	 * Comprueba si la fecha de entrega es posterior a la fecha del sistema.
	 * 
	 * @param fecha de entrega.
	 * @return true si la fecha de entrega es posterior a la fecha del sistema, false, en caso contrario.
	 */
	private boolean esFechaEntregaCorrecta(DatePicker fechaEntrega)
	{
		boolean resultado = true;	
		Date fechaSistema = this.calendarFechaSistema.getTime();
		Date fecha = new Date(fechaEntrega.getYear() - 1900, fechaEntrega.getMonth(), fechaEntrega.getDayOfMonth());
		
		//Las horas las ignoramos, por eso hacemos que la hora de la fecha de entrega sea siempre mayor que la del sistema
		fechaSistema.setHours(0);		
		fecha.setHours(1);
		
		if (fecha.compareTo(fechaSistema)<0)
		{
			resultado = false;
		}
		
		return resultado;
	}
	
	/**
	 * - Devuelve a la activity que lo solicito los datos de consulta para el nuevo pedido, introducidos por el usuario.
	 * - Indica que le ejecución de la activity ha sido OK.
	 * - Finaliza la activity.
	 */
	private void returnDatosDCNP()
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_OK;

		//Enviamos los datos del pedido a la activity que solicito el subdialogo
		if (this.invocacionDesdeActiviyPrincipal)
		{
			intent = new Intent(this, com.technicalnorms.intraza.InTrazaActivity.class);
		}
		else
		{
			intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaRutero.class);
		}
		intent.putExtra("DATOS_PEDIDO", this.datosPedido);

		setResult(resultadoActivity, intent);
				
		finish();
	}
	
	/**
	 * Termina la activity
	 */
	private void cancelarDialogo()
	{
		finish();
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
}