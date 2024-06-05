package client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class RunCliente {
	public static void main(String[] args) throws NotBoundException, IOException {
		Cliente cliente = new Cliente();
		Scanner lector = new Scanner(System.in);
		String opcion;
		cliente.startCliente("Santiago");
		
		System.out.println("Cliente arriba!");
		System.out.println("Bienvenido a Market Tracker");

		while(true) {
			System.out.println("Registrarse (1)");
			System.out.println("Iniciar Sesión (2)");
			System.out.println("Salir (3)");
			System.out.print("Ingrese la opción: ");
			opcion = lector.nextLine();

			if (opcion.equals("1")) {
				try {
					cliente.registrarUsuario();
				}
				catch(Exception e) {
					System.out.println("Problema para conectar con el servidor, cambiando a servidor de respaldo");
					cliente.startCliente("Valparaíso");
				}

			} else if (opcion.equals("2")) {
				try {
					if (cliente.iniciarSesion()) break;
				}
				catch(Exception e) {
					System.out.println("Problema para conectar con el servidor, cambiando a servidor de respaldo");
					cliente.startCliente("Valparaíso");
				}

			} else if (opcion.equals("3")) {
				cliente.salir();
			}
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}

		while(true) {	
			System.out.println("Visualizar información general de mercados (1)");
			System.out.println("Visualizar información específica de mercados (2)");
			System.out.println("Consultar información específica de mercados favoritos (3)");
			System.out.println("Eliminar un mercado favorito (4)");
			System.out.println("Cambiar nombre de usuario (5)");
			System.out.println("Eliminar cuenta (6)");
			System.out.println("Salir (7)");
			System.out.print("Ingrese la opción: ");
			opcion = lector.nextLine();
			
			if (opcion.equals("1")) {
				try {
					cliente.infoGeneralMercado();
				}
				catch(Exception e) {
					System.out.println("Problema para conectar con el servidor, cambiando a servidor de respaldo");
					cliente.startCliente("Valparaíso");
				}
			} else if (opcion.equals("2")) {
				try {
					cliente.infoEspecificaMercado();
				}
				catch(Exception e) {
					System.out.println("Problema para conectar con el servidor, cambiando a servidor de respaldo");
					cliente.startCliente("Valparaíso");
				}
			} else if (opcion.equals("3")) {
				try {
					cliente.consultarMercadosFavoritos();
				}
				catch(Exception e) {
					System.out.println("Problema para conectar con el servidor, cambiando a servidor de respaldo");
					cliente.startCliente("Valparaíso");
				}
			} else if (opcion.equals("4")) {
				try {
					cliente.eliminarMercadoFavorito();
				}
				catch(Exception e) {
					System.out.println("Problema para conectar con el servidor, cambiando a servidor de respaldo");
					cliente.startCliente("Valparaíso");
				}
			} else if (opcion.equals("5")) {
				try {
					cliente.cambiarNombre();
				}
				catch(Exception e) {
					System.out.println("Problema para conectar con el servidor, cambiando a servidor de respaldo");
					cliente.startCliente("Valparaíso");
				}
			} else if (opcion.equals("6")) {
				try {
					cliente.eliminarCuenta();
				}
				catch(Exception e) {
					System.out.println("Problema para conectar con el servidor, cambiando a servidor de respaldo");
					cliente.startCliente("Valparaíso");
				}
			} else if (opcion.equals("7")) {
				cliente.salir();
			}
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}
	}
}
