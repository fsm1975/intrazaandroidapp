package com.technicalnorms.intraza;

import java.text.DecimalFormat;

/**
 * Clase que define constantes usadas en distintos puntos de la aplicacion
 * 
 * @author JLZS
 *
 */
public class Constantes 
{
	// Indica el orden de los valores al mostrarlos en la tabla de rutero
	public static final int COLUMNA_REFERENCIA_LP = 1;
	public static final int COLUMNA_ARTICULO_LP = 2;
	public static final int COLUMNA_FECHA_ULTIMO_LP = 3;
	public static final int COLUMNA_CANTIDAD_ULTIMO_LP = 4;
	public static final int COLUMNA_TARIFA_ULTIMO_LP = 5;
	public static final int COLUMNA_CANTIDAD_NUEVO_LP = 6;
	public static final int COLUMNA_TARIFA_NUEVO_LP = 7;
	public static final int COLUMNA_PRECIO_TOTAL_LP = 8;
	public static final int COLUMNA_TARIFA_LISTA_LP = 9;
	public static final int COLUMNA_DATOS_LP = 10;
	public static final int COLUMNA_DATOS_INICIALES_LP = 11;
	public static final int COLUMNAS_TOTALES_LP = 11;
	
	// Indica el orden de los valores al mostrarlos en la tabla de pedidos
	public static final int COLUMNA_ID_PEDIDO_P = 1;
	public static final int COLUMNA_CLIENTE_P = 2;
	public static final int COLUMNA_FECHA_PEDIDO_P = 3;
	public static final int COLUMNA_FECHA_ENTREGA_P = 4;
	public static final int COLUMNA_PRECIO_TOTAL_P = 5;
	public static final int COLUMNA_PENDIENTE_ENVIAR_P = 6;
	public static final int COLUMNA_SUPRIMIR_P = 7;
	public static final int COLUMNAS_TOTALES_P = 7;
	
	//Para los spinner
	public static final String SPINNER_NINGUNO = "<ninguno>";
	public static final String SPINNER_TODO = "TODOS";
	
	// Para indicar el formato de los numeros
	public static final DecimalFormat formatearFloat2Decimales = FormatoNumeros.dameFormato2Decimales();
	public static final DecimalFormat formatearFloat3Decimales = FormatoNumeros.dameFormato3Decimales();
	
	public static final String EURO = " €";
	public static final String SEPARADOR_MEDIDA_TARIFA = " / ";
	public static final String SEPARADOR_FECHA = "-";
	public static final String MARCA_FIJAR_TARIFA = "*";
	public static final String MARCA_FIJAR_ARTICULO = "*";
	public static final String MARCA_TARIFA_DEFECTO_CAMBIADA_RECIENTEMENTE = "*";
	public static final String SEPARADOR_CANTIDAD_TOTAL_ANIO = " / ";
	public static final String MARCA_TONELADAS = " T";
	
	// Cadena cuando que se muestra en los datos del nuevo pedido, cuando este no ha sido introducido
	public static final String DATOS_NUEVO_PEDIDO_SIN_INTRODUCIR = "????";
		
	// Indica el texto a mostrar en el boton de comentarios
	public static final String SIN_COMENTARIOS = "  +\n...";
	public static final String CON_COMENTARIOS = "\n...";
	
	// Usada en el dialogo/activity que pide los datos del nuevo pedido
	public static final String KILOGRAMOS = "KG";
	public static final String UNIDADES = "UD";
	
	// Para inicializar los datos de la linea de pedido, cuando no tenemos un pedido anterior
	public static final String SIN_FECHA_ANTERIOR_LINEA_PEDIDO = "---";
	public static final String INFO_SIN_FECHA_ANTERIOR_LINEA_PEDIDO = "No hay datos de pedidos anteriores.";

	// Mensajes a mostar con Toast
	public static final String MENSAJE_OBSERVACIONES_NUEVO_PEDIDO_ACEPTADOS = "¡Observaciones pedido aceptadas!";
	
