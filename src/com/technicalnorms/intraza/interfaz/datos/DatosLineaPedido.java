package com.technicalnorms.intraza.interfaz.datos;

import android.os.Parcel;
import android.os.Parcelable;

import com.technicalnorms.intraza.Constantes;

/**
 * Almacena los datos que definen una linea de pedido
 * @author JLZS
 *
 */
public class DatosLineaPedido implements Parcelable
{
	private String codArticulo = null;
	private String articulo = null;
	private String medida = Constantes.KILOGRAMOS;
	private int esCongelado = 0; 
	private String ultimaFecha = null;
	private float ultimaCantidad = 0;
	private float ultimaTarifa = 0;
	private float cantidad = 0;
	private float cantidadTotalAnio = 0;
	private float tarifaCliente = 0;
	private float tarifaLista = 0;
	private String fechaCambioTarifaLista = null;
	private String observaciones = null;
	private int fijarTarifa = 0;
	private int fijarArticulo = 0;
	private int fijarObservaciones = 0;
	
	//Indica si estos datos se han guardado en la bd de la tablet
	private int hayCambiosSinGuardar = 0;

	/**
	 * Constructor
	 * 
	 * @param referencia
	 * @param articulo
	 * @param medida
	 * @param esCongelado
	 * @param ultimaFecha
	 * @param ultimaCantidad
	 * @param ultimaTarifa
	 * @param cantidad
	 * @param cantidadTotalAnio
	 * @param tarifaCliente
	 * @param tarifaLista
	 * @param fechaCambioTarifaLista
	 * @param observaciones
	 */
	public DatosLineaPedido(String codArticulo, String articulo, String medida, boolean esCongelado, String ultimaFecha, float ultimaCantidad, float cantidadTotalAnio, float ultimaTarifa, float cantidad, float tarifaCliente, float tarifaLista, String fechaCambioTarifaLista, String observaciones)
	{
		this.codArticulo = codArticulo;
		this.articulo = articulo;
		this.medida = medida;
		this.setEsCongelado(esCongelado);
		this.ultimaFecha = ultimaFecha;
		this.ultimaCantidad = ultimaCantidad;
		this.ultimaTarifa = ultimaTarifa;
		this.cantidad = cantidad;
		this.cantidadTotalAnio = cantidadTotalAnio;
		this.tarifaCliente = tarifaCliente;
		this.tarifaLista = tarifaLista;
		this.fechaCambioTarifaLista = fechaCambioTarifaLista;
		this.observaciones = observaciones;
	}
	
	// ***********************
	// METODOS GETTER Y SETTER
	// ***********************
	
	public String getCodArticulo() {
		return codArticulo;
	}

	public void setCodArticulo(String referencia) {
		this.codArticulo = referencia;
	}

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getMedida() {
		return medida;
	}

	public void setMedida(String medida) {
		this.medida = medida;
	}
	
	public boolean getEsCongelado() {
		if (this.esCongelado==0)
		{
			return false;			
		}
		else
		{
			return true;
		}
	}

	public void setEsCongelado(boolean esCongelado) 
	{
		if (esCongelado)
		{
			this.esCongelado = 1;			
		}
		else
		{
			this.esCongelado = 0;
		}
	}

	public String getUltimaFecha() {
		return ultimaFecha;
	}

	public void setUltimaFecha(String ultimaFecha) {
		this.ultimaFecha = ultimaFecha;
	}

	public float getUltimaCantidad() {
		return ultimaCantidad;
	}

	public void setUltimaCantidad(float ultimaCantidad) {
		this.ultimaCantidad = ultimaCantidad;
	}

	public float getUltimaTarifa() {
		return ultimaTarifa;
	}

	public void setUltimaTarifa(float ultimaTarifa) {
		this.ultimaTarifa = ultimaTarifa;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}
	
	public float getCantidadTotalAnio() {
		return cantidadTotalAnio;
	}

	public void setCantidadTotalAnio(float cantidadTotalAnio) {
		this.cantidadTotalAnio = cantidadTotalAnio;
	}

	public float getTarifaCliente() {
		return tarifaCliente;
	}

	public void setTarifaCliente(float tarifaCliente) {
		this.tarifaCliente = tarifaCliente;
	}

	public float getPrecio() {
		return this.cantidad * this.tarifaCliente;
	}

	public float getTarifaLista() {
		return tarifaLista;
	}

	public void setTarifaLista(float tarifaLista) {
		this.tarifaLista = tarifaLista;
	}
	
	public String getFechaCambioTarifaLista() {
		return fechaCambioTarifaLista;
	}

