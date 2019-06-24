package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity (tableName = "leagues", foreignKeys = @ForeignKey(entity = Organization.class, parentColumns = "id", childColumns = "orgId", onDelete = CASCADE), indices = {@Index(value = "orgId")})
public class League implements Parcelable
{
    @PrimaryKey
    @NonNull
    private String leagueId;

    private String orgId;

    @SerializedName("leagueName")
    @Expose
    private String leagueName;
    @SerializedName("useDh")
    @Expose
    private boolean useDh;
    @SerializedName("divisions")
    @Expose
    @Ignore
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

    public League(Parcel in) {
        this.leagueId = ((String) in.readValue((String.class.getClassLoader())));
        this.orgId = ((String) in.readValue((String.class.getClassLoader())));
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
     * @param orgId
     */
    public League(String leagueName, boolean useDh, List<Division> divisions, String orgId) {
        super();
        this.leagueName = leagueName;
        this.useDh = useDh;
        this.divisions = divisions;
        this.orgId = orgId;
        this.leagueId = UUID.randomUUID().toString();
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

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @NotNull
    @Override
    public String toString() {
        return "League Name : " + leagueName + ", Uses DH : " + useDh + "\nDivisions : \n" + divisions;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(leagueId);
        dest.writeValue(orgId);
        dest.writeValue(leagueName);
        dest.writeValue(useDh);
        dest.writeList(divisions);
    }

    public int describeContents() {
        return 0;
    }

}