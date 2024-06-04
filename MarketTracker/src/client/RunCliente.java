package client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class RunCliente {
	public static void main(String[] args) throws NotBoundException, IOException {
		Cliente cliente = new Cliente();
		Scanner lector = new Scanner(System.in);
		String opcion;
		cliente.startCliente();
		
		System.out.println("Cliente arriba!");
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("Bienvenido a Market Tracker");

		while(true) {
			System.out.println("Registrarse (1)");
			System.out.println("Iniciar Sesión (2)");
			System.out.println("Salir (3)");
			System.out.print("Ingrese la opción: ");
			opcion = lector.nextLine();

			if (opcion.equals("1")) {
				cliente.registrarUsuario();
			} else if (opcion.equals("2")) {
				if (cliente.iniciarSesion()) break;
			} else if (opcion.equals("3")) {
				cliente.guardar();
				System.exit(0);
			}
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}

		while(true) {	
			System.out.println("Visualizar información general de mercados (1)");
			System.out.println("Visualizar información específica de mercados (2)");
			System.out.println("Salir (3)");
			System.out.print("Ingrese la opción: ");
			opcion = lector.nextLine();
			
			if (opcion.equals("1")) {
				cliente.infoGeneralMercado();
			} else if (opcion.equals("2")) {
				cliente.infoEspecificaMercado();
			} else if (opcion.equals("3")) {
				cliente.guardar();
				System.exit(0);
			}
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}
		//lector.close();
	}
}
