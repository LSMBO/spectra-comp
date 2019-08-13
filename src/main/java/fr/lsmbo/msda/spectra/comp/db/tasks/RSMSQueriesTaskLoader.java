package fr.lsmbo.msda.spectra.comp.db.tasks;

import java.util.function.Consumer;

/**
 * Defines result set Ms queries loader.
 * 
 * @author Aromdhani
 *
 */
public class RSMSQueriesTaskLoader implements SpectraCompTask {
	private Long m_projectId;

	@Override
	public void doWork(Consumer<Long> consumer) {
		// TODO Auto-generated method stub
		consumer.accept(m_projectId);
	}
}
