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
	//Almacena la URL de sincronizacion con los web services
	private EditText uriSincronizacion = null;
	
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
		
		this.uriSincronizacion = (EditText)findViewById(R.id.editUrlSincronizacionDCS);
		this.uriSincronizacion.setText(Configuracion.dameUriWebServicesSincronizacion(this));
		
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
				//Comprobamos si el usuario ha cambiado la URL de sincronizacion para almacenarla en la BD
				if (!uriSincronizacion.getText().toString().equals(Configuracion.dameUriWebServicesSincronizacion(contexto)))
				{
					Configuracion.ponUriWebServicesSincronizacion(contexto, uriSincronizacion.getText().toString());
				}
				
				returnContinuar();
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
	 */
	private void returnContinuar()
	{
		Intent intent = null;
		int resultadoActivity = Activity.RESULT_OK;

		intent = new Intent(this, com.technicalnorms.intraza.InTrazaActivity.class);

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