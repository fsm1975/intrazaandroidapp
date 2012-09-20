package com.technicalnorms.intraza.interfaz.datosBD;

import java.util.Vector;

/**
 * Almacena los datos que definen un pedido en la BD
 * @author JLZS
 *
 */
public class PedidoBD
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
	private Vector<LineaPedidoBD> lineasPedido = null;
	
	//Para control de la lista de pedidos mostrada en pantalla
	private boolean checkBoxEnviar = false;
	private boolean checkBoxBorrar = false;
	
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
	public PedidoBD(int idPedido, int idCliente, String cliente, int diaFechaPedido, int mesFechaPedido, int anioFechaPedido, int diaFechaEntrega, int mesFechaEntrega, int anioFechaEntrega, String observaciones, boolean fijarObservaciones)
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
		
		this.lineasPedido = new Vector<LineaPedidoBD>();
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
	
	public void setLineasPedido(Vector<LineaPedidoBD> lineasPedido)
	{
		this.lineasPedido = lineasPedido;
	}
	
	public void setCheckBoxEnviar(boolean check)
	{
		this.checkBoxEnviar = check;
	}
	
	public void setCheckBoxBorrar(boolean check)
	{
		this.checkBoxBorrar = check;
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
		return rellena0(this.diaFechaPedido)+"/"+rellena0(this.mesFechaPedido)+"/"+this.anioFechaPedido;
	}
	
	public String getFechaEntrega()
	{
		return rellena0(this.diaFechaEntrega)+"/"+rellena0(this.mesFechaEntrega)+"/"+this.anioFechaEntrega;
	}
	
	/**
	 * El precio total se obtiene de las lineas de pedido
	 * @return
	 */
	public float getPrecioTotal()
	{
		float precioTotal = 0;
		
		for (int i=0; i<this.lineasPedido.size(); i++)
		{
			precioTotal += this.lineasPedido.elementAt(i).getCantidad() * this.lineasPedido.elementAt(i).getPrecio(); 
		}
		
		return precioTotal;
	}
	
	public String getObservaciones()
	{
		return this.observaciones;
	}
	
	public boolean getFijarObservaciones()
	{
		return this.fijarObservaciones;
	}
	
	public Vector<LineaPedidoBD> getLineasPedido()
	{
		return this.lineasPedido;
	}
	
	public boolean getCheckBoxEnviar()
	{
		return this.checkBoxEnviar;
	}
	
	public boolean getCheckBoxBorrar()
	{
		return this.checkBoxBorrar;
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
	
	/**
	 * Indica si hay observaciones en el pedido
	 * 
	 * @return true si hay observaciones, o false en caso contrario
	 */
	public boolean hayObservaciones()
	{
		boolean resultado = false;
		
		if (this.observaciones!=null && !this.observaciones.trim().equals(""))
		{
			resultado = true;
		}
		
		return resultado;
	}
	
}
