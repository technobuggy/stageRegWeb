/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.user;

import domain.Bedrijf;
import javax.persistence.*;


/**
 *
 * @author pieter
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "Begeleider.findAll",
            query = "SELECT b FROM Begeleider b")
//    ),
//    @NamedQuery(
//            name = "Begeleider.findByBedrijfId",
//            query = "SELECT b FROM Begeleider bg JOIN Bedrijf bd where bd.ID = :dbID"
//    )
})
public class Begeleider extends User {

    //TODO check join strategy
//    @ManyToOne
//    @JoinTable
//    Bedrijf b;
    
    @ManyToOne 
    private Bedrijf bedrijf;

    public Begeleider() {
    }

    public Begeleider(Bedrijf bedrijf, int ID, String voorNaam, String famNaam, String email, String telefoon, String wachtwoord) {
        super(ID, voorNaam, famNaam, email, telefoon, wachtwoord);
        this.bedrijf = bedrijf;
    }
    
    

    public Bedrijf getBedrijf() {
        return bedrijf;
    }

    public void setBedrijf(Bedrijf bedrijf) {
        this.bedrijf = bedrijf;
    }
    
    
}
