package com.technicalnorms.intraza.interfaz;

import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.interfaz.datosBD.AdaptadorBD;
import com.technicalnorms.intraza.interfaz.datosBD.ParametroConfiguracionBD;
import com.technicalnorms.intraza.interfaz.datosBD.TablaConfiguracion;
import com.technicalnorms.intraza.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ScrollView;

import java.util.Vector;

public class PantallaConfiguracion extends Activity
{
	//Almacena los parametros de configuracion obtenidos en la consulta a la BD
	Vector<ParametroConfiguracionBD> parametrosBD = null;
	
	//El widget que forma la tabla de parametros en pantalla
	private TableLayout tablaParametros = null;
	
	private boolean colorFilaClaro = true;
		
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_configuracion);
		
		//Inicializamos las variables mientro de la clase
		this.tablaParametros = (TableLayout)findViewById(R.id.parametrosTableC);
		
		//Obtenemos los parametros de configuracion de la BD
		this.parametrosBD = consultaParametrosConfiguracionBD();
		
		//Mostramos la informacion de los parametros en la pantalla
		cargaParametrosEnPantalla(this.parametrosBD);
		
		//Definimos el evento onClick del boton de guardar
		((Button)findViewById(R.id.botonGuardarCambiosPC)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{	
				habilitaClickEnActivity(false);
				
				guardaParametrosConfiguracion();
			}
		});
		
		//Para ocultar el teclado virtual
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	/**
	 * Consultamos en la BD de la tablet los parametros de configuracion
	 * 
	 * @param Datos de la consulta
	 * @return un Vector con los datos de los pedidos consultados
	 */
	private Vector<ParametroConfiguracionBD> consultaParametrosConfiguracionBD()
	{
		Vector<ParametroConfiguracionBD> parametrosConsultados = new Vector<ParametroConfiguracionBD>();
		AdaptadorBD db = new AdaptadorBD(this);
		Cursor cursorParametros = null;
		boolean esEditable = true;
		
		db.abrir();
		
		cursorParametros = db.obtenerTodosLosParametrosDeConfiguracion();	
		
		//Si tenemos resultado de la consulta...
		if (cursorParametros.moveToFirst())
		{	
			//Recorremos el cursor para obtener los datos de los parametros de configuracion
			do 
			{
				if (cursorParametros.getInt(TablaConfiguracion.POS_CAMPO_ES_EDITABLE) == 1)
				{
					esEditable = true;
				}
				else
				{
					esEditable = false;
				}
				
				parametrosConsultados.add(new ParametroConfiguracionBD(cursorParametros.getString(TablaConfiguracion.POS_KEY_CAMPO_NOMBRE_PARAMETRO), 
																	   cursorParametros.getString(TablaConfiguracion.POS_CAMPO_VALOR),
																	   cursorParametros.getString(TablaConfiguracion.POS_CAMPO_DESCRIPCION),
																	   esEditable));

			} while (cursorParametros.moveToNext());
		}		

		db.cerrar();
		
		//Toast.makeText(getBaseContext(), Constantes.MENSAJE_PANTALLA_CONSULTA_PEDIDOS_TERMINADA, Toast.LENGTH_SHORT).show();		
		
		return parametrosConsultados;
	}
	
	/**
	 * Carga en la pantalla los parametros de configuracion
	 */
	private void cargaParametrosEnPantalla(Vector<ParametroConfiguracionBD> parametros)
	{	
		this.colorFilaClaro = true;
				
		for (int i=0; i<parametros.size(); i++)
		{
			//El parametro solo se muestra en pantalla si es editable
			if (parametros.elementAt(i).getEsEditable())
			{
				insertaLineaParametroConfiguracionEnTabla(parametros.elementAt(i));
			}
		}
		
		//Posicionamos la tabla de configuracion en pantalla, al princio del scroll
		((ScrollView)findViewById(R.id.scrollTablaC)).scrollTo(0, 0);
	}
	
	/**
	 * Inserta una fila en la pantalla en la tabla de configuracion
	 * 
	 * @param parametro
	 */
	public void insertaLineaParametroConfiguracionEnTabla(ParametroConfiguracionBD parametro)
	{
		int colorFila = dameColorFila();
		TableRow filaP = new TableRow(this);   
		
		filaP.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		filaP.addView(creaVistaDescripcion(parametro.getNombre(), parametro.getDescripcion(), colorFila));
		filaP.addView(creaVistaValor(parametro.getNombre(), parametro.getValor(), colorFila));
		
		//Insertamos en la tabla de pedidos de la pantalla el nuevo pedido
		this.tablaParametros.addView(filaP, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * Crea un widget para mostrar la descripcion de los parametros en pantalla
	 * 
	 * @param nombre
	 * @param descripcion
	 * @param colorFila
	 * @param colorTextoFila
	 * 
	 * @return la View que muestra el dato con el formato adecuado
	 */
	private View creaVistaDescripcion(String nombre, String descripcion, int colorFila)
	{
		TextView datoDescripcion = new TextView(this);

		datoDescripcion.setGravity(Gravity.LEFT);
		datoDescripcion.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTablaConfiguracion));
		datoDescripcion.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColDescripcionParametro));
		datoDescripcion.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTablaConfiguracion));
		datoDescripcion.setBackgroundColor(colorFila);
		datoDescripcion.setTextColor(this.getResources().getColor(R.color.colorTextoDescripcionParametro));
		datoDescripcion.setMaxLines(3);

		//Guardamos el nombre del parametro para actualizar la BD
		datoDescripcion.setContentDescription(nombre);
		
		//Ponemos el dato
		datoDescripcion.setText(descripcion);
		
		return datoDescripcion;
	}
	
	/**
	 * Crea un widget para mostrar el valor de los parametros en pantalla
	 * 
	 * @param nombre
	 * @param valor
	 * @param colorFila
	 * @param colorTextoFila
	 * 
	 * @return la View que muestra el dato con el formato adecuado
	 */
	private View creaVistaValor(String nombre, String valor, int colorFila)
	{
		EditText datoValor = new EditText(this);

		datoValor.setGravity(Gravity.LEFT);
		datoValor.setHeight(this.getResources().getDimensionPixelSize(R.dimen.heightFilaDatosTablaConfiguracion));
		datoValor.setWidth(this.getResources().getDimensionPixelSize(R.dimen.widthColValorParametro));
		datoValor.setTextSize(this.getResources().getDimensionPixelSize(R.dimen.textSizeFilaDatosTablaConfiguracion));

		//Guardamos el nombre del parametro para actualizar la BD
		datoValor.setContentDescription(nombre);
		
		//Ponemos el dato
		datoValor.setText(valor);
		
		return datoValor;
	}
	
	/**
	 * Guardamos en la BD el valor de los parametros mostrados en pantalla.
	 */
	private void guardaParametrosConfiguracion()
	{
		AdaptadorBD db = new AdaptadorBD(this);
		TableRow filaTablaView = null;
		TextView descripcionView = null;
		EditText valorView = null;
		
		db.abrir();
		
		//Recorremos las filas de la tabla
		for (int i=0; i<this.tablaParametros.getChildCount(); i++)
		{
			//Para cada fila obtenemos la descripcion y el valor
			filaTablaView = (TableRow)this.tablaParametros.getChildAt(i);
			
			descripcionView = (TextView)filaTablaView.getChildAt(0);
			valorView = (EditText)filaTablaView.getChildAt(1);
		
			//Guardamos los valores en la BD
			db.actualizarValorParametroConfiguracion(descripcionView.getContentDescription().toString(), valorView.getText().toString().trim());
		}
		
		db.cerrar();
		
		Toast.makeText(getBaseContext(), Constantes.MENSAJE_PARAMETROS_PC_GUARDADOS, Toast.LENGTH_SHORT).show();
		
		habilitaClickEnActivity(true);
	}
	
	/**
	 * Deshabilita o no, todo los eventos onClick de la activity para evitar ejecutar dos click seguidos
	 * @param deshabilita
	 */
	private void habilitaClickEnActivity(boolean habilita)
	{
		((Button)findViewById(R.id.botonGuardarCambiosPC)).setClickable(habilita);
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
}
