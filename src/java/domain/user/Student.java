/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.user;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author pieter
 * http://docs.oracle.com/javaee/6/tutorial/doc/bnbqn.html#bnbqo
 */
@Entity
@NamedQuery(name = "Student.findAll",query = "Select s from Student s")
public class Student extends User{
    
    
    String afstudeerRichting;

    public Student() {
    }

    public Student(String afstudeerRichting, int ID, String voorNaam, String famNaam, String email, String telefoon, String wachtwoord) {
        super(ID, voorNaam, famNaam, email, telefoon, wachtwoord);
        this.afstudeerRichting = afstudeerRichting;
    }
    
    

    public String getAfstudeerRichting() {
        return afstudeerRichting;
    }

    public void setAfstudeerRichting(String afstudeerRichting) {
        this.afstudeerRichting = afstudeerRichting;
    }
    
    
}
