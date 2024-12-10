package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Termination {

    private LocalDate terminationDate; // Date de terminaison explicite
    private long numberOfOccurrences;  // Nombre d'occurrences (si défini)
    private ChronoUnit frequency;      // Fréquence de répétition
    private LocalDate start;           // Date de début

    /**
     * Constructeur pour une date de fin explicite.
     */
    public Termination(LocalDate start, ChronoUnit frequency, LocalDate terminationInclusive) {
        if (start == null || frequency == null || terminationInclusive == null) {
            throw new IllegalArgumentException("Start date, frequency, and termination date must not be null");
        }
        this.start = start;
        this.frequency = frequency;
        this.terminationDate = terminationInclusive;
        this.numberOfOccurrences = calculateNumberOfOccurrences(); // Calcul automatique
    }

    /**
     * Constructeur pour un nombre d'occurrences défini.
     */
    public Termination(LocalDate start, ChronoUnit frequency, long numberOfOccurrences) {
        if (start == null || frequency == null || numberOfOccurrences <= 0) {
            throw new IllegalArgumentException("Start date, frequency, and number of occurrences must be valid");
        }
        this.start = start;
        this.frequency = frequency;
        this.numberOfOccurrences = numberOfOccurrences;
        this.terminationDate = calculateTerminationDate(); // Calcul automatique
    }

    /**
     * Retourne la date de fin (inclusivement).
     */
    public LocalDate terminationDateInclusive() {
        return terminationDate;
    }

    /**
     * Retourne le nombre d'occurrences.
     */
    public long numberOfOccurrences() {
        return numberOfOccurrences;
    }

    /**
     * Calcul automatique de la date de fin basée sur les occurrences.
     */
    private LocalDate calculateTerminationDate() {
        if (numberOfOccurrences > 0) {
            return start.plus(numberOfOccurrences - 1, frequency);
        }
        return null;
    }

    /**
     * Calcul automatique du nombre d'occurrences si une date explicite est définie.
     */
    private long calculateNumberOfOccurrences() {
        if (terminationDate != null) {
            return frequency.between(start, terminationDate) + 1;
        }
        return 0;
    }
}