	// Usada para los botones de pedido
	public static final String PREFIJO_TEXTO_FECHA_PEDIDO = "FECHA PEDIDO: ";
	public static final String PREFIJO_TEXTO_ID_PEDIDO = "ID. PEDIDO: ";
	public static final String PREFIJO_TEXTO_PRECIO_TOTAL = "PRECIO TOTAL: ";
	public static final String PREFIJO_BOTON_FECHA_ENTREGA = "FECHA ENTREGA: ";
	//public static final String PREFIJO_BOTON_CLIENTE = "CLIENTE: ";
	public static final String PREFIJO_BOTON_CLIENTE = "";
	
	//Cuando se crea un articulo que es clon, se le añade esto seguido del numero de clon al codigo del articulo para diferenciarlo del articulo
	//del que ha sido clonado
	public static final String CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO = "_";
	public static final String MARCA_CLON_CODIGO_ARTICULO = CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO+"clon"+CARACTER_OBLIGATORIO_MARCA_CLON_CODIGO_ARTICULO;
	
	// *******************************
	// VENTANA PIDE DATOS LINEA PEDIDO
	// *******************************
	public static final String CADENA_PREFIJO_PRECIO_TOTAL = "Precio: ";
	
	// Mensajes a mostar con Toast
	public static final String MENSAJE_TITULO_DIALOGO_NUEVO_PEDIDO = "Introduzca los datos del nuevo pedido";
	public static final String MENSAJE_TITULO_DIALOGO_MODIFICA_PEDIDO = "Introduzca los datos del pedido";
	public static final String MENSAJE_DATOS_NUEVO_LP_ACEPTADOS = "¡Datos aceptados para el pedido!";
	public static final String MENSAJE_DATOS_NUEVO_LP_ELIMINADOS = "¡Datos eliminados del pedido!";
	public static final String MENSAJE_DATOS_NUEVO_LP_CLONAR_ACEPTADOS = "¡Datos articulo clonado aceptados para el pedido!";
	public static final String MENSAJE_DATOS_INCLUIR_SELECCIONES = "¡Selecciones incluidas en las observaciones!";
	public static final String MENSAJE_DATOS_QUITAR_SELECCIONES = "¡Se han puesto las observaciones por defecto!";
	public static final String AVISO_DATOS_NUEVO_LP_CANTIDAD_0 = "¡LA CANTIDAD NO PUEDE SER 0!";
	public static final String AVISO_DATOS_NUEVO_LP_TARIFA_CLIENTE_0 = "¡LA TARIFA CLIENTE NO PUEDE SER 0!";
	//public static final String MENSAJE_PANTALLA_CARGAR_RUTERO = "Cargando rutero ...";
	//public static final String MENSAJE_PANTALLA_RUTERO_CARGADO = "¡Rutero cargado!";
	//public static final String MENSAJE_PANTALLA_CARGAR_PEDIDO = "Cargando pedido ...";
	//public static final String MENSAJE_PANTALLA_PEDIDO_CARGADO = "¡Pedido cargado!";
	public static final String AVISO_DATOS_DCNP_CLIENTE_NO_VALIDO = "¡EL CLIENTE INDICADO NO ES VALIDO!";
	public static final String AVISO_DATOS_DCNP_FECHA_NO_VALIDA = "¡LA FECHA DE ENTREGA NO ES VALIDA, DEBE SER POSTERIOR A LA FECHA ACTUAL!";
	public static final String MENSAJE_DATOS_PEDIDO_GUARDADOS = "¡Datos del pedido guardados correctamente!";
	public static final String ERROR_GUARDAR_DATOS_PEDIDO = "ERROR: Al guardar los datos del pedido.";
	public static final String ERROR_BORRAR_DATOS_PEDIDO = "ERROR: Al borrar los datos del pedido ";
	public static final String MENSAJE_PEDIDO_SIN_LINEAS_PEDIDO = "¡No hay ninguna linea de pedido!";
	public static final String AVISO_DATOS_DNAR_ARTICULO_NO_VALIDO = "¡EL ARTICULO INDICADO NO ES VALIDO!";
	public static final String AVISO_DATOS_DCP_CLIENTE_NO_VALIDO = "¡EL CLIENTE INDICADO NO ES VALIDO!";
	public static final String AVISO_DATOS_DCP_ID_PEDIDO_NO_VALIDO = "¡EL IDENTIFICADOR DEL PEDIDO NO ES VALIDO!";
	public static final String AVISO_DATOS_DCP_SOLO_UN_DATO = "¡DEBE RELLENAR SOLO UNO DE LOS DATOS SOLICITADOS!";
	public static final String AVISO_DATOS_DCP_NINGUN_DATO = "¡DEBE RELLENAR SOLO UNO DE LOS DATOS SOLICITADOS!";
	//public static final String MENSAJE_PANTALLA_CONSULTANDO_PEDIDOS = "Consultando ...";
	//public static final String MENSAJE_PANTALLA_CONSULTA_PEDIDOS_TERMINADA = "¡Consulta terminada!";
	public static final String MENSAJE_PANTALLA_PEDIDOS_BORRADOS = "¡Se han borrado los pedidos!";
	public static final String MENSAJE_PANTALLA_PEDIDOS_ENVIADOS = "¡Pedidos enviados a InTraza!";
	public static final String MENSAJE_PANTALLA_NINGUN_PEDIDO_SELECCIONADO = "¡Ningún pedido seleccionado!";
	
