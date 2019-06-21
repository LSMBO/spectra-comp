package fr.lsmbo.msda.spectra.comp.view;

import fr.lsmbo.msda.spectra.comp.model.Spectrum;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class SpectrumProperty {
	private ObjectProperty<Spectrum> refSpectrumProperty = new SimpleObjectProperty<Spectrum>();
	private ObjectProperty<Spectrum> testSpectrumProperty = new SimpleObjectProperty<Spectrum>();

	/**
	 * @return the refSpectrum
	 */
	public final Spectrum getRefSpectrum() {
		return refSpectrumProperty.getValue();
	}

	/**
	 * @param refSpectrum
	 *            the refSpectrum to set
	 */
	public final void setRefSpectrum(Spectrum refSpectrum) {
		this.refSpectrumProperty.setValue(refSpectrum);
	}

	/**
	 * @return the refSpectrumProperty
	 */
	public final ObjectProperty<Spectrum> getRefSpectrumProperty() {
		return refSpectrumProperty;
	}

	/**
	 * @param spectrumProperty
	 *            the refSpectrumProperty to set
	 */
	public final void setRefSpectrumProperty(ObjectProperty<Spectrum> refSpectrumProperty) {
		this.refSpectrumProperty = refSpectrumProperty;
	}

	/**
	 * @param testSpectrum
	 *            the testSpectrum to set
	 */
	public final void setTestSpectrum(Spectrum testSpectrum) {
		this.testSpectrumProperty.setValue(testSpectrum);
	}

	/**
	 * @return the testSpectrum
	 */
	public final Spectrum getTestSpectrum() {
		return testSpectrumProperty.getValue();
	}

	/**
	 * @return the testSpectrumProperty
	 */
	public final ObjectProperty<Spectrum> getTestSpectrumProperty() {
		return testSpectrumProperty;
	}

	/**
	 * @param testSpectrumProperty
	 *            the testSpectrumProperty to set
	 */
	public final void setTestSpectrumProperty(ObjectProperty<Spectrum> testSpectrumProperty) {
		this.testSpectrumProperty = testSpectrumProperty;
	}
}
