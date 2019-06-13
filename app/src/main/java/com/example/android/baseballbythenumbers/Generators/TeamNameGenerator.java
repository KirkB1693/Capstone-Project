package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;

import com.example.android.baseballbythenumbers.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.TreeMap;

import timber.log.Timber;

public class TeamNameGenerator {
        private final Random random;

        private final NameGeneratorOptions options;

        private TreeMap<Float, String> teamNames;

        private Context context;

        public TeamNameGenerator(Context context) {
            this(new NameGeneratorOptions(), context);
        }

        public TeamNameGenerator(NameGeneratorOptions options, Context context) {
            this.context = context;
            this.options = options;
            if (options.getRandomSeed() == null) {
                this.random = new Random();
            } else {
                this.random = new Random( options.getRandomSeed() );
            }
            try {
                teamNames = loadTeamNames(R.raw.teamnicknames);
            }
            catch (IOException e) {
                Timber.e("IOException while loading names files.  You may not get any results!");
            }
        }

        /**
         * Generate a single name
         *
         * @return a randomly generated team name.
         */
        public String generateTeamName() {
            return (pickName(teamNames));  // Team Name

        }


        /**
         * Pick a name from the specified TreeMap based on a random number.
         *
         * @return the picked name.
         */
        private String pickName(final TreeMap<Float, String> map) {
            assert !map.isEmpty();

            Float key = null;
            while (key == null) {
                try {
                    key = map.ceilingKey(random.nextFloat());
                }
                catch (NullPointerException ignored) {
                    //Do nothing.
                }
            }
            String teamName = map.get(key);
            map.remove(key);                       // Remove the name from the list so it isn't picked again
            return teamName;
        }

        /**
         * Loads all of the names in the given file into a TreeMap keyed by the name's cumulative frequency.
         *
         * @param fileResource the resource path to the text file containing the names to load.
         * @return a TreeMap of all names in the file.
         */
        private TreeMap<Float, String> loadTeamNames(final int fileResource) throws IOException {
            TreeMap<Float, String> names = new TreeMap<>();
            InputStream is = context.getResources().openRawResource(fileResource);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                String[] fields = line.split(",");
                names.put(Float.parseFloat(fields[1]), fields[0]);
                line = reader.readLine();
            }


            return names;
        }
}
