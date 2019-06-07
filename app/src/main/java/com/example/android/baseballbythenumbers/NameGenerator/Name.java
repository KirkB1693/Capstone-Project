package com.example.android.baseballbythenumbers.NameGenerator;

import org.jetbrains.annotations.NotNull;

public class Name
{

    private final String firstName;

    private final String lastName;

    private final String middleName;

    Name(final String firstName, final String middleName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public final String getMiddleName() {
        return middleName;
    }

    public final String getFirstName() {
        return firstName;
    }

    public final String getLastName() {
        return lastName;
    }

    @NotNull
    @Override
    public String toString() {
        return this.firstName + " " + this.middleName + " " + this.lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Name name = (Name) o;

        if (firstName != null ? !firstName.equals(name.firstName) : name.firstName != null) {
            return false;
        }

        if (middleName != null ? !middleName.equals(name.middleName) : name.middleName != null) {
            return false;
        }

        return lastName != null ? lastName.equals(name.lastName) : name.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        return result;
    }
}
