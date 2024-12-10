package agenda;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class Event {

    /**
     * The myTitle of this event
     */
    private String myTitle;
    
    /**
     * The starting time of the event
     */
    private LocalDateTime myStart;

    /**
     * The durarion of the event 
     */
    private Duration myDuration;

    /**
     * Repetition settings for this event
     */
    private Repetition repetition;


    /**
     * Constructs an event
     *
     * @param title the title of this event
     * @param start the start time of this event
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
                    terminationInclusive,      // La date de terminaison
                    repetition.getFrequency(), // La fréquence de répétition
                    myStart.toLocalDate()      // La date de début
            ));
        }
    }

    public void setTermination(long numberOfOccurrences) {
        if (repetition != null) {
            repetition.setTermination(new Termination(
                    null,                     // Pas de date de fin explicite
                    repetition.getFrequency(), // La fréquence de répétition
                    numberOfOccurrences       // Nombre d'occurrences
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
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    public boolean isInDay(LocalDate aDay) {
        // Vérifier si la date est une exception dans la répétition
        if (repetition != null && repetition.isException(aDay)) {
            return false;
        }

        // Calcul de la date et heure de fin
        LocalDateTime endDateTime = myStart.plus(myDuration);

        // Si l'événement se termine avant le début du jour spécifié, il ne se produit pas ce jour-là
        if (endDateTime.toLocalDate().isBefore(aDay)) {
            return false;
        }

        // Si l'événement commence après la fin du jour spécifié, il ne se produit pas ce jour-là
        if (myStart.toLocalDate().isAfter(aDay)) {
            return false;
        }

        // Sinon, l'événement se produit ce jour-là
        return true;
    }

    /**
     * @return the myTitle
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @return the myStart
     */
    public LocalDateTime getStart() {
        return myStart;
    }


    /**
     * @return the myDuration
     */
    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}
