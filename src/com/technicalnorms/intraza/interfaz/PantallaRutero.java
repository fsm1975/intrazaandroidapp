package com.technicalnorms.intraza.interfaz;

import com.technicalnorms.intraza.Configuracion;
import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.interfaz.datos.DatosLineaPedido;
import com.technicalnorms.intraza.interfaz.datos.DatosPedido;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.LineaPedidoBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaArticulo;
import com.technicalnorms.intraza.interfaz.datosBD.TablaCliente;
import com.technicalnorms.intraza.interfaz.datosBD.TablaPrepedido;
import com.technicalnorms.intraza.interfaz.datosBD.TablaPrepedidoItem;
import com.technicalnorms.intraza.interfaz.datosBD.TablaRutero;
import com.technicalnorms.intraza.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.ScrollView;

import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

public class PantallaRutero extends Activity
{
	//Codigos de los subdialogos que se usan en la Activity
	private static final int DIALOGO_PIDE_DATOS_LINEA_PEDIDO = 0;
	private static final int DIALOGO_PIDE_DATOS_NUEVO_PEDIDO = 1;
	private static final int DIALOGO_PIDE_DATOS_PEDIDO = 2;
	private static final int DIALOGO_AVISO_DATOS_SIN_GUARDAR = 3;
	private static final int DIALOGO_AVISO_DATOS_SIN_GUARDAR_TECLA_ATRAS = 4;
	private static final int DIALOGO_PIDE_DATOS_NUEVO_ARTICULO_RUTERO = 5;
	
	//Almacena los datos del pedido
	private DatosPedido datosPedido = null;

	//Contendra todos los TextView que forman la tabla de linea de pedidos de cliente y los datos de la linea de pedido, la key sera el ID de la View
	private Hashtable<Integer, Object> viewsTablaRutero = null;
	
	//El widget que forma la tabla en pantalla
	private TableLayout tablaRutero = null;
	private boolean colorFilaClaro = true;
	
	//Almacena los datos de las lineas de pedido que se reflejan en la pantalla del rutero
	private Vector<DatosLineaPedido> lineasPedido = null;
	
	//Almacena los datos de las lineas de pedido que hay en la BD, se usa para una modificiacion de pedido
	private Vector<LineaPedidoBD> lineasPedidoBD = null;
	
	//Almacena las observaciones por defecto para el pedido configuradas en intraza
	private String observacionesDefectoPedido = null;
	
	private View viewLineaRutero = null;
	
