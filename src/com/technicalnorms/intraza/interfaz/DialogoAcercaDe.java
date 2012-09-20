package com.technicalnorms.intraza.interfaz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.technicalnorms.intraza.Constantes;
import com.technicalnorms.intraza.R;

/**
 * Activity que muestra la información de "Acerda De" de la aplicacion
 * 
 * @author JLZS
 *
 */
public class DialogoAcercaDe extends Activity
{		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button aceptarBtn = null;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_acerca_de);
							    
		//*******
		//BOTONES
		//*******
				
		aceptarBtn = (Button)findViewById(R.id.aceptarAcercaDe);
		aceptarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				cancelarDialogo();
			}
		});
	}	
	
	/**
	 * Termina la activity
	 */
	private void cancelarDialogo()
	{
		finish();
	}
}