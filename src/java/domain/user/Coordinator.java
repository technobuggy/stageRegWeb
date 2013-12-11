/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain.user;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author pieter
 */
@Entity
@NamedQuery(
            name = "Coordinator.findAll",
            query = "SELECT c FROM Coordinator c")
public class Coordinator extends User{
    
}
