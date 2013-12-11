/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.user;

import domain.Bedrijf;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author pieter
 */
@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class User {
    @Id @GeneratedValue
    @Min(value =0, message = "een id moet positief zijn") 
    private int ID;
        
    @Size(min = 1, max = 50, message = "de voornaam moet tussen 1 en 50 karakters lang zijn")
    private String voorNaam;
    @Size(min = 1, max = 50, message = "de naam moet tussen 1 en 50 karakters lang zijn")
    private String famNaam;
    @Email(message = "geen correct email formaat")
    private String email;
    private String telefoon;
    private String wachtwoord;
    
    @NotNull(message = "gebruiker niet gekend")
    @ManyToOne
    private Login owner;
    
    
    public User(){
        
    }

    public User(int ID, String voorNaam, String famNaam, String email, String telefoon, String wachtwoord) {
        this.ID = ID;
        this.voorNaam = voorNaam;
        this.famNaam = famNaam;
        this.email = email;
        this.telefoon = telefoon;
        this.wachtwoord = wachtwoord;
    }
    
    

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getVoorNaam() {
        return voorNaam;
    }

    public void setVoorNaam(String voorNaam) {
        this.voorNaam = voorNaam;
    }

    public String getFamNaam() {
        return famNaam;
    }

    public void setFamNaam(String famNaam) {
        this.famNaam = famNaam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoon() {
        return telefoon;
    }

    public void setTelefoon(String telefoon) {
        this.telefoon = telefoon;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
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
        hash = 41 * hash + ID;
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
        final User other = (User) obj;
        if (this.ID != other.ID) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "User{" + "ID=" + ID + ", voorNaam=" + voorNaam + ", famNaam=" + famNaam + ", email=" + email + ", telefoon=" + telefoon + '}';
    }
    
    

}