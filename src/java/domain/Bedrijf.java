/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import domain.user.Begeleider;
import domain.user.Login;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author pieter
 */
@Entity
@NamedQuery(name = "Bedrijf.findAll", query = "SELECT b FROM Bedrijf b")
public class Bedrijf {
    
    @Id @GeneratedValue
    @Min(value =0, message = "een id moet positief zijn") 
    private int id;
    
    @Size(min = 1, max = 50, message = "de naam moet tussen 1 en 50 karakters lang zijn")
    private String naam;
    
    private String adres;
    
    private String gemeente;
    
    private String land;
    
//    @OneToMany(mappedBy="b"  ,cascade={CascadeType.PERSIST,CascadeType.MERGE})
//    private List<Begeleider> begeleider;
    
    //@NotNull(message = "Gebruiker moet gekend zijn.")
    @ManyToOne
    private Login owner;
    
    public Bedrijf(){
        
    }

    public Bedrijf(int id, String naam, String adres, String gemeente, String land) {
        this.id = id;
        this.naam = naam;
        this.adres = adres;
        this.gemeente = gemeente;
        this.land = land;
//        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public Login getOwner() {
        return owner;
    }

    public void setOwner(Login owner) {
        this.owner = owner;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 41 * hash + (this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bedrijf other = (Bedrijf) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
   
    

    @Override
    public String toString() {
        //return "Bedrijf{" + "id=" + id + ", naam=" + naam + ", adres=" + adres + ", gemeente=" + gemeente + ", land=" + land + ", begeleider=" + begeleider + '}';
        return "Bedrijf{" + "id=" + id + ", naam=" + naam + ", adres=" + adres + ", gemeente=" + gemeente + ", land=" + land + '}';
    }
    
    
    
    
}
