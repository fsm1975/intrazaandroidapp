package com.technicalnorms.intraza.interfaz;

import java.util.ArrayList;
import java.util.Collections;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.interfaz.datos.DatosLineaPedido;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaArticulo;
import com.technicalnorms.intraza.R;

/**
 * Activity que solicita al usuario los datos del nuevo pedido.
 * 
 * @author JLZS
 *
 */
public class DialogoDatosNuevoArticuloRutero extends Activity
{	
	//Almacena los datos de la nueva linea de pedido
	private DatosLineaPedido datosLineaPedido = null;
		
	//Widgeds que contienen los datos que debe introducir el usuario para la nueva linea de pedido
	private AutoCompleteTextView referenciaView = null;
	private AutoCompleteTextView articuloView = null;
	private CheckBox fijarArticulo = null;
	private Spinner spinnerArticulo = null;
	
	//Almacena los articulo y referencias que hay en la BD y que por tanto puede indicar el usuario
	private ArrayList<String> articuloArrayList = null;
	private ArrayList<String> referenciasArrayList = null;
	
	//Almacena en cada posición un array de String de 2 elementos, guardaremos en la posicion 0 el codigo del articulo y en la 1 el nombre del articulo
	//el orden corresponde al de  los ArrayList, asi cuando el usuario selecciona un elemento del ArrayList, tenemos sus datos y nos evitamos una 
	//consulta a la BD
	private Vector<String[]> vectorDatosArticulos = null;
	
	//Almacena los codigos de los articulos que ya estan en la pantalla del rutero del cliente y que por tanto no se deben mostar al usuario
	//para que los vuelva a incluir
	private String[] articulosYaEnRutero = null;
	
