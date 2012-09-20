package com.technicalnorms.intraza.interfaz.datos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Almacena los datos que configuran el dialogo mensaje
 * @author JLZS
 *
 */
public class DatosDialogoMensaje implements Parcelable
{
	private String activity = null;
	private String titulo = null;
	private String informacion = null;
	
	/**
	 * Constructor.
	 * 
	 * @param idPedido
	 * @param cliente
	 */
	public DatosDialogoMensaje(String activity, String titulo, String informacion)
	{
		this.activity = activity;
		this.titulo = titulo;
		this.informacion = informacion;
	}
	
	// ***********************
	// METODOS GETTER Y SETTER
	// ***********************
	
	public void setActivity(String activity) {
		this.activity = activity;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	public String getActivity()
	{
		return this.activity;
	}
	
	public String getTitulo()
	{
		return this.titulo;
	}
	
	public String getInformacion()
	{
		return this.informacion;
	}
			
	// ********************************************************************************************
	// Codigo para implementar Parcelable y poder pasar el objeto en el Intent a la siguiente Activity
	// ********************************************************************************************
	
    // 99.9% of the time you can just ignore this
    public int describeContents() 
    {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) 
    {
        out.writeString(this.activity);
        out.writeString(this.titulo);
        out.writeString(this.informacion);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<DatosDialogoMensaje> CREATOR = new Parcelable.Creator<DatosDialogoMensaje>() 
    {
        public DatosDialogoMensaje createFromParcel(Parcel in) 
        {
            return new DatosDialogoMensaje(in);
        }

        public DatosDialogoMensaje[] newArray(int size) 
        {
            return new DatosDialogoMensaje[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private DatosDialogoMensaje(Parcel in) 
    {
    	//You must do this in the same order you put them in (that is, in a FIFO approach)
        this.activity = in.readString();
        this.titulo = in.readString();
        this.informacion = in.readString();
    }
}
