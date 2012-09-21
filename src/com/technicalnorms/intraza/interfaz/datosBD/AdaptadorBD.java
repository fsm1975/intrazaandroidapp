package com.technicalnorms.intraza.interfaz.datosBD;

import com.technicalnorms.intraza.Configuracion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author JLZS
 * 
 * Clase de acceso a la BD
 *
 */
public class AdaptadorBD 
{
	//Constantes
	private static final String TAG = "AdaptadorBD";
	private static final String NOMBRE_BASEDATOS = "InTraza";
	private static final int VERSION_BASEDATOS = 1;
	
	//Miembros
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public AdaptadorBD(Context contexto) 
	{
		DBHelper = new DatabaseHelper(contexto);
	}

	/**
	 * 
	 * Clase usada para crear o actualizar la BD
	 *
	 */
	public class DatabaseHelper extends SQLiteOpenHelper 
	{
		DatabaseHelper(final Context context) 
		{
			super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			try 
			{
				//Creamos la tabla y los parametros de configuracion
				db.execSQL(TablaConfiguracion.SQL_CREA_TABLA);
				
				//Insertamos los parametros de configuracion
				ContentValues valoresIniciales = new ContentValues();			
				valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, Configuracion.NOMBRE_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, Configuracion.VALOR_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, Configuracion.DESCRIPCION_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, Configuracion.ES_EDITABLE_PARAMETRO_ULTIMA_FECHA_SINCRONIZACION);
				db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales);
				
