/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import domain.user.Login;
import domain.user.Student;
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
            name = "StageAanvraag.findAll",
            query = "SELECT sa FROM StageAanvraag sa")
public class StageAanvraag {
    
    @Id @GeneratedValue
    @Min(value =0, message = "een id moet positief zijn") 
    private int id;
    
    @ManyToOne 
    private Student student;
    
    @ManyToOne 
    private StagePlaats stageplaats;
    
    private boolean aanvaard;
    
    @ManyToOne
    private Login owner;

    public StageAanvraag() {
    }
        
    public StageAanvraag(int id, boolean aanvaard) {
        this.id = id;
        this.aanvaard = aanvaard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StagePlaats getStageplaats() {
        return stageplaats;
    }

    public void setStageplaats(StagePlaats stageplaats) {
        this.stageplaats = stageplaats;
    }
    
     public Login getOwner() {
        return owner;
    }

    public void setOwner(Login owner) {
        this.owner = owner;
    }

    public boolean getAanvaard() {
        return aanvaard;
    }

    public void setAanvaard(boolean aanvaard) {
        this.aanvaard = aanvaard;
    }

    @Override
    public String toString() {
        return "StageAanvraag{" + "id=" + id + ", studentId=" + student + ", stageplaatsId=" + stageplaats + ", aanvaard=" + aanvaard + '}';
    }
    
    
    
    
    
}