	//Mensajes para el subdialogo mensaje
	public static final String TITULO_SIN_PEDIDOS_CONSULTA = "CONSULTA SIN PEDIDOS";
	public static final String INFORMACION_SIN_PEDIDOS_CONSULTA = "No se ha obtenido ningún pedido en la consulta.";
	public static final String TITULO_SIN_ARTICULOS_NUEVOS = "SIN ARTICULOS NUEVOS";
	public static final String INFORMACION_SIN_ARTICULOS_NUEVOS = "Todos los articulos existentes, ya se encuentran en el rutero del cliente.";
	
	//Mensajes para AlertDialog en sincronizacion correcta
	public static final String TITULO_ALERT_SINCRONIZACION_CORRECTA = "SINCRONIZACIÓN DE DATOS INTRAZA CORRECTA";
	public static final String MENSAJE_ALERT_SINCRONIZACION_CORRECTA = "Los datos de la tablet están sincronizados con InTraza.";
	
	//Mensajes para AlertDialog en error al sincronizar
	public static final String TITULO_ALERT_ERROR_SINCRONIZAR = "SINCRONIZACIÓN DE DATOS INTRAZA INCORRECTA";
	public static final String MENSAJE_ALERT_ERROR_SINCRONIZAR = "Se ha producido un error al realizar la sincronización.\n\nPor favor, compruebe la conexion con InTraza e intentelo de nuevo y si el error persiste contacte con el administrador.";
	
	//Mensajes para AlertDialog en envio prepedidos correcto
	public static final String TITULO_ALERT_ENVIO_PREPEDIDOS_CORRECTO = "ENVIO DE PEDIDOS A INTRAZA CORRECTO";
	public static final String MENSAJE_ALERT_ENVIO_PREPEDIDOS_CORRECTO = "Los pedidos seleccionados han sido enviados a InTraza.";
	
	//Mensajes para AlertDialog en error al enviar prepedidos
	public static final String TITULO_ALERT_ERROR_ENVIO_PREPEDIDOS = "ENVIO DE PEDIDOS A INTRAZA INCORRECTO";
	public static final String MENSAJE_ALERT_ERROR_ENVIO_PREPEDIDOS = "Se ha producido un error al enviar todos o alguno de los pedidos seleccionados.\n\nPor favor, compruebe la conexion con InTraza e intentelo de nuevo y si el error persiste contacte con el administrador.";
	
	//Mensajes para AlertDialog al no permitir sincronizacion por tener pedidos pendientes de enviar
	public static final String TITULO_ALERT_ERROR_NO_SINCRONIZAR_PEDIDOS_PENDIENTES = "SINCRONIZACION CON INTRAZA NO PERMITIDA";
	public static final String MENSAJE_ALERT_ERROR_NO_SINCRONIZAR_PEDIDOS_PENDIENTES = "Existen pedidos pendientes de enviar a InTraza.\nDebe enviarlos o borrarlos para poder realizar la sincronización.";
}
