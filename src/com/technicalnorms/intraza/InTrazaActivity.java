package com.technicalnorms.intraza;

import com.technicalnorms.intraza.interfaz.datos.DatosConsultaPedidos;
import com.technicalnorms.intraza.interfaz.datos.DatosPedido;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaObservacion;
import com.technicalnorms.intraza.task.SubdialogoProgresoSincronizacion;
import com.technicalnorms.intraza.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Activity principal.
 * 
 * @author JLZS
 *
 */
public class InTrazaActivity extends Activity 
{
	// Codigos de los subdialogos que se usan en la Activity
	private static final int DIALOGO_PIDE_DATOS_NUEVO_PEDIDO = 0;
	private static final int PANTALLA_RUTERO = 1;
	private static final int DIALOGO_PIDE_DATOS_CONSULTA_PEDIDOS = 2;
	private static final int PANTALLA_LISTA_PEDIDOS = 3;
	private static final int PANTALLA_CONFIRMACION_SINCRONIZAR = 4;
	
	/** Se invoca cuando la Activity es creada la primera vez */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Definimos los eventos onclick de los botones de la pantalla principal
		((Button)findViewById(R.id.botonMainRutero)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				
				subdialogoDatosNuevoPedido();
			}
		});
		
		((Button)findViewById(R.id.botonMainPedidos)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				
				subdialogoDatosConsultaPedidos();
			}
		});
		
		((Button)findViewById(R.id.botonMainSincronizar)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				
				subdialogoConfirmacionSincronizar();
			}
		});
		
		((Button)findViewById(R.id.botonMainAcercaDe)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				
				subdialogoAcercaDe();
			}
		});
		
		//Crea datos de prueba en la BD
		//creaDatosPruebaBD();
    }
	
	private void creaDatosPruebaBD()
	{
		AdaptadorBD db = new AdaptadorBD(this);
		db.abrir();
		
		muestraMensaje("Resultado insert articulo 1 "+db.insertarArticulo("1111111111", "Chuletas de cerdo", true, false, (float)8.10, "22-05-2012"));
		muestraMensaje("Resultado insert articulo 2 "+db.insertarArticulo("2222222221", "Chuletas de lechal", false, false, (float)8.10, "22-05-2012"));
		muestraMensaje("Resultado insert articulo 3 "+db.insertarArticulo("2222222223", "Muslos de pollo", false, true, (float)8.10, "22-05-2012"));
		muestraMensaje("Resultado insert articulo 4 "+db.insertarArticulo("2222222224", "Jamon Iberico", false, false, (float)8.10, "22-05-2012"));
		
		muestraMensaje("Resultado insert cliente 1 "+db.insertarCliente(1, "Mercadona", "Observaciones Mercadona"));
		muestraMensaje("Resultado insert cliente 2 "+db.insertarCliente(2, "Carrefour", "Observaciones Carrefour"));
		muestraMensaje("Resultado insert cliente 3 "+db.insertarCliente(3, "Restaurante Perez", "Observaciones Perez"));

		//muestraMensaje("Resultado insert prepedido 1 "+db.insertarPrepedido(1, 1, "2012-06-02", "2012-06-20", "Observaciones prepedido 1"));
		//muestraMensaje("Resultado insert prepedido 2 "+db.insertarPrepedido(2, 2, "2012-06-25", "2012-07-12", "Observaciones prepedido 2"));
		
		//muestraMensaje("Resultado insert prepedido item 1 "+db.insertarPrepedidoItem(1, 1, "1111111111", true, (float)45.323, (float)4.6, "Observaciones prepedido item 1", false, false));
		//muestraMensaje("Resultado insert prepedido item 2 "+db.insertarPrepedidoItem(2, 1, "2222222221", true, (float)123.350, (float)6.7, "Observaciones prepedido item 2", true, true));
		
		muestraMensaje("Resultado insert Rutero 1 "+db.insertarRutero("1111111111", 1, "22-05-2012", (float)232.456, (float)444, (float)8.25, (float)8.10, "Observaciones rutero 1"));
		muestraMensaje("Resultado insert Rutero 2 "+db.insertarRutero("2222222221", 1, "12-05-2012", (float)32, (float)444, (float)4.25, (float)3.10, "Observaciones rutero 2"));
		
		muestraMensaje("Resultado insert Rutero 3 "+db.insertarRutero("1111111111", 2, "22-05-2012", (float)232.456, (float)444, (float)8.25, (float)8.10, "Observaciones rutero 1"));
		muestraMensaje("Resultado insert Rutero 4 "+db.insertarRutero("2222222221", 2, "12-05-2012", (float)32, (float)444, (float)4.25, (float)3.10, "Observaciones rutero 2"));

		muestraMensaje("Resultado insert observacion 1 "+db.insertarObservacion(1, TablaObservacion.TIPO_PREPEDIDO, "En paquetes de 10."));
		muestraMensaje("Resultado insert observacion 2 "+db.insertarObservacion(2, TablaObservacion.TIPO_PREPEDIDO, "En paquetes de 50."));
		muestraMensaje("Resultado insert observacion 3 "+db.insertarObservacion(3, TablaObservacion.TIPO_PREPEDIDO_ITEM, "En paquetitos de 30."));
		muestraMensaje("Resultado insert observacion 4 "+db.insertarObservacion(4, TablaObservacion.TIPO_PREPEDIDO_ITEM, "En paquetitos de 70."));
		
		db.cerrar();
	}
	
	/**
	 * Deshabilita o no, todo los eventos onClick de la activity para evitar ejecutar dos click seguidos
	 * @param deshabilita
	 */
	private void habilitaClickEnActivity(boolean habilita)
	{
		((Button)findViewById(R.id.botonMainRutero)).setClickable(habilita);
		((Button)findViewById(R.id.botonMainPedidos)).setClickable(habilita);
		((Button)findViewById(R.id.botonMainSincronizar)).setClickable(habilita);
		((Button)findViewById(R.id.botonMainAcercaDe)).setClickable(habilita);
	}
	
	private void subdialogoDatosNuevoPedido()
	{
		Bundle bundle = null;
		Intent intent = null;
		
		//Para indicar al subdialogo que la peticion se esta haciendo desde la activity principal
		bundle = new Bundle();
		bundle.putBoolean("ACTIVITY_PRINCIPAL", true);
		
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoDatosPedido.class);
		intent.putExtras(bundle);
		
		startActivityForResult(intent, DIALOGO_PIDE_DATOS_NUEVO_PEDIDO);
	}
	
	private void pantallaRutero(DatosPedido datosPedido)
	{
		Intent intent = null;

		intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaRutero.class);
		//Pasamos los datos del pedido, a la Activity
		intent.putExtra("DATOS_PEDIDO", datosPedido);
			
		startActivityForResult(intent, PANTALLA_RUTERO);
	}
	
	private void subdialogoDatosConsultaPedidos()
	{
		Intent intent = null;
		Bundle bundle = null;
		
		//Para indicar al subdialogo que la peticion se esta haciendo desde la activity principal
		bundle = new Bundle();
		bundle.putBoolean("ACTIVITY_PRINCIPAL", true);	
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoDatosConsultaPedidos.class);
		intent.putExtras(bundle);
		
		startActivityForResult(intent, DIALOGO_PIDE_DATOS_CONSULTA_PEDIDOS);
	}
	
	private void pantallaListaPedidos(DatosConsultaPedidos datosConsultaPedidos)
	{
		Intent intent = null;

		intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaListaPedidos.class);
		//Pasamos los datos de la consulta pedido, a la Activity
		intent.putExtra("DATOS_CONSULTA_PEDIDOS", datosConsultaPedidos);
			
		startActivityForResult(intent, PANTALLA_LISTA_PEDIDOS);
	}
	
	private void subdialogoConfirmacionSincronizar()
	{
		Intent intent = null;
		boolean permitidaSincronizacion = true;
		
		//Si no esta permitida la sincronizacion si hay lineas de pedido pendientes, lo comprobamos
		if (!Configuracion.estaPermitidoSincronizacionConPedidosPendientes(this))
		{
			if (hayPedidosPendientes())
			{			
				permitidaSincronizacion = false;
				
				AlertDialog.Builder popup=new AlertDialog.Builder(this);
				popup.setTitle(Constantes.TITULO_ALERT_ERROR_NO_SINCRONIZAR_PEDIDOS_PENDIENTES);
				popup.setMessage(Constantes.MENSAJE_ALERT_ERROR_NO_SINCRONIZAR_PEDIDOS_PENDIENTES);
				popup.setPositiveButton("ACEPTAR", null);
				popup.show();
			}
		}
	
		if (permitidaSincronizacion)
		{
			intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoConfirmacionSincronizar.class);
			
			startActivityForResult(intent, PANTALLA_CONFIRMACION_SINCRONIZAR);
		}
		else
		{
			//Habilita los eventos onClick de la activity
			habilitaClickEnActivity(true);
		}
	}
	
	/**
	 * Comprueba mediante una consulta a la BD si existen pedidos pendientes de enviar a InTraza.
	 * 
	 * @return true en caso de existir pedido pendiente, false en cualquier otro caso
	 */
	private boolean hayPedidosPendientes()
	{
		boolean hayPendientes = false;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorPedidos = null;
		
		db.abrir();		
		cursorPedidos = db.obtenerTodosLosPrepedidos();
			
		if (cursorPedidos.getCount()>0)
		{
			hayPendientes = true;
		}
			
		db.cerrar();
		
		return hayPendientes;
	}
	
	private void subdialogoAcercaDe()
	{
		Intent intent = null;

		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoAcercaDe.class);
			
		startActivity(intent);
		
		//Habilita los eventos onClick de la activity
		habilitaClickEnActivity(true);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) 
		{
			case (DIALOGO_PIDE_DATOS_NUEVO_PEDIDO): 
			{
				//Cuando el usuario pulsa el boton ACEPTAR ya podemos crear el rutero para el pedido
				if (resultCode == Activity.RESULT_OK) 
				{
					//Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_CARGAR_RUTERO, Toast.LENGTH_SHORT).show();
					
					//Obtenemos los datos del pedido
					DatosPedido datosPedido = (DatosPedido) data.getParcelableExtra("DATOS_PEDIDO");
					
					pantallaRutero(datosPedido);
				}
								
				break;
			}
			
			case (DIALOGO_PIDE_DATOS_CONSULTA_PEDIDOS): 
			{
				//Cuando el usuario pulsa el boton CONSULTAR hacemos la consulta de los pedidos y los mostramos en pantalla al usuario
				if (resultCode == Activity.RESULT_OK) 
				{
					//Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_CONSULTANDO_PEDIDOS, Toast.LENGTH_SHORT).show();
					
					//Obtenemos los datos de la consulta
					DatosConsultaPedidos datosConsultaPedidos = (DatosConsultaPedidos) data.getParcelableExtra("DATOS_CONSULTA_PEDIDOS");
				
					pantallaListaPedidos(datosConsultaPedidos);
				}
								
				break;
			}
			
			case (PANTALLA_CONFIRMACION_SINCRONIZAR): 
			{
				//Cuando el usuario pulsa el boton SINCRONIZAR hacemos la sincronizacion con los datos de InTraza
				if (resultCode == Activity.RESULT_OK) 
				{
					SubdialogoProgresoSincronizacion progreso = new SubdialogoProgresoSincronizacion(this);
					progreso.execute();
				}
								
				break;
			}
		}
		
		//Habilita los eventos onClick de la activity
		habilitaClickEnActivity(true);
	}
	
	private void muestraMensaje(String mensaje)
	{
		Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
	}
}