package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedFetcher.BreedNotFoundException {
        String key = breed.toLowerCase();

        // Check cache first
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // Always count that we're making a call to the underlying fetcher
        callsMade++;

        try {
            List<String> subBreeds = fetcher.getSubBreeds(key);
            cache.put(key, subBreeds);
            return subBreeds;
        } catch (BreedFetcher.BreedNotFoundException e) {
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
