package com.technicalnorms.intraza.interfaz.datos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Almacena los datos que definen un pedido
 * @author JLZS
 *
 */
public class DatosConsultaPedidos implements Parcelable
{
	private int idPedido = 0;
	private int idCliente = 0;
	private String cliente = null;
	
	/**
	 * Constructor.
	 * 
	 * @param idPedido
	 * @param cliente
	 */
	public DatosConsultaPedidos(int idPedido, int idCliente, String cliente)
	{
		this.idPedido = idPedido;
		this.idCliente = idCliente;
		this.cliente = cliente;
	}
	
	// ***********************
	// METODOS GETTER Y SETTER
	// ***********************
	
	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}
	
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public int getIdPedido()
	{
		return this.idPedido;
	}
	
	public int getIdCliente()
	{
		return this.idCliente;
	}
	
	public String getCliente()
	{
		return this.cliente;
	}
			
	// ******************
	// Metodos auxiliares
	// ******************	
	
	/**
	 * Indica si hay dato de pedido.
	 * 
	 * @return true si tenemos dato pedido, false en caso contrario
	 */
	public boolean hayDatoIdPedido()
	{
		boolean resultado = false;
		
		if (this.idPedido != 0)
		{
			resultado = true;
		}
		
		return resultado;
	}
	
	/**
	 * Indica si hay dato cliente.
	 * 
	 * @return true si tenemos dato cliente, false en caso contrario
	 */
	public boolean hayDatoCliente()
	{
		boolean resultado = false;
		
		if (this.idCliente != 0)
		{
			resultado = true;
		}
		
		return resultado;
	}
	
	/**
	 * Dato un objeto comprueba si sus datos son iguales.
	 * 
	 * @param datos consulta pedido
	 * 
	 * @return true si son iguales o false en caso contrario
	 */
	public boolean esIgual(DatosConsultaPedidos dcp)
	{
		boolean resultado = false;
		
		if (this.getIdPedido() == dcp.getIdPedido() && 
			this.getIdCliente() == dcp.getIdCliente())
		{
			resultado = true;
		}
		
		return resultado;
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
        out.writeInt(this.idPedido);
        out.writeInt(this.idCliente);
        out.writeString(this.cliente);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<DatosConsultaPedidos> CREATOR = new Parcelable.Creator<DatosConsultaPedidos>() 
    {
        public DatosConsultaPedidos createFromParcel(Parcel in) 
        {
            return new DatosConsultaPedidos(in);
        }

        public DatosConsultaPedidos[] newArray(int size) 
        {
            return new DatosConsultaPedidos[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private DatosConsultaPedidos(Parcel in) 
    {
    	//You must do this in the same order you put them in (that is, in a FIFO approach)
        this.idPedido = in.readInt();
        this.idCliente = in.readInt();
        this.cliente = in.readString();
    }
}