	//Para evitar que se cree un bucle recursivo
	private boolean estaDeshabilitadoEventoTextChanged = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button aceptarBtn = null;
		Button cancelarBtn = null;
		Button abreSpinnerBtn = null;
		ArrayAdapter<String> adapter = null;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_datos_nuevo_articulo_rutero);
		
		//Obtemos los articulos que ya estan en la pantalla del rutero
		this.articulosYaEnRutero = this.getIntent().getExtras().getStringArray("ARRAY_ARTICULOS_EN_RUTERO");
		
		//Obtenemos los datos de los articulos que se pueden añadir al rutero del cliente
		this.vectorDatosArticulos = dameArticulosParaRuteroBD(this.getIntent().getExtras().getInt("ID_CLIENTE"));
		
		//Segun haya articulos o no, mostramos un mensaje o la pantalla de alta del nuevo articulo
		if (this.vectorDatosArticulos.size()>0)
		{	
			setContentView(R.layout.dialogo_datos_nuevo_articulo_rutero);
			
			//Se obtienen los datos para el TextView AutoComplete y Spinner
			adapter = new ArrayAdapter<String>(this, R.layout.list_item, dameArticulos(this.vectorDatosArticulos));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			this.spinnerArticulo = (Spinner)findViewById(R.id.spinnerInvisibleClienteDNAR);
			this.spinnerArticulo.setAdapter(adapter);
			this.spinnerArticulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{
	    			public void onItemSelected(AdapterView<?> parent, View view, int pos,long id)
	    			{
	    				referenciaView.setText(vectorDatosArticulos.elementAt(pos)[0]);
	    				referenciaView.dismissDropDown();
	    				articuloView.setText(vectorDatosArticulos.elementAt(pos)[1]);
	    				articuloView.dismissDropDown();
	    			}
	    	 
	    			@Override
	    			public void onNothingSelected(AdapterView<?> arg0)
	    			{
	    			}
	    		});
	    
			adapter = new ArrayAdapter<String>(this, R.layout.list_item, dameArticulos(this.vectorDatosArticulos));
			this.articuloView = (AutoCompleteTextView)findViewById(R.id.articuloDNAR);
			this.articuloView.setAdapter(adapter);
		    this.articuloView.addTextChangedListener(new TextWatcher() {  
		    	
		    	/*
		    	 * Cuando tengamos un articulo valido actualizamos la referencia
		    	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
		    	 */
		    	public void afterTextChanged(Editable s)  
		    	{  
		    		String referenciaArticulo = null;
		    		
		    		if (!estaDeshabilitadoEventoTextChanged)
		    		{
		    			estaDeshabilitadoEventoTextChanged = true;
		    			
		    			referenciaArticulo = dameReferenciaArticuloSegunNombre(articuloView.getText().toString().trim());
		    		
		    			if (referenciaArticulo!=null)
		    			{
		    				referenciaView.setText(referenciaArticulo);
		    				referenciaView.dismissDropDown();
		    			}
		    			
		    			estaDeshabilitadoEventoTextChanged = false;
		    		}
		    	}  
		    	  
		    	public void beforeTextChanged(CharSequence s, int start, int count, int after)  
		    	{  
		    	}  
		    	  
		    	public void onTextChanged(CharSequence s, int start, int before, int count)  
		    	{  
		    	}  
		    });
			
			adapter = new ArrayAdapter<String>(this, R.layout.list_item, dameReferencias(this.vectorDatosArticulos));
			this.referenciaView = (AutoCompleteTextView)findViewById(R.id.ReferenciaDNAR);
		    this.referenciaView.setAdapter(adapter);
		    this.referenciaView.addTextChangedListener(new TextWatcher() {  
		    	
		    	/*
		    	 * Cuando tengamos una referencia valida actualizamos la descripcion del articulo
		    	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
		    	 */
		    	public void afterTextChanged(Editable s)  
		    	{  
		    		String nombreArticulo = null;
		    		
		    		if (!estaDeshabilitadoEventoTextChanged)
		    		{
		    			estaDeshabilitadoEventoTextChanged = true;
		    			
		    			nombreArticulo = dameNombreArticuloSegunReferencia(referenciaView.getText().toString().trim());
		    		
		    			if (nombreArticulo!=null)
		    			{
		    				articuloView.setText(nombreArticulo);
		    				articuloView.dismissDropDown();
		    			}
		    			
		    			estaDeshabilitadoEventoTextChanged = false;
		    		}
		    	}  
		    	  
		    	public void beforeTextChanged(CharSequence s, int start, int count, int after)  
		    	{  
		    	}  
		    	  
		    	public void onTextChanged(CharSequence s, int start, int before, int count)  
		    	{  
		    	}  
		    });
	    
			//Por defecto el checkBox de fijar articulo, se chequea
			this.fijarArticulo = (CheckBox)findViewById(R.id.checkFijarArticuloDNAR);
			this.fijarArticulo.setChecked(true);

			//*******
			//BOTONES
			//*******
		
			//Definimos el evento onClick para los botones del layout
			aceptarBtn = (Button)findViewById(R.id.aceptarBotonDialogoDNAR);
			aceptarBtn.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{		
					float tarifaDefecto = 0;
					
					//Comprobamos si el articulo seleccionado es valido
					if (esArticuloValido(articuloView.getText().toString()))
					{
						tarifaDefecto = consultaTarifaDefectoArticuloEnBD(referenciaView.getText().toString()); 
								
						datosLineaPedido =  new DatosLineaPedido(referenciaView.getText().toString(), articuloView.getText().toString(), consultaMedidaArticuloEnBD(referenciaView.getText().toString()), consultaCongeladoArticuloEnBD(referenciaView.getText().toString()), Constantes.SIN_FECHA_ANTERIOR_LINEA_PEDIDO, 0, (float)0, (float)0, (float)0, tarifaDefecto, tarifaDefecto, consultaFechaCambioTarifaDefectoArticuloEnBD(referenciaView.getText().toString()), "");
					
						//Chequeamos si hay que fijar el articulo en la BD de intraza, para los futuros ruteros
						if (fijarArticulo.isChecked())
						{
							datosLineaPedido.setFijarArticulo(true);
						}
					
						returnDatosDNAR();
					}
					else
					{
						toastMensajeError(Constantes.AVISO_DATOS_DNAR_ARTICULO_NO_VALIDO);
					}
				}
			});
		
			cancelarBtn = (Button)findViewById(R.id.cancelarBotonDialogoDNAR);
			cancelarBtn.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{
					cancelarDialogo();
				}
			});
		
			abreSpinnerBtn = (Button)findViewById(R.id.botonAbreSpinnerDNAR);
			abreSpinnerBtn.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{				
					spinnerArticulo.performClick();
				}
			});
		}
		else
		{
			setContentView(R.layout.dialogo_mensaje);
			
			//Se actualizan los widget del layout con los datos del mensaje
			((TextView)findViewById(R.id.tituloDM)).setText(Constantes.TITULO_SIN_ARTICULOS_NUEVOS);
			((TextView)findViewById(R.id.informacionDM)).setText(Constantes.INFORMACION_SIN_ARTICULOS_NUEVOS);
								    
			//*******
			//BOTONES
			//*******
					
			aceptarBtn = (Button)findViewById(R.id.aceptarDM);
			aceptarBtn.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{
					cancelarDialogo();
				}
			});
		}
	}
	

	/**
	 * Dado un codigo de articulo, obtiene su medida para la linea de pedidos
	 * 
	 * @param codArticulo
	 * @return la medida para la linea de pedido.
	 */
	private String consultaMedidaArticuloEnBD(String codArticulo)
	{
		String medida = Constantes.KILOGRAMOS;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorArticulos = null;
		
		db.abrir();
		
		cursorArticulos = db.obtenerArticulo(codArticulo);
		
		if (cursorArticulos.moveToFirst())
		{
			if (cursorArticulos.getString(TablaArticulo.POS_CAMPO_ES_KG).equals("0"))
			{
				medida = Constantes.UNIDADES;
			}
		}
		
		db.cerrar();
		
		return medida;
	}
	
	/**
	 * Dado un codigo de articulo, obtiene si es congelado o no
	 * 
	 * @param codArticulo
	 * @return la medida para la linea de pedido.
	 */
	private boolean consultaCongeladoArticuloEnBD(String codArticulo)
	{
		boolean esCongelado = false;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorArticulos = null;
		
		db.abrir();
		
		cursorArticulos = db.obtenerArticulo(codArticulo);
		
		if (cursorArticulos.moveToFirst())
		{
			if (cursorArticulos.getString(TablaArticulo.POS_CAMPO_ES_CONGELADO).equals("1"))
			{
				esCongelado = true;
			}
		}
		
		db.cerrar();
		
		return esCongelado;
	}
	
	/**
	 * Dado un codigo de articulo, obtiene su tarifa por defecto para la linea de pedidos
	 * 
	 * @param codArticulo
	 * @return La tarifa por defecto
	 */
	private float consultaTarifaDefectoArticuloEnBD(String codArticulo)
	{
		float tarifaDefecto = 0;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorArticulos = null;
		
		db.abrir();
		
		cursorArticulos = db.obtenerArticulo(codArticulo);
		
		if (cursorArticulos.moveToFirst())
		{
			tarifaDefecto = cursorArticulos.getFloat(TablaArticulo.POS_CAMPO_TARIFA_DEFECTO);
		}
		
		db.cerrar();
		
		return tarifaDefecto;
	}
	
	/**
	 * Dado un codigo de articulo, obtiene su fecha de cambio de tarifa por defecto para la linea de pedidos
	 * 
	 * @param codArticulo
	 * @return La fecha de cambio de tarifa por defecto
	 */
	private String consultaFechaCambioTarifaDefectoArticuloEnBD(String codArticulo)
	{
		String fechaCambioTarifaDefecto = null;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorArticulos = null;
		
		db.abrir();
		
		cursorArticulos = db.obtenerArticulo(codArticulo);
		
		if (cursorArticulos.moveToFirst())
		{	
			fechaCambioTarifaDefecto = cursorArticulos.getString(TablaArticulo.POS_CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO);
		}
		
		db.cerrar();
		
		return fechaCambioTarifaDefecto;
	}
	
	/**
	 * Obtiene los articulos que no estan en el rutero del cliente y que por tanto pueden ser incluidos en el mismo.
	 * 
	 * @param idCliente
	 * 
	 * @return Vector de String[] con los datos de los articulos, cada elemento es un array de String de 2 posiciones, en la 0 se guarda el codigo del
	 * articulo y en la 1 la descripcion del mismo.
	 */
	private Vector<String[]> dameArticulosParaRuteroBD(int idCliente)
	{
		Vector <String[]> articulosObtenidos = new Vector<String[]>();
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorArticulos = null;
		String [] datosArticulo = null;
		
		//Primero lo inicializamos a vacio
		datosArticulo = new String[2];
		datosArticulo[0] = "";
		datosArticulo[1] = "";
		articulosObtenidos.add(datosArticulo);
		
		db.abrir();
		
		cursorArticulos = db.obtenerArticulosNoEnRuteroCliente(idCliente);
		
		if (cursorArticulos.moveToFirst())
		{
			do 
			{								
				datosArticulo = new String[2];
				datosArticulo[0] = cursorArticulos.getString(TablaArticulo.POS_KEY_CAMPO_CODIGO);
				datosArticulo[1] = cursorArticulos.getString(TablaArticulo.POS_CAMPO_NOMBRE);		
				
				//Si el articulo no esta en la pantalla de rutero, damos la posibilidad de que sea incluido
				if (!estaArticuloYaEnPantallaRutero(datosArticulo[0]))
				{
					articulosObtenidos.add(datosArticulo);
				}
				
			} while (cursorArticulos.moveToNext());
		}
		
		db.cerrar();
		
		return articulosObtenidos;
	}
	
	/**
	 * Comprube si el articulo esta ya en la pantalla de rutero.
	 * 
	 * @param codArticulo
	 * @return true si el articulo ya esta en la pantalla de rutero o false en cualquier otro caso
	 */
	private boolean estaArticuloYaEnPantallaRutero(String codArticulo)
	{
		boolean esta = false;
		
		for (int i=0; i<this.articulosYaEnRutero.length; i++)
		{
			if (this.articulosYaEnRutero[i].equals(codArticulo))
			{
				esta = true;
				break;
			}
		}
		
		return esta;
	}
	
	/**
	 * Busca el nombre de un articulo dada su referencia.
	 * 
	 * @param referencia del articulo a buscar
	 * 
	 * @return el nombre del articulo o null si no hay ningun articulo para la referencia dada
	 */
	private String dameNombreArticuloSegunReferencia(String referencia)
	{
		String nombreArticulo = null;
		
		
		if (!referencia.trim().equals(""))
		{
			for (int i=0; i<this.vectorDatosArticulos.size(); i++)
			{
				if (this.vectorDatosArticulos.elementAt(i)[0].equals(referencia))
				{
					nombreArticulo = this.vectorDatosArticulos.elementAt(i)[1];
					break;
				}
			}
		}
		
		return nombreArticulo;
	}
	
	/**
	 * Busca la referencia de un articulo dado su nombre.
	 * 
	 * @param nombre del articulo a buscar
	 * 
	 * @return la referencia del articulo o null si no hay ningun articulo para el nombre dado
	 */
	private String dameReferenciaArticuloSegunNombre(String nombre)
	{
		String referenciaArticulo = null;
		
		if (!nombre.trim().equals(""))
		{
			for (int i=0; i<this.vectorDatosArticulos.size(); i++)
			{
				if (this.vectorDatosArticulos.elementAt(i)[1].equals(nombre))
				{
					referenciaArticulo = this.vectorDatosArticulos.elementAt(i)[0];
					break;
				}
			}
		}
		
		return referenciaArticulo;
	}
	
	/**
	 * Devuelve un objeto ArrayList<String> con los articulos, QUE NO ESTAN EN EL RUTERO y que el usuario puede incluir.
	 * 
	 * Vector con los nombres de los articulos, cada elemento es un array de String en la posicion 0 se almacena el codigo del articulo y en la 1 la descripcion
	 * del mismo
	 * 
	 * @return ArrayList con la lista de articulos que el usuario puede añadir
	 */
	private ArrayList<String> dameArticulos(Vector<String[]> articulos)
	{
		this.articuloArrayList = new ArrayList<String>();
		
		for (int i=0; i<articulos.size(); i++)
		{
			this.articuloArrayList.add(articulos.elementAt(i)[1]);			
		}
		
		//Ordenamos el array list
		Collections.sort(this.articuloArrayList);
						
		return this.articuloArrayList;
	}
	
	/**
	 * Devuelve un objeto ArrayList<String> con las referencias de los articulos, QUE NO ESTAN EN EL RUTERO y que el usuario puede incluir.
	 * 
	 * Vector con los datos de los articulos, cada elemento es un array de String en la posicion 0 se almacena el codigo del articulo y en la 1 la descripcion
	 * del mismo
	 * 
	 * @return ArrayList con la referencia de los articulos que el usuario puede añadir
	 */
	private ArrayList<String> dameReferencias(Vector<String[]> articulos)
	{
		this.referenciasArrayList = new ArrayList<String>();
		
		for (int i=0; i<articulos.size(); i++)
		{
			this.referenciasArrayList.add(articulos.elementAt(i)[0]);			
		}
		
		//Ordenamos el array list
		Collections.sort(this.referenciasArrayList);
						
		return this.referenciasArrayList;
	}
	
	/**
	 * Comprueba si el articulo pasado como parametro, es uno de los articulos de la lista.
	 * 
	 * @param articulo a comprobar si pertenece a la lista.
	 * @return true si el articulo pertenece a la lista de articulos o false en caso contrario.
	 */
	private boolean esArticuloValido(String cliente)
	{
		return this.articuloArrayList.contains(cliente);
	}
		
	/**
	 * - Devuelve a la activity que lo solicito los datos de la nueva linea de pedido, introducidos por el usuario.
	 * - Indica que le ejecución de la activity ha sido OK.
	 * - Finaliza la activity.
	 */
	private void returnDatosDNAR()
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_OK;

		intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaRutero.class);
		intent.putExtra("DATOS_LINEA_PEDIDO", this.datosLineaPedido);

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
}