				valoresIniciales = new ContentValues();			
				valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, Configuracion.NOMBRE_PARAMETRO_TIMEOUT_WEB_SERVICES_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, Configuracion.VALOR_PARAMETRO_TIMEOUT_WEB_SERVICE_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, Configuracion.DESCRIPCION_PARAMETRO_TIMEOUT_WEB_SERVICE_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, Configuracion.ES_EDITABLE_PARAMETRO_TIMEOUT_WEB_SERVICE_SINCRONIZACION);
				db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales);
				
				valoresIniciales = new ContentValues();			
				valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, Configuracion.NOMBRE_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, Configuracion.VALOR_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, Configuracion.DESCRIPCION_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION);
				valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, Configuracion.ES_EDITABLE_PARAMETRO_URI_WEB_SERVICES_SINCRONIZACION);
				db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales);
				
				valoresIniciales = new ContentValues();			
				valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, Configuracion.NOMBRE_PARAMETRO_PERMITIR_PRECIO_0);
				valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, Configuracion.VALOR_PARAMETRO_PERMITIR_PRECIO_0);
				valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, Configuracion.DESCRIPCION_PARAMETRO_PERMITIR_PRECIO_0);
				valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, Configuracion.ES_EDITABLE_PARAMETRO_PERMITIR_PRECIO_0);
				db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales);
				
				valoresIniciales = new ContentValues();			
				valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, Configuracion.NOMBRE_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO);
				valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, Configuracion.VALOR_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO);
				valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, Configuracion.DESCRIPCION_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO);
				valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, Configuracion.ES_EDITABLE_PARAMETRO_NUM_DIAS_ANTIGUEDAD_MARCA_TARIFA_DEFECTO);
				db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales);
				
				valoresIniciales = new ContentValues();			
				valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, Configuracion.NOMBRE_PARAMETRO_USUARIO_WS);
				valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, Configuracion.VALOR_PARAMETRO_USUARIO_WS);
				valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, Configuracion.DESCRIPCION_PARAMETRO_USUARIO_WS);
				valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, Configuracion.ES_EDITABLE_PARAMETRO_USUARIO_WS);
				db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales);
				
				valoresIniciales = new ContentValues();			
				valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, Configuracion.NOMBRE_PARAMETRO_PASSWORD_WS);
				valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, Configuracion.VALOR_PARAMETRO_PASSWORD_WS);
				valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, Configuracion.DESCRIPCION_PARAMETRO_PASSWORD_WS);
				valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, Configuracion.ES_EDITABLE_PARAMETRO_PASSWORD_WS);
				db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales);
				
				valoresIniciales = new ContentValues();			
				valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, Configuracion.NOMBRE_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES);
				valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, Configuracion.VALOR_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES);
				valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, Configuracion.DESCRIPCION_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES);
				valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, Configuracion.ES_EDITABLE_PARAMETRO_PERMITIR_SINCRONIZAR_CON_PEDIDOS_PENDIENTES);
				db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales);

				//Creamos el resto de tablas de la BD
				db.execSQL(TablaArticulo.SQL_CREA_TABLA);
				db.execSQL(TablaCliente.SQL_CREA_TABLA);
				db.execSQL(TablaPrepedido.SQL_CREA_TABLA);
				db.execSQL(TablaPrepedidoItem.SQL_CREA_TABLA);
				db.execSQL(TablaRutero.SQL_CREA_TABLA);
				db.execSQL(TablaObservacion.SQL_CREA_TABLA);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				Log.e(TAG, "Excepcion al crear BD: "+e.getMessage());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			Log.w(TAG, "Actualizando base de datos de versión " + oldVersion + " a "
					+ newVersion + ", lo que destruirá todos los viejos datos");
			
			db.execSQL(TablaObservacion.SQL_CREA_TABLA);
			db.execSQL(TablaRutero.SQL_CREA_TABLA);
			db.execSQL(TablaPrepedidoItem.SQL_CREA_TABLA);
			db.execSQL(TablaPrepedido.SQL_CREA_TABLA);
			db.execSQL(TablaCliente.SQL_CREA_TABLA);
			db.execSQL(TablaArticulo.SQL_CREA_TABLA);
			db.execSQL(TablaConfiguracion.SQL_CREA_TABLA);
			
			onCreate(db);
		}
	}
	
	/**
	 * Abre la BD.
	 * 
	 * @return AdaptadorBD
	 * @throws SQLException
	 */
	public AdaptadorBD abrir() throws SQLException 
	{
		//Abre la BD y si la BD no existe llama al metodo onCreate
		db = DBHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Cierra la BD.
	 * 
	 * @return AdaptadorBD
	 */
	public void cerrar() 
	{
		DBHelper.close();
	}
	
	///////////////////////////////////////////
	//
	//   TABLA CONFIGURACION
	//
	///////////////////////////////////////////

	/**
	 * Inserta un parametro en la tabla de configuracion.
	 * 
	 * @param nombre
	 * @param valor
	 * @param descripcion
	 * @param esEditable
	 * 
	 * @return true si ha ido bien, o false en caso de error
	 */
	public boolean insertarParametroConfiguracion(String nombre, String valor, String descripcion, boolean esEditable) 
	{
		ContentValues valoresIniciales = new ContentValues();
		
		valoresIniciales.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, nombre);
		valoresIniciales.put(TablaConfiguracion.CAMPO_VALOR, valor);
		valoresIniciales.put(TablaConfiguracion.CAMPO_DESCRIPCION, descripcion);
		valoresIniciales.put(TablaConfiguracion.CAMPO_ES_EDITABLE, esEditable);
		
		return db.insert(TablaConfiguracion.NOMBRE_TABLA, null, valoresIniciales) != -1;
	}

	/**
	 * Borra un parametro en concreto de la tabla de configuracion
	 * 
	 * @param nombre del parametro a borrar
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarParametroConfiguracion(String nombre) 
	{
		return db.delete(TablaConfiguracion.NOMBRE_TABLA, TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO + " like '" + nombre + "'", null) > 0;
	}
	
	/**
	 * Recupera todos los parametros de la tabla de configuracion
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosParametrosDeConfiguracion() 
	{
		return db.query(TablaConfiguracion.NOMBRE_TABLA, 
						new String[] { TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, TablaConfiguracion.CAMPO_VALOR, TablaConfiguracion.CAMPO_DESCRIPCION, TablaConfiguracion.CAMPO_ES_EDITABLE}, 
						null, null, null, null, null);
	}

	/**
	 * Recupera un parametro en concreto
	 * 
	 * @param nombre del parametro a recuperar
	 * 
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor obtenerParametroConfiguracion(String nombre) throws SQLException 
	{
		Cursor mCursor = db.query(true, TablaConfiguracion.NOMBRE_TABLA, 
								  new String[] { TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, TablaConfiguracion.CAMPO_VALOR, TablaConfiguracion.CAMPO_DESCRIPCION, TablaConfiguracion.CAMPO_ES_EDITABLE }, 
								  TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO + " like '" + nombre + "'",
								  null, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}

	/**
	 * Actualiza los datos de un parametro en concreto
	 * 
	 * @param nombre
	 * @param valor
	 * @param descripcion
	 * @param esEditable
	 * 
	 * @return true si los datos se han actualizado correctamente, o false en caso contrario
	 */
	public boolean actualizarParametroConfiguracion(String nombre, String valor, String descripcion, boolean esEditable) 
	{
		ContentValues args = new ContentValues();
		
		args.put(TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO, nombre);
		args.put(TablaConfiguracion.CAMPO_VALOR, valor);
		args.put(TablaConfiguracion.CAMPO_DESCRIPCION, descripcion);
		args.put(TablaConfiguracion.CAMPO_ES_EDITABLE, esEditable);
		
		return db.update(TablaConfiguracion.NOMBRE_TABLA, args, TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO + " like '" + nombre + "'", null) > 0;
	}
	
	/**
	 * Actualiza el valor de un parametro en concreto
	 * 
	 * @param nombre
	 * @param valor
	 * 
	 * @return true si los datos se han actualizado correctamente, o false en caso contrario
	 */
	public boolean actualizarValorParametroConfiguracion(String nombre, String valor) 
	{
		ContentValues args = new ContentValues();
		
		args.put(TablaConfiguracion.CAMPO_VALOR, valor);
		
		return db.update(TablaConfiguracion.NOMBRE_TABLA, args, TablaConfiguracion.KEY_CAMPO_NOMBRE_PARAMETRO + " like '" + nombre + "'", null) > 0;
	}
	
	///////////////////////////////////////////
	//
	//   TABLA ARTICULO
	//
	///////////////////////////////////////////

	/**
	 * Inserta un articulo en la tabla de articulos.
	 * 
	 * @param codigo
	 * @param nombre
	 * @param esKg
	 * @param esCongelado
	 * @param tarifaDefecto
	 * @param fechaCambiotarifaDefecto
	 * 
	 * @return true si ha ido bien, o false en caso de error
	 */
	public boolean insertarArticulo(String codigo, String nombre, boolean esKg, boolean esCongelado, float tarifaDefecto, String fechaCambioTarifaDefecto) 
	{
		ContentValues valoresIniciales = new ContentValues();
		
		valoresIniciales.put(TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO, codigo);
		valoresIniciales.put(TablaArticulo.CAMPO_NOMBRE, nombre);
		valoresIniciales.put(TablaArticulo.CAMPO_ES_KG, esKg);
		valoresIniciales.put(TablaArticulo.CAMPO_ES_CONGELADO, esCongelado);
		valoresIniciales.put(TablaArticulo.CAMPO_TARIFA_DEFECTO, tarifaDefecto);
		valoresIniciales.put(TablaArticulo.CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO, fechaCambioTarifaDefecto);
		
		return db.insert(TablaArticulo.NOMBRE_TABLA, null, valoresIniciales) != -1;
	}

	/**
	 * Borra un articulo en concreto de la tabla de articulos
	 * 
	 * @param codigo del articulo a borrar
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarArticulo(String codigo) 
	{
		return db.delete(TablaArticulo.NOMBRE_TABLA, TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codigo + "'", null) > 0;
	}
	
	/**
	 * Borra todos los articulos
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarTodosLosArticulos() 
	{
		return db.delete(TablaArticulo.NOMBRE_TABLA, null, null) > 0;
	}
	
	/**
	 * Recupera todos los articulos de la tabla de articulos que no estan en el rutero del cliente
	 * 
	 * @param idCliente
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerArticulosNoEnRuteroCliente(int idCliente) 
	{
		return db.query(TablaArticulo.NOMBRE_TABLA, 
						new String[] { TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO, TablaArticulo.CAMPO_NOMBRE, TablaArticulo.CAMPO_ES_KG, TablaArticulo.CAMPO_ES_CONGELADO, TablaArticulo.CAMPO_TARIFA_DEFECTO, TablaArticulo.CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO }, 
						TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO + " NOT IN (SELECT "+TablaRutero.KEY_CAMPO_CODIGO_ARTICULO+" FROM "+TablaRutero.NOMBRE_TABLA+" WHERE "+TablaRutero.KEY_CAMPO_ID_CLIENTE + " = " + idCliente+")", 
						null, null, null, TablaArticulo.CAMPO_NOMBRE);
	}

	/**
	 * Recupera todos los articulos de la tabla de articulos
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosArticulos() 
	{
		return db.query(TablaArticulo.NOMBRE_TABLA, 
						new String[] { TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO, TablaArticulo.CAMPO_NOMBRE, TablaArticulo.CAMPO_ES_KG, TablaArticulo.CAMPO_ES_CONGELADO, TablaArticulo.CAMPO_TARIFA_DEFECTO, TablaArticulo.CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO }, 
						null, null, null, null, null);
	}

	/**
	 * Recupera un articulo en concreto
	 * 
	 * @param codigo del articulo a recuperar
	 * 
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor obtenerArticulo(String codigo) throws SQLException 
	{
		Cursor mCursor = db.query(true, TablaArticulo.NOMBRE_TABLA, 
								  new String[] { TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO, TablaArticulo.CAMPO_NOMBRE, TablaArticulo.CAMPO_ES_KG, TablaArticulo.CAMPO_ES_CONGELADO, TablaArticulo.CAMPO_TARIFA_DEFECTO, TablaArticulo.CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO }, 
								  TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codigo + "'",
								  null, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}

	/**
	 * Actualiza los datos de un articulo en concreto
	 * 
	 * @param codigo
	 * @param nombre
	 * @param esKg
	 * @param esCongelado
	 * @param tarifaDefecto
	 * @param fechaCambioTarifaDefecto
	 * 
	 * @return true si los datos se han actualizado correctamente, o false en caso contrario
	 */
	public boolean actualizarArticulo(String codigo, String nombre, boolean esKg, boolean esCongelado, float tarifaDefecto, String fechaCambioTarifaDefecto) 
	{
		ContentValues args = new ContentValues();
		
		args.put(TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO, codigo);
		args.put(TablaArticulo.CAMPO_NOMBRE, nombre);
		args.put(TablaArticulo.CAMPO_ES_KG, esKg);
		args.put(TablaArticulo.CAMPO_ES_CONGELADO, esCongelado);
		args.put(TablaArticulo.CAMPO_TARIFA_DEFECTO, tarifaDefecto);
		args.put(TablaArticulo.CAMPO_FECHA_CAMBIO_TARIFA_DEFECTO, fechaCambioTarifaDefecto);
		
		return db.update(TablaArticulo.NOMBRE_TABLA, args, TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codigo + "'", null) > 0;
	}
	
	///////////////////////////////////////////
	//
	//   TABLA CLIENTE
	//
	///////////////////////////////////////////

	/**
	 * Inserta un cliente en la tabla de clientes.
	 * 
	 * @param id
	 * @param nombre
	 * @param observaciones
	 * 
	 * @return true si ha ido bien, o false en caso de error
	 */
	public boolean insertarCliente(int id, String nombre, String observaciones) 
	{
		ContentValues valoresIniciales = new ContentValues();
		
		valoresIniciales.put(TablaCliente.KEY_CAMPO_ID_CLIENTE, id);
		valoresIniciales.put(TablaCliente.CAMPO_NOMBRE_CLIENTE, nombre);
		valoresIniciales.put(TablaCliente.CAMPO_OBSERVACIONES_PREPEDIDO, observaciones);
		
		return db.insert(TablaCliente.NOMBRE_TABLA, null, valoresIniciales) != -1;
	}

	/**
	 * Borra un cliente en concreto de la tabla de clientes
	 * 
	 * @param id del cliente a borrar
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarCliente(int id) 
	{
		return db.delete(TablaCliente.NOMBRE_TABLA, TablaCliente.KEY_CAMPO_ID_CLIENTE + " = " + id, null) > 0;
	}
	
	/**
	 * Borra todos los clientes de la tabla cliente
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarTodosLosClientes() 
	{
		return db.delete(TablaCliente.NOMBRE_TABLA, null, null) > 0;
	}

	/**
	 * Recupera todos los clientes de la tabla de clientes
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosClientes() 
	{
		return db.query(TablaCliente.NOMBRE_TABLA, 
						new String[] { TablaCliente.KEY_CAMPO_ID_CLIENTE, TablaCliente.CAMPO_NOMBRE_CLIENTE, TablaCliente.CAMPO_OBSERVACIONES_PREPEDIDO }, 
						null, null, null, null, TablaCliente.CAMPO_NOMBRE_CLIENTE);
	}

	/**
	 * Recupera un cliente en concreto
	 * 
	 * @param codigo del cliente a recuperar
	 * 
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor obtenerCliente(int id) throws SQLException 
	{
		Cursor mCursor = db.query(true, TablaCliente.NOMBRE_TABLA, 
								  new String[] { TablaCliente.KEY_CAMPO_ID_CLIENTE, TablaCliente.CAMPO_NOMBRE_CLIENTE, TablaCliente.CAMPO_OBSERVACIONES_PREPEDIDO }, 
								  TablaCliente.KEY_CAMPO_ID_CLIENTE + " = " + id,
								  null, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}

	/**
	 * Actualiza los datos de un cliente en concreto
	 * @param id
	 * @param nombre
	 * @param observaciones
	 * 
	 * @return true si los datos se han actualizado correctamente, o false en caso contrario
	 */
	public boolean actualizarCliente(int id, String nombre, String observaciones) 
	{
		ContentValues args = new ContentValues();
		
		args.put(TablaCliente.KEY_CAMPO_ID_CLIENTE, id);
		args.put(TablaCliente.CAMPO_NOMBRE_CLIENTE, nombre);
		args.put(TablaCliente.CAMPO_OBSERVACIONES_PREPEDIDO, observaciones);
		
		return db.update(TablaCliente.NOMBRE_TABLA, args, TablaCliente.KEY_CAMPO_ID_CLIENTE + " = " + id, null) > 0;
	}
	
	///////////////////////////////////////////
	//
	//   TABLA PREPEDIDO
	//
	///////////////////////////////////////////
	
	/**
	 * Guarda los datos de un prepedido en la BD, para ello primero lo intenta actualizar y si falla hace un insert de los datos.
	 * 
	 * @param idPrepedido
	 * @param idCliente
	 * @param fechaEntrega
	 * @param fechaPrepedido
	 * @param observaciones
	 * @param fijarObservaciones
	 * 
	 * @return true si los datos se han guardado correctamente o false en caso contrario.
	 */
	public boolean guardaPrepedido(int idPrepedido, int idCliente, String fechaEntrega, String fechaPrepedido, String observaciones, boolean fijarObservaciones)
	{
		boolean resultado = actualizarPrepedido(idPrepedido, idCliente, fechaEntrega, fechaPrepedido, observaciones, fijarObservaciones);
		
		if (!resultado)
		{
			resultado = insertarPrepedido(idPrepedido, idCliente, fechaEntrega, fechaPrepedido, observaciones, fijarObservaciones);
		}
		
		return resultado;
	}

	/**
	 * Inserta un prepedido.
	 * 
	 * @param id pepedido
	 * @param id cliente
	 * @param fecha entrega
	 * @param fecha prepedid
	 * @param observaciones
	 * @param fijarObservaciones
	 * 
	 * @return true si ha ido bien, o false en caso de error
	 */
	public boolean insertarPrepedido(int idPrepedido, int idCliente, String fechaEntrega, String fechaPrepedido, String observaciones, boolean fijarObservaciones) 
	{
		ContentValues valoresIniciales = new ContentValues();
		
		valoresIniciales.put(TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO, idPrepedido);
		valoresIniciales.put(TablaPrepedido.CAMPO_ID_CLIENTE, idCliente);
		valoresIniciales.put(TablaPrepedido.CAMPO_FECHA_ENTREGA, fechaEntrega);
		valoresIniciales.put(TablaPrepedido.CAMPO_FECHA_PREPEDIDO, fechaPrepedido);
		valoresIniciales.put(TablaPrepedido.CAMPO_OBSERVACIONES, observaciones);
		valoresIniciales.put(TablaPrepedido.CAMPO_FIJAR_OBSERVACIONES, fijarObservaciones);
		
		return db.insert(TablaPrepedido.NOMBRE_TABLA, null, valoresIniciales) != -1;
	}

	/**
	 * Borra un prepedido en concreto
	 * 
	 * @param id del prepedido a borrar
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarPrepedido(int id) 
	{
		return db.delete(TablaPrepedido.NOMBRE_TABLA, TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO + " = " + id, null) > 0;
	}
	
	/**
	 * Borra un prepedido en concreto con sus linea de prepedido
	 * 
	 * @param id del prepedido a borrar
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarPrepedidoConLosPrepedidosItem(int id) 
	{
		boolean borrarPrepedidosItem = false;
		boolean borrarPrepedido = false;
		
		borrarPrepedidosItem = borrarTodosLosPrepedidoItemDelPrepedido(id);
		
		borrarPrepedido = db.delete(TablaPrepedido.NOMBRE_TABLA, TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO + " = " + id, null) > 0;
		
		return borrarPrepedidosItem && borrarPrepedido;
	}

	/**
	 * Recupera todos los prepedidos de la tabla de prepedido
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosPrepedidos() 
	{
		return db.query(TablaPrepedido.NOMBRE_TABLA, 
						new String[] { TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO, TablaPrepedido.CAMPO_ID_CLIENTE, TablaPrepedido.CAMPO_FECHA_ENTREGA,
									   TablaPrepedido.CAMPO_FECHA_PREPEDIDO, TablaPrepedido.CAMPO_OBSERVACIONES,  TablaPrepedido.CAMPO_FIJAR_OBSERVACIONES }, 
						null, null, null, null, null);
	}
	
	/**
	 * Recupera todos los prepedidos de la tabla de prepedido para un cliente
	 * 
	 * id cliente de los prepedidos que se quiere obtener
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosPrepedidosDelCliente(int idCliente) 
	{
		return db.query(TablaPrepedido.NOMBRE_TABLA, 
						new String[] { TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO, TablaPrepedido.CAMPO_ID_CLIENTE, TablaPrepedido.CAMPO_FECHA_ENTREGA,
									   TablaPrepedido.CAMPO_FECHA_PREPEDIDO, TablaPrepedido.CAMPO_OBSERVACIONES,  TablaPrepedido.CAMPO_FIJAR_OBSERVACIONES }, 
						TablaPrepedido.CAMPO_ID_CLIENTE + " = " + idCliente,
						null, null, null, null, null);
	}	

	/**
	 * Recupera un prepedido en concreto
	 * 
	 * @param codigo del prepedido a recuperar
	 * 
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor obtenerPrepedido(int idPrepedido) throws SQLException 
	{
		Cursor mCursor = db.query(true, TablaPrepedido.NOMBRE_TABLA, 
								  new String[] { TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO, TablaPrepedido.CAMPO_ID_CLIENTE, TablaPrepedido.CAMPO_FECHA_ENTREGA,
												 TablaPrepedido.CAMPO_FECHA_PREPEDIDO, TablaPrepedido.CAMPO_OBSERVACIONES,  TablaPrepedido.CAMPO_FIJAR_OBSERVACIONES }, 
				   				  TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO + " = " + idPrepedido,
								  null, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	/**
	 * Recupera todos los prepedidos de la tabla de prepedido y los datos de los clientes asociados
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosPrepedidosConDatosCliente() 
	{
		//La select, devolvera 8 campos 5 de la tabla prepedido + 3 de la tabla cliente
		String select = "SELECT * FROM "+TablaPrepedido.NOMBRE_TABLA+" p INNER JOIN "+TablaCliente.NOMBRE_TABLA+" c ON p."+TablaPrepedido.CAMPO_ID_CLIENTE+"=c."+TablaCliente.KEY_CAMPO_ID_CLIENTE+" ORDER BY p."+TablaPrepedido.CAMPO_FECHA_PREPEDIDO+", p."+TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO;

		return db.rawQuery(select, new String[]{});
	}
	
	/**
	 * Recupera todos los prepedidos de la tabla de prepedido para un cliente y los datos de los clientes asociados
	 * 
	 * id cliente de los prepedidos que se quiere obtener
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosPrepedidosDelClienteConDatosCliente(int idCliente) 
	{
		String select = "SELECT * FROM "+TablaPrepedido.NOMBRE_TABLA+" p INNER JOIN "+TablaCliente.NOMBRE_TABLA+" c ON p."+TablaPrepedido.CAMPO_ID_CLIENTE+"=c."+TablaCliente.KEY_CAMPO_ID_CLIENTE+" WHERE p."+TablaPrepedido.CAMPO_ID_CLIENTE+"=? ORDER BY p."+TablaPrepedido.CAMPO_FECHA_PREPEDIDO+", p."+TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO;
		
		return db.rawQuery(select, new String[]{String.valueOf(idCliente)});
	}	

	/**
	 * Recupera un prepedido en concreto y los datos de los clientes asociados
	 * 
	 * @param codigo del prepedido a recuperar
	 * 
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor obtenerPrepedidoConDatosCliente(int idPrepedido) throws SQLException 
	{
		String select = "SELECT * FROM "+TablaPrepedido.NOMBRE_TABLA+" p INNER JOIN "+TablaCliente.NOMBRE_TABLA+" c ON p."+TablaPrepedido.CAMPO_ID_CLIENTE+"=c."+TablaCliente.KEY_CAMPO_ID_CLIENTE+" WHERE p."+TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO+"=?";
		
		return db.rawQuery(select, new String[]{String.valueOf(idPrepedido)});
	}

	/**
	 * Actualiza los datos de un prepedido en concreto
	 * @param id prepedido
	 * @param id cliente
	 * @param fecha entrega
	 * @param fecha prepedido
	 * @param observaciones
	 * @param fijarObservaciones
	 * 
	 * @return true si los datos se han actualizado correctamente, o false en caso contrario
	 */
	public boolean actualizarPrepedido(int idPrepedido, int idCliente, String fechaEntrega, String fechaPrepedido, String observaciones, boolean fijarObservaciones) 
	{
		ContentValues args = new ContentValues();
		
		args.put(TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO, idPrepedido);
		args.put(TablaPrepedido.CAMPO_ID_CLIENTE, idCliente);
		args.put(TablaPrepedido.CAMPO_FECHA_ENTREGA, fechaEntrega);
		args.put(TablaPrepedido.CAMPO_FECHA_PREPEDIDO, fechaPrepedido);
		args.put(TablaPrepedido.CAMPO_OBSERVACIONES, observaciones);
		args.put(TablaPrepedido.CAMPO_FIJAR_OBSERVACIONES, fijarObservaciones);
		
		return db.update(TablaPrepedido.NOMBRE_TABLA, args, TablaPrepedido.KEY_CAMPO_ID_PREPEDIDO + " = " + idPrepedido, null) > 0;
	}
	
	///////////////////////////////////////////
	//
	//   TABLA PREPEDIDO ITEM
	//
	///////////////////////////////////////////
	
	/**
	 * Guardamos en la BD un prepedido Item (linea de pedido), para ello hacemos primero un update y si da un error hacemos un insert.
	 * 
	 * @param idPrepedido
	 * @param codArticulo
	 * @param cantidad
	 * @param precio
	 * @param observaciones
	 * @param fijarPrecio
	 * @param fijarArticulo
	 * @param fijarObservaciones
	 * 
	 * @return true si se ha guardado correctamente y false en caso contrario.
	 */
	public boolean guardaPrepedidoItem(int idPrepedido, String codArticulo,
										float cantidad, float precio, String observaciones, boolean fijarPrecio, boolean fijarArticulo, boolean fijarObservaciones)
	{
		boolean resultado = actualizarPrepedidoItem(idPrepedido, codArticulo, cantidad, precio, observaciones, fijarPrecio, fijarArticulo, fijarObservaciones);
		
		if (!resultado)
		{
			resultado = insertarPrepedidoItem(idPrepedido, codArticulo, cantidad, precio, observaciones, fijarPrecio, fijarArticulo, fijarObservaciones);
		}
		
		return resultado;
	}


	/**
	 * Inserta un prepedido item.
	 * 
	 * @param id prepedido
	 * @param codigo articulo
	 * @param cantidad
	 * @param precio
	 * @param observaciones
	 * @param fijar tarifa
	 * @param fijar articulo
	 * @param fijar observaciones
	 * 
	 * @return true si ha ido bien, o false en caso de error
	 */
	public boolean insertarPrepedidoItem(int idPrepedido, String codArticulo,
										 float cantidad, float precio, String observaciones, boolean fijarPrecio, boolean fijarArticulo, boolean fijarObservaciones) 
	{
		ContentValues valoresIniciales = new ContentValues();
		
		valoresIniciales.put(TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO, idPrepedido);
		valoresIniciales.put(TablaPrepedidoItem.KEY_CAMPO_CODIGO_ARTICULO, codArticulo);
		valoresIniciales.put(TablaPrepedidoItem.CAMPO_CANTIDAD, cantidad);
		valoresIniciales.put(TablaPrepedidoItem.CAMPO_PRECIO, precio);
		valoresIniciales.put(TablaPrepedidoItem.CAMPO_OBSERVACIONES, observaciones);
		valoresIniciales.put(TablaPrepedidoItem.CAMPO_FIJAR_PRECIO, fijarPrecio);
		valoresIniciales.put(TablaPrepedidoItem.CAMPO_FIJAR_ARTICULO, fijarArticulo);
		valoresIniciales.put(TablaPrepedidoItem.CAMPO_FIJAR_OBSERVACIONES, fijarObservaciones);
		
		return db.insert(TablaPrepedidoItem.NOMBRE_TABLA, null, valoresIniciales) != -1;
	}

	/**
	 * Borra un prepedido item en concreto
	 * 
	 * @param id del prepedido a borrar
	 * @param codigo articulo a borrar
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarPrepedidoItem(int idPrepedido, String codArticulo) 
	{
		return db.delete(TablaPrepedidoItem.NOMBRE_TABLA, 
						 TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO + " = " + idPrepedido + " and " + TablaPrepedidoItem.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codArticulo + "'", 
						 null) > 0;
	}
	
	/**
	 * Borra todos los prepedidos item de un prepedido en concreto
	 * 
	 * @param id del prepedido cuyos prepedidos item queremos borrar
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarTodosLosPrepedidoItemDelPrepedido(int idPrepedido) 
	{
		boolean resultado = true;
		
		//Antes de borrar comprobamos si existen lineas de prepedido para el pedido, ya que sino se interpretaria como un error por devolver false
		if (obtenerTodosLosPrepedidosItemDelPrepedido(idPrepedido).getCount()>0)
		{
			resultado =  db.delete(TablaPrepedidoItem.NOMBRE_TABLA, 
					 TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO + " = " + idPrepedido, 
					 null) > 0;
		}
		
		return resultado;		
	}

	/**
	 * Recupera todos los prepedidos item
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosPrepedidosItem() 
	{
		return db.query(TablaPrepedidoItem.NOMBRE_TABLA, 
						new String[] { TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO, TablaPrepedidoItem.KEY_CAMPO_CODIGO_ARTICULO,
									   TablaPrepedidoItem.CAMPO_CANTIDAD, TablaPrepedidoItem.CAMPO_PRECIO,
									   TablaPrepedidoItem.CAMPO_OBSERVACIONES, TablaPrepedidoItem.CAMPO_FIJAR_PRECIO, TablaPrepedidoItem.CAMPO_FIJAR_ARTICULO, TablaPrepedidoItem.CAMPO_FIJAR_OBSERVACIONES }, 
						null, null, null, null, null);
	}
	
	/**
	 * Recupera todos los prepedidos item de un prepedido
	 * 
	 * @param predido cusos prepedido item se desea obtener
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosPrepedidosItemDelPrepedido(int idPrepedido) 
	{
		return db.query(TablaPrepedidoItem.NOMBRE_TABLA, 
						new String[] { TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO, TablaPrepedidoItem.KEY_CAMPO_CODIGO_ARTICULO,
									   TablaPrepedidoItem.CAMPO_CANTIDAD, TablaPrepedidoItem.CAMPO_PRECIO,
									   TablaPrepedidoItem.CAMPO_OBSERVACIONES, TablaPrepedidoItem.CAMPO_FIJAR_PRECIO, TablaPrepedidoItem.CAMPO_FIJAR_ARTICULO, TablaPrepedidoItem.CAMPO_FIJAR_OBSERVACIONES }, 
						TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO + " = " + idPrepedido,
						null, null, null, null);
	}

	/**
	 * Recupera un prepedido item en concreto
	 * 
	 * @param id del prepedido a recuperar
	 * @param codigo del articulo a borrar
	 * 
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor obtenerPrepedidoItem(int idPrepedido, String codArticulo) throws SQLException 
	{
		Cursor mCursor = db.query(true, TablaPrepedidoItem.NOMBRE_TABLA, 
								  new String[] { TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO, TablaPrepedidoItem.KEY_CAMPO_CODIGO_ARTICULO,
												 TablaPrepedidoItem.CAMPO_CANTIDAD, TablaPrepedidoItem.CAMPO_PRECIO,
												 TablaPrepedidoItem.CAMPO_OBSERVACIONES, TablaPrepedidoItem.CAMPO_FIJAR_PRECIO, TablaPrepedidoItem.CAMPO_FIJAR_ARTICULO, TablaPrepedidoItem.CAMPO_FIJAR_OBSERVACIONES }, 
												 TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO + " = " + idPrepedido + " and " + TablaPrepedidoItem.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codArticulo + "'",
								  null, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}

	/**
	 * Actualiza los datos de un prepedido item en concreto
	 * 
	 * @param id prepedido
	 * @param codigo articulo
	 * @param cantidad
	 * @param precio
	 * @param observaciones
	 * @param fijar tarifa
	 * @param fijar articulo
	 * @param fijar observaciones
	 * 
	 * @return true si los datos se han actualizado correctamente, o false en caso contrario
	 */
	public boolean actualizarPrepedidoItem(int idPrepedido, String codArticulo,
										   float cantidad, float precio, String observaciones, boolean fijarPrecio, boolean fijarArticulo, boolean fijarObservaciones) 
	{
		ContentValues args = new ContentValues();
		
		args.put(TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO, idPrepedido);
		args.put(TablaPrepedidoItem.KEY_CAMPO_CODIGO_ARTICULO, codArticulo);
		args.put(TablaPrepedidoItem.CAMPO_CANTIDAD, cantidad);
		args.put(TablaPrepedidoItem.CAMPO_PRECIO, precio);
		args.put(TablaPrepedidoItem.CAMPO_OBSERVACIONES, observaciones);
		args.put(TablaPrepedidoItem.CAMPO_FIJAR_PRECIO, fijarPrecio);
		args.put(TablaPrepedidoItem.CAMPO_FIJAR_ARTICULO, fijarArticulo);
		args.put(TablaPrepedidoItem.CAMPO_FIJAR_OBSERVACIONES, fijarObservaciones);
		
		return db.update(TablaPrepedidoItem.NOMBRE_TABLA, args, 
						 TablaPrepedidoItem.KEY_CAMPO_ID_PREPEDIDO + " = " + idPrepedido + " and " + TablaPrepedidoItem.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codArticulo + "'",
						 null) > 0;
	}
	
	///////////////////////////////////////////
	//
	//   TABLA RUTERO
	//
	///////////////////////////////////////////

	/**
	 * Inserta un rutero.
	 * 
	 * @param codigo articulo
	 * @param id cliente
	 * @param fecha ultima compra
	 * @param cantidad ultima compra
	 * @param cantidad total_anio
	 * @param tarifa ultima compra
	 * @param tarifa cliente
	 * @param observaciones
	 * 
	 * @return true si ha ido bien, o false en caso de error
	 */
	public boolean insertarRutero(String codArticulo, int idCliente,
								  String ultimaFecha, double ultimaCantidad, double cantidadTotalAnio, double ultimaTarifa,
								  double tarifaCliente, String observaciones) 
	{
		ContentValues valoresIniciales = new ContentValues();
		
		valoresIniciales.put(TablaRutero.KEY_CAMPO_CODIGO_ARTICULO, codArticulo);
		valoresIniciales.put(TablaRutero.KEY_CAMPO_ID_CLIENTE, idCliente);
		valoresIniciales.put(TablaRutero.CAMPO_FECHA_ULTIMA_COMPRA, ultimaFecha);
		valoresIniciales.put(TablaRutero.CAMPO_CANTIDAD_ULTIMA_COMPRA, ultimaCantidad);
		valoresIniciales.put(TablaRutero.CAMPO_CANTIDAD_TOTAL_ANIO, cantidadTotalAnio);
		valoresIniciales.put(TablaRutero.CAMPO_TARIFA_ULTIMA_COMPRA, ultimaTarifa);
		valoresIniciales.put(TablaRutero.CAMPO_TARIFA_CLIENTE, tarifaCliente);
		valoresIniciales.put(TablaRutero.CAMPO_OBSERVACIONES, observaciones);
		
		return db.insert(TablaRutero.NOMBRE_TABLA, null, valoresIniciales) != -1;
	}

	/**
	 * Borra un rutero en concreto
	 * 
	 * @param codigo articulo
	 * @param id cliente
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarRutero(String codArticulo, int idCliente) 
	{
		return db.delete(TablaRutero.NOMBRE_TABLA, TablaRutero.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codArticulo + "' and " + 
												   TablaRutero.KEY_CAMPO_ID_CLIENTE + " = " + idCliente, null) > 0;
	}
	
	/**
	 * Borra todos los ruteros
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarTodosLosRuteros() 
	{
		return db.delete(TablaRutero.NOMBRE_TABLA, null, null) > 0;
	}

	/**
	 * Recupera todos los ruteros
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosRuteros() 
	{
		return db.query(TablaRutero.NOMBRE_TABLA, 
						new String[] { TablaRutero.KEY_CAMPO_CODIGO_ARTICULO, TablaRutero.KEY_CAMPO_ID_CLIENTE,
									   TablaRutero.CAMPO_FECHA_ULTIMA_COMPRA, TablaRutero.CAMPO_CANTIDAD_ULTIMA_COMPRA, TablaRutero.CAMPO_CANTIDAD_TOTAL_ANIO, 
									   TablaRutero.CAMPO_TARIFA_ULTIMA_COMPRA,TablaRutero.CAMPO_TARIFA_CLIENTE, TablaRutero.CAMPO_OBSERVACIONES }, 
						null, null, null, null, null);
	}
	
	/**
	 * Recupera todos los ruteros de un cliente
	 * 
	 * @param idCliente
	 * @return Cursor
	 */
	public Cursor obtenerTodosLosRuterosDelClienteConArticulo(int idCliente) 
	{
		//La select, devolvera 12 campos 10 de la tabla rutero + 2 de la tabla articulo
		String select = "SELECT * FROM "+TablaRutero.NOMBRE_TABLA+" r INNER JOIN "+TablaArticulo.NOMBRE_TABLA+" a ON r."+TablaRutero.KEY_CAMPO_CODIGO_ARTICULO+"=a."+TablaArticulo.KEY_CAMPO_CODIGO_ARTICULO+" WHERE r."+TablaRutero.KEY_CAMPO_ID_CLIENTE+"=?";

		return db.rawQuery(select, new String[]{String.valueOf(idCliente)});
	}

	/**
	 * Recupera un rutero en concreto
	 * 
	 * @param codigo articulo
	 * @param id cliente
	 * 
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor obtenerRutero(String codArticulo, int idCliente) throws SQLException 
	{
		Cursor mCursor = db.query(true, TablaRutero.NOMBRE_TABLA, 
								  new String[] { TablaRutero.KEY_CAMPO_CODIGO_ARTICULO, TablaRutero.KEY_CAMPO_ID_CLIENTE,
												 TablaRutero.CAMPO_FECHA_ULTIMA_COMPRA, TablaRutero.CAMPO_CANTIDAD_ULTIMA_COMPRA,
												 TablaRutero.CAMPO_CANTIDAD_TOTAL_ANIO, TablaRutero.CAMPO_TARIFA_ULTIMA_COMPRA,
												 TablaRutero.CAMPO_TARIFA_CLIENTE, TablaRutero.CAMPO_OBSERVACIONES }, 
								  TablaRutero.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codArticulo + "' and " + TablaRutero.KEY_CAMPO_ID_CLIENTE + " = " + idCliente,
								  null, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}

	/**
	 * Actualiza los datos de un rutero en concreto
	 * 
	 * @param codigo articulo
	 * @param id cliente
	 * @param fecha ultima compra
	 * @param cantidad ultima compra
	 * @param cantidad total_anio
	 * @param tarifa ultima compra
	 * @param tarifa cliente
	 * @param observaciones
	 * 
	 * @return true si los datos se han actualizado correctamente, o false en caso contrario
	 */
	public boolean actualizarRutero(String codArticulo, int idCliente,
									String ultimaFecha, double ultimaCantidad,
									double cantidadTotalAnio, double ultimaTarifa,
									double tarifaCliente, double tarifaDefecto, String observaciones) 
	{
		ContentValues args = new ContentValues();
		
		args.put(TablaRutero.KEY_CAMPO_CODIGO_ARTICULO, codArticulo);
		args.put(TablaRutero.KEY_CAMPO_ID_CLIENTE, idCliente);
		args.put(TablaRutero.CAMPO_FECHA_ULTIMA_COMPRA, ultimaFecha);
		args.put(TablaRutero.CAMPO_CANTIDAD_ULTIMA_COMPRA, ultimaCantidad);
		args.put(TablaRutero.CAMPO_CANTIDAD_TOTAL_ANIO, cantidadTotalAnio);
		args.put(TablaRutero.CAMPO_TARIFA_ULTIMA_COMPRA, ultimaTarifa);
		args.put(TablaRutero.CAMPO_TARIFA_CLIENTE, tarifaCliente);
		args.put(TablaRutero.CAMPO_OBSERVACIONES, observaciones);
		
		return db.update(TablaRutero.NOMBRE_TABLA, args, TablaRutero.KEY_CAMPO_CODIGO_ARTICULO + " like '" + codArticulo + "' and " + 
														 TablaRutero.KEY_CAMPO_ID_CLIENTE + " = " + idCliente, null) > 0;
	}
	
	///////////////////////////////////////////
	//
	//   TABLA OBSERVACION
	//
	///////////////////////////////////////////

	/**
	 * Inserta una observacion en la tabla de observaciones.
	 * 
	 * @param idObservacion
	 * @param tipo
	 * @param descripcion
	 * 
	 * @return true si ha ido bien, o false en caso de error
	 */
	public boolean insertarObservacion(int idObservacion, int tipo, String descripcion) 
	{
		ContentValues valoresIniciales = new ContentValues();
		
		valoresIniciales.put(TablaObservacion.KEY_CAMPO_ID_OBSERVACION, idObservacion);
		valoresIniciales.put(TablaObservacion.CAMPO_TIPO, tipo);
		valoresIniciales.put(TablaObservacion.CAMPO_DESCRIPCION, descripcion);
		
		return db.insert(TablaObservacion.NOMBRE_TABLA, null, valoresIniciales) != -1;
	}

	/**
	 * Borra una observacion en concreto de la tabla de observaciones
	 * 
	 * @param codigo de la observacion a borrar
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarObservacion(int idObservacion) 
	{
		return db.delete(TablaObservacion.NOMBRE_TABLA, TablaObservacion.KEY_CAMPO_ID_OBSERVACION + " = " + idObservacion, null) > 0;
	}
	
	/**
	 * Borra todas las observaciones
	 * 
	 * @return true si ha ido bien o false en caso de error
	 */
	public boolean borrarTodasLasObservaciones() 
	{
		return db.delete(TablaObservacion.NOMBRE_TABLA, null, null) > 0;
	}
	
	/**
	 * Recupera todas las observaciones de la tabla de observaciones que son de tipo prepedido
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodasLasObservacionesPrepedido() 
	{
		return db.query(TablaObservacion.NOMBRE_TABLA, 
						new String[] { TablaObservacion.KEY_CAMPO_ID_OBSERVACION, TablaObservacion.CAMPO_TIPO, TablaObservacion.CAMPO_DESCRIPCION }, 
						TablaObservacion.CAMPO_TIPO + " = "+TablaObservacion.TIPO_PREPEDIDO, 
						null, null, null, TablaObservacion.CAMPO_DESCRIPCION, null);
	}
	
	/**
	 * Recupera todas las observaciones de la tabla de observaciones que son de tipo prepedido item
	 * 
	 * @return Cursor
	 */
	public Cursor obtenerTodasLasObservacionesPrepedidoItem() 
	{
		return db.query(TablaObservacion.NOMBRE_TABLA, 
						new String[] { TablaObservacion.KEY_CAMPO_ID_OBSERVACION, TablaObservacion.CAMPO_TIPO, TablaObservacion.CAMPO_DESCRIPCION }, 
						TablaObservacion.CAMPO_TIPO + " = "+TablaObservacion.TIPO_PREPEDIDO_ITEM, 
						null, null, null, TablaObservacion.CAMPO_DESCRIPCION, null);
	}

	/**
	 * Recupera una observacion en concreto
	 * 
	 * @param codigo de la observacion a recuperar
	 * 
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor obtenerObservacion(int idObservacion) throws SQLException 
	{
		Cursor mCursor = db.query(true, TablaObservacion.NOMBRE_TABLA, 
								  new String[] { TablaObservacion.KEY_CAMPO_ID_OBSERVACION, TablaObservacion.CAMPO_TIPO, TablaObservacion.CAMPO_DESCRIPCION }, 
								  TablaObservacion.KEY_CAMPO_ID_OBSERVACION + " = " + idObservacion,
								  null, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}

	/**
	 * Actualiza los datos de un articulo en concreto
	 * 
	 * @param idObservacion
	 * @param tipo
	 * @param descripcion
	 * 
	 * @return true si los datos se han actualizado correctamente, o false en caso contrario
	 */
	public boolean actualizarObservacion(int idObservacion, int tipo, String descripcion) 
	{
		ContentValues args = new ContentValues();
		
		args.put(TablaObservacion.KEY_CAMPO_ID_OBSERVACION, idObservacion);
		args.put(TablaObservacion.CAMPO_TIPO, tipo);
		args.put(TablaObservacion.CAMPO_DESCRIPCION, descripcion);
		
		return db.update(TablaObservacion.NOMBRE_TABLA, args, TablaObservacion.KEY_CAMPO_ID_OBSERVACION + " = " + idObservacion, null) > 0;
	}
	
	private void log(String text)
	{
		Log.d("AdaptadorBD", text);
	}
}