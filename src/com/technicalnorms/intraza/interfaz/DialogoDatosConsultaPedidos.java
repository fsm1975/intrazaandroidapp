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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.interfaz.datos.DatosConsultaPedidos;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaCliente;
import com.technicalnorms.intraza.interfaz.datosBD.TablaPrepedido;
import com.technicalnorms.intraza.R;

/**
 * Activity que solicita al usuario los datos para consultar los pedidos.
 * 
 * @author JLZS
 *
 */
public class DialogoDatosConsultaPedidos extends Activity
{	
	//Almacena los datos de la consulta
	private DatosConsultaPedidos datosConsultaPedidos = null;
	
	//Widgeds que contienen los datos que debe introducir el usuario para el nuevo pedido
	private AutoCompleteTextView clienteView = null;
	private AutoCompleteTextView idPedidoView = null;
	private Spinner spinnerCliente = null;
	
	//Almacena los clientes e idPedidos que hay en la BD y que por tanto puede indicar el usuario
	private ArrayList<String> clienteArrayList = null;
	private ArrayList<String> idPedidoArrayList = null;
	
	//Indica si el subdialo se invoco desde la activity principal es decir, desde el menu principal de la aplicacion
	private boolean invocacionDesdeActiviyPrincipal = false;
	
	//Almacena en cada posición un array de String de 2 elementos, guardaremos en la posicion 0 el id del cliente, en la 1 el nombre del cliente, 
	//el orden corresponde al de clienteArrayList, asi cuando el usuario selecciona un cliente del ArrayList, tenemos sus datos y nos evitamos
	//una consulta a la BD
	private Vector<String[]> vectorDatosClientes = null;
	
