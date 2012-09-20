package com.technicalnorms.intraza.interfaz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.technicalnorms.intraza.interfaz.datos.DatosDialogoMensaje;
import com.technicalnorms.intraza.R;

/**
 * Activity que muestra al usuario las observaciones del pedido con boton aceptar.
 * 
 * @author JLZS
 *
 */
public class DialogoMuestraObservacionesPedido extends Activity
{	
	//Contiene la clase activity que solicito el subdialogo
	String activity = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button aceptarBtn = null;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_muestra_observaciones_pedido);
				
		//Se actualizan los widget del layout con los datos
		((TextView)findViewById(R.id.tituloDMOP)).setText(this.getIntent().getExtras().getString("ID_PEDIDO"));
		((EditText)findViewById(R.id.observacionesDMOP)).setText(this.getIntent().getExtras().getString("OBSERVACIONES"));
			
		//En el xml que define el widget para mostrar las observaciones, ya se han indicado las propiedades para que no sea editable
			    
		//*******
		//BOTONES
		//*******
				
		aceptarBtn = (Button)findViewById(R.id.aceptarDMOP);
		aceptarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				finalizar();
			}
		});
		
		//Para que la pantalla de fondo no se vea
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	}	
	
	/**
	 * - Finaliza la activity.
	 */
	private void finalizar()
	{
		finish();
	}
}