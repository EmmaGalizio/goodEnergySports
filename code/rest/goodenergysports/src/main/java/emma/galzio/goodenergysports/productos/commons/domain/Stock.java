package emma.galzio.goodenergysports.productos.commons.domain;

import emma.galzio.goodenergysports.utils.exception.DomainException;

import java.time.LocalDate;

public class Stock {

    private Integer stockDisponible;
    private Talle talle;
    private LocalDate fechaBaja;

    public Integer getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public Talle getTalle() {
        return talle;
    }

    public void setTalle(Talle talle) {
        this.talle = talle;
    }

    public boolean validate(){
        if(stockDisponible != null && stockDisponible <0) return false;
        if(talle == null || !talle.validate()) return false;
        return talle.validate();

    }
    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public boolean estaActivo(){
        return fechaBaja == null;
    }

    public boolean esTalleActivo(){
        return talle.estaActivo();
    }
    public boolean tieneStock(){
        return stockDisponible > 0;
    }

    public void registrarBaja(){
        if(fechaBaja != null){
            DomainException domainException = new DomainException("El ítem ya se encuentra inactivo");
            domainException.addCause("BAJA_STOCK", "El ítem de stock ya se encuentra inactivo");
            throw domainException;
        }
        fechaBaja = LocalDate.now();
    }
    public boolean esDeTalle(String sTalle){
        return talle.getTalle().equals(sTalle.trim().toUpperCase());
    }

}
