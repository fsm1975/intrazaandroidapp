package com.technicalnorms.intraza.interfaz.datosBD;

/**
 * Almacena los datos que definen un parametro de configuracion en la BD
 * @author JLZS
 *
 */
public class ParametroConfiguracionBD
{
	private String nombre = null;
	private String valor = null;
	private String descripcion = null;
	private boolean esEditable = true;

	/**
	 * Constructor.
	 * 
	 * @param nombre
	 * @param valor
	 * @param descripcion
	 * @param esEditable
	 */
	public ParametroConfiguracionBD(String nombre, String valor, String descripcion, boolean esEditable)
	{
		this.nombre = nombre;
		this.valor = valor;
		this.descripcion = descripcion;
		this.esEditable = esEditable;
	}
	
	// ***********************
	// METODOS GETTER Y SETTER
	// ***********************
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setEsEditable(boolean esEditable) {
		this.esEditable = esEditable;
	}

	public String getNombre()
	{
		return this.nombre;
	}
	
	public String getValor()
	{
		return this.valor;
	}
	
	public String getDescripcion()
	{
		return this.descripcion;
	}	
	
	public boolean getEsEditable()
	{
		return this.esEditable;
	}
}
