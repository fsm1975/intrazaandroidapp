package com.technicalnorms.intraza.interfaz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.technicalnorms.intraza.interfaz.datos.DatosDialogoMensaje;
import com.technicalnorms.intraza.R;

/**
 * Activity que muestra al usuario un mensaje cono boton aceptar.
 * 
 * @author JLZS
 *
 */
public class DialogoMensaje extends Activity
{	
	//Contiene la clase activity que solicito el subdialogo
	String activity = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button aceptarBtn = null;
		DatosDialogoMensaje datosMensaje = null;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_mensaje);
				
		//Obtenemos del intent los datos del mensaje
		datosMensaje = (DatosDialogoMensaje) this.getIntent().getParcelableExtra("DATOS_MENSAJE");
		
		if (datosMensaje != null)
		{
			//Se actualizan los widget del layout con los datos del mensaje
			((TextView)findViewById(R.id.tituloDM)).setText(datosMensaje.getTitulo());
			((TextView)findViewById(R.id.informacionDM)).setText(datosMensaje.getInformacion());
			
			//Recuperamos la activity que solicito el subdialogo
			this.activity = datosMensaje.getActivity();
		}
			    
		//*******
		//BOTONES
		//*******
				
		aceptarBtn = (Button)findViewById(R.id.aceptarDM);
		aceptarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				returnDM();
			}
		});
		
		//Para que la pantalla de fondo no se vea
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	}	
	
	/**
	 * - Devuelve a la activity que solicito el mensaje el control.
	 * - Indica que le ejecución de la activity ha sido OK.
	 * - Finaliza la activity.
	 */
	private void returnDM()
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_OK;

		//Le pasamos los datos de la consulta a la activity principal en el intent
		try
		{
			intent = new Intent(this, Class.forName(this.activity));
		}
		catch (ClassNotFoundException e)
		{
			//Si no se encuntra la clase mandamos el intent a la activity principal
			intent = new Intent(this, com.technicalnorms.intraza.InTrazaActivity.class);
		}
			
		setResult(resultadoActivity, intent);
				
		finish();
	}
}