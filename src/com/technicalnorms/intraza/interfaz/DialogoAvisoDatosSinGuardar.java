package com.technicalnorms.intraza.interfaz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.technicalnorms.intraza.R;

/**
 * Activity que solicita al usuario la confirmación de la acción a tomar cuando hay datos pendientes de guardar en el pedido y solicita
 * crear un pedido nuevo.
 * 
 * @author JLZS
 *
 */
public class DialogoAvisoDatosSinGuardar extends Activity
{	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button siBtn = null;
		Button noBtn = null;
		Button cancelarBtn = null;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_aviso_datos_sin_guardar);
			    
		//*******
		//BOTONES
		//*******
				
		siBtn = (Button)findViewById(R.id.siBotonAvisoDatosSinGuardar);
		siBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{		
						returnSi();
			}
		});
		
		noBtn = (Button)findViewById(R.id.noBotonAvisoDatosSinGuardar);
		noBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{		
						returnNo();
			}
		});
		
		cancelarBtn = (Button)findViewById(R.id.cancelarBotonAvisoDatosSinGuardar);
		cancelarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				cancelarDialogo();
			}
		});
	}	
	
	
	/**
	 * - Devuelve a la activity que lo solicito que el usuario ha pulsado el boton SI.
	 * - Finaliza la activity.
	 */
	private void returnSi()
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_OK;

		//Enviamos los datos del pedido a la activity principal
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaRutero.class);

		setResult(resultadoActivity, intent);
				
		finish();
	}
	
	/**
	 * - Devuelve a la activity que lo solicito que el usuario ha pulsado el boton NO.
	 * - Finaliza la activity.
	 */
	private void returnNo()
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_FIRST_USER;

		//Enviamos los datos del pedido a la activity principal
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaRutero.class);

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
}