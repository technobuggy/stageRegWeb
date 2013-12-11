package domain.user;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * Merk op dat we in deze klasse heel wat tabel- en kolomnamen zelf kiezen. Dit
 * was niet altijd nodig, maar het is handig als geheugensteuntje omdat we deze
 * namen nodig hebben bij het configureren van de security realm.
 * 
 * De klasse bevat ook nog geen validatie. Dit mag je zelf toevoegen ;)
 */
@Entity
@Table(name = "TBL_USER") // Want USER is een gereserveerd woord in SQL.
@SecondaryTable(name = "USER_PASSWORD") // Het is een goede gewoonte om paswoorden op te slaan in een aparte tabel.
public class Login
{
    @Id
    private String username;

    /*
     * Het plain text paswoord slaan we niet op in de databank. We houden het
     * wel tijdelijk bij als attribuut zodat we het kunnen valideren.
     */
    @Transient
    private String plainPassword;

    /*
     * Dit geëncrypteerde paswoord slaan we wel op in de databank. We kiezen een
     * kolomnaam en plaatsen het in de aparte tabel. Deze tabel zal ook een
     * kolom USERNAME bevatten als vreemde sleutel naar TBL_USER.
     */
    @Column(name = "PASSWORD", table = "USER_PASSWORD")
    private String ecryptedPassword;

    /*
     * Hier was de annotatie @ElementCollection voldoende geweest. De andere
     * annotaties dienen puur om de tabel- en kolomnamen expliciet te maken.
     */
    @ElementCollection
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USERNAME"))
    @Column(name = "ROLES")
    private final List<String> roles = new ArrayList<>();

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return plainPassword;
    }

    public String getEcryptedPassword()
    {
        return ecryptedPassword;
    }

    public void setPassword(String plainPassword)
    {
        this.plainPassword = plainPassword;
    }

    /*
     * Deze methode zal het plainPassword ecrypteren op basis van SHA-256.
     */
    public void encryptPassword()
    {
        try {
            BigInteger hash = new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(plainPassword.getBytes("UTF-8")));
            ecryptedPassword = hash.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getRoles()
    {
        return Collections.unmodifiableList(roles);
    }

    public void addRole(String role)
    {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public void removeRole(String role)
    {
        roles.remove(role);
    }

    public void clearRoles()
    {
        roles.clear();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.username);
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
        final Login other = (Login) obj;
        return Objects.equals(this.username, other.username);
    }

    @Override
    public String toString()
    {
        return username;
    }
}
