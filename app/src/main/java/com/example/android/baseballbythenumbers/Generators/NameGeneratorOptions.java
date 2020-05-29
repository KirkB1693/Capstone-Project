package com.example.android.baseballbythenumbers.Generators;

import org.jetbrains.annotations.NotNull;

public class NameGeneratorOptions
{


    private Long randomSeed;


    /**
     * The seed used to initialize the pseudorandom number generator.
     *
     * @return the random seed
     */
    public Long getRandomSeed() {
        return randomSeed;
    }

    /**
     * Set the seed used to initialize the pseudorandom number generator.
     *
     * @param randomSeed A random seed.
     * @see #getRandomSeed()
     */
    public void setRandomSeed(Long randomSeed) {
        this.randomSeed = randomSeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NameGeneratorOptions that = (NameGeneratorOptions) o;

        return randomSeed != null ? randomSeed.equals(that.randomSeed) : that.randomSeed == null;
    }

    @Override
    public int hashCode() {
        int result;
        result = (randomSeed != null ? randomSeed.hashCode() : 0);
        return result;
    }

    @NotNull
    @Override
    public String toString() {
        return "NameGeneratorOptions{" +
                ", randomSeed=" + randomSeed +
                '}';
    }
}