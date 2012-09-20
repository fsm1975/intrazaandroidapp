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

import com.technicalnorms.intraza.R;

/**
 * Activity que solicita al usuario la confirmación de la acción a tomar cuando hay datos pendientes de guardar en el pedido y solicita
 * crear un pedido nuevo.
 * 
 * @author JLZS
 *
 */
public class DialogoConfirmacionEnviarPedidos extends Activity
{	
	//Id y cliente de los pedidos a enviar
	private String[] idPedidos = null;
	private String[] clientePedidos = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Button siBtn = null;
		Button noBtn = null;
		String mensajeAviso = "";
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogo_confirmacion_enviar_pedidos);

		//Obtenemos los datos de los pedidos a enviar del intent
		this.idPedidos = this.getIntent().getExtras().getStringArray("ARRAY_ID_PEDIDOS");
		this.clientePedidos = this.getIntent().getExtras().getStringArray("ARRAY_CLIENTE_PEDIDOS");
		
		//Formamos el mensaje de aviso a mostar, mostramos la información de todos los pedidos a enviar
		for (int i=0; i<idPedidos.length; i++)
		{
			mensajeAviso += this.idPedidos[i]+" - "+this.clientePedidos[i];
					
			if (i!=idPedidos.length-1)
			{
				mensajeAviso += "\n";
			}
		}
		
		((TextView)findViewById(R.id.textoScrollMensajeAvisoDCEP)).setText(mensajeAviso);
			    
		//*******
		//BOTONES
		//*******
				
		siBtn = (Button)findViewById(R.id.siBotonDCEP);
		siBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{		
				returnSi();
			}
		});
		
		noBtn = (Button)findViewById(R.id.noBotonDCEP);
		noBtn.setOnClickListener(new OnClickListener()
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
		Bundle bundle = null;
		int resultadoActivity = Activity.RESULT_OK;

		//Enviamos los id de los pedidos a enviar a la activity principal
		bundle = new Bundle();
		bundle.putStringArray("ARRAY_ID_PEDIDOS", this.idPedidos);
		intent = new Intent(this, com.technicalnorms.intraza.interfaz.PantallaListaPedidos.class);
		intent.putExtras(bundle);

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