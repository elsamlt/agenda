package agenda;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class Event {

    /**
     * The title of this event.
     */
    private String myTitle;

    /**
     * The starting time of the event.
     */
    private LocalDateTime myStart;

    /**
     * The duration of the event.
     */
    private Duration myDuration;

    /**
     * Repetition settings for this event.
     */
    private Repetition repetition;

    /**
     * Constructs an event.
     *
     * @param title    the title of this event
     * @param start    the start time of this event
     * @param duration the duration of this event
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetition = new Repetition(frequency);
    }

    public void addException(LocalDate date) {
        if (repetition != null) {
            repetition.addException(date);
        }
    }

    public void setTermination(LocalDate terminationInclusive) {
        if (repetition != null) {
            repetition.setTermination(new Termination(
                    myStart.toLocalDate(),     // La date de départ
                    repetition.getFrequency(), // La fréquence
                    terminationInclusive       // La date de fin explicite
            ));
        }
    }

    public void setTermination(long numberOfOccurrences) {
        if (repetition != null) {
            repetition.setTermination(new Termination(
                    myStart.toLocalDate(),     // La date de départ
                    repetition.getFrequency(), // La fréquence
                    numberOfOccurrences        // Nombre d'occurrences
            ));
        }
    }

    public int getNumberOfOccurrences() {
        if (repetition != null && repetition.getTermination() != null) {
            return (int) repetition.getTermination().numberOfOccurrences();
        }
        return 0;
    }

    public LocalDate getTerminationDate() {
        if (repetition != null && repetition.getTermination() != null) {
            return repetition.getTermination().terminationDateInclusive();
        }
        return null;
    }

    /**
     * Tests if an event occurs on a given day.
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    public boolean isInDay(LocalDate aDay) {
        if (repetition != null && repetition.isException(aDay)) {
            return false;
        }

        LocalDateTime currentDateTime = myStart;

        if (repetition == null) {
            LocalDateTime endOfEvent = currentDateTime.plus(myDuration);
            return !currentDateTime.toLocalDate().isAfter(aDay) && !endOfEvent.toLocalDate().isBefore(aDay);
        }

        while (currentDateTime.toLocalDate().isBefore(aDay) || currentDateTime.toLocalDate().isEqual(aDay)) {
            if (currentDateTime.toLocalDate().isEqual(aDay)) {
                LocalDateTime endOfEvent = currentDateTime.plus(myDuration);
                if (!endOfEvent.toLocalDate().isBefore(aDay)) {
                    return true;
                }
            }

            switch (repetition.getFrequency()) {
                case DAYS -> currentDateTime = currentDateTime.plusDays(1);
                case WEEKS -> currentDateTime = currentDateTime.plusWeeks(1);
                case MONTHS -> currentDateTime = currentDateTime.plusMonths(1);
            }
        }
        System.out.println("Termination Date: " + getTerminationDate());
        System.out.println("Checking for day: " + aDay);


        return false;
    }

    /**
     * @return the title of the event.
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @return the start time of the event.
     */
    public LocalDateTime getStart() {
        return myStart;
    }

    /**
     * @return the duration of the event.
     */
    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}
