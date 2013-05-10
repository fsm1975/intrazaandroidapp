package com.technicalnorms.intraza.interfaz;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import com.technicalnorms.intraza.Configuracion;
import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.interfaz.datos.DatosLineaPedido;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaObservacion;
import com.technicalnorms.intraza.R;

/**
 * Activity que solicita al usuario los datos del nuevo pedido.
 * 
 * @author JLZS
 *
 */
public class DialogoDatosLineaPedido extends Activity
{
	// Widgeds que contienen los datos que debe introducir el usuario para la nueva linea de pedido
	private EditText cantidadKgNuevoLPEdit = null;
	private EditText cantidadUdNuevoLPEdit = null;
	private EditText tarifaNuevoLPEdit = null;
	private CheckBox fijarTarifa = null;
	private CheckBox suprimirTarifa = null;
	private CheckBox fijarObservaciones = null;
	private EditText observacionesLPEdit = null;
	
	// Widget usado para mostrar el precio total de la linea de pedido, se calcula multiplicando la cantidad por la tarifa de la linea de pedido
	private TextView precioTotalLPView = null;
	
	//Almacena los datos de la linea de pedido
	DatosLineaPedido datosLineaPedido = null; 
	
	//Almacena las observaciones por defecto
	String observacionesDefecto = null;
	
