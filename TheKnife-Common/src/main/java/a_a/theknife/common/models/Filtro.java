package a_a.theknife.common.models;

import java.io.Serializable;

public class Filtro<T> implements Serializable{
    private static final long serialVersionUID = 1L;
    private String tipo;
    private T valore;
    
    public Filtro(String tipo, T valore){
        this.tipo = tipo;
        this.valore = valore;
    }
    
    public String getTipo(){
        return(this.tipo);
    }
    
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    
    public T getValore(){
        return(this.valore);
    }
    
    public void setValore(T valore){
        this.valore = valore;
    }
}
