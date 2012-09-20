package com.technicalnorms.intraza.task;

/**
 * Almacena los datos que definen una linea de pedido en la BD
 * @author JLZS
 *
 */
public class JsonLineaPedido
{
	private int idPrepedido = 0;
	private String codArticulo = null;
	private String nombreArticulo = null;
	private float cantidad = 0;
	private float precio = 0;
	private String observaciones = null;
	boolean fijarPrecio = false;
	boolean fijarArticulo = false;
	boolean fijarObservaciones = false;

	/**
	 * Constructor
	 * 
	 * @param idPrepedido
	 * @param codArticulo
	 * @param nombreArticulo
	 * @param cantidad
	 * @param precio
	 * @param observaciones
	 * @param fijarPrecio
	 * @param fijarArticulo
	 * @param fijarObservaciones
	 */
	public JsonLineaPedido(int idPrepedido, String codArticulo, String nombreArticulo, float cantidad, float precio, String observaciones, boolean fijarPrecio, boolean fijarArticulo, boolean fijarObservaciones)
	{
		this.idPrepedido = idPrepedido;
		this.codArticulo = codArticulo;
		this.nombreArticulo = nombreArticulo;
		this.cantidad = cantidad;
		this.precio = precio;
		this.observaciones = observaciones;
		this.fijarPrecio = fijarPrecio;
		this.fijarArticulo = fijarArticulo;
		this.fijarObservaciones = fijarObservaciones;
	}
	
	// ***********************
	// METODOS GETTER Y SETTER
	// ***********************
	
	public int getIdPrepedido() {
		return idPrepedido;
	}
	
	public void setIdPrepedido(int idPrepedido) {
		this.idPrepedido = idPrepedido;
	}
	
	public String getCodArticulo() {
		return codArticulo;
	}

	public void setCodArticulo(String codArticulo) {
		this.codArticulo = codArticulo;
	}
	
	public String getNombreArticulo() {
		return nombreArticulo;
	}

	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}
	
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public boolean getFijarPrecio() {
		return fijarPrecio;
	}

	public void setFijarPrecio(boolean fijar) {
		this.fijarPrecio = fijar;
	}
	
	public boolean getFijarArticulo() {
		return fijarArticulo;
	}

	public void setFijarArticulo(boolean fijar) {
		this.fijarArticulo = fijar;
	}
	
	public boolean getFijarObservaciones() {
		return fijarObservaciones;
	}

	public void setFijarObservaciones(boolean fijar) {
		this.fijarObservaciones = fijar;
	}	
}
