package com.technicalnorms.intraza.interfaz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.technicalnorms.intraza.Configuracion;
import com.technicalnorms.intraza.R;

/**
 * Activity que solicita al usuario la confirmación para realizar la sincronización con la BD de InTraza
 * crear un pedido nuevo.
 * 
 * @author JLZS
 *
 */
public class DialogoConfirmacionSincronizar extends Activity
{	
	//Guardamos el contexto
	private Context contexto = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button continuarBtn = null;
		Button cancelarBtn = null;
		String mensaje = null;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_confirmacion_sincronizar);
		
		this.contexto = this;
		
		//Formamos el mensaje a mostrar al usuario
		mensaje = "La última sincronización se realizó el "+Configuracion.dameUltimaFechaSincronizacion(this)+".\n\n"+
				  "Se va a proceder a la sincronización de datos con InTraza.\n¿Desea continuar?";
		((TextView)findViewById(R.id.textoMensajeAvisoDCS)).setText(mensaje);
			    
		//*******
		//BOTONES
		//*******
				
		continuarBtn = (Button)findViewById(R.id.continuarBotonDCS);
		continuarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{		
				boolean usar3G = ((ToggleButton)findViewById(R.id.botonUsar3gDCS)).isChecked();
				returnContinuar(usar3G);
			}
		});
		
		cancelarBtn = (Button)findViewById(R.id.cancelarBotonDCS);
		cancelarBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{		
				cancelarDialogo();
			}
		});		
	}	
	
	
	/**
	 * - Devuelve a la activity que lo solicito que el usuario ha pulsado el boton CANCELAR.
	 * - Finaliza la activity.
	 * 
	 * @param usar3G
	 */
	private void returnContinuar(boolean usar3G)
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_OK;

		intent = new Intent(this, com.technicalnorms.intraza.InTrazaActivity.class);
		intent.putExtra("USAR_3G", usar3G);

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