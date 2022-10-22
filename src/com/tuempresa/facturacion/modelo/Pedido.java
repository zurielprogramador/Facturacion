package com.tuempresa.facturacion.modelo;

import java.time.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.util.*;

import lombok.*;

@Entity
@Getter
@Setter
@View(extendsView = "super.DEFAULT", members = "diasEntregaEstimados, entregado," + "factura { factura } ")

@View(name = "SinClienteNiFactura", members = "anyo, numero, fecha;" + "detalles;" + "observaciones")


public class Pedido extends DocumentoComercial {

	@ManyToOne
	@ReferenceView("SinClienteNiPedidos")
	Factura factura;

	@Depends("fecha")
	public int getDiasEntregaEstimados() {
		if (getFecha().getDayOfYear() < 15) {
			return 20 - getFecha().getDayOfYear();
		}
		if (getFecha().getDayOfWeek() == DayOfWeek.SUNDAY)
			return 2;
		if (getFecha().getDayOfWeek() == DayOfWeek.SATURDAY)
			return 3;
		return 1;
	}

	@Column(columnDefinition = "INTEGER DEFAULT 1")
	int diasEntrega;

	@PrePersist @PreUpdate
	private void recalcularDiasEntrega() {
		setDiasEntrega(getDiasEntregaEstimados());
}
	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	boolean entregado;

	
    @AssertTrue(message="pedido_debe_estar_entregado")
    private boolean isEntregadoParaEstarEnFactura() {
    	return factura == null || isEntregado();
	}
	
    @PreRemove
    private void validarPreBorrar() {  
    	if (factura != null) { 
    		throw new javax.validation.ValidationException(
    				XavaResources.getString(
                    "no_puede_borrar_pedido_con_factura"));
    	}
    }
	
}


