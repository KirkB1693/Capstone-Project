package com.example.android.baseballbythenumbers.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;


@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(tableName = "teams", foreignKeys = @ForeignKey(entity = Division.class, parentColumns = "divisionId", childColumns = "divisionId", onDelete = CASCADE), indices = @Index(value = "divisionId"))
public class Team implements Parcelable {
    @PrimaryKey
    @NonNull
    private String teamId;

    private String divisionId;

    @SerializedName("teamName")
    @Expose
    private String teamName;
    @SerializedName("teamCity")
    @Expose
    private String teamCity;
    @SerializedName("useDh")
    @Expose
    private boolean useDh;
    @SerializedName("teamBudget")
    @Expose
    private int teamBudget;
    @SerializedName("wins")
    @Expose
    private int wins;
    @SerializedName("losses")
    @Expose
    private int losses;

    @SerializedName("players")
    @Expose
    @Ignore
    private List<Player> players = null;
    public final static Parcelable.Creator<Team> CREATOR = new Creator<Team>() {


        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        public Team[] newArray(int size) {
            return (new Team[size]);
        }

    };

    @SuppressWarnings("ConstantConditions")
    protected Team(Parcel in) {
        teamId = in.readString();
        divisionId = in.readString();
        teamName = in.readString();
        teamCity = in.readString();
        useDh = in.readByte() != 0x00;
        teamBudget = in.readInt();
        wins = in.readInt();
        losses = in.readInt();
        if (in.readByte() == 0x01) {
            players = new ArrayList<>();
            in.readList(players, Player.class.getClassLoader());
        } else {
            players = null;
        }
    }

    /**
     * No args constructor for use in serialization
     */

    public Team() {
    }

    /**
     * @param teamName the name of the team
     * @param teamBudget the budget for the team
     * @param teamCity the city the team plays in
     * @param players a list of the players on the team
     * @param useDh a flag for whether the team uses a designated hitter for the pitcher when playing at home
     */
    @Ignore
    public Team(String teamName, String teamCity, boolean useDh, int teamBudget, List<Player> players, String divisionId, int wins, int losses) {
        this.teamName = teamName;
        this.teamCity = teamCity;
        this.useDh = useDh;
        this.teamBudget = teamBudget;
        this.players = players;
        this.divisionId = divisionId;
        this.wins = wins;
        this.losses = losses;
        this.teamId = UUID.randomUUID().toString();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamCity() {
        return teamCity;
    }

    public void setTeamCity(String teamCity) {
        this.teamCity = teamCity;
    }

    public boolean isUseDh() {
        return useDh;
    }

    public void setUseDh(boolean useDh) {
        this.useDh = useDh;
    }

    public int getTeamBudget() {
        return teamBudget;
    }

    public void setTeamBudget(int teamBudget) {
        this.teamBudget = teamBudget;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @NotNull
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(@NotNull String teamId) {
        this.teamId = teamId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void incrementWins() {
        wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void incrementLosses() {
        losses++;
    }

    public static Comparator<Team> WonLossRecordComparator = new Comparator<Team>() {
        @Override
        public int compare(Team team1, Team team2) {
            int winLossPctTeam1;
            int winLossPctTeam2;
            if (team1.wins != 0 && team2.wins != 0) {
                winLossPctTeam1 = (int) (team1.wins * 100000.0 / (team1.wins + team1.losses));
                winLossPctTeam2 = (int) (team2.wins * 100000.0 / (team2.wins + team2.losses));
            } else {
                winLossPctTeam1 = team1.wins;
                winLossPctTeam2 = team2.wins;
            }

            if (winLossPctTeam1 == winLossPctTeam2) {  // if the percentages are equal put them in alphabetical order
                if ((team1.getTeamName()).compareToIgnoreCase((team2.getTeamName())) > 0) {
                    winLossPctTeam1 = 1;
                    winLossPctTeam2 = 0;
                } else {
                    winLossPctTeam1 = 0;
                    winLossPctTeam2 = 1;
                }
            }
            //descending order so winLossPctTeam2 - winLossPctTeam1 --> Gives output in order from best to worst W-L pct number
            return winLossPctTeam2 - winLossPctTeam1;
        }
    };

    @NotNull
    @Override
    public String toString() {
        return "Team Name : " + teamName + "\nTeam City : " + teamCity + "\nTeam Budget : " + teamBudget + "\nPlayers :\n" + players.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(teamId);
        dest.writeString(divisionId);
        dest.writeString(teamName);
        dest.writeString(teamCity);
        dest.writeByte((byte) (useDh ? 0x01 : 0x00));
        dest.writeInt(teamBudget);
        dest.writeInt(wins);
        dest.writeInt(losses);
        if (players == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(players);
        }
    }

    public int describeContents() {
        return 0;
    }

}