package com.trilogyed.retailapiservice.domain;

import com.insomnyak.util.regex.EmailRx;

import javax.validation.constraints.*;
import java.util.Objects;

public class Customer {

    @NotNull(message = "Please provide the customerId. " +
            "If you do not have a customerId, please reach out to your Admin Professional " +
            "so they can create one for you.")
    @Min(value = 1)
    private Integer customerId;

    @NotBlank(message = "Please provide your first name. " +
            "This must match the entry in the system. Use /customers/{customerId} for exact values used.")
    @Size(max=50)
    private String firstName;

    @NotBlank(message = "Please provide your last name. " +
            "This must match the entry in the system. Use /customers/{customerId} for exact values used.")
    @Size(max=50)
    private String lastName;

    @NotBlank(message = "Please provide your street. " +
            "This must match the entry in the system. Use /customers/{customerId} for exact values used.")
    @Size(max=50)
    private String street;

    @NotBlank(message = "Please provide your city. " +
            "This must match the entry in the system. Use /customers/{customerId} for exact values used.")
    @Size(max=50)
    private String city;

    @NotBlank(message = "Please provide your zip. " +
            "This must match the entry in the system. Use /customers/{customerId} for exact values used.")
    @Size(max=10)
    private String zip;

    @NotBlank(message = "Please provide your email. " +
            "This must match the entry in the system. Use /customers/{customerId} for exact values used.")
    @Size(max=75)
    @Pattern(regexp = EmailRx.emailStr, message = "Must provide a valid email. i.e. john.doe@random.com")
    private String email;

    @NotBlank(message = "Please provide your phone number. " +
            "This must match the entry in the system. Use /customers/{customerId} for exact values used.")
    @Size(max=20)
    private String phone;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getCustomerId(), customer.getCustomerId()) &&
                Objects.equals(getFirstName(), customer.getFirstName()) &&
                Objects.equals(getLastName(), customer.getLastName()) &&
                Objects.equals(getStreet(), customer.getStreet()) &&
                Objects.equals(getCity(), customer.getCity()) &&
                Objects.equals(getZip(), customer.getZip()) &&
                Objects.equals(getEmail(), customer.getEmail()) &&
                Objects.equals(getPhone(), customer.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getCustomerId(), getFirstName(), getLastName(), getStreet(), getCity(), getZip(), getEmail(),
                        getPhone());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
