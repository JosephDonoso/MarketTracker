package common;

import java.io.Serializable;
import java.util.Date;

public class Mercado implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private String id;
    private String rut_cliente;
    private String moneda_cambio;
    private String moneda_pago;
    private String estado; //Ninguno - Actualizado - Borrado - Añadido
	private double monto_min_orden;
    private double ultimo_precio;
    private double min_precio_venta;
    private double max_precio_compra;
    private double volumen;
    private double variacion_precio_24h;
    private double variacion_precio_7d;
    private Date fecha_consulta;

    public Mercado(String id, String moneda_cambio, String moneda_pago, double monto_min_orden) {
        this.setId(id);
        this.setMoneda_cambio(moneda_cambio);
        this.setMoneda_pago(moneda_pago);
        this.setMonto_min_orden(monto_min_orden);
        this.setEstado("Ninguno");
    }

	public Mercado(String id, String moneda_cambio, String moneda_pago, double ultimo_precio, 
					double min_precio_venta, double max_precio_compra, double volumen, 
					double variacion_precio_24h, double variacion_precio_7d ) {
        this.setId(id);
        this.setMoneda_cambio(moneda_cambio);
        this.setMoneda_pago(moneda_pago);
        this.setUltimo_precio(ultimo_precio);
		this.setMin_precio_venta(min_precio_venta);
		this.setMax_precio_compra(max_precio_compra);
		this.setVolumen(volumen);
		this.setVariacion_precio_24h(variacion_precio_24h);
		this.setVariacion_precio_7d(variacion_precio_7d);
        this.setEstado("Ninguno");
    }

	public String getId() {
        return id;
    }

	public void setId(String id) {
		this.id = id;
	}

	public String getRut_cliente() {
		return rut_cliente;
	}


	public void setRut_cliente(String rut_cliente) {
		this.rut_cliente = rut_cliente;
	}


	public String getMoneda_cambio() {
		return moneda_cambio;
	}


	public void setMoneda_cambio(String moneda_cambio) {
		this.moneda_cambio = moneda_cambio;
	}


	public String getMoneda_pago() {
		return moneda_pago;
	}


	public void setMoneda_pago(String moneda_pago) {
		this.moneda_pago = moneda_pago;
	}

    public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public double getMonto_min_orden() {
		return monto_min_orden;
	}


	public void setMonto_min_orden(double monto_min_orden) {
		this.monto_min_orden = monto_min_orden;
	}


	public double getUltimo_precio() {
		return ultimo_precio;
	}


	public void setUltimo_precio(double ultimo_precio) {
		this.ultimo_precio = ultimo_precio;
	}


	public double getMin_precio_venta() {
		return min_precio_venta;
	}


	public void setMin_precio_venta(double min_precio_venta) {
		this.min_precio_venta = min_precio_venta;
	}


	public double getMax_precio_compra() {
		return max_precio_compra;
	}


	public void setMax_precio_compra(double max_precio_compra) {
		this.max_precio_compra = max_precio_compra;
	}


	public double getVolumen() {
		return volumen;
	}


	public void setVolumen(double volumen) {
		this.volumen = volumen;
	}


	public double getVariacion_precio_24h() {
		return variacion_precio_24h;
	}


	public void setVariacion_precio_24h(double variacion_precio_24h) {
		this.variacion_precio_24h = variacion_precio_24h;
	}


	public double getVariacion_precio_7d() {
		return variacion_precio_7d;
	}


	public void setVariacion_precio_7d(double variacion_precio_7d) {
		this.variacion_precio_7d = variacion_precio_7d;
	}


	public Date getFecha_consulta() {
		return fecha_consulta;
	}


	public void setFecha_consulta(Date fecha_consulta) {
		this.fecha_consulta = fecha_consulta;
	}
	
}