package com.example.android.baseballbythenumbers.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(tableName = "batting_stats", foreignKeys = @ForeignKey(entity = Player.class, parentColumns = "playerId", childColumns = "playerId", onDelete = CASCADE), indices = @Index(value = "playerId"))
public class BattingStats implements Parcelable, Comparable<BattingStats> {

    @PrimaryKey
    @NonNull
    private String battingStatsId;

    private String playerId;

    private int year;

    private int games;

    private int plateAppearances;

    private int hits;

    private int singles;

    private int doubles;

    private int triples;

    private int homeRuns;

    private int runs;

    private int runsBattedIn;

    private int walks;

    private int strikeOuts;

    private int hitByPitch;

    private int stolenBases;

    private int caughtStealing;

    private int groundBalls;

    private int lineDrives;

    private int flyBalls;

    private int errors;

    /**
     * No args constructor for use in serialization
     */
    public BattingStats() {
    }

    /**
     * @param games is the number of games played
     * @param stolenBases is the number of stolen bases
     * @param singles = number of singles
     * @param runsBattedIn = number of RBI's
     * @param lineDrives = number of line drives hit
     * @param flyBalls = number of fly balls hit
     * @param hits = total number of hits
     * @param errors = number of errors made by player
     * @param walks = number of walks drawn by player
     * @param caughtStealing = number of times caught stealing
     * @param homeRuns = number of home runs hit
     * @param plateAppearances = total number of plate appearances
     * @param triples = number of triples hit
     * @param strikeOuts = number of strike outs
     * @param year = year for these stats
     * @param doubles = number of doubles
     * @param hitByPitch = number of times player was hit by a pitch
     * @param runs = number of runs scored by the player
     * @param groundBalls = number of ground balls hit
     */
    @Ignore
    public BattingStats(int year, int games, int plateAppearances, int hits, int singles, int doubles, int triples, int homeRuns, int runs, int runsBattedIn, int walks, int strikeOuts,
                        int hitByPitch, int stolenBases, int caughtStealing, int groundBalls, int lineDrives, int flyBalls, int errors, String playerId) {
        super();
        this.year = year;
        this.games = games;
        this.plateAppearances = plateAppearances;
        this.hits = hits;
        this.singles = singles;
        this.doubles = doubles;
        this.triples = triples;
        this.homeRuns = homeRuns;
        this.runs = runs;
        this.runsBattedIn = runsBattedIn;
        this.walks = walks;
        this.strikeOuts = strikeOuts;
        this.hitByPitch = hitByPitch;
        this.stolenBases = stolenBases;
        this.caughtStealing = caughtStealing;
        this.groundBalls = groundBalls;
        this.lineDrives = lineDrives;
        this.flyBalls = flyBalls;
        this.errors = errors;
        this.playerId = playerId;
        this.battingStatsId = UUID.randomUUID().toString();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void incrementGames() {
        games ++;
    }

    public int getPlateAppearances() {
        return plateAppearances;
    }

    public void setPlateAppearances(int plateAppearances) {
        this.plateAppearances = plateAppearances;
    }

    public void incrementPlateAppearances() {
        plateAppearances ++;
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

    public int getSingles() {
        return singles;
    }

    public void setSingles(int singles) {
        this.singles = singles;
    }

    public void incrementSingles() {
        singles ++;
    }

    public int getDoubles() {
        return doubles;
    }

    public void setDoubles(int doubles) {
        this.doubles = doubles;
    }

    public void incrementDoubles() {
        doubles ++;
    }

    public int getTriples() {
        return triples;
    }

    public void setTriples(int triples) {
        this.triples = triples;
    }

    public void incrementTriples() {
        triples ++;
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

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public void incrementRuns() {
        runs ++;
    }

    public int getRunsBattedIn() {
        return runsBattedIn;
    }

    public void setRunsBattedIn(int runsBattedIn) {
        this.runsBattedIn = runsBattedIn;
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

    public int getStolenBases() {
        return stolenBases;
    }

    public void setStolenBases(int stolenBases) {
        this.stolenBases = stolenBases;
    }

    public void incrementStolenBases() {
        stolenBases ++;
    }

    public int getCaughtStealing() {
        return caughtStealing;
    }

    public void setCaughtStealing(int caughtStealing) {
        this.caughtStealing = caughtStealing;
    }

    public void incrementCaughtStealing() {
        caughtStealing ++;
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

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public void incrementErrors() {
        errors ++;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @NotNull
    public String getBattingStatsId() {
        return battingStatsId;
    }

    public void setBattingStatsId(@NotNull String battingStatsId) {
        this.battingStatsId = battingStatsId;
    }

    public double getAverage(){
        if ((plateAppearances - walks - hitByPitch) != 0) {
            return ((double) hits / (double) (plateAppearances - walks - hitByPitch));
        } else {
            return 0.0;
        }
    }

    public double getOnBasePct() {
        if (plateAppearances != 0) {
            return ((double) (hits + walks + hitByPitch) / (double) plateAppearances);
        } else {
            return 0.0;
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "\nYear : " + year + ", Games : " + games + ", Plate Appearances : " + plateAppearances +
                "\nHits : " + hits + ", Singles : " + singles + ", Doubles : " + doubles + ", Triples : " + triples + " Home Runs : " + homeRuns +
                "\nRuns : " + runs + ", RBI : " + runsBattedIn + ", BB : " + walks + ", SO : " + strikeOuts + ", HBP : " + hitByPitch +
                ", SB : " + stolenBases + ", CS : " + caughtStealing + "\nGround Balls : " + groundBalls + ", Line Drives : " + lineDrives +
                ", Fly Balls : " + flyBalls + "\nErrors : " + errors + ", Avg : " + getAvg() + ", OBP : " + getOBP();
    }

    private int getOBP() {
        if ((plateAppearances) != 0) {
            return (hits + walks + hitByPitch) * 1000 / (plateAppearances);
        } else {
            return 0;
        }
    }

    private int getAvg() {
        if ((plateAppearances - walks) != 0) {
            return hits * 1000 / (plateAppearances - walks);
        } else {
            return 0;
        }

    }

    @Override
    public int compareTo(BattingStats battingStats) {
        if (year > battingStats.year) {
            return 1;
        } else if (year < battingStats.year) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.battingStatsId);
        dest.writeString(this.playerId);
        dest.writeInt(this.year);
        dest.writeInt(this.games);
        dest.writeInt(this.plateAppearances);
        dest.writeInt(this.hits);
        dest.writeInt(this.singles);
        dest.writeInt(this.doubles);
        dest.writeInt(this.triples);
        dest.writeInt(this.homeRuns);
        dest.writeInt(this.runs);
        dest.writeInt(this.runsBattedIn);
        dest.writeInt(this.walks);
        dest.writeInt(this.strikeOuts);
        dest.writeInt(this.hitByPitch);
        dest.writeInt(this.stolenBases);
        dest.writeInt(this.caughtStealing);
        dest.writeInt(this.groundBalls);
        dest.writeInt(this.lineDrives);
        dest.writeInt(this.flyBalls);
        dest.writeInt(this.errors);
    }

    @SuppressWarnings("ConstantConditions")
    protected BattingStats(Parcel in) {
        this.battingStatsId = in.readString();
        this.playerId = in.readString();
        this.year = in.readInt();
        this.games = in.readInt();
        this.plateAppearances = in.readInt();
        this.hits = in.readInt();
        this.singles = in.readInt();
        this.doubles = in.readInt();
        this.triples = in.readInt();
        this.homeRuns = in.readInt();
        this.runs = in.readInt();
        this.runsBattedIn = in.readInt();
        this.walks = in.readInt();
        this.strikeOuts = in.readInt();
        this.hitByPitch = in.readInt();
        this.stolenBases = in.readInt();
        this.caughtStealing = in.readInt();
        this.groundBalls = in.readInt();
        this.lineDrives = in.readInt();
        this.flyBalls = in.readInt();
        this.errors = in.readInt();
    }

    public static final Creator<BattingStats> CREATOR = new Creator<BattingStats>() {
        @Override
        public BattingStats createFromParcel(Parcel source) {
            return new BattingStats(source);
        }

        @Override
        public BattingStats[] newArray(int size) {
            return new BattingStats[size];
        }
    };
}
