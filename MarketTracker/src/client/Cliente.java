package client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import common.InterfazDeServer;
import common.Usuario;
import common.Mercado;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Cliente {
	private InterfazDeServer server;
	private Usuario sesion_actual = null;
	
	public Cliente() {	}
	public void startCliente() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", 5000);
		server = (InterfazDeServer) registry.lookup("server");
	}
	
	public ArrayList<Usuario> getUsuarios() throws RemoteException{
		ArrayList<Usuario> listado_usuarios = server.getUsuarios();
		
		System.out.println("RUT NOMBRE");
		
		for (int i = 0; i < listado_usuarios.size(); i++) {
			Usuario usuario = listado_usuarios.get(i);
			String rut, nombre;
			
			rut = usuario.getRut();
			nombre = usuario.getNombre();
		
			
			System.out.println(rut + " " + nombre );
		}
		
		return null;
	}
	
	String getDataFromApi() throws RemoteException {
		return server.getDataFromApi();
	}
	
	public ArrayList<Mercado> infoGeneralMercado() throws RemoteException {
		ArrayList<Mercado> mercadosArray = server.infoGeneralMercado();
		if(mercadosArray == null) {
			System.out.println("Hubo un error, no llegó nada de la API");
			return null;
		} else {
			System.out.println("Mercados Disponibles");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.printf("%-15s | %-20s | %-20s | %-20s%n", "ID", "Moneda de Cambio", "Moneda de Pago", "Orden Mínimo Aceptado");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			DecimalFormat decimalFormat = new DecimalFormat("#.################");
			for (Mercado market : mercadosArray){
				String id = market.getId();
				String moneda_cambio = market.getMoneda_cambio();
				String moneda_pago = market.getMoneda_pago();
				double monto_min_orden = market.getMonto_min_orden();
				System.out.printf("%-15s | %-20s | %-20s | %-20s%n", id, moneda_cambio, moneda_pago, decimalFormat.format(monto_min_orden));
			}
		}
		return mercadosArray;
	}

	public void registrarUsuario() throws RemoteException, IOException {
		Scanner lector = new Scanner(System.in);
		//lector.nextLine();
		
		System.out.print("Ingrese nombre de usuario: ");
		String name = lector.nextLine();
		
		System.out.print("\nIngrese rut de usuario: ");
		String rut = lector.nextLine();

		//lector.close();
		
		Usuario newUsuario = new Usuario(rut, name, "Añadido");
		
		if (server.registrarUsuario(newUsuario) == true) {
			System.out.println("El usuario se registró correctamente.");
		}
		else {
			System.out.println("Ocurrió un error, no se pudo registrar el usuario.");
		}
	}

	public Boolean iniciarSesion() throws RemoteException, IOException {
		Scanner lector = new Scanner(System.in);

		System.out.print("Ingrese su rut: ");
		String rut = lector.nextLine();

		//lector.close();

		sesion_actual = server.iniciarSesion(rut);
		
		if (sesion_actual != null){
			System.out.println("Inició sesión con éxito.");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.printf("Bienvenid@ %s \n", sesion_actual.getNombre());
			return true;
		}
		else {
			System.out.println("Ocurrió un error, no se pudo iniciar sesión.");
			return false;
		}
	}

	public void guardar() throws RemoteException{
		server.guardar();
		System.out.println("Nos vemos pronto❣");
	}
	
	public void infoEspecificaMercado() throws RemoteException{
		Scanner lector = new Scanner(System.in);
		System.out.print("Ingrese el ID del mercado: ");
		String id_mercado = lector.nextLine();
		Mercado mercado = server.infoEspecificaMercado(id_mercado);
		//Mercado(String id, String moneda_cambio, String moneda_pago, double ultimo_precio,double min_precio_venta, double max_precio_compra, double volumen,double variacion_precio_24h, double variacion_precio_7d )
		
		if(mercado == null) {
			System.out.println("Hubo un error, no llegó nada de la API");
		} else {
			System.out.println("Mercado Disponible");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.printf("%-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s%n", "ID Mercado", "Moneda de Cambio", "Moneda de Pago", "Último Precio", "Menor Precio Venta", "Mayor Precio Compra", "Volumen", "Variación 24 Horas", "Variación 7 Días");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			//DecimalFormat decimalFormat = new DecimalFormat("#.################");
			
			String market_id = mercado.getId();
			String[] partes = market_id.split("-");
	        String moneda_cambio = partes[0];
	        String moneda_pago = partes[1];
			double last_price = mercado.getUltimo_precio();
			double min_ask = mercado.getMin_precio_venta();
			double max_bid = mercado.getMax_precio_compra();
			double volume = mercado.getVolumen();
			double price_variation_24h = mercado.getVariacion_precio_24h();
			double price_variation_7d = mercado.getVariacion_precio_7d();
			System.out.printf("%-20s | %-20s | %-20s | %-20f | %-20f | %-20f | %-20f | %-20f | %-20f%n", market_id, moneda_cambio, moneda_pago, last_price, min_ask, max_bid, volume, price_variation_24h, price_variation_7d);
		}
	}
}