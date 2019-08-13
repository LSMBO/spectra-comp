package fr.lsmbo.msda.spectra.comp.db.tasks;

import java.util.function.Consumer;

/**
 * Defines a Spectra-Comp task
 * 
 * @author Aromdhani
 *
 *
 */
public interface SpectraCompTask {
	public void doWork(Consumer<Long> consumer);
}
