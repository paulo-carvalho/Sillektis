package android.lehman.sillektis;

/**
 * Class from Sillektis at android.lehman.sillektis
 * Created by Paulo-Lehman on 5/3/2015.
 */
public class Debtor {
    private String name;
    private String email;
    private String phone;
    private Double amount;

    public Debtor() {
        name = "";
        email = "";
        phone = "";
        amount = 0.0;
    }

    public Debtor(String name, String email, String phone, Double amount) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
