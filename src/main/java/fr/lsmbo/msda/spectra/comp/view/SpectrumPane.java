/*
 * 
 */
package fr.lsmbo.msda.spectra.comp.view;

import java.awt.Color;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.compomics.util.gui.spectrum.SpectrumPanel;

import fr.lsmbo.msda.spectra.comp.model.Fragment;
import fr.lsmbo.msda.spectra.comp.model.Spectrum;

// TODO: Auto-generated Javadoc
/**
 * Create and displays a spectrum view that enables the plotting of a spectrum
 * with specific annotations .
 * 
 * @author Aromdhani
 *
 */
public class SpectrumPane {

	/** The spectrum to plot. */
	private Spectrum spectrum;

	/** A list of the peaks to plot. */
	private ArrayList<Fragment> fragments;

	/** The panel that can be embedded in the interface. */
	private SpectrumPanel panel;

	/**
	 * *.
	 *
	 * @param spectrumToPlot
	 *            the spectrum to plot
	 * @param isBeauty
	 *            show the notifications
	 */
	public SpectrumPane(Spectrum spectrumToPlot, Boolean isBeauty) {
		drawSpectrum(spectrumToPlot);
		if (isBeauty) {
			this.panel.setAnnotateHighestPeak(true);
			this.panel.setFilenameColor(Color.BLUE);
			this.panel.setDataPointAndLineColor(Color.pink, 500);
			this.panel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
	}

	/**
	 * Draw a spectrum in graphics.
	 * 
	 * @param spectrumToplot
	 *            the spectrum to plot.
	 */
	public void drawSpectrum(Spectrum spectrumToplot) {
		List<Double> mozListTemp = new ArrayList<>();
		List<Double> intListTemp = new ArrayList<>();
		spectrumToplot.getFragments().forEach(frg -> {
			mozListTemp.add(frg.getMz());
			intListTemp.add((double) frg.getIntensity());
		});
		this.panel = new SpectrumPanel(convertToDouble(mozListTemp), convertToDouble(intListTemp),
				spectrumToplot.getM_precursorMoz(), String.valueOf(spectrumToplot.getM_precursorCharge()),
				spectrumToplot.getM_title());
		
	}

	/**
	 * Add a spectrum as a mirror.
	 *
	 * @param spectrumToplot
	 *            the spectrum to add in panel as mirror
	 */
	public void addMirroredSpectrum(Spectrum spectrumToplot) {
		List<Double> mozListTemp = new ArrayList<>();
		List<Double> intensitiesListTemp = new ArrayList<>();
		spectrumToplot.getFragments().forEach(frg -> {
			mozListTemp.add(frg.getMz());
			intensitiesListTemp.add((double) frg.getIntensity());
		});
		this.panel.addMirroredSpectrum(convertToDouble(mozListTemp), convertToDouble(intensitiesListTemp),
				spectrumToplot.getM_precursorMoz(), String.valueOf(spectrumToplot.getM_precursorCharge()),
				spectrumToplot.getM_title(), false, Color.BLUE, Color.RED);

	}

	/**
	 * Convert a list of objects to an array[double].
	 *
	 * @param List
	 *            the list
	 * @return the double[]
	 */
	private double[] convertToDouble(List<Double> List) {
		double[] ret = new double[List.size()];
		Iterator<Double> iterator = List.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next().intValue();
		}
		return ret;
	}

	/**
	 * Gets the spectrum.
	 *
	 * @return the spectrum
	 */
	public final Spectrum getSpectrum() {
		return spectrum;
	}

	/**
	 * Sets the spectrum.
	 *
	 * @param spectrum
	 *            the spectrum to set
	 */
	public final void setSpectrum(Spectrum spectrum) {
		this.spectrum = spectrum;
	}

	/**
	 * Gets the fragments.
	 *
	 * @return the fragments
	 */
	public final ArrayList<Fragment> getFragments() {
		return fragments;
	}

	/**
	 * Sets the fragments.
	 *
	 * @param fragments
	 *            the fragments to set
	 */
	public final void setFragments(ArrayList<Fragment> fragments) {
		this.fragments = fragments;
	}

	/**
	 * Gets the panel.
	 *
	 * @return the panel
	 */
	public final SpectrumPanel getPanel() {
		return panel;
	}

	/**
	 * Sets the panel.
	 *
	 * @param panel
	 *            the panel to set
	 */
	public final void setPanel(SpectrumPanel panel) {
		this.panel = panel;
	}

}
