package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Repetition {
    public ChronoUnit getFrequency() {
        return myFrequency;
    }

    /**
     * Stores the frequency of this repetition, one of :
     * <UL>
     * <LI>ChronoUnit.DAYS for daily repetitions</LI>
     * <LI>ChronoUnit.WEEKS for weekly repetitions</LI>
     * <LI>ChronoUnit.MONTHS for monthly repetitions</LI>
     * </UL>
     */
    private final ChronoUnit myFrequency;
    private final ArrayList<LocalDate> exceptions = new ArrayList<>();
    private Termination termination;

    public Repetition(ChronoUnit myFrequency) {
        this.myFrequency = myFrequency;
    }

    /**
     * Les exceptions à la répétition
     * @param date un date à laquelle l'événement ne doit pas se répéter
     */
    public void addException(LocalDate date) {
        exceptions.add(date);
    }

    /**
     * La terminaison d'une répétition (optionnelle)
     * @param termination la terminaison de la répétition
     */
    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    /**
     * Vérifie si une date est marquée comme exception
     * @param date une date à vérifier
     * @return true si la date est une exception, false sinon
     */
    public boolean isException(LocalDate date) {
        return exceptions.contains(date);
    }

    /**
     * Retourne la terminaison associée à la répétition
     * @return la terminaison de la répétition (ou null si aucune n'est définie)
     */
    public Termination getTermination() {
        return termination;
    }
}
