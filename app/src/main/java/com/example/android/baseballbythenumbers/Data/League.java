package com.example.android.baseballbythenumbers.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class League implements Parcelable
{

    @SerializedName("leagueName")
    @Expose
    private String leagueName;
    @SerializedName("useDh")
    @Expose
    private boolean useDh;
    @SerializedName("divisions")
    @Expose
    private List<Division> divisions = null;
    public final static Parcelable.Creator<League> CREATOR = new Creator<League>() {


        @SuppressWarnings({
                "unchecked"
        })
        public League createFromParcel(Parcel in) {
            return new League(in);
        }

        public League[] newArray(int size) {
            return (new League[size]);
        }

    }
            ;

    protected League(Parcel in) {
        this.leagueName = ((String) in.readValue((String.class.getClassLoader())));
        this.useDh = ((boolean) in.readValue((boolean.class.getClassLoader())));
        in.readList(this.divisions, (com.example.android.baseballbythenumbers.Data.Division.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public League() {
    }

    /**
     *
     * @param leagueName
     * @param divisions
     * @param useDh
     */
    public League(String leagueName, boolean useDh, List<Division> divisions) {
        super();
        this.leagueName = leagueName;
        this.useDh = useDh;
        this.divisions = divisions;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public boolean isUseDh() {
        return useDh;
    }

    public void setUseDh(boolean useDh) {
        this.useDh = useDh;
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<Division> divisions) {
        this.divisions = divisions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("leagueName", leagueName).append("useDh", useDh).append("divisions", divisions).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(leagueName);
        dest.writeValue(useDh);
        dest.writeList(divisions);
    }

    public int describeContents() {
        return 0;
    }

}