package com.technicalnorms.intraza.interfaz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
	TextView urlTextView = null;
	TextView emailTextView = null;
	
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
		
		
		//********
		//TEXTVIEW
		//********
		
		urlTextView = (TextView)findViewById(R.id.informacionAcercaDe2);
		urlTextView.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				mostrarWeb(urlTextView.getText().toString());
			}
		});
		
		emailTextView = (TextView)findViewById(R.id.informacionAcercaDe6);
		emailTextView.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				enviarCorreo(emailTextView.getText().toString());
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
	
	/**
	 * Abre un navegador con la URL indicada en el parametro de entrada.
	 * 
	 * @param url
	 */
	private void mostrarWeb(String url)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
		startActivity(intent);
	}
	
	/**
	 * Envia un correo a la direccion de correo indicada en el parametro.
	 * @param dirEmail
	 */
	private void enviarCorreo(String dirEmail)
	{
		String to = dirEmail;
		String subject = "InTraza Android";
		String message = "";

		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
		//email.putExtra(Intent.EXTRA_CC, new String[]{ to});
		//email.putExtra(Intent.EXTRA_BCC, new String[]{to});
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		email.putExtra(Intent.EXTRA_TEXT, message);

		//need this to prompts email client only
		email.setType("message/rfc822");

		startActivity(Intent.createChooser(email, "Elige un programa para enviar el correo:"));
	}
	
}