	//Almacena los datos de la linea de pedido que se recibio como entrada al subdialogo, se usa para comprobar si ha cambiado algun dato
	//y por tanto esta pendiente de guardar
	DatosLineaPedido datosLineaPedidoOriginal = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button incluirObservacionesBtn = null;
		Button quitarObservacionesBtn = null;
		Button aceptarBtn = null;
		Button cancelarBtn = null;
		Button eliminarBtn = null;
		Button clonarBtn = null;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_datos_linea_pedido);
		
		//Obtenemos del intent los datos de la linea de pedido y las observaciones por defecto
		this.datosLineaPedido = (DatosLineaPedido) this.getIntent().getParcelableExtra("DATOS_LINEA_PEDIDO");
		this.observacionesDefecto = this.getIntent().getStringExtra("OBSERVACIONES_DEFECTO");
		
		//Tenemos que crear una nueva instancia del objeto, ya que sino se referencia al mismo objeto
		this.datosLineaPedidoOriginal = new DatosLineaPedido(this.datosLineaPedido.getCodArticulo(), this.datosLineaPedido.getArticulo(), 
																this.datosLineaPedido.getMedida(), this.datosLineaPedido.getEsCongelado(), this.datosLineaPedido.getUltimaFecha(),
																this.datosLineaPedido.getUltimaUnidades(), this.datosLineaPedido.getUltimaCantidad(), this.datosLineaPedido.getUnidadesTotalAnio(), this.datosLineaPedido.getCantidadTotalAnio(),
																this.datosLineaPedido.getUltimaTarifa(), this.datosLineaPedido.getCantidadKg(), this.datosLineaPedido.getCantidadUd(),
																this.datosLineaPedido.getTarifaCliente(), this.datosLineaPedido.getTarifaLista(), 
																this.datosLineaPedido.getFechaCambioTarifaLista(), this.datosLineaPedido.getObservaciones());
		
		this.datosLineaPedidoOriginal.setFijarTarifa(this.datosLineaPedido.getFijarTarifa());
		this.datosLineaPedidoOriginal.setSuprimirTarifa(this.datosLineaPedido.getSuprimirTarifa());
		
		//Actulizamos los widget de la pantalla con los datos, para mostrarselos al usuario
		//En caso de ser un codigo de articulo que indica que es un clon, al usuario solo se le muestra el codigo del articulo del original del clon
		//por eso se hace el split
		if (this.datosLineaPedido.getEsCongelado())
		{
			((TextView)findViewById(R.id.datoLP)).setTextColor(this.getResources().getColor(R.color.colorTextoArticuloCongelado));
		}
		((TextView)findViewById(R.id.datoLP)).setText(this.datosLineaPedido.getCodArticulo().split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[0]+" - "+this.datosLineaPedido.getArticulo()+ponerMarcaCongelado(this.datosLineaPedido.getEsCongelado()));
		
		if (this.datosLineaPedido.getUltimaFecha().equals(Constantes.SIN_FECHA_ANTERIOR_LINEA_PEDIDO))
		{
			((TextView)findViewById(R.id.infoDatosAnterioresLP)).setText(Constantes.INFO_SIN_FECHA_ANTERIOR_LINEA_PEDIDO);
		}
		else
		{
			((TextView)findViewById(R.id.infoDatosAnterioresLP)).setText("Pedido del día "+this.datosLineaPedido.getUltimaFecha()+".");
		}
		((TextView)findViewById(R.id.infoDatosAnterioresLP)).setTextColor(this.getResources().getColor(R.color.colorFilaDatoAnterior));
		((TextView)findViewById(R.id.datoCantidadAnteriorLP)).setText("Ultima cantidad: "+Constantes.formatearFloat3Decimales.format(this.datosLineaPedido.getUltimaCantidad())+Constantes.SEPARADOR_CANTIDAD_TOTAL_ANIO+" Total año: "+Constantes.formatearFloat2Decimales.format(this.datosLineaPedido.getCantidadTotalAnio()));
		((TextView)findViewById(R.id.datoCantidadAnteriorLP)).setTextColor(this.getResources().getColor(R.color.colorFilaDatoAnterior));
		((TextView)findViewById(R.id.datoTarifaAnteriorLP)).setText("Ultima tarifa: "+Constantes.formatearFloat2Decimales.format(this.datosLineaPedido.getUltimaTarifa())+Constantes.EURO+Constantes.SEPARADOR_MEDIDA_TARIFA+this.datosLineaPedido.getMedida());
		((TextView)findViewById(R.id.datoTarifaAnteriorLP)).setTextColor(this.getResources().getColor(R.color.colorFilaDatoAnterior));
		
		((EditText)findViewById(R.id.cantidadKgNuevoLP)).setText(Constantes.formatearFloat3Decimales.format(this.datosLineaPedido.getCantidadKg()));
		((EditText)findViewById(R.id.cantidadUdNuevoLP)).setText(new Integer(this.datosLineaPedido.getCantidadUd()).toString());
		((EditText)findViewById(R.id.tarifaNuevoLP)).setText(Constantes.formatearFloat2Decimales.format(this.datosLineaPedido.getTarifaCliente()));
		((CheckBox)findViewById(R.id.checkFijarTarifaLP)).setChecked(this.datosLineaPedido.getFijarTarifa());
		((CheckBox)findViewById(R.id.checkSuprimirTarifaLP)).setChecked(this.datosLineaPedido.getSuprimirTarifa());
		((TextView)findViewById(R.id.textoTarifaListaLP)).setText("Tarifa lista:  "+ponerMarcaCambioTarifaListaReciente(this.datosLineaPedido.getFechaCambioTarifaLista())+Constantes.formatearFloat2Decimales.format(this.datosLineaPedido.getTarifaLista())+Constantes.EURO+Constantes.SEPARADOR_MEDIDA_TARIFA+this.datosLineaPedido.getMedida());
		((TextView)findViewById(R.id.textoPrecioTotalLP)).setText(Constantes.CADENA_PREFIJO_PRECIO_TOTAL+Constantes.formatearFloat2Decimales.format(this.datosLineaPedido.getPrecio())+Constantes.EURO);
		((EditText)findViewById(R.id.observacionesLP)).setText(this.datosLineaPedido.getObservaciones());
		((CheckBox)findViewById(R.id.checkFijarObservacionesLP)).setChecked(this.datosLineaPedido.getFijarObservaciones());
		
		//Obtenemos la referencia a los widgets para obtener los datos del usuario
		cantidadKgNuevoLPEdit = (EditText)findViewById(R.id.cantidadKgNuevoLP);
		cantidadUdNuevoLPEdit = (EditText)findViewById(R.id.cantidadUdNuevoLP);
		tarifaNuevoLPEdit = (EditText)findViewById(R.id.tarifaNuevoLP);
		fijarTarifa = (CheckBox)findViewById(R.id.checkFijarTarifaLP);
		suprimirTarifa = (CheckBox)findViewById(R.id.checkSuprimirTarifaLP);
		observacionesLPEdit = (EditText)findViewById(R.id.observacionesLP);
		fijarObservaciones = (CheckBox)findViewById(R.id.checkFijarObservacionesLP);
		precioTotalLPView = (TextView)findViewById(R.id.textoPrecioTotalLP);
		
		//Creamos los checkBox de las observaciones
		creaCheckBoxObservacionesBD();
		
		//Si tenemos datos anteriores en la linea de pedido habilitamos el boton eliminar y clonar
		if (this.datosLineaPedido.getCantidadKg()>0 || this.datosLineaPedido.getCantidadUd()>0)
		{
			((Button)findViewById(R.id.eliminarBotonDialogoNuevoLP)).setEnabled(true);
			((Button)findViewById(R.id.clonarBotonDialogoNuevoLP)).setEnabled(true);
		}
		
		//Definimos evento del EditText que recoge la cantidad de la linea de pedido, cuando pierde el foco, para recalcular el precio total
		cantidadKgNuevoLPEdit.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (!hasFocus)
				{
					if (cantidadKgNuevoLPEdit.getText().toString().length()==0 || cantidadKgNuevoLPEdit.getText().toString().equals("-") || cantidadKgNuevoLPEdit.getText().toString().equals("0") || cantidadKgNuevoLPEdit.getText().toString().equals("-0"))
					{
						cantidadKgNuevoLPEdit.setText("-1");
					}
						
					datosLineaPedido.setCantidadKg(Float.parseFloat(cantidadKgNuevoLPEdit.getText().toString().replace(',', '.')));
					precioTotalLPView.setText(Constantes.CADENA_PREFIJO_PRECIO_TOTAL+Constantes.formatearFloat2Decimales.format(datosLineaPedido.getPrecio())+Constantes.EURO);
				}
			}
		});
		
		//Definimos evento del EditText que recoge la cantidad de la linea de pedido, cuando pierde el foco, para recalcular el precio total
		cantidadUdNuevoLPEdit.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (!hasFocus)
				{
					if (cantidadUdNuevoLPEdit.getText().toString().length()==0 || cantidadUdNuevoLPEdit.getText().toString().equals("-") || cantidadUdNuevoLPEdit.getText().toString().equals("-0"))
					{
						cantidadUdNuevoLPEdit.setText("0");
					}
					
					datosLineaPedido.setCantidadUd(Integer.parseInt(cantidadUdNuevoLPEdit.getText().toString()));
					precioTotalLPView.setText(Constantes.CADENA_PREFIJO_PRECIO_TOTAL+Constantes.formatearFloat2Decimales.format(datosLineaPedido.getPrecio())+Constantes.EURO);
				}
			}
		});
		
		//Definimos evento del EditText que recoge la tarifa de la linea de pedido, cuando pierde el foco, para recalcular el precio total
		tarifaNuevoLPEdit.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (!hasFocus)
				{
					if (tarifaNuevoLPEdit.getText().toString().length()==0)
					{
						tarifaNuevoLPEdit.setText("0");
					}
					
					datosLineaPedido.setTarifaCliente(Float.parseFloat(tarifaNuevoLPEdit.getText().toString().replace(',', '.')));
					precioTotalLPView.setText(Constantes.CADENA_PREFIJO_PRECIO_TOTAL+Constantes.formatearFloat2Decimales.format(datosLineaPedido.getPrecio())+Constantes.EURO);
				}
			}
		});
		
		//Segun la medida del articulo, se pone el foco en el EditText de Kg o Ud y cambiamos el TextView de color para indicar que es la medida por defecto al usuario
		if (datosLineaPedido.getMedida().equals(Constantes.KILOGRAMOS))
		{
			this.cantidadKgNuevoLPEdit.requestFocus();
			((TextView)findViewById(R.id.textoCantidadKgNuevoLP)).setTextColor(this.getResources().getColor(R.color.colorMedidaArticuloPorDefecto));
		}
		else
		{
			this.cantidadUdNuevoLPEdit.requestFocus();
			((TextView)findViewById(R.id.textoCantidadUdNuevoLP)).setTextColor(this.getResources().getColor(R.color.colorMedidaArticuloPorDefecto));
		}
		
		//*******
		//BOTONES
		//*******
		
		//Definimos el evento onClick para los botones del layout
		incluirObservacionesBtn = (Button)findViewById(R.id.botonIncluirObservacionesLP);
		incluirObservacionesBtn.setOnClickListener(new OnClickListener()
		{
			//Tenemos que añadir en las observaciones, los checkbox que esten seleccionados
			public void onClick(View v) 
			{	
				boolean seHanIncluido = false;
				
				//Recorremos todos los checkBox y añadimos el que este chequeado				
				for (int i=0; i<((RelativeLayout)findViewById(R.id.listaCheckBoxObservacionesLP)).getChildCount(); i++)
				{
					CheckBox observacionLista = (CheckBox)((RelativeLayout)findViewById(R.id.listaCheckBoxObservacionesLP)).getChildAt(i);
					if (observacionLista.isChecked())
					{		
						seHanIncluido = true;
						
						String observaciones = observacionesLPEdit.getText().toString().trim();
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
						observacionesLPEdit.setText(observaciones);
					}

					//Deschequeamos el CheckBox, pues ya hemos añadido la linea de comentario
					((CheckBox)((RelativeLayout)findViewById(R.id.listaCheckBoxObservacionesLP)).getChildAt(i)).setChecked(false);
				}
				
				if (seHanIncluido)
				{
					Toast.makeText(getBaseContext(), Constantes.MENSAJE_DATOS_INCLUIR_SELECCIONES, Toast.LENGTH_SHORT).show();
				}
				
				//Guardamos las observaciones en los datos de la linea de pedido para no perderlos
				datosLineaPedido.setObservaciones(observacionesLPEdit.getText().toString().trim());
			}
		});
		
		quitarObservacionesBtn = (Button)findViewById(R.id.botonQuitarObservacionesLP);
		quitarObservacionesBtn.setOnClickListener(new OnClickListener()
		{
			//Tenemos que quitar las observaciones que se hubieran añadido con los checkbox, es decir, ponemos las observaciones por defecto
			public void onClick(View v) 
			{	
				//Actualizamos el widget con las observaciones por defecto si no estan ya puestas
				if (!observacionesDefecto.trim().equals(observacionesLPEdit.getText().toString().trim()))
				{
					observacionesLPEdit.setText(observacionesDefecto);
				
					Toast.makeText(getBaseContext(), Constantes.MENSAJE_DATOS_QUITAR_SELECCIONES, Toast.LENGTH_SHORT).show();
				}
				
    			//Al poner las observaciones por defecto, no tiene sentido que este chequeado el fijarlas
				fijarObservaciones.setChecked(false);
				
				//Guardamos las observaciones en los datos de la linea de pedido para no perderlos
				datosLineaPedido.setObservaciones(observacionesLPEdit.getText().toString().trim());
			}
		});
		
		aceptarBtn = (Button)findViewById(R.id.aceptarBotonDialogoNuevoLP);
		aceptarBtn.setOnClickListener(dameOnClickListenerReturnDatos(false, this));
		
		cancelarBtn = (Button)findViewById(R.id.cancelarBotonDialogoNuevoLP);
		cancelarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				cancelarDialogo();
			}
		});

		eliminarBtn = (Button)findViewById(R.id.eliminarBotonDialogoNuevoLP);
		eliminarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				eliminarDatosNuevoLP();
			}
		});
		
		fijarTarifa.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				if (fijarTarifa.isChecked())
				{
					suprimirTarifa.setChecked(false);
					suprimirTarifa.setEnabled(false);
				}
				else
				{
					suprimirTarifa.setEnabled(true);
				}
			}
		});
		
		suprimirTarifa.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				if (suprimirTarifa.isChecked())
				{
					fijarTarifa.setChecked(false);
					fijarTarifa.setEnabled(false);
				}
				else
				{
					fijarTarifa.setEnabled(true);
				}
			}
		});
		
		clonarBtn = (Button)findViewById(R.id.clonarBotonDialogoNuevoLP);
		clonarBtn.setOnClickListener(dameOnClickListenerReturnDatos(true, this));

		
		//Para que la pantalla de fondo no se vea
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	}
	
	/**
	 * Muestra una marca al final de la descripcion del articulo en caso que sea congelado
	 * 
	 * @param esCongelado
	 * 
	 * @return la marca a poner o cadena vacia en caso de no llevar marca
	 */
	private String ponerMarcaCongelado(boolean esCongelado)
	{
		String resultado = "";
		
		if (esCongelado)
		{
			resultado = Constantes.MARCA_CONGELADO;
		}
		
		return resultado;
	}
	
	/**
	 * Comprueba si hay que poner la marca de cambio reciente en la tarifa por defecto.
	 * Las fechas en la BD de la tablet se guardan como DD/MM/YYYY
	 * 
	 * @param fecha de cambio de la tarifa por defecto
	 * @return la marca si hay que poner la marca, cadena vacia en cualquier otro caso
	 */
	private String ponerMarcaCambioTarifaListaReciente(String fecha)
	{
		String marca = "";
		String[] fechaDescompuesta = null;
		GregorianCalendar calendarFechaCaducidadCambioReciente = null;
		
		if (fecha!=null && !fecha.trim().equals("null") && !fecha.trim().equals(""))
		{
			fechaDescompuesta = fecha.split(Constantes.SEPARADOR_FECHA);
		
			//A la fecha actual le sumamos los dias configurados para que se muestre la marca de cambio de tarifa por defecto,
			//asi obtenemos la fecha en que caduca la marca
			calendarFechaCaducidadCambioReciente = new GregorianCalendar(Integer.parseInt(fechaDescompuesta[2]), Integer.parseInt(fechaDescompuesta[1]), Integer.parseInt(fechaDescompuesta[0]));
			calendarFechaCaducidadCambioReciente.add(GregorianCalendar.DAY_OF_YEAR, Configuracion.dameNumDiasAntiguedadMarcaTarifaDefecto(this));
		
			//Comprobamos si la marca ha caducado
			if (calendarFechaCaducidadCambioReciente.after(new GregorianCalendar()))
			{
				marca = Constantes.MARCA_TARIFA_DEFECTO_CAMBIADA_RECIENTEMENTE;
			}
		}
		
		return marca;
	}
	
	/**
	 * Devuelve el tratamiento del evento OnClick cuando se han introducido los datos de la linea de pedido y se quieren guardar, ya sea al ACEPTAR o al CLONAR
	 * 
	 * @param indica si es un clon del articulo
	 * 
	 * @return un objeto OnClickListener() con la definicion del evento OnClick()
	 */
	private OnClickListener dameOnClickListenerReturnDatos(final boolean esClon, final Context context)
	{
		OnClickListener evento = new OnClickListener()
		{
			public void onClick(View v) 
			{	
				if (cantidadKgNuevoLPEdit.getText().toString().length()==0 || cantidadKgNuevoLPEdit.getText().toString().equals("-") || cantidadKgNuevoLPEdit.getText().toString().equals("0") || cantidadKgNuevoLPEdit.getText().toString().equals("-0"))
				{
					cantidadKgNuevoLPEdit.setText("-1");
				}
				
				if (cantidadUdNuevoLPEdit.getText().toString().length()==0 || cantidadUdNuevoLPEdit.getText().toString().equals("-") || cantidadUdNuevoLPEdit.getText().toString().equals("-0"))
				{
					cantidadUdNuevoLPEdit.setText("0");
				}
				
				//La tarifa solo adminte numeros positivos
				if (tarifaNuevoLPEdit.getText().toString().length()==0)
				{
					tarifaNuevoLPEdit.setText("0");
				}
				
				//Primero guardamos y recalculamos los datos
				datosLineaPedido.setCantidadKg(Float.parseFloat(cantidadKgNuevoLPEdit.getText().toString().replace(',', '.')));
				datosLineaPedido.setCantidadUd(Integer.parseInt(cantidadUdNuevoLPEdit.getText().toString()));
				datosLineaPedido.setTarifaCliente(Float.parseFloat(tarifaNuevoLPEdit.getText().toString().replace(',', '.')));
				datosLineaPedido.setFijarTarifa(fijarTarifa.isChecked());
				datosLineaPedido.setSuprimirTarifa(suprimirTarifa.isChecked());
				datosLineaPedido.setObservaciones(observacionesLPEdit.getText().toString().trim());
				datosLineaPedido.setFijarObservaciones(fijarObservaciones.isChecked());
				
				//No se puede aceptar los datos si la cantidad es 0, por defecto la cantidad en KG es -1
				if (datosLineaPedido.getCantidadKg()==-1 && datosLineaPedido.getCantidadUd()==0)
				{
					toastMensajeError(Constantes.AVISO_DATOS_NUEVO_LP_CANTIDAD_0);
				}
				//No se puede aceptar los datos si la tarifa del cliente es 0 a no ser que este configurado para que si los acepte
				else if (datosLineaPedido.getTarifaCliente()==0 && !Configuracion.estaPermitidoLineasPedidoConPrecio0(context))
				{
					toastMensajeError(Constantes.AVISO_DATOS_NUEVO_LP_TARIFA_CLIENTE_0);
				}
				else
				{
					returnDatosNuevoLP(esClon);
				}
			}
		};
		
		return evento;
	}
	
	/**
	 * Crea la lista de chekBox, obteniendo las datos de la lista de la BD
	 */
	private void creaCheckBoxObservacionesBD()
	{
		RelativeLayout listaCheckBox = (RelativeLayout)findViewById(R.id.listaCheckBoxObservacionesLP);
		RelativeLayout.LayoutParams paramsRelativeLayout = null;		
		CheckBox checkBox = null;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorObservaciones = null;
		int idViewAnterior = listaCheckBox.getId();
		
		//Consultamos todas las observaciones y creamos con  cada una de ellas un checkBox
		db.abrir();	
		
		cursorObservaciones = db.obtenerTodasLasObservacionesPrepedidoItem();
		
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
	 * Muestra un mensaje de error toast con el formato correspondiente. 
	 * 
	 * @param mensaje de error a mostrar
	 */
	public void toastMensajeError(String mensaje)
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
	
	/**
	 * - Devuelve a la activity que lo solicito los datos el nuevo pedido, introducidos por el usuario.
	 * - Indica que le ejecución de la activity ha sido OK.
	 * - Finaliza la activity.
	 * 
	 * @param indica si es un clon del articulo
	 */
	private void returnDatosNuevoLP(boolean esClon)
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_OK;

		//Comprobamos si hay algun cambio en los datos de la linea de pedido
		if (!this.datosLineaPedido.esIgual(this.datosLineaPedidoOriginal))
		{
			this.datosLineaPedido.setHayCambiosSinGuardar(true);
		}

		//Le pasamos los datos de la linea de pedido a la activity principal en el intent y si tenemos que clonar o no
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaRutero.class);
		intent.putExtra("DATOS_LINEA_PEDIDO", this.datosLineaPedido);
		intent.putExtra("ES_CLON", esClon);

		setResult(resultadoActivity, intent);
		
		if (!esClon)
		{
			Toast.makeText(getBaseContext(), Constantes.MENSAJE_DATOS_NUEVO_LP_ACEPTADOS, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getBaseContext(), Constantes.MENSAJE_DATOS_NUEVO_LP_CLONAR_ACEPTADOS, Toast.LENGTH_SHORT).show();
		}
		
		finish();
	}
	
	/**
	 * - Devuelve a la activity que lo solicito los datos el nuevo pedido, introducidos por el usuario.
	 * - Indica que le ejecución de la activity ha sido OK.
	 * - Finaliza la activity.
	 */
	private void eliminarDatosNuevoLP()
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_FIRST_USER;
		
		//No necesitamos mandar ninguna información a la activity
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaRutero.class);
		setResult(resultadoActivity, intent);
		
		Toast.makeText(getBaseContext(), Constantes.MENSAJE_DATOS_NUEVO_LP_ELIMINADOS, Toast.LENGTH_SHORT).show();
		
		finish();
	}
	
	/**
	 * Termina la activity
	 */
	private void cancelarDialogo()
	{
		finish();
	}
	
	private void log(String text)
	{
		Log.d("DialogoDatosNuevoPedido", text);
	}

}