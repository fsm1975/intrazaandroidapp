package com.technicalnorms.intraza.interfaz.datos;

import com.technicalnorms.intraza.Constantes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Almacena los datos que definen un pedido
 * @author JLZS
 *
 */
public class DatosPedido implements Parcelable
{
	private int idPedido = 0;
	private int idCliente = 0; 
	private String cliente = null;
	private int diaFechaPedido = 0;
	private int mesFechaPedido = 0;
	private int anioFechaPedido = 0;
	private int diaFechaEntrega = 0;
	private int mesFechaEntrega = 0;
	private int anioFechaEntrega = 0;
	private float precioTotal = 0;
	private String observaciones = null;
	private int fijarObservaciones = 0;
	
	//Indica si el pedido tiene algun cambio en los datos generales del pedido o en las lineas de pedido sin guardar
	private int hayDatosPedidoSinGuardar = 0;
	
	//Indica si el pedido tiene lineas de pedido, sin lineas de pedido no se permite guardar el pedido
	private int hayLineasPedido = 0;
	
	/**
	 * Constructor.
	 * 
	 * @param idPedido
	 * @param idCliente
	 * @param cliente
	 * @param diaFechaPedido
	 * @param mesFechaPedido
	 * @param anioFechaPedido
	 * @param diaFechaEntrega
	 * @param mesFechaEntrega
	 * @param anioFechaEntrega
	 * @param observaciones
	 * @param fijarObservaciones
	 */
	public DatosPedido(int idPedido, int idCliente, String cliente, int diaFechaPedido, int mesFechaPedido, int anioFechaPedido, int diaFechaEntrega, int mesFechaEntrega, int anioFechaEntrega, float precioTotal, String observaciones, boolean fijarObservaciones)
	{
		this.idPedido = idPedido;
		this.idCliente = idCliente;
		this.cliente = cliente;
		this.diaFechaPedido = diaFechaPedido;
		this.mesFechaPedido = mesFechaPedido;
		this.anioFechaPedido = anioFechaPedido;
		this.diaFechaEntrega = diaFechaEntrega;
		this.mesFechaEntrega = mesFechaEntrega;
		this.anioFechaEntrega = anioFechaEntrega;
		this.precioTotal = precioTotal;
		this.observaciones = observaciones;
		this.setFijarObservaciones(fijarObservaciones);
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

	public void setDiaFechaPedido(int diaFechaPedido) {
		this.diaFechaPedido = diaFechaPedido;
	}

	public void setMesFechaPedido(int mesFechaPedido) {
		this.mesFechaPedido = mesFechaPedido;
	}

	public void setAnioFechaPedido(int anioFechaPedido) {
		this.anioFechaPedido = anioFechaPedido;
	}

	public void setDiaFechaEntrega(int diaFechaEntrega) {
		this.diaFechaEntrega = diaFechaEntrega;
	}

	public void setMesFechaEntrega(int mesFechaEntrega) {
		this.mesFechaEntrega = mesFechaEntrega;
	}

	public void setAnioFechaEntrega(int anioFechaEntrega) {
		this.anioFechaEntrega = anioFechaEntrega;
	}
	
	public void setPrecioTotal(float precioTotal)
	{
		this.precioTotal = precioTotal;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	
	public int getDiaFechaPedido() {
		return diaFechaPedido;
	}

	public int getMesFechaPedido() {
		return mesFechaPedido;
	}

	public int getAnioFechaPedido() {
		return anioFechaPedido;
	}
	
	public int getDiaFechaEntrega()
	{
		return this.diaFechaEntrega;
	}
	
	public int getMesFechaEntrega()
	{
		return this.mesFechaEntrega;
	}
	
	public int getAnioFechaEntrega()
	{
		return this.anioFechaEntrega;
	}

	public String getFechaPedido()
	{
		return rellena0(this.diaFechaPedido)+Constantes.SEPARADOR_FECHA+rellena0(this.mesFechaPedido)+Constantes.SEPARADOR_FECHA+this.anioFechaPedido;
	}
	
	public String getFechaEntrega()
	{
		return rellena0(this.diaFechaEntrega)+Constantes.SEPARADOR_FECHA+rellena0(this.mesFechaEntrega)+Constantes.SEPARADOR_FECHA+this.anioFechaEntrega;
	}
	
	public float getPrecioTotal()
	{
		return this.precioTotal;
	}
	
	public String getObservaciones()
	{
		return this.observaciones;
	}
	
	public void setFijarObservaciones(boolean fijar) 
	{
		if (fijar)
		{
			this.fijarObservaciones = 1;			
		}
		else
		{
			this.fijarObservaciones = 0;
		}
	}
	
	public boolean getFijarObservaciones() 
	{
		if (this.fijarObservaciones==0)
		{
			return false;			
		}
		else
		{
			return true;
		}
	}
		
	// ******************
	// Metodos auxiliares
	// ******************
	
	/**
	 * Dato un valor entero lo formatea para que tenga 2 digitos justificando a 0 por la izquierda
	 * @param valor
	 * @return
	 */
	private String rellena0(int valor)
	{
		String resultado = null;
		
		if (valor>=0 && valor<=9)
		{
			resultado = "0"+valor;
		}
		else
		{
			resultado = new Integer(valor).toString();
		}
		
		return resultado;
	}
	
	public void setHayDatosPedidoSinGuardar(boolean pedidosSinGuardar) 
	{
		if (pedidosSinGuardar)
		{
			this.hayDatosPedidoSinGuardar = 1;			
		}
		else
		{
			this.hayDatosPedidoSinGuardar = 0;
		}
	}
	
	public boolean hayDatosPedidoSinGuardar() 
	{
		if (this.hayDatosPedidoSinGuardar==0)
		{
			return false;			
		}
		else
		{
			return true;
		}
	}
	
	public void setHayLineasPedido(boolean hayPedidos) 
	{
		if (hayPedidos)
		{
			this.hayLineasPedido = 1;			
		}
		else
		{
			this.hayLineasPedido = 0;
		}
	}
	
	public boolean hayLineasPedido() 
	{
		if (this.hayLineasPedido==0)
		{
			return false;			
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Dato un objeto comprueba si sus datos son iguales.
	 * 
	 * @param datos pedido
	 * 
	 * @return true si son iguales o false en caso contrario
	 */
	public boolean esIgual(DatosPedido dp)
	{
		boolean resultado = false;
		
		if (this.getIdCliente() == dp.getIdCliente() &&
			this.getCliente().equals(dp.getCliente()) &&
			this.getFechaEntrega().equals(dp.getFechaEntrega()) &&
			this.getObservaciones().equals(dp.getObservaciones()) &&
			this.getFijarObservaciones() == dp.getFijarObservaciones())
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
        out.writeInt(this.diaFechaPedido);
        out.writeInt(this.mesFechaPedido);
        out.writeInt(this.anioFechaPedido);
        out.writeInt(this.diaFechaEntrega);
        out.writeInt(this.mesFechaEntrega);
        out.writeInt(this.anioFechaEntrega);
        out.writeFloat(this.precioTotal);
        out.writeString(this.observaciones);
        out.writeInt(this.fijarObservaciones);
        out.writeInt(this.hayDatosPedidoSinGuardar);
        out.writeInt(this.hayLineasPedido);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<DatosPedido> CREATOR = new Parcelable.Creator<DatosPedido>() 
    {
        public DatosPedido createFromParcel(Parcel in) 
        {
            return new DatosPedido(in);
        }

        public DatosPedido[] newArray(int size) 
        {
            return new DatosPedido[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private DatosPedido(Parcel in) 
    {
    	//You must do this in the same order you put them in (that is, in a FIFO approach)
        this.idPedido = in.readInt();
        this.idCliente = in.readInt();
        this.cliente = in.readString();
        this.diaFechaPedido = in.readInt();
        this.mesFechaPedido = in.readInt();
        this.anioFechaPedido = in.readInt();
        this.diaFechaEntrega = in.readInt();
        this.mesFechaEntrega = in.readInt();
        this.anioFechaEntrega = in.readInt();
        this.precioTotal = in.readFloat();
        this.observaciones = in.readString();
        this.fijarObservaciones = in.readInt();
        this.hayDatosPedidoSinGuardar = in.readInt();
        this.hayLineasPedido = in.readInt();
    }
}
