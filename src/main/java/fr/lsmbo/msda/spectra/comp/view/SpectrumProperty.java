package fr.lsmbo.msda.spectra.comp.view;

import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class SpectrumProperty.
 */
public final class SpectrumProperty {
	
	/** The ref spectrum property. */
	private ObjectProperty<Spectrum> refSpectrumProperty = new SimpleObjectProperty<Spectrum>();
	
	/** The test spectrum property. */
	private ObjectProperty<Spectrum> testSpectrumProperty = new SimpleObjectProperty<Spectrum>();

	/**
	 * Gets the ref spectrum.
	 *
	 * @return the refSpectrum
	 */
	public final Spectrum getRefSpectrum() {
		return refSpectrumProperty.getValue();
	}

	/**
	 * Sets the ref spectrum.
	 *
	 * @param refSpectrum            the refSpectrum to set
	 */
	public final void setRefSpectrum(Spectrum refSpectrum) {
		this.refSpectrumProperty.setValue(refSpectrum);
	}

	/**
	 * Gets the ref spectrum property.
	 *
	 * @return the refSpectrumProperty
	 */
	public final ObjectProperty<Spectrum> getRefSpectrumProperty() {
		return refSpectrumProperty;
	}

	/**
	 * Sets the ref spectrum property.
	 *
	 * @param refSpectrumProperty the new ref spectrum property
	 */
	public final void setRefSpectrumProperty(ObjectProperty<Spectrum> refSpectrumProperty) {
		this.refSpectrumProperty = refSpectrumProperty;
	}

	/**
	 * Sets the test spectrum.
	 *
	 * @param testSpectrum            the testSpectrum to set
	 */
	public final void setTestSpectrum(Spectrum testSpectrum) {
		this.testSpectrumProperty.setValue(testSpectrum);
	}

	/**
	 * Gets the test spectrum.
	 *
	 * @return the testSpectrum
	 */
	public final Spectrum getTestSpectrum() {
		return testSpectrumProperty.getValue();
	}

	/**
	 * Gets the test spectrum property.
	 *
	 * @return the testSpectrumProperty
	 */
	public final ObjectProperty<Spectrum> getTestSpectrumProperty() {
		return testSpectrumProperty;
	}

	/**
	 * Sets the test spectrum property.
	 *
	 * @param testSpectrumProperty            the testSpectrumProperty to set
	 */
	public final void setTestSpectrumProperty(ObjectProperty<Spectrum> testSpectrumProperty) {
		this.testSpectrumProperty = testSpectrumProperty;
	}
}
