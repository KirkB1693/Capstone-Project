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

import java.util.ArrayList;
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


        public League createFromParcel(Parcel in) {
            return new League(in);
        }

        public League[] newArray(int size) {
            return (new League[size]);
        }

    }
            ;

    protected League(Parcel in) {
        orgId = in.readString();
        leagueName = in.readString();
        useDh = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            divisions = new ArrayList<>();
            in.readList(divisions, Division.class.getClassLoader());
        } else {
            divisions = null;
        }
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

    @NotNull
    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(@NotNull String leagueId) {
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orgId);
        dest.writeString(leagueName);
        dest.writeByte((byte) (useDh ? 0x01 : 0x00));
        if (divisions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(divisions);
        }
    }

    public int describeContents() {
        return 0;
    }

}