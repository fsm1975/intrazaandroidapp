package com.technicalnorms.intraza.task;

import java.util.ArrayList;

/**
 * Almacena los datos que definen un pedido en la BD
 * @author JLZS
 *
 */
public class JsonPedido
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
	private String observaciones = null;
	private boolean fijarObservaciones = false;
	private ArrayList<JsonLineaPedido> lineasPedido = null;
	
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
	public JsonPedido(int idPedido, int idCliente, String cliente, int diaFechaPedido, int mesFechaPedido, int anioFechaPedido, int diaFechaEntrega, int mesFechaEntrega, int anioFechaEntrega, String observaciones, boolean fijarObservaciones)
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
		this.observaciones = observaciones;
		this.fijarObservaciones = fijarObservaciones;
		
		this.lineasPedido = new ArrayList<JsonLineaPedido>();
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

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public void setFijarObservaciones(boolean fijar) {
		this.fijarObservaciones = fijar;
	}
	
	public void setLineasPedido(ArrayList<JsonLineaPedido> lineasPedido) {
		this.lineasPedido = lineasPedido;
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
	
	public String getObservaciones()
	{
		return this.observaciones;
	}
	
	public boolean getFijarObservaciones()
	{
		return this.fijarObservaciones;
	}
	
	public ArrayList<JsonLineaPedido> getLineasPedido()
	{
		return this.lineasPedido;
	}
	
	
}
