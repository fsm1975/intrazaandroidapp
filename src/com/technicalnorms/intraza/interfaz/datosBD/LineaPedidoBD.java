package com.technicalnorms.intraza.interfaz.datosBD;

/**
 * Almacena los datos que definen una linea de pedido en la BD
 * @author JLZS
 *
 */
public class LineaPedidoBD
{
	private int idPrepedido = 0;
	private String codArticulo = null;
	private boolean esMedidaEnKg = false;
	private boolean esCongelado = false;
	private float cantidadKg = -1;
	private int cantidadUd = 0;
	private float precio = 0;
	private String observaciones = null;
	boolean fijarPrecio = false;
	boolean suprimirPrecio = false;
	boolean fijarArticulo = false;
	boolean fijarObservaciones = false;

	/**
	 * Constructor
	 * 
	 * @param idPrepedido
	 * @param codArticulo
	 * @param medidaEnKg
	 * @param esCongelado
	 * @param cantidadKg
	 * @param cantidadUd
	 * @param precio
	 * @param observaciones
	 * @param fijarPrecio
	 * @param suprimirPrecio
	 * @param fijarArticulo
	 * @param fijarObservaciones
	 */
	public LineaPedidoBD(int idPrepedido, String codArticulo, boolean esMedidaEnKg, boolean esCongelado, float cantidadKg, int cantidadUd, float precio, String observaciones, boolean fijarPrecio, boolean suprimirPrecio, boolean fijarArticulo, boolean fijarObservaciones)
	{
		this.idPrepedido = idPrepedido;
		this.codArticulo = codArticulo;
		this.esMedidaEnKg = esMedidaEnKg;
		this.esCongelado = esCongelado;
		this.cantidadKg = cantidadKg;
		this.cantidadUd = cantidadUd;
		this.precio = precio;
		this.observaciones = observaciones;
		this.fijarPrecio = fijarPrecio;
		this.suprimirPrecio = suprimirPrecio;
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
	
	public boolean esMedidaEnKg() {
		return esMedidaEnKg;
	}

	public void setEsMedidaEnKg(boolean esMedidaEnKg) {
		this.esMedidaEnKg = esMedidaEnKg;
	}
	
	public boolean esCongelado() {
		return esCongelado;
	}

	public void setEsCongelado(boolean esCongelado) {
		this.esCongelado = esCongelado;
	}

	public float getCantidadKg() {
		return cantidadKg;
	}

	public void setCantidadKg(float cantidad) {
		this.cantidadKg = cantidad;
	}
	
	public int getCantidadUd() {
		return cantidadUd;
	}

	public void setCantidadUd(int cantidad) {
		this.cantidadUd = cantidad;
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
	
	public boolean getSuprimirPrecio() {
		return suprimirPrecio;
	}

	public void setSuprimirPrecio(boolean suprimir) {
		this.suprimirPrecio = suprimir;
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
	
	// ******************
	// Metodos auxiliares
	// ******************
	
}
