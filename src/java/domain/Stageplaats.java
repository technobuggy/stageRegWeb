/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import domain.user.Begeleider;
import domain.user.Login;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Min;

/**
 *
 * @author pieter
 */

@Entity
@NamedQuery(
            name = "StagePlaats.findAll",
            query = "SELECT s FROM StagePlaats s")
public class StagePlaats {
    
    @Id @GeneratedValue
    @Min(value =0, message = "een id moet positief zijn") 
    private int id;
    
    private String omschrijving;
    
    private boolean goedgekeurd;
    
    @Min(value =0, message = "aantal moet positief zijn") 
    private int aantal;
    
    @ManyToOne 
    private Begeleider begeleider;
    
    @ManyToOne
    private Login owner;

    public StagePlaats() {
    }
    
    
    public StagePlaats(int id, String omschrijving, boolean goedgekeurd, int aantal, Begeleider begeleider) {
        this.id = id;
        this.omschrijving = omschrijving;
        this.goedgekeurd = goedgekeurd;
        this.aantal = aantal;
        this.begeleider = begeleider;
    }
        
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public boolean getGoedgekeurd() {
        return goedgekeurd;
    }

    public void setGoedgekeurd(boolean goedgekeurd) {
        this.goedgekeurd = goedgekeurd;
    }

    public int getAantal() {
        return aantal;
    }

    public void setAantal(int aantal) {
        this.aantal = aantal;
    }

    public Begeleider getBegeleider() {
        return begeleider;
    }

    public void setBegeleider(Begeleider begeleider) {
        this.begeleider = begeleider;
    }
    
     public Login getOwner() {
        return owner;
    }

    public void setOwner(Login owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Stageplaats{" + "id=" + id + ", omschrijving=" + omschrijving + ", goedgekeurd=" + goedgekeurd + ", aantal=" + aantal + ", begeleiderId=" + begeleider + '}';
    }
    
    
    
}