	public void setFechaCambioTarifaLista(String fecha) {
		this.fechaCambioTarifaLista = fecha;
	}
	
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public boolean getFijarTarifa() {
		if (this.fijarTarifa==0)
		{
			return false;			
		}
		else
		{
			return true;
		}
	}

	public void setFijarTarifa(boolean fijar) 
	{
		if (fijar)
		{
			this.fijarTarifa = 1;			
		}
		else
		{
			this.fijarTarifa = 0;
		}
	}
	
	public boolean getFijarArticulo() {
		if (this.fijarArticulo==0)
		{
			return false;			
		}
		else
		{
			return true;
		}
	}

	public void setFijarArticulo(boolean fijar) 
	{
		if (fijar)
		{
			this.fijarArticulo = 1;			
		}
		else
		{
			this.fijarArticulo = 0;
		}
	}
	
	public boolean getFijarObservaciones() {
		if (this.fijarObservaciones==0)
		{
			return false;			
		}
		else
		{
			return true;
		}
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
	
	// ******************
	// Metodos auxiliares
	// ******************
	
	public void setHayCambiosSinGuardar(boolean cambiosSinGuardar) 
	{
		if (cambiosSinGuardar)
		{
			this.hayCambiosSinGuardar = 1;			
		}
		else
		{
			this.hayCambiosSinGuardar = 0;
		}
	}
	
	public boolean hayCambiosSinGuardar() 
	{
		if (this.hayCambiosSinGuardar==0)
		{
			return false;			
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Dato un objeto comprueba si sus datos son iguales
	 * @param dlp
	 * 
	 * @return true si son iguales o false en caso contrario
	 */
	public boolean esIgual(DatosLineaPedido dlp)
	{
		boolean resultado = false;
		
		if (this.getCodArticulo().equals(dlp.getCodArticulo()) &&
			this.getArticulo().equals(dlp.getArticulo()) &&
			this.getMedida().equals(dlp.getMedida()) &&
			this.getEsCongelado() == dlp.getEsCongelado() &&
			this.getUltimaFecha().equals(dlp.getUltimaFecha()) &&
			this.getUltimaCantidad() == dlp.getUltimaCantidad() &&
			this.getUltimaTarifa() == dlp.getUltimaTarifa() &&
			this.getCantidad() == dlp.getCantidad() &&
			this.getCantidadTotalAnio() == dlp.getCantidadTotalAnio() &&
			this.getTarifaCliente() == dlp.getTarifaCliente() &&
			this.getTarifaLista() == dlp.getTarifaLista() &&
			this.getObservaciones().equals(dlp.getObservaciones()) &&
			this.getFijarTarifa() == dlp.getFijarTarifa() &&
			this.getFijarObservaciones() == dlp.getFijarObservaciones())
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
        out.writeString(this.codArticulo);
        out.writeString(this.articulo);
        out.writeString(this.medida);
        out.writeInt(this.esCongelado);
        out.writeString(this.ultimaFecha);
        out.writeFloat(this.ultimaCantidad);
        out.writeFloat(this.ultimaTarifa);
        out.writeFloat(this.cantidad);
        out.writeFloat(this.cantidadTotalAnio);
        out.writeFloat(this.tarifaCliente);
        out.writeFloat(this.tarifaLista);
        out.writeString(this.fechaCambioTarifaLista);
        out.writeString(this.observaciones);
        out.writeInt(this.fijarTarifa);
        out.writeInt(this.fijarArticulo);
        out.writeInt(this.fijarObservaciones);
        out.writeInt(this.hayCambiosSinGuardar);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<DatosLineaPedido> CREATOR = new Parcelable.Creator<DatosLineaPedido>() 
    {
        public DatosLineaPedido createFromParcel(Parcel in) 
        {
            return new DatosLineaPedido(in);
        }

        public DatosLineaPedido[] newArray(int size) 
        {
            return new DatosLineaPedido[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private DatosLineaPedido(Parcel in) 
    {
    	//You must do this in the same order you put them in (that is, in a FIFO approach)
        this.codArticulo = in.readString();
        this.articulo = in.readString();
        this.medida = in.readString();
        this.esCongelado = in.readInt();
        this.ultimaFecha = in.readString();
        this.ultimaCantidad = in.readFloat();
        this.ultimaTarifa = in.readFloat();
        this.cantidad = in.readFloat();
        this.cantidadTotalAnio = in.readFloat();
        this.tarifaCliente = in.readFloat();
        this.tarifaLista = in.readFloat();
        this.fechaCambioTarifaLista = in.readString();
        this.observaciones = in.readString();
        this.fijarTarifa = in.readInt();
        this.fijarArticulo = in.readInt();
        this.fijarObservaciones = in.readInt();
        this.hayCambiosSinGuardar = in.readInt();
    }
}
