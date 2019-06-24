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

import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "pitching_stats", foreignKeys = @ForeignKey(entity = Player.class, parentColumns = "playerId", childColumns = "playerId", onDelete = CASCADE), indices = @Index(value = "playerId", unique = true))
public class PitchingStats implements Parcelable {

    @PrimaryKey
    @NonNull
    private String pitchingStatsId;

    private String playerId;

    @SerializedName("Year")
    @Expose
    private int year;
    @SerializedName("Wins")
    @Expose
    private int wins;
    @SerializedName("Losses")
    @Expose
    private int losses;
    @SerializedName("Games")
    @Expose
    private int games;
    @SerializedName("GamesStarted")
    @Expose
    private int gamesStarted;
    @SerializedName("CompleteGames")
    @Expose
    private int completeGames;
    @SerializedName("ShutOuts")
    @Expose
    private int shutOuts;
    @SerializedName("Saves")
    @Expose
    private int saves;
    @SerializedName("Holds")
    @Expose
    private int holds;
    @SerializedName("BlownSaves")
    @Expose
    private int blownSaves;
    @SerializedName("InningsPitched")
    @Expose
    private float inningsPitched;
    @SerializedName("TotalBattersFaced")
    @Expose
    private int totalBattersFaced;
    @SerializedName("Hits")
    @Expose
    private int hits;
    @SerializedName("Runs")
    @Expose
    private int runs;
    @SerializedName("EarnedRuns")
    @Expose
    private int earnedRuns;
    @SerializedName("HomeRuns")
    @Expose
    private int homeRuns;
    @SerializedName("Walks")
    @Expose
    private int walks;
    @SerializedName("StrikeOuts")
    @Expose
    private int strikeOuts;
    @SerializedName("HitByPitch")
    @Expose
    private int hitByPitch;
    @SerializedName("WildPitches")
    @Expose
    private int wildPitches;
    @SerializedName("Balks")
    @Expose
    private int balks;
    @SerializedName("GroundBalls")
    @Expose
    private int groundBalls;
    @SerializedName("LineDrives")
    @Expose
    private int lineDrives;
    @SerializedName("FlyBalls")
    @Expose
    private int flyBalls;

    public final static Parcelable.Creator<PitchingStats> CREATOR = new Creator<PitchingStats>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PitchingStats createFromParcel(Parcel in) {
            return new PitchingStats(in);
        }