	//Almacena la posicion del elemento de vectorDatosClientes que esta seleccionado, 0 es TODOS
	int posClienteSeleccionado = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button consultarBtn = null;
		Button cancelarBtn = null;
		Button abreSpinnerBtn = null;
		ArrayAdapter<String> adapter = null;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_datos_consulta_pedidos);
		
		//Inicializamos los miembros
		this.vectorDatosClientes = new Vector<String[]>();
		
		//Comprobamos desde donde se solicito el subdialogo
		this.invocacionDesdeActiviyPrincipal = this.getIntent().getExtras().getBoolean("ACTIVITY_PRINCIPAL");
		
		//Inicializarmos los arrayList a utlizar en el ArrayAdapter
		inicializaDatosClientesBD();
		inicializaIdPedidosBD();
		
		//Se obtienen los datos para los TextView AutoComplete y Spinner	    
	    adapter = new ArrayAdapter<String>(this, R.layout.list_item, this.clienteArrayList);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    this.spinnerCliente = (Spinner)findViewById(R.id.spinnerInvisibleClienteDCP);
	    this.spinnerCliente.setAdapter(adapter);
	    this.spinnerCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
	    	{
	    		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id)
	    		{
	    			posClienteSeleccionado = pos;
	    			
	    			if (parent.getItemAtPosition(posClienteSeleccionado).toString().equals(Constantes.SPINNER_NINGUNO))
	    			{
	    				clienteView.setText("");
	    			}
	    			else
	    			{
	    				clienteView.setText(parent.getItemAtPosition(posClienteSeleccionado).toString());
	    			}
	    		}
	    	 
	    		@Override
	    		public void onNothingSelected(AdapterView<?> arg0)
	    		{
	    		}
	    	});
	    
		adapter = new ArrayAdapter<String>(this, R.layout.list_item, this.clienteArrayList);
		this.clienteView = (AutoCompleteTextView)findViewById(R.id.clienteDCP);
	    this.clienteView.setAdapter(adapter);
	    this.clienteView.addTextChangedListener(new TextWatcher() {  
	    	
	    	/*
	    	 * Cuando tengamos un cliente valido actualizamos la posicion del spinner
	    	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	    	 */
	    	public void afterTextChanged(Editable s)  
	    	{  
	    		posClienteSeleccionado = damePosDatosCliente(clienteView.getText().toString().trim());
	    	}  
	    	  
	    	public void beforeTextChanged(CharSequence s, int start, int count, int after)  
	    	{  
	    	}  
	    	  
	    	public void onTextChanged(CharSequence s, int start, int before, int count)  
	    	{  
	    	}  
	    });
	    
		this.idPedidoView = (AutoCompleteTextView)findViewById(R.id.idPedidoDCP);
		adapter = new ArrayAdapter<String>(this, R.layout.list_item, this.idPedidoArrayList);
	    this.idPedidoView.setAdapter(adapter);
	    
		//*******
		//BOTONES
		//*******
		
		consultarBtn = (Button)findViewById(R.id.consultarBotonDialogoDCP);
		consultarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				int idPedido = 0;
				int idCliente = 0;
				String nombreCliente = null;
				
				/*
				//Comprobamos que el usuario haya introducido alguno de los datos solicitados para la consulta
				if (clienteView.getText().toString().trim().equals("") && idPedidoView.getText().toString().trim().equals(""))
				{
					toastMensajeError(Constantes.AVISO_DATOS_DCP_NINGUN_DATO);
				}	
				//Comprobamos que el usuario solo ha introducido uno de los datos solicitados para la consulta
				else if (!clienteView.getText().toString().trim().equals("") && !idPedidoView.getText().toString().trim().equals(""))
				{
					toastMensajeError(Constantes.AVISO_DATOS_DCP_SOLO_UN_DATO);
				}
				//Comprobamos si el cliente seleccionado es valido
				else if (!clienteView.getText().toString().trim().equals("") && !esClienteValido(clienteView.getText().toString().trim()))
				*/
				//Comprobamos si el cliente seleccionado es valido
				if (!clienteView.getText().toString().trim().equals("") && !esClienteValido(clienteView.getText().toString().trim()))
				{
					toastMensajeError(Constantes.AVISO_DATOS_DCP_CLIENTE_NO_VALIDO);
				}
				//Comprobamos que el id. pedido seleccionado es valido
				else if (!idPedidoView.getText().toString().trim().equals("") && !esIdPedidoValido(idPedidoView.getText().toString().trim()))
				{
					toastMensajeError(Constantes.AVISO_DATOS_DCP_ID_PEDIDO_NO_VALIDO);
				}
				else
				{
					if (!idPedidoView.getText().toString().trim().equals(""))
					{
						idPedido = Integer.parseInt(idPedidoView.getText().toString().trim());
						
					}
					
					if (vectorDatosClientes.elementAt(posClienteSeleccionado)!=null)
					{
						idCliente = Integer.parseInt(vectorDatosClientes.elementAt(posClienteSeleccionado)[0]);
					}
					
					nombreCliente = clienteView.getText().toString().trim();
					
					
					datosConsultaPedidos = new DatosConsultaPedidos(idPedido, idCliente, nombreCliente);
					
					returnDatosDCP();
				}
			}
		});
		
		cancelarBtn = (Button)findViewById(R.id.cancelarBotonDialogoDCP);
		cancelarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				cancelarDialogo();
			}
		});
		
		abreSpinnerBtn = (Button)findViewById(R.id.botonAbreSpinnerDCP);
		abreSpinnerBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{				
			    spinnerCliente.performClick();
			}
		});
	}
	
	/**
	 * Dado un nombre de cliente obtiene la posicion que ocupan sus datos en el vector de datos de clientes, esta posicion corresponde con la posición
	 * en el spinner
	 * 
	 * @param nombreCliente
	 * @return array de String con los datos del cliente (id, nombre, observaciones)
	 */
	private int damePosDatosCliente(String nombreCliente)
	{
		int posDatosCliente = 0;
		
		//Recorremos los datos de los clientes para obtener el cliente buscado
		for (int i=0; i<vectorDatosClientes.size(); i++)
		{
			if (vectorDatosClientes.elementAt(i)[1].equals(clienteView.getText().toString().trim()))
			{
				posDatosCliente = i;
				break;
			}
		}
		
		return posDatosCliente;
	}
	
	/**
	 * Devuelve un ArrayList<String> con los clientes.
	 * 
	 * @return ArrayList con la lista de clientes
	 */
	private ArrayList<String> inicializaDatosClientesBD()
	{
		this.clienteArrayList = new ArrayList<String>();		
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorClientes = null;
		String [] datosCliente = null;

		//Consultamos todos los clientes y metemos cada uno de ellos en el ArrayList, tambien guardamos sus datos pues los necesitaremos posteriormente
		db.abrir();	
		
		cursorClientes = db.obtenerTodosLosClientes();
		
		if (cursorClientes.moveToFirst())
		{
			// Primero incluimos las opciones de "ninguno" y "TODOS", por defecto se seleccionan todos los clientes
			this.clienteArrayList.add(Constantes.SPINNER_TODO);
			datosCliente = new String[2];
			datosCliente[0] = "11111"; //Este valor nos da igual nunca se va ha utilizar
			datosCliente[1] = Constantes.SPINNER_TODO;
			this.vectorDatosClientes.add(datosCliente);
			
			/*
			this.clienteArrayList.add(Constantes.SPINNER_NINGUNO);
			datosCliente = new String[2];
			datosCliente[0] = "22222"; //Este valor nos da igual nunca se va ha utilizar
			datosCliente[1] = Constantes.SPINNER_NINGUNO;
			this.vectorDatosClientes.add(datosCliente);
			*/
			
			do 
			{				
				this.clienteArrayList.add(cursorClientes.getString(TablaCliente.POS_CAMPO_NOMBRE_CLIENTE));		
				
				datosCliente = new String[2];
				datosCliente[0] = cursorClientes.getString(TablaCliente.POS_KEY_CAMPO_ID_CLIENTE);
				datosCliente[1] = cursorClientes.getString(TablaCliente.POS_CAMPO_NOMBRE_CLIENTE);
				this.vectorDatosClientes.add(datosCliente);
				
			} while (cursorClientes.moveToNext());
		}
		
		db.cerrar();
		
		return this.clienteArrayList;
	}
	
	/**
	 * Devuelve un objeto Set<String> con los id. pedidos de la BD
	 * 
	 * @return ArrayList con la lista de id. pedidos
	 */
	private ArrayList<String> inicializaIdPedidosBD()
	{
		this.idPedidoArrayList = new ArrayList<String>();
				
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorPedidos = null;

		//Consultamos todos los prepedidos y metemos cada uno de ellos en el ArrayList
		db.abrir();	
		
		cursorPedidos = db.obtenerTodosLosPrepedidos();
		
		if (cursorPedidos.moveToFirst())
		{			
			do 
			{				
				this.idPedidoArrayList.add(cursorPedidos.getString(TablaPrepedido.POS_KEY_CAMPO_ID_PREPEDIDO));				
			} while (cursorPedidos.moveToNext());
		}
		
		//Ordenamos el array list
		Collections.sort(this.idPedidoArrayList);
		
		db.cerrar();
		
		return this.idPedidoArrayList;
	}
	
	/**
	 * Comprueba si el cliente pasado como parametro, es uno de los clientes de la lista.
	 * 
	 * @param cliente a comprobar si pertenece a la lista.
	 * @return true si el cliente pertenece a la lista de clientes o false en caso contrario.
	 */
	private boolean esClienteValido(String cliente)
	{
		return this.clienteArrayList.contains(cliente);
	}
	
	/**
	 * Comprueba si el id. pedido pasado como parametro, es uno de los id. pedidos de la lista.
	 * 
	 * @param id. pedido a comprobar si pertenece a la lista.
	 * @return true si el id. pedido pertenece a la lista de pedidos o false en caso contrario.
	 */
	private boolean esIdPedidoValido(String cliente)
	{
		return this.idPedidoArrayList.contains(cliente);
	}
	
	/**
	 * - Devuelve a la activity que lo solicito los datos de consulta para el nuevo pedido, introducidos por el usuario.
	 * - Indica que le ejecución de la activity ha sido OK.
	 * - Finaliza la activity.
	 */
	private void returnDatosDCP()
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
			intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaListaPedidos.class);
		}

		intent.putExtra("DATOS_CONSULTA_PEDIDOS", this.datosConsultaPedidos);
			
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