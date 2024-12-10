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
            return false; // Si la date est une exception, l'événement ne se produit pas ce jour-là.
        }

        // Calculer la prochaine occurrence de l'événement
        LocalDateTime currentDateTime = myStart;

        // Si l'événement ne se répète pas, on ne vérifie que la date de départ
        if (repetition == null) {
            // L'événement peut commencer dans le jour aDay ou finir dans aDay
            LocalDateTime endOfEvent = currentDateTime.plus(myDuration);
            return !currentDateTime.toLocalDate().isAfter(aDay) && !endOfEvent.toLocalDate().isBefore(aDay);
        }

        // Répéter l'événement en fonction de la fréquence
        while (currentDateTime.toLocalDate().isBefore(aDay) || currentDateTime.toLocalDate().isEqual(aDay)) {
            // Si l'événement tombe exactement sur cette date, on vérifie
            if (currentDateTime.toLocalDate().isEqual(aDay)) {
                // Vérifier que l'événement ne se termine avant cette date
                LocalDateTime endOfEvent = currentDateTime.plus(myDuration);
                if (!endOfEvent.toLocalDate().isBefore(aDay)) {
                    return true; // Si l'événement est actif ce jour-là
                }
            }

            // Déplacer à la prochaine occurrence
            switch (repetition.getFrequency()) {
                case DAYS:
                    currentDateTime = currentDateTime.plusDays(1);
                    break;
                case WEEKS:
                    currentDateTime = currentDateTime.plusWeeks(1);
                    break;
                case MONTHS:
                    currentDateTime = currentDateTime.plusMonths(1);
                    break;
            }
        }

        // Si aucune correspondance n'est trouvée, l'événement ne se produit pas
        return false;
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