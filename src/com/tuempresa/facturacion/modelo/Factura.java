package com.tuempresa.facturacion.modelo;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;
@Entity @Getter @Setter
public class Factura extends DocumentoComercial{
    @OneToMany(mappedBy = "factura")
	Collection<Pedido>pedidos;
    
    @ManyToOne
    @ReferenceView("SinClienteNiPedidos")
    Factura factura;
}
