package ro.contezi.shopping.list.action.item;

import edu.emory.mathcs.backport.java.util.Collections;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.MILLIS;

class ItemWithAddDates {
    private final String name;
    private final long millisSinceLastAdded;
    private final double averageMillisBetweenBuys;

    ItemWithAddDates(String name, Collection<ZonedDateTime> buyDates) {
        List<ZonedDateTime> sorted = new ArrayList<>(buyDates);
        Collections.sort(sorted);
        millisSinceLastAdded =  MILLIS.between(sorted.get(sorted.size() - 1), ZonedDateTime.now());
        averageMillisBetweenBuys = IntStream.range(0, sorted.size() - 1)
                .mapToLong(i -> MILLIS.between(sorted.get(i), sorted.get(i + 1)))
                .average().orElse(Double.POSITIVE_INFINITY);
        this.name = name;
    }

    static Comparator<ItemWithAddDates> comparator() {
        Comparator<ItemWithAddDates> comparator = Comparator.comparingDouble(
                x -> x.millisSinceLastAdded / x.averageMillisBetweenBuys);
        return comparator.reversed();
    }

    String getName() {
        return name;
    }
}