        public PitchingStats[] newArray(int size) {
            return (new PitchingStats[size]);
        }

    };

    protected PitchingStats(Parcel in) {
        this.pitchingStatsId = ((String) in.readValue((String.class.getClassLoader())));
        this.playerId = ((String) in.readValue((String.class.getClassLoader())));
        this.year = ((int) in.readValue((int.class.getClassLoader())));
        this.wins = ((int) in.readValue((int.class.getClassLoader())));
        this.losses = ((int) in.readValue((int.class.getClassLoader())));
        this.games = ((int) in.readValue((int.class.getClassLoader())));
        this.gamesStarted = ((int) in.readValue((int.class.getClassLoader())));
        this.completeGames = ((int) in.readValue((int.class.getClassLoader())));
        this.shutOuts = ((int) in.readValue((int.class.getClassLoader())));
        this.saves = ((int) in.readValue((int.class.getClassLoader())));
        this.holds = ((int) in.readValue((int.class.getClassLoader())));
        this.blownSaves = ((int) in.readValue((int.class.getClassLoader())));
        this.inningsPitched = ((float) in.readValue((float.class.getClassLoader())));
        this.totalBattersFaced = ((int) in.readValue((int.class.getClassLoader())));
        this.hits = ((int) in.readValue((int.class.getClassLoader())));
        this.runs = ((int) in.readValue((int.class.getClassLoader())));
        this.earnedRuns = ((int) in.readValue((int.class.getClassLoader())));
        this.homeRuns = ((int) in.readValue((int.class.getClassLoader())));
        this.walks = ((int) in.readValue((int.class.getClassLoader())));
        this.strikeOuts = ((int) in.readValue((int.class.getClassLoader())));
        this.hitByPitch = ((int) in.readValue((int.class.getClassLoader())));
        this.wildPitches = ((int) in.readValue((int.class.getClassLoader())));
        this.balks = ((int) in.readValue((int.class.getClassLoader())));
        this.groundBalls = ((int) in.readValue((int.class.getClassLoader())));
        this.lineDrives = ((int) in.readValue((int.class.getClassLoader())));
        this.flyBalls = ((int) in.readValue((int.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public PitchingStats() {
    }

    /**
     * @param blownSaves
     * @param games
     * @param gamesStarted
     * @param shutOuts
     * @param totalBattersFaced
     * @param wildPitches
     * @param completeGames
     * @param lineDrives
     * @param flyBalls
     * @param hits
     * @param walks
     * @param earnedRuns
     * @param balks
     * @param homeRuns
     * @param saves
     * @param strikeOuts
     * @param losses
     * @param year
     * @param hitByPitch
     * @param runs
     * @param holds
     * @param inningsPitched
     * @param groundBalls
     * @param wins
     */
    @Ignore
    public PitchingStats(int year, int wins, int losses, int games, int gamesStarted, int completeGames, int shutOuts, int saves, int holds, int blownSaves, float inningsPitched,
                         int totalBattersFaced, int hits, int runs, int earnedRuns, int homeRuns, int walks, int strikeOuts, int hitByPitch, int wildPitches, int balks, int groundBalls,
                         int lineDrives, int flyBalls, String playerId) {
        super();
        this.year = year;
        this.wins = wins;
        this.losses = losses;
        this.games = games;
        this.gamesStarted = gamesStarted;
        this.completeGames = completeGames;
        this.shutOuts = shutOuts;
        this.saves = saves;
        this.holds = holds;
        this.blownSaves = blownSaves;
        this.inningsPitched = inningsPitched;
        this.totalBattersFaced = totalBattersFaced;
        this.hits = hits;
        this.runs = runs;
        this.earnedRuns = earnedRuns;
        this.homeRuns = homeRuns;
        this.walks = walks;
        this.strikeOuts = strikeOuts;
        this.hitByPitch = hitByPitch;
        this.wildPitches = wildPitches;
        this.balks = balks;
        this.groundBalls = groundBalls;
        this.lineDrives = lineDrives;
        this.flyBalls = flyBalls;
        this.playerId = playerId;
        this.pitchingStatsId = UUID.randomUUID().toString();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void incrementGames() {
        games++;
    }

    public int getGamesStarted() {
        return gamesStarted;
    }

    public void setGamesStarted(int gamesStarted) {
        this.gamesStarted = gamesStarted;
    }

    public void incrementGamesStarted() {
        gamesStarted++;
    }

    public int getCompleteGames() {
        return completeGames;
    }

    public void setCompleteGames(int completeGames) {
        this.completeGames = completeGames;
    }

    public void incrementCompleteGames() {
        completeGames++;
    }

    public int getShutOuts() {
        return shutOuts;
    }

    public void setShutOuts(int shutOuts) {
        this.shutOuts = shutOuts;
    }

    public void incrementShutOuts() {
        shutOuts ++;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public void incrementSaves() {
        saves ++;
    }

    public int getHolds() {
        return holds;
    }

    public void setHolds(int holds) {
        this.holds = holds;
    }

    public void incrementHolds() {
        holds ++;
    }

    public int getBlownSaves() {
        return blownSaves;
    }

    public void setBlownSaves(int blownSaves) {
        this.blownSaves = blownSaves;
    }

    public void incrementBlownSaves() {
        blownSaves ++;
    }

    public float getInningsPitched() {
        return inningsPitched;
    }

    public void setInningsPitched(float inningsPitched) {
        this.inningsPitched = inningsPitched;
    }

    public void addToInningsPitched(int outs) {
        for (int i = 0; i < outs; i++) {
            inningsPitched += .1;
            if (Math.round(inningsPitched * 10) >= (((Math.round(inningsPitched) * 10) + 3))) {
                inningsPitched = inningsPitched + 1.0f - 0.3f;
                inningsPitched = Math.round(inningsPitched * 10) / 10.0f;
            }
        }

    }

    public int getTotalBattersFaced() {
        return totalBattersFaced;
    }

    public void setTotalBattersFaced(int totalBattersFaced) {
        this.totalBattersFaced = totalBattersFaced;
    }

    public void incrementTotalBattersFaced() {
        totalBattersFaced ++;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void incrementHits() {
        hits ++;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getEarnedRuns() {
        return earnedRuns;
    }

    public void setEarnedRuns(int earnedRuns) {
        this.earnedRuns = earnedRuns;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }

    public void incrementHomeRuns() {
        homeRuns ++;
    }

    public int getWalks() {
        return walks;
    }

    public void setWalks(int walks) {
        this.walks = walks;
    }

    public void incrementWalks() {
        walks ++;
    }

    public int getStrikeOuts() {
        return strikeOuts;
    }

    public void setStrikeOuts(int strikeOuts) {
        this.strikeOuts = strikeOuts;
    }

    public void incrementStrikeOuts() {
        strikeOuts ++;
    }

    public int getHitByPitch() {
        return hitByPitch;
    }

    public void setHitByPitch(int hitByPitch) {
        this.hitByPitch = hitByPitch;
    }

    public void incrementHitByPitch() {
        hitByPitch ++;
    }

    public int getWildPitches() {
        return wildPitches;
    }

    public void setWildPitches(int wildPitches) {
        this.wildPitches = wildPitches;
    }

    public void incrementWildPitches() {
        wildPitches ++;
    }

    public int getBalks() {
        return balks;
    }

    public void setBalks(int balks) {
        this.balks = balks;
    }

    public void incrementBalks() {
        balks ++;
    }

    public int getGroundBalls() {
        return groundBalls;
    }

    public void setGroundBalls(int groundBalls) {
        this.groundBalls = groundBalls;
    }

    public void incrementGroundBalls() {
        groundBalls ++;
    }

    public int getLineDrives() {
        return lineDrives;
    }

    public void setLineDrives(int lineDrives) {
        this.lineDrives = lineDrives;
    }

    public void incrementLineDrives() {
        lineDrives ++;
    }

    public int getFlyBalls() {
        return flyBalls;
    }

    public void setFlyBalls(int flyBalls) {
        this.flyBalls = flyBalls;
    }

    public void incrementFlyBalls() {
        flyBalls ++;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPitchingStatsId() {
        return pitchingStatsId;
    }

    public void setPitchingStatsId(String pitchingStatsId) {
        this.pitchingStatsId = pitchingStatsId;
    }

    public double getERA() {
        if (inningsPitched > 0) {
            double ip = ((inningsPitched % 1) * 3.333333333) + Math.round(inningsPitched);
            return Math.round(earnedRuns / (ip / 9) * 100.0) / 100.0;
        } else {
            if (earnedRuns > 0) {
                return 1000000;
            }
            return 0;
        }
    }

    public double getWHIP() {
        if (inningsPitched > 0) {
            double ip = ((inningsPitched % 1) * 3.333333333) + Math.round(inningsPitched);
            return Math.round(((walks + hits) / (ip)) * 100.0) / 100.0;
        } else {
            if ((walks + hits) > 0) {
                return 1000000;
            }
            return 0;
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pitchingStatsId);
        dest.writeValue(playerId);
        dest.writeValue(year);
        dest.writeValue(wins);
        dest.writeValue(losses);
        dest.writeValue(games);
        dest.writeValue(gamesStarted);
        dest.writeValue(completeGames);
        dest.writeValue(shutOuts);
        dest.writeValue(saves);
        dest.writeValue(holds);
        dest.writeValue(blownSaves);
        dest.writeValue(inningsPitched);
        dest.writeValue(totalBattersFaced);
        dest.writeValue(hits);
        dest.writeValue(runs);
        dest.writeValue(earnedRuns);
        dest.writeValue(homeRuns);
        dest.writeValue(walks);
        dest.writeValue(strikeOuts);
        dest.writeValue(hitByPitch);
        dest.writeValue(wildPitches);
        dest.writeValue(balks);
        dest.writeValue(groundBalls);
        dest.writeValue(lineDrives);
        dest.writeValue(flyBalls);
    }

    public int describeContents() {
        return 0;
    }

}