	//Indica si la pantalla se solicito para modificar o consultar los datos de un pedido
	private boolean esPantallaModificacionPedido = false;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rutero);
		
		//Inicializamos las variables miembro necesarias
		this.lineasPedido = new Vector<DatosLineaPedido>();
		this.viewsTablaRutero = new Hashtable<Integer, Object>();
		this.tablaRutero = (TableLayout)findViewById(R.id.ruteroTableLayout);
		
		//Obtenemos los datos del pedido y los ponemos en la pantalla
		this.datosPedido = this.getIntent().getParcelableExtra("DATOS_PEDIDO");
		
		//Si no hay datos de pedido, es porque esta activiy se inicio para modificar o consultar los datos de un pedido desde la pantalla de pedidos
		if (this.datosPedido == null)
		{
			//Cambiamos el color de fondo del marco, por el asociado a los pedidos
			((RelativeLayout)findViewById(R.id.marcoP)).setBackgroundColor(this.getResources().getColor(R.color.colorBotonPedidos));
			
			this.esPantallaModificacionPedido = this.getIntent().getExtras().getBoolean("PANTALLA_MODIFICACION");
			
			//consultamos en la BD los datos del pedido
			this.datosPedido = consultaDatosPedidoBD(this.getIntent().getExtras().getInt("ID_PEDIDO"));
		}
		
		cargaDatosPedido();
		
		///////////////////////////////////////////
		//Definimos los eventos onClick del pedido
		///////////////////////////////////////////
		
		((Button)findViewById(R.id.botonFechaEntregaP)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				subdialogoDatosPedido();
			}
		});
		
		//Si estamos en una pantalla de modificacion de pedido, no permitimos cambiar de cliente
		if (this.esPantallaModificacionPedido)
		{
			((Button)findViewById(R.id.botonClienteP)).setEnabled(false);
			((Button)findViewById(R.id.botonClienteP)).setTextColor(this.getResources().getColor(R.color.colorTextoBotonDeshabilitadoTablaP));
		}
		else
		{
			((Button)findViewById(R.id.botonClienteP)).setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{	
					habilitaClickEnActivity(false);
					
					// Comprueba si hay que mostrar un aviso al usuario de perdida de informacion al cargar el nuevo rutero. Este se mostrara cuando tengamos 
					// datos sin guardar en el pedido y contenga lineas de pedido, ya que no tiene sentido guardar un pedido sin lineas de pedido.
					if (datosPedido.hayDatosPedidoSinGuardar() && datosPedido.hayLineasPedido())
					{
						subdialogoAvisoDatosSinGuardar();
					}
					else
					{
						subdialogoDatosNuevoPedido();
					}
				}
		});
		}
		
		((Button)findViewById(R.id.botonObservacionesP)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				
				subdialogoDatosPedido();
			}
		});
		
		((ImageButton)findViewById(R.id.botonGuardarP)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				
				guardarDatosPedidoBD();
				
				habilitaClickEnActivity(true);
			}
		});
		
		((ToggleButton)findViewById(R.id.botonDescuentoEspecialP)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				//Si tenemos lineas de pedido, habilitamos el boton
				if (datosPedido.hayLineasPedido())
				{
					habilitaBotonGuardar();
				}
			}
		});
			
		((ToggleButton)findViewById(R.id.botonOcultarRuteroP)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				//Coprueba si hay que ocultar o mostrar las lineas de rutero en la pantalla
				compruebaBotonOcultarRutero(v);
			}
		});
		
		((Button)findViewById(R.id.botonNuevoArticuloP)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				
				subdialogoDatosNuevoArticuloRutero();
			}
		});
		
		//Cargamos el rutero
		cargaRuteroEnPantalla(this.datosPedido.getIdCliente());
	}	
	
	/**
	 * Deshabilita o no, todo los eventos onClick de la activity para evitar ejecutar dos click seguidos
	 * @param deshabilita
	 */
	private void habilitaClickEnActivity(boolean habilita)
	{
		((Button)findViewById(R.id.botonFechaEntregaP)).setClickable(habilita);
		((Button)findViewById(R.id.botonClienteP)).setClickable(habilita);
		((Button)findViewById(R.id.botonObservacionesP)).setClickable(habilita);
		((ImageButton)findViewById(R.id.botonGuardarP)).setClickable(habilita);
		((Button)findViewById(R.id.botonNuevoArticuloP)).setClickable(habilita);
		
		for (int i=1; i<=this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP; i++)
		{
			((TextView)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_ARTICULO_LP)).setClickable(habilita);
			((TextView)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_CANTIDAD_NUEVO_KG_LP)).setClickable(habilita);
			((TextView)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_CANTIDAD_NUEVO_UD_LP)).setClickable(habilita);
			((TextView)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_TARIFA_NUEVO_LP)).setClickable(habilita);
		}
	}
	
	/**
	 * Cuando se pulse la tecla "atras", si hay cambios pendientes el usuario debe indicar si quiere o no guardarlos
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// Comprueba si hay que mostrar un aviso al usuario de perdida de informacion al salir de la pantalla. Este se mostrara cuando tengamos 
		// datos sin guardar en el pedido y contenga lineas de pedido, ya que no tiene sentido guardar un pedido sin lineas de pedido.
		if (keyCode == KeyEvent.KEYCODE_BACK && datosPedido.hayDatosPedidoSinGuardar() && datosPedido.hayLineasPedido()) 
		{
	    	subdialogoAvisoDatosSinGuardarTeclaAtras();
				
	    	return true;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	/**
	 * Guarda en la BD de la tablet los datos del pedido y las lineas de pedido, pendientes de guardar.
	 */
	private void guardarDatosPedidoBD()
	{
		boolean hayError = false;
		AdaptadorBD db = new AdaptadorBD(this);
		int descuentoEspecial = 0;
		
		db.abrir();
		
		if (((ToggleButton)findViewById(R.id.botonDescuentoEspecialP)).isChecked())
		{
			descuentoEspecial = Constantes.DESCUENTO_ESPECIAL;
		}
		
		// Guardamos los datos generales del pedido
		hayError = !db.guardaPrepedido(this.datosPedido.getIdPedido(), this.datosPedido.getIdCliente(), 
									  this.datosPedido.getFechaEntrega(), this.datosPedido.getFechaPedido(), 
									  this.datosPedido.getObservaciones(), this.datosPedido.getFijarObservaciones(), descuentoEspecial);
		
		if (!hayError)
		{
			// Recorremos las lineas de pedido para guardar en la BD aquellas que tengan algun cambio pendiente de guardar
			for (int i=1; i<=this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP; i++)
			{	
				DatosLineaPedido datosLineaPedido = (DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_LP);
				
				if (datosLineaPedido.hayCambiosSinGuardar())
				{
					//Si la linea de pedido tiene cantidad 0 es porque teniamos cambios de la linea de pedido que hay que borrar de la BD
					if (datosLineaPedido.getCantidadKg()==0 && datosLineaPedido.getCantidadUd()==0)
					{
						//En esta caso no controlamos que haya error, ya que si el usuario borra una linea de pedido que no estaba previamente en 
						//la BD se produciria un error
						db.borrarPrepedidoItem(this.datosPedido.getIdPedido(), datosLineaPedido.getCodArticulo());
					}
					else
					{
						hayError = !db.guardaPrepedidoItem(this.datosPedido.getIdPedido(), datosLineaPedido.getCodArticulo(),
														   datosLineaPedido.getCantidadKg(), datosLineaPedido.getCantidadUd(), 
														   datosLineaPedido.getTarifaCliente(), datosLineaPedido.getObservaciones(), 
														   datosLineaPedido.getFijarTarifa(), datosLineaPedido.getFijarArticulo(), datosLineaPedido.getFijarObservaciones());
					}
				
					if (!hayError)
					{
						//Indicamos que esa linea de pedidos no tiene cambios pendientes ya que los hemos guardado en la BD
						((DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_LP)).setHayCambiosSinGuardar(false);
					}
					else
					{
						//En caso de error dejamos de hacer cosas en la BD
						break;
					}
				}
			}
		}
		
		db.cerrar();
		
		//Segun se haya producido error o no, hacemos las acciones correspondientes
		if (!hayError)
		{
			Toast.makeText(getBaseContext(), Constantes.MENSAJE_DATOS_PEDIDO_GUARDADOS, Toast.LENGTH_SHORT).show();
			//El boton guardar se habilita solo cuando hay cambios pendientes de guardar
			inhabilitaBotonGuardar();
		}
		else
		{
			toastMensajeError(Constantes.ERROR_GUARDAR_DATOS_PEDIDO);
		}
	}
	
	/**
	 * Inhabilita el boton guardar e indica en los datos del pedido que no hay cambios pendiente de guardar
	 */
	private void inhabilitaBotonGuardar()
	{
		((ImageButton)findViewById(R.id.botonGuardarP)).setEnabled(false);
		((ImageButton)findViewById(R.id.botonGuardarP)).setImageDrawable(this.getResources().getDrawable(R.drawable.no_guardar));
	
		//Indicamos que el pedido tiene guardados todos los cambios
		this.datosPedido.setHayDatosPedidoSinGuardar(false);
	}
	
	/**
	 * Habilita el boton guardar e indica en los datos del pedido que hay cambios pendiente de guardar
	 */
	private void habilitaBotonGuardar()
	{
		((ImageButton)findViewById(R.id.botonGuardarP)).setEnabled(true);
		((ImageButton)findViewById(R.id.botonGuardarP)).setImageDrawable(this.getResources().getDrawable(R.drawable.guardar));
	
		//Indicamos que el pedido tiene guardados todos los cambios
		this.datosPedido.setHayDatosPedidoSinGuardar(true);
	}
	
	/**
	 * Comprueba si hay que ocultar o mostrar las lineas de rutero en la pantalla
	 * 
	 * @param vista del boton que indica si se debe o no ocultar las lineas de rutero
	 */
	private void compruebaBotonOcultarRutero(View v)
	{
		if (((ToggleButton)v).isChecked())
		{
			ocultarLineasRutero();
		}
		else
		{
			mostrarLineasRutero();				
		}
	}
	
	/**
	 * Oculta de la pantalla las lineas de rutero, de forma que solo se muestran las lineas de pedido
	 */
	private void ocultarLineasRutero()
	{	
		Vector<DatosLineaPedido> lineasRuteroPedido = new Vector<DatosLineaPedido>();
		Vector<DatosLineaPedido> lineasIniRuteroPedido = new Vector<DatosLineaPedido>();
		
		//Eliminamos todas las fila en pantalla
		this.tablaRutero.removeAllViews();
		
		//Obtenemos los datos de la linea de pedido que nos interesan, recorremos el rutero para obtener las lineas de pedido introducidas por el usuario, 
		//el numero de filas es el numero de elementos / numeroColumnas
		for (int i=1; i<=this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP; i++)
		{
			DatosLineaPedido datosLineaPedido = (DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_LP);
			
			if (datosLineaPedido.getCantidadKg()!=0 || datosLineaPedido.getCantidadUd()!=0)
			{
				lineasRuteroPedido.addElement(datosLineaPedido);
				lineasIniRuteroPedido.addElement((DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_INICIALES_LP));
			}
		}
		
		//Una vez obtenidos los datos limpiamos, la HashTable que guarda las view de la pantalla y los datos de la linea de pedido
		this.viewsTablaRutero.clear();
		
		//Mostramos en pantalla las lineas de pedido
		muestraRutero(lineasRuteroPedido, lineasIniRuteroPedido);
	}
	
	/**
	 * Muestra en pantalla tanto las lineas de rutero como las de pedido
	 */
	private void mostrarLineasRutero()
	{
		Vector<DatosLineaPedido> lineasRuteroPedido = new Vector<DatosLineaPedido>();
		Vector<DatosLineaPedido> lineasIniRuteroPedido = new Vector<DatosLineaPedido>();
		boolean estaLineaIncluida = false;
		
		//Eliminamos todas la filas en pantalla
		this.tablaRutero.removeAllViews();
		
		//Recorremos las lineas de pedido del rutero, cogiendo los datos de la linea del pedido, si es el caso
		for (int j=0; j<this.lineasPedido.size(); j++)
		{
			//Obtenemos los datos de la linea de pedido que nos interesan, recorremos el rutero para obtener las lineas de pedido introducidas por el usuario, 
			//el numero de filas es el numero de elementos / numeroColumnas
			estaLineaIncluida = false;
			for (int i=1; i<=this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP; i++)
			{
				DatosLineaPedido datosLineaPedido = (DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_LP);
			
				//Si la referencia del articulo es la misma, y tiene datos en la linea de pedido los cogemos
				if (this.lineasPedido.elementAt(j).getCodArticulo().equals(datosLineaPedido.getCodArticulo()) && (datosLineaPedido.getCantidadKg()!=0 || datosLineaPedido.getCantidadUd()!=0))
				{
					lineasRuteroPedido.addElement(datosLineaPedido);
					lineasIniRuteroPedido.addElement((DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_INICIALES_LP));
					
					//Indicamos que ya se ha incluido la linea en el rutero y salimos del for, pues ya hemos terminado de buscar
					estaLineaIncluida = true;
					break;
				}				
			}
			
			//Si la linea no tenia datos de pedido, incluimos los datos recuperados de la BD
			if (!estaLineaIncluida)
			{
				lineasRuteroPedido.addElement(this.lineasPedido.elementAt(j));
				lineasIniRuteroPedido.addElement(this.lineasPedido.elementAt(j));
			}
		}
		
		//Una vez obtenidos los datos limpiamos, la hashtable que guarda las view de la pantalla y los datos de la linea de pedido
		this.viewsTablaRutero.clear();
		
		//Mostramos en pantalla las lineas de pedido
		muestraRutero(lineasRuteroPedido, lineasIniRuteroPedido);
	}
		
	/**
	 * Abre el subdialogo que solicita los datos del pedido
	 */
	private void subdialogoDatosPedido()
	{
		Bundle bundle = null;
		Intent intent = null;
		
		//Para indicar al subdialogo que la peticion no se esta haciendo desde la activity principal
		bundle = new Bundle();
		bundle.putBoolean("ACTIVITY_PRINCIPAL", false);
				
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoDatosPedido.class);
		intent.putExtras(bundle);
		
		//Pasamos los datos del pedido al subdialogo
		intent.putExtra("DATOS_PEDIDO", this.datosPedido);
		//Tomamos de los datos iniciales de la linea de pedido las observaciones por defecto
		intent.putExtra("OBSERVACIONES_DEFECTO", this.observacionesDefectoPedido);
			
		startActivityForResult(intent, DIALOGO_PIDE_DATOS_PEDIDO);
	}
	
	/**
	 * Abre el subdialogo que solicita los datos del pedido
	 */
	private void subdialogoDatosNuevoPedido()
	{
		Bundle bundle = null;
		Intent intent = null;
		
		//Para indicar al subdialogo que la peticion no se esta haciendo desde la activity principal
		bundle = new Bundle();
		bundle.putBoolean("ACTIVITY_PRINCIPAL", false);
		
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoDatosPedido.class);
		intent.putExtras(bundle);
		
		startActivityForResult(intent, DIALOGO_PIDE_DATOS_NUEVO_PEDIDO);
	}
	
	/**
	 * Abre el subdialogo que solicita los datos de un articulo nuevo para al rutero
	 */
	private void subdialogoDatosNuevoArticuloRutero()
	{
		Bundle bundle = null;
		Intent intent = null;
		String[] codigosArticulosRutero = null;
		
		//Creamos un array con los articulos que ya estan en el rutero de la pantalla, para que no se puedan incluir
		codigosArticulosRutero = new String[this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP];
		for (int i=1; i<=this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP; i++)
		{	
			DatosLineaPedido datosLineaRutero = (DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_INICIALES_LP);
			codigosArticulosRutero[i-1] = datosLineaRutero.getCodArticulo();			
		}	
		
		//Para indicar al subdialogo el id cliente del rutero
		bundle = new Bundle();
		bundle.putInt("ID_CLIENTE", this.datosPedido.getIdCliente());
		bundle.putStringArray("ARRAY_ARTICULOS_EN_RUTERO", codigosArticulosRutero);
		
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoDatosNuevoArticuloRutero.class);
		intent.putExtras(bundle);
		
		startActivityForResult(intent, DIALOGO_PIDE_DATOS_NUEVO_ARTICULO_RUTERO);
	}
	
	/**
	 * Abre el subdialogo que solicita la confirmación del usuario cuando tenemos datos en el pedido sin guardar
	 */
	private void subdialogoAvisoDatosSinGuardar()
	{
		Intent intent = null;
		
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoAvisoDatosSinGuardar.class);
			
		startActivityForResult(intent, DIALOGO_AVISO_DATOS_SIN_GUARDAR);
	}
	
	/**
	 * Abre el subdialogo que solicita la confirmación del usuario cuando tenemos datos en el pedido sin guardar y se pulsa la tecla de ir atras
	 */
	private void subdialogoAvisoDatosSinGuardarTeclaAtras()
	{
		Intent intent = null;
		
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoAvisoDatosSinGuardar.class);
			
		startActivityForResult(intent, DIALOGO_AVISO_DATOS_SIN_GUARDAR_TECLA_ATRAS);
	}
	
	/**
	 * Carga los datos del pedido en la pantalla
	 */
	private void cargaDatosPedido()
	{
		boolean hayDescuentoEspecial = false;
		String clienteParaBoton = datosPedido.getCliente();
		
		((TextView)findViewById(R.id.textoFechaP)).setText(Constantes.PREFIJO_TEXTO_FECHA_PEDIDO+this.datosPedido.getFechaPedido());
		((TextView)findViewById(R.id.textoIdP)).setText(Constantes.PREFIJO_TEXTO_ID_PEDIDO+this.datosPedido.getIdPedido());
		((TextView)findViewById(R.id.textoPrecioTotalP)).setText(Constantes.PREFIJO_TEXTO_PRECIO_TOTAL+Constantes.formatearFloat2Decimales.format(this.datosPedido.getPrecioTotal())+Constantes.EURO);
		((Button)findViewById(R.id.botonFechaEntregaP)).setText(Constantes.PREFIJO_BOTON_FECHA_ENTREGA+this.datosPedido.getFechaEntrega());
		
		if (clienteParaBoton.length()>29)
		{
			clienteParaBoton = clienteParaBoton.substring(0, 29);
		}
			
		((Button)findViewById(R.id.botonClienteP)).setText(Constantes.PREFIJO_BOTON_CLIENTE+clienteParaBoton);
		
		if (this.datosPedido.getDescuentoEspecial()==Constantes.DESCUENTO_ESPECIAL)
		{
			hayDescuentoEspecial = true;
		}
		
		((ToggleButton)findViewById(R.id.botonDescuentoEspecialP)).setChecked(hayDescuentoEspecial);
	}
	
	/**
	 * Cargamos el rutero en pantalla, para ello primero consultamos los datos del cliente en la BD, para obener el rutero
	 * y luego lo mostramos en la tabla de rutero
	 */
	private void cargaRuteroEnPantalla(int idCliente)
	{
		//El boton guardar se habilita solo cuando hay cambios pendientes de guardar
		inhabilitaBotonGuardar();
		
		//Ponemos en off el boton de ocultar la lineas de rutero
		((ToggleButton)findViewById(R.id.botonOcultarRuteroP)).setChecked(false);

		//Eliminamos todas la filas en pantalla y la HashTable que contiene las view de los datos en pantalla
		this.tablaRutero.removeAllViews();
		this.viewsTablaRutero.clear();
		this.lineasPedido.clear();
		
		//Consultamos los datos del rutero en la BD y los mostramos en pantalla, actualizando en pantalla el precio total del pedido
		if (this.esPantallaModificacionPedido)
		{
			//Si es una modificacion de pedido, tenemos que inicializar el rutero con los datos de las lineas de pedido que teniamos en la BD
			inicializaRuteroConLineasPedido(consultaDatosRuteroBD(idCliente), this.lineasPedidoBD);
		}
		else
		{
			inicializaRutero(consultaDatosRuteroBD(idCliente));
		}
		
		refrescaPrecioTotalPedido();
		
		/*
		if (this.esPantallaModificacionPedido)
		{
			Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_PEDIDO_CARGADO, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_RUTERO_CARGADO, Toast.LENGTH_SHORT).show();			
		}
		*/
	}
	
	/**
	 * Obtenemos mediante una consulta a la BD de la tablet, los datos del pedido
	 * 
	 * @return Vector de DatosLineaPedido
	 */
	private DatosPedido consultaDatosPedidoBD(int idPedido)
	{
		DatosPedido datosPedido = null;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorPedido = null;
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
		int descuentoEspecial = 0;
		//Datos para la linea de pedido
		int idPrepedidoLP = 0;
		String codArticuloLP = null;
		boolean esMedidaKgLP = false;
		boolean esCongeladoLP = false;
		float cantidadKgLP = 0;
		int cantidadUdLP = 0;
		float tarifaClienteLP = 0; 
		String observacionesLP = null;
		boolean fijarTarifaLP = false;
		boolean fijarArticuloLP = false;
		boolean fijarObservacionesLP = false;
		
		//Constiene los datos de una fecha descompuesta, es decir, para una fecha DD-MM-YYYY, contiene 3 elementos, 
		//en la posicion 0 tiene DD, en la posicion 1 tiene MM y en la posicion 2 tiene YYYY.
		String[] fechaDescompuesta = null;
		
		db.abrir();
		
		cursorPedido = db.obtenerPrepedidoConDatosCliente(idPedido);	
		
		//Si tenemos resultado de la consulta...
		if (cursorPedido.moveToFirst())
		{	
			//Recorremos el cursor para obtener los datos de los pedidos recuperados
			do 
			{
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
				
				descuentoEspecial = cursorPedido.getInt(TablaPrepedido.POS_CAMPO_DESCUENTO_ESPECIAL);
				
				//Guardamos en una variable global las observaciones por defecto del pedido, ya que la tenemos que enviar al subdialogo que solicita los datos
				//del pedido al usuario
				this.observacionesDefectoPedido = cursorPedido.getString(TablaPrepedido.NUM_CAMPOS+TablaCliente.POS_CAMPO_OBSERVACIONES_PREPEDIDO);
				
				//Ahora para cada pedido recuperamos sus lineas de pedido
				lineasPedidoBD = new Vector<LineaPedidoBD>();
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
						
						lineasPedidoBD.add(new LineaPedidoBD(idPrepedidoLP, codArticuloLP, esMedidaKgLP, esCongeladoLP, cantidadKgLP, cantidadUdLP, tarifaClienteLP, observacionesLP, fijarTarifaLP, fijarArticuloLP, fijarObservacionesLP));
					} while (cursorLineasPedido.moveToNext());
				}
				
				datosPedido = new DatosPedido(idPrepedidoP, idClienteP, clienteP, diaFechaPedidoP, mesFechaPedidoP, anioFechaPedidoP, diaFechaEntregaP, mesFechaEntregaP, anioFechaEntregaP, (float)0, observacionesP, fijarObservacionesP, descuentoEspecial);

			} while (cursorPedido.moveToNext());
		}		
		
		db.cerrar();

		return datosPedido;
	}
	
	/**
	 * Obtenemos mediante una consulta a la BD de la tablet, las lineas de pedido que forman el rutero del cliente
	 * 
	 * @param idCliente del rutero a obtener
	 * @return Vector de DatosLineaPedido
	 */
	private Vector<DatosLineaPedido> consultaDatosRuteroBD(int idCliente)
	{
		Vector<DatosLineaPedido> lineasPedidoRutero = new Vector<DatosLineaPedido>();
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorRutero = null;

		//Obtenemos las lineas de rutero del cliente
		db.abrir();	
		
		cursorRutero = db.obtenerTodosLosRuterosDelClienteConArticulo(idCliente);
		
		//Recorremos el cursor con las lineas de pedido de la BD para formar las lineas de pedido del rutero a mostrar en pantalla
		if (cursorRutero.moveToFirst())
		{
			do 
			{		
				String codArticulo = cursorRutero.getString(TablaRutero.POS_KEY_CAMPO_CODIGO_ARTICULO);
				String articulo = cursorRutero.getString(TablaRutero.NUM_CAMPOS+TablaArticulo.POS_CAMPO_NOMBRE);
				String medida = Constantes.KILOGRAMOS;
				
				if (cursorRutero.getString(TablaRutero.NUM_CAMPOS+TablaArticulo.POS_CAMPO_ES_KG).equals("0"))
				{
					medida = Constantes.UNIDADES;
				}
				
				boolean esCongelado = false;
				
				if (cursorRutero.getString(TablaRutero.NUM_CAMPOS+TablaArticulo.POS_CAMPO_ES_CONGELADO).equals("1"))
				{
					esCongelado = true;
				}
				
				String ultimaFecha = cursorRutero.getString(TablaRutero.POS_CAMPO_FECHA_ULTIMA_COMPRA);
				float ultimaCantidad = Float.parseFloat(cursorRutero.getString(TablaRutero.POS_CAMPO_CANTIDAD_ULTIMA_COMPRA));
				float cantidadTotalAnio = Float.parseFloat(cursorRutero.getString(TablaRutero.POS_CAMPO_CANTIDAD_TOTAL_ANIO));
				float ultimaTarifa = Float.parseFloat(cursorRutero.getString(TablaRutero.POS_CAMPO_TARIFA_ULTIMA_COMPRA));
				float cantidadKg = 0;
				int cantidadUd = 0;
				float tarifaCliente = Float.parseFloat(cursorRutero.getString(TablaRutero.POS_CAMPO_TARIFA_CLIENTE));
				float tarifaLista = Float.parseFloat(cursorRutero.getString(TablaRutero.NUM_CAMPOS+TablaArticulo.POS_CAMPO_TARIFA_DEFECTO));
				String fechaCambioTarifaLista = cursorRutero.getString(TablaRutero.NUM_CAMPOS+TablaArticulo.POS_CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO);
				String observaciones = cursorRutero.getString(TablaRutero.POS_CAMPO_OBSERVACIONES);
				
				lineasPedidoRutero.add(new DatosLineaPedido(codArticulo,
															articulo,
															medida,
															esCongelado,
															ultimaFecha,
															ultimaCantidad,
															cantidadTotalAnio,
															ultimaTarifa,
															cantidadKg,
															cantidadUd,
															tarifaCliente,
															tarifaLista,
															fechaCambioTarifaLista,
															observaciones));
				
			} while (cursorRutero.moveToNext());
		}
		
		db.cerrar();
		
		return lineasPedidoRutero;
	}
	
	/**
	 * Presenta la pantalla inicial con los datos el rutero inicializados con los datos de las lineas de pedido que hay en la BD
	 * 
	 * @param lineasRutero
	 * @param lineasPedidoBD
	 */
	private void inicializaRuteroConLineasPedido(Vector<DatosLineaPedido> lineasRutero, Vector<LineaPedidoBD> lineasPedidoBD)
	{	
		Vector<DatosLineaPedido> lineasRuteroConPedido = new Vector<DatosLineaPedido>();
		DatosLineaPedido datosInicialesRutero = null;
		DatosLineaPedido datosLineaPedido = null;
		LineaPedidoBD lineaPedidoBD = null;
		
		if (lineasPedidoBD.size()>0)
		{
			//Indicamos en el pedido que hay lineas de pedido
			this.datosPedido.setHayLineasPedido(true);
		}
		
		//Primero ponemos las lineas de pedido de articulos que no estuvieran en el rutero
		for (int i=0; i<lineasPedidoBD.size(); i++)
		{
			if (!estaArticuloEnRutero(lineasRutero, lineasPedidoBD.elementAt(i).getCodArticulo()))
			{
				//Creamos una linea de rutero inicial para que se incluya esta linea de pedido en el rutero de pantalla.
				datosInicialesRutero = inicializaLineaRuteroConDatosArticuroBD(lineasPedidoBD.elementAt(i).getCodArticulo());
				
				//Si fuera nulo es que el articulo no existe por que se borro en una sincrnozacion con intraza, inicializamos los datos
				//para que al menos se muestre la linea de pedido
				if (datosInicialesRutero==null)
				{
					datosInicialesRutero = new DatosLineaPedido(lineasPedidoBD.elementAt(i).getCodArticulo(), "", "", false, Constantes.SIN_FECHA_ANTERIOR_LINEA_PEDIDO, (float)0, (float)0, (float)0, (float)0, 0, (float)0, (float)0, null, "");
				}
				
				//Lo insertamos en la posicion 0 del vector para que asi las lineas de pedido sin rutero aparezcan las primera en pantalla
				lineasRutero.insertElementAt(datosInicialesRutero, 0);
			}			
		}
		
		//Recorremos las linea de rutero, para comprobar con cada una, si tenemos datos de linea de pedido, y si asi es, inicializarlos
		for (int i=0; i<lineasRutero.size(); i++)
		{	
			//Se debe crear un nuevo objeto, sino se referencia al mismo
			datosLineaPedido = new DatosLineaPedido(lineasRutero.elementAt(i).getCodArticulo(),
													lineasRutero.elementAt(i).getArticulo(),
													lineasRutero.elementAt(i).getMedida(),
													lineasRutero.elementAt(i).getEsCongelado(),
													lineasRutero.elementAt(i).getUltimaFecha(),
													lineasRutero.elementAt(i).getUltimaCantidad(),
													lineasRutero.elementAt(i).getCantidadTotalAnio(),
													lineasRutero.elementAt(i).getUltimaTarifa(),
													lineasRutero.elementAt(i).getCantidadKg(),
													lineasRutero.elementAt(i).getCantidadUd(),
													lineasRutero.elementAt(i).getTarifaCliente(),
													lineasRutero.elementAt(i).getTarifaLista(),
													lineasRutero.elementAt(i).getFechaCambioTarifaLista(),
													lineasRutero.elementAt(i).getObservaciones());
			
			lineaPedidoBD = buscaLineaPedidoBD(lineasPedidoBD, datosLineaPedido.getCodArticulo());
			
			if (lineaPedidoBD!=null)
			{
				//Si tenemos datos de pedido para esta linea de rutero, los cogemos
				if (lineaPedidoBD.esMedidaEnKg())
				{
					datosLineaPedido.setMedida(Constantes.KILOGRAMOS);
				}
				else
				{
					datosLineaPedido.setMedida(Constantes.UNIDADES);					
				}
				datosLineaPedido.setEsCongelado(lineaPedidoBD.esCongelado());
				datosLineaPedido.setCantidadKg(lineaPedidoBD.getCantidadKg());
				datosLineaPedido.setCantidadUd(lineaPedidoBD.getCantidadUd());
				datosLineaPedido.setTarifaCliente(lineaPedidoBD.getPrecio());
				datosLineaPedido.setObservaciones(lineaPedidoBD.getObservaciones());
				datosLineaPedido.setFijarTarifa(lineaPedidoBD.getFijarPrecio());
				datosLineaPedido.setFijarArticulo(lineaPedidoBD.getFijarArticulo());
				datosLineaPedido.setFijarObservaciones(lineaPedidoBD.getFijarObservaciones());
			}
			
			lineasRuteroConPedido.add(datosLineaPedido);
		}	
		
		muestraRuteroModificacion(lineasRuteroConPedido, lineasRutero);
	}
	
	/**
	 * Inicializa una linea de rutero con los datos de un articulo en la BD
	 * 
	 * @param codArticulo
	 * @return los datos de la linea de pedido iniciales.
	 */
	private DatosLineaPedido inicializaLineaRuteroConDatosArticuroBD(String codArticulo)
	{
		DatosLineaPedido dlp = null;
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorArticulos = null;
		String medida = Constantes.KILOGRAMOS;
		
		db.abrir();
		
		//Hacemos el split por si el codigo del articulo tiene el indicador de clon, sino no se obtendran los datos del articulo de la BD
		cursorArticulos = db.obtenerArticulo(codArticulo.split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[0]);
		
		if (cursorArticulos.moveToFirst())
		{
			do 
			{		
				if (cursorArticulos.getString(TablaArticulo.POS_CAMPO_ES_KG).equals("0"))
				{
					medida = Constantes.UNIDADES;
				}
				
				dlp = new DatosLineaPedido(codArticulo,
										   cursorArticulos.getString(TablaArticulo.POS_CAMPO_NOMBRE), 
										   medida, 
										   false,
										   Constantes.SIN_FECHA_ANTERIOR_LINEA_PEDIDO, 
										   (float)0, 
										   (float)0, 
										   (float)0,
										   (float)0, 
										   0,
										   (float)0, 
										   (float)0, 
										   null,
										   "");
				
			} while (cursorArticulos.moveToNext());
		}
		
		db.cerrar();
		
		return dlp;
	}
	
	
	/**
	 * Comprueba si un articulo esta en el rutero del cliente.
	 * 
	 * @param lineasRutero
	 * @param codArticulo
	 * @return true si el articulo esta en el rutero, false en cualquier otro caso
	 */
	private boolean estaArticuloEnRutero(Vector<DatosLineaPedido> lineasRutero, String codArticulo)
	{
		boolean esta = false;
		
		for (int i=0; i<lineasRutero.size(); i++)
		{
			if (lineasRutero.elementAt(i).getCodArticulo().equals(codArticulo))
			{
				esta = true;
				break;
			}
		}
		
		return esta;
	}
	
	/**
	 * Busca en las lineas de pedido, los datos de la linea de pedido del articulo
	 * 
	 * @param lineas de pedido
	 * @param codArticulo cuya linea de pedido queremos buscar
	 * 
	 * @return los datos de la linea de pedido o null si no se ha encontrado ninguna
	 */
	private LineaPedidoBD buscaLineaPedidoBD(Vector<LineaPedidoBD> lineasPedidoBD, String codArticulo)
	{
		LineaPedidoBD lineaPedidoEncontrada = null;
		
		for (int i=0; i<lineasPedidoBD.size(); i++)
		{
			if (lineasPedidoBD.elementAt(i).getCodArticulo().equals(codArticulo))
			{
				lineaPedidoEncontrada = lineasPedidoBD.elementAt(i);
				break;
			}
		}
		
		return lineaPedidoEncontrada;
	}
	
	/**
	 * Presenta la pantalla inicial con los datos el rutero
	 * 
	 * @param lineasRutero
	 * @param lineasRuteroIni
	 */
	private void inicializaRutero(Vector<DatosLineaPedido> lineasRutero)
	{	
		this.colorFilaClaro = true;
		
		for (int i=0; i<lineasRutero.size(); i++)
		{
			//Se debe crear un nuevo objeto, sino se referencia al mismo
			DatosLineaPedido datosLineaIniRutero = new DatosLineaPedido(lineasRutero.elementAt(i).getCodArticulo(),
																			lineasRutero.elementAt(i).getArticulo(),
																			lineasRutero.elementAt(i).getMedida(),
																			lineasRutero.elementAt(i).getEsCongelado(),
																			lineasRutero.elementAt(i).getUltimaFecha(),
																			lineasRutero.elementAt(i).getUltimaCantidad(),
																			lineasRutero.elementAt(i).getCantidadTotalAnio(),
																			lineasRutero.elementAt(i).getUltimaTarifa(),
																			lineasRutero.elementAt(i).getCantidadKg(),
																			lineasRutero.elementAt(i).getCantidadUd(),
																			lineasRutero.elementAt(i).getTarifaCliente(),
																			lineasRutero.elementAt(i).getTarifaLista(),
																			lineasRutero.elementAt(i).getFechaCambioTarifaLista(),
																			lineasRutero.elementAt(i).getObservaciones());
			insertaLineaInicialPedidoEnRutero(lineasRutero.elementAt(i), datosLineaIniRutero);
		}
		
		//Posicionamos la tabla de rutero en pantalla, al princio del scroll
		((ScrollView)findViewById(R.id.scrollTablaLineasPedidoP)).scrollTo(0, 0);
	}
	
	/**
	 * Crea los datos de pedido, en la linea de rutero asociada a la view pasada como parametro
	 * 
	 * @param v
	 * @param datosLineaPedido
	 */
	private void creaLineaPedido(View v, DatosLineaPedido datosLineaPedido)
	{
		guardaDatosLineaPedidoRuteroEnView(v, Constantes.COLUMNA_DATOS_LP, datosLineaPedido);
		
		//Indicamos en el pedido que hay lineas de pedido
		this.datosPedido.setHayLineasPedido(true);
		
		//Refrescamos los datos en la pantalla del rutero
		ponDatoNuevoPedidoFilaEnColumna(v, Constantes.COLUMNA_CANTIDAD_NUEVO_KG_LP, Constantes.formatearFloat3Decimales.format(datosLineaPedido.getCantidadKg()), this.getResources().getColor(R.color.colorFilaDatoIntroducido));
		ponDatoNuevoPedidoFilaEnColumna(v, Constantes.COLUMNA_CANTIDAD_NUEVO_UD_LP, new Integer(datosLineaPedido.getCantidadUd()).toString(), this.getResources().getColor(R.color.colorFilaDatoIntroducido));
		ponDatoNuevoPedidoFilaEnColumna(v, Constantes.COLUMNA_TARIFA_NUEVO_LP, ponerMarcaFijarTarifa(datosLineaPedido.getFijarTarifa())+Constantes.formatearFloat2Decimales.format(datosLineaPedido.getTarifaCliente())+Constantes.EURO+Constantes.SEPARADOR_MEDIDA_TARIFA+datosLineaPedido.getMedida(), this.getResources().getColor(R.color.colorFilaDatoIntroducido));
		
		//Refrescamos el precio total del pedido, recalculandolo segun las lineas del pedido del rutero
		refrescaPrecioTotalPedido();		
	}
	
	/**
	 * Presenta en pantalla las lineas de pedido que conponen el rutero, cuando es una pantalla de modificacion de pedido
	 * 
	 * @param lineasRutero
	 * @param lineasRuteroIni
	 */
	private void muestraRuteroModificacion(Vector<DatosLineaPedido> lineasRutero, Vector<DatosLineaPedido> lineasIniRutero)
	{	
		this.colorFilaClaro = true;
		
		for (int i=0; i<lineasRutero.size(); i++)
		{
			insertaLineaInicialPedidoEnRutero(lineasRutero.elementAt(i), lineasIniRutero.elementAt(i));
		}
	}
	
	/**
	 * Presenta en pantalla las lineas de pedido que conponen el rutero
	 * 
	 * @param lineasRutero
	 * @param lineasRuteroIni
	 */
	private void muestraRutero(Vector<DatosLineaPedido> lineasRutero, Vector<DatosLineaPedido> lineasIniRutero)
	{	
		this.colorFilaClaro = true;
		
		for (int i=0; i<lineasRutero.size(); i++)
		{
			insertaLineaPedidoEnRutero(lineasRutero.elementAt(i), lineasIniRutero.elementAt(i));
		}
	}	
	
	/**
	 * Inserta una fila en la pantalla de inicializacion, ya sea de rutero o de linea de pedido
	 * 
	
	 * @param lineaRutero
	 * @param lineaIniRutero
	 * @param esLineaPdido
	 */
	private void insertaLineaInicialPedidoEnRutero(DatosLineaPedido lineaRutero, DatosLineaPedido lineaIniRutero)
	{
		int colorTextoFila = dameColorTextoFila();
		int colorFila = dameColorFila();
		TableRow filaLP = new TableRow(this);   
		
		filaLP.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		filaLP.addView(creaVistaReferencia(lineaRutero.getCodArticulo(), colorFila, colorTextoFila));
		filaLP.addView(creaVistaArticulo(lineaRutero.getArticulo(), lineaRutero.getEsCongelado(), lineaRutero.getFijarArticulo(), colorFila, colorTextoFila));
		filaLP.addView(creaVistaFechaAnterior(lineaRutero.getUltimaFecha(), colorFila, colorTextoFila));
		filaLP.addView(creaVistaCantidadAnterior(lineaRutero.getUltimaCantidad(), lineaRutero.getCantidadTotalAnio(), colorFila, colorTextoFila));
		filaLP.addView(creaVistaTarifaAnterior(lineaRutero.getUltimaTarifa(), lineaRutero.getMedida(), colorFila, colorTextoFila));
		
		//Comprobamos si tenemos datos de la linea de pedido
		if (lineaRutero.getCantidadKg()!=0 || lineaRutero.getCantidadUd()!=0)
		{
			filaLP.addView(creaVistaCantidadKg(lineaRutero.getCantidadKg(), lineaRutero.getCantidadUd(), colorFila, this.getResources().getColor(R.color.colorFilaDatoIntroducido)));
			filaLP.addView(creaVistaCantidadUd(lineaRutero.getCantidadUd(), lineaRutero.getCantidadKg(), colorFila, this.getResources().getColor(R.color.colorFilaDatoIntroducido)));
			filaLP.addView(creaVistaTarifaCliente(lineaRutero.getTarifaCliente(), lineaRutero.getMedida(), lineaRutero.getFijarTarifa(), colorFila, this.getResources().getColor(R.color.colorFilaDatoIntroducido)));
		}
		else
		{
			filaLP.addView(creaVistaCantidadKg(lineaRutero.getCantidadKg(), lineaRutero.getCantidadUd(), colorFila, this.getResources().getColor(R.color.colorTextoSinValorPedido)));
			filaLP.addView(creaVistaCantidadUd(lineaRutero.getCantidadUd(), lineaRutero.getCantidadKg(), colorFila, this.getResources().getColor(R.color.colorTextoSinValorPedido)));
			filaLP.addView(creaVistaTarifaCliente(lineaRutero.getTarifaCliente(), lineaRutero.getMedida(), lineaRutero.getFijarTarifa(), colorFila, this.getResources().getColor(R.color.colorTextoSinValorPedido)));
		}
		
		filaLP.addView(creaVistaTarifaLista(lineaRutero.getTarifaLista(), lineaRutero.getFechaCambioTarifaLista(), lineaRutero.getMedida(), colorFila, this.getResources().getColor(R.color.colorTextoSinValorPedido)));
		
		//Guardamos en la fila de rutero los datos de la linea de pedido
		guardaDatosLineaPedidoRutero(lineaRutero, Constantes.COLUMNA_DATOS_LP);
		
		//Guardamos en la fila de rutero los datos de la linea de pedido como iniciales, pues los necesitaremos tener
		guardaDatosLineaPedidoRutero(lineaIniRutero, Constantes.COLUMNA_DATOS_INICIALES_LP);
		
		//Guardamos los datos de esta linea de pedidos
		this.lineasPedido.add(lineaIniRutero);
		
		//Insertamos en la tabla de rutero de la pantalla la nueva linea de pedido
		this.tablaRutero.addView(filaLP, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * Inserta una fila en la pantalla, ya sea de rutero o de linea de pedido
	 * 
	 * @param lineaRutero
	 * @param lineaIniRutero
	 */
	private void insertaLineaPedidoEnRutero(DatosLineaPedido lineaRutero, DatosLineaPedido lineaIniRutero)
	{
		int colorTextoFila = dameColorTextoFila();
		int colorFila = dameColorFila();
		TableRow filaLP = new TableRow(this);   
		
		filaLP.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		filaLP.addView(creaVistaReferencia(lineaRutero.getCodArticulo(), colorFila, colorTextoFila));
		filaLP.addView(creaVistaArticulo(lineaRutero.getArticulo(), lineaRutero.getEsCongelado(), lineaRutero.getFijarArticulo(), colorFila, colorTextoFila));
		filaLP.addView(creaVistaFechaAnterior(lineaRutero.getUltimaFecha(), colorFila, colorTextoFila));
		filaLP.addView(creaVistaCantidadAnterior(lineaRutero.getUltimaCantidad(), lineaRutero.getCantidadTotalAnio(), colorFila, colorTextoFila));
		
		//Obtenemos el dato de la tarifa anterior		
		filaLP.addView(creaVistaTarifaAnterior(lineaRutero.getUltimaTarifa(), lineaRutero.getMedida(), colorFila, colorTextoFila));
		
		//Comprobamos si tenemos datos de la linea de pedido
		if (lineaRutero.getCantidadKg()!=0 || lineaRutero.getCantidadUd()!=0)
		{
			filaLP.addView(creaVistaCantidadKg(lineaRutero.getCantidadKg(), lineaRutero.getCantidadUd(), colorFila, this.getResources().getColor(R.color.colorFilaDatoIntroducido)));
			filaLP.addView(creaVistaCantidadUd(lineaRutero.getCantidadUd(), lineaRutero.getCantidadKg(), colorFila, this.getResources().getColor(R.color.colorFilaDatoIntroducido)));
			
			//Obtenemos el dato de la tarifa cliente
			filaLP.addView(creaVistaTarifaCliente(lineaRutero.getTarifaCliente(), lineaRutero.getMedida(), lineaRutero.getFijarTarifa(), colorFila, this.getResources().getColor(R.color.colorFilaDatoIntroducido)));
		}
		else
		{
			filaLP.addView(creaVistaCantidadKg(lineaRutero.getCantidadKg(), lineaRutero.getCantidadUd(), colorFila, this.getResources().getColor(R.color.colorTextoSinValorPedido)));
			filaLP.addView(creaVistaCantidadUd(lineaRutero.getCantidadUd(), lineaRutero.getCantidadKg(), colorFila, this.getResources().getColor(R.color.colorTextoSinValorPedido)));
			
			//Obtenemos el dato de la tarifa cliente
			filaLP.addView(creaVistaTarifaCliente(lineaRutero.getTarifaCliente(), lineaRutero.getMedida(), lineaRutero.getFijarTarifa(), colorFila, this.getResources().getColor(R.color.colorTextoSinValorPedido)));		
		}
		
		//Obtenemos el dato de la tarifa lista
		filaLP.addView(creaVistaTarifaLista(lineaRutero.getTarifaLista(), lineaRutero.getFechaCambioTarifaLista(), lineaRutero.getMedida(), colorFila, this.getResources().getColor(R.color.colorTextoSinValorPedido)));
		
		//Guardamos en la fila de rutero los datos de la linea de pedido
		guardaDatosLineaPedidoRutero(lineaRutero, Constantes.COLUMNA_DATOS_LP);
		
		//Guardamos en la fila de rutero los datos de la linea de pedido como iniciales, pues los necesitaremos tener
		guardaDatosLineaPedidoRutero(lineaIniRutero, Constantes.COLUMNA_DATOS_INICIALES_LP);
		
		//Insertamos en la tabla de rutero de la pantalla la nueva linea de pedido
		this.tablaRutero.addView(filaLP, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	private View creaVistaReferencia(String referencia, int colorFila, int colorTextoFila)
	{
		TextView datoReferencia = new TextView(this);
		
		datoReferencia.setId(dameIdViewNuevo(Constantes.COLUMNA_REFERENCIA_LP));
		datoReferencia.setGravity(Gravity.CENTER);
		datoReferencia.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoReferencia.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColReferencia));
		datoReferencia.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoReferencia.setBackgroundColor(colorFila);
		datoReferencia.setTextColor(this.getResources().getColor(R.color.colorFilaDatoAnterior));
		//En caso de ser un codigo de articulo que indica que es un clon, al usuario solo se le muestra el codigo del articulo del original del clon
		datoReferencia.setText(referencia.split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[0]);
		datoReferencia.setMaxLines(1);
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoReferencia.getId(), datoReferencia);
		
		return datoReferencia;
	}
	
	private View creaVistaArticulo(String articulo, boolean esCongelado, boolean fijarArticulo, int colorFila, int colorTextoFila)
	{
		TextView datoArticulo = new TextView(this);
		
		datoArticulo.setId(dameIdViewNuevo(Constantes.COLUMNA_ARTICULO_LP));
		datoArticulo.setGravity(Gravity.CENTER);
		datoArticulo.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoArticulo.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColArticulo));
		datoArticulo.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoArticulo.setBackgroundColor(colorFila);
		if (esCongelado)
		{
			datoArticulo.setTextColor(this.getResources().getColor(R.color.colorTextoArticuloCongelado));
		}
		else
		{
			datoArticulo.setTextColor(colorTextoFila);			
		}
		
		datoArticulo.setMaxLines(1);
		//El ancho maximo es de 22 caracteres
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(22);
		datoArticulo.setFilters(filterArray);
		
		//Ponemos el dato
		datoArticulo.setText(ponerMarcaFijarArticulo(fijarArticulo)+articulo+ponerMarcaCongelado(esCongelado));
		datoArticulo.setClickable(true);
		
		datoArticulo.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v)
			{
				habilitaClickEnActivity(false);
				
				pideDatosLineaPedido(v);
			}
		});
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoArticulo.getId(), datoArticulo);
		
		return datoArticulo;
	}
	
	private View creaVistaFechaAnterior(String fecha, int colorFila, int colorTextoFila)
	{
		TextView datoFechaAnterior = new TextView(this);

		datoFechaAnterior.setId(dameIdViewNuevo(Constantes.COLUMNA_FECHA_ULTIMO_LP));
		datoFechaAnterior.setGravity(Gravity.CENTER);
		datoFechaAnterior.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoFechaAnterior.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColFechaAnterior));
		datoFechaAnterior.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoFechaAnterior.setBackgroundColor(colorFila);
		datoFechaAnterior.setTextColor(this.getResources().getColor(R.color.colorFilaDatoAnterior));
		datoFechaAnterior.setText(fecha);
		datoFechaAnterior.setMaxLines(1);
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoFechaAnterior.getId(), datoFechaAnterior);
		
		return datoFechaAnterior;
	}
	
	private View creaVistaCantidadAnterior(float cantidad, float cantidadTotalAnio, int colorFila, int colorTextoFila)
	{
		TextView datoCantidadAnterior = new TextView(this);
		String cantidadTotalFormateada = null;
		
		datoCantidadAnterior.setId(dameIdViewNuevo(Constantes.COLUMNA_CANTIDAD_ULTIMO_LP));
		datoCantidadAnterior.setGravity(Gravity.CENTER);
		datoCantidadAnterior.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoCantidadAnterior.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColCantidadAnterior));
		datoCantidadAnterior.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoCantidadAnterior.setBackgroundColor(colorFila);
		datoCantidadAnterior.setTextColor(this.getResources().getColor(R.color.colorFilaDatoAnterior));
		
		//Si la cantidad total es mayor de 1000 se pasa a toneladas
		if (cantidadTotalAnio>1000)
		{	
			cantidadTotalFormateada = Constantes.formatearFloat2Decimales.format(cantidadTotalAnio/1000)+Constantes.MARCA_TONELADAS;
		}
		else
		{
			cantidadTotalFormateada = Constantes.formatearFloat2Decimales.format(cantidadTotalAnio);
		}
		
		datoCantidadAnterior.setText(Constantes.formatearFloat3Decimales.format(cantidad)+Constantes.SEPARADOR_CANTIDAD_TOTAL_ANIO+cantidadTotalFormateada);
		datoCantidadAnterior.setMaxLines(1);
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoCantidadAnterior.getId(), datoCantidadAnterior);
		
		return datoCantidadAnterior;
	}
	
	private View creaVistaTarifaAnterior(float tarifa, String medida, int colorFila, int colorTextoFila)
	{
		TextView datoTarifaAnterior = new TextView(this);
		
		datoTarifaAnterior.setId(dameIdViewNuevo(Constantes.COLUMNA_TARIFA_ULTIMO_LP));
		datoTarifaAnterior.setGravity(Gravity.CENTER);
		datoTarifaAnterior.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoTarifaAnterior.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColTarifaAnterior));
		datoTarifaAnterior.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoTarifaAnterior.setBackgroundColor(colorFila);
		datoTarifaAnterior.setTextColor(this.getResources().getColor(R.color.colorFilaDatoAnterior));
		datoTarifaAnterior.setText(Constantes.formatearFloat2Decimales.format(tarifa)+Constantes.EURO+Constantes.SEPARADOR_MEDIDA_TARIFA+medida);
		datoTarifaAnterior.setMaxLines(1);
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoTarifaAnterior.getId(), datoTarifaAnterior);
		
		return datoTarifaAnterior;
	}
	
	private View creaVistaCantidadKg(float cantidadKg, int cantidadUd, int colorFila, int colorTextoFila)
	{
		TextView datoCantidad = new TextView(this);
		
		datoCantidad.setId(dameIdViewNuevo(Constantes.COLUMNA_CANTIDAD_NUEVO_KG_LP));
		datoCantidad.setGravity(Gravity.CENTER);
		datoCantidad.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoCantidad.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColCantidadKg));
		datoCantidad.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoCantidad.setBackgroundColor(colorFila);
		datoCantidad.setTextColor(colorTextoFila);
		datoCantidad.setMaxLines(1);
		
		if (cantidadKg!=0)
		{
			datoCantidad.setText(Constantes.formatearFloat3Decimales.format(cantidadKg));
		}
		else
		{
			if (cantidadUd!=0)
			{
				datoCantidad.setText("0");
			}
			else
			{
				datoCantidad.setText(Constantes.DATOS_NUEVO_PEDIDO_SIN_INTRODUCIR);
			}
		}

		datoCantidad.setCursorVisible(false);
		
		datoCantidad.setClickable(true);
		datoCantidad.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v)
			{
				habilitaClickEnActivity(false);
				
				pideDatosLineaPedido(v);
			}
		});
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoCantidad.getId(), datoCantidad);
		
		return datoCantidad;
	}
	
	private View creaVistaCantidadUd(int cantidadUd, float cantidadKg, int colorFila, int colorTextoFila)
	{
		TextView datoCantidad = new TextView(this);
		
		datoCantidad.setId(dameIdViewNuevo(Constantes.COLUMNA_CANTIDAD_NUEVO_UD_LP));
		datoCantidad.setGravity(Gravity.CENTER);
		datoCantidad.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoCantidad.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColCantidadUd));
		datoCantidad.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoCantidad.setBackgroundColor(colorFila);
		datoCantidad.setTextColor(colorTextoFila);
		datoCantidad.setMaxLines(1);
		
		if (cantidadUd!=0)
		{
			datoCantidad.setText(Constantes.formatearFloat3Decimales.format(cantidadUd));
		}
		else
		{
			if (cantidadKg!=0)
			{
				datoCantidad.setText("0");
			}
			else
			{
				datoCantidad.setText(Constantes.DATOS_NUEVO_PEDIDO_SIN_INTRODUCIR);
			}
		}

		datoCantidad.setCursorVisible(false);
		
		datoCantidad.setClickable(true);
		datoCantidad.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v)
			{
				habilitaClickEnActivity(false);
				
				pideDatosLineaPedido(v);
			}
		});
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoCantidad.getId(), datoCantidad);
		
		return datoCantidad;
	}
	
	private View creaVistaTarifaCliente(float tarifa, String medida, boolean fijarTarifa, int colorFila, int colorTextoFila)
	{
		TextView datoTarifa = new TextView(this);
		
		datoTarifa.setId(dameIdViewNuevo(Constantes.COLUMNA_TARIFA_NUEVO_LP));
		datoTarifa.setGravity(Gravity.CENTER);
		datoTarifa.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoTarifa.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColTarifa));
		datoTarifa.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoTarifa.setBackgroundColor(colorFila);
		datoTarifa.setTextColor(colorTextoFila);
		log("TRAZA - tarifa ("+tarifa+") - tarifa formateada ("+Constantes.formatearFloat2Decimales.format(tarifa)+")");
		datoTarifa.setText(ponerMarcaFijarTarifa(fijarTarifa)+Constantes.formatearFloat2Decimales.format(tarifa)+Constantes.EURO+Constantes.SEPARADOR_MEDIDA_TARIFA+medida);
		datoTarifa.setMaxLines(1);

		datoTarifa.setClickable(true);
		datoTarifa.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v)
			{
				habilitaClickEnActivity(false);
				
				pideDatosLineaPedido(v);
			}
		});
		
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoTarifa.getId(), datoTarifa);
		
		return datoTarifa;
	}
		
	private View creaVistaTarifaLista(float tarifaLista, String fechaCambioTarifaLista, String medida, int colorFila, int colorTextoFila)
	{
		TextView datoTarifaLista = new TextView(this);
		
		datoTarifaLista.setId(dameIdViewNuevo(Constantes.COLUMNA_TARIFA_LISTA_LP));
		datoTarifaLista.setGravity(Gravity.CENTER);
		datoTarifaLista.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTabla));
		datoTarifaLista.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColTarifaLista));
		datoTarifaLista.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTabla));
		datoTarifaLista.setBackgroundColor(colorFila);
		datoTarifaLista.setTextColor(this.getResources().getColor(R.color.colorFilaDatoAnterior));
		datoTarifaLista.setText(ponerMarcaCambioTarifaListaReciente(fechaCambioTarifaLista)+Constantes.formatearFloat2Decimales.format(tarifaLista)+Constantes.EURO+Constantes.SEPARADOR_MEDIDA_TARIFA+medida);
		datoTarifaLista.setMaxLines(1);
				
		//Guardamos la nueva vista para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(datoTarifaLista.getId(), datoTarifaLista);
		
		return datoTarifaLista;
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
	 * Para guardar los datos del pedido asociados con los datos presentados en pantalla
	 * @param datos
	 * @param columna de la tabla
	 */
	private void guardaDatosLineaPedidoRutero(DatosLineaPedido datos, int columna)
	{
		//Guardamos la informacion en la tablaHash para asi poder consultarla posteriormente
		this.viewsTablaRutero.put(dameIdViewNuevo(columna), datos);
		
		//Tambien actualiza los datos en el Vector de pedidos
		for (int i=0; i<this.lineasPedido.size(); i++)
		{
			if (this.lineasPedido.elementAt(i).getCodArticulo().equals(datos.getCodArticulo()))
			{
				this.lineasPedido.remove(i);
				this.lineasPedido.insertElementAt(datos, i);
				break;
			}
		}
	}

	private void pideDatosLineaPedido(View v)
	{
		Intent intent = null;
		
		//Guardamos la vista que ha seleccionado el usuario para solicitar los datos del pedido
		this.viewLineaRutero = v;
		
		//Pasamos los datos que tenemos de la linea de pedido, en el Intent
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.DialogoDatosLineaPedido.class);
		intent.putExtra("DATOS_LINEA_PEDIDO", dameDatosLineaPedidoRuteroEnView(v, Constantes.COLUMNA_DATOS_LP));
		
		//Tomamos de los datos iniciales de la linea de pedido las observaciones por defecto
		intent.putExtra("OBSERVACIONES_DEFECTO", dameDatosLineaPedidoRuteroEnView(v, Constantes.COLUMNA_DATOS_INICIALES_LP).getObservaciones());
		
		startActivityForResult(intent, DIALOGO_PIDE_DATOS_LINEA_PEDIDO);
	}
		
	/**
	 * Dado un codigo de articulo, obtiene el numero de clones que tiene el articulo
	 * @param codArticulo
	 * 
	 * @return numero de clones del articulo
	 */
	private int dameSiguienteNumeroDeClonParaElArticulo(String codArticulo)
	{
		int numMayorClon = 0;
		
		//Recorremos todas la lineas de pedido para buscar el numero de clon mayor del articulo
		for (int i=0; i<this.lineasPedido.size(); i++)
		{
			if (this.lineasPedido.elementAt(i).getCodArticulo().startsWith(codArticulo.split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[0]+Constantes.MARCA_CLON_CODIGO_ARTICULO))
			{
				if (Integer.parseInt(this.lineasPedido.elementAt(i).getCodArticulo().split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[2])>numMayorClon)
				{
					numMayorClon = Integer.parseInt(this.lineasPedido.elementAt(i).getCodArticulo().split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[2]);
				}
			}
		}
		
		//El siguiente indice sera el mayor actual + 1
		return numMayorClon+1;
	}
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) 
		{
			case (DIALOGO_PIDE_DATOS_LINEA_PEDIDO): 
			{
				//Cuando el usuario pulsa el boton ACEPTAR para añadir los datos de una nueva linea de pedido
				if (resultCode == Activity.RESULT_OK) 
				{
					//Obtenemos los datos de la linea de pedido
					DatosLineaPedido datosLineaPedido = (DatosLineaPedido) data.getParcelableExtra("DATOS_LINEA_PEDIDO");
					
					//Recuperamos el valor que nos indica si este articulo es o no un clon
					boolean esClon = data.getBooleanExtra("ES_CLON", false);
					
					if (esClon)
					{
						//Tomamos los datos iniciales de la linea de pedido para guardados en la linea del rutero del clon
						DatosLineaPedido datosIniLineaPedido = dameDatosLineaPedidoRuteroEnView(this.viewLineaRutero, Constantes.COLUMNA_DATOS_INICIALES_LP);
		
						//Tenemos que crear una copia del objeto para que no se referencia al de la linea de pedido de la que se clono
						DatosLineaPedido datosIniLineaPedidoClon = new DatosLineaPedido(datosIniLineaPedido.getCodArticulo(),
																						datosIniLineaPedido.getArticulo(),
																						datosIniLineaPedido.getMedida(),
																						datosIniLineaPedido.getEsCongelado(),
																						datosIniLineaPedido.getUltimaFecha(),
																						datosIniLineaPedido.getUltimaCantidad(),
																						datosIniLineaPedido.getCantidadTotalAnio(),
																						datosIniLineaPedido.getUltimaTarifa(),
																						datosIniLineaPedido.getCantidadKg(),
																						datosIniLineaPedido.getCantidadUd(),
																						datosIniLineaPedido.getTarifaCliente(),
																						datosIniLineaPedido.getTarifaLista(),
																						datosIniLineaPedido.getFechaCambioTarifaLista(),
																						datosIniLineaPedido.getObservaciones());
						
						//Le añadimos un sufijo de clon al codigo del articulo para que se trate como un articulo independiente, aunque este sufijo
						//no se le muestra al usuario, es algo interno
						String codigoArticuloClon = datosLineaPedido.getCodArticulo().split(Constantes.CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO)[0]+Constantes.MARCA_CLON_CODIGO_ARTICULO+dameSiguienteNumeroDeClonParaElArticulo(datosLineaPedido.getCodArticulo());
						datosLineaPedido.setCodArticulo(codigoArticuloClon);
						datosIniLineaPedidoClon.setCodArticulo(codigoArticuloClon);
						
						//Guardamos los datos como una nueva linea de pedido y posteriormente la insertamos en pantalla
						this.lineasPedido.add(datosLineaPedido);
						insertaLineaPedidoEnRutero(datosLineaPedido, datosIniLineaPedidoClon);
					}
					else
					{											
						//Guardamos los datos en la linea de rutero asociada
						guardaDatosLineaPedidoRuteroEnView(this.viewLineaRutero, Constantes.COLUMNA_DATOS_LP, datosLineaPedido);
						
						//Refrescamos los datos en la pantalla del rutero
						ponDatoNuevoPedidoFilaEnColumna(this.viewLineaRutero, Constantes.COLUMNA_CANTIDAD_NUEVO_KG_LP, Constantes.formatearFloat3Decimales.format(datosLineaPedido.getCantidadKg()), this.getResources().getColor(R.color.colorFilaDatoIntroducido));
						ponDatoNuevoPedidoFilaEnColumna(this.viewLineaRutero, Constantes.COLUMNA_CANTIDAD_NUEVO_UD_LP, new Integer(datosLineaPedido.getCantidadUd()).toString(), this.getResources().getColor(R.color.colorFilaDatoIntroducido));
						ponDatoNuevoPedidoFilaEnColumna(this.viewLineaRutero, Constantes.COLUMNA_TARIFA_NUEVO_LP, ponerMarcaFijarTarifa(datosLineaPedido.getFijarTarifa())+Constantes.formatearFloat2Decimales.format(datosLineaPedido.getTarifaCliente())+Constantes.EURO+Constantes.SEPARADOR_MEDIDA_TARIFA+datosLineaPedido.getMedida(), this.getResources().getColor(R.color.colorFilaDatoIntroducido));
					}
					
					//Si hay cambios que guardar habilitamos el boton
					if (datosLineaPedido.hayCambiosSinGuardar())
					{
						habilitaBotonGuardar();
						
						//Indicamos en el pedido que hay lineas de pedido
						this.datosPedido.setHayLineasPedido(true);
					}
										
					//Refrescamos el precio total del pedido, recalculandolo segun las lineas del pedido del rutero
					refrescaPrecioTotalPedido();
				}
				
				//Cuando el usuario pulsa el boton ELIMINAR, para eliminar los datos que introdujo anteriormente de la linea de pedido
				if (resultCode == Activity.RESULT_FIRST_USER) 
				{
					//Hay un cambio que hay que guardar
					habilitaBotonGuardar();
					
					//Tomamos como datos de la linea de pedido los iniciales guardados en la linea del rutero
					DatosLineaPedido datosIniLineaPedido = dameDatosLineaPedidoRuteroEnView(this.viewLineaRutero, Constantes.COLUMNA_DATOS_INICIALES_LP);
					
					//Indicamos que hay un cambio sin guardar ya que el registro de la linea de pedido, se debe borrar de la BD
					datosIniLineaPedido.setHayCambiosSinGuardar(true);
					
					guardaDatosLineaPedidoRuteroEnView(this.viewLineaRutero, Constantes.COLUMNA_DATOS_LP, datosIniLineaPedido);
					
					//Refrescamos los datos en la pantalla del rutero
					ponDatoNuevoPedidoFilaEnColumna(this.viewLineaRutero, Constantes.COLUMNA_CANTIDAD_NUEVO_KG_LP, Constantes.DATOS_NUEVO_PEDIDO_SIN_INTRODUCIR, this.getResources().getColor(R.color.colorTextoSinValorPedido));
					ponDatoNuevoPedidoFilaEnColumna(this.viewLineaRutero, Constantes.COLUMNA_CANTIDAD_NUEVO_UD_LP, Constantes.DATOS_NUEVO_PEDIDO_SIN_INTRODUCIR, this.getResources().getColor(R.color.colorTextoSinValorPedido));
					ponDatoNuevoPedidoFilaEnColumna(this.viewLineaRutero, Constantes.COLUMNA_TARIFA_NUEVO_LP, Constantes.formatearFloat2Decimales.format(datosIniLineaPedido.getTarifaCliente())+Constantes.EURO+Constantes.SEPARADOR_MEDIDA_TARIFA+datosIniLineaPedido.getMedida(), this.getResources().getColor(R.color.colorTextoSinValorPedido));
					
					//Refrescamos el precio total del pedido, recalculandolo segun las lineas del pedido del rutero
					refrescaPrecioTotalPedido();
					
					//Si esta pulsado el boton que indica ocultar las lineas de rutero en la pantalla, la refrescamos para eliminar esta linea de pedido
					if (((ToggleButton)findViewById(R.id.botonOcultarRuteroP)).isChecked())
					{
						ocultarLineasRutero();
					}
					
					//Comprobamos si nos hemos quedado sin lineas de pedido
					if (!hayLineasPedido())
					{
						this.datosPedido.setHayLineasPedido(false);
						Toast.makeText(getBaseContext(), Constantes.MENSAJE_PEDIDO_SIN_LINEAS_PEDIDO, Toast.LENGTH_SHORT).show();
					}
				}
				
				//Cuando el usuario pulsa el boton CANCELAR, en una ventana de solicitud de datos de linea de pedido de un nuevo articulo
				if (resultCode == Activity.RESULT_CANCELED) 
				{
					//Si esta pulsado el boton que indica ocultar las lineas de rutero en la pantalla, la refrescamos para eliminar esta linea de pedido
					//sino, estaria sin contener datos
					if (((ToggleButton)findViewById(R.id.botonOcultarRuteroP)).isChecked())
					{
						ocultarLineasRutero();
					}
				}

				
				break;
			}
			
			case (DIALOGO_PIDE_DATOS_NUEVO_PEDIDO): 
			{
				//Cuando el usuario pulsa el boton ACEPTAR para los datos del pedido
				if (resultCode == Activity.RESULT_OK) 
				{
					//Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_CARGAR_RUTERO, Toast.LENGTH_SHORT).show();
					
					//Obtenemos los datos del pedido
					this.datosPedido = (DatosPedido) data.getParcelableExtra("DATOS_PEDIDO");
										
					//Actualizamos la pantalla con los datos del nuevo pedido
					cargaDatosPedido();
					cargaRuteroEnPantalla(this.datosPedido.getIdCliente());
				}
								
				break;
			}
			
			case (DIALOGO_PIDE_DATOS_PEDIDO): 
			{
				//Cuando el usuario pulsa el boton ACEPTAR para los datos del pedido
				if (resultCode == Activity.RESULT_OK) 
				{
					//Obtenemos los datos del pedido
					this.datosPedido = (DatosPedido) data.getParcelableExtra("DATOS_PEDIDO");
					
					//Si hay cambios que guardar y tenemos lineas de pedido, habilitamos el boton
					if (this.datosPedido.hayDatosPedidoSinGuardar() && this.datosPedido.hayLineasPedido())
					{
						habilitaBotonGuardar();
					}
					
					cargaDatosPedido();
				}
								
				break;
			}
			
			case (DIALOGO_AVISO_DATOS_SIN_GUARDAR): 
			{
				//Cuando el usuario pulsa el boton SI, indicando que quiere guardar los datos del pedido
				if (resultCode == Activity.RESULT_OK) 
				{
					//Guardamos los datos del pedido
					guardarDatosPedidoBD();
					
					//Mostramos el nuevo subdialogo para solicitar los datos del nuevo pedido al usuario
					subdialogoDatosNuevoPedido();
				}
				
				//Cuando el usuario pulsa el boton NO, indicando que NO quiere guardar los datos del pedido
				if (resultCode == Activity.RESULT_FIRST_USER) 
				{
					//Mostramos el nuevo subdialogo para solicitar los datos del nuevo pedido al usuario
					subdialogoDatosNuevoPedido();
				}
								
				break;
			}
			
			case (DIALOGO_AVISO_DATOS_SIN_GUARDAR_TECLA_ATRAS): 
			{
				//Cuando el usuario pulsa el boton SI, indicando que quiere guardar los datos del pedido antes de salir
				if (resultCode == Activity.RESULT_OK) 
				{
					//Guardamos los datos del pedido
					guardarDatosPedidoBD();					
				}
				
				//Cuando el usuario pulsa el boton SI o NO, indicando que quiere o no guardar los datos despues se finaliza la activity, a no ser
				//que haya querido cancelar la operacion de salir de la activity
				if (resultCode != Activity.RESULT_CANCELED) 
				{
					//Finalizamos la Activity, pues se pulso la tecla atras
					finish();
				}
												
				break;
			}
			
			case (DIALOGO_PIDE_DATOS_NUEVO_ARTICULO_RUTERO): 
			{
				//Cuando el usuario pulsa el boton ACEPTAR, para crear un nueva linea de pedido con un nuevo articulo
				if (resultCode == Activity.RESULT_OK) 
				{
					//Obtenemos los datos de la nueva linea de pedido y creamos una nueva linea de pedido en la pantalla del rutero
					DatosLineaPedido datosLineaPedido = (DatosLineaPedido) data.getParcelableExtra("DATOS_LINEA_PEDIDO");
					
					//Guardamos los datos de esta linea de pedido y posteriormente la insertamos en pantalla
					this.lineasPedido.add(datosLineaPedido);
					insertaLineaPedidoEnRutero(datosLineaPedido, datosLineaPedido);
					
					//Abrimos el subdialogo con los datos de la nueva linea de pedido, para que los modifique el usuario
					pideDatosLineaPedido((View)this.viewsTablaRutero.get(((this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP) * 100) + Constantes.COLUMNA_CANTIDAD_NUEVO_KG_LP));
				}
												
				break;
			}
		}
		
		//Hablilitamos los eventos click
		habilitaClickEnActivity(true);
	}
	
	/**
	 * Comprueba si el pedido tiene alguna linea de pedido con datos.
	 * 
	 * @return true si hay alguna linea de pedido con datos, false en cualquier otro caso
	 */
	private boolean hayLineasPedido()
	{
		boolean resultado = false;
		
		//Recorremos el rutero para comprobar si hay alguna linea de pedido con datos, en el momento que encontremos una salimos
		for (int i=1; i<=this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP; i++)
		{
			if (((DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_LP)).getCantidadKg()!=0 ||
				((DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_LP)).getCantidadUd()!=0)
			{
				resultado = true;
				break;
			}
		}
		
		return resultado;
	}
		
	/**
	 * Muestra una marca al lado de la tarifa de cliente, en caso de que el usuario haya indicado que la quiere fijar en intraza
	 * @param ponerMarca
	 * 
	 * @return la marca a poner o cadena vacia en caso de no llevar marca
	 */
	private String ponerMarcaFijarTarifa(boolean ponerMarca)
	{
		String resultado = "";
		
		if (ponerMarca)
		{
			resultado = Constantes.MARCA_FIJAR_TARIFA;
		}
		
		return resultado;
	}
	
	/**
	 * Muestra una marca al lado del articulo, en caso de que el usuario lo haya creado nuevo en el rutero e indicado que lo quiere fijar en intraza
	 * @param ponerMarca
	 * 
	 * @return la marca a poner o cadena vacia en caso de no llevar marca
	 */
	private String ponerMarcaFijarArticulo(boolean ponerMarca)
	{
		String resultado = "";
		
		if (ponerMarca)
		{
			resultado = Constantes.MARCA_FIJAR_ARTICULO;
		}
		
		return resultado;
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
	 * Dada una vista y un numero de columna, devuelve el dato de la fila de la tabla de pedidos de cliente, donde esta la vista y que ocupa 
	 * la columna indicada
	 * 
	 * @param v
	 * @param columna
	 * 
	 * @return el dato buscado
	 */
	private String dameDatoFilaEnColumna(View v, int columna)
	{
		TextView textViewBuscado = (TextView)this.viewsTablaRutero.get(calculaIdView(v, columna));
		
		return textViewBuscado.getText().toString();
	}
	
	/**
	 * Dada una vista, devuelve la información adicional al pedido de la tabla de pedidos de cliente, donde esta la vista. 
	 * la columna indicada
	 * 
	 * @param v
	 * @param columna
	 * 
	 * @return objeto Bundle que contiene la informacion
	 */
	private DatosLineaPedido dameDatosLineaPedidoRuteroEnView(View v, int columna)
	{	
		return (DatosLineaPedido)this.viewsTablaRutero.get(calculaIdView(v, columna));
	}
	
	/**
	 * Dada una vista de una linea del rutero, guarda los datos de la linea de pedido asociados
	 * 
	 * @param v
	 * @param columna
	 * @param datos linea de pedido
	 */
	private void guardaDatosLineaPedidoRuteroEnView(View v, int columna, DatosLineaPedido datos)
	{
		this.viewsTablaRutero.put(calculaIdView(v, columna), datos);
	}
	
	/**
	 * Dada una vista y un numero de columna, devuelve el dato de la fila de la tabla de pedidos de cliente, donde esta la vista y que ocupa 
	 * la columna indicada
	 * 
	 * @param v
	 * @param columna
	 * 
	 * @return el dato buscado
	 */
	private int dameColorTextoDatoFilaEnColumna(View v, int columna)
	{		
		TextView textViewBuscado = (TextView)this.viewsTablaRutero.get(calculaIdView(v, columna));
		
		return textViewBuscado.getTextColors().getDefaultColor();
	}
	
	/**
	 * Dada una vista y un numero de columna, pone un dato del nuevo pedido en la fila de la tabla de pedidos de cliente, en la fila donde esta 
	 * la vista y que ocupa la columna indicada
	 * 
	 * @param v
	 * @param columna
	 * @param dato
	 * @param idColor
	 * 
	 * @return el dato buscado
	 */
	private void ponDatoNuevoPedidoFilaEnColumna(View v, int columna, String dato, int idColor)
	{		
		TextView textViewBuscado = (TextView)this.viewsTablaRutero.get(calculaIdView(v, columna));
		
		textViewBuscado.setText(dato);
		textViewBuscado.setTextColor(idColor);
		//textViewBuscado.setTextColor(dameColorTextoDatoFilaEnColumna(v, 1));
	}

	/**
	 * Actualiza el precio total en el widget de la pantalla, recalculandolo
	 */
	private void refrescaPrecioTotalPedido()
	{
		float precioTotal = 0;
		
		//Recorremos el rutero para acumular el precio de las lineas de pedido introducidas por el usuario, el numero de filas es el numero de elementos / numeroColumnas
		for (int i=1; i<=this.viewsTablaRutero.size()/Constantes.COLUMNAS_TOTALES_LP; i++)
		{
			DatosLineaPedido datosLineaPedido = (DatosLineaPedido)this.viewsTablaRutero.get(((i) * 100) + Constantes.COLUMNA_DATOS_LP);
			precioTotal += datosLineaPedido.getPrecio();
		}
		
		this.datosPedido.setPrecioTotal(precioTotal);
		((TextView)findViewById(R.id.textoPrecioTotalP)).setText(Constantes.PREFIJO_TEXTO_PRECIO_TOTAL+Constantes.formatearFloat2Decimales.format(this.datosPedido.getPrecioTotal())+Constantes.EURO);
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
		return ((this.tablaRutero.getChildCount()+1) * 100) + columnaTabla;
	}
	
	/**
	 * Devuelve el ID de la view, que sera uno de los datos de la tabla. El ID debe ser entero y sera de la forma
	 * x..xy, donde "x...x" indica la fila e "y" la columna, el numero de filas no se sabe cual sera, pero el numero de
	 * columnas es fijo, siempre sera de 1 a 11. Asi para una vista con ID 2604, la fila sera "26" y la columna "4".
	 * 
	 * @param view de la fial al que pertenece la columna
	 * @param columnaTabla, la columna que ocupa la view, para la cual queremos obtener su ID.
	 * 
	 * @return el ID de la view de la columna
	 */
	private int calculaIdView(View v, int columna)
	{
		return ((v.getId()/10)*10)+columna;
	}
	
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
	
	private int dameColorTextoFila()
	{
		int resultado = this.getResources().getColor(R.color.colorTextoFilaClaro);
		
		if (!this.colorFilaClaro)
		{
			resultado = this.getResources().getColor(R.color.colorTextoFilaOscuro);
		}
		
		return resultado;
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
	
	private void log(String text)
	{
		Log.d("PantallaRutero", text);
	}
